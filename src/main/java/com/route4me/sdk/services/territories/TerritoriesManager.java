package com.route4me.sdk.services.territories;

import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.DeleteResponse;
import java.awt.Point;
import java.awt.Polygon;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages territories and avoidance zones through the V4 API ({@code https://api.route4me.com/api.v4/territory.php} and
 * {@code https://api.route4me.com/api.v4/avoidance.php}), and finds the territories that contain a point.
 */
public class TerritoriesManager extends Manager {

    /** The path of the avoidance zones, on {@code api.route4me.com}. */
    public static final String AVOIDANCE_EP = "/api.v4/avoidance.php";
    /** The path of the territories, on {@code api.route4me.com}. */
    public static final String TERRITORY_EP = "/api.v4/territory.php";

    private static final int FACTOR = 1000000;

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as the {@code api_key} query parameter
     */
    public TerritoriesManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Reads the territories with {@link #getTerritories()} and turns each polygon territory into a
     * {@link java.awt.Polygon} of its coordinates (multiplied by 1,000,000 and truncated to integers). Circle and
     * rectangle territories are skipped. If the request fails, the error is logged and the list is empty.
     *
     * @return the polygon of each polygon territory
     */
    public List<TerritoryPolygon> getPolygonsFromTerritories() {
        List<TerritoryPolygon> territoryPolygons = new ArrayList<>();
        try {
            List<Territory> territories = this.getTerritories();
            for (Territory territory : territories) {
                switch (territory.getTerritory().getType()) {
                    case "poly":
                        List<String> coordinates = territory.getTerritory().getData();
                        int[] pointsX = new int[coordinates.size()];
                        int[] pointsY = new int[coordinates.size()];
                        int i = 0;
                        for (String coordinate : coordinates) {
                            String[] coords = coordinate.split(",");
                            pointsX[i] = (int) (Float.parseFloat(coords[0]) * FACTOR);
                            pointsY[i] = (int) (Float.parseFloat(coords[1]) * FACTOR);
                            i++;
                        }
                        Polygon polygon = new Polygon(pointsX, pointsY, coordinates.size());
                        TerritoryPolygon territoryPolygon = new TerritoryPolygon(territory, polygon);
                        territoryPolygons.add(territoryPolygon);
                        break;
                    case "circle":
                        break;
                    case "rect":
                        break;
                    default:
                        // Other territory types have no polygon.
                        break;
                }
            }

        } catch (APIException ex) {
            Logger.getLogger(TerritoriesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return territoryPolygons;
    }

    /**
     * Finds the territories whose polygon contains a point.
     *
     * @param territoryAllocated the point, by its latitude and longitude; {@code null} finds nothing
     * @param territoryPolygons the polygons, from {@link #getPolygonsFromTerritories()}
     * @return the territories that contain the point
     */
    public List<Territory> allocateTerritories(TerritoryAllocated territoryAllocated,
            List<TerritoryPolygon> territoryPolygons) {

        List<Territory> foundTerritories = new ArrayList<>();

        if (territoryAllocated != null) {
            int x = (int) (territoryAllocated.getLatitude() * FACTOR);
            int y = (int) (territoryAllocated.getLongitude() * FACTOR);

            Point point = new Point(x, y);

            for (TerritoryPolygon territoryPolygon : territoryPolygons) {
                if (territoryPolygon.getPolygon() != null && territoryPolygon.getPolygon().contains(point)) {
                    foundTerritories.add(territoryPolygon.getTerritory());
                }
            }
        }
        return foundTerritories;

    }

    /**
     * Creates an avoidance zone: {@code POST https://api.route4me.com/api.v4/avoidance.php}, with the zone as the JSON
     * body.
     *
     * @param territory the zone
     * @return the created zone
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Territory addAvoidanceZone(Territory territory) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(AVOIDANCE_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(territory), Territory.class);
    }

    /**
     * Deletes an avoidance zone: {@code DELETE /api.v4/avoidance.php?territory_id=...}.
     *
     * @param territoryId the zone ID
     * @return whether the zone was deleted; {@code false} also when the answer is empty
     * @throws APIException if the request fails
     */
    public boolean deleteAvoidanceZone(String territoryId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(AVOIDANCE_EP);
        builder.setParameter("territory_id", territoryId);
        DeleteResponse response = this.makeRequest(RequestMethod.DELETE, builder, "", DeleteResponse.class);
        return response != null && response.isDeleted();
    }

    /**
     * Updates an avoidance zone: {@code PUT /api.v4/avoidance.php?territory_id=...} with the
     * zone as the JSON body.
     *
     * @param territory the zone, with its {@code territory_id} set
     * @return the updated zone
     * @throws APIException if the request fails
     */
    public Territory updateAvoidanceZone(Territory territory) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(AVOIDANCE_EP);
        if (territory.getTerritoryId() != null) {
            builder.setParameter("territory_id", territory.getTerritoryId());
        }
        return this.makeJSONRequest(RequestMethod.PUT, builder, territory, Territory.class);
    }

    /**
     * Lists the avoidance zones: {@code GET https://api.route4me.com/api.v4/avoidance.php}.
     *
     * @return the zones
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public List<Territory> getAvoidanceZones() throws APIException {
        URIBuilder builder = Manager.defaultBuilder(AVOIDANCE_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, new TypeToken<ArrayList<Territory>>() {
        }.getType());
    }

    /**
     * Gets one avoidance zone: {@code GET https://api.route4me.com/api.v4/avoidance.php?territory_id=...}.
     *
     * @param territoryId the zone ID
     * @return the zone
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Territory getAvoidanceZone(String territoryId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(AVOIDANCE_EP);
        builder.setParameter("territory_id", territoryId);
        return this.makeRequest(RequestMethod.GET, builder, "", Territory.class);
    }

    /**
     * Creates a territory: {@code POST https://api.route4me.com/api.v4/territory.php}, with the territory as the JSON
     * body.
     *
     * @param territory the territory
     * @return the created territory
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Territory addTerritory(Territory territory) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TERRITORY_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(territory), Territory.class);
    }

    /**
     * Deletes a territory: {@code DELETE /api.v4/territory.php?territory_id=...}.
     *
     * @param territoryId the territory ID
     * @return whether the territory was deleted; {@code false} also when the answer is empty
     * @throws APIException if the request fails
     */
    public boolean deleteTerritory(String territoryId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TERRITORY_EP);
        builder.setParameter("territory_id", territoryId);
        DeleteResponse response = this.makeRequest(RequestMethod.DELETE, builder, "", DeleteResponse.class);
        return response != null && response.isDeleted();
    }

    /**
     * Updates a territory: {@code PUT /api.v4/territory.php?territory_id=...} with the
     * territory as the JSON body.
     *
     * @param territory the territory, with its {@code territory_id} set
     * @return the updated territory
     * @throws APIException if the request fails
     */
    public Territory updateTerritory(Territory territory) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TERRITORY_EP);
        if (territory.getTerritoryId() != null) {
            builder.setParameter("territory_id", territory.getTerritoryId());
        }
        return this.makeJSONRequest(RequestMethod.PUT, builder, territory, Territory.class);
    }

    /**
     * Lists the territories: {@code GET https://api.route4me.com/api.v4/territory.php}.
     *
     * @return the territories
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public List<Territory> getTerritories() throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TERRITORY_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, new TypeToken<ArrayList<Territory>>() {
        }.getType());
    }

    /**
     * Gets one territory: {@code GET https://api.route4me.com/api.v4/territory.php?territory_id=...}.
     *
     * @param territoryId the territory ID
     * @return the territory
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Territory getTerritory(String territoryId) throws APIException {
        return this.getTerritory(territoryId, false, false);
    }

    /**
     * Gets one territory with the orders in it: {@code GET
     * https://api.route4me.com/api.v4/territory.php?territory_id=...&orders=1}.
     *
     * @param territoryId the territory ID
     * @return the territory, with its order IDs
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Territory getOrdersInTerritory(String territoryId) throws APIException {
        return this.getTerritory(territoryId, false, true);
    }

    /**
     * Gets one territory with the addresses in it:
     * {@code GET https://api.route4me.com/api.v4/territory.php?territory_id=...&addresses=1}.
     *
     * @param territoryId the territory ID
     * @return the territory, with its address IDs
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Territory getAddressesInTerritory(String territoryId) throws APIException {
        return this.getTerritory(territoryId, true, false);
    }

    /**
     * Gets a territory: {@code GET /api.v4/territory.php?territory_id=...}.
     *
     * @param territoryId the territory ID
     * @param addresses   whether to include the addresses in the territory; {@code null}
     *                    leaves the {@code addresses} parameter out
     * @param orders      whether to include the orders in the territory; {@code null}
     *                    leaves the {@code orders} parameter out
     * @return the territory
     * @throws APIException if the request fails
     */
    public Territory getTerritory(String territoryId, Boolean addresses, Boolean orders) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TERRITORY_EP);
        builder.setParameter("territory_id", territoryId);
        // 1 or 0, not "true" or "false": PHP reads the string "false" as true.
        if (orders != null) {
            builder.setParameter("orders", orders ? "1" : "0");
        }
        if (addresses != null) {
            builder.setParameter("addresses", addresses ? "1" : "0");
        }
        return this.makeRequest(RequestMethod.GET, builder, "", Territory.class);
    }

}

package com.route4me.sdk.services.territories;

import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.DeleteResponse;
import com.route4me.sdk.services.routing.GeoCoordinates;
import java.awt.Point;
import java.awt.Polygon;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TerritoriesManager extends Manager {

    public static final String AVOIDANCE_EP = "/api.v4/avoidance.php";
    public static final String TERRITORY_EP = "/api.v4/territory.php";

    private static final int FACTOR = 1000000;

    public TerritoriesManager(String apiKey) {
        super(apiKey);
    }

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
                }
            }

        } catch (APIException ex) {
            Logger.getLogger(TerritoriesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return territoryPolygons;
    }

    public List<Territory> allocateTerritories(TerritoryAllocated territoryAllocated, List<TerritoryPolygon> territoryPolygons) {

        List<Territory> foundTerritories = new ArrayList<>();

        int x = (int) (territoryAllocated.getLatitude() * FACTOR);
        int y = (int) (territoryAllocated.getLongitude() * FACTOR);

        Point point = new Point(x, y);

        for (TerritoryPolygon territoryPolygon : territoryPolygons) {
            if (territoryPolygon.getPolygon().contains(point)) {
                foundTerritories.add(territoryPolygon.getTerritory());
            }
        }

        return foundTerritories;

    }

    public Territory addAvoidanceZone(Territory territory) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(AVOIDANCE_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(territory), Territory.class);
    }

    public boolean deleteAvoidanceZone(String territoryId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(AVOIDANCE_EP);
        builder.setParameter("territory_id", territoryId);
        return this.makeRequest(RequestMethod.DELETE, builder, "", DeleteResponse.class).isDeleted();
    }

    public Territory updateAvoidanceZone(Territory territory) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(AVOIDANCE_EP);
        return this.makeJSONRequest(RequestMethod.DELETE, builder, territory, Territory.class);
    }

    public List<Territory> getAvoidanceZones() throws APIException {
        URIBuilder builder = Manager.defaultBuilder(AVOIDANCE_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, new TypeToken<ArrayList<Territory>>() {
        }.getType());
    }

    public Territory getAvoidanceZone(String territoryId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(AVOIDANCE_EP);
        builder.setParameter("territory_id", territoryId);
        return this.makeRequest(RequestMethod.GET, builder, "", Territory.class);
    }

    public Territory addTerritory(Territory territory) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TERRITORY_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(territory), Territory.class);
    }

    public boolean deleteTerritory(String territoryId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TERRITORY_EP);
        builder.setParameter("territory_id", territoryId);
        return this.makeRequest(RequestMethod.DELETE, builder, "", DeleteResponse.class).isDeleted();
    }

    public Territory updateTerritory(Territory territory) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TERRITORY_EP);
        return this.makeJSONRequest(RequestMethod.DELETE, builder, territory, Territory.class);
    }

    public List<Territory> getTerritories() throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TERRITORY_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, new TypeToken<ArrayList<Territory>>() {
        }.getType());
    }

    public Territory getTerritory(String territoryId) throws APIException {
        return this.getTerritory(territoryId, false, false);
    }

    public Territory getOrdersInTerritory(String territoryId) throws APIException {
        return this.getTerritory(territoryId, false, true);
    }

    public Territory getAddressesInTerritory(String territoryId) throws APIException {
        return this.getTerritory(territoryId, true, false);
    }

    public Territory getTerritory(String territoryId, Boolean addresses, Boolean orders) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TERRITORY_EP);
        builder.setParameter("territory_id", territoryId);
        builder.setParameter("orders", orders.toString());
        builder.setParameter("addresses", addresses.toString());
        return this.makeRequest(RequestMethod.GET, builder, "", Territory.class);
    }

}

package com.route4me.sdk.services.routing.breaks;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.http.client.utils.URIBuilder;

public class RouteBreaksManager extends Manager {

    public static final String ROUTE_BREAKS_EP = "/modules/api/v5.0/route-breaks";

    public RouteBreaksManager(String apiKey) {
        super(apiKey);
    }

    private URIBuilder getURI(String endpoint){
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }

    private Object insertBreak(Integer duration, Integer amount, String type, boolean replaceExisting, String [] routeIDs) throws APIException{
        if (amount == null) {
            throw new IllegalArgumentException("The break amount (" + type + ") must not be null");
        }
        if (routeIDs == null || routeIDs.length == 0) {
            throw new IllegalArgumentException("At least one route ID is required");
        }
        URIBuilder builder = this.getURI(ROUTE_BREAKS_EP);
        RouteBreak routeBreak = new RouteBreak();
        routeBreak.setDuration(duration);
        routeBreak.setType(type);
        int params[] = new int[routeIDs.length];
        Arrays.fill(params, amount);
        routeBreak.setParams(params);
        List<RouteBreak> breaks = new ArrayList();
        breaks.add(routeBreak);
        RouteBreaks routeBreaks = new RouteBreaks();
        routeBreaks.setReplaceExisting(replaceExisting);
        routeBreaks.setBreaks(breaks);
        routeBreaks.setRouteID(routeIDs);
        return this.makeJSONRequest(RequestMethod.POST, builder, routeBreaks, null, Object.class);
    }

    /**
     * Inserts a break of {@code duration} seconds after a given travel time:
     * {@code POST /modules/api/v5.0/route-breaks}.
     *
     * @param duration        the break length, in seconds
     * @param travelTime      the travel time before the break, in seconds; not {@code null}
     * @param replaceExisting whether to replace the routes' existing breaks
     * @param routeIDs        the routes; at least one
     * @return the decoded answer
     * @throws IllegalArgumentException if {@code travelTime} is {@code null} or no route ID is given
     * @throws APIException             if the request fails
     */
    public Object insertBreakAfterCertainTravelTime(Integer duration, Integer travelTime, boolean replaceExisting, String ... routeIDs) throws APIException {
        return this.insertBreak(duration, travelTime, RouteBreaksEnum.RouteBreaksType.CERTAIN_NUMBER_OF_TRAVEL_TIME.getValue(), replaceExisting, routeIDs);
    }

    /**
     * Inserts a break of {@code duration} seconds after a given number of stops:
     * {@code POST /modules/api/v5.0/route-breaks}.
     *
     * @param duration        the break length, in seconds
     * @param locationsNumber the number of stops before the break; not {@code null}
     * @param replaceExisting whether to replace the routes' existing breaks
     * @param routeIDs        the routes; at least one
     * @return the decoded answer
     * @throws IllegalArgumentException if {@code locationsNumber} is {@code null} or no route ID is given
     * @throws APIException             if the request fails
     */
    public Object insertBreakAfterCertainLocations(Integer duration, Integer locationsNumber, boolean replaceExisting, String ... routeIDs) throws APIException {
        return this.insertBreak(duration, locationsNumber, RouteBreaksEnum.RouteBreaksType.CERTAIN_NUMBER_OF_LOCATIONS.getValue(), replaceExisting, routeIDs);
    }

    /**
     * Inserts a break of {@code duration} seconds after a given service time:
     * {@code POST /modules/api/v5.0/route-breaks}.
     *
     * @param duration        the break length, in seconds
     * @param serviceTime     the service time before the break, in seconds; not {@code null}
     * @param replaceExisting whether to replace the routes' existing breaks
     * @param routeIDs        the routes; at least one
     * @return the decoded answer
     * @throws IllegalArgumentException if {@code serviceTime} is {@code null} or no route ID is given
     * @throws APIException             if the request fails
     */
    public Object insertBreakAfterCertainServiceTime(Integer duration, Integer serviceTime, boolean replaceExisting, String ... routeIDs) throws APIException {
        return this.insertBreak(duration, serviceTime, RouteBreaksEnum.RouteBreaksType.CERTAIN_NUMBER_OF_SERVICE_TIME.getValue(), replaceExisting, routeIDs);
    }

    /**
     * Inserts a break of {@code duration} seconds after a given elapsed time:
     * {@code POST /modules/api/v5.0/route-breaks}.
     *
     * @param duration        the break length, in seconds
     * @param elapsedTime     the elapsed time before the break, in seconds; not {@code null}
     * @param replaceExisting whether to replace the routes' existing breaks
     * @param routeIDs        the routes; at least one
     * @return the decoded answer
     * @throws IllegalArgumentException if {@code elapsedTime} is {@code null} or no route ID is given
     * @throws APIException             if the request fails
     */
    public Object insertBreakAfterCertainElapsed(Integer duration, Integer elapsedTime, boolean replaceExisting, String ... routeIDs) throws APIException {
        return this.insertBreak(duration, elapsedTime, RouteBreaksEnum.RouteBreaksType.CERTAIN_NUMBER_OF_TOTAL_ELAPSED_TIME.getValue(), replaceExisting, routeIDs);
    }



}

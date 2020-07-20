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
    
    public Object insertBreakAfterCertainTravelTime(Integer duration, Integer travelTime, boolean replaceExisting, String ... routeIDs) throws APIException {
        return this.insertBreak(duration, travelTime, RouteBreaksEnum.RouteBreaksType.CERTAIN_NUMBER_OF_TRAVEL_TIME.getValue(), replaceExisting, routeIDs);
    }

    public Object insertBreakAfterCertainLocations(Integer duration, Integer locationsNumber, boolean replaceExisting, String ... routeIDs) throws APIException {
        return this.insertBreak(duration, locationsNumber, RouteBreaksEnum.RouteBreaksType.CERTAIN_NUMBER_OF_LOCATIONS.getValue(), replaceExisting, routeIDs);
    }

    public Object insertBreakAfterCertainServiceTime(Integer duration, Integer serviceTime, boolean replaceExisting, String ... routeIDs) throws APIException {
        return this.insertBreak(duration, serviceTime, RouteBreaksEnum.RouteBreaksType.CERTAIN_NUMBER_OF_SERVICE_TIME.getValue(), replaceExisting, routeIDs);
    }

    public Object insertBreakAfterCertainElapsed(Integer duration, Integer elapsedTime, boolean replaceExisting, String ... routeIDs) throws APIException {
        return this.insertBreak(duration, elapsedTime, RouteBreaksEnum.RouteBreaksType.CERTAIN_NUMBER_OF_TOTAL_ELAPSED_TIME.getValue(), replaceExisting, routeIDs);
    }


    
}

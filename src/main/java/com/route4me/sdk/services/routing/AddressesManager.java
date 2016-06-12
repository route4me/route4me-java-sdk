package com.route4me.sdk.services.routing;

import com.google.gson.Gson;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.model.Routes;
import com.route4me.sdk.model.enums.APIEndpoints;
import java.util.List;
import org.apache.http.client.utils.URIBuilder;

public class AddressesManager extends Manager {

    public AddressesManager(String apiKey) {
        super(apiKey, new Gson());
    }

//    public DataObject addAddressesToOptimizaion(String optimizationProblemID, List<Address> addresses) {
//        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.)
//        try {
//            getParams().put("optimization_problem_id", optimizationProblemID);
//            getParams().put("reoptimize", "1");
//            DataObject dataObj = new DataObject();
//            dataObj.setAddresses(addresses);
//            String data = getGson().toJson(dataObj);
//            String params = Manager.transformParams(this.getParams());
//            Response response = Manager.makeURLConnectionRequest(RequestMethod.PUT, buildBaseURL(), params, data);
//            
//            DataObject responseObject = DataObjectDeserializer.GSON_DESERIALIZER.fromJson(response.getResponseBody(), DataObject.class);
//            return responseObject;
//        } catch (UnsupportedEncodingException |InvalidRequestException ex) {
//            throw new APIException(ex);
//        }
//    }
    public DataObject addAddressesToRoute(String routeId, List<Address> addresses) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.ROUTE_HOST);
        builder.setParameter("route_id", routeId);
        DataObject dataObj = new DataObject();
        dataObj.setAddresses(addresses);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(dataObj), DataObject.class);
    }

    public Address getAddress(String routeId, Number routeDestinationId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.ADDRESS_HOST);
        builder.setParameter("route_id", routeId);
        builder.setParameter("route_destination_id", routeDestinationId.toString());
        return this.makeRequest(RequestMethod.GET, builder, "", Address.class);
    }

    public Routes moveAddresses(DataObject dataObj, String routeId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.ROUTE_HOST);
        builder.setParameter("route_id", routeId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(dataObj), Routes.class);
    }

    public void deleteAddress(String routeId, Number routeDestinationId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.ROUTE_HOST);
        builder.setParameter("route_id", routeId);
        builder.setParameter("route_destination_id", routeDestinationId.toString());
        this.makeRequest(RequestMethod.DELETE, builder, "", null);
    }

    public Routes updateAddressAttribute(String routeId, Number routeDestinationId, Address dataObj) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.ROUTE_HOST);
        builder.setParameter("route_id", routeId);
        builder.setParameter("route_destination_id", routeDestinationId.toString());
        return this.makeRequest(RequestMethod.PUT, Manager.defaultBuilder(APIEndpoints.ROUTE_HOST), this.gson.toJson(dataObj), Routes.class);
    }

}

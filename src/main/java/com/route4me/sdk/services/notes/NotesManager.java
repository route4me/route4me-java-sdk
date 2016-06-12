package com.route4me.sdk.services.notes;

import com.google.gson.Gson;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.Manager;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.model.enums.APIEndpoints;
import com.route4me.sdk.model.enums.Constants;
import com.route4me.sdk.services.routing.Address;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

public class NotesManager extends Manager {

    public NotesManager(String apiKey) {
        super(apiKey, new Gson());
    }

    public List<Note> getAddressNotes(String routeId, String routeDestinationId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.ADDRESS_HOST);
        builder.setParameter("route_id", routeId);
        builder.setParameter("route_destination_id", routeDestinationId);
        builder.setParameter("notes", "1");
        return this.makeRequest(RequestMethod.GET, builder, "", Address.class).getNotes();
    }

    public Notes addAddressNotes(String routeId, String routeDestinationId,
            String note, String activityType, Constants.DeviceType deviceType,
            Number latitude, Number longitude) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.ADD_ROUTE_NOTES_HOST);
        builder.setParameter("route_id", routeId);
        builder.setParameter("address_id", routeDestinationId);
        builder.setParameter("device_type", deviceType.toString());
        builder.setParameter("dev_lat", latitude.toString());
        builder.setParameter("dev_lng", longitude.toString());
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("strNoteContents", note));
        data.add(new BasicNameValuePair("strUpdateType", activityType));
        return this.makeRequest(RequestMethod.POST, builder, data, Notes.class);
    }

}

package com.route4me.sdk.services.notes;

import com.google.gson.Gson;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.services.routing.Address;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class NotesManager extends Manager {
    public static final String ADD_NOTES_EP = "/actions/addRouteNotes.php";
    public static final String GET_NOTES_EP = "/api.v4/address.php";

    public NotesManager(String apiKey) {
        super(apiKey, new Gson());
    }

    public List<Note> getAddressNotes(String routeId, String routeDestinationId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(GET_NOTES_EP);
        builder.setParameter("route_id", routeId);
        builder.setParameter("route_destination_id", routeDestinationId);
        builder.setParameter("notes", "1");
        return this.makeRequest(RequestMethod.GET, builder, "", Address.class).getNotes();
    }

    public Notes addAddressNotes(String routeId, String routeDestinationId,
                                 String note, String activityType, Constants.DeviceType deviceType,
                                 Number latitude, Number longitude) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(ADD_NOTES_EP);
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

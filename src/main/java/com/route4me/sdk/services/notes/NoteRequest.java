package com.route4me.sdk.services.notes;

import com.route4me.sdk.queryconverter.QueryParameter;
import com.route4me.sdk.services.routing.Constants;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The query parameters of {@link NotesManager#addAddressNotes(NoteRequest, String, StatusUpdateType)}: the route,
 * the destination and the location of the device.
 */
@Data
@Accessors(chain = true)
public class NoteRequest {
    /** Unique ID of a route, sent in the query ({@code route_id}). */
    @QueryParameter("route_id")
    private String routeId;
    /** Unique ID of a route destination, sent in the query ({@code address_id}). */
    @QueryParameter("address_id")
    private String addressId;
    /** Latitude of a device position, sent in the query ({@code dev_lat}). */
    @QueryParameter("dev_lat")
    private Double latitude;
    /** Longitude of a device position, sent in the query ({@code dev_lng}). */
    @QueryParameter("dev_lng")
    private Double longitude;
    /** The device that adds the note, such as {@code web} or {@code iphone} ({@code device_type}). */
    @QueryParameter("device_type")
    private Constants.DeviceType deviceType;
}

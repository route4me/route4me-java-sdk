
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.services.notes.Note;
import java.util.List;
import lombok.Data;

@Data
public class Address {

    @SerializedName("route_destination_id")
    private long routeDestinationId;
    private String alias;
    @SerializedName("member_id")
    private int memberId;
    private String address;
    @SerializedName("is_depot")
    private boolean isDepot;
    @SerializedName("lat")
    private double latitude;
    @SerializedName("lng")
    private double longitude;
    @SerializedName("route_id")
    private String routeId;
    @SerializedName("original_route_id")
    private String originalRouteId;
    @SerializedName("optimization_problem_id")
    private String optimizationProblemId;
    @SerializedName("sequence_no")
    private long sequenceNo;
    private boolean geocoded;
    @SerializedName("preferred_geocoding")
    private int preferredGeocoding;
    @SerializedName("failed_geocoding")
    private boolean failedGeocoding;
    @SerializedName("contact_id")
    private int contactId;
    @SerializedName("is_visited")
    private boolean isVisited;
    @SerializedName("destination_note_count")
    private int destinationNoteCount;
    @SerializedName("channel_name")
    private String channelName;
    @SerializedName("time_window_start")
    private long timeWindowStart;
    @SerializedName("time_window_end")
    private long timeWindowEnd;
    private long time;
    private List<Note> notes;
    
    public Address() {
    }

    public Address(String address, double lat, double lng, long time) {
        this.address = address;
        this.latitude = lat;
        this.longitude = lng;
        this.time = time;
    }

    public Address(String address, boolean isDepot, double lat, double lng, long time) {
        this(address, lat, lng, time);
        this.isDepot = isDepot;
    }

    public Address(String address, boolean isDepot, String alias, double lat, double lng, long time) {
        this(address, isDepot, lat, lng, time);
        this.alias = alias;
    }

    public Address(String address, boolean isDepot, double lat, double lng, long time, long timeWindowStart, long timeWindowEnd) {
        this.address = address;
        this.isDepot = isDepot;
        this.latitude = lat;
        this.longitude = lng;
        this.time = time;
        this.timeWindowStart = timeWindowStart;
        this.timeWindowEnd = timeWindowEnd;
    }

    public Address(String address, double lat, double lng, long time, long timeWindowStart, long timeWindowEnd) {
        this.address = address;
        this.latitude = lat;
        this.longitude = lng;
        this.time = time;
        this.timeWindowStart = timeWindowStart;
        this.timeWindowEnd = timeWindowEnd;
    }

    public Address(String address, String alias, double lat, double lng, long time) {
        this.alias = alias;
        this.address = address;
        this.latitude = lat;
        this.longitude = lng;
        this.time = time;
    }
}

// codebeat:disable[TOO_MANY_FUNCTIONS]
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.services.notes.Note;
import java.util.List;
import lombok.Data;

@Data
public class Address {

    @SerializedName("route_destination_id")
    private Long routeDestinationId;
    @SerializedName("alias")
    private String alias;
    @SerializedName("member_id")
    private Integer memberId;
    @SerializedName("address")
    private String address;
    @SerializedName("is_depot")
    private Boolean depot;
    @SerializedName("lat")
    private Double latitude;
    @SerializedName("lng")
    private Double longitude;
    @SerializedName("route_id")
    private String routeId;
    @SerializedName("original_route_id")
    private String originalRouteId;
    @SerializedName("optimization_problem_id")
    private String optimizationProblemId;
    @SerializedName("sequence_no")
    private Long sequenceNo;
    @SerializedName("geocoded")
    private Boolean geocoded;
    @SerializedName("preferred_geocoding")
    private Integer preferredGeocoding;
    @SerializedName("failed_geocoding")
    private Boolean failedGeocoding;
    @SerializedName("contact_id")
    private Integer contactId;
    @SerializedName("is_visited")
    private Boolean isVisited;
    @SerializedName("destination_note_count")
    private Integer destinationNoteCount;
    @SerializedName("channel_name")
    private String channelName;
    @SerializedName("time_window_start")
    private Long timeWindowStart;
    @SerializedName("time_window_end")
    private Long timeWindowEnd;
    @SerializedName("time")
    private Long time;
    @SerializedName("notes")
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
        this.depot = isDepot;
    }

    public Address(String address, boolean isDepot, String alias, double lat, double lng, long time) {
        this(address, isDepot, lat, lng, time);
        this.alias = alias;
    }

    public Address(String address, boolean isDepot, double lat, double lng, long time, long timeWindowStart, long timeWindowEnd) {
        this.address = address;
        this.depot = isDepot;
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
// codebeat:enable[TOO_MANY_FUNCTIONS]

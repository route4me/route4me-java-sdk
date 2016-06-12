
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.services.notes.Note;
import java.util.List;
import lombok.Data;

@Data
public class Address {

    @SerializedName("route_destination_id")
    private Number routeDestinationId;
    private String alias;
    @SerializedName("member_id")
    private Number memberId;
    private String address;
    @SerializedName("is_depot")
    private Boolean isDepot;
    @SerializedName("lat")
    private Number latitude;
    @SerializedName("longitude")
    private Number longitude;
    @SerializedName("route_id")
    private String routeId;
    @SerializedName("original_route_id")
    private String originalRouteId;
    @SerializedName("optimization_problem_id")
    private String optimizationProblemId;
    @SerializedName("sequence_no")
    private Number sequenceNo;
    private Boolean geocoded;
    @SerializedName("preferred_geocoding")
    private Number preferredGeocoding;
    @SerializedName("failed_geocoding")
    private Boolean failedGeocoding;
    @SerializedName("contact_id")
    private Number contactId;
    @SerializedName("is_visited")
    private Boolean isVisited;
    @SerializedName("destination_note_count")
    private Number destinationNoteCount;
    @SerializedName("channel_name")
    private String channelName;
    @SerializedName("time_window_start")
    private Number timeWindowStart;
    @SerializedName("time_window_end")
    private Number timeWindowEnd;
    private Number time;
    private List<Note> notes;
    
    public Address() {
    }

    public Address(String address, Number lat, Number lng, Number time) {
        this.address = address;
        this.latitude = lat;
        this.longitude = lng;
        this.time = time;
    }

    public Address(String address, Boolean isDepot, Number lat, Number lng, Number time) {
        this(address, lat, lng, time);
        this.isDepot = isDepot;
    }

    public Address(String address, Boolean isDepot, String alias, Number lat, Number lng, Number time) {
        this(address, isDepot, lat, lng, time);
        this.alias = alias;
    }

    public Address(String address, Boolean isDepot, Number lat, Number lng, Number time, Number timeWindowStart, Number timeWindowEnd) {
        this.address = address;
        this.isDepot = isDepot;
        this.latitude = lat;
        this.longitude = lng;
        this.time = time;
        this.timeWindowStart = timeWindowStart;
        this.timeWindowEnd = timeWindowEnd;
    }

    public Address(String address, Number lat, Number lng, Number time, Number timeWindowStart, Number timeWindowEnd) {
        this.address = address;
        this.latitude = lat;
        this.longitude = lng;
        this.time = time;
        this.timeWindowStart = timeWindowStart;
        this.timeWindowEnd = timeWindowEnd;
    }

    public Address(String address, String alias, Number lat, Number lng, Number time) {
        this.alias = alias;
        this.address = address;
        this.latitude = lat;
        this.longitude = lng;
        this.time = time;
    }
}

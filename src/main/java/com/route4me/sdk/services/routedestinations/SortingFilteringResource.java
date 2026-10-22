package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SortingFilteringResource {
    @SerializedName("destination_id")
    private Integer destinationId;

    @SerializedName("destination_uuid")
    private String destinationUuid;

    @SerializedName("route_name")
    private String routeName;

    @SerializedName("route_id")
    private String routeId;

    @SerializedName("scheduled_for")
    private Integer scheduledFor;

    @SerializedName("destination_address")
    private String destinationAddress;

    @SerializedName("sequence_no")
    private Integer sequenceNo;

    @SerializedName("assigned_user_driver")
    private String assignedUserDriver;

    @SerializedName("member_id")
    private Integer memberId;

    @SerializedName("assigned_vehicle")
    private String assignedVehicle;

    @SerializedName("vehicle_id")
    private String vehicleId;

    @SerializedName("tracking_number")
    private String trackingNumber;

    @SerializedName("label_code")
    private String labelCode;

    @SerializedName("path")
    private String path;
}

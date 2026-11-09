package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link VehiclesManagerV5#getPaginatedVehiclesList(VehiclePaginateRequest)}: one page of vehicles. */
@Data
public class VehiclesPaginatedResponse {
    /** The vehicles of the page ({@code data}). */
    @SerializedName("data")
    private List<VehicleV5> data;
    /** The number of vehicles on all pages ({@code total}). */
    @SerializedName("total")
    private Integer total;
    /** Error messages, empty when there is none ({@code error}). */
    @SerializedName("error")
    private List<String> error;
    /** The paging data ({@code meta}). */
    @SerializedName("meta")
    private VehiclePageMeta meta;
}

package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link VehiclesManagerV5#getVehicleProfiles(Integer, Integer)}: one page of vehicle profiles. */
@Data
public class VehicleProfilesResponse {
    /** The vehicle profiles of the page ({@code data}). */
    @SerializedName("data")
    private List<VehicleProfile> data;
    /** The page number ({@code current_page}). */
    @SerializedName("current_page")
    private Integer currentPage;
    /** The number of the last page ({@code last_page}). */
    @SerializedName("last_page")
    private Integer lastPage;
    /** The number of profiles per page ({@code per_page}). */
    @SerializedName("per_page")
    private Integer perPage;
    /** The position of the first profile of the page, from 1 ({@code from}). */
    @SerializedName("from")
    private Integer from;
    /** The position of the last profile of the page ({@code to}). */
    @SerializedName("to")
    private Integer to;
    /** The number of profiles on all pages ({@code total}). */
    @SerializedName("total")
    private Integer total;
    /** The URL of the next page, or {@code null} on the last page ({@code next_page_url}). */
    @SerializedName("next_page_url")
    private String nextPageUrl;
}

package com.route4me.sdk.services.territories;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/**
 * The body of {@link TerritoriesManagerV5#createAutomaticTerritoriesJob}: the addresses to cluster into territories,
 * given in {@code addresses} or by a temporary address storage. Fields left {@code null} are not sent.
 */
@Data
public class AutomaticTerritoriesJobRequest {
    /**
     * The ID of a temporary address storage, such as the one {@code GeocodingManager#jsonUploadGeocoder} returns;
     * required when there are no {@code addresses} ({@code temporary_addresses_storage_id}).
     */
    @SerializedName("temporary_addresses_storage_id")
    private String temporaryAddressesStorageId;
    /** The addresses; required when there is no storage ID ({@code addresses}). */
    @SerializedName("addresses")
    private List<AutomaticTerritoriesAddress> addresses;
    /** The clustering mode: 0, 1 or 2 ({@code mode}). */
    @SerializedName("mode")
    private Integer mode;
    /** Optional clustering parameters ({@code params}). */
    @SerializedName("params")
    private List<String> params;
}

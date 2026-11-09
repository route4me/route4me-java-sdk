package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of the {@code getAddressBookContactsClustering} methods of {@link AddressBookManagerV5}. */
@Data
public class AddressBookClusteringResponse {
    /** The clusters ({@code clusters}). */
    @SerializedName("clusters")
    private List<AddressBookCluster> clusters;

    /** The total number of contacts ({@code total}). */
    @SerializedName("total")
    private Integer total;
}

package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The clustering options of an {@link AddressBookClusteringRequest}. */
@Data
public class AddressBookClustering {
    /** The geohash precision, from 1 (large clusters) to 12 (small ones); the API defaults to 5 ({@code precision}). */
    @SerializedName("precision")
    private Integer precision;
}

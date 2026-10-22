package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * One cluster of contacts, in an {@link AddressBookClusteringResponse}. The spec puts the position of the cluster
 * next to {@code address_count}; the C# and PHP SDKs read it from a nested {@code cluster} object. Both are read.
 */
@Data
public class AddressBookCluster {
    /** The geohash of the cluster ({@code geohash}). */
    @SerializedName("geohash")
    private String geohash;

    /** The latitude of the cluster ({@code lat}). */
    @SerializedName("lat")
    private Double lat;

    /** The longitude of the cluster ({@code lng}). */
    @SerializedName("lng")
    private Double lng;

    /** The bounds of the cluster, as {@code [lat, lng]} corners ({@code bbox}). */
    @SerializedName("bbox")
    private List<List<Double>> bbox;

    /** The number of contacts in the cluster ({@code address_count}). */
    @SerializedName("address_count")
    private Integer addressCount;

    /** The position of the cluster, when the answer nests it ({@code cluster}). */
    @SerializedName("cluster")
    private AddressBookCluster cluster;
}

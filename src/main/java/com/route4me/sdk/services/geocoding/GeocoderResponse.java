/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.geocoding;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

/** The answer of {@link GeocodingManager#jsonUploadGeocoder(List)}. */
@Data
public class GeocoderResponse {

    /** The ID of the stored addresses ({@code optimization_problem_id}). */
    @SerializedName("optimization_problem_id")
    @QueryParameter("optimization_problem_id")
    private String optimizationProblemID;
    /** The ID of the stored addresses, which the websocket geocoder takes ({@code temporary_addresses_storage_id}). */
    @SerializedName("temporary_addresses_storage_id")
    @QueryParameter("temporary_addresses_storage_id")
    private String temporaryAddressesStorageID;
    /** The number of addresses to geocode ({@code address_count}). */
    @SerializedName("address_count")
    @QueryParameter("address_count")
    private Integer addressCount;
    /** Whether the addresses were accepted ({@code status}). */
    @SerializedName("status")
    @QueryParameter("status")
    private Boolean status;
}

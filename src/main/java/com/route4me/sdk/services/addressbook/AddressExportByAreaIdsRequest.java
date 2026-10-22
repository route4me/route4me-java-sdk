package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * The body of {@link AddressBookManagerV5#exportAddressesByAreaIds(AddressExportByAreaIdsRequest)}: the territories
 * whose contacts to export, and the file to make. Fields left {@code null} are not sent.
 */
@Data
public class AddressExportByAreaIdsRequest {
    /** The IDs of the territories ({@code territory_ids}). */
    @SerializedName("territory_ids")
    private List<String> territoryIds;

    /** The name of the file ({@code filename}). */
    @SerializedName("filename")
    private String filename;

    /** The kind of file: {@code csv} (the default), {@code xls} or {@code xlsx} ({@code extension}). */
    @SerializedName("extension")
    private String extension;

    /** The time zone of the dates in the file, such as {@code America/New_York} ({@code tz}). */
    @SerializedName("tz")
    private String timezone;
}

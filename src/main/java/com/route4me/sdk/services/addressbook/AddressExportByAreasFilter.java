package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * The {@code filter} of {@link AddressBookManagerV5#exportAddressesByAreas(AddressExportByAreasFilter)}: the contacts
 * to export and, as the spec puts them inside the filter, the file to make. Fields left {@code null} are not sent;
 * {@code query} and {@code bounding_box} are optional.
 */
@Data
public class AddressExportByAreasFilter {
    /** Text to search for in the contacts ({@code query}). */
    @SerializedName("query")
    private String query;

    /** Only the contacts inside this rectangle ({@code bounding_box}). */
    @SerializedName("bounding_box")
    private BoundingBox boundingBox;

    /** Only the contacts inside these areas ({@code selected_areas}). */
    @SerializedName("selected_areas")
    private List<SelectedArea> selectedAreas;

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

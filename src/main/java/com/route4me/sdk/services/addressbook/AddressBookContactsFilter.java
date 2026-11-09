package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * The {@code filter} of the contact list and clustering bodies of {@link AddressBookManagerV5}; filters left
 * {@code null} are not sent.
 */
@Data
public class AddressBookContactsFilter {
    /** Text to search for in the contacts ({@code query}). */
    @SerializedName("query")
    private String query;

    /** Only the contacts inside these areas ({@code selected_areas}). */
    @SerializedName("selected_areas")
    private List<SelectedArea> selectedAreas;

    /** Only the contacts inside this rectangle ({@code bounding_box}). */
    @SerializedName("bounding_box")
    private BoundingBox boundingBox;

    /** The center of a circle to search in, with {@code distance} ({@code center}). */
    @SerializedName("center")
    private GeoPoint center;

    /** The radius of the circle around {@code center}; the API defaults to 500 ({@code distance}). */
    @SerializedName("distance")
    private Double distance;

    /** Which contacts to return: {@code all}, {@code routed} or {@code unrouted} ({@code display}). */
    @SerializedName("display")
    private String display;

    /** Which contacts to return: {@code routed}, {@code unrouted} or both ({@code display_list}). */
    @SerializedName("display_list")
    private List<String> displayList;

    /** Only the contacts assigned to this member ({@code assigned_member_id}). */
    @SerializedName("assigned_member_id")
    private Long assignedMemberId;

    /** Only the contacts that are, or are not, assigned to a member ({@code is_assigned}). */
    @SerializedName("is_assigned")
    private Boolean isAssigned;

    /** Only the contacts that come, or do not come, from an external system ({@code is_external}). */
    @SerializedName("is_external")
    private Boolean isExternal;

    /** Only the contacts of these external connections ({@code external_connection_ids}). */
    @SerializedName("external_connection_ids")
    private List<Long> externalConnectionIds;

    /** Only the contacts of these external vendors ({@code external_vendor_ids}). */
    @SerializedName("external_vendor_ids")
    private List<Long> externalVendorIds;

    /** Only the contacts of these customers ({@code customer_ids}). */
    @SerializedName("customer_ids")
    private List<String> customerIds;
}

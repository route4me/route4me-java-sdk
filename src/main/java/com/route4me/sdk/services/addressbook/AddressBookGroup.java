package com.route4me.sdk.services.addressbook;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * An address book group: a named set of rules that selects contacts. The V4 group methods of
 * {@link AddressBookManager} take and return it, and {@link AddressBookManagerV5#getAddressBookGroups()} returns it.
 * Fields left {@code null} are not sent.
 */
@Data
public class AddressBookGroup {
    /** The ID of the group ({@code group_id}). */
    @SerializedName("group_id")
    private String groupId;

    /** The name of the group ({@code group_name}). */
    @SerializedName("group_name")
    private String groupName;

    /** The color of the group, as a hex code without {@code #}, such as {@code 92e1c0} ({@code group_color}). */
    @SerializedName("group_color")
    private String groupColor;

    /** The icon of the group ({@code group_icon}). */
    @SerializedName("group_icon")
    private String groupIcon;

    /** The ID of the member who owns the group ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;

    /** The rules the contacts of the group match ({@code filter}). */
    @SerializedName("filter")
    private AddressBookGroupFilter filter;

    /** Whether the rules of the group are valid ({@code valid}). */
    @SerializedName("valid")
    private Boolean valid;
}

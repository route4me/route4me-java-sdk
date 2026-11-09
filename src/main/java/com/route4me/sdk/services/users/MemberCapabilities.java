package com.route4me.sdk.services.users;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
import java.util.Map;

/** The capabilities of the account, as {@link UsersManager#getMemberCapabilities()} returns them. */
@Data
public class MemberCapabilities {
    /** What routes may avoid, such as {@code highways} ({@code avoid}). */
    @SerializedName("avoid")
    private List<String> avoid;

    /** The roads routes may avoid ({@code avoid_roads}). */
    @SerializedName("avoid_roads")
    private List<String> avoidRoads;

    /** The features of the account, by name, and whether each is enabled ({@code features}). */
    @SerializedName("features")
    private Map<String, Boolean> features;

    /** The travel modes of the account, by key ({@code travelModes}). */
    @SerializedName("travelModes")
    private Map<String, String> travelModes;

    /** What routes may be navigated by ({@code navigateBy}). */
    @SerializedName("navigateBy")
    private List<String> navigateBy;

    /** The modules licensed to the account ({@code LicensedModules}). */
    @SerializedName("LicensedModules")
    private List<String> licensedModules;

    /** Whether the account is commercial ({@code commercial}). */
    @SerializedName("commercial")
    private Boolean commercial;
}

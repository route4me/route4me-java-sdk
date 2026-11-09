package com.route4me.sdk.services.users;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

/**
 * A member session, as {@link UsersManager#userAuthentication(String, String)} and
 * {@link UsersManager#validateSession(String, long)} return it. It holds the API key and the session of the
 * member: do not log them; {@code toString()} leaves them out.
 */
@Data
public class MemberResponse {
    /** Whether the request succeeded ({@code status}). */
    @SerializedName("status")
    private Boolean status;

    /** Whether the session is valid, in the answer of a session check ({@code authenticated}). */
    @SerializedName("authenticated")
    private Boolean authenticated;

    /** The geocoding service of the account ({@code geocoding_service}). */
    @SerializedName("geocoding_service")
    private String geocodingService;

    /** The session ID; {@code toString()} leaves it out ({@code session_id}). */
    @ToString.Exclude
    @SerializedName("session_id")
    private Long sessionId;

    /** The session GUID, a credential; {@code toString()} leaves it out ({@code session_guid}). */
    @ToString.Exclude
    @SerializedName("session_guid")
    private String sessionGuid;

    /** The member ID ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;

    /** The API key of the member, a credential; {@code toString()} leaves it out ({@code api_key}). */
    @ToString.Exclude
    @SerializedName("api_key")
    private String apiKey;

    /** How long a tracking point is kept, in seconds ({@code tracking_ttl}). */
    @SerializedName("tracking_ttl")
    private Integer trackingTtl;

    /** The shape of the geofence around a destination ({@code geofence_polygon_shape}). */
    @SerializedName("geofence_polygon_shape")
    private String geofencePolygonShape;

    /** The size of the geofence around a destination ({@code geofence_polygon_size}). */
    @SerializedName("geofence_polygon_size")
    private Long geofencePolygonSize;

    /** How long a driver must stay in the geofence to trigger a visit, in seconds. */
    @SerializedName("geofence_time_onsite_trigger_secs")
    private Long geofenceTimeOnsiteTriggerSecs;

    /** The speed under which a geofence triggers ({@code geofence_minimum_trigger_speed}). */
    @SerializedName("geofence_minimum_trigger_speed")
    private Integer geofenceMinimumTriggerSpeed;

    /** Whether the subscription of the account is past due ({@code is_subscription_past_due}). */
    @SerializedName("is_subscription_past_due")
    private Boolean isSubscriptionPastDue;

    /** Whether the mobile app may mark destinations as visited and departed ({@code visited_departed_enabled}). */
    @SerializedName("visited_departed_enabled")
    private String visitedDepartedEnabled;

    /** Whether a long press is enabled in the mobile app ({@code long_press_enabled}). */
    @SerializedName("long_press_enabled")
    private String longPressEnabled;

    /** The account type ID ({@code account_type_id}). */
    @SerializedName("account_type_id")
    private Long accountTypeId;

    /** The account type ({@code account_type_alias}). */
    @SerializedName("account_type_alias")
    private String accountTypeAlias;

    /** The member type, such as {@code SUB_ACCOUNT_DRIVER} ({@code member_type}). */
    @SerializedName("member_type")
    private String memberType;

    /** The maximum number of stops per route ({@code max_stops_per_route}). */
    @SerializedName("max_stops_per_route")
    private Integer maxStopsPerRoute;

    /** The maximum number of routes ({@code max_routes}). */
    @SerializedName("max_routes")
    private Integer maxRoutes;

    /** The number of routes planned ({@code routes_planned}). */
    @SerializedName("routes_planned")
    private Integer routesPlanned;

    /** The preferred distance unit, such as {@code MI} ({@code preferred_units}). */
    @SerializedName("preferred_units")
    private String preferredUnits;

    /** The preferred language, such as {@code en} ({@code preferred_language}). */
    @SerializedName("preferred_language")
    private String preferredLanguage;

    /** Whether routed addresses are hidden ({@code HIDE_ROUTED_ADDRESSES}). */
    @SerializedName("HIDE_ROUTED_ADDRESSES")
    private String hideRoutedAddresses;

    /** Whether visited addresses are hidden ({@code HIDE_VISITED_ADDRESSES}). */
    @SerializedName("HIDE_VISITED_ADDRESSES")
    private String hideVisitedAddresses;

    /** Whether routes that are not in the future are hidden ({@code HIDE_NONFUTURE_ROUTES}). */
    @SerializedName("HIDE_NONFUTURE_ROUTES")
    private String hideNonfutureRoutes;

    /** When the session logs out by itself, as a Unix time ({@code auto_logout_ts}). */
    @SerializedName("auto_logout_ts")
    private Long autoLogoutTs;
}

package com.route4me.sdk.services.tracking;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * The answer of {@link TrackingManager#findAsset(String)}: where an order or asset is, by its tracking number, with
 * the branding of the tracking page. Matches {@code FindAssetResponse} of the C# SDK.
 */
@Data
public class FindAssetResponse {
    /** The tracking number ({@code tracking_number}). */
    @SerializedName("tracking_number")
    private String trackingNumber;
    /** The large logo of the tracking page ({@code large_logo_uri}). */
    @SerializedName("large_logo_uri")
    private String largeLogoUri;
    /** The large logo at double resolution ({@code large_logo_uri_2x}). */
    @SerializedName("large_logo_uri_2x")
    private String largeLogoUri2x;
    /** The mobile logo of the tracking page ({@code mobile_logo_uri}). */
    @SerializedName("mobile_logo_uri")
    private String mobileLogoUri;
    /** The mobile logo at double resolution ({@code mobile_logo_uri_2x}). */
    @SerializedName("mobile_logo_uri_2x")
    private String mobileLogoUri2x;
    /** The color of the map ({@code map_color}). */
    @SerializedName("map_color")
    private String mapColor;
    /** The alignment of the large logo ({@code large_logo_alignment}). */
    @SerializedName("large_logo_alignment")
    private String largeLogoAlignment;
    /** The alignment of the mobile logo ({@code mobile_logo_alignment}). */
    @SerializedName("mobile_logo_alignment")
    private String mobileLogoAlignment;
    /** Whether the map shows its zoom controls ({@code show_map_zoom_controls}). */
    @SerializedName("show_map_zoom_controls")
    private Boolean showMapZoomControls;
    /** The driver's phone number ({@code driver_phone}). */
    @SerializedName("driver_phone")
    private String driverPhone;
    /** Whether the route has started ({@code route_started}). */
    @SerializedName("route_started")
    private Boolean routeStarted;
    /** The customer service phone number ({@code customer_service_phone}). */
    @SerializedName("customer_service_phone")
    private String customerServicePhone;
    /** Whether the tracking page hides its COVID-19 warning ({@code hide_covid19_warning}). */
    @SerializedName("hide_covid19_warning")
    private String hideCovid19Warning;
    /** The driver's name ({@code driver_name}). */
    @SerializedName("driver_name")
    private String driverName;
    /** The driver's picture ({@code driver_picture}). */
    @SerializedName("driver_picture")
    private String driverPicture;
    /** The subheadline of the tracking page ({@code tracking_page_subheadline}). */
    @SerializedName("tracking_page_subheadline")
    private String trackingPageSubheadline;
    /** The first line of the destination address ({@code destination_address_1}). */
    @SerializedName("destination_address_1")
    private String destinationAddress1;
    /** The second line of the destination address ({@code destination_address_2}). */
    @SerializedName("destination_address_2")
    private String destinationAddress2;
    /** The status changes, oldest first ({@code status_history}). */
    @SerializedName("status_history")
    private List<StatusHistory> statusHistory;
    /** The points to show on the map: the driver and the destination ({@code locations}). */
    @SerializedName("locations")
    private List<Location> locations;
    /** Custom data ({@code custom_data}). */
    @SerializedName("custom_data")
    private Map<String, String> customData;
    /** The expected arrival windows ({@code arrival}). */
    @SerializedName("arrival")
    private List<Arrival> arrival;
    /** Whether the order was delivered ({@code delivered}). */
    @SerializedName("delivered")
    private Boolean delivered;
    /** When the driver entered the geofence of the destination, as a Unix timestamp. */
    @SerializedName("timestamp_geofence_visited")
    private Long timestampGeofenceVisited;
    /** When the destination was last visited, as a Unix timestamp ({@code timestamp_last_visited}). */
    @SerializedName("timestamp_last_visited")
    private Long timestampLastVisited;

    /** One status change of a {@link FindAssetResponse}. */
    @Data
    public static class StatusHistory {
        /** When the status changed, as a Unix timestamp ({@code unix_timestamp}). */
        @SerializedName("unix_timestamp")
        private Long unixTimestamp;
        /** The status ({@code info}). */
        @SerializedName("info")
        private String info;
    }

    /** One point on the map of a {@link FindAssetResponse}. */
    @Data
    public static class Location {
        /** Whether the point is the destination ({@code is_destination}). */
        @SerializedName("is_destination")
        private Boolean isDestination;
        /** Latitude ({@code lat}). */
        @SerializedName("lat")
        private Double lat;
        /** Longitude ({@code lng}). */
        @SerializedName("lng")
        private Double lng;
        /** The map icon ({@code icon}). */
        @SerializedName("icon")
        private String icon;
        /** The size of the icon ({@code size}). */
        @SerializedName("size")
        private Integer size;
        /** The anchor of the icon, in pixels ({@code anchor}). */
        @SerializedName("anchor")
        private List<Integer> anchor;
        /** The anchor of the popup, in pixels ({@code popupAnchor}). */
        @SerializedName("popupAnchor")
        private List<Integer> popupAnchor;
        /** The rotation of the icon, in degrees ({@code angle}). */
        @SerializedName("angle")
        private Integer angle;
        /** The text of the popup ({@code info}). */
        @SerializedName("info")
        private String info;
    }

    /** An expected arrival window of a {@link FindAssetResponse}. */
    @Data
    public static class Arrival {
        /** The start of the window, as a Unix timestamp ({@code from_unix_timestamp}). */
        @SerializedName("from_unix_timestamp")
        private Long fromUnixTimestamp;
        /** The end of the window, as a Unix timestamp ({@code to_unix_timestamp}). */
        @SerializedName("to_unix_timestamp")
        private Long toUnixTimestamp;
    }
}

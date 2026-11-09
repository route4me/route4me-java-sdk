package com.route4me.sdk.services.routedestinations;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.services.facilities.FacilityCoordinates;
import lombok.Data;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A facility linked to a route destination.
 *
 * <p>The destinations spec types {@code facility_id} as an integer and {@code coordinates} as a {@code "lat,lng"}
 * string, while the facilities API and the C# SDK use a 32-character hex ID and a {@code {"lat", "lng"}} object.
 * Both shapes are read.
 */
@Data
public class FacilityDataResource {
    /**
     * The facility ID ({@code facility_id}): a 32-character hex ID, or the text of a numeric ID when the API sends a
     * number.
     */
    @SerializedName("facility_id")
    private String facilityId;

    /** Human-readable alias for the facility (e.g., "Main Warehouse") ({@code facility_alias}). */
    @SerializedName("facility_alias")
    private String facilityAlias;

    /** Status label for the facility (e.g., "Active") ({@code status}). */
    @SerializedName("status")
    private String status;

    /** The status code of the facility, as text; the API may send it as a number or a string ({@code status_code}). */
    @SerializedName("status_code")
    private String statusCode;

    /**
     * The coordinates of the facility ({@code coordinates}), read from a {@code {"lat", "lng"}} object or from a
     * {@code "lat,lng"} string; {@code null} when the text is not two numbers or the API sends an empty array.
     */
    @SerializedName("coordinates")
    @JsonAdapter(CoordinatesReader.class)
    private FacilityCoordinates coordinates;

    /** Street address of the facility ({@code address}). */
    @SerializedName("address")
    private String address;

    /** List of facility types associated with this facility ({@code facility_types}). */
    @SerializedName("facility_types")
    private List<FacilityType> facilityTypes;

    /**
     * Reads {@code coordinates} from a {@code {"lat", "lng"}} object or a {@code "lat,lng"} string, and an empty array
     * as {@code null}. Writing is Gson's own.
     */
    static final class CoordinatesReader implements JsonDeserializer<FacilityCoordinates> {
        @Override
        public FacilityCoordinates deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
            if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
                return parse(json.getAsString());
            }
            if (json.isJsonArray() && json.getAsJsonArray().size() == 0) {
                return null;
            }
            return context.deserialize(json, FacilityCoordinates.class);
        }

        /**
         * Reads {@code "lat,lng"}.
         *
         * @param text the text
         * @return the coordinates, or {@code null} when the text is not two numbers separated by a comma
         */
        private static FacilityCoordinates parse(String text) {
            String[] parts = text.split(",");
            if (parts.length != 2) {
                return null;
            }
            try {
                FacilityCoordinates coordinates = new FacilityCoordinates();
                coordinates.setLat(Double.valueOf(parts[0].trim()));
                coordinates.setLng(Double.valueOf(parts[1].trim()));
                return coordinates;
            } catch (NumberFormatException ex) {
                return null;
            }
        }
    }
}

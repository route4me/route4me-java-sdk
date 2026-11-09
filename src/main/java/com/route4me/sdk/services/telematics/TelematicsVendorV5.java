package com.route4me.sdk.services.telematics;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/**
 * One telematics vendor, as {@link TelematicsManager#getTelematicsVendorV5(long)} returns it.
 */
@Data
public class TelematicsVendorV5 {

    /** The vendor ID. */
    @SerializedName("id")
    private Long id;
    /** Whether the vendor is enabled. */
    @SerializedName("is_enabled")
    private Boolean isEnabled;
    /** Whether the vendor is read-only. */
    @SerializedName("is_readonly")
    private Boolean isReadonly;
    /** The internal vendor alias, such as {@code tomtom}. */
    @SerializedName("name")
    private String name;
    /** The display name. */
    @SerializedName("title")
    private String title;
    /** The icon URL. */
    @SerializedName("icon")
    private String icon;
    /** A hint about connecting to the vendor. */
    @SerializedName("hint")
    private String hint;
    /** When the vendor was created. */
    @SerializedName("created_at")
    private String createdAt;
    /** When the vendor was last updated. */
    @SerializedName("updated_at")
    private String updatedAt;
    /** The entities that can be synchronised from the vendor. */
    @SerializedName("sync_entities")
    private List<SyncEntity> syncEntities;
    /** The credentials a connection to the vendor needs. */
    @SerializedName("credentials")
    private List<Credential> credentials;

    /** An entity that can be synchronised from a vendor, such as vehicles. */
    @Data
    public static class SyncEntity {
        /** The entity ID. */
        @SerializedName("id")
        private String id;
        /** The entity alias. */
        @SerializedName("alias")
        private String alias;
        /** The display name. */
        @SerializedName("title")
        private String title;
        /** Whether the entity is synchronised by default. */
        @SerializedName("default_value")
        private Boolean defaultValue;
    }

    /** A credential field of a vendor connection. It describes the field; it holds no secret. */
    @Data
    public static class Credential {
        /** The field name, such as {@code api_key}. */
        @SerializedName("name")
        private String name;
        /** Whether the field is required. */
        @SerializedName("is_required")
        private Boolean isRequired;
        /** The default value of the field, if any. */
        @SerializedName("default")
        private String defaultValue;
    }
}

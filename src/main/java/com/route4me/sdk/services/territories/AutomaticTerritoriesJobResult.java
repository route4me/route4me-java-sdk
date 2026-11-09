package com.route4me.sdk.services.territories;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** The answer of {@link TerritoriesManagerV5#getAutomaticTerritoriesJobResult(String)}: the clusters of a job. */
@Data
public class AutomaticTerritoriesJobResult {
    /** The job ID ({@code job_id}). */
    @SerializedName("job_id")
    private String jobId;
    /** The clusters, one per territory ({@code clusters}). */
    @SerializedName("clusters")
    private List<Cluster> clusters;

    /** One cluster of addresses. */
    @Data
    public static class Cluster {
        /** The cluster ID ({@code cluster_id}). */
        @SerializedName("cluster_id")
        private String clusterId;
        /** The number of addresses in the cluster ({@code addresses_count}). */
        @SerializedName("addresses_count")
        private Integer addressesCount;
        /** The center of the cluster ({@code centroid}). */
        @SerializedName("centroid")
        private Centroid centroid;
        /** The shape of the cluster ({@code shape}). */
        @SerializedName("shape")
        private Shape shape;
    }

    /** The center of a {@link Cluster}. */
    @Data
    public static class Centroid {
        /** Latitude ({@code lat}). */
        @SerializedName("lat")
        private Double lat;
        /** Longitude ({@code lng}). */
        @SerializedName("lng")
        private Double lng;
    }

    /** The shape of a {@link Cluster}, as a GeoJSON geometry. */
    @Data
    public static class Shape {
        /** The GeoJSON geometry ({@code geometry}). */
        @SerializedName("geometry")
        private Map<String, Object> geometry;
        /** The GeoJSON type ({@code type}). */
        @SerializedName("type")
        private String type;
    }
}

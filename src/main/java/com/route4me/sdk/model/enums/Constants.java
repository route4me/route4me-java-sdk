package com.route4me.sdk.model.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author juan
 */
public class Constants {

    public enum AlgorithmType {

        TSP("1"),
        VRP("2"),
        CVRP_TW_SD("3"),
        CVRP_TW_MD("4"),
        TSP_TW("5"),
        TSP_TW_CR("6"),
        BBCVRP("7");

        private String value;

        private AlgorithmType(String value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }

    }

    public enum TypeofMatrix {

        R4M_PROPRIETARY_ROUTING("1"),
        R4M_TRAFFIC_ENGINE("3"),
        TRUCKING("6");

        private String value;

        private TypeofMatrix(String value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }

    }

    
    public enum DirectionsMethod {

        R4M_PROPRIETARY_INTERNAL_NAVIGATION_SYSTEM("1"),
        TRUCKING("3");

        private String value;

        private DirectionsMethod(String value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }

    }
    
    
    
    public enum TravelMode {

        DRIVING("Driving"),
        WALKING("Walking"),
        TRUCKING("Trucking");

        private final String value;

        TravelMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }

    public enum DistanceUnit {

        MI("mi"),
        KM("km");

        private String value;

        private DistanceUnit(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }

    public enum Avoid {

        HIGHWAYS("Highways"),
        TOLLS("Tolls"),
        MINIMIZE_HIGHWAYS("minimizeHighways"),
        MINIMIZE_TOLLS("minimizeTolls"),
        NONE("");

        private String value;

        private Avoid(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public enum Optimize {

        DISTANCE("Distance"),
        TIME("Time"),
        TIME_WITH_TRAFFIC("timeWithTraffic");

        private String value;

        private Optimize(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public enum Metric {

        ROUTE4ME_METRIC_EUCLIDEAN(1),
        ROUTE4ME_METRIC_MANHATTAN(2),
        ROUTE4ME_METRIC_GEODESIC(3),
        ROUTE4ME_METRIC_MATRIX(4),
        ROUTE4ME_METRIC_EXACT_2D(5);

        private int value;
        private static final Map<Integer, Metric> lookup
                = new HashMap<>();

        static {
            for (Metric s : EnumSet.allOf(Metric.class)) {
                lookup.put(s.getValue(), s);
            }
        }

        private Metric(int value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(int value) {
            this.value = value;
        }

        public static Metric get(int code) {
            return lookup.get(code);
        }

    }

    public enum DeviceType {

        WEB("web"),
        IPHONE("iphone"),
        IPAD("ipad"),
        ANDROID_PHONE("android_phone"),
        ANDROID_TABLET("android_tablet");

        private String value;

        private DeviceType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public enum Format {

        CSV("csv"),
        SERIALIZED("serialized"),
        XML("xml");

        private String value;

        private Format(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public enum OptimizationState {

        OPTIMIZATION_STATE_INITIAL(1),
        OPTIMIZATION_STATE_MATRIX_PROCESSING(2),
        OPTIMIZATION_STATE_OPTIMIZING(3),
        OPTIMIZATION_STATE_OPTIMIZED(4),
        OPTIMIZATION_STATE_ERROR(5),
        OPTIMIZATION_STATE_COMPUTING_DIRECTIONS(6);
        private int value;
        private static final Map<Integer, OptimizationState> lookup
                = new HashMap<>();

        static {
            for (OptimizationState s : EnumSet.allOf(OptimizationState.class)) {
                lookup.put(s.getValue(), s);
            }
        }

        private OptimizationState(int value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(int value) {
            this.value = value;
        }

        public static OptimizationState get(int code) {
            return lookup.get(code);
        }

    }

    public enum RoutePathOutput {

        NONE("None"),
        POINTS("Points");

        private String value;

        private RoutePathOutput(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}

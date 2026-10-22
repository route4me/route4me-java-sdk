package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Value enums of the V4 routing and optimization API. Each constant carries the value the API uses;
 * {@code toString()} (and {@code getValue()} where present) returns it.
 */
public class Constants {

    /** The types of member (user) of an account ({@code member_type}). */
    public enum MemberType {
        /** The account owner ({@code PRIMARY_ACCOUNT}). */
        PRIMARY_ACCOUNT("PRIMARY_ACCOUNT"),
        /** An administrator ({@code SUB_ACCOUNT_ADMIN}). */
        SUB_ACCOUNT_ADMIN("SUB_ACCOUNT_ADMIN"),
        /** A regional manager ({@code SUB_ACCOUNT_REGIONAL_MANAGER}). */
        SUB_ACCOUNT_REGIONAL_MANAGER("SUB_ACCOUNT_REGIONAL_MANAGER"),
        /** A dispatcher ({@code SUB_ACCOUNT_DISPATCHER}). */
        SUB_ACCOUNT_DISPATCHER("SUB_ACCOUNT_DISPATCHER"),
        /** A route planner ({@code SUB_ACCOUNT_PLANNER}). */
        SUB_ACCOUNT_PLANNER("SUB_ACCOUNT_PLANNER"),
        /** A driver ({@code SUB_ACCOUNT_DRIVER}). */
        SUB_ACCOUNT_DRIVER("SUB_ACCOUNT_DRIVER");

        private final String value;

        MemberType(String value) {
            this.value = value;
        }

        /**
         * Returns the API value, the same as {@link #getValue()}.
         *
         * @return the value, such as {@code SUB_ACCOUNT_DRIVER}
         */
        @Override
        public String toString() {
            return this.value;
        }

        /**
         * Returns the API value.
         *
         * @return the value, such as {@code SUB_ACCOUNT_DRIVER}
         */
        public String getValue() {
            return value;
        }

    }

    /** The values of the boolean member properties, which the API takes as text. */
    public enum MemberPropertyValue {
        /** True ({@code TRUE}). */
        TRUE("TRUE"),
        /** False ({@code FALSE}). */
        FALSE("FALSE");

        private final String value;

        MemberPropertyValue(String value) {
            this.value = value;
        }

        /**
         * Returns the API value, the same as {@link #getValue()}.
         *
         * @return the value, such as {@code TRUE}
         */
        @Override
        public String toString() {
            return this.value;
        }

        /**
         * Returns the API value.
         *
         * @return the value, such as {@code TRUE}
         */
        public String getValue() {
            return value;
        }
    }

    /** The optimization algorithms ({@code algorithm_type}). */
    public enum AlgorithmType {

        /** One route: traveling salesman ({@code 1}). */
        @SerializedName("1")
        TSP("1"),
        /** Several routes: vehicle routing ({@code 2}). */
        @SerializedName("2")
        VRP("2"),
        /** Several routes from one depot, with capacities and time windows ({@code 3}). */
        @SerializedName("3")
        CVRP_TW_SD("3"),
        /** Several routes from several depots, with capacities and time windows ({@code 4}). */
        @SerializedName("4")
        CVRP_TW_MD("4"),
        /** One route with time windows ({@code 5}). */
        @SerializedName("5")
        TSP_TW("5"),
        /** The {@code TSP_TW_CR} variant of one route with time windows ({@code 6}). */
        @SerializedName("6")
        TSP_TW_CR("6"),
        /** The {@code BBCVRP} variant of several routes with capacities ({@code 7}). */
        @SerializedName("7")
        BBCVRP("7"),
        /** Advanced constraints; the API sets it when {@code advanced_constraints} is not empty ({@code 9}). */
        @SerializedName("9")
        ADVANCED_CVRP_TW("9"),
        /** Balanced team routes; it needs more than 10 stops ({@code 101}). */
        @SerializedName("101")
        ALG_LEGACY_DISTRIBUTED("101"),
        /** No optimization: the stops keep the given order ({@code 100}). */
        @SerializedName("100")
        ALG_NONE("100");

        private final String value;

        AlgorithmType(String value) {
            this.value = value;
        }

        /**
         * Returns the API value.
         *
         * @return the value, such as {@code 1}
         */
        public String getValue() {
            return value;
        }

        /**
         * Returns the API value, the same as {@link #getValue()}.
         *
         * @return the value, such as {@code 1}
         */
        @Override
        public String toString() {
            return this.value;
        }

    }

    /** The road data the distance matrix of an optimization uses. */
    public enum TypeofMatrix {

        /** Route4Me road routing ({@code 1}). */
        @SerializedName("1")
        R4M_PROPRIETARY_ROUTING("1"),
        /** Route4Me road routing with traffic ({@code 3}). */
        @SerializedName("3")
        R4M_TRAFFIC_ENGINE("3"),
        /** Truck routing ({@code 6}). */
        @SerializedName("6")
        TRUCKING("6");

        private final String value;

        TypeofMatrix(String value) {
            this.value = value;
        }

        /**
         * Returns the API value.
         *
         * @return the value, such as {@code 1}
         */
        public String getValue() {
            return value;
        }

        /**
         * Returns the API value, the same as {@link #getValue()}.
         *
         * @return the value, such as {@code 1}
         */
        @Override
        public String toString() {
            return this.value;
        }

    }

    /** The way the driving directions of a route are computed. */
    public enum DirectionsMethod {

        /** Route4Me navigation ({@code 1}). */
        @SerializedName("1")
        R4M_PROPRIETARY_INTERNAL_NAVIGATION_SYSTEM("1"),
        /** Truck navigation ({@code 3}). */
        @SerializedName("3")
        TRUCKING("3");

        private final String value;

        DirectionsMethod(String value) {
            this.value = value;
        }

        /**
         * Returns the API value.
         *
         * @return the value, such as {@code 1}
         */
        public String getValue() {
            return value;
        }

        /**
         * Returns the API value, the same as {@link #getValue()}.
         *
         * @return the value, such as {@code 1}
         */
        @Override
        public String toString() {
            return this.value;
        }

    }

    /** The travel modes for distances, travel times and directions ({@code travel_mode}). */
    public enum TravelMode {

        /** Driving ({@code Driving}). */
        DRIVING("Driving"),
        /** Walking ({@code Walking}). */
        WALKING("Walking"),
        /** Bicycling ({@code Bicycling}). */
        BICYCLING("Bicycling"),
        /** Truck driving ({@code Trucking}). */
        TRUCKING("Trucking");

        private final String value;

        TravelMode(String value) {
            this.value = value;
        }

        /**
         * Returns the API value.
         *
         * @return the value, such as {@code Driving}
         */
        @Override
        public String toString() {
            return this.value;
        }

    }

    /** The distance units of a route ({@code distance_unit}). */
    public enum DistanceUnit {

        /** Miles ({@code mi}). */
        MI("mi"),
        /** Kilometers ({@code km}). */
        KM("km");

        private final String value;

        DistanceUnit(String value) {
            this.value = value;
        }

        /**
         * Returns the API value.
         *
         * @return the value, such as {@code mi}
         */
        @Override
        public String toString() {
            return this.value;
        }

    }

    /** The road types a route avoids ({@code avoid}). */
    public enum Avoid {

        /** Highways ({@code Highways}). */
        HIGHWAYS("Highways"),
        /** Toll roads ({@code Tolls}). */
        TOLLS("Tolls"),
        /** Use highways as little as possible ({@code minimizeHighways}). */
        MINIMIZE_HIGHWAYS("minimizeHighways"),
        /** Use toll roads as little as possible ({@code minimizeTolls}). */
        MINIMIZE_TOLLS("minimizeTolls"),
        /** Highways and toll roads ({@code highways,tolls}). */
        HIGHWAYS_TOLLS("highways,tolls"),
        /** Nothing to avoid (the empty string). */
        NONE("");

        private final String value;

        Avoid(String value) {
            this.value = value;
        }

        /**
         * Returns the API value.
         *
         * @return the value, such as {@code Tolls}
         */
        @Override
        public String toString() {
            return this.value;
        }
    }

    /** What the routes are optimized for ({@code optimize}). */
    public enum Optimize {

        /** The shortest distance ({@code Distance}). */
        DISTANCE("Distance"),
        /** The shortest time ({@code Time}). */
        TIME("Time"),
        /** The shortest time with traffic ({@code timeWithTraffic}). */
        TIME_WITH_TRAFFIC("timeWithTraffic");

        private final String value;

        Optimize(String value) {
            this.value = value;
        }

        /**
         * Returns the API value.
         *
         * @return the value, such as {@code Distance}
         */
        @Override
        public String toString() {
            return this.value;
        }
    }

    /** How distances between stops are measured ({@code metric}). */
    public enum Metric {

        /** Euclidean distance ({@code 1}). */
        ROUTE4ME_METRIC_EUCLIDEAN(1),
        /** Manhattan distance ({@code 2}). */
        ROUTE4ME_METRIC_MANHATTAN(2),
        /** Geodesic distance, a straight line on the globe ({@code 3}). */
        ROUTE4ME_METRIC_GEODESIC(3),
        /** Road network distance ({@code 4}). */
        ROUTE4ME_METRIC_MATRIX(4),
        /** Exact 2D distance ({@code 5}). */
        ROUTE4ME_METRIC_EXACT_2D(5);

        private final int value;
        private static final Map<Integer, Metric> LOOKUP
                = new HashMap<>();

        static {
            for (Metric s : EnumSet.allOf(Metric.class)) {
                LOOKUP.put(s.getValue(), s);
            }
        }

        Metric(int value) {
            this.value = value;
        }

        /**
         * Returns the API code.
         *
         * @return the code, such as {@code 4}
         */
        public int getValue() {
            return value;
        }

        /**
         * Returns the constant of a code.
         *
         * @param code the API code
         * @return the constant, or {@code null} for an unknown code
         */
        public static Metric get(int code) {
            return LOOKUP.get(code);
        }

    }

    /** The types of device that create optimizations, notes and positions ({@code device_type}). */
    public enum DeviceType {

        /** A web browser ({@code web}). */
        @SerializedName("web")
        WEB("web"),
        /** An iPhone ({@code iphone}). */
        @SerializedName("iphone")
        IPHONE("iphone"),
        /** An iPad ({@code ipad}). */
        @SerializedName("ipad")
        IPAD("ipad"),
        /** An Android phone ({@code android_phone}). */
        @SerializedName("android_phone")
        ANDROID_PHONE("android_phone"),
        /** An Android tablet ({@code android_tablet}). */
        @SerializedName("android_tablet")
        ANDROID_TABLET("android_tablet");

        private final String value;

        DeviceType(String value) {
            this.value = value;
        }

        /**
         * Returns the API value.
         *
         * @return the value, such as {@code web}
         */
        @Override
        public String toString() {
            return this.value;
        }
    }

    /** The formats of an API answer ({@code format}). */
    public enum Format {

        /** CSV ({@code csv}). */
        CSV("csv"),
        /** Serialized PHP ({@code serialized}). */
        SERIALIZED("serialized"),
        /** XML ({@code xml}). */
        XML("xml");

        private final String value;

        Format(String value) {
            this.value = value;
        }

        /**
         * Returns the API value.
         *
         * @return the value, such as {@code csv}
         */
        @Override
        public String toString() {
            return this.value;
        }
    }

    /** The states of an optimization ({@code state}). */
    public enum OptimizationState {

        /** Created ({@code 1}). */
        OPTIMIZATION_STATE_INITIAL(1),
        /** Computing the distance matrix ({@code 2}). */
        OPTIMIZATION_STATE_MATRIX_PROCESSING(2),
        /** Optimizing ({@code 3}). */
        OPTIMIZATION_STATE_OPTIMIZING(3),
        /** Optimized ({@code 4}). */
        OPTIMIZATION_STATE_OPTIMIZED(4),
        /** Failed ({@code 5}). */
        OPTIMIZATION_STATE_ERROR(5),
        /** Computing the directions ({@code 6}). */
        OPTIMIZATION_STATE_COMPUTING_DIRECTIONS(6);
        private final int value;
        private static final Map<Integer, OptimizationState> LOOKUP
                = new HashMap<>();

        static {
            for (OptimizationState s : EnumSet.allOf(OptimizationState.class)) {
                LOOKUP.put(s.getValue(), s);
            }
        }

        OptimizationState(int value) {
            this.value = value;
        }

        /**
         * Returns the API code.
         *
         * @return the code, such as {@code 4}
         */
        public int getValue() {
            return value;
        }

        /**
         * Returns the constant of a code.
         *
         * @param code the API code
         * @return the constant, or {@code null} for an unknown code
         */
        public static OptimizationState get(int code) {
            return LOOKUP.get(code);
        }

    }

    /** Whether a route answer includes the path points of the route ({@code route_path_output}). */
    public enum RoutePathOutput {

        /** No path ({@code None}). */
        NONE("None"),
        /** The path points ({@code Points}). */
        POINTS("Points");

        private final String value;

        RoutePathOutput(String value) {
            this.value = value;
        }

        /**
         * Returns the API value.
         *
         * @return the value, such as {@code Points}
         */
        @Override
        public String toString() {
            return this.value;
        }
    }

    /** Whether routes may make U-turns ({@code uturn}). */
    public enum UTurn {

        /** Allow U-turns ({@code 1}). */
        UTURN_DEPART_SHORTEST(1),
        /** Avoid U-turns ({@code 2}). */
        UTURN_DEPART_TO_RIGHT(2);

        private final int value;
        private static final Map<Integer, UTurn> LOOKUP
                = new HashMap<>();

        static {
            for (UTurn s : EnumSet.allOf(UTurn.class)) {
                LOOKUP.put(s.getValue(), s);
            }
        }

        UTurn(int value) {
            this.value = value;
        }

        /**
         * Returns the API code.
         *
         * @return the code, such as {@code 1}
         */
        public int getValue() {
            return value;
        }

        /**
         * Returns the constant of a code.
         *
         * @param code the API code
         * @return the constant, or {@code null} for an unknown code
         */
        public static UTurn get(int code) {
            return LOOKUP.get(code);
        }

    }

    /** Whether routes may turn left ({@code leftturn}). */
    public enum LeftTurn {

        /** Allow left turns ({@code 1}). */
        LEFTTURN_ALLOW(1),
        /** Avoid left turns ({@code 2}). */
        LEFTTURN_FORBID(2),
        /** Allow approaching a stop from either side ({@code 3}). */
        LEFTTURN_MULTIAPPROACH(3);

        private final int value;
        private static final Map<Integer, LeftTurn> LOOKUP
                = new HashMap<>();

        static {
            for (LeftTurn s : EnumSet.allOf(LeftTurn.class)) {
                LOOKUP.put(s.getValue(), s);
            }
        }

        LeftTurn(int value) {
            this.value = value;
        }

        /**
         * Returns the API code.
         *
         * @return the code, such as {@code 1}
         */
        public int getValue() {
            return value;
        }

        /**
         * Returns the constant of a code.
         *
         * @param code the API code
         * @return the constant, or {@code null} for an unknown code
         */
        public static LeftTurn get(int code) {
            return LOOKUP.get(code);
        }

    }

    /** The hazardous goods classes a truck may carry ({@code truck_hazardous_goods}). */
    public enum TruckHazardousGoods {

        /** No hazardous goods (the empty string). */
        NONE(""),
        /** Explosives ({@code explosive}). */
        EXPLOSIVE("explosive"),
        /** Gas ({@code gas}). */
        GAS("gas"),
        /** Flammable goods ({@code flammable}). */
        FLAMMABLE("flammable"),
        /** Combustible goods ({@code combustible}). */
        COMBUSTIBLE("combustible"),
        /** Organic goods ({@code organic}). */
        ORGANIC("organic"),
        /** Poison ({@code poison}). */
        POISON("poison"),
        /** Radioactive goods ({@code radioActive}). */
        RADIOACTIVE("radioActive"),
        /** Corrosive goods ({@code corrosive}). */
        CORROSIVE("corrosive"),
        /** Goods poisonous by inhalation ({@code poisonousInhalation}). */
        POISONOUSINHALATION("poisonousInhalation"),
        /** Goods harmful to water ({@code harmfulToWater}). */
        HARMFULTOWATER("harmfulToWater"),
        /** Other hazardous goods ({@code other}). */
        OTHER("other"),
        /** Every class of hazardous goods ({@code allHazardousGoods}). */
        ALLHAZARDOUSGOODS("allHazardousGoods");

        private final String value;

        TruckHazardousGoods(String value) {
            this.value = value;
        }

        /**
         * Returns the API value.
         *
         * @return the value, such as {@code flammable}
         */
        @Override
        public String toString() {
            return this.value;
        }
    }

    /** The shapes of a territory ({@code territory.type}). */
    public enum TerritoryType {

        /** A circle ({@code circle}). */
        CIRCLE("circle"),
        /** A polygon ({@code poly}). */
        POLY("poly"),
        /** A rectangle ({@code rect}). */
        RECT("rect");

        private final String value;

        TerritoryType(String value) {
            this.value = value;
        }

        /**
         * Returns the API value.
         *
         * @return the value, such as {@code poly}
         */
        @Override
        public String toString() {
            return this.value;
        }
    }

    /** Whether routes may turn right ({@code rightturn}). */
    public enum RightTurn {

        /** Allow right turns ({@code 1}). */
        RIGHTTURN_ALLOW(1),
        /** Avoid right turns ({@code 2}). */
        RIGHTTURN_FORBID(2),
        /** Allow approaching a stop from either side ({@code 3}). */
        RIGHTTURN_MULTIAPPROACH(3);

        private final int value;
        private static final Map<Integer, RightTurn> LOOKUP
                = new HashMap<>();

        static {
            for (RightTurn s : EnumSet.allOf(RightTurn.class)) {
                LOOKUP.put(s.getValue(), s);
            }
        }

        RightTurn(int value) {
            this.value = value;
        }

        /**
         * Returns the API code.
         *
         * @return the code, such as {@code 1}
         */
        public int getValue() {
            return value;
        }

        /**
         * Returns the constant of a code.
         *
         * @param code the API code
         * @return the constant, or {@code null} for an unknown code
         */
        public static RightTurn get(int code) {
            return LOOKUP.get(code);
        }

    }

    /** The types of stop of an address ({@code address_stop_type}). */
    public enum AddressStopType {
        /** A delivery ({@code DELIVERY}). */
        DELIVERY("DELIVERY"),
        /** A pickup ({@code PICKUP}). */
        PICKUP("PICKUP"),
        /** A break ({@code BREAK}). */
        BREAK("BREAK"),
        /** A meeting ({@code MEETUP}). */
        MEETUP("MEETUP"),
        /** A service visit ({@code SERVICE}). */
        SERVICE("SERVICE"),
        /** A visit ({@code VISIT}). */
        VISIT("VISIT"),
        /** A drive-by ({@code DRIVEBY}). */
        DRIVEBY("DRIVEBY");

        private final String value;

        AddressStopType(String value) {
            this.value = value;
        }

        /**
         * Returns the API value, the same as {@link #getValue()}.
         *
         * @return the value, such as {@code DELIVERY}
         */
        @Override
        public String toString() {
            return this.value;
        }

        /**
         * Returns the API value.
         *
         * @return the value, such as {@code DELIVERY}
         */
        public String getValue() {
            return value;
        }

    }

}

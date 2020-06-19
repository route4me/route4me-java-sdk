package com.route4me.sdk.services.routing;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author juan
 */
public class Constants {

    public enum MemberType {
        PRIMARY_ACCOUNT("PRIMARY_ACCOUNT"),
        SUB_ACCOUNT_ADMIN("SUB_ACCOUNT_ADMIN"),
        SUB_ACCOUNT_REGIONAL_MANAGER("SUB_ACCOUNT_REGIONAL_MANAGER"),
        SUB_ACCOUNT_DISPATCHER("SUB_ACCOUNT_DISPATCHER"),
        SUB_ACCOUNT_PLANNER("SUB_ACCOUNT_PLANNER"),
        SUB_ACCOUNT_DRIVER("SUB_ACCOUNT_DRIVER");

        private String value;

        private MemberType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
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

    public enum MemberPropertyValue {
        TRUE("TRUE"),
        FALSE("FALSE");

        private String value;

        private MemberPropertyValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
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

    public enum AlgorithmType {

        TSP("1"),
        VRP("2"),
        CVRP_TW_SD("3"),
        CVRP_TW_MD("4"),
        TSP_TW("5"),
        TSP_TW_CR("6"),
        BBCVRP("7"),
        ALG_LEGACY_DISTRIBUTED("101"),
        ALG_NONE("100");

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

    public enum UTurn {

        UTURN_DEPART_SHORTEST(1),
        UTURN_DEPART_TO_RIGHT(2);

        private int value;
        private static final Map<Integer, UTurn> lookup
                = new HashMap<>();

        static {
            for (UTurn s : EnumSet.allOf(UTurn.class)) {
                lookup.put(s.getValue(), s);
            }
        }

        private UTurn(int value) {
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

        public static UTurn get(int code) {
            return lookup.get(code);
        }

    }

    public enum LeftTurn {

        LEFTTURN_ALLOW(1),
        LEFTTURN_FORBID(2),
        LEFTTURN_MULTIAPPROACH(3);

        private int value;
        private static final Map<Integer, LeftTurn> lookup
                = new HashMap<>();

        static {
            for (LeftTurn s : EnumSet.allOf(LeftTurn.class)) {
                lookup.put(s.getValue(), s);
            }
        }

        private LeftTurn(int value) {
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

        public static LeftTurn get(int code) {
            return lookup.get(code);
        }

    }

    public enum TruckHazardousGoods {

        NONE(""),
        EXPLOSIVE("explosive"),
        GAS("gas"),
        FLAMMABLE("flammable"),
        COMBUSTIBLE("combustible"),
        ORGANIC("organic"),
        POISON("poison"),
        RADIOACTIVE("radioActive"),
        CORROSIVE("corrosive"),
        POISONOUSINHALATION("poisonousInhalation"),
        HARMFULTOWATER("harmfulToWater"),
        OTHER("other"),
        ALLHAZARDOUSGOODS("allHazardousGoods");

        private String value;

        private TruckHazardousGoods(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public enum TerritoryType {

        CIRCLE("circle"),
        POLY("poly"),
        RECT("rect");

        private String value;

        private TerritoryType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public enum RightTurn {

        RIGHTTURN_ALLOW(1),
        RIGHTTURN_FORBID(2),
        RIGHTTURN_MULTIAPPROACH(3);

        private int value;
        private static final Map<Integer, RightTurn> lookup
                = new HashMap<>();

        static {
            for (RightTurn s : EnumSet.allOf(RightTurn.class)) {
                lookup.put(s.getValue(), s);
            }
        }

        private RightTurn(int value) {
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

        public static RightTurn get(int code) {
            return lookup.get(code);
        }

    }

    public enum AddressStopType {
        DELIVERY("DELIVERY"),
        PICKUP("PICKUP"),
        BREAK("BREAK"),
        MEETUP("MEETUP"),
        SERVICE("SERVICE"),
        VISIT("VISIT"),
        DRIVEBY("DRIVEBY");

        private String value;

        private AddressStopType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
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

}

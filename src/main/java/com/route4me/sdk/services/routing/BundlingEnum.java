/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * The modes of address bundling ({@link Bundling}): how stops are grouped, and how the service time and
 * the destinations of a bundle are computed.
 */
public class BundlingEnum {

    /** What makes stops part of the same bundle ({@code bundling.mode}). */
    public enum BundlingMode {
        /** The same address ({@code 1}, the default). */
        BUNDLING_BY_ADDRESS(1),
        /** The same coordinates ({@code 2}). */
        BUNDLING_BY_COORDINATES(2),
        /** The same value of one address field ({@code 3}). */
        BUNDLING_BY_ANY_STATIC_ADDRESS_FIELD(3),
        /** The same value of one custom field ({@code 4}). */
        BUNDLING_BY_ANY_CUSTOM_DATA(4),
        /** The same values of several fields ({@code 5}). */
        BUNDLING_BY_MULTIPLE_FIELDS(5);

        private final int value;
        private static final Map<Integer, BundlingEnum.BundlingMode> LOOKUP
                = new HashMap<>();

        static {
            for (BundlingEnum.BundlingMode s : EnumSet.allOf(BundlingEnum.BundlingMode.class)) {
                LOOKUP.put(s.getValue(), s);
            }
        }

        BundlingMode(int value) {
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
        public static BundlingEnum.BundlingMode get(int code) {
            return LOOKUP.get(code);
        }

    }

    /** The service time of the first stop of a bundle ({@code first_item_mode}). */
    public enum BundlingFirstItemMode {
        /** The stop keeps its own service time ({@code 1}). */
        KEEP_ORIGINAL_SERVICE_TIME(1),
        /** A custom time, from {@code first_item_mode_params} ({@code 2}). */
        USE_CUSTOM_SERVICE_TIME(2);

        private final int value;
        private static final Map<Integer, BundlingEnum.BundlingFirstItemMode> LOOKUP
                = new HashMap<>();

        static {
            for (BundlingEnum.BundlingFirstItemMode s : EnumSet.allOf(BundlingEnum.BundlingFirstItemMode.class)) {
                LOOKUP.put(s.getValue(), s);
            }
        }

        BundlingFirstItemMode(int value) {
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
        public static BundlingEnum.BundlingFirstItemMode get(int code) {
            return LOOKUP.get(code);
        }

    }

    /** The service time of the other stops of a bundle ({@code additional_items_mode}). */
    public enum BundlingAdditionalItemMode {
        /**
         * Code {@code 1}. The V4 API spec describes it as "use the first stop's service time"; the name of the constant
         * says the stops keep their own.
         */
        KEEP_ORIGINAL_SERVICE_TIME_FOR_ADDITIONAL_ITEM(1),
        /** A custom time, from {@code additional_items_mode_params} ({@code 2}). */
        USE_CUSTOM_SERVICE_TIME_FOR_ADDITIONAL_ITEM(2),
        /**
         * Code {@code 3}. The V4 API spec describes it as "keep each stop's own service time"; the name of the constant
         * says no service time is added.
         */
        DO_NOT_ADD_SERVICE_TIME_FOR_ADDITIONAL_ITEM(3);

        private final int value;
        private static final Map<Integer, BundlingEnum.BundlingAdditionalItemMode> LOOKUP
                = new HashMap<>();

        static {
            for (BundlingEnum.BundlingAdditionalItemMode s
                    : EnumSet.allOf(BundlingEnum.BundlingAdditionalItemMode.class)) {
                LOOKUP.put(s.getValue(), s);
            }
        }

        BundlingAdditionalItemMode(int value) {
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
        public static BundlingEnum.BundlingAdditionalItemMode get(int code) {
            return LOOKUP.get(code);
        }

    }

    /** What becomes of the stops of a bundle in the routes ({@code merge_mode}). */
    public enum BundledItemsMode {

        /** They stay separate destinations ({@code 1}, the default). */
        KEEP_AS_SEPARATE_DESTINATIONS(1),
        /** They are merged into one destination ({@code 2}). */
        MERGE_INTO_SINGLE_DESTINATION(2);

        private final int value;
        private static final Map<Integer, BundlingEnum.BundledItemsMode> LOOKUP
                = new HashMap<>();

        static {
            for (BundlingEnum.BundledItemsMode s : EnumSet.allOf(BundlingEnum.BundledItemsMode.class)) {
                LOOKUP.put(s.getValue(), s);
            }
        }

        BundledItemsMode(int value) {
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
        public static BundlingEnum.BundledItemsMode get(int code) {
            return LOOKUP.get(code);
        }

    }

}

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
 *
 * @author route4me
 */
public class BundlingEnum {

    public enum BundlingMode {
        BUNDLING_BY_ADDRESS(1),
        BUNDLING_BY_COORDINATES(2),
        BUNDLING_BY_ANY_STATIC_ADDRESS_FIELD(3),
        BUNDLING_BY_ANY_CUSTOM_DATA(4);

        private int value;
        private static final Map<Integer, BundlingEnum.BundlingMode> lookup
                = new HashMap<>();

        static {
            for (BundlingEnum.BundlingMode s : EnumSet.allOf(BundlingEnum.BundlingMode.class)) {
                lookup.put(s.getValue(), s);
            }
        }

        private BundlingMode(int value) {
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

        public static BundlingEnum.BundlingMode get(int code) {
            return lookup.get(code);
        }

    }



    public enum BundlingFirstItemMode {
        KEEP_ORIGINAL_SERVICE_TIME(1),
        USE_CUSTOM_SERVICE_TIME(2);

        private int value;
        private static final Map<Integer, BundlingEnum.BundlingFirstItemMode> lookup
                = new HashMap<>();

        static {
            for (BundlingEnum.BundlingFirstItemMode s : EnumSet.allOf(BundlingEnum.BundlingFirstItemMode.class)) {
                lookup.put(s.getValue(), s);
            }
        }

        private BundlingFirstItemMode(int value) {
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

        public static BundlingEnum.BundlingFirstItemMode get(int code) {
            return lookup.get(code);
        }

    }

    
    public enum BundlingAdditionalItemMode {
        KEEP_ORIGINAL_SERVICE_TIME_FOR_ADDITIONAL_ITEM(1),
        USE_CUSTOM_SERVICE_TIME_FOR_ADDITIONAL_ITEM(2),
        DO_NOT_ADD_SERVICE_TIME_FOR_ADDITIONAL_ITEM(3);

        private int value;
        private static final Map<Integer, BundlingEnum.BundlingAdditionalItemMode> lookup
                = new HashMap<>();

        static {
            for (BundlingEnum.BundlingAdditionalItemMode s : EnumSet.allOf(BundlingEnum.BundlingAdditionalItemMode.class)) {
                lookup.put(s.getValue(), s);
            }
        }

        private BundlingAdditionalItemMode(int value) {
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

        public static BundlingEnum.BundlingAdditionalItemMode get(int code) {
            return lookup.get(code);
        }

    }
    
    public enum BundledItemsMode {

        KEEP_AS_SEPARATE_DESTINATIONS(1),
        MERGE_INTO_SINGLE_DESTINATION(2);

        private int value;
        private static final Map<Integer, BundlingEnum.BundledItemsMode> lookup
                = new HashMap<>();

        static {
            for (BundlingEnum.BundledItemsMode s : EnumSet.allOf(BundlingEnum.BundledItemsMode.class)) {
                lookup.put(s.getValue(), s);
            }
        }

        private BundledItemsMode(int value) {
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

        public static BundlingEnum.BundledItemsMode get(int code) {
            return lookup.get(code);
        }
        
    
    }
    


}

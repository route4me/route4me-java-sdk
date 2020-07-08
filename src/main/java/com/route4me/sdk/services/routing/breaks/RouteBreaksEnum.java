package com.route4me.sdk.services.routing.breaks;

public class RouteBreaksEnum {

    public enum RouteBreaksType {
        CERTAIN_NUMBER_OF_TOTAL_ELAPSED_TIME("certain_number_of_total_elapsed_time"),
        CERTAIN_NUMBER_OF_LOCATIONS("certain_number_of_locations"),
        CERTAIN_NUMBER_OF_TRAVEL_TIME("certain_number_of_travel_time");

        private String value;

        private RouteBreaksType(String value) {
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

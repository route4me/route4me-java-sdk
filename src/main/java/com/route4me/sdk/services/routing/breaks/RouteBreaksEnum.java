package com.route4me.sdk.services.routing.breaks;

/** Values of the route break API. */
public class RouteBreaksEnum {

    /** What a break follows; {@code toString()} and {@link #getValue()} give the API value. */
    public enum RouteBreaksType {
        /** After a given elapsed time ({@code certain_number_of_total_elapsed_time}). */
        CERTAIN_NUMBER_OF_TOTAL_ELAPSED_TIME("certain_number_of_total_elapsed_time"),
        /** After a given number of stops ({@code certain_number_of_locations}). */
        CERTAIN_NUMBER_OF_LOCATIONS("certain_number_of_locations"),
        /** After a given travel time ({@code certain_number_of_travel_time}). */
        CERTAIN_NUMBER_OF_TRAVEL_TIME("certain_number_of_travel_time"),
        /** After a given service time ({@code certain_number_of_service_time}). */
        CERTAIN_NUMBER_OF_SERVICE_TIME("certain_number_of_service_time");

        private final String value;

        RouteBreaksType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        /**
         * Returns the API value of the type.
         *
         * @return the value, such as {@code certain_number_of_locations}
         */
        public String getValue() {
            return value;
        }
    }

}

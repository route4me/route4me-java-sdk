/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.users;

/** Values of the user API. */
public class UsersEnum {

    /** The plan of a new account, for {@link UserRegistration}; {@code toString()} gives the API value. */
    public enum Plan {
        /** The marketplace plan ({@code marketplace}). */
        MARKETPLACE("marketplace");

        private final String value;

        Plan(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        /**
         * Returns the API value of the plan.
         *
         * @return the value, such as {@code marketplace}
         */
        public String getValue() {
            return value;
        }

    }

}

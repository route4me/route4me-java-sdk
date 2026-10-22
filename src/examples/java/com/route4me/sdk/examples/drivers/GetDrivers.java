package com.route4me.sdk.examples.drivers;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.drivers.DriversManager;

/** Prints the drivers of the account, as the API sends them, with {@link DriversManager#getDrivers}. */
public class GetDrivers {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        DriversManager manager = new DriversManager(apiKey);
        try {
            System.out.println(manager.getDrivers());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}

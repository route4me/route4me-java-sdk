package com.route4me.sdk.examples.authhistory;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.authhistory.AuthHistoryApiConfig;
import com.route4me.sdk.services.authhistory.AuthHistoryManager;

/** Prints how the authentication history can be sorted and filtered, with {@link AuthHistoryManager#getApiConfig}. */
public class GetAuthHistoryApiConfig {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AuthHistoryManager manager = new AuthHistoryManager(apiKey);
        try {
            AuthHistoryApiConfig config = manager.getApiConfig();
            if (config.getApiCapabilities() != null) {
                System.out.println("Sortable: " + config.getApiCapabilities().getSortableFields());
                System.out.println("Filterable: " + config.getApiCapabilities().getFilterableFields());
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}

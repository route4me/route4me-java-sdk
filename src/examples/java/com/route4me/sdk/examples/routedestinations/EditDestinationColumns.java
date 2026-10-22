package com.route4me.sdk.examples.routedestinations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routedestinations.EditDestinationColumnsRequest;
import com.route4me.sdk.services.routedestinations.EditDestinationColumnsResponse;
import com.route4me.sdk.services.routedestinations.RouteDestinationColumnsResource;
import com.route4me.sdk.services.routedestinations.RouteDestinationsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reads the column order of the destination list with {@link RouteDestinationsManager#getDestinationColumns}, then
 * saves it reversed with {@link RouteDestinationsManager#editDestinationColumns}.
 */
public class EditDestinationColumns {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RouteDestinationsManager manager = new RouteDestinationsManager(apiKey);
        String key = "COLUMNS_CONFIGURATOR_KEY_HERE";
        try {
            RouteDestinationColumnsResource columns = manager.getDestinationColumns(key);
            System.out.println("Current order: " + columns.getOrder());

            if (columns.getOrder() == null) {
                return;
            }
            List<String> reversed = new ArrayList<>(columns.getOrder());
            Collections.reverse(reversed);
            EditDestinationColumnsRequest request = new EditDestinationColumnsRequest();
            request.setColumnsConfiguratorKey(key);
            request.setOrder(reversed);
            EditDestinationColumnsResponse saved = manager.editDestinationColumns(request);
            System.out.println("New order: " + saved.getOrder());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}

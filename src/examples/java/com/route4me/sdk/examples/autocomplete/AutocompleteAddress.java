package com.route4me.sdk.examples.autocomplete;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.autocomplete.AutocompleteManager;

/** Prints address suggestions for a partly typed address with {@link AutocompleteManager#autocompleteAddress}. */
public class AutocompleteAddress {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AutocompleteManager manager = new AutocompleteManager(apiKey);
        try {
            System.out.println(manager.autocompleteAddress("Los Angeles Intern"));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}

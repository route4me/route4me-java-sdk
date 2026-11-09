package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookContactV5;
import com.route4me.sdk.services.addressbook.AddressBookContactsResponse;
import com.route4me.sdk.services.addressbook.AddressBookGroupFilter;
import com.route4me.sdk.services.addressbook.AddressBookGroupRule;
import com.route4me.sdk.services.addressbook.AddressBookGroupRulesRequest;
import com.route4me.sdk.services.addressbook.AddressBookManagerV5;

import java.util.Collections;

/**
 * Lists the routed contacts whose address contains a text, by group rules, with
 * {@link AddressBookManagerV5#filterContactsByGroupRules}.
 */
public class FilterContactsByGroupRulesV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManagerV5 manager = new AddressBookManagerV5(apiKey);
        try {
            AddressBookGroupRule rule = new AddressBookGroupRule();
            rule.setId("address_1");
            rule.setField("address_1");
            rule.setOperator("contains");
            rule.setValue("Main St");
            AddressBookGroupFilter rules = new AddressBookGroupFilter();
            rules.setCondition("AND");
            rules.setRules(Collections.singletonList(rule));
            AddressBookGroupRulesRequest request = new AddressBookGroupRulesRequest();
            request.setRules(rules);
            request.setLimit(50);
            AddressBookContactsResponse response = manager.filterContactsByGroupRules(request, "routed");
            if (response.getResults() != null) {
                for (AddressBookContactV5 contact : response.getResults()) {
                    System.out.println("Contact: " + contact.getAddress1());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}

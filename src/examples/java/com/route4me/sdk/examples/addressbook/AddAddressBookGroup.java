package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookGroup;
import com.route4me.sdk.services.addressbook.AddressBookGroupFilter;
import com.route4me.sdk.services.addressbook.AddressBookGroupRule;
import com.route4me.sdk.services.addressbook.AddressBookManager;

import java.util.Collections;

/**
 * Creates an address book group of the contacts whose address contains a text, with
 * {@link AddressBookManager#addAddressBookGroup}.
 */
public class AddAddressBookGroup {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AddressBookManager manager = new AddressBookManager(apiKey);
        try {
            AddressBookGroupRule rule = new AddressBookGroupRule();
            rule.setId("address_1");
            rule.setField("address_1");
            rule.setOperator("contains");
            rule.setValue("Main St");
            AddressBookGroupFilter filter = new AddressBookGroupFilter();
            filter.setCondition("AND");
            filter.setRules(Collections.singletonList(rule));
            AddressBookGroup group = new AddressBookGroup();
            group.setGroupName("Main Street contacts");
            group.setGroupColor("92e1c0");
            group.setFilter(filter);
            AddressBookGroup created = manager.addAddressBookGroup(group);
            System.out.println("Created group " + created.getGroupId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}

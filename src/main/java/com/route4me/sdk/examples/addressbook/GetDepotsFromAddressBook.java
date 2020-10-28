/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.addressbook;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.addressbook.Contact;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juan
 */
public class GetDepotsFromAddressBook {
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        AddressBookManager manager = new AddressBookManager(apiKey);
        try {
            List<Contact> depots = manager.getDepotsFromAddressBook();
            System.out.println(depots);
        } catch (APIException ex) {
            Logger.getLogger(GetDepotsFromAddressBook.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

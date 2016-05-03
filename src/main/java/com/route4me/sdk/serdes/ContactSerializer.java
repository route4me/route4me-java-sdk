/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.serdes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.route4me.sdk.model.Contact;
import java.lang.reflect.Type;

/**
 *
 * @author juan
 */
public class ContactSerializer extends GenericSerializer implements JsonSerializer<Contact> {


    public static final Gson GSON_SERIALIZER = new GsonBuilder().registerTypeAdapter(Contact.class, new ContactSerializer()).setPrettyPrinting().create();


    @Override
    public JsonElement serialize(Contact t, Type type, JsonSerializationContext jsc) {
        final JsonObject jsonObject;
        jsonObject = objectSerializer(t);
        return jsonObject;
    }
    
}

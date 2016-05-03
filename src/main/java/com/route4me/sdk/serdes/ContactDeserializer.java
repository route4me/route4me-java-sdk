package com.route4me.sdk.serdes;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.route4me.sdk.model.Contact;
import com.route4me.sdk.model.DataObject;
import static com.route4me.sdk.serdes.GenericDeserializer.newInstance;
import static com.route4me.sdk.serdes.GenericDeserializer.setFieldValue;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author juan
 */
public class ContactDeserializer extends GenericDeserializer implements JsonDeserializer<Contact> {

    static Map<String, Class> objectClass = new HashMap<>();

    static {
        objectClass.put("results", Contact.class);

    }

    public static final Gson GSON_DESERIALIZER = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).registerTypeAdapter(Contact.class, new ContactDeserializer()).create();

    public ContactDeserializer() {

    }

    @Override
    public Contact deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        Contact response = new Contact();
        JsonObject jsonObject = je.getAsJsonObject();
        response = (Contact) deserializeJSON(response, jsonObject);
        return response;
    }

    protected Object deserializeJSON(Object object, JsonObject jsonObject) {
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement element = entry.getValue();
            if (element.isJsonNull()) {
                try {
                    setFieldValue(object, key, null);
                } catch (NoSuchFieldException ex) {
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(ContactDeserializer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (element.isJsonObject()) {
                try {
                    if (ContactDeserializer.objectClass.get(key) != null) {
                        setFieldValue(object, key, deserializeJSON(newInstance(ContactDeserializer.objectClass.get(key)), element.getAsJsonObject()));
                    }
                } catch (NoSuchFieldException ex) {
                } catch (IllegalArgumentException | IllegalAccessException | SecurityException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
                    Logger.getLogger(ContactDeserializer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (element.isJsonPrimitive()) {
                try {
                    setFieldValue(object, key, deserializeJsonPrimitive(element.getAsJsonPrimitive()));
                } catch (NoSuchFieldException ex) {
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                }
            } else if (element.isJsonArray()) {
                try {
                    if (ContactDeserializer.objectClass.get(key) != null) {
                        setFieldValue(object, key, deserializeJSONArray(key, element.getAsJsonArray()));
                    }
                } catch (IllegalArgumentException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    Logger.getLogger(ContactDeserializer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchFieldException ex) {
                }
            } else {
                System.err.println("Unknown JSON element type for element " + element + ". ");
                return null;
            }
        }
        return object;
    }

    protected List<Object> deserializeJSONArray(String key, JsonArray arr) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<Object> elems = new ArrayList<>();
        Iterator<JsonElement> elemIter = arr.iterator();
        while (elemIter.hasNext()) {
            JsonElement elem = elemIter.next();
            elems.add(deserializeJSON(newInstance(ContactDeserializer.objectClass.get(key)), (JsonObject) elem));
        }
        return elems;
    }

}

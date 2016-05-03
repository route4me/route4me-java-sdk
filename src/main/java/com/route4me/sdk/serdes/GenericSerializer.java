/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.serdes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juan
 */
class GenericSerializer {

    protected JsonElement listSerializer(Object objects) {
        final JsonArray jsonDataArray = new JsonArray();
        for (Object object : (List) objects) {
            jsonDataArray.add(objectSerializer(object));
        }
        return jsonDataArray;
    }

    protected JsonObject objectSerializer(Object t) {
        JsonObject jsonObject = new JsonObject();
        Class<?> clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            Class<?> fClazz = field.getType();
            field.setAccessible(true);
            try {
                if (fClazz.isAssignableFrom(Number.class)) {
                    jsonObject.addProperty(name, (Number) field.get(t));
                } else if (fClazz.isAssignableFrom(String.class)) {
                    jsonObject.addProperty(name, (String) field.get(t));
                } else if (fClazz.isAssignableFrom(Boolean.class)) {
                    jsonObject.addProperty(name, (Boolean) field.get(t));
                } else if (fClazz.isAssignableFrom(List.class)) {
                    Object object = field.get(t);
                    if (object != null) {
                        jsonObject.add(name, listSerializer(object));
                    }
                } else {
                    Object object = field.get(t);
                    if (object != null) {
                        jsonObject.add(name, objectSerializer(object));
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(DataObjectSerializer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jsonObject;
    }
    
}

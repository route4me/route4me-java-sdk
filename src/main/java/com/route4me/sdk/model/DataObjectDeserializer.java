package com.route4me.sdk.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juan
 */
class DataObjectDeserializer implements JsonDeserializer<DataObject> {

    static Map<String, Class> objectClass = new HashMap<>();

    static {
        objectClass.put("parameters", Parameters.class);
        objectClass.put("links", Links.class);
        objectClass.put("addresses", Address.class);
        objectClass.put("tracking_history", TrackingHistory.class);
        objectClass.put("routes", Routes.class);
        objectClass.put("optimizations", Optimizations.class);

    }

    public DataObjectDeserializer() {

    }

    public static <T> T newInstance(Class<T> clazz)
            throws IllegalArgumentException, SecurityException,
            InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        return newInstance(clazz, new Class[0], new Object[0]);
    }

    public static <T> T newInstance(Class<T> clazz, Class<?>[] paramClazzes,
            Object[] params) throws IllegalArgumentException,
            SecurityException, InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {

        return clazz.getConstructor(paramClazzes).newInstance(params);
    }

    public static void setFieldValue(Object object, String fieldName,
            Object newValue) throws NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, newValue);
    }

    private Object deserializeJsonPrimitive(JsonPrimitive element) {
        if (element.isBoolean()) {
            return element.getAsBoolean();
        } else if (element.isNumber()) {
            return element.getAsNumber();
        } else {
            return element.getAsString();
        }
    }

    private List<Object> deserializeJSONArray(String key, JsonArray arr) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<Object> elems = new ArrayList<>();
        Iterator<JsonElement> elemIter = arr.iterator();
        while (elemIter.hasNext()) {
            JsonElement elem = elemIter.next();
            elems.add(deserializeJSON(newInstance(objectClass.get(key)), (JsonObject) elem));

        }
        return elems;
    }

    private Object deserializeJSON(Object object, JsonObject jsonObject) {
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement element = entry.getValue();
            if (element.isJsonNull()) {
                try {
                    setFieldValue(object, key, null);
                } catch (NoSuchFieldException ex) {
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(DataObjectDeserializer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (element.isJsonObject()) {
                try {
                    if (objectClass.get(key) != null) {
                        setFieldValue(object, key, deserializeJSON(newInstance(objectClass.get(key)),element.getAsJsonObject()));
                    }
                } catch (NoSuchFieldException ex) {
                    
                } catch (IllegalArgumentException | IllegalAccessException | SecurityException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
                    Logger.getLogger(DataObjectDeserializer.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (element.isJsonPrimitive()) {
                try {
                    setFieldValue(object, key, deserializeJsonPrimitive(element.getAsJsonPrimitive()));
                } catch (NoSuchFieldException ex) {
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                }
            } else if (element.isJsonArray()) {
                try {
                    if (objectClass.get(key) != null) {
                        setFieldValue(object, key, deserializeJSONArray(key,element.getAsJsonArray()));
                    }
                } catch (IllegalArgumentException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    Logger.getLogger(DataObjectDeserializer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchFieldException ex) {
                }

            } else {
                System.err.println("Unknown JSON element type for element " + element + ". ");
                return null;
            }

        }
        return object;
    }

    @Override
    public DataObject deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        DataObject response = new DataObject();
        JsonObject jsonObject = je.getAsJsonObject();
        response = (DataObject) deserializeJSON(response, jsonObject);
        return response;
    }

}

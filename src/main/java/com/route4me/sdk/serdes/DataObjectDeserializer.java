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
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.model.Links;
import com.route4me.sdk.model.Optimizations;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.model.Routes;
import com.route4me.sdk.services.tracking.TrackingHistory;
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

/**
 *
 * @author juan
 */
public class DataObjectDeserializer extends GenericDeserializer implements JsonDeserializer<DataObject> {

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

    @Override
    public DataObject deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        DataObject response = new DataObject();
        JsonObject jsonObject = je.getAsJsonObject();
        response = (DataObject) deserializeJSON(response, jsonObject);
        return response;
    }

    public static final Gson GSON_DESERIALIZER = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).registerTypeAdapter(DataObject.class, new DataObjectDeserializer()).create();

    
    protected Object deserializeJSON(Object object, JsonObject jsonObject) {
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
                    if (DataObjectDeserializer.objectClass.get(key) != null) {
                        setFieldValue(object, key, deserializeJSON(newInstance(DataObjectDeserializer.objectClass.get(key)), element.getAsJsonObject()));
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
                    if (DataObjectDeserializer.objectClass.get(key) != null) {
                        setFieldValue(object, key, deserializeJSONArray(key, element.getAsJsonArray()));
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

    protected List<Object> deserializeJSONArray(String key, JsonArray arr) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<Object> elems = new ArrayList<>();
        Iterator<JsonElement> elemIter = arr.iterator();
        while (elemIter.hasNext()) {
            JsonElement elem = elemIter.next();
            elems.add(deserializeJSON(newInstance(DataObjectDeserializer.objectClass.get(key)), (JsonObject) elem));
        }
        return elems;
    }
    
    
    
}

package com.route4me.sdk.serdes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.route4me.sdk.services.routing.DataObject;
import java.lang.reflect.Type;

/**
 *
 * @author juan
 */
public class DataObjectSerializer extends GenericSerializer implements JsonSerializer<DataObject> {

    public static final Gson GSON_SERIALIZER = new GsonBuilder().registerTypeAdapter(DataObject.class, new DataObjectSerializer()).setPrettyPrinting().create();

    @Override
    public JsonElement serialize(DataObject t, Type type, JsonSerializationContext jsc) {
        final JsonObject jsonObject;
        jsonObject = objectSerializer(t);
        return jsonObject;
    }


}

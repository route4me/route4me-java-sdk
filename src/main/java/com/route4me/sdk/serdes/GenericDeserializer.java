
package com.route4me.sdk.serdes;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.route4me.sdk.services.routing.DataObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juan
 */
public class GenericDeserializer {

    public static <T> T newInstance(Class<T> clazz) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return newInstance(clazz, new Class[0], new Object[0]);
    }

    public static <T> T newInstance(Class<T> clazz, Class<?>[] paramClazzes, Object[] params) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return clazz.getConstructor(paramClazzes).newInstance(params);
    }

    public static void setFieldValue(Object object, String fieldName, Object newValue) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, newValue);
    }


    protected Object deserializeJsonPrimitive(JsonPrimitive element) {
        if (element.isBoolean()) {
            return element.getAsBoolean();
        } else if (element.isNumber()) {
            return element.getAsNumber();
        } else {
            return element.getAsString();
        }
    }
    
}

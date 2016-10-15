package com.route4me.sdk.queryconverter;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class QueryConverter {
    /**
     * Converts a POJO to a list of query string parameters, does not support nesting.
     *
     * @param object to convert to query string parameters
     * @return A list of NameValuePair each representing a different parameter
     * @throws IllegalAccessException if SecurityPolicy does not allow getting value using reflection
     */
    public static List<NameValuePair> convertObjectToParameters(Object object) throws IllegalAccessException {
        List<NameValuePair> parameters = new ArrayList<>();

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            QueryParameter annot = f.getAnnotation(QueryParameter.class);
            if (annot != null) {
                f.setAccessible(true);
                Object fieldValue = f.get(object);
                //skip if null
                if (fieldValue == null) {
                    continue;
                }
                //if array add multiple parameters
                if (fieldValue.getClass().isArray()) {
                    for (int i = 0; i < Array.getLength(fieldValue); i++) {
                        parameters.add(new BasicNameValuePair(annot.value(), Array.get(fieldValue, i).toString()));
                    }
                } else if (fieldValue instanceof Iterable) { //or iterable
                    for (Object element : (Iterable) fieldValue) {
                        parameters.add(new BasicNameValuePair(annot.value(), element.toString()));
                    }
                } else {
                    //Convert booleans to numbers
                    if (fieldValue instanceof Boolean && ((Boolean) fieldValue).booleanValue()) {
                        parameters.add(new BasicNameValuePair(annot.value(), "1"));
                    } else {
                        parameters.add(new BasicNameValuePair(annot.value(), fieldValue.toString()));
                    }
                }
            }
        }
        return parameters;
    }
}

package com.route4me.sdk.queryconverter;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /**
     * Converts the {@link QueryParameter} fields of a POJO to query parameters for the V5 API,
     * with {@link #toBracketedParameters(String, Object)}: lists and arrays become
     * {@code name[]}, maps {@code name[key]}, and booleans {@code 1} or {@code 0}.
     *
     * @param object the object to convert
     * @return the parameters, in field order; {@code null} fields are left out
     * @throws IllegalAccessException if the security policy does not allow reading a field
     */
    public static List<NameValuePair> convertObjectToBracketedParameters(Object object) throws IllegalAccessException {
        List<NameValuePair> parameters = new ArrayList<>();
        for (Field field : object.getClass().getDeclaredFields()) {
            QueryParameter annotation = field.getAnnotation(QueryParameter.class);
            if (annotation != null) {
                field.setAccessible(true);
                addBracketed(parameters, annotation.value(), field.get(object));
            }
        }
        return parameters;
    }

    /**
     * Encodes one query value the way PHP, and so the V5 API, reads arrays from a query
     * string.
     *
     * <p>PHP reads an array only from bracketed keys: {@code ids[]=1&ids[]=2} is a list, but
     * {@code ids=1&ids=2} keeps only the last value, and a single {@code ids=1} is read as a
     * scalar. So:
     * <ul>
     *   <li>a list, a set or an array of plain values becomes one {@code name[]} pair per
     *       value, also when it holds a single value;</li>
     *   <li>a list or an array that holds lists, arrays or maps is indexed:
     *       {@code [["name","asc"]]} becomes {@code name[0][]=name&name[0][]=asc};</li>
     *   <li>a map becomes one {@code name[key]} entry per item, encoded the same way, so
     *       {@code {"status": [1, 2]}} becomes {@code name[status][]=1&name[status][]=2};</li>
     *   <li>a boolean becomes {@code 1} or {@code 0}, which the Laravel boolean rule
     *       accepts;</li>
     *   <li>any other value is sent as its {@code toString()}.</li>
     * </ul>
     * {@code null} values, and {@code null} items of lists and maps, are left out.
     *
     * @param name  the parameter name, without brackets
     * @param value the value
     * @return the parameters, in order; empty for {@code null} or an empty list
     */
    public static List<NameValuePair> toBracketedParameters(String name, Object value) {
        List<NameValuePair> parameters = new ArrayList<>();
        addBracketed(parameters, name, value);
        return parameters;
    }

    /** Adds the bracketed parameters of one value to a list; see {@link #toBracketedParameters}. */
    private static void addBracketed(List<NameValuePair> parameters, String key, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof Map) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                addBracketed(parameters, key + "[" + entry.getKey() + "]", entry.getValue());
            }
            return;
        }
        List<Object> items = asList(value);
        if (items == null) {
            parameters.add(new BasicNameValuePair(key, scalar(value)));
            return;
        }
        boolean nested = false;
        for (Object item : items) {
            if (item instanceof Map || asList(item) != null) {
                nested = true;
                break;
            }
        }
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            if (nested) {
                addBracketed(parameters, key + "[" + i + "]", item);
            } else if (item != null) {
                parameters.add(new BasicNameValuePair(key + "[]", scalar(item)));
            }
        }
    }

    /** Returns the items of an array or an iterable, or {@code null} for any other value. */
    private static List<Object> asList(Object value) {
        if (value == null) {
            return null;
        }
        List<Object> items = new ArrayList<>();
        if (value.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(value); i++) {
                items.add(Array.get(value, i));
            }
            return items;
        }
        if (value instanceof Iterable) {
            for (Object item : (Iterable<?>) value) {
                items.add(item);
            }
            return items;
        }
        return null;
    }

    /** Converts a plain value to its query text: {@code 1} or {@code 0} for a boolean. */
    private static String scalar(Object value) {
        if (value instanceof Boolean) {
            return ((Boolean) value) ? "1" : "0";
        }
        return value.toString();
    }
}

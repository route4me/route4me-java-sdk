package com.route4me.sdk.queryconverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Marks a field that {@link QueryConverter} sends as a query or form parameter, and gives its name. */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface QueryParameter {
    /**
     * Returns the name of the parameter.
     *
     * @return the parameter name, such as {@code route_id}
     */
    String value();
}

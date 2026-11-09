package com.route4me.sdk.services.locations;

import lombok.Data;

/**
 * The filters of {@link LocationManager#getLocationTypes(LocationTypeFilters, Integer, Integer)}, sent in the query
 * as {@code filters[name]=value}; filters left {@code null} are not sent, and booleans are sent as {@code 1} or
 * {@code 0}.
 */
@Data
public class LocationTypeFilters {
    /** Only the types that can, or cannot, have child locations ({@code filters[is_parent_type]}). */
    private Boolean isParentType;

    /** Only the types that can, or cannot, be the child of another location ({@code filters[is_child_type]}). */
    private Boolean isChildType;

    /** Only the types the account created, without the built-in ones ({@code filters[only_custom_types]}). */
    private Boolean onlyCustomTypes;

    /** Text to search for in the type names ({@code filters[search_query]}). */
    private String searchQuery;
}

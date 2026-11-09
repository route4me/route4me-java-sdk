package com.route4me.sdk.services.customers;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.Map;

/** A contact person of a customer, sent in a {@link CustomerStoreRequest}. */
@Data
public class CustomerContact {
    /**
     * The kind of contact, such as {@code primary}, {@code billing}, {@code sales} or {@code shipping} ({@code type}).
     */
    @SerializedName("type")
    private String type;

    /** First name ({@code first_name}). */
    @SerializedName("first_name")
    private String firstName;

    /** Last name ({@code last_name}). */
    @SerializedName("last_name")
    private String lastName;

    /** Phone ({@code phone}). */
    @SerializedName("phone")
    private String phone;

    /** Email ({@code email}). */
    @SerializedName("email")
    private String email;

    /** Custom data ({@code custom_data}). */
    @SerializedName("custom_data")
    private Map<String, Object> customData;
}

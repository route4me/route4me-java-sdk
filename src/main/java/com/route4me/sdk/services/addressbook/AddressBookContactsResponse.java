package com.route4me.sdk.services.addressbook;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import lombok.Data;

import java.lang.reflect.Type;
import java.util.List;

/**
 * The answer of the contact list methods of {@link AddressBookManagerV5}: {@code {"results": [...], "total": n}}.
 * A bare JSON array of contacts is read too, as the C# and PHP SDKs read the answer of
 * {@link AddressBookManagerV5#batchUpdateContacts(BatchUpdateContactsRequest)}; {@code total} is then {@code null}.
 */
@Data
@JsonAdapter(AddressBookContactsResponse.Reader.class)
public class AddressBookContactsResponse {
    /** The contacts ({@code results}). */
    @SerializedName("results") private List<AddressBookContactV5> results;
    /** Total number of the returned address book contacts ({@code total}). */
    @SerializedName("total") private Integer total;

    /** Reads the answer from an object with {@code results} and {@code total}, or from an array of contacts. */
    static final class Reader implements JsonDeserializer<AddressBookContactsResponse> {
        private static final Type CONTACTS = new TypeToken<List<AddressBookContactV5>>() { }.getType();

        @Override
        public AddressBookContactsResponse deserialize(JsonElement json, Type type,
                JsonDeserializationContext context) {
            AddressBookContactsResponse response = new AddressBookContactsResponse();
            if (json.isJsonArray()) {
                response.setResults(context.<List<AddressBookContactV5>>deserialize(json, CONTACTS));
                return response;
            }
            if (!json.isJsonObject()) {
                throw new JsonParseException("Expected an object or an array of contacts");
            }
            JsonObject object = json.getAsJsonObject();
            response.setResults(context.<List<AddressBookContactV5>>deserialize(object.get("results"), CONTACTS));
            response.setTotal(context.<Integer>deserialize(object.get("total"), Integer.class));
            return response;
        }
    }
}

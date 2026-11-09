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
 * The answer of the contact list methods of {@link AddressBookManagerV5}: {@code {"results": [...], "total": n}}, with
 * {@code current_page}, {@code last_page}, {@code per_page} and {@code fields} on the paginated lists.
 * A bare JSON array of contacts is read too, as the C# and PHP SDKs read the answer of
 * {@link AddressBookManagerV5#batchUpdateContacts(BatchUpdateContactsRequest)}; the other fields are then {@code null}.
 */
@Data
@JsonAdapter(AddressBookContactsResponse.Reader.class)
public class AddressBookContactsResponse {
    /** The contacts ({@code results}). */
    @SerializedName("results") private List<AddressBookContactV5> results;
    /** Total number of the returned address book contacts ({@code total}). */
    @SerializedName("total") private Integer total;
    /** The number of this page, from 1, on a paginated list ({@code current_page}). */
    @SerializedName("current_page") private Integer currentPage;
    /** The number of the last page, on a paginated list ({@code last_page}). */
    @SerializedName("last_page") private Integer lastPage;
    /** The number of contacts per page, on a paginated list ({@code per_page}). */
    @SerializedName("per_page") private Integer perPage;
    /** The names of the fields each contact holds, when the request named them ({@code fields}). */
    @SerializedName("fields") private List<String> fields;

    /** Reads the answer from an object with {@code results}, {@code total} and the paging fields, or from an array. */
    static final class Reader implements JsonDeserializer<AddressBookContactsResponse> {
        private static final Type CONTACTS = new TypeToken<List<AddressBookContactV5>>() { }.getType();
        private static final Type FIELDS = new TypeToken<List<String>>() { }.getType();

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
            response.setCurrentPage(context.<Integer>deserialize(object.get("current_page"), Integer.class));
            response.setLastPage(context.<Integer>deserialize(object.get("last_page"), Integer.class));
            response.setPerPage(context.<Integer>deserialize(object.get("per_page"), Integer.class));
            response.setFields(context.<List<String>>deserialize(object.get("fields"), FIELDS));
            return response;
        }
    }
}

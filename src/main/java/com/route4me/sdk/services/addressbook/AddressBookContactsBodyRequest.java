package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
@Data
public class AddressBookContactsBodyRequest {
    @SerializedName("filters") private AddressBookFilters filters;
    @SerializedName("order_by") private List<String[]> orderBy;
    @SerializedName("page") private Integer page;
    @SerializedName("per_page") private Integer perPage;
    @SerializedName("fields") private List<String> fields;
}

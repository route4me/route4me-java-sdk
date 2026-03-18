package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
@Data
public class AddressBookFilters {
    @SerializedName("search_query") private String searchQuery;
    @SerializedName("address_id") private List<Long> addressId;
    @SerializedName("display") private List<String> display;
}

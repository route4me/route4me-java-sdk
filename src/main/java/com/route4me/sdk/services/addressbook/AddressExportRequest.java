package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
@Data
public class AddressExportRequest {
    @SerializedName("filters") private AddressBookFilters filters;
    @SerializedName("columns") private List<String> columns;
    @SerializedName("filename") private String filename;
    @SerializedName("format") private String format;
}

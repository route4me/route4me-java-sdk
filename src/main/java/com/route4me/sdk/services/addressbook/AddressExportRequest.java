package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
/** The body of {@link AddressBookManagerV5#exportAddresses(AddressExportRequest)}. */
@Data
public class AddressExportRequest {
    /** The filters of the contacts to export ({@code filters}). */
    @SerializedName("filters") private AddressBookFilters filters;
    /** Array of column field names to include in export ({@code columns}). */
    @SerializedName("columns") private List<String> columns;
    /** Output filename (without extension) ({@code filename}). */
    @SerializedName("filename") private String filename;
    /** Export format: "csv", "xls", or "xlsx" ({@code format}). */
    @SerializedName("format") private String format;
}

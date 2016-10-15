package com.route4me.sdk.services.addressbook;

import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class ContactsRequest {
    private List<String> addressIds = new ArrayList<>();
    @QueryParameter("query")
    private String query;
    @QueryParameter("fields")
    private String fields;
    @QueryParameter("limit")
    private Integer limit;
    @QueryParameter("offset")
    private Integer offset;
    @QueryParameter("start")
    private Integer start;
    @QueryParameter("end")
    private Integer end;
    @QueryParameter("display")
    private String display;
}

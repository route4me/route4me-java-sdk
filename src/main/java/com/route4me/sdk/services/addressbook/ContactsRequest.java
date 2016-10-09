package com.route4me.sdk.services.addressbook;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class ContactsRequest {
    private List<String> addressIds = new ArrayList<>();
    private String query;
    private String fields;
    private Integer limit;
    private Integer offset;
    private Integer start;
    private Integer end;
    private String display;
}

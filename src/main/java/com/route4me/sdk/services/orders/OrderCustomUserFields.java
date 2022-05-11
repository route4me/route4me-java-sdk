/*
 * The MIT License
 *
 * Copyright 2022 Route4Me.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 *
 * @author route4me
 */
@Data
class OrderCustomUserFields {

    @SerializedName("order_custom_field_id")
    private Integer orderCustomFieldID;
    @SerializedName("order_custom_field_name")
    private String orderCustomFieldName;
    @SerializedName("order_custom_field_label")
    private String orderCustomFieldLabel;
    @SerializedName("order_custom_field_type")
    private String orderCustomFieldType;
    @SerializedName("order_custom_field_value")
    private String orderCustomFieldValue;
    @SerializedName("root_owner_member_id")
    private Integer rootOwnerMemberID;
    @SerializedName("order_custom_field_type_info")
    private OrderCustomFieldTypeInfo orderCustomFieldTypeInfo;

}

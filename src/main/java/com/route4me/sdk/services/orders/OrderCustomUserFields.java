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

@Data
class OrderCustomUserFields {

    /** Custom order field ID ({@code order_custom_field_id}). */
    @SerializedName("order_custom_field_id")
    private Integer orderCustomFieldID;
    /** Custom order field name ({@code order_custom_field_name}). */
    @SerializedName("order_custom_field_name")
    private String orderCustomFieldName;
    /** Custom order field label ({@code order_custom_field_label}). */
    @SerializedName("order_custom_field_label")
    private String orderCustomFieldLabel;
    /** Order custom field type (default: 'checkbox') ({@code order_custom_field_type}). */
    @SerializedName("order_custom_field_type")
    private String orderCustomFieldType;
    /**
     * Order custom field value. If order_custom_field_type='checkbox', valid values are: 'true', 'false' ({@code
     * order_custom_field_value}).
     */
    @SerializedName("order_custom_field_value")
    private String orderCustomFieldValue;
    /** Account owner member ID ({@code root_owner_member_id}). */
    @SerializedName("root_owner_member_id")
    private Integer rootOwnerMemberID;
    /** Information about an order's custom field ({@code order_custom_field_type_info}). */
    @SerializedName("order_custom_field_type_info")
    private OrderCustomFieldTypeInfo orderCustomFieldTypeInfo;

}

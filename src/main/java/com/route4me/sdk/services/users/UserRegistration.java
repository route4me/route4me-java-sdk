/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.users;

import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author Route4Me
 */
@Data
public class UserRegistration {

    @QueryParameter("format")
    private String format;
    @QueryParameter("strEmail")
    private String strEmail;
    @QueryParameter("strPhone")
    private String strPhone;
    @QueryParameter("strFirstName")
    private String strFirstName;
    @QueryParameter("strLastName")
    private String strLastName;
    @QueryParameter("strCompany")
    private String strCompany;
    @QueryParameter("strCompanySize")
    private String strCompanySize;
    @QueryParameter("industry")
    private String industry;
    @ToString.Exclude
    @QueryParameter("strPassword_1")
    @SuppressWarnings("checkstyle:MemberName") // the name of the Lombok accessors, public API
    private String strPassword_1;
    @ToString.Exclude
    @QueryParameter("strPassword_2")
    @SuppressWarnings("checkstyle:MemberName") // the name of the Lombok accessors, public API
    private String strPassword_2;
    @QueryParameter("chkTerms")
    private String chkTerms;
    @QueryParameter("plan")
    private String plan;
    @QueryParameter("business_member_type")
    private String businessMemberType;
    @ToString.Exclude
    @QueryParameter("organization_api_key")
    private String organizationApiKey;
    @ToString.Exclude
    @QueryParameter("activation_key")
    private String activationKey;

}

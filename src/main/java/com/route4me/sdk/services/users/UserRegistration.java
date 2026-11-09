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
 * The form of {@link UsersManager#registerUser(UserRegistration)}; the fields are sent under their
 * {@code QueryParameter} names.
 */
@Data
public class UserRegistration {

    /** Response format. Available values: 'json', 'xml' ({@code format}). */
    @QueryParameter("format")
    private String format;
    /** Member email ({@code strEmail}). */
    @QueryParameter("strEmail")
    private String strEmail;
    /** The phone number of the user ({@code strPhone}). */
    @QueryParameter("strPhone")
    private String strPhone;
    /** First name of a member ({@code strFirstName}). */
    @QueryParameter("strFirstName")
    private String strFirstName;
    /** Last name of a member ({@code strLastName}). */
    @QueryParameter("strLastName")
    private String strLastName;
    /** The name of the company ({@code strCompany}). */
    @QueryParameter("strCompany")
    private String strCompany;
    /** The size of the company ({@code strCompanySize}). */
    @QueryParameter("strCompanySize")
    private String strCompanySize;
    /** The industry of the company ({@code industry}). */
    @QueryParameter("industry")
    private String industry;
    /** User Password ({@code strPassword_1}). */
    @ToString.Exclude
    @QueryParameter("strPassword_1")
    @SuppressWarnings("checkstyle:MemberName") // the name of the Lombok accessors, public API
    private String strPassword_1;
    /** User confirmation Password ({@code strPassword_2}). */
    @ToString.Exclude
    @QueryParameter("strPassword_2")
    @SuppressWarnings("checkstyle:MemberName") // the name of the Lombok accessors, public API
    private String strPassword_2;
    /** If equal to 1, a user agreed to the Route4Me terms. Available values: 1, 0 ({@code chkTerms}). */
    @QueryParameter("chkTerms")
    private String chkTerms;
    /**
     * The plan type that the user selected. Available values: 'free', 'basic', 'pro', 'premium', 'personal' ({@code
     * plan}).
     */
    @QueryParameter("plan")
    private String plan;
    /** The kind of business of the account ({@code business_member_type}). */
    @QueryParameter("business_member_type")
    private String businessMemberType;
    /** The API key of the organization to register in; do not log it ({@code organization_api_key}). */
    @ToString.Exclude
    @QueryParameter("organization_api_key")
    private String organizationApiKey;
    /** The activation key of the account ({@code activation_key}). */
    @ToString.Exclude
    @QueryParameter("activation_key")
    private String activationKey;

}

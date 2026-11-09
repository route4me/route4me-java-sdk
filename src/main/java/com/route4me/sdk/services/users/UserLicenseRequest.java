package com.route4me.sdk.services.users;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

/**
 * A user license bought in a mobile app, the body of {@link UsersManager#purchaseUserLicense(UserLicenseRequest)}.
 * The session GUID, the token and the payload are credentials or receipts; {@code toString()} leaves them out.
 */
@Data
public class UserLicenseRequest {
    /** The member ID ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;

    /** The session GUID of the member; {@code toString()} leaves it out ({@code session_guid}). */
    @ToString.Exclude
    @SerializedName("session_guid")
    private String sessionGuid;

    /** The ID of the device ({@code device_id}). */
    @SerializedName("device_id")
    private String deviceId;

    /** The type of the device, such as {@code ipad} ({@code device_type}). */
    @SerializedName("device_type")
    private String deviceType;

    /** The name of the subscription ({@code subscription_name}). */
    @SerializedName("subscription_name")
    private String subscriptionName;

    /** The purchase token of the app store; {@code toString()} leaves it out ({@code token}). */
    @ToString.Exclude
    @SerializedName("token")
    private String token;

    /** The purchase receipt of the app store; {@code toString()} leaves it out ({@code payload}). */
    @ToString.Exclude
    @SerializedName("payload")
    private String payload;

    /** The answer format, such as {@code json} ({@code format}). */
    @SerializedName("format")
    private String format;
}

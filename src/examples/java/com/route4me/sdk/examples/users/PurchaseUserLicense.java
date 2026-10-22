package com.route4me.sdk.examples.users;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.users.UserLicenseRequest;
import com.route4me.sdk.services.users.UsersManager;

/**
 * Records a user license bought in a mobile app with {@link UsersManager#purchaseUserLicense}. It reads the session
 * GUID and the purchase token and receipt from the {@code R4M_SESSION_GUID}, {@code R4M_PURCHASE_TOKEN} and
 * {@code R4M_PURCHASE_RECEIPT} environment variables.
 */
public class PurchaseUserLicense {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        UsersManager manager = new UsersManager(apiKey);
        try {
            UserLicenseRequest license = new UserLicenseRequest();
            license.setMemberId(1L);
            license.setSessionGuid(System.getenv("R4M_SESSION_GUID"));
            license.setDeviceId("DEVICE_ID_HERE");
            license.setDeviceType("ipad");
            license.setSubscriptionName("IPAD_MONTHLY");
            license.setToken(System.getenv("R4M_PURCHASE_TOKEN"));
            license.setPayload(System.getenv("R4M_PURCHASE_RECEIPT"));
            license.setFormat("json");
            System.out.println(manager.purchaseUserLicense(license));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}

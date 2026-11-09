package com.route4me.sdk.examples.users;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.users.UserLicenseRequest;
import com.route4me.sdk.services.users.UsersManager;

/**
 * Records a user license bought in a mobile app with {@link UsersManager#purchaseUserLicense}. It reads the member
 * and the device from {@code MEMBER_ID} and {@code DEVICE_ID}, and the session GUID and the purchase token and
 * receipt from the {@code R4M_SESSION_GUID}, {@code R4M_PURCHASE_TOKEN} and {@code R4M_PURCHASE_RECEIPT} environment
 * variables, and stops when any of them is unset. Use a member of a test account: the example records a license for
 * it.
 */
public class PurchaseUserLicense {
    public static void main(String[] args) {
        String memberId = System.getenv("MEMBER_ID");
        String deviceId = System.getenv("DEVICE_ID");
        String sessionGuid = System.getenv("R4M_SESSION_GUID");
        String token = System.getenv("R4M_PURCHASE_TOKEN");
        String receipt = System.getenv("R4M_PURCHASE_RECEIPT");
        if (memberId == null || memberId.isEmpty() || deviceId == null || deviceId.isEmpty() || sessionGuid == null
                || sessionGuid.isEmpty() || token == null || token.isEmpty() || receipt == null || receipt.isEmpty()) {
            System.err.println("Set MEMBER_ID and DEVICE_ID to a member of a test account and its device, and "
                    + "R4M_SESSION_GUID, R4M_PURCHASE_TOKEN and R4M_PURCHASE_RECEIPT to the purchase; "
                    + "this example records the license.");
            return;
        }
        Long id = ExampleInput.toLong("MEMBER_ID", memberId);
        if (id == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        UsersManager manager = new UsersManager(apiKey);
        try {
            UserLicenseRequest license = new UserLicenseRequest();
            license.setMemberId(id);
            license.setSessionGuid(sessionGuid);
            license.setDeviceId(deviceId);
            license.setDeviceType("ipad");
            license.setSubscriptionName("IPAD_MONTHLY");
            license.setToken(token);
            license.setPayload(receipt);
            license.setFormat("json");
            System.out.println(manager.purchaseUserLicense(license));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}

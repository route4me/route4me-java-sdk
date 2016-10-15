package com.route4me.sdk.services.notes;

public enum StatusUpdateType {
    PICKUP("pickup"),
    DROPOFF("dropoff"),
    NOANSWER("noanswer"),
    NOT_FOUND("notfound"),
    NOT_PAID("notpaid"),
    PAID("paid"),
    WRONG_DELIVERY("wrongdelivery"),
    WRONGA_DDRESS_RECIPIENT("wrongaddressrecipient"),
    NOT_PRESENT("notpresent"),
    PARTS_MISSING("parts_missing"),
    SERVICE_RENDERED("service_rendered"),
    FOLLOW_UP("follow_up"),
    LEFT_INFORMATION("left_information"),
    SPOKE_WITH_DECISION_MAKER("spoke_with_decision_maker"),
    SPOKE_WITH_DECISION_INFLUENCER("spoke_with_decision_influencer"),
    COMPETITIVE_ACCOUNT("competitive_account"),
    SCHEDULED_FOLLOW_UP_MEETING("scheduled_follow_up_meeting"),
    SCHEDULED_LUNCH("scheduled_lunch"),
    SCHEDULED_PRODUCT_DEMO("scheduled_product_demo"),
    SCHEDULED_CLINICAL_DEMO("scheduled_clinical_demo"),
    NO_OPPORTUNITY("no_opportunity");

    private final String internal;

    StatusUpdateType(String internal) {
        this.internal = internal;
    }

    @Override
    public String toString() {
        return this.internal;
    }
}

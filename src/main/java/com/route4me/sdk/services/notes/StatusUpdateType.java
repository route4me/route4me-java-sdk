package com.route4me.sdk.services.notes;

/**
 * The type of a note added with {@link NotesManager#addAddressNotes(NoteRequest, String, StatusUpdateType)}, sent as
 * {@code strUpdateType}; {@code toString()} gives the API value.
 */
public enum StatusUpdateType {
    /** Pickup ({@code pickup}). */
    PICKUP("pickup"),
    /** Dropoff ({@code dropoff}). */
    DROPOFF("dropoff"),
    /** No answer ({@code noanswer}). */
    NOANSWER("noanswer"),
    /** Not found ({@code notfound}). */
    NOT_FOUND("notfound"),
    /** Not paid ({@code notpaid}). */
    NOT_PAID("notpaid"),
    /** Paid ({@code paid}). */
    PAID("paid"),
    /** Wrong delivery ({@code wrongdelivery}). */
    WRONG_DELIVERY("wrongdelivery"),
    /** Wrong address or recipient ({@code wrongaddressrecipient}). */
    WRONGA_DDRESS_RECIPIENT("wrongaddressrecipient"),
    /** Recipient not present ({@code notpresent}). */
    NOT_PRESENT("notpresent"),
    /** Parts missing ({@code parts_missing}). */
    PARTS_MISSING("parts_missing"),
    /** Service rendered ({@code service_rendered}). */
    SERVICE_RENDERED("service_rendered"),
    /** Follow up ({@code follow_up}). */
    FOLLOW_UP("follow_up"),
    /** Left information ({@code left_information}). */
    LEFT_INFORMATION("left_information"),
    /** Spoke with decision maker ({@code spoke_with_decision_maker}). */
    SPOKE_WITH_DECISION_MAKER("spoke_with_decision_maker"),
    /** Spoke with decision influencer ({@code spoke_with_decision_influencer}). */
    SPOKE_WITH_DECISION_INFLUENCER("spoke_with_decision_influencer"),
    /** Competitive account ({@code competitive_account}). */
    COMPETITIVE_ACCOUNT("competitive_account"),
    /** Scheduled follow up meeting ({@code scheduled_follow_up_meeting}). */
    SCHEDULED_FOLLOW_UP_MEETING("scheduled_follow_up_meeting"),
    /** Scheduled lunch ({@code scheduled_lunch}). */
    SCHEDULED_LUNCH("scheduled_lunch"),
    /** Scheduled product demo ({@code scheduled_product_demo}). */
    SCHEDULED_PRODUCT_DEMO("scheduled_product_demo"),
    /** Scheduled clinical demo ({@code scheduled_clinical_demo}). */
    SCHEDULED_CLINICAL_DEMO("scheduled_clinical_demo"),
    /** No opportunity ({@code no_opportunity}). */
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

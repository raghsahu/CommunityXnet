package com.logical.communityapp.firebase_service;

public class Constants {

    public static final String CHANNEL_ID = "my_channel_01";
    public static final String CHANNEL_NAME = "CommunityXnet";
    public static final String CHANNEL_DESCRIPTION = "www.logicalsofttech.com";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
}

package com.logical.communityapp.firebase_service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.logical.communityapp.Activity.MainActivity;
import com.logical.communityapp.Preference.SessionManager;
import com.logical.communityapp.Preference.Shared_Preference;
import com.logical.communityapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private String title_msg = "";
    RemoteMessage remoteMessage;
    private String user_id = "";
    private NotificationUtils notificationUtils;


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        SessionManager session = new SessionManager(this);
        session.saveToken(s);

        Log.e("token_fcm_service", s);
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //if the message contains data payload
        //It is a map of custom keyvalues
        //we can read it easily
        if(remoteMessage.getData().size() > 0){
            //handle the data message here
            Log.e(TAG, "Data_Payload: " + remoteMessage.getData().toString());

            try {

               // JSONObject json = new JSONObject(remoteMessage.getData().toString());
                //handleDataMessage(json);
                handleDataMessage(remoteMessage);

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());

            //getting the title and the body
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            Log.e(TAG, "title_body: " + title+" "+body);
            //then here we can use the title and body to build a notification
            handleNotification(title,body);
        }


    }



    private void handleNotification(String title, String body) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
            String format = s.format(new Date());

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Log.e("rr22","pppp22");
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", body);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.showNotificationMessage(title, body, format, resultIntent);
                notificationUtils.playNotificationSound();
            }else{
                Log.e("rrrr","pppp");
                sendNoti(title, body);
                // If the app is in background, firebase itself handles the notification
            }
        }

    }

    private void sendNoti(String title, String body) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("NOTIFICATION", "NOTIFICATION");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(this);
        String channelId = "Default";

        b.setAutoCancel(false);
        b.setDefaults(Notification.DEFAULT_ALL);
        b.setWhen(System.currentTimeMillis());
        b.setSmallIcon(R.drawable.community);
       // b.setTicker("Hearty365");
        b.setContentTitle(title);
        b.setContentText(body);
        b.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
        b.setContentIntent(contentIntent);
       // b.setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1, b.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void handleDataMessage(RemoteMessage remoteMessage) {
       // Log.e(TAG, "push_json: " + json.toString());
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new Date());
        try {
            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);
            Log.e("JSON OBJECT", object.toString());
            String title = object.getString("title");
            String body = object.getString("body");
            System.out.println("check number" + title);


//            JSONObject data = json.getJSONObject("data");
//
//            String title = data.getString("title");
//            String message = data.getString("message");
//            boolean isBackground = data.getBoolean("is_background");
//            String imageUrl = data.getString("image");
//            String timestamp = data.getString("timestamp");
//            JSONObject payload = data.getJSONObject("payload");
//
//            Log.e(TAG, "title: " + title);
//            Log.e(TAG, "message: " + message);
//            Log.e(TAG, "isBackground: " + isBackground);
//            Log.e(TAG, "payload: " + payload.toString());
//            Log.e(TAG, "imageUrl: " + imageUrl);
//            Log.e(TAG, "timestamp: " + timestamp);
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", body);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                Log.d("BroadcastReceiver::", "BroadcastReceiver");
                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.showNotificationMessage(title, body, format, resultIntent);
                notificationUtils.playNotificationSound();

            } else {
                // app is in background, show the notification in notification tray

                resultIntent.putExtra("message", body);

                showNotificationMessage(getApplicationContext(), title, body, format, resultIntent);

                // check for image attachment
//                if (TextUtils.isEmpty(imageUrl)) {
                    //showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
//                } else {
//                    // image is present, show notification with image
//                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
//                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {

        Log.e("rr22666","pppp22111");
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

}

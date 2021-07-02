package au.com.bluedot.clevertapdemoapp;

import android.os.Bundle;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.pushnotification.NotificationInfo;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Adil Bhatti on 7/11/18.
 */
public class MainFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message){
        try {
            if (message.getData().size() > 0) {
                Bundle extras = new Bundle();
                for (Map.Entry<String, String> entry : message.getData().entrySet()) {
                    extras.putString(entry.getKey(), entry.getValue());
                }

                NotificationInfo info = CleverTapAPI.getNotificationInfo(extras);

                if (info.fromCleverTap) {
                    CleverTapAPI.createNotification(getApplicationContext(), extras);
                } else {
                    // not from CleverTap handle yourself or pass to another provider
                }
            }
        } catch (Throwable t) {
            System.out.println("MYFCMLIST -- Error parsing FCM message: "+t.getMessage());
        }
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
        System.out.println("-- onSendError: " + s + " e: " + e.getMessage());
    }

    @Override
    public void onNewToken(String token) {
        CleverTapAPI.getDefaultInstance(this).pushFcmRegistrationId(token,true);
        System.out.println("Token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }
}

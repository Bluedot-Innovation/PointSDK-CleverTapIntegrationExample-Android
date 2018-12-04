package au.com.bluedot.clevertapdemoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.clevertap.android.sdk.CleverTapAPI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        try {
            CleverTapAPI.getDefaultInstance(this).pushNotificationClickedEvent(intent.getExtras());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        super.onNewIntent(intent);
    }
}

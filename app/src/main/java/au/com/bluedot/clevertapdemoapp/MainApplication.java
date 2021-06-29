package au.com.bluedot.clevertapdemoapp;

import android.Manifest;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bluedot.application.model.Proximity;
import au.com.bluedot.point.ApplicationNotificationListener;
import au.com.bluedot.point.ServiceStatusListener;
import au.com.bluedot.point.net.engine.BDError;
import au.com.bluedot.point.net.engine.BeaconInfo;
import au.com.bluedot.point.net.engine.FenceInfo;
import au.com.bluedot.point.net.engine.InitializationResultListener;
import au.com.bluedot.point.net.engine.LocationInfo;
import au.com.bluedot.point.net.engine.ServiceManager;
import au.com.bluedot.point.net.engine.ZoneInfo;

import static android.app.Notification.PRIORITY_MAX;

/**
 * Created by Adil Bhatti on 17/05/16.
 */
public class MainApplication extends Application implements InitializationResultListener {

    private ServiceManager serviceManager;
    private final String projectId = ""; //Project Id for the Point Demo App

    @Override
    public void onCreate() {
        super.onCreate();

        //Start the CleverTap SDK.
        initCleverTap();

        //Start the Bluedot Point SDK
        initPointSDK();
    }

    public void initPointSDK() {

        int checkPermissionCoarse = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int checkPermissionFine = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if(checkPermissionCoarse == PackageManager.PERMISSION_GRANTED && checkPermissionFine == PackageManager.PERMISSION_GRANTED) {
            serviceManager = ServiceManager.getInstance(this);

            if(!serviceManager.isBlueDotPointServiceRunning()) {
                serviceManager.initialize(projectId, this);
            }
        }
        else
        {
            requestPermissions();
        }
    }

    private void initCleverTap() {
        ActivityLifecycleCallback.register(this);
    }

    private void requestPermissions() {

        Intent intent = new Intent(getApplicationContext(), RequestPermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onInitializationFinished(@Nullable BDError bdError) {
        if (bdError != null){
            Toast.makeText(getApplicationContext(),
                    "Bluedot Initialization Error " + bdError.getReason(),
                    Toast.LENGTH_LONG).show();

            return;
        }
    }
}

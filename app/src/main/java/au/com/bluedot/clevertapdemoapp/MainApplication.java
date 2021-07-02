package au.com.bluedot.clevertapdemoapp;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.clevertap.android.sdk.ActivityLifecycleCallback;

import org.jetbrains.annotations.Nullable;

import au.com.bluedot.point.net.engine.BDError;
import au.com.bluedot.point.net.engine.InitializationResultListener;
import au.com.bluedot.point.net.engine.ServiceManager;

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

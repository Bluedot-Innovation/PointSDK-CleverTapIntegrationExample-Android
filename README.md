# CleverTap Events Example

A sample project used to test the integration between CleverTap SDK and Bluedot Point SDK.

## Getting started

This project depends on `PointSDK-Android` and `clevertap-android-sdk`. Both dependencies are managed by Gradle.

### Implement `PointSDK-Android`

1. Add `PointSDK-Android` module as a dependency to your application.

```gradle
dependencies {
    ...
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation 'com.google.android.material:material:1.1.0-alpha04'
    implementation 'com.gitlab.bluedotio.android:point_sdk_android:15.0.0'
}
```

2. Request permissions in `AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.VIBRATE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
```

3. Start `PointSDK` in the application's `onCreate`

```java
@Override
public void onCreate() {
    super.onCreate();

    ...

    // start Point SDK
    int checkPermissionCoarse = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
    int checkPermissionFine = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);

    if(checkPermissionCoarse == PackageManager.PERMISSION_GRANTED && checkPermissionFine == PackageManager.PERMISSION_GRANTED) {
        serviceManager = ServiceManager.getInstance(this);

        if(!serviceManager.isBlueDotPointServiceRunning()) {
            // Setting Notification for foreground service, required for Android Oreo and above.
            // Setting targetAllAPIs to TRUE will display foreground notification for Android versions lower than Oreo
            serviceManager.setForegroundServiceNotification(createNotification(), false);
            serviceManager.sendAuthenticationRequest("Your Bluedot API key", this, false);
        }
    }
    else
    {
        requestPermissions();
    }
}
```

4. Implement `Point SDK` callbacks

```java
@Override
public void onCheckIntoFence(FenceInfo fenceInfo, ZoneInfo zoneInfo, LocationInfolocationInfo, Map<String, String> customDataMap, boolean b) {
    ...
}

@Override
public void onCheckedOutFromFence(FenceInfo fenceInfo, ZoneInfo zoneInfo, int dwellTime,Map<String, String> customDataMap) {
    ...
}

@Override
public void onCheckIntoBeacon(BeaconInfo beaconInfo, ZoneInfo zoneInfo, LocationInfolocationInfo, Proximity proximity, Map<String, String> customDataMap, boolean b) {
    ...
}

@Override
public void onCheckedOutFromBeacon(BeaconInfo beaconInfo, ZoneInfo zoneInfo, int dwellTime,Map<String, String> customDataMap) {
    ...
}
```

### Implement `clevertap-android-sdk`

CleverTap has to be initialised before sending any check-in/check-out events.

1. Add the `clevertap-android-sdk` module as a dependency in your application.

```gradle
dependencies {
    ...

    implementation 'com.clevertap.android:clevertap-android-sdk:3.7.2'
    implementation 'com.android.installreferrer:installreferrer:1.0'
    implementation 'androidx.appcompat:appcompat:1.0.0'
    implementation 'androidx.core:core:1.0.0'
    implementation 'com.google.firebase:firebase-messaging:19.0.1'
}
```

2. Add the following inside the <application></application> tags of your AndroidManifest.xml

```
<meta-data  
     android:name="CLEVERTAP_ACCOUNT_ID"  
     android:value="Your CleverTap Account ID"/>  
<meta-data  
     android:name="CLEVERTAP_TOKEN"  
     android:value="Your CleverTap Account Token"/>
```

3. Manually `takeOff` in the application's `onCreate`

```java
@Override
public void onCreate() {
    super.onCreate();

    ...

    // Initialise CleverTap 
    ActivityLifecycleCallback.register(this);
}
```

or add `Autopilot` configuration to `AndroidManifest.xml`

```xml
<application
    android:label="@string/app_name"
    android:icon="@drawable/ic_launcher"
    android:name="com.clevertap.android.sdk.Application">
```

4. Track `CleverTap` events in your checkins/checkouts

```java
@Override
public void onCheckIntoFence(FenceInfo fenceInfo, ZoneInfo zoneInfo, LocationInfo locationInfo, Map<String, String> customDataMap, boolean b) {
    CleverTapAPI cleverTap = CleverTapAPI.getDefaultInstance(getApplicationContext());
    HashMap<String, Object> checkInAction = new HashMap<String, Object>
    checkInAction.put("bluedot_zone_id", zoneInfo.getZoneId());
    checkInAction.put("bluedot_zone_name", zoneInfo.getZoneName());
    if(customDataMap != null && !customDataMap.isEmpty()) {
        for(Map.Entry<String, String> data : customDataMap.entrySet()) {
            checkInAction.put(data.getKey(), data.getValue());
        }
    }

    cleverTap.event.push("bluedot_entry", checkInAction);
}

@Override
public void onCheckedOutFromFence(FenceInfo fenceInfo, ZoneInfo zoneInfo, int dwellTime, Map<String, String> customDataMap) { {
    CleverTapAPI cleverTap = CleverTapAPI.getDefaultInstance(getApplicationContext());
    HashMap<String, Object> checkInAction = new HashMap<String, Object>
    checkInAction.put("bluedot_zone_id", zoneInfo.getZoneId());
    checkInAction.put("bluedot_zone_name", zoneInfo.getZoneName());
    if(customDataMap != null && !customDataMap.isEmpty()) {
        for(Map.Entry<String, String> data : customDataMap.entrySet()) {
            checkInAction.put(data.getKey(), data.getValue());
        }
    }

    if(dwellTime != -1) {
        checkInAction.put("dwell_time", dwellTime);
    }

    cleverTap.pushEvent("bluedot_entry", checkInAction);}
```

## Next steps
Full documentation can be found at https://docs.bluedot.io/android-sdk/ and https://developer.clevertap.com/docs/android respectivelly.

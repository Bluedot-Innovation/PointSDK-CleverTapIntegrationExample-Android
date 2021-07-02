package au.com.bluedot.clevertapdemoapp;

import android.content.Context;
import android.util.Log;

import com.clevertap.android.sdk.CleverTapAPI;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bluedot.point.net.engine.GeoTriggeringEventReceiver;
import au.com.bluedot.point.net.engine.ZoneEntryEvent;
import au.com.bluedot.point.net.engine.ZoneExitEvent;
import au.com.bluedot.point.net.engine.ZoneInfo;

public class BluedotGeoTriggerReceiver extends GeoTriggeringEventReceiver {
    private final String TAG = "BluedotApp";

    @Override
    public void onZoneInfoUpdate(@NotNull List<ZoneInfo> list, @NotNull Context context) {
        Log.i(TAG, "Zones updated at: " + new Date().toString()
                + " ZoneInfos count: " + list.size());
    }

    @Override
    public void onZoneEntryEvent(@NotNull ZoneEntryEvent zoneEntryEvent, @NotNull Context context) {
        Log.i(TAG, "Zones entered at: " + new Date().toString()
                + " Zone name:" + zoneEntryEvent.toString());
        sendCustomEvent(
                "bluedot-entry",
                zoneEntryEvent.getZoneInfo(),
                -1,
                zoneEntryEvent.getZoneInfo().getCustomData(),
                context);
    }

    @Override
    public void onZoneExitEvent(@NotNull ZoneExitEvent zoneExitEvent, @NotNull Context context) {
        Log.i(TAG, "Zones exited at: " + new Date().toString()
                + " Zone name:" + zoneExitEvent.toString());
        sendCustomEvent(
                "bluedot-exit",
                zoneExitEvent.getZoneInfo(),
                zoneExitEvent.getDwellTime(),
                zoneExitEvent.getZoneInfo().getCustomData(),
                context);
    }

    private void sendCustomEvent(String eventName, ZoneInfo zoneInfo, int dwellTime, Map<String, String> customDataMap, Context context) {
        CleverTapAPI cleverTap = CleverTapAPI.getDefaultInstance(context);
        HashMap<String, Object> checkInAction = new HashMap<String, Object>();
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

        cleverTap.pushEvent(eventName, checkInAction);
    }
}

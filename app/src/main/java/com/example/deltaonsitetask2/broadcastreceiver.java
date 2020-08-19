package com.example.deltaonsitetask2;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class broadcastreceiver extends BroadcastReceiver {

    SharedPreferences prefs;
    static int hour;
    static int minutes;

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar c = Calendar.getInstance();
        prefs = context.getSharedPreferences("STORAGE",
                Context.MODE_PRIVATE);
        hour = prefs.getInt("hour", 1);
        minutes = prefs.getInt("minutes", 1);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED && c.get(Calendar.MINUTE) == minutes && c.get(Calendar.HOUR_OF_DAY) == hour) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(prefs.getString("phone", ""), null, prefs.getString("message", ""), null, null);
        }
    }
}

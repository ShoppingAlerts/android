package com.example.sofiya.smartshoppinglist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.sofiya.smartshoppinglist.services.ShoppingService;

/**
 * Created by sofiya on 3/25/15.
 */
public class ShoppingAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.example.sofiya.alarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, ShoppingService.class);
        context.startService(i);
    }
}
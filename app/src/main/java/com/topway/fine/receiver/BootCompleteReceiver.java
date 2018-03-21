package com.topway.fine.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.topway.fine.service.PhoneStatusService;

public class BootCompleteReceiver extends BroadcastReceiver {

    private static final String TAG = "BootCompleteReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //手机启动完毕，监视到了手机启动的广播事件，开启后台监听的服务
        Intent i = new Intent(context,PhoneStatusService.class);
        context.startService(i);
    }

}
package com.topway.fine.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneStatusService extends Service {

    private TelephonyManager phonyManager;
    private MyPhoneListener phoneListener;
    private MediaRecorder mediaRecorder;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    //当服务第一次被创建的时候执行，由系统执行的
    @Override
    public void onCreate() {
        //获取手机电话状态管理的服务
        phonyManager  = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);

        //创建一个监听器，监听电话呼叫状态的变化
        phoneListener = new MyPhoneListener();

        //开始监听用户的通话状态
        phonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    //当服务被停止的时候调用
    @Override
    public void onDestroy() {
        //服务停止取消电话的监听器
        phonyManager.listen(phoneListener, PhoneStateListener.LISTEN_NONE);
        phoneListener = null;
        super.onDestroy();
    }
    
    private class MyPhoneListener extends PhoneStateListener{
        //当手机呼叫状态变化的时候执行下面代码
        //state 电话的状态
        //incomingnumber 来电号码
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            try {
                //判断我们当前手机的通话状态
                switch (state) {
                    //手机处于空闲状态，没有人打电话 没有零响
                    case TelephonyManager.CALL_STATE_IDLE:
                        break;
                    //手机零响状态
                    case TelephonyManager.CALL_STATE_RINGING:
                        break;
                    //电话接通状态，用户正在打电话
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.i("Phone:","来点号码" + incomingNumber);
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
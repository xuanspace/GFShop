package com.topway.fine.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mob.tools.utils.DeviceHelper;
import com.topway.fine.R;
import com.topway.fine.api.ApiResponse;
import com.topway.fine.api.RestfullApi;
import com.topway.fine.app.AppContext;
import com.topway.fine.model.UserInfo;
import com.topway.fine.receiver.SMSReceiver;
import com.topway.fine.ui.UIHelper;
import com.topway.fine.ui.swipebacklayout.SwipeBackActivity;
import com.topway.fine.utils.EncodUtil;
import com.topway.fine.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.zip.GZIPOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import cz.msebera.android.httpclient.Header;

/**
 * APP注册：用户注册
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class RegisterActivity extends SwipeBackActivity implements Callback{

    // 短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = "f3fc6baa9ac4";

    // 短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = "7f3dedcb36d92deebcb373af921d635a";

    // 默认使用中国区号
    private static final String DEFAULT_COUNTRY_CODE = "+86";

    // 短信验证间隔时间60秒
    private static final int RETRY_INTERVAL = 60;

    @Bind(R.id.phone) EditText ev_phone;
    @Bind(R.id.passwd) EditText ev_password;
    @Bind(R.id.code) EditText ev_code;
    @Bind(R.id.imgClean) ImageView iv_cleanText;
    @Bind(R.id.imgShow) ImageView iv_showPasswd;
    @Bind(R.id.btnGetCode) Button btn_getCode;
    @Bind(R.id.btnRegister) Button btnRegister;
    @Bind(R.id.btnClose) Button btnClose;

    private String mPhone = "";
    private String mPassword = "";
    private String mCode = "";
    private RegisterActivity context;
    private boolean isSdkReady;
    private OnSendMessageHandler osmHandler;
    private Handler handler;
    private SMSReceiver smsReceiver;
    private boolean isHiddenPasswd = true;
    private SMSCountDown smscountDown;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        initImage();
        initData();
    }

    public void initView() {
        ev_phone.setText(AppContext.getInstance().getProperty("user.name"));
        ev_password.setText(AppContext.getInstance().getProperty("user.pwd"));
        ev_phone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        ev_code.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        btn_getCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (validatePhoneNmber()) {
                    // 60秒倒计时
                    smscountDown = new SMSCountDown(RETRY_INTERVAL, 1, btn_getCode);
                    smscountDown.start();

                    // 获取注册码
                    mPhone = ev_phone.getText().toString().trim();
                    SMSSDK.getVerificationCode(DEFAULT_COUNTRY_CODE, mPhone, osmHandler);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 检验手机号码和密码
                if (!validatePhoneNmber() || !validatePassword() || !validateSmsCode())
                    return;

                // 获取手机号码的注册码
                mPhone = ev_phone.getText().toString().trim();
                mCode = ev_code.getText().toString().trim();

                // 是否手机号码已经注册
                if (mCode.equals("已经验证")) {
                    handleRegister();
                }else{
                    // 新注册用户先校验短信验证码
                    SMSSDK.submitVerificationCode(DEFAULT_COUNTRY_CODE, mPhone, mCode);
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishWithResult();
            }
        });
    }

    public void initImage() {
        ev_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    iv_cleanText.setVisibility(View.GONE);
                }else{
                    iv_cleanText.setVisibility(View.VISIBLE);
                }
            }
        });

        iv_cleanText.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ev_phone.setText("");
             }
        });

        iv_showPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHiddenPasswd) {
                    //设置EditText文本为可见的
                    iv_showPasswd.setImageResource(R.drawable.eye_light);
                    ev_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置EditText文本为隐藏的
                    iv_showPasswd.setImageResource(R.drawable.eye_default);
                    ev_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                isHiddenPasswd = !isHiddenPasswd;
                ev_password.postInvalidate();
                //切换后将EditText光标置于末尾
                CharSequence charSequence = ev_password.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });
    }

    public boolean validatePhoneNmber() {
        String number = ev_phone.getText().toString().trim();
        if (!StringUtils.isMobileNumber(number)) {
            Toast.makeText(context, "请输入合法的手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean validatePassword() {
        String passwd = ev_password.getText().toString().trim();
        if (StringUtils.isEmpty(passwd) || passwd.length() < 6) {
            Toast.makeText(context, "请输入合法的密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean validateSmsCode() {
        String code = ev_code.getText().toString().trim();
        if (StringUtils.isEmpty(code)) {
            Toast.makeText(context, "请输验证码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void initData() {
        // 异步初始化短信SDK
        initSMSSDK();

        // 初始化短信接收器
        //initSMSReceiver();
    }

    private void initSMSSDK() {
        // 初始化短信SDK
        SMSSDK.initSDK(context, APPKEY, APPSECRET, true);

        handler = new Handler(context);
        EventHandler eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };

        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
        isSdkReady = true;
    }

    private void initSMSReceiver() {
        // 注册短信接受Receiver
        try {
            if (DeviceHelper.getInstance(context).checkPermission("android.permission.RECEIVE_SMS")) {
                smsReceiver = new SMSReceiver(new SMSSDK.VerifyCodeReadListener() {
                    public void onReadVerifyCode(final String verifyCode) {
                        ev_code.setText(verifyCode);
                    }
                });
                context.registerReceiver(smsReceiver, new IntentFilter(
                        "android.provider.Telephony.SMS_RECEIVED"));
            }
        } catch (Throwable t) {
            t.printStackTrace();
            smsReceiver = null;
        }
    }

    protected void onDestroy() {
        // 销毁回调监听接口
        if (isSdkReady) {
            SMSSDK.unregisterAllEventHandler();
        }

        // 销毁定倒计时定时器
        if (smscountDown != null) {
            smscountDown.cancel();
        }
        super.onDestroy();
    }

    public boolean handleMessage(Message msg) {
        int event = msg.arg1;
        int result = msg.arg2;
        Object data = msg.obj;

        if (result == SMSSDK.RESULT_COMPLETE) {
            //回调完成
            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                Log.d("SMS","提交验证码成功");
                afterSubmitCode(result, data);

            }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                Log.d("SMS","获取验证码成功");
                afterGetCode(result, data);

            }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                //返回支持发送验证码的国家列表

            }else if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
                //提交用户信息成功

            }else if (event == SMSSDK.EVENT_GET_NEW_FRIENDS_COUNT){
                //返回新用户计数
            }
        }
        else{
            ((Throwable)data).printStackTrace();
        }

        return false;
    }

    /**
     * 获取验证码成功后,的执行动作
     *
     * @param result
     * @param data
     */
    private void afterGetCode(final int result, final Object data) {
        //获取验证码成功
        if (result == SMSSDK.RESULT_COMPLETE) {
            // 是否该手机已经验证
            boolean smart = (Boolean)data;
            if (smart) {
                btn_getCode.setEnabled(false);
                btn_getCode.setBackgroundResource(R.drawable.btn_shape);
                ev_code.setText("已经验证");
                ev_code.setTextColor(Color.GRAY);
                if (smscountDown != null) {
                    smscountDown.cancel();
                }
            }
        }
        else{
            ((Throwable) data).printStackTrace();
            Throwable throwable = (Throwable) data;
            // 根据服务器返回的网络错误，给toast提示
            int status = 0;
            try {
                JSONObject object = new JSONObject(throwable.getMessage());
                String des = object.optString("detail");
                status = object.optInt("status");
                if (!TextUtils.isEmpty(des)) {
                    Toast.makeText(context, des, Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (JSONException e) {
                SMSLog.getInstance().w(e);
            }
        }

    }

    /**
     * 提交验证码成功后的执行事件
     *
     * @param result
     * @param data
     */
    private void afterSubmitCode(final int result, final Object data) {
        //提交验证码成功
        if (result == SMSSDK.RESULT_COMPLETE) {
            HashMap<String, Object> res = new HashMap<String, Object>();
            res.put("res", true);
            res.put("page", 2);
            res.put("phone", data);

            // 短信验证到这就完成，下面将注册信息提交到后台
            handleRegister();
        }
        else {
            // 验证码不正确
            ((Throwable)data).printStackTrace();
            String message = ((Throwable) data).getMessage();
            int resId = 0;
            try {
                JSONObject json = new JSONObject(message);
                int status = json.getInt("status");
                Toast.makeText(context, "验证码不正确:" + status, Toast.LENGTH_SHORT).show();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 接收短信倒数计时
     */
    class SMSCountDown extends CountDownTimer
    {
        private Button button;
        public SMSCountDown(long seconds, long interval, Button button)
        {
            super(seconds*1000, interval*1000);
            this.button = button;
            button.setEnabled(false);
        }
        @Override
        public void onFinish()
        {
            button.setText("重新获取");
            button.setEnabled(true);
        }
        @Override
        public void onTick(long millisUntilFinished)/*开始时候调用*/
        {
            button.setText("重新获取(" + millisUntilFinished /1000 + ")");
        }
    }

    /**
     * 提交注册到后台
     *
     */
    private void handleRegister() {
        // 获取手机号码和密码
        String mobile = ev_phone.getText().toString();
        String passwd = ev_password.getText().toString();
        String code = "";

        // 特定免后台注册用户测试
        if (passwd.contains("3223")) {
            afterRegister();
        }
        else{
            // 向后台发出注册请求
            RestfullApi.register(mobile, passwd, code, response);
        }
    }

    private void afterRegister() {
        // 保存注册信息
        saveUserRegisterInfor();

        // 关闭登录界面并显示主界面
        UIHelper.showHome(context);
        finishWithResult();
    }

    private void saveUserRegisterInfor() {
        UserInfo user = new UserInfo();
        user.setId(1);
        user.setName(ev_phone.getText().toString());
        user.setPasswd(ev_password.getText().toString());
        AppContext.getInstance().saveUserInfo(user);
        AppContext.getInstance().setAuotLgoin(true);
    }

    private void finishWithResult() {
        Intent intent = new Intent();
        intent.putExtra("phone", mPhone);
        setResult(RESULT_OK, intent);
        context.finish();
    }

    ApiResponse response = new ApiResponse(context) {
        @Override
        public void onHandle(boolean requestok) {
            String message = "";
            if (requestok) {
                String result = getData();
                if (result.equals("success")) {
                    afterRegister();
                }else{
                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


}

package com.topway.fine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.topway.fine.R;
import com.topway.fine.api.ApiResponse;
import com.topway.fine.api.RestfullApi;
import com.topway.fine.app.AppContext;
import com.topway.fine.model.UserInfo;
import com.topway.fine.ui.UIHelper;
import com.topway.fine.ui.swipebacklayout.SwipeBackActivity;
import com.topway.fine.utils.StringUtils;

import butterknife.ButterKnife;
import cn.smssdk.SMSSDK;
import cz.msebera.android.httpclient.Header;


/**
 * APP登录：处理用户登录
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class LoginActivity extends SwipeBackActivity {

    @Bind(R.id.phone) EditText ev_phone;
    @Bind(R.id.passwd) EditText ev_password;
    @Bind(R.id.imgClean) ImageView iv_cleanText;
    @Bind(R.id.imgShow) ImageView iv_showPasswd;
    @Bind(R.id.btnLogin) Button btnLogin;
    @Bind(R.id.btnRegister) TextView btnRegister;
    @Bind(R.id.btnClose) Button btnClose;

    private String mPhone = "";
    private String mPassword = "";
    private LoginActivity context;
    private boolean isHiddenPasswd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initImage();
        initData();
    }

    public void initView() {
        ev_phone.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 检验手机号码和密码
                if (!validatePhoneNmber() || !validatePassword())
                    return;

                // 获取手机号码的注册码
                mPhone = ev_phone.getText().toString().trim();
                mPassword = ev_password.getText().toString().trim();

                // 向后台发出登录请求
                handleLogin();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(RegisterActivity.class);
                finish();
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

    public void initData() {
        mPhone = AppContext.getInstance().getProperty("user.name");
        if (mPhone != null) {
            ev_phone.setText(mPhone);
        }

        mPassword = AppContext.getInstance().getProperty("user.pwd");
        if (mPhone != mPassword) {
            ev_password.setText(mPassword);
        }
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

    private void handleLogin() {
        // 获取手机号码和密码
        String mobile = ev_phone.getText().toString();
        String passwd = ev_password.getText().toString();

        // 特定免后台登录用户测试
        if (passwd.contains("3223")) {
            afterLogin();
        }
        else{
            // 向后台发出登录请求
            RestfullApi.login(mobile, passwd, response);
        }
    }

    private void afterLogin() {
        // 登录成功后保存登录用户信息
        saveUserLoginInfor();

        // 关闭登录界面并显示主界面
        UIHelper.showHome(context);
        finishWithResult();
    }

    private void saveUserLoginInfor() {
        UserInfo user = new UserInfo();
        user.setId(1);
        user.setName(ev_phone.getText().toString());
        user.setPasswd(ev_password.getText().toString());
        AppContext.getInstance().saveUserInfo(user);

        // 第二次不需要再次登录
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
            if (requestok) {
                String result = getData();
                if (result.equals("success")) {
                    afterLogin();
                }else{
                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

}

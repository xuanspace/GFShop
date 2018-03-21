package com.topway.fine.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.topway.fine.R;
import com.topway.fine.adapter.PhoneContactAdapter;
import com.topway.fine.api.PhoneBelong;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.model.PhoneContact;
import com.topway.fine.model.PhoneData;


import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 联系人详细信息
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class PhoneContactDetailActivity extends BaseActivity {

    static final int HANDLER_LASTTIME = 1;

    @Bind(R.id.tv_name) TextView tv_name;
    @Bind(R.id.tv_nickname) TextView tv_nickname;
    @Bind(R.id.ll_contacts) LinearLayout ll_contacts;

    private PhoneContactDetailActivity context;
    private PhoneContactAdapter adapter;
    private PhoneContact contact;
    private boolean isLongClickMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_contact);
        setActivityTitle("");
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        isLongClickMode = false;
    }

    private void initData() {
        int position = getPosition();
        adapter = new PhoneContactAdapter(context);
        contact = (PhoneContact) adapter.getItem(position);
        if (contact == null)
            return;

        tv_name.setText(contact.getName());
        tv_nickname.setText("昵称：" + contact.getNickName());

        for (int i = 0; i < contact.data.size(); i++) {
            PhoneData item = contact.data.get(i);
            if (item.type.contains("/nickname")) {
                continue;
            }
            if (item.data1 == null || item.data1.isEmpty()) {
                continue;
            }
            item.transData2();
            addContactViews(item);
        }
    }

    private View addContactViews(PhoneData item) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_contact_item, null);
        view.setLayoutParams(lp);

        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView tv_type = (TextView) view.findViewById(R.id.tv_type);
        TextView tv_data = (TextView) view.findViewById(R.id.tv_data);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        //tv_type.setText(item.data2);
        tv_data.setText(item.data1);
        iv_icon.setImageResource(item.getImage());

        view.setTag(item);
        if (item.isPhone()) {
            // 获取手机归属地
            PhoneBelong.get(item.data1, tv_type);

            // 获取手机最后通讯时间
            getPhoneLastTime(item.data1, tv_time);

            // 调用打电话界面
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    PhoneData value = (PhoneData) v.getTag();
                    if (isLongClickMode) {
                        return;
                    }
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + value.data1));
                    startActivity(intent);
                }
            });
        }

        if (item.isAddress()) {
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    PhoneData value = (PhoneData) v.getTag();
                    if (isLongClickMode) {
                        return;
                    }
                }
            });

        }

        // 设置长按事件监听
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isLongClickMode = true;
                PhoneData value = (PhoneData) view.getTag();
                showPopMenu(value, view);
                return false;
            }
        });

        ll_contacts.addView(view);
        return view;
    }

    public void showPopMenu(PhoneData item, final View view) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isLongClickMode = false;
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.menu_contact);

        // 删除通讯项
        TextView tv_delete = (TextView) window.findViewById(R.id.tv_delete);
        tv_delete.setTag(item);
        tv_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PhoneData data = (PhoneData) v.getTag();
                adapter.deletePhoneData(contact, data);
                ll_contacts.removeView(view);
                dialog.cancel();
            }
        });

        // 编辑通讯项
        TextView tv_edit = (TextView) window.findViewById(R.id.tv_edit);
        tv_edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_EDIT,Uri.parse("content://com.android.contacts/contacts/"+contact.contact_id));
                startActivity(intent);
                dialog.cancel();
            }
        });

        TextView tv_top = (TextView) window.findViewById(R.id.tv_top);
        tv_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_address:
                    break;
                case R.id.btn_save:
                    break;
            }
        }
    }

    // 获取该电话号码最后通讯的时间
    public void getPhoneLastTime(String number, TextView view) {
        GetPhoneLastTimeAsyncTask task = new GetPhoneLastTimeAsyncTask(view, number);
        task.execute();
    }

    // 创建异步任务去获取最后通讯时间
    public class GetPhoneLastTimeAsyncTask extends AsyncTask<Integer, Integer, String> {
        private TextView view;
        private String number;

        public GetPhoneLastTimeAsyncTask(TextView view, String number) {
            super();
            this.view = view;
            this.number = number;
        }

        @Override
        protected String doInBackground(Integer... params) {
            return adapter.getPhoneLastTime(number);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result!= null && !result.isEmpty())
                view.setText(result);
        }
    }

}

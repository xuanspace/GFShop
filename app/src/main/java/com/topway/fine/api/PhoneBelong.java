package com.topway.fine.api;

import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.topway.fine.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * 获取手机号码归属地
 * @version 1.0
 * @created 2016-3-1
 * @author linweixuan@gmail.com
 */
public class PhoneBelong {

    private final static String BASE_URL = "http://v.showji.com/Locating/showji.com2016234999234.aspx?m=";

    public static void get(String number, final View view) {
        // 去掉+86开头的数字
        if (number.contains("+86"))
            number = number.substring(3);

        // 检查是否手机号码
        if (!StringUtils.isMobileNumber(number))
            return;

        // 异步请求获取手机归属地
        AsyncHttpClient client = new AsyncHttpClient();
        String url = BASE_URL + number + "&output=json&callback=querycallback&timestamp=" + new Date().getTime();
        client.get(BASE_URL+number, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String location = "";
                String city = "";
                if (responseString != null) {
                    // <Province>上海</Province>
                    // <City>上海</City>
                    int pos = 0, end = 0;
                    pos = responseString.indexOf("Province");
                    if (pos != -1) {
                        pos = responseString.indexOf(">", pos + 8);
                        end = responseString.indexOf("<", pos + 1);
                        if (pos != -1 && end != -1 && pos != end - 1)
                            location = responseString.substring(pos + 1, end);
                    }
                    if (end == -1) pos = 0;
                    pos = responseString.indexOf("City", end+1);
                    if (pos != -1) {
                        pos = responseString.indexOf(">", pos + 4);
                        end = responseString.indexOf("<", pos + 1);
                        if (pos != -1 && end != -1 && pos != end - 1) {
                            city = responseString.substring(pos + 1, end);
                        }
                    }

                    if (!location.contains(city)) {
                        location += city;
                    }

                    TextView lable = (TextView) view;
                    lable.setText(location);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
            }
        });
    }
}
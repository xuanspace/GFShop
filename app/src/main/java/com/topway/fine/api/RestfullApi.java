package com.topway.fine.api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * RestfullApi：APP访问后端接口集合
 * @version 1.0
 * @created 2016-3-1
 * @author linweixuan@gmail.com
 */
public class RestfullApi {
    /**
     * 用户登陆
     *
     * @param username 用户名
     * @param password 密码
     * @param response 响应
     */
    public static void login(String username, String password,
                             ApiResponse response) {
        RequestParams params = new RequestParams();
        params.put("mobile", username);
        params.put("password", password);
        String url = "user/login";
        ApiHttpClient.post(url, params, response.getHandler());
    }

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @param response 响应
     */
    public static void register(String username, String password, String code,
                                ApiResponse response) {
        RequestParams params = new RequestParams();
        params.put("mobile", username);
        params.put("password", password);
        String url = "user/register";
        ApiHttpClient.post(url, params, response.getHandler());
    }

    /**
     * 上传图片
     *
     * @param filename 文件名
     * @param response 响应
     */
    public static void upload(String filename, ApiResponse response) {
        RequestParams params = new RequestParams();
        File file = new File(filename);
        if(file.exists() && file.length() > 0){
            try {
                params.put("image", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        String url = "photo/upload";
        ApiHttpClient.post(url, params, response.getHandler());
    }

}
package com.topway.fine.api;

import android.app.Activity;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpStatus;

public class ApiResponse {
    private Activity context;
    private int statusCode;
    private Header[] headers;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private String jsonString;

    public ApiResponse(Activity context) {
        this.context = context;
    }

    public String getData() {
        return getJsonValue("data");
    }

    public String getJsonValue(String name) {
        String value = "";
        if (jsonObject != null) {
            try {
                value = (String) jsonObject.get(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JsonHttpResponseHandler getHandler() {
        return handler;
    }

    public void onHandle(boolean success) {

    }

    private final JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, String response) {
            setStatusCode(statusCode);
            setHeaders(headers);
            setJsonString(response);
            onHandle(true);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            setStatusCode(statusCode);
            setHeaders(headers);
            setJsonObject(response);
            onHandle(true);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            setStatusCode(statusCode);
            setHeaders(headers);
            setJsonArray(response);
            onHandle(true);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            setStatusCode(statusCode);
            setHeaders(headers);
            setJsonObject(errorResponse);
            Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
            onHandle(false);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            setStatusCode(statusCode);
            setHeaders(headers);
            setJsonArray(errorResponse);
            Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
            onHandle(false);
        }

        @Override
        public void onRetry(int retryNo) {

        }

        @Override
        public void onProgress(long bytesWritten, long totalSize) {

        }

        @Override
        public void onFinish() {
            super.onFinish();
        }
    };
}

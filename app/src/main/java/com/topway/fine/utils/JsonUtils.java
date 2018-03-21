package com.topway.fine.utils;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonUtils {

    /** 用于解析json类 */
    private static Gson GSON = new Gson();

    /**
     * 把json字符串转换为JavaBean
     * @param json json字符串
     * @param beanClass JavaBean的Class
     * @return
     */
    public static <T> T fromJson(String json, Class<T> beanClass) {
        T object = null;
        try {
            object = GSON.fromJson(json, beanClass);
        } catch (Exception e) {
            Log.i("JsonUtil", "解析json数据时出现异常\njson = " + json, e);
        }
        return object;
    }

    /**
     * 把json字符串转换为JavaBean。如果json的根节点就是一个集合，则使用此方法<p>
     * type参数的获取方式为：Type type = new TypeToken<集合泛型>(){}.getType();
     * @param json json字符串
     * @return type 指定要解析成的数据类型
     */
    public static <T> T fromJson(String json, Type type) {
        T object = null;
        try {
            object = GSON.fromJson(json, type);
        } catch (Exception e) {
            Log.i("JsonUtil", "解析json数据时出现异常\njson = " + json, e);
        }
        return object;
    }

    /**
     * 把Java对象转换为json字符串
     * @param object Java的对象
     * @return
     */
    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    /**
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T[]> clazz)
    {
        Gson gson = new Gson();
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }

    /**
     * @param json
     * @param clazz
     * @return
     */
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz)
    {
        Type type = new TypeToken<ArrayList<JsonObject>>(){}.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects)
        {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }
}
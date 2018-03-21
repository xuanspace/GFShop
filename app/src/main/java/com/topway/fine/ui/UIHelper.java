package com.topway.fine.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.topway.fine.activity.BrandActivity;
import com.topway.fine.activity.BrandDetailActivity;
import com.topway.fine.activity.CategoryActivity;
import com.topway.fine.activity.CategoryDetailActivity;
import com.topway.fine.activity.EngineListActivity;
import com.topway.fine.activity.HouseDetailActivity;
import com.topway.fine.activity.LoginActivity;
import com.topway.fine.activity.MainActivity;
import com.topway.fine.activity.ManufacturerActivity;
import com.topway.fine.activity.PersonalInfoActivity;
import com.topway.fine.activity.RegisterActivity;
import com.topway.fine.activity.SearchActivity;
import com.topway.fine.activity.SerieListActivity;
import com.topway.fine.activity.SettingActivity;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 */
public class UIHelper {

	public final static String TAG = "UIHelper";

	public final static int RESULT_OK = 0x00;
	public final static int REQUEST_CODE = 0x01;

	public static void ToastMessage(Context cont, String msg) {
        if(cont == null || msg == null) {
            return;
        }
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, int msg) {
        if(cont == null || msg <= 0) {
            return;
        }
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, String msg, int time) {
        if(cont == null || msg == null) {
            return;
        }
		Toast.makeText(cont, msg, time).show();
	}

    public static void showActivity(Activity context, Class<?> cls, Object param) {
        Intent intent = new Intent(context, cls);
        Bundle bundle = new Bundle();
        bundle.putParcelable("entity", (Parcelable)param);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void showHome(Activity context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void showLogin(Activity context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void showRegister(Activity context){
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public static void showHouseDetailActivity(Activity context){
        Intent intent = new Intent(context, HouseDetailActivity.class);
        context.startActivity(intent);
    }

    public static void showCategoryActivity(Activity context, Object param){
        showActivity(context, CategoryActivity.class, param);
    }

    public static void showCategoryDetailActivity(Activity context, Object param){
        showActivity(context, CategoryDetailActivity.class, param);
    }

    public static void showBrandActivity(Activity context){
        Intent intent = new Intent(context, BrandActivity.class);
        context.startActivity(intent);
    }

    public static void showManufacturerActivity(Activity context, Object param){
        showActivity(context, ManufacturerActivity.class, param);
    }

    public static void showEngineActivity(Activity context, int brandId) {
        Intent intent = new Intent(context, EngineListActivity.class);
        context.startActivity(intent);
    }

    public static void showSeriesActivity(Activity context, Object param){
        showActivity(context, SerieListActivity.class, param);
    }

    public static void showBrandDetailActivity(Activity context, Object param){
        showActivity(context, BrandDetailActivity.class, param);
    }

    public static void showSettingActivity(Activity context){
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    public static void showPersonalInfo(Activity context){
        Intent intent = new Intent(context, PersonalInfoActivity.class);
        context.startActivity(intent);
    }

    public static void showSearchActivity(Activity context){
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

}

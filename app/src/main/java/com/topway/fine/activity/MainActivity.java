
package com.topway.fine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioGroup;

import com.topway.fine.R;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.fragment.BufferKnifeFragment;
import com.topway.fine.fragment.CategoryFragment;
import com.topway.fine.fragment.FunctionFragment;
import com.topway.fine.fragment.HomeFragment;
import com.topway.fine.fragment.MainPagerFragment;
import com.topway.fine.fragment.ProfileFragment;
import com.topway.fine.service.PhoneStatusService;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * APP主界面：功能按钮和底部按钮
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FRAGMENT_TAGS = "fragmentTags";
    private static final String CURR_INDEX = "currIndex";
    private static int currIndex = 0;

    private RadioGroup group;
    private ArrayList<String> fragmentTags;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActivityTitle(R.string.home_title);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            initData();
            initView();
            startService();
        } else {
            initFromSavedInstantsState(savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURR_INDEX, currIndex);
        outState.putStringArrayList(FRAGMENT_TAGS, fragmentTags);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initFromSavedInstantsState(savedInstanceState);
    }

    private void initFromSavedInstantsState(Bundle savedInstanceState) {
        currIndex = savedInstanceState.getInt(CURR_INDEX);
        fragmentTags = savedInstanceState.getStringArrayList(FRAGMENT_TAGS);
        showFragment();
    }

    private void initData() {
        currIndex = 0;
        fragmentTags = new ArrayList<>(Arrays.asList("ListFragment", "ImFragment",
                "InterestFragment", "MemberFragment"));
    }

    private void initView() {
        group = (RadioGroup) findViewById(R.id.group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.foot_bar_home: currIndex = 0; break;
                    case R.id.foot_bar_im: currIndex = 1; break;
                    case R.id.foot_bar_interest: currIndex = 2; break;
                    case R.id.main_footbar_user: currIndex = 3; break;
                    default: break;
                }
                showFragment();
            }
        });
        showFragment();
    }

    private void showFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags.get(currIndex));
        if(fragment == null) {
            fragment = instantFragment(currIndex);
        }
        for (int i = 0; i < fragmentTags.size(); i++) {
            Fragment f = fragmentManager.findFragmentByTag(fragmentTags.get(i));
            if(f != null && f.isAdded()) {
                fragmentTransaction.hide(f);
            }
        }
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.fragment_container, fragment, fragmentTags.get(currIndex));
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    private Fragment instantFragment(int currIndex) {
        switch (currIndex) {
            case 0: return new HomeFragment();
            case 1: return new CategoryFragment();
            case 2: return new FunctionFragment();
            case 3: return new ProfileFragment();
            default: return null;
        }
    }

    /**
     * 开启服务
     */
    public void startService(){
        //定义服务的意图对象
        Intent intent = new Intent(this,PhoneStatusService.class);
        startService(intent);
    }

    /**
     * 关闭服务
     */
    public void stopService(){
        Intent intent = new Intent(this,PhoneStatusService.class);
        stopService(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }




}

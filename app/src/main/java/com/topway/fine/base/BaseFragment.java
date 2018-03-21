package com.topway.fine.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.topway.fine.R;
import com.topway.fine.app.AppContext;
import com.topway.fine.async.BrandFilterAsync;
import com.topway.fine.async.BrandHotAsync;

import butterknife.ButterKnife;

/**
 * Fragment基类
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class BaseFragment extends Fragment implements android.view.View.OnClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 500;

    protected BaseFragment mContext;
    protected BaseActivity mActivity;
    protected LayoutInflater mInflater;
    protected View mView;
    protected boolean isReload;
    protected Handler mHandler;
    protected long mLastClickTime = 0;

    public AppContext getApplication() {
        return (AppContext) getActivity().getApplication();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isReload = false;
        if (mView != null) {
            isReload = true;
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
            return mView;
        }

        mContext = this;
        mInflater = inflater;
        mView = inflater.inflate(getLayoutId(), null);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isReload) {
            ButterKnife.bind(this, mView);
            initView();
            initEvent();
            initData();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            mActivity = (BaseActivity)activity;
            mHandler = mActivity.getHandler();
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement from BaseActivity");
        }
    }

    /**
     * 获取当前布局id
     */
    public int getLayoutId() {
        return 0;
    }

    /**
     * 加载layout资源
     * @param resId 资源id
     */
    protected View inflateView(int resId) {
        return this.mInflater.inflate(resId, null);
    }


    /**
     * 初始化各个View
     */
    public void initView() {
    }

    /**
     * 初始化数据
     */
    public void initData() {
    }

    /**
     * 初始化事件
     */
    public void initEvent() {
    }

    /**
     * 通知事件消息
     */
    public void postEvent(Message message) {
        if (mHandler != null) {
            mHandler.sendMessage(message);
        }
    }

    /**
     * 通知事件消息
     */
    public void postEvent(Object obj) {
        if (mHandler != null) {
            Message msg = Message.obtain();
            msg.obj = obj;
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 判断是否快速点击
     */
    public synchronized boolean isDoubleClick() {
        long currentTime = System.currentTimeMillis();
        if ( currentTime - mLastClickTime < MIN_CLICK_DELAY_TIME) {
            return true;
        }
        mLastClickTime = currentTime;
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 异步加载数据回调
     * @param type 数据标签
     * @param data 数据对象
     */
    public void onAsyncData(Object type, Object data) {
    }

    /**
     * Back键事件
     */
    public boolean onBack() {
        return false;
    }

    /**
     * 显示Activity并传递对象
     * @param cls 显示Activity的class
     * @param object 任意对象
     */
    public void showActivity(Class<?> cls, Object object) {
        Intent intent = new Intent(mActivity, cls);
        Bundle bundle = new Bundle();
        bundle.putParcelable("entity", (Parcelable)object);
        //bundle.putParcelable(entity.getClass().getSimpleName(), (Parcelable)entity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 显示Fragment界面
     * @param fragment
     * @param containerViewId 容器id
     */
    protected void showFragment(Fragment fragment, int containerViewId) {
        showFragment(fragment, containerViewId, null, true);
    }

    /**
     * 显示Fragment界面
     * @param fragment
     * @param containerViewId 容器id
     * @param bundle 传递参数
     */
    protected void showFragment(Fragment fragment, int containerViewId, Bundle bundle) {
        showFragment(fragment, containerViewId, bundle, true);
    }

    /**
     * 显示Fragment界面
     * @param fragment
     * @param containerViewId 容器id
     * @param bundle 参数
     * @param isAddBackStack 是否添加到栈
     */
    protected void showFragment(Fragment fragment, int containerViewId, Bundle bundle, boolean isAddBackStack) {
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
        fragmentTransaction.replace(containerViewId, fragment);

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        if(isAddBackStack){
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 返回上一个Fragment界面
     */
    protected void back() {
        //showHideFragment();
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }


}
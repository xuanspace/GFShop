package com.topway.fine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;

/**
 * 上下文弹出菜单
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class ContextMenuAcitivity extends BaseActivity {

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = getIntent().getIntExtra("memu", -1);
        if (type == APP.CONTEXT_GOOD_MENU) {
            setContentView(R.layout.context_menu_list);
        }
        position = getIntent().getIntExtra("position", -1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    public void edit(View view){
        setResult(APP.RESULT_ITEM_EDIT, new Intent().putExtra("position", position));
        finish();
    }
    public void delete(View view){
        setResult(APP.RESULT_ITEM_DELETE, new Intent().putExtra("position", position));
        finish();
    }

}


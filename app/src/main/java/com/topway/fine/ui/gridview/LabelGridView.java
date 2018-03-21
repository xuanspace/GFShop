package com.topway.fine.ui.gridview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class LabelGridView extends GridView {
    public LabelGridView(Context context) {
        super(context);
    }

    public LabelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LabelGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction()==MotionEvent.ACTION_MOVE) {
            return true;
        }
        return super.onTouchEvent(ev);
    }
}

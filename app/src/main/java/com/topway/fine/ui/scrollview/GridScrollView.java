package com.topway.fine.ui.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class GridScrollView extends GridView {

    public GridScrollView(Context context) {
        super(context);
    }

    public GridScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridScrollView(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 重写该方法，达到使GridView适应ScrollView的效果
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

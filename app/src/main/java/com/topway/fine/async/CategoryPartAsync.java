package com.topway.fine.async;

import com.topway.fine.app.APP;
import com.topway.fine.base.BaseAsyncTask;
import com.topway.fine.base.BaseFragment;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Category;

import java.util.List;

/**
 * 获取部件分类异步任务
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class CategoryPartAsync extends BaseAsyncTask<Long, Void, List<Category>> {

    private Long categoryType;

    public CategoryPartAsync(BaseFragment fragment){
        super(fragment);
    }

    protected List<Category> doInBackground(Long... params) {
        categoryType = params[0];
        List<Category> data = DatabaseHelper.instance().getCategoryByFather(categoryType);
        return data;
    }
}

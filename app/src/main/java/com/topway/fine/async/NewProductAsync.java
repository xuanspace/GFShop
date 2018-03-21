package com.topway.fine.async;

import com.topway.fine.base.BaseAsyncTask;
import com.topway.fine.base.BaseFragment;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Category;
import com.topway.fine.model.Product;

import java.util.List;

/**
 * 获取最新的部件产品
 * @version 1.0
 * @created 2017-4-18
 * @aouthor linweixuan@gmial.com
 */

public class NewProductAsync extends BaseAsyncTask<Long, Void, List<Product>> {

    public NewProductAsync(BaseFragment fragment){
        super(fragment);
    }

    protected List<Product> doInBackground(Long... params) {
        int quantity = 10;
        List<Product> data = DatabaseHelper.instance().getNewProduct(quantity);
        return data;
    }
}

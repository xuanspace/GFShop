package com.topway.fine.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.topway.fine.R;
import com.topway.fine.adapter.DraweePagerAdapter;
import com.topway.fine.adapter.GalleryPagerAdapter;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Photo;
import com.topway.fine.model.Product;
import com.topway.fine.ui.photoview.MultiTouchViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import me.relex.photodraweeview.PhotoDraweeView;

import static com.topway.fine.app.AppConfig.IMAGE_DOMAIN;


public class ViewPagerActivity extends Activity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ViewPagerActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_browser);

        /*
        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        onBackPressed();
                    }
                });
        */
        List<String> items;
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            Product product = bundle.getParcelable("entity");
            if (product != null) {
                List<Photo> data = DatabaseHelper.instance().getProductPhotos(product.getId().longValue());
                items = new ArrayList<String>();
                for (int i = 0; i < data.size(); i++) {
                    items.add(data.get(i).getUrl());
                }
            }
        }

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        MultiTouchViewPager viewPager = (MultiTouchViewPager) findViewById(R.id.view_pager);
        GalleryPagerAdapter adapter = new GalleryPagerAdapter(this);


        items = new ArrayList<String>(Arrays.asList(
                "http://192.168.1.100:8000/uploads/33ad43b7338b0478c18934502f111f26.jpg",
                IMAGE_DOMAIN + "/ads/ads2.jpg",
                IMAGE_DOMAIN + "/ads/ads3.jpg",
                IMAGE_DOMAIN + "/ads/ads4.jpg"));

        adapter.setImages(items);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
    }

}


package com.sohu.focus.salesmaster.progress.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.kernal.imageloader.FocusImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import com.sohu.focus.salesmaster.base.BaseActivity;
import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;
import com.sohu.focus.salesmaster.uiframe.PhotoViewPager;

/**
 * 预览照片
 * Created by yuanminjia on 2017/11/1.
 */

public class ViewPhotoActivity extends BaseActivity {

    @BindView(R.id.view_photo_viewpager)
    PhotoViewPager viewPager;
    @BindView(R.id.view_photo_tip)
    TextView tip;
    @BindView(R.id.view_photo_delete)
    ImageView delete;
    @BindView(R.id.gallery_top)
    RelativeLayout top;
    private ArrayList<String> mImgPaths;
    private int mCurPostion;
    private MyPageAdapter mPageAdapter;
    private boolean needShowDelete = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_photo);
        ButterKnife.bind(this);
        initDefaultData();
        initView();
    }

    public void initDefaultData() {
        mImgPaths = getIntent().getStringArrayListExtra(SalesConstants.EXTRA_PHOTO_PATHS);
        mCurPostion = getIntent().getIntExtra(SalesConstants.EXTRA_PHOTO_POSITION, 0);
        needShowDelete = getIntent().getBooleanExtra(SalesConstants.EXTRA_GALLERY_OPTION, false);
    }

    public void initView() {
        if (!needShowDelete) {
            top.setVisibility(View.GONE);
        }
        mPageAdapter = new MyPageAdapter(this);
        viewPager.setAdapter(mPageAdapter);
        viewPager.setCurrentItem(mCurPostion);
        tip.setText((mCurPostion + 1) + "/" + mImgPaths.size());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurPostion = position;
                tip.setText((mCurPostion + 1) + "/" + mImgPaths.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.view_photo_back)
    void goBack() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(SalesConstants.EXTRA_PHOTO_PATHS, mImgPaths);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(SalesConstants.EXTRA_PHOTO_PATHS, mImgPaths);
        setResult(RESULT_OK, intent);
        super.onBackPressed();

    }

    @OnClick(R.id.view_photo_delete)
    void delete() {
        if (mImgPaths.size() > 1) {
            mImgPaths.remove(mCurPostion);
            mPageAdapter.notifyDataSetChanged();
            tip.setText((mCurPostion + 1) + "/" + mImgPaths.size());
        } else if (mImgPaths.size() == 1) {
            mImgPaths.remove(mCurPostion);
            mPageAdapter.notifyDataSetChanged();
            tip.setText("0/0");
        } else {
            ToastUtil.toast("已经没有照片了");
        }

    }

    private class MyPageAdapter extends PagerAdapter {
        Context mContext;

        MyPageAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mImgPaths.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) (container)).removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.viewpager_img_browser, null);
            container.addView(view);
            PhotoView pv = (PhotoView) view.findViewById(R.id.img_browser_photoview);
            if (!needShowDelete) {
                pv.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float x, float y) {
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra(SalesConstants.EXTRA_PHOTO_PATHS, mImgPaths);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
            FocusImageLoader.getLoader(mContext).load(mImgPaths.get(position)).fitCenter().into(pv).display();
            return view;

        }
    }
}

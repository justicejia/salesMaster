package com.sohu.focus.salesmaster.uiframe.easyrecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.RecyclerArrayAdapter;



/**
 * 如果需要recyclerview拉到最下面的时候有个留白，使用该类
 * Created by zhaoqiang on 2017/9/25.
 */

public class SpaceFooter implements RecyclerArrayAdapter.ItemView {

    private Context context;
    private int height;

    public SpaceFooter(Context context) {
        this.context = context;
    }

    public SpaceFooter(Context context, int height) {
        this.context = context;
        this.height = height;
    }


    @Override
    public View onCreateView(ViewGroup parent) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.ui_framework_footer_space_layout, parent, false);
        View spaceView = view.findViewById(R.id.space_view);
        if(height != 0) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) spaceView.getLayoutParams();
            layoutParams.height = height;
            spaceView.setLayoutParams(layoutParams);
        }
        return view;
    }

    @Override
    public void onBindView(View headerView) {}
}

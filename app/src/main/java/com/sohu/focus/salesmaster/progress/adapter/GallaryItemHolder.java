package com.sohu.focus.salesmaster.progress.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.kernal.imageloader.FocusImageLoader;
import com.sohu.focus.salesmaster.progress.model.GalleryModel;
import com.sohu.focus.salesmaster.uiframe.easyrecyclerview.adapter.BaseViewHolder;


/**
 * Created by yuanminjia on 2017/10/31.
 */

public class GallaryItemHolder extends BaseViewHolder<GalleryModel> {

    private ImageView show;
    private RelativeLayout add;

    public GallaryItemHolder(ViewGroup parent) {
        super(parent, R.layout.holder_gallery_item);
        show = $(R.id.gallery_show);
        add = $(R.id.gallery_add);
    }

    @Override
    public void setData(GalleryModel data) {
        if (data != null) {
            if (!data.isPhoto()) {
                add.setVisibility(View.VISIBLE);
                show.setVisibility(View.GONE);
            } else {
                add.setVisibility(View.GONE);
                show.setVisibility(View.VISIBLE);
                FocusImageLoader.getLoader(getContext()).load(data.getPhotoPath()).into(show).display();
            }


        }
    }
}

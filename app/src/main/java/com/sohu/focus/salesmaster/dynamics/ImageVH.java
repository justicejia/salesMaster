package com.sohu.focus.salesmaster.dynamics;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.kernal.imageloader.FocusImageLoader;

/**
 * Created by luckyzhangx on 2017/11/3.
 */

public class ImageVH extends RecyclerView.ViewHolder {
    ImageView img;

    public ImageVH(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_img, parent, false));
        img = (ImageView) itemView.findViewById(R.id.dynamic_img);
    }

    public void setImage(String url) {
        FocusImageLoader.getLoader(img.getContext())
                .load(url)
                .into(img)
                .placeholder(R.drawable.img_holder_gray)
                .display();
    }
}

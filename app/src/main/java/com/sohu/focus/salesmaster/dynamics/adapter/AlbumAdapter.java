package com.sohu.focus.salesmaster.dynamics.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.dynamics.ImageVH;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.progress.view.ViewPhotoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luckyzhangx on 2017/11/3.
 */

public class AlbumAdapter extends RecyclerView.Adapter<ImageVH> {

    List<String> imgUrls;

    @Override
    public ImageVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageVH(parent);
    }

    @Override
    public void onBindViewHolder(ImageVH holder, final int position) {
        holder.setImage(imgUrls.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImg(v.getContext(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (CommonUtils.isEmpty(imgUrls))
            return 0;
        return imgUrls.size();
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
        notifyDataSetChanged();
    }

    private void showImg(Context context, int position) {
        Intent intent = new Intent();
        intent.putExtra(SalesConstants.EXTRA_PHOTO_POSITION, position);
        intent.putExtra(SalesConstants.EXTRA_GALLERY_OPTION, false);
        intent.putStringArrayListExtra(SalesConstants.EXTRA_PHOTO_PATHS, geneArrayList());
        intent.setClass(context, ViewPhotoActivity.class);
        context.startActivity(intent);
    }

    ArrayList<String> geneArrayList() {
        if (CommonUtils.isEmpty(imgUrls))
            return new ArrayList<>();
        if (imgUrls instanceof ArrayList)
            return (ArrayList) imgUrls;
        ArrayList<String> list = new ArrayList<>();
        for (String Url : imgUrls) {
            list.add(Url);
        }
        return list;
    }
}

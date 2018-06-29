package com.sohu.focus.salesmaster.progress.model;

import com.sohu.focus.salesmaster.kernal.http.BaseModel;

/**
 * Created by yuanminjia on 2017/10/31.
 */

public class GalleryModel extends BaseModel {
    private String photoPath;
    private boolean isPhoto;

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public boolean isPhoto() {
        return isPhoto;
    }

    public void setPhoto(boolean photo) {
        isPhoto = photo;
    }
}

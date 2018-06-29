package com.sohu.focus.salesmaster.dynamics.model;


import java.io.Serializable;
import java.util.List;

/**
 * Created by yuanminjia on 2017/11/3.
 */

public class PublishDynamicsModel implements Serializable {

    private String userId;
    private int estateId;
    private int stageId;
    private String adMoney;
    private String remark;
    private List<ImagesBean> images;
    private List<String> customerList;

    public int getEstateId() {
        return estateId;
    }

    public void setEstateId(int estateId) {
        this.estateId = estateId;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<String> customerList) {
        this.customerList = customerList;
    }

    public String getAdMoney() {
        return adMoney;
    }

    public void setAdMoney(String adMoney) {
        this.adMoney = adMoney;
    }

    public static class ImagesBean implements Serializable {
        private int size;
        private String imgCloudPath;
        private int width;
        private String url;
        private String fileType;
        private String md5;
        private int height;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getImgCloudPath() {
            return imgCloudPath;
        }

        public void setImgCloudPath(String imgCloudPath) {
            this.imgCloudPath = imgCloudPath;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}

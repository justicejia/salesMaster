package com.sohu.focus.salesmaster.main.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.io.Serializable;

/**
 * Created by yuanminjia on 2017/12/13.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateModel extends BaseModel {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean implements Serializable {
        private boolean hasNewVersion;
        private String updateDescription;
        private String updateURL;
        private boolean isForcedUpdate;
        private String version;

        public boolean isHasNewVersion() {
            return hasNewVersion;
        }

        public void setHasNewVersion(boolean hasNewVersion) {
            this.hasNewVersion = hasNewVersion;
        }

        public String getUpdateDescription() {
            return updateDescription;
        }

        public void setUpdateDescription(String updateDescription) {
            this.updateDescription = updateDescription;
        }

        public String getUpdateURL() {
            return CommonUtils.getDataNotNull(updateURL);
        }

        public void setUpdateURL(String updateURL) {
            this.updateURL = updateURL;
        }

        public boolean isIsForcedUpdate() {
            return isForcedUpdate;
        }

        public void setIsForcedUpdate(boolean isForcedUpdate) {
            this.isForcedUpdate = isForcedUpdate;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}

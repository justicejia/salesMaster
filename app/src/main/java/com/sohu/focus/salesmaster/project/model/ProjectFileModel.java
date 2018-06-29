package com.sohu.focus.salesmaster.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.io.Serializable;
import java.util.List;


/**
 * 项目文件
 * Created by yuanminjia on 2018/1/31.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectFileModel extends BaseModel {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean implements Serializable {
        private List<FileListBean> fileList;

        public List<FileListBean> getFileList() {
            return fileList;
        }

        public void setFileList(List<FileListBean> fileList) {
            this.fileList = fileList;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class FileListBean implements Serializable {
            private String id;
            private String estateId;
            private String estateName;
            private String personId;
            private String personName;
            private String url;
            private String fileName;
            private String fileType;
            private String md5;
            private long createTime;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getEstateId() {
                return estateId;
            }

            public void setEstateId(String estateId) {
                this.estateId = estateId;
            }

            public String getPersonId() {
                return personId;
            }

            public void setPersonId(String personId) {
                this.personId = personId;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getFileName() {
                return CommonUtils.getDataNotNull(fileName);
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
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

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getEstateName() {
                return CommonUtils.getDataNotNull(estateName);
            }

            public void setEstateName(String estateName) {
                this.estateName = estateName;
            }

            public String getPersonName() {
                return CommonUtils.getDataNotNull(personName);
            }

            public void setPersonName(String personName) {
                this.personName = personName;
            }
        }
    }
}

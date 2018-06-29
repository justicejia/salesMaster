package com.sohu.focus.salesmaster.dynamics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicCommentBean implements Serializable {
    @JsonProperty("projectId")
    public String dynamicId;
    public String commentId;
    public String salesUserId;
    public String salesUserName;
    public String replyToUserId;
    public String replyToUserName;
    public String content;
    @JsonProperty("estateName")
    public String projName;
    @JsonProperty("projectStage")
    public String projStage;
    @JsonProperty("createTime")
    public String timeStamp;

    public String getDynamicId() {
        return dynamicId;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getSalesUserId() {
        return salesUserId;
    }

    public String getSalesUserName() {
        return salesUserName;
    }

    public String getReplyToUserId() {
        return replyToUserId;
    }

    public String getReplyToUserName() {
        return replyToUserName;
    }

    public String getContent() {
        return content;
    }

    public String getProjName() {
        return projName;
    }

    public String getProjStage() {
        return projStage;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}

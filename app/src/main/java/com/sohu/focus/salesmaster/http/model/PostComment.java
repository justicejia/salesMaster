package com.sohu.focus.salesmaster.http.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostComment implements Serializable {
    @JsonProperty("projectId")
    public String dynamicId;
    public String replyToUserId;
    public String content;

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public void setReplyToUserId(String replyToUserId) {
        this.replyToUserId = replyToUserId;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

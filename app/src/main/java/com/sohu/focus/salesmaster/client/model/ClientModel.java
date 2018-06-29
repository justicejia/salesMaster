package com.sohu.focus.salesmaster.client.model;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.io.Serializable;

/**
 * Created by yuanminjia on 2018/1/3.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientModel implements Serializable {

    @JsonProperty("customerId")
    public String clientId;
    @JsonProperty("phone")
    public String tel;
    public String name;
    @JsonProperty("title")
    public String job;

    @Override
    public boolean equals(@NonNull Object obj) {
        ClientModel model = (ClientModel) obj;
        if (clientId == null || model.clientId == null) {
            return super.equals(model);
        } else {
            return clientId.equals(model.clientId);
        }
    }

    public String getClientId() {
        return CommonUtils.getDataNotNull(clientId);
    }

    public String getTel() {
        return CommonUtils.getDataNotNull(tel);
    }

    public String getName() {
        return CommonUtils.getDataNotNull(name);
    }

    public String getJob() {
        return CommonUtils.getDataNotNull(job);
    }
}

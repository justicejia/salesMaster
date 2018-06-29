package com.sohu.focus.salesmaster.http.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by luckyzhangx on 05/02/2018.
 */

/**
 * 通过具体时间（2017年12月） 获取用户 400 kpi 列表
 */

public class GetUsrKpiHistory2 implements Parcelable {

    @JsonProperty("personId")
    public String userId;

    public String month;

    public GetUsrKpiHistory2(String userId) {
        this.userId = userId;
    }

//    Parcel

    public GetUsrKpiHistory2(Parcel in) {
        userId = in.readString();
        month = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(month);
    }

    public static final Creator<GetUsrKpiHistory2> CREATOR
            = new Creator<GetUsrKpiHistory2>() {
        @Override
        public GetUsrKpiHistory2 createFromParcel(Parcel source) {
            return new GetUsrKpiHistory2(source);
        }

        @Override
        public GetUsrKpiHistory2[] newArray(int size) {
            return new GetUsrKpiHistory2[0];
        }
    };

    public GetUsrKpiHistory2 copy() {
        GetUsrKpiHistory2 model = new GetUsrKpiHistory2(userId);
        model.month = month;
        return model;
    }
}

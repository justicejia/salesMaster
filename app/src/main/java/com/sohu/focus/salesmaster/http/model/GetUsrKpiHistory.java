package com.sohu.focus.salesmaster.http.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by luckyzhangx on 05/02/2018.
 */

/**
 * 通过类型（周月）和相对时间（本上下/周月) 获取用户 400 kpi 列表
 */

public class GetUsrKpiHistory implements Parcelable {

    public enum Type {
        WEEK("week"), MONTH("month");

        Type(String type) {
            this.type = type;
        }

        public String type;
    }

    public enum Partition {
        THIS("this"), LAST("last"), NEXT("next");

        Partition(String partition) {
            this.partition = partition;
        }

        public String partition;
    }

    @JsonProperty("personId")
    public String userId;

    /**
     * {@link Type#type}
     */
    public String type;

    /**
     * {@link Partition#partition}
     */
    public String partition;

    public GetUsrKpiHistory(String userId) {
        this.userId = userId;
    }

//    Parcel

    public GetUsrKpiHistory(Parcel in) {
        userId = in.readString();
        type = in.readString();
        partition = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(type);
        dest.writeString(partition);
    }

    public static final Parcelable.Creator<GetUsrKpiHistory> CREATOR
            = new Parcelable.Creator<GetUsrKpiHistory>() {
        @Override
        public GetUsrKpiHistory createFromParcel(Parcel source) {
            return new GetUsrKpiHistory(source);
        }

        @Override
        public GetUsrKpiHistory[] newArray(int size) {
            return new GetUsrKpiHistory[0];
        }
    };

    public GetUsrKpiHistory copy() {
        GetUsrKpiHistory model = new GetUsrKpiHistory(userId);
        model.type = type;
        model.partition = partition;
        return model;
    }
}

package com.sohu.focus.salesmaster.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by luckyzhangx on 11/01/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientsModel extends BaseModel {

    public DataUnit data;

    public DataUnit getData() {
        return data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)

    public static class DataUnit implements Serializable {
        public int count;
        public List<ClientModel> list;

        public int getCount() {
            return count;
        }

        public List<ClientModel> getClients() {
            return list;
        }
    }

}

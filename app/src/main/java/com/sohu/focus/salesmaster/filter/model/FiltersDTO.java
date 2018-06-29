package com.sohu.focus.salesmaster.filter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sohu.focus.salesmaster.filter.base.FilterVO;
import com.sohu.focus.salesmaster.kernal.http.BaseMappingModel;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luckyzhangx on 15/01/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class FiltersDTO extends BaseMappingModel<FiltersVO> {

    public DataUnit data;

    @Override
    public FiltersVO transform() {
        if (data == null) return null;
        return data.transform();
    }

    public static class DataUnit extends BaseMappingModel<FiltersVO> {

        @Override
        public FiltersVO transform() {
            FiltersVO vos = new FiltersVO();

            vos.city = convert(city);
            vos.salesRole = convert(salesRole);
            vos.progress = convert(progress);
            vos.projOrder = convert(projOrder);

            return vos;
        }

        @JsonProperty("cityList")
        public List<CityFilterDTO> city;
        @JsonProperty("salesRoleList")
        public List<SalesRoleFilterDTO> salesRole;
        @JsonProperty("EstateOrder")
        public List<ProjOrderFilterDTO> projOrder;
        @JsonProperty("stateWithAll")
        public List<ProgressFilterDTO> progress;
    }


    private static <T extends BaseFilterDTO> List<FilterVO> convert(List<T> dtos) {
        if (CommonUtils.isEmpty(dtos))
            return null;
        ArrayList<FilterVO> vos = new ArrayList<>();
        for (T dto : dtos) {
            vos.add(dto.transform());
        }
        return vos;
    }
}

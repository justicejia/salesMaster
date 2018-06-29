package com.sohu.focus.salesmaster.newFilter.model;

import java.io.Serializable;

/**
 * Created by jiayuanmin on 2018/5/21
 * description:
 */
public class CommonFilterItemModel implements Serializable {
    private String label;
    private int option;
    private boolean isSelected;
    private int type;

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CommonFilterItemModel
                && ((CommonFilterItemModel) obj).getOption() == this.option;
    }

    public static CommonFilterItemModel getDefault() { // 默认北京
        CommonFilterItemModel model = new CommonFilterItemModel();
        model.setOption(110100);
        model.setLabel("北京");
        return model;
    }
}

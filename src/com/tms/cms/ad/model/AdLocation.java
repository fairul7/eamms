package com.tms.cms.ad.model;

import kacang.model.DefaultDataObject;

import java.util.ArrayList;
import java.util.List;

public class AdLocation extends DefaultDataObject {
    public static final int AD_TYPE_FULL_BANNER_468_x_60 = 1;
    public static final int AD_TYPE_HALF_BANNER_234_x_60 = 2;
    public static final int AD_TYPE_MICRO_BAR_88_x_31 = 3;
    public static final int AD_TYPE_BUTTON_1_120_x_90 = 4;
    public static final int AD_TYPE_BUTTON_2_120_x_60 = 5;
    public static final int AD_TYPE_WIDE_SKYSCRAPER_160_x_600 = 6;
    public static final int AD_TYPE_SKYSCRAPER_120_x_600 = 7;
    public static final int AD_TYPE_SQUARE_BUTTON_125_x_125 = 8;
    public static final int AD_TYPE_RECTANGLE_180_x_150 = 9;
    public static final int AD_TYPE_VERTICAL_BANNER_120_x_240 = 10;
    public static final int AD_TYPE_MEDIUM_RECTANGLE_300_x_250 = 11;
    public static final int AD_TYPE_SQUARE_POP_UP_250_x_250 = 12;
    public static final int AD_TYPE_VERTICAL_RECTANGLE_240_x_400 = 13;
    public static final int AD_TYPE_LARGE_RECTANGLE_336_x_280 = 14;
    public static final int AD_TYPE_CUSTOM_FREE_SIZE = 15;

    private String name;
    private boolean active;
    private String description;
    private int adType;
    private List adList;
    private List activeAdList;

    public AdLocation() {
        adList = new ArrayList();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }

    public String getDescription() {
        return description==null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name==null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getAdList() {
        return adList;
    }

    public void setAdList(List adList) {
        this.adList = adList;
    }

    public String getAdLocationId() {
        return super.getId();
    }

    public void setAdLocationId(String id) {
        super.setId(id);
    }

    public List getActiveAdList() {
        return activeAdList;
    }

    public void setActiveAdList(List activeAdList) {
        this.activeAdList = activeAdList;
    }

}

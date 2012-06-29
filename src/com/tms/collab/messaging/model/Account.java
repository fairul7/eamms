package com.tms.collab.messaging.model;

import kacang.model.DefaultDataObject;

import java.util.Map;
import java.util.HashMap;

/**
 * Data object to represent messaging accounts. This class is currently
 * extended by <code>IntranetAccount</code> and <code>Pop3Account</code>.
 */
public abstract class Account extends DefaultDataObject {
    /**
     * Intranet messaging account type.
     */
    public static final int ACCOUNT_TYPE_INTRANET = 1;
    /**
     * POP3 messaging account type.
     */
    public static final int ACCOUNT_TYPE_POP3 = 2;

    public static final int INDICATOR_RED = 1;
    public static final int INDICATOR_GREEN = 2;
    public static final int INDICATOR_BLUE = 3;
    public static final int INDICATOR_PURPLE = 4;
    public static final int INDICATOR_YELLOW = 5;
    public static final int INDICATOR_CYAN = 6;
    public static final int INDICATOR_PINK = 7;
    public static final int INDICATOR_GRAY = 8;

    public static final Map indicatorColorMap;
    static {
        indicatorColorMap = new HashMap();
        indicatorColorMap.put(Integer.toString(INDICATOR_RED), "#FF0000");
        indicatorColorMap.put(Integer.toString(INDICATOR_GREEN), "#00CC33");
        indicatorColorMap.put(Integer.toString(INDICATOR_BLUE), "#3366FF");
        indicatorColorMap.put(Integer.toString(INDICATOR_PURPLE), "#CC66CC");
        indicatorColorMap.put(Integer.toString(INDICATOR_YELLOW), "#FFCC00");
        indicatorColorMap.put(Integer.toString(INDICATOR_CYAN), "#00CCFF");
        indicatorColorMap.put(Integer.toString(INDICATOR_PINK), "#FF00FF");
        indicatorColorMap.put(Integer.toString(INDICATOR_GRAY), "#999999");
    }

    private String accountId;
    private String userId;
    private String name;
    private int indicator;
    private String deliveryFolderId;
    private boolean filterEnabled;

    public abstract int getAccountType();

    public String getId() {
        return getAccountId();
    }

    public void setId(String s) {
        setAccountId(s);
    }


    // === [ getters/setters ] =================================================
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndicator() {
        return indicator;
    }

    public void setIndicator(int indicator) {
        this.indicator = indicator;
    }

    public String getDeliveryFolderId() {
        return deliveryFolderId;
    }

    public void setDeliveryFolderId(String deliveryFolderId) {
        this.deliveryFolderId = deliveryFolderId;
    }

    public boolean isFilterEnabled() {
        return filterEnabled;
    }

    public void setFilterEnabled(boolean filterEnabled) {
        this.filterEnabled = filterEnabled;
    }
}

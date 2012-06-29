package com.tms.cms.ad.ui;

import com.tms.cms.ad.model.Ad;
import com.tms.cms.ad.model.AdException;
import com.tms.cms.ad.model.AdLocation;
import com.tms.cms.ad.model.AdModule;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import java.io.IOException;

public class DisplayAd extends LightWeightWidget {
    private Ad ad;
    private AdLocation adLocation;

    private String adClickUrl;
    private String adLocationName;
    private String adId;
    private String errorMessage;
    private String preview;

    public void onRequest(Event evt) {
        AdModule adModule;
        boolean boolPreview = false;

        if("true".equalsIgnoreCase(getPreview())) {
            boolPreview = true;
        }

        adModule = (AdModule) Application.getInstance().getModule(AdModule.class);

        setErrorMessage(null);

        try {
            if(adId!=null) {
                // clicked
                ad = adModule.clickAdEvent(evt.getRequest(), adId, boolPreview);
                try {
                    evt.getResponse().sendRedirect(ad.getUrl());
                } catch (IOException e) {
                    Log.getLog(this.getClass()).error("Error redirecting to Ad's URL", e);
                    throw new RuntimeException("Error redirecting to Ad's URL: " + e.toString());
                }

            } else if(adLocationName!=null) {
                // viewed
                adLocation = adModule.getAdLocationByName(adLocationName);
                try {
                    ad = adModule.viewAdEvent(evt.getRequest(), adLocationName, boolPreview);
                } catch (DataObjectNotFoundException e) {
                    Log.getLog(getClass()).debug(e.getMessage());
                }
                String defaultAdClickUrl;
                defaultAdClickUrl = evt.getRequest().getContextPath() + "/cmsadmin/ad/adClick.jsp";

                adClickUrl = adClickUrl==null ? defaultAdClickUrl : adClickUrl;

            } else {
                Log.getLog(this.getClass()).error("Required parameters for DisplayAd missing");
                setErrorMessage("Required parameters for DisplayAd missing");
            }

        } catch (AdException e) {
            Log.getLog(this.getClass()).debug("Ad or ad location error: " + e.getMessage(), e);
            setErrorMessage(e.getMessage());
        }
    }

    public String getTemplate() {
        return "ad/displayAd";
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public AdLocation getAdLocation() {
        return adLocation;
    }

    public void setAdLocation(AdLocation adLocation) {
        this.adLocation = adLocation;
    }

    public String getAdClickUrl() {
        return adClickUrl;
    }

    public void setAdClickUrl(String adClickUrl) {
        this.adClickUrl = adClickUrl;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdLocationName() {
        return adLocationName;
    }

    public void setAdLocationName(String adLocationName) {
        this.adLocationName = adLocationName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }
}

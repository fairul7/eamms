package com.tms.cms.ad.ui;

import com.tms.cms.ad.model.AdException;
import com.tms.cms.ad.model.AdModule;
import kacang.Application;
import kacang.stdui.Table;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.util.Log;

import java.util.Iterator;

public class AdUi extends Widget {
    private AdLocationForm adLocationForm;
    private AdForm adForm;
    private AdLocationTable adLocationTable;
    private AdTable adTable;
    private String title;

    private boolean showMenu = true;

    public void init() {
        removeChildren();

        Application application = Application.getInstance();
        setTitle(application.getMessage("general.label.bannerAds", application.getMessage("general.label.bannerAds", "Banner Ads")));

        adLocationTable = new AdLocationTable("adLocationTable");
        adLocationTable.addEventListener(this);
        addChild(adLocationTable);
        adLocationTable.init();

        adLocationForm = new AdLocationForm("adLocationForm");
        adLocationForm.setMethod("post");
        adLocationForm.addEventListener(this);
        addChild(adLocationForm);
        adLocationForm.init();

        adTable = new AdTable("adTable");
        adTable.addEventListener(this);
        addChild(adTable);
        adTable.init();

        adForm = new AdForm("adForm");
        adLocationForm.setMethod("post");
        adForm.addEventListener(this);
        addChild(adForm);
        adForm.init();

        showOnly(adLocationTable);

    }

    public Forward actionPerformed(Event evt) {
        Log log = Log.getLog(this.getClass());
        log.debug("Event occurred for AdUi. Widget=" +
        evt.getWidget().getAbsoluteName() +
        " Event=" + evt.getType());

        Application application = Application.getInstance();
        if(this.equals(evt.getWidget()) && "listAdLocations".equals(evt.getType())) {
            // list AdLocationTable
            showOnly(adLocationTable);
            setTitle(application.getMessage("ad.label.adLocationListing", "Ad Location Listing"));

        } else if (this.equals(evt.getWidget()) && "listAds".equals(evt.getType())) {
            // list AdTable
            showOnly(adTable);
            setTitle(application.getMessage("ad.label.adListing", "Ad Listing"));

        } else if (adLocationTable.equals(evt.getWidget()) && "newAdLocation".equals(evt.getType())) {
            // new AdLocation
            adLocationForm.init();
            showOnly(adLocationForm);
            setTitle(application.getMessage("ad.label.newAdLocation", "New Ad Location"));

        } else if (adTable.equals(evt.getWidget()) && "newAd".equals(evt.getType())) {
            // new Ad
            adForm.init();
            showOnly(adForm);
            setTitle(application.getMessage("ad.label.newAd", "New Ad"));

        } else if (this.equals(evt.getWidget()) && "refreshModule".equals(evt.getType())) {
            // refresh module
            AdModule adModule = (AdModule) Application.getInstance().getModule(AdModule.class);
            try {
                adModule.refreshModule();

            } catch (AdException e) {
                throw new RuntimeException("Error refreshing AdModule: " + e.toString());
            }

        } else if (adForm.equals(evt.getWidget()) && "savedAd".equals(evt.getType())) {
            // after saving an Ad
            showOnly(adTable);

        } else if (adLocationForm.equals(evt.getWidget()) && "savedAdLocation".equals(evt.getType())) {
            // after saving an AdLocation
            showOnly(adLocationTable);
            setTitle(application.getMessage("ad.label.adLocationListing", "Ad Location Listing"));

        } else if (adTable.equals(evt.getWidget()) && Table.PARAMETER_KEY_SELECTION.equals(evt.getType())) {
            // edit Ad
            adForm.setAdId(evt.getRequest().getParameter("id"));
            showOnly(adForm);
            setTitle(application.getMessage("ad.label.editingAd", "Editing Ad"));

        } else if (adLocationTable.equals(evt.getWidget()) && Table.PARAMETER_KEY_SELECTION.equals(evt.getType())) {
            // edit AdLocation
            adLocationForm.setAdLocationId(evt.getRequest().getParameter("id"));
            showOnly(adLocationForm);
            setTitle(application.getMessage("ad.label.editingAdLocation", "Editing Ad Location"));

        } else {
            // log ignored events
            log.debug("Ignored event in AdUi. Widget=" +
            evt.getWidget().getAbsoluteName() +
            " Event=" + evt.getType());
        }

        // disable back/forward in browser!
        return new Forward(null, evt.getRequest().getRequestURI(), true);
        //return super.actionPerformed(evt);
    }

    private void showOnly(Widget widget) {
        Widget childWidget;
        Iterator iterator;

        iterator = getChildren().iterator();
        while(iterator.hasNext()) {
            childWidget = (Widget) iterator.next();
            childWidget.setHidden(true);
        }

        widget.setHidden(false);
    }

    // --- widgets methods -----------------------------------------------------
    public String getTemplate() {
        return "ad/adUi";
    }

    // --- getters & setters
    public AdForm getAdForm() {
        return adForm;
    }

    public void setAdForm(AdForm adForm) {
        this.adForm = adForm;
    }

    public AdLocationForm getAdLocationForm() {
        return adLocationForm;
    }

    public void setAdLocationForm(AdLocationForm adLocationForm) {
        this.adLocationForm = adLocationForm;
    }

    public AdLocationTable getAdLocationTable() {
        return adLocationTable;
    }

    public void setAdLocationTable(AdLocationTable adLocationTable) {
        this.adLocationTable = adLocationTable;
    }

    public AdTable getAdTable() {
        return adTable;
    }

    public void setAdTable(AdTable adTable) {
        this.adTable = adTable;
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
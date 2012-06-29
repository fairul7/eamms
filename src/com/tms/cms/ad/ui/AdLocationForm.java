package com.tms.cms.ad.ui;

import com.tms.cms.ad.model.Ad;
import com.tms.cms.ad.model.AdException;
import com.tms.cms.ad.model.AdLocation;
import com.tms.cms.ad.model.AdModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

public class AdLocationForm extends Form {
    private boolean newAdLocation;
    private AdLocation adLocation;
    private String adLocationId;

    // childs...
    private TextField ffName;
    private Radio ffActiveYes, ffActiveNo;
    private SelectBox ffType;
    private TextBox ffDescription;
    private ComboSelectBox ffAds;

    // --- do events -----------------------------------------------------------
    private void doSave() {
        Log log = Log.getLog(this.getClass());
        try {
            AdModule adModule = (AdModule) Application.getInstance().getModule(AdModule.class);
            if(newAdLocation) {
                adModule.createAdLocation(adLocation);
                log.info("AdLocation created");
            } else {
                adModule.updateAdLocation(adLocation);

                log.info("AdLocation updated");
            }

        } catch(AdException e) {
            log.error("Error saving AdLocation", e);
        }
    }

    // --- widget --------------------------------------------------------------
    public Forward onValidate(Event evt) {
        Forward forward;

        // update adLocation
        adLocation.setAdLocationId(getAdLocationId());
        adLocation.setName((String)ffName.getValue());
        adLocation.setActive(ffActiveYes.isChecked());
        List values = (List) ffType.getValue();
        adLocation.setAdType(Integer.parseInt((String)values.get(0)));
        adLocation.setDescription((String)ffDescription.getValue());
        adLocation.setAdList(new ArrayList(ffAds.getRightValues().keySet()));

        doSave();
        evt.setType("savedAdLocation");

        forward = super.onValidate(evt);
        return forward!=null ? forward : new Forward("savedAdLocation");
    }

    public AdLocationForm() {
    }

    public AdLocationForm(String name) {
        this();
        setName(name);
    }

    public void init() {
        removeChildren();

        // reset to new AdLocation
        this.newAdLocation = true;
        this.adLocation = new AdLocation();
        this.adLocationId = UuidGenerator.getInstance().getUuid();

        // child widgets...
        Application application = Application.getInstance();
        ffName = new TextField("ffName");
        ffName.addChild(new ValidatorNotEmpty("fvName"));
        ffName.setValue("");
        addChild(ffName);

        ffActiveYes = new Radio("ffActiveYes");
        ffActiveYes.setText(application.getMessage("general.label.active", "Active"));
        ffActiveYes.setGroupName("ffActive");
        ffActiveYes.setChecked(true);
        addChild(ffActiveYes);

        ffActiveNo = new Radio("ffActiveNo");
        ffActiveNo.setText(application.getMessage("general.label.notActive", "Not Active"));
        ffActiveNo.setGroupName("ffActive");
        ffActiveNo.setChecked(false);
        addChild(ffActiveNo);

        ffType = new SelectBox("ffType");
        ffType.setMultiple(false);
        ffType.setOptions("0=--- "+application.getMessage("general.label.selectOne", "SELECT ONE")+" ---;" +
        "1=FULL_BANNER_468x60;" +
        "2=HALF_BANNER_234x60;" +
        "3=MICRO_BAR_88x31;" +
        "4=BUTTON_1_120x90;" +
        "5=BUTTON_2_120x60;" +
        "6=WIDE_SKYSCRAPER_160x600;" +
        "7=SKYSCRAPER_120x600;" +
        "8=SQUARE_BUTTON_125x125;" +
        "9=RECTANGLE_180x150;" +
        "10=VERTICAL_BANNER_120x240;" +
        "11=MEDIUM_RECTANGLE_300x250;" +
        "12=SQUARE_POP_UP_250x250;" +
        "13=VERTICAL_RECTANGLE_240x400;" +
        "14=LARGE_RECTANGLE_336x280;" +
        "15=CUSTOM_FREE_SIZE");
        addChild(ffType);

        ffDescription = new TextBox("ffDescription");
        //ffDescription.addChild(new ValidatorNotEmpty("fvDescription"));
        ffDescription.setValue("");
        addChild(ffDescription);

        ffAds = new ComboSelectBox("ffAds");
        addChild(ffAds);
        ffAds.init();

        Map leftMap = getAdsAsMap(null, false);
        Map rigthMap = new HashMap();
        ffAds.setLeftValues(leftMap);
        ffAds.setRightValues(rigthMap);

    }

    public String getTemplate() {
        return "ad/adLocationForm";
    }

    // --- private ---
    private Map getAdsAsMap(Collection memberList, boolean getMembers) {
        Map map;
        Collection ads;
        Ad ad;
        Iterator iterator;

        map = new SequencedHashMap();
        try {
            AdModule adModule = (AdModule) Application.getInstance().getModule(AdModule.class);
            if(getMembers && memberList!=null) {
                ads = memberList;
            } else {
                ads = adModule.getAds("*", null, false, 0, -1);
            }

            iterator = ads.iterator();
            while(iterator.hasNext()) {
                ad = (Ad) iterator.next();
                if(memberList!=null && getMembers==false) {
                    // get non members... only put if not in memberList
                    if(!memberList.contains(ad)) {
                        map.put(ad.getAdId(), ad.getName());
                    }

                } else {
                    //just put...
                    map.put(ad.getAdId(), ad.getName());
                }
            }

        } catch (AdException e) {
            Log log = Log.getLog(this.getClass());
            log.error(e.getMessage(), e);
        }

        return map;
    }

    // --- getters & setters ---
    public String getAdLocationId() {
        return adLocationId;
    }

    public void setAdLocationId(String adLocationId) {
        // update AdLocation based on locationId
        try {
            AdModule adModule = (AdModule) Application.getInstance().getModule(AdModule.class);
            adLocation = adModule.getAdLocation(adLocationId);
            // set only if loaded sucessfully
            this.adLocationId = adLocationId;
            setNewAdLocation(false);

            // update widgets
            ffName.setValue(adLocation.getName());

            // TODO: this is stupid!
            ArrayList al = new ArrayList();
            al.add(Integer.toString(adLocation.getAdType()));
            ffType.setValue(al);

            // TODO: true false don't work
            if(adLocation.isActive()) {
                ffActiveYes.setChecked(true);
                ffActiveNo.setChecked(false);
            } else {
                ffActiveYes.setChecked(false);
                ffActiveNo.setChecked(true);
            }

            ffDescription.setValue(adLocation.getDescription());

            Map leftMap = getAdsAsMap(adLocation.getAdList(), false);
            Map rigthMap = getAdsAsMap(adLocation.getAdList(), true);
            ffAds.setLeftValues(leftMap);
            ffAds.setRightValues(rigthMap);

        } catch (AdException e) {
            Log log = Log.getLog(this.getClass());
            log.error(e);
            init();
        }
    }

    public AdLocation getAdLocation() {
        return adLocation;
    }

    public void setAdLocation(AdLocation adLocation) {
        this.adLocation = adLocation;
    }

    public boolean isNewAdLocation() {
        return newAdLocation;
    }

    public void setNewAdLocation(boolean newAdLocation) {
        this.newAdLocation = newAdLocation;
    }

    public Radio getFfActiveNo() {
        return ffActiveNo;
    }

    public void setFfActiveNo(Radio ffActiveNo) {
        this.ffActiveNo = ffActiveNo;
    }

    public Radio getFfActiveYes() {
        return ffActiveYes;
    }

    public void setFfActiveYes(Radio ffActiveYes) {
        this.ffActiveYes = ffActiveYes;
    }

    public TextBox getFfDescription() {
        return ffDescription;
    }

    public void setFfDescription(TextBox ffDescription) {
        this.ffDescription = ffDescription;
    }

    public TextField getFfName() {
        return ffName;
    }

    public void setFfName(TextField ffName) {
        this.ffName = ffName;
    }

    public SelectBox getFfType() {
        return ffType;
    }

    public void setFfType(SelectBox ffType) {
        this.ffType = ffType;
    }

    public ComboSelectBox getFfAds() {
        return ffAds;
    }

    public void setFfAds(ComboSelectBox ffAds) {
        this.ffAds = ffAds;
    }
}
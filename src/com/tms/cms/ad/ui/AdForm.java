package com.tms.cms.ad.ui;

import com.tms.cms.ad.model.Ad;
import com.tms.cms.ad.model.AdException;
import com.tms.cms.ad.model.AdModule;
import kacang.Application;
import kacang.services.storage.StorageFile;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.io.IOException;

public class AdForm extends Form {
    private boolean newAd;
    private Ad ad;
    private String adId;

    // childs...
    private TextField ffName;
    private Radio ffActiveYes, ffActiveNo;
    private TextField ffUrl;
    private Radio ffNewWindowYes, ffNewWindowNo;
    private TextField ffAlternateText;
    private Radio ffUseScriptYes, ffUseScriptNo;
    private TextBox ffScript;
    private DateField ffStartDate;
    private Radio ffStartDateEnabledYes, ffStartDateEnabledNo;
    private DateField ffEndDate;
    private Radio ffEndDateEnabledYes, ffEndDateEnabledNo;
    private FileUpload ffImageFile;

    // --- do events -----------------------------------------------------------
    private void doSave(Event evt) {
        Log log = Log.getLog(this.getClass());
        StorageFile sf;

        try {
            AdModule adModule = (AdModule) Application.getInstance().getModule(AdModule.class);
            sf = ffImageFile.getStorageFile(evt.getRequest());

            if(newAd) {
                adModule.createAd(ad, sf);
                log.info("Ad created");
            } else {
                adModule.updateAd(ad, sf);
                log.info("Ad updated");
            }

        } catch(AdException e) {
            log.error("Error saving Ad", e);
        } catch (IOException e) {
            log.error("Error saving Ad", e);
        }
    }

    // --- widget --------------------------------------------------------------
    public Forward onValidate(Event evt) {
        // update com.tms.cms.ad
        ad.setAdId(getAdId());
        ad.setName((String)ffName.getValue());
        ad.setActive(ffActiveYes.isChecked());
        ad.setUrl((String)ffUrl.getValue());
        ad.setNewWindow(ffNewWindowYes.isChecked());
        ad.setAlternateText((String)ffAlternateText.getValue());
        ad.setUseScript(ffUseScriptYes.isChecked());
        ad.setScript((String)ffScript.getValue());
        ad.setStartDate(ffStartDate.getDate());
        ad.setStartDateEnabled(ffStartDateEnabledYes.isChecked());
        ad.setEndDate(ffEndDate.getDate());
        ad.setEndDateEnabled(ffEndDateEnabledYes.isChecked());

        doSave(evt);

        evt.setType("savedAd");

        return super.onValidate(evt);
    }

    public AdForm() {
    }

    public AdForm(String name) {
        this();
        setName(name);
    }

    public void init() {
        removeChildren();

        // reset to new Ad
        this.newAd = true;
        this.ad = new Ad();
        this.adId = UuidGenerator.getInstance().getUuid();

        // child widgets...
        // TODO: has got to be a better way to do this?!?! Visual tool the only option?
        ffName = new TextField("ffName");
        ffName.addChild(new ValidatorNotEmpty("fvName"));
        addChild(ffName);

        Application application = Application.getInstance();
        ffActiveYes = new Radio("ffActiveYes");
        ffActiveYes.setGroupName("ffActive");
        ffActiveYes.setText(application.getMessage("general.label.yes", "Yes"));
        ffActiveYes.setChecked(true);
        addChild(ffActiveYes);

        ffActiveNo = new Radio("ffActiveNo");
        ffActiveNo.setGroupName("ffActive");
        ffActiveNo.setText(application.getMessage("general.label.no", "No"));
        ffActiveNo.setChecked(false);
        addChild(ffActiveNo);

        ffUrl = new TextField("ffUrl");
        ffUrl.addChild(new ValidatorNotEmpty("fvUrl"));
        addChild(ffUrl);

        ffNewWindowYes = new Radio("ffNewWindowYes");
        ffNewWindowYes.setGroupName("ffNewWindow");
        ffNewWindowYes.setText(application.getMessage("general.label.yes", "Yes"));
        ffNewWindowYes.setChecked(true);
        addChild(ffNewWindowYes);

        ffNewWindowNo = new Radio("ffNewWindowNo");
        ffNewWindowNo.setGroupName("ffNewWindow");
        ffNewWindowNo.setText(application.getMessage("general.label.no", "No"));
        ffNewWindowNo.setChecked(false);
        addChild(ffNewWindowNo);

        ffAlternateText = new TextField("ffAlternateText");
        //ffAlternateText.addChild(new ValidatorNotEmpty("fvAlternateText"));
        addChild(ffAlternateText);

        ffUseScriptYes = new Radio("ffUseScriptYes");
        ffUseScriptYes.setGroupName("ffUseScript");
        ffUseScriptYes.setText(application.getMessage("general.label.yes", "Yes"));
        ffUseScriptYes.setChecked(false);
        addChild(ffUseScriptYes);

        ffUseScriptNo = new Radio("ffUseScriptNo");
        ffUseScriptNo.setGroupName("ffUseScript");
        ffUseScriptNo.setText(application.getMessage("general.label.no", "No"));
        ffUseScriptNo.setChecked(true);
        addChild(ffUseScriptNo);

        ffScript = new TextBox("ffScript");
        //ffScript.addChild(new ValidatorNotEmpty("fvScript"));
        addChild(ffScript);

        ffStartDate = new DateField("ffStartDate");
        addChild(ffStartDate);

        ffStartDateEnabledYes = new Radio("ffStartDateEnabledYes");
        ffStartDateEnabledYes.setGroupName("ffStartDateEnabled");
        ffStartDateEnabledYes.setText(application.getMessage("general.label.yes", "Yes"));
        ffStartDateEnabledYes.setChecked(false);
        addChild(ffStartDateEnabledYes);

        ffStartDateEnabledNo = new Radio("ffStartDateEnabledNo");
        ffStartDateEnabledNo.setGroupName("ffStartDateEnabled");
        ffStartDateEnabledNo.setText(application.getMessage("general.label.no", "No"));
        ffStartDateEnabledNo.setChecked(true);
        addChild(ffStartDateEnabledNo);

        ffEndDate = new DateField("ffEndDate");
        addChild(ffEndDate);

        ffEndDateEnabledYes = new Radio("ffEndDateEnabledYes");
        ffEndDateEnabledYes.setGroupName("ffEndDateEnabled");
        ffEndDateEnabledYes.setText(application.getMessage("general.label.yes", "Yes"));
        ffEndDateEnabledYes.setChecked(false);
        addChild(ffEndDateEnabledYes);

        ffEndDateEnabledNo = new Radio("ffEndDateEnabledNo");
        ffEndDateEnabledNo.setGroupName("ffEndDateEnabled");
        ffEndDateEnabledNo.setText(application.getMessage("general.label.no", "No"));
        ffEndDateEnabledNo.setChecked(true);
        addChild(ffEndDateEnabledNo);

        ffImageFile = new FileUpload("ffImageFile");
        addChild(ffImageFile);

    }

    public String getTemplate() {
        return "ad/adForm";
    }

    // --- getters & setters ---
    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        // update Ad based on adId
        try {
            AdModule adModule = (AdModule) Application.getInstance().getModule(AdModule.class);
            ad = adModule.getAd(adId);
            // set only if loaded sucessfully
            this.adId = adId;
            setNewAd(false);

            // update widgets
            // TODO: got to be a better way to do this!?
            ffName.setValue(ad.getName());

            if(ad.isActive()) {
                ffActiveYes.setChecked(true);
                ffActiveNo.setChecked(false);
            } else {
                ffActiveYes.setChecked(false);
                ffActiveNo.setChecked(true);
            }

            ffUrl.setValue(ad.getUrl());

            if(ad.isNewWindow()) {
                ffNewWindowYes.setChecked(true);
                ffNewWindowNo.setChecked(false);
            } else {
                ffNewWindowYes.setChecked(false);
                ffNewWindowNo.setChecked(true);
            }

            ffAlternateText.setValue(ad.getAlternateText());

            if(ad.isUseScript()) {
                ffUseScriptYes.setChecked(true);
                ffUseScriptNo.setChecked(false);
            } else {
                ffUseScriptYes.setChecked(false);
                ffUseScriptNo.setChecked(true);
            }

            ffScript.setValue(ad.getScript());

            ffStartDate.setDate(ad.getStartDate());
            if(ad.isStartDateEnabled()) {
                ffStartDateEnabledYes.setChecked(true);
                ffStartDateEnabledNo.setChecked(false);
            } else {
                ffStartDateEnabledYes.setChecked(false);
                ffStartDateEnabledNo.setChecked(true);
            }

            ffEndDate.setDate(ad.getEndDate());
            if(ad.isEndDateEnabled()) {
                ffEndDateEnabledYes.setChecked(true);
                ffEndDateEnabledNo.setChecked(false);
            } else {
                ffEndDateEnabledYes.setChecked(false);
                ffEndDateEnabledNo.setChecked(true);
            }

        } catch (AdException e) {
            Log log = Log.getLog(this.getClass());
            log.error(e);
            init();
        }
    }

    public boolean isNewAd() {
        return newAd;
    }

    public void setNewAd(boolean newAd) {
        this.newAd = newAd;
    }

    public Radio getFfUseScriptYes() {
        return ffUseScriptYes;
    }

    public void setFfUseScriptYes(Radio ffUseScriptYes) {
        this.ffUseScriptYes = ffUseScriptYes;
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

    public TextField getFfAlternateText() {
        return ffAlternateText;
    }

    public void setFfAlternateText(TextField ffAlternateText) {
        this.ffAlternateText = ffAlternateText;
    }

    public DateField getFfEndDate() {
        return ffEndDate;
    }

    public void setFfEndDate(DateField ffEndDate) {
        this.ffEndDate = ffEndDate;
    }

    public Radio getFfEndDateEnabledNo() {
        return ffEndDateEnabledNo;
    }

    public void setFfEndDateEnabledNo(Radio ffEndDateEnabledNo) {
        this.ffEndDateEnabledNo = ffEndDateEnabledNo;
    }

    public Radio getFfEndDateEnabledYes() {
        return ffEndDateEnabledYes;
    }

    public void setFfEndDateEnabledYes(Radio ffEndDateEnabledYes) {
        this.ffEndDateEnabledYes = ffEndDateEnabledYes;
    }

    public Radio getFfNewWindowNo() {
        return ffNewWindowNo;
    }

    public void setFfNewWindowNo(Radio ffNewWindowNo) {
        this.ffNewWindowNo = ffNewWindowNo;
    }

    public Radio getFfNewWindowYes() {
        return ffNewWindowYes;
    }

    public void setFfNewWindowYes(Radio ffNewWindowYes) {
        this.ffNewWindowYes = ffNewWindowYes;
    }

    public TextBox getFfScript() {
        return ffScript;
    }

    public void setFfScript(TextBox ffScript) {
        this.ffScript = ffScript;
    }

    public DateField getFfStartDate() {
        return ffStartDate;
    }

    public void setFfStartDate(DateField ffStartDate) {
        this.ffStartDate = ffStartDate;
    }

    public Radio getFfStartDateEnabledNo() {
        return ffStartDateEnabledNo;
    }

    public void setFfStartDateEnabledNo(Radio ffStartDateEnabledNo) {
        this.ffStartDateEnabledNo = ffStartDateEnabledNo;
    }

    public Radio getFfStartDateEnabledYes() {
        return ffStartDateEnabledYes;
    }

    public void setFfStartDateEnabledYes(Radio ffStartDateEnabledYes) {
        this.ffStartDateEnabledYes = ffStartDateEnabledYes;
    }

    public TextField getFfUrl() {
        return ffUrl;
    }

    public void setFfUrl(TextField ffUrl) {
        this.ffUrl = ffUrl;
    }

    public Radio getFfUseScriptNo() {
        return ffUseScriptNo;
    }

    public void setFfUseScriptNo(Radio ffUseScriptNo) {
        this.ffUseScriptNo = ffUseScriptNo;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public FileUpload getFfImageFile() {
        return ffImageFile;
    }

    public void setFfImageFile(FileUpload ffImageFile) {
        this.ffImageFile = ffImageFile;
    }

    public TextField getFfName() {
        return ffName;
    }

    public void setFfName(TextField ffName) {
        this.ffName = ffName;
    }
}
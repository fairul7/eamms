package com.tms.cms.mobile.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.mobile.model.MobileChannel;
import com.tms.cms.mobile.model.MobileModule;
import com.tms.cms.section.Section;
import com.tms.cms.section.ui.SectionSelectBox;
import com.tms.cms.vsection.VSection;
import com.tms.ekms.setup.model.SetupModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import org.apache.commons.collections.SequencedHashMap;

public class MobileChannelForm extends Form {

    private String id;
    TextField titleField;
    TextField locationField;
    TextField maxSizeField;
    TextField depthField;
    CheckBox allowImagesBox;
    CheckBox offsiteLinksBox;
    Radio refreshAlways;
    Radio refreshOnce;
    Radio refreshHourly;
    TextField refreshHourlyDuration;
    Radio refreshDaily;
    TimeField refreshDailyTime;
    Button submitButton;
    SectionSelectBox contentBox;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultTemplate() {
        return "cms/mobile/mobileChannelForm";
    }

    public void init() {
        super.init();
        initFields();
    }

    public void onRequest(Event evt) {
        super.onRequest(evt);
        initFields();
    }

    public void initFields() {
        setMethod("POST");
        removeChildren();

        // add fields
        titleField = new TextField("title");
        titleField.addChild(new ValidatorNotEmpty("vTitle"));
        addChild(titleField);

        locationField = new TextField("location");
        addChild(locationField);

        maxSizeField = new TextField("maxSize", "100");
        maxSizeField.addChild(new ValidatorIsNumeric("vMaxSize"));
        maxSizeField.setSize("10");
        addChild(maxSizeField);

        depthField = new TextField("depth", "0");
        depthField.addChild(new ValidatorIsNumeric("vDepth"));
        depthField.setSize("10");
        addChild(depthField);

        allowImagesBox = new CheckBox("allowImages");
        addChild(allowImagesBox);

        offsiteLinksBox = new CheckBox("offsiteLinks");
        addChild(offsiteLinksBox);

        refreshAlways = new Radio("refreshAlways");
        refreshAlways.setGroupName("refresh");
        refreshAlways.setChecked(true);
        addChild(refreshAlways);

        refreshOnce = new Radio("refreshOnce");
        refreshOnce.setGroupName("refresh");
        addChild(refreshOnce);

        refreshHourly = new Radio("refreshHourly");
        refreshHourly.setGroupName("refresh");
        addChild(refreshHourly);

        refreshHourlyDuration = new TextField("refreshHourlyDuration", "1");
        refreshHourlyDuration.addChild(new ValidatorIsNumeric("vRefreshHourlyDuration"));
        refreshHourlyDuration.setSize("10");
        addChild(refreshHourlyDuration);

        refreshDaily = new Radio("refreshDaily");
        refreshDaily.setGroupName("refresh");
        addChild(refreshDaily);

        refreshDailyTime = new TimeField("refreshDailyTime");
        addChild(refreshDailyTime);

        contentBox = new SectionSelectBox("contentBox");
        addChild(contentBox);
        contentBox.init();

        Application application = Application.getInstance();
        submitButton = new Button("submitButton", application.getMessage("general.label.save", "Save"));
        addChild(submitButton);

        // if editing, load data
        if (id != null) {

            MobileModule mod = (MobileModule)Application.getInstance().getModule(MobileModule.class);
            try {
                MobileChannel channel = mod.getChannel(id);
                titleField.setValue(channel.getTitle());
                titleField.setHidden(true);

                maxSizeField.setValue(String.valueOf(channel.getMaxSize()));
                depthField.setValue(String.valueOf(channel.getDepth()));
                allowImagesBox.setChecked(channel.isAllowImages());
                offsiteLinksBox.setChecked(channel.isOffsiteLinks());
                refreshAlways.setChecked(MobileChannel.REFRESH_ALWAYS.equals(channel.getRefresh()));
                refreshDaily.setChecked(MobileChannel.REFRESH_DAILY.equals(channel.getRefresh()));
                refreshHourly.setChecked(MobileChannel.REFRESH_HOURLY.equals(channel.getRefresh()));
                refreshOnce.setChecked(MobileChannel.REFRESH_ONCE.equals(channel.getRefresh()));
                refreshHourlyDuration.setValue(String.valueOf(channel.getRefreshHourlyDuration()));
                refreshDailyTime.setDate(channel.getRefreshDailyTime());
                contentBox.setSelectedOptions(new String[] {channel.getContentId()});

                // get server URL
                //Application application = Application.getInstance();
                SetupModule setup = (SetupModule)application.getModule(SetupModule.class);
                String serverUrl = setup.get("siteUrl") + "/cmsmobile/index.jsp?id=" + channel.getTitle();

                // formulate location URL
                StringBuffer url = new StringBuffer();
                url.append("http://avantgo.com/mydevice/autoadd.html?");
                url.append("title=" + URLEncoder.encode(id));
                url.append("&url=" + URLEncoder.encode(serverUrl));
                url.append("&max=" + channel.getMaxSize());
                url.append("&depth=" + channel.getDepth());
                url.append("&images=" + ((channel.isAllowImages()) ? "1" : "0"));
                url.append("&links=" + ((channel.isOffsiteLinks()) ? "1" : "0"));
                url.append("&refresh=" + channel.getRefresh());
                url.append("&hours=" + channel.getRefreshHourlyDuration());
                url.append("&rtime_hour=" + new SimpleDateFormat("hh").format(channel.getRefreshDailyTime()));
                url.append("&rtime_minute=" + new SimpleDateFormat("mm").format(channel.getRefreshDailyTime()));
                url.append("&rtime_ampm=" + new SimpleDateFormat("aa").format(channel.getRefreshDailyTime()));
                locationField.setValue(url.toString());

            } catch (Exception e) {
                Log.getLog(getClass()).error("Channel " + id + " could not be retrieved " + e.toString(), e);
                throw new RuntimeException("Channel " + id + " could not be retrieved");
            }

        }
    }

    public Forward onValidate(Event evt) {

        // populate values
        MobileChannel channel = new MobileChannel();
        channel.setTitle((String)titleField.getValue());
        channel.setMaxSize(Integer.parseInt((String)maxSizeField.getValue()));
        channel.setDepth(Integer.parseInt((String)depthField.getValue()));
        channel.setAllowImages(allowImagesBox.isChecked());
        channel.setOffsiteLinks(offsiteLinksBox.isChecked());
        if (refreshAlways.isChecked())
            channel.setRefresh(MobileChannel.REFRESH_ALWAYS);
        else if (refreshDaily.isChecked())
            channel.setRefresh(MobileChannel.REFRESH_DAILY);
        else if (refreshHourly.isChecked())
            channel.setRefresh(MobileChannel.REFRESH_HOURLY);
        else if (refreshOnce.isChecked())
            channel.setRefresh(MobileChannel.REFRESH_ONCE);
        channel.setRefreshHourlyDuration(Integer.parseInt((String)refreshHourlyDuration.getValue()));
        channel.setRefreshDailyTime(refreshDailyTime.getDate());
        channel.setContentId((String)contentBox.getSelectedOptions().keySet().iterator().next());

        // save
        MobileModule mod = (MobileModule)Application.getInstance().getModule(MobileModule.class);
        try {
            mod.saveChannel(channel);
        } catch (ContentException e) {
            Log.getLog(getClass()).error("Channel " + id + " could not be saved " + e.toString(), e);
            throw new RuntimeException("Channel " + id + " could not be saved");
        }

        initFields();
        // reset section select box
        contentBox.populateSelectBox(evt);

        Forward f = super.onValidate(evt);
        return (f != null) ? f : new Forward("saved");
    }

    private Map getContentIdMap() {
        String text;

        ContentPublisher cp = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
        Map leftMap = new SequencedHashMap();
        try {
            Collection co = cp.viewList(null, new String[]{Section.class.getName(), VSection.class.getName()}, null, null, Boolean.FALSE, "name", false, 0, -1, null, null);
            for(Iterator iter = co.iterator(); iter.hasNext();) {
                ContentObject c = (ContentObject) iter.next();
                text = c.getName();
                if(text.length()>25) {
                    text = text.substring(0, 21) + "...";
                }
                leftMap.put(c.getId(), text);
            }
        } catch(ContentException e) {
            Log log = Log.getLog(this.getClass());
            log.error(e.getMessage(), e);
        }

        return leftMap;
    }

}

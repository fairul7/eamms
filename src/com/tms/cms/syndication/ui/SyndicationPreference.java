package com.tms.cms.syndication.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.Application;
import kacang.util.UuidGenerator;
import kacang.util.Log;
import kacang.ui.Forward;
import kacang.ui.Event;
import com.tms.cms.syndication.ui.validator.ValidatorRefreshRate;
import com.tms.cms.syndication.model.SynObject;
import com.tms.cms.syndication.model.SyndicationModule;
import com.tms.cms.syndication.model.SyndicationDaoException;
import com.tms.cms.syndication.model.SynFeedPredefined;

import java.util.*;

public class SyndicationPreference extends Form {
    public static String LINK_EXIST = "exist";
    private TextField tfTitle;
    private TextField tfLink;
    private TextField tfRefreshRate;
    private Button btAdd;
    private Button btCancel;
    private Button btUpdate;
    private Button btSubscribe;
    private ButtonGroup bgChannelList;
    private List linkList;
    private List predefinedList;


    private String id;

    public void init() {
        tfTitle = new TextField("title");
        tfTitle.addChild(new ValidatorNotEmpty("vntTitle",Application.getInstance().getMessage("syndication.label.title.message","Title cannot be empty")));
        addChild(tfTitle);

        tfLink = new TextField("link");
        tfLink.addChild(new ValidatorNotEmpty("vntLink",Application.getInstance().getMessage("syndication.label.link.message","Link cannot be empty")));
        addChild(tfLink);

        tfRefreshRate = new TextField("refreshRate");
        tfRefreshRate.setSize("5");
        tfRefreshRate.addChild(new ValidatorRefreshRate("vntRefreshRate",Application.getInstance().getMessage("syndication.label.refresh.rate.message","Refresh Rate must be numeric"),false));
        addChild(tfRefreshRate);

        btAdd = new Button("add");
        btAdd.setText(Application.getInstance().getMessage("syndication.label.add.button","Add"));
        addChild(btAdd);

        btUpdate = new Button("update");
        btUpdate.setText(Application.getInstance().getMessage("syndication.label.update.button","Add"));
        addChild(btUpdate);

        btCancel = new Button(CANCEL_FORM_ACTION);
        btCancel.setText(Application.getInstance().getMessage("syndication.label.cancel.button","Cancel"));
        addChild(btCancel);

        btSubscribe = new Button("subscribe");
        btSubscribe.setText(Application.getInstance().getMessage("syndication.label.subscribe","Subscribe"));
        addChild(btSubscribe);

        setMethod("post");
    }

    public void onRequest(Event event) {
        initializeFields();
        retriveList(event);
        retrivePredefineList();
    }

    public Forward onSubmit(Event event) {
        super.onSubmit(event);

        if (btSubscribe.getAbsoluteName().equals(findButtonClicked(event))) {
            initializeValidator();
        }
        return null;
    }

    public Forward onValidate(Event event) {
        String action = findButtonClicked(event);

        if (action != null) {
            if (btAdd.getAbsoluteName().equals(action)) {
                if(!linkExist()){
                    addSyndication(event);
                    return new Forward("added");
                }else return new Forward(LINK_EXIST);
            }
            else if (btUpdate.getAbsoluteName().equals(action)) {
                if(!linkExist()){
                    updateSyndication(event);
                    return new Forward("updated");
                }else return new Forward(LINK_EXIST);
            }
            else if (btSubscribe.getAbsoluteName().equals(action)) {
                addSyndications(event);
                return new Forward("subscribed");
            }

        }

        return null;
    }

    private boolean linkExist(){
        for(Iterator itr = linkList.iterator(); itr.hasNext();){
            SynObject sObj = (SynObject) itr.next();
            if(tfLink.getValue().toString().equals(sObj.getLink())){
                return true;
            }
        }
        return false;
    }

    public Forward actionPerformed(Event event) {
        String buttonClicked =  findButtonClicked(event);
        if ("editLink".equals(event.getType())) {
            setId(event.getRequest().getParameter("id"));
            populateData(getId());
            return null;
        }
        else if ("deleteLink".equals(event.getType())) {
            setId(event.getRequest().getParameter("id"));
            deleteLink(getId());
            return new Forward("deleted");
        }
        else if (btCancel.getAbsoluteName().equals(buttonClicked)){
            return new Forward("cancel");
        }
        else {
            return super.actionPerformed(event);
        }
    }

    public String getDefaultTemplate() {
        return "syndication/preference";
    }

    public void initializeFields() {
        tfTitle.setValue("");
        tfTitle.setInvalid(false);


        tfRefreshRate.setValue("");
        tfRefreshRate.setInvalid(false);

        tfLink.setValue("http://");
        tfLink.setInvalid(false);

        setId(null);

        initializeValidator();

    }

    public void initializeValidator() {
        ((ValidatorNotEmpty)tfTitle.getChild("vntTitle")).setInvalid(false);
        ((ValidatorRefreshRate)tfRefreshRate.getChild("vntRefreshRate")).setInvalid(false);
        ((ValidatorNotEmpty)tfLink.getChild("vntLink")).setInvalid(false);
        setInvalid(false);
    }

    public void retriveList(Event event) {
        SyndicationModule module = (SyndicationModule) Application.getInstance().getModule(SyndicationModule.class);
        try {
            linkList = (List) module.getSynObjectByUserId(event.getWidgetManager().getUser().getId());
        }
        catch (SyndicationDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
    }

    public void retrivePredefineList() {
        List list = null;
        CheckBox checkbox = null;
        CheckBox[] array = null;
        String categoryId = "";
        SynObject synObject = null;
        SynFeedPredefined object = null;
        Map linkMap = new HashMap();

        SyndicationModule module = (SyndicationModule) Application.getInstance().getModule(SyndicationModule.class);

        try {
            for (Iterator iterator = linkList.iterator(); iterator.hasNext();) {
                synObject = (SynObject) iterator.next();
                linkMap.put(synObject.getLink(),null);
            }

            predefinedList = (List) module.getSynFeedPredefined();
            list = new ArrayList();

            for (Iterator iterator = predefinedList.iterator(); iterator.hasNext();) {
                object = (SynFeedPredefined) iterator.next();

                if (!categoryId.equals(object.getCategoryId())) {
                    addChild(new Label("lbCategoryName"+ object.getCategoryId(), object.getCategoryName()));
                }
                checkbox = new CheckBox("cbChannel" + object.getId() , object.getName(),false);
                checkbox.setValue(object.getId());
                if (linkMap.containsKey(object.getLink()))
                    checkbox.setChecked(true);
                list.add(checkbox);
                categoryId = object.getCategoryId();
            }

            array = (CheckBox[]) list.toArray(new CheckBox[list.size()]);
            bgChannelList = new ButtonGroup("channelList",array);
            addChild(bgChannelList);
        }
        catch (SyndicationDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
    }

    public void addSyndication(Event event) {
        SynObject object = new SynObject();
        object.setUserId(event.getWidgetManager().getUser().getId());
        object.setId(UuidGenerator.getInstance().getUuid());
        object.setTitle(tfTitle.getValue().toString());
        object.setLink(tfLink.getValue().toString());
        object.setRefreshRate(tfRefreshRate.getValue().toString());

        SyndicationModule module = (SyndicationModule) Application.getInstance().getModule(SyndicationModule.class);
        try {
            module.addSynObject(object);
        }
        catch (SyndicationDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
    }

    public void addSyndications(Event event) {
        Collection buttons = bgChannelList.getButtons();
        Collection feeds = null;
        Collection synObject = null;
        CheckBox checkbox = null;
        SynObject object = null;


        SyndicationModule module = (SyndicationModule) Application.getInstance().getModule(SyndicationModule.class);
        for (Iterator iterator = buttons.iterator(); iterator.hasNext();) {
            checkbox =  (CheckBox)iterator.next();
            try {
                feeds = module.getSynFeedPredefined(checkbox.getValue().toString());
                if (checkbox.isChecked()) {
                    object = new SynObject();
                    object.setUserId(event.getWidgetManager().getUser().getId());
                    object.setId(UuidGenerator.getInstance().getUuid());
                    object.setTitle(checkbox.getText());
                    setFeeds(feeds,object);
                    synObject = module.getSynObject(object.getLink(),object.getUserId());
                    if ( (synObject = module.getSynObject(object.getLink(),object.getUserId())).size() > 0) {
                        object.setId(((SynObject)synObject.iterator().next()).getId());
                        module.updateSynObject(object,object.getId());
                    }
                    else {
                        module.addSynObject(object);
                    }

                }
                else {
                    if (feeds.iterator().hasNext())
                        module.deleteSynObject(((SynFeedPredefined)feeds.iterator().next()).getLink(),event.getWidgetManager().getUser().getId());
                }
            }
            catch (SyndicationDaoException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }

        }
    }

    public void setFeeds(Collection feeds, SynObject object) {
        SynFeedPredefined feed = null;

        for (Iterator iterator = feeds.iterator(); iterator.hasNext();) {
            feed =  (SynFeedPredefined) iterator.next();
            object.setRefreshRate(feed.getRefreshRate());
            object.setLink(feed.getLink());
        }

    }

    public void updateSyndication(Event event) {
        SynObject object = new SynObject();
        object.setUserId(event.getWidgetManager().getUser().getId());
        object.setId(getId());
        object.setTitle(tfTitle.getValue().toString());
        object.setLink(tfLink.getValue().toString());
        object.setRefreshRate(tfRefreshRate.getValue().toString());

        SyndicationModule module = (SyndicationModule) Application.getInstance().getModule(SyndicationModule.class);
        try {
            module.updateSynObject(object,getId());
        }
        catch (SyndicationDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }


    }

    public void deleteLink(String id) {
        SyndicationModule module = (SyndicationModule) Application.getInstance().getModule(SyndicationModule.class);
        try {
            module.deleteSynObject(id);
        }
        catch (SyndicationDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
    }

    public void populateData(String id) {
        SyndicationModule module = (SyndicationModule) Application.getInstance().getModule(SyndicationModule.class);

        try {
            Collection data = module.getSynObject(id);
            if (data.iterator().hasNext()) {
                SynObject object = (SynObject) data.iterator().next();
                tfTitle.setValue(object.getTitle());
                tfLink.setValue(object.getLink());
                tfRefreshRate.setValue(object.getRefreshRate());
            }
        }
        catch (SyndicationDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
    }

    public TextField getTfTitle() {
        return tfTitle;
    }

    public void setTfTitle(TextField tfTitle) {
        this.tfTitle = tfTitle;
    }

    public TextField getTfLink() {
        return tfLink;
    }

    public void setTfLink(TextField tfLink) {
        this.tfLink = tfLink;
    }

    public TextField getTfRefreshRate() {
        return tfRefreshRate;
    }

    public void setTfRefreshRate(TextField tfRefreshRate) {
        this.tfRefreshRate = tfRefreshRate;
    }

    public Button getBtAdd() {
        return btAdd;
    }

    public void setBtAdd(Button btAdd) {
        this.btAdd = btAdd;
    }

    public Button getBtCancel() {
        return btCancel;
    }

    public void setBtCancel(Button btCancel) {
        this.btCancel = btCancel;
    }

    public List getLinkList() {
        return linkList;
    }

    public void setLinkList(List linkList) {
        this.linkList = linkList;
    }

    public Button getBtUpdate() {
        return btUpdate;
    }

    public void setBtUpdate(Button btUpdate) {
        this.btUpdate = btUpdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List getPredefinedList() {
        return predefinedList;
    }

    public void setPredefinedList(List predefinedList) {
        this.predefinedList = predefinedList;
    }

    public ButtonGroup getBgChannelList() {
        return bgChannelList;
    }

    public void setBgChannelList(ButtonGroup bgChannelList) {
        this.bgChannelList = bgChannelList;
    }

    public Button getBtSubscribe() {
        return btSubscribe;
    }

    public void setBtSubscribe(Button btSubscribe) {
        this.btSubscribe = btSubscribe;
    }


}

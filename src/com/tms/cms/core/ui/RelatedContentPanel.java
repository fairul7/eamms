package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentUtil;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.stdui.event.FormEventAdapter;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import java.util.*;

public class RelatedContentPanel extends Panel {

    private String id;

    private Form containerForm;
    private TextField keywordField;
    private ContentKeywordsSelectBox keywordSelectBox;
    private Button submitButton;
    private Button cancelButton;

    public RelatedContentPanel() {
    }

    public RelatedContentPanel(String name) {
        super(name);
    }

    public void init() {
        addContainerForm();
    }

    public void onRequest(Event evt) {
        ContentObject co = ContentHelper.getContentObject(evt, getId());
        if (co != null) {
            setId(co.getId());
        }
        addContainerForm();
    }

    protected void addContainerForm() {
        // remove existing widgets
        removeChildren();

        // add container form
        containerForm = new Form("containerForm");
        containerForm.setMethod("POST");
        containerForm.setColumns(2);
        addChild(containerForm);

        // get content form
        try {
            if (getId() != null) {
                // retrieve from module
                Application application = Application.getInstance();
                User user = getWidgetManager().getUser();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                ContentObject co = cm.view(getId(), user);

                containerForm.addChild(new Label("l1", application.getMessage("cms.label.selectKeywords", "Select Existing Keywords")));

                // add keyword select box
                keywordSelectBox = new ContentKeywordsSelectBox("keywordSelectBox");
                containerForm.addChild(keywordSelectBox);
                keywordSelectBox.init();
                Collection coKeywords = ContentUtil.keywordStringToCollection(co.getKeywords());
                String[] keywordsArray = (String[])coKeywords.toArray(new String[0]);
                keywordSelectBox.setIds(keywordsArray);

                containerForm.addChild(new Label("l2", application.getMessage("cms.label.newKeyword", "New Keyword")));

                // add keyword field
                keywordField = new TextField("keywordField");
                keywordField.setSize("20");
                containerForm.addChild(keywordField);

                containerForm.addChild(new Label("l3", ""));

                // add buttons
                Panel buttonPanel = new Panel("buttonPanel");
                containerForm.addChild(buttonPanel);
                submitButton = new Button("submitButton");
                submitButton.setText(application.getMessage("general.label.submit", "Submit"));
                buttonPanel.addChild(submitButton);
                cancelButton = new Button(Form.CANCEL_FORM_ACTION);
                cancelButton.setText(application.getMessage("general.label.cancel", "Cancel"));
                buttonPanel.addChild(cancelButton);

                // add form listener
                containerForm.addFormEventListener(new FormEventAdapter() {
                    public Forward onValidate(Event evt) {
                        // get selected button
                        WidgetManager wm = evt.getWidgetManager();
                        String buttonClicked = containerForm.findButtonClicked(evt);
                        Widget button = wm.getWidget(buttonClicked);
                        if (submitButton.equals(button)) {
                            updateKeywords();
                            init();
                            return new Forward("submit");
                        }
                        else {
                            return new Forward(Form.CANCEL_FORM_ACTION);
                        }
                    }
                });

                // add forwards
                for (Iterator it=getForwardMap().values().iterator(); it.hasNext();) {
                    containerForm.addForward((Forward)it.next());
                }
            }
        }
        catch(ContentException e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected void updateKeywords() {
        try {
            // get keywords
            String keywords = (String)keywordField.getValue();
            Collection kwList = new ArrayList(Arrays.asList(keywordSelectBox.getIds()));
            kwList.addAll(ContentUtil.keywordStringToCollection(keywords));

            // set keywords
            User user = getWidgetManager().getUser();
            ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
            ContentObject co = cm.view(getId(), user);
            co.setKeywords(ContentUtil.keywordCollectionToString(kwList));

            // save
            cm.updateRelated(co, user);

        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }


}

package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentUtil;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Panel;
import kacang.stdui.TextField;
import kacang.stdui.event.FormEventAdapter;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import java.util.Collection;
import java.util.Iterator;

public class ContentKeywordsPanel extends Panel {

    private Form containerForm;
    private TextField keywordField;
    private Button submitButton;
    private Button cancelButton;

    public ContentKeywordsPanel() {
    }

    public ContentKeywordsPanel(String name) {
        super(name);
    }

    public void init() {
        addContainerForm();
    }

    public void onRequest(Event evt) {
    }

    protected void addContainerForm() {
        // remove existing widgets
        removeChildren();

        // add container form
        containerForm = new Form("containerForm");
        addChild(containerForm);

        // get content form
        try {
	        Application application = Application.getInstance();
            // add keyword field
            keywordField = new TextField("keywordField");
            keywordField.setSize("20");
            containerForm.addChild(keywordField);

            // add buttons
            submitButton = new Button("submitButton");
            submitButton.setText(application.getMessage("general.label.submit", "Submit"));
            containerForm.addChild(submitButton);
            cancelButton = new Button(Form.CANCEL_FORM_ACTION);
            cancelButton.setText(application.getMessage("general.label.cancel", "Cancel"));
            containerForm.addChild(cancelButton);

            // add form listener
            containerForm.addFormEventListener(new FormEventAdapter() {
                public Forward onValidate(Event evt) {
                    // get selected button
                    WidgetManager wm = evt.getWidgetManager();
                    String buttonClicked = containerForm.findButtonClicked(evt);
                    Widget button = wm.getWidget(buttonClicked);
                    if (submitButton.equals(button)) {
                        createKeywords();
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
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
            throw new RuntimeException(e.toString());
        }

    }

    protected void createKeywords() {
        try {
            // get keywords
            String keywords = (String)keywordField.getValue();

            // set keywords
            ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
            Collection kwList = ContentUtil.keywordStringToCollection(keywords);
            if (kwList.size() > 0) {
                cm.createKeywords((String[])kwList.toArray(new String[0]));
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }


}

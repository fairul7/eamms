package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentModule;
import com.tms.cms.core.model.ContentObject;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.stdui.event.FormEventAdapter;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import java.util.Iterator;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 21, 2003
 * Time: 4:44:04 PM
 * To change this template use Options | File Templates.
 */
public class PublishContentObjectPanel extends Panel {

    private String id;

    private Form containerForm;

    private ContentObjectView contentObjectView;

    private CheckBox scheduleCheckBox;
    private DateField startDate;
    private TimeField startTime;
    private DateField endDate;
    private TimeField endTime;
    private CheckBox recursiveBox;
    private Button publishButton;
    private Button withdrawButton;
    private Button cancelButton;

    public PublishContentObjectPanel() {
    }

    public PublishContentObjectPanel(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/publishContentObjectPanel";
    }

    public void init() {
        // remove existing widgets
        removeChildren();

        // add content specific form
        addContainerForm();
    }

    public void onRequest(Event evt) {
        ContentObject co = ContentHelper.getContentObject(evt, getId());
        if (co != null) {
            setId(co.getId());
        }
        init();
    }

    protected void addContainerForm() {
        try {
            if (getId() != null) {
                // retrieve from module
                User user = getWidgetManager().getUser();
                ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
                ContentObject contentObject = cm.view(getId(), user);

                // retrieve specific class
                ContentModule module = (ContentModule)Application.getInstance().getModule(contentObject.getContentModuleClass());
                contentObjectView = module.getContentObjectView(contentObject.getClass());

                // initialize content view
                contentObjectView.setName("contentObjectView");
                contentObjectView.init();
                contentObjectView.setContentObject(contentObject);

                // add content view
                addChild(contentObjectView);

                // add container form
                containerForm = new Form("containerForm");
                addChild(containerForm);

                // add scheduling
                Application application = Application.getInstance();
                scheduleCheckBox = new CheckBox("scheduleCheckBox");
                scheduleCheckBox.setText(application.getMessage("general.label.schedule", "Schedule"));
                if (contentObject.getStartDate() != null) {
                    scheduleCheckBox.setChecked(true);
                }
                containerForm.addChild(scheduleCheckBox);

                startDate = new DateField("startDate");
                if (contentObject.getStartDate() != null) {
                    startDate.setDate(contentObject.getStartDate());
                }
                containerForm.addChild(startDate);

                startTime = new TimeField("startTime");
                if (contentObject.getStartDate() != null) {
                    startTime.setDate(contentObject.getStartDate());
                }
                containerForm.addChild(startTime);

                endDate = new DateField("endDate");
                if (contentObject.getEndDate() != null) {
                    endDate.setDate(contentObject.getEndDate());
                }
                containerForm.addChild(endDate);

                endTime = new TimeField("endTime");
                if (contentObject.getEndDate() != null) {
                    endTime.setDate(contentObject.getEndDate());
                }
                containerForm.addChild(endTime);

                // add checkboxes
                recursiveBox = new CheckBox("recursiveBox");
                recursiveBox.setText(application.getMessage("general.label.propagate", "Propagate"));
                containerForm.addChild(recursiveBox);

                // add buttons
                publishButton = new Button("publishButton");
                publishButton.setText(application.getMessage("general.label.publish", "Publish"));
                containerForm.addChild(publishButton);
                withdrawButton = new Button("withdrawButton");
                withdrawButton.setText(application.getMessage("general.label.withdraw", "Withdraw"));
                containerForm.addChild(withdrawButton);
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
                        if (publishButton.equals(button)) {
                            ContentObject contentObject = contentObjectView.getContentObject();
                            boolean recursive = recursiveBox.isChecked();
                            boolean schedule = scheduleCheckBox.isChecked();
                            if (schedule) {
                                Calendar cDate = Calendar.getInstance();
                                Calendar cTime = Calendar.getInstance();
                                cDate.setTime(startDate.getDate());
                                cTime.setTime(startTime.getDate());
                                cDate.set(Calendar.HOUR_OF_DAY, cTime.get(Calendar.HOUR_OF_DAY));
                                cDate.set(Calendar.MINUTE, cTime.get(Calendar.MINUTE));
                                cDate.set(Calendar.SECOND, 0);
                                contentObject.setStartDate(cDate.getTime());

                                cDate.setTime(endDate.getDate());
                                cTime.setTime(endTime.getDate());
                                cDate.set(Calendar.HOUR_OF_DAY, cTime.get(Calendar.HOUR_OF_DAY));
                                cDate.set(Calendar.MINUTE, cTime.get(Calendar.MINUTE));
                                cDate.set(Calendar.SECOND, 0);
                                contentObject.setEndDate(cDate.getTime());
                                contentObject.setEndDate(cDate.getTime());
                            }
                            else {
                                contentObject.setStartDate(null);
                                contentObject.setEndDate(null);
                            }
                            publishContentObject(contentObject, recursive);
                            init();
                            return new Forward("publish");
                        }
                        else if (withdrawButton.equals(button)) {
                            ContentObject contentObject = contentObjectView.getContentObject();
                            boolean recursive = recursiveBox.isChecked();
                            withdrawContentObject(contentObject, recursive);
                            init();
                            return new Forward("withdraw");
                        }
                        else {
                            return new Forward(Form.CANCEL_FORM_ACTION);
                        }
                    }
                });

                // add forwards
                for (Iterator i=getForwardMap().values().iterator(); i.hasNext();) {
                    containerForm.addForward((Forward)i.next());
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

    protected void publishContentObject(ContentObject contentObject, boolean recursive) {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
        try {
            // publish
            User user = getWidgetManager().getUser();
            contentManager.publishOnSchedule(contentObject, recursive, user);
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    protected void withdrawContentObject(ContentObject contentObject, boolean recursive) {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
        try {
            // withdraw
            User user = getWidgetManager().getUser();
            contentManager.withdraw(contentObject.getId(), recursive, user);
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

}

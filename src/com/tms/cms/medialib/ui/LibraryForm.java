/*
 * LibraryForm
 * Date Created: Jun 16, 2005
 * Author: Tien Soon, Law
 * Description: Abstract library form
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.ui;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.ComboSelectBox;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.collab.forum.ui.UserGroupComboSelect;


public abstract class LibraryForm extends Form {
    protected TextField libraryName;
    protected TextBox description;
    protected TextField maxWidth;
    protected ComboSelectBox managerGroup;
    protected ComboSelectBox contributorGroup;
    protected ComboSelectBox viewerGroup;
    protected Radio needApproval;
    protected Radio noNeedApproval;
    protected Button submit;
    protected Button cancel;
    protected ValidatorNotEmpty vne;
    protected static final String FORWARD_SUCCESS = "success";
    protected static final String FORWARD_FAILURE = "failure";
    
    public void init() {
        setMethod("POST");
        setColumns(2);
        
        Application app = Application.getInstance();
        
        vne = new ValidatorNotEmpty("vne", app.getMessage("medialib.message.notEmpty", "Please fill in this field"));
        
        // Library Name
        addChild(new Label("l1", app.getMessage("medialib.label.name*", "Name *")));
        libraryName = new TextField("libraryName");
        libraryName.setMaxlength("255");
        libraryName.addChild(vne);
        addChild(libraryName);
        
        // Descriptions
        addChild(new Label("l2", app.getMessage("medialib.label.description", "Description")));
        description = new TextBox("description");
        addChild(description);
        
        // Max Width
        addChild(new Label("l10", app.getMessage("medialib.label.maxWidthPixel*", "Max Image Width (pixel) *")));
        maxWidth = new TextField("maxWidth");
        maxWidth.setValue("500");
        maxWidth.setMaxlength("10");
        maxWidth.setSize("10");
        maxWidth.setAutoTrim(true);
        maxWidth.addChild(vne);
        addChild(maxWidth);
        
        // Publishing Options
        addChild(new Label("l3", app.getMessage("medialib.label.publishingOptions*", "Publishing Options *")));
        needApproval = new Radio("needApproval");
        needApproval.setText(app.getMessage("medialib.label.publishingOptions.approvalNeeded", "Approval needed before publishing"));
        needApproval.setGroupName("publishingOp");
        needApproval.setChecked(true);
        
        noNeedApproval = new Radio("noNeedApproval");
        noNeedApproval.setText(app.getMessage("medialib.label.publishingOptions.noApprovalNeeded", "No approval needed before publishing"));
        noNeedApproval.setGroupName("publishingOp");
        
        Panel publishingOpPanel = new Panel("publishingOpPanel");
        publishingOpPanel.setColumns(1);
        publishingOpPanel.addChild(needApproval);
        publishingOpPanel.addChild(noNeedApproval);
        addChild(publishingOpPanel);
        
        addChild(new Label("l5", ""));
        addChild(new Label("l6", ""));
        
        // Sub title - Permission Setting
        Label permissionLabel = new Label("permissionLabel", app.getMessage("medialib.label.permissionSetting", "Permission Setting"));
        permissionLabel.setTemplate("medialib/formSubHeading");
        addChild(permissionLabel);
        addChild(new Label("l4", ""));
        
        // Manager Group
        addChild(new Label("l7", app.getMessage("medialib.label.managerGroup*" , "Manager Group *")));
        managerGroup = new UserGroupComboSelect("managerGroup");
        addChild(managerGroup);
        managerGroup.init();
        managerGroup.getRightSelect().addChild(new ValidatorNotEmpty("vModeratorGroup", app.getMessage("medialib.error.emptyManager", "Album Manager Group must not be empty")));
        
        // Contributor Group
        addChild(new Label("l8", app.getMessage("medialib.label.contributorGroup", "Contributor Group")));
        contributorGroup = new UserGroupComboSelect("contributorGroup");
        addChild(contributorGroup);
        contributorGroup.init();
        
        // Viewer Group
        addChild(new Label("l9", app.getMessage("medialib.label.viewerGroup", "Viewer Group")));
        viewerGroup = new UserGroupComboSelect("viewerGroup");
        addChild(viewerGroup);
        viewerGroup.init();
        
        // Submit and Cancel Buttons
        submit = new Button("submit");
        submit.setText(app.getMessage("medialib.label.submit", "Submit"));
        
        cancel = new Button(Form.CANCEL_FORM_ACTION, "cancel");
        cancel.setText(app.getMessage("medialib.label.cancel", "Cancel"));
        cancel.setOnClick("return back()");
        
        Panel buttonsPanel = new Panel("buttonsPanel");
        buttonsPanel.addChild(submit);
        buttonsPanel.addChild(cancel);
        addChild(buttonsPanel);
    }
    
    public void onRequest(Event evt) {
        init();
    }
    
    public Forward onValidate(Event event) {
        super.removeChildren();
        super.init();
        
        return new Forward(FORWARD_SUCCESS);
    }
    
    public Forward onSubmit(Event event) {
        Forward forward = super.onSubmit(event);
        String maxWidthValue = (String) maxWidth.getValue();
        
        try {
            Integer.parseInt(maxWidthValue);
        }
        catch(NumberFormatException error) {
            maxWidth.setInvalid(true);
            this.setInvalid(true);
        }
        
        return forward;
    }
}

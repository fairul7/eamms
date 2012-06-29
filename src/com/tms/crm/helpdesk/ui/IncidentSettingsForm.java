package com.tms.crm.helpdesk.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.Application;
import kacang.util.Log;
import kacang.ui.Event;
import kacang.ui.Forward;
import com.tms.crm.helpdesk.HelpdeskHandler;
import com.tms.crm.helpdesk.IncidentSettings;
import com.tms.crm.helpdesk.HelpdeskException;

public class IncidentSettingsForm extends Form {

    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_FAILURE = "failure";

    protected TextBox severityOptions;
    protected TextBox resolutionStateOptions;
    protected TextBox contactedByOptions;
    protected TextBox incidentTypeOptions;
    protected TextField property1Label;
    protected TextField property2Label;
    protected TextField property3Label;
    protected TextField property4Label;
    protected TextField property5Label;
    protected TextField property6Label;
    protected TextBox property1Options;
    protected TextBox property2Options;
    protected TextBox property3Options;
    protected TextBox property4Options;
    protected TextBox property5Options;
    protected TextBox property6Options;
    protected Button submit;
    protected Button cancel;
    protected ValidatorNotEmpty validseverityOptions;
    protected ValidatorNotEmpty validresolutionStateOptions;
    protected ValidatorNotEmpty validcontactedByOptions;
    protected ValidatorNotEmpty validincidentTypeOptions;

    
    public String getDefaultTemplate() {
    	return "helpdesk/incidentSettings";
    }
    
    
    public IncidentSettingsForm() {
        super();
    }

    public IncidentSettingsForm(String name) {
        super(name);
    }

    public void init() {
        Application app = Application.getInstance();
        setMethod("POST");
        setColumns(2);

        // init fields
        severityOptions = new TextBox("severityOptions");
        resolutionStateOptions = new TextBox("resolutionStateOptions");
        contactedByOptions = new TextBox("contactedByOptions");
        incidentTypeOptions = new TextBox("incidentTypeOptions");
        property1Label = new TextField("property1Label");
        property2Label = new TextField("property2Label");
        property3Label = new TextField("property3Label");
        property4Label = new TextField("property4Label");
        property5Label = new TextField("property5Label");
        property6Label = new TextField("property6Label");
        property1Options = new TextBox("property1Options");
        property2Options = new TextBox("property2Options");
        property3Options = new TextBox("property3Options");
        property4Options = new TextBox("property4Options");
        property5Options = new TextBox("property5Options");
        property6Options = new TextBox("property6Options");
        validseverityOptions = new ValidatorNotEmpty("validseverityOptions");
        validresolutionStateOptions = new ValidatorNotEmpty("validresolutionStateOptions");
        validcontactedByOptions = new ValidatorNotEmpty("validcontactedByOptions");
        validincidentTypeOptions = new ValidatorNotEmpty("validincidentTypeOptions");
        submit = new Button("submit", app.getMessage("general.label.submit"));
        cancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("general.label.cancel"));
        severityOptions.addChild(validseverityOptions);
        resolutionStateOptions.addChild(validresolutionStateOptions);
        contactedByOptions.addChild(validcontactedByOptions);
        incidentTypeOptions.addChild(validincidentTypeOptions);
        // add fields to form
        addChild(new Label("l1", app.getMessage("helpdesk.label.severityOptions")+" *"));
        addChild(severityOptions);
        addChild(new Label("l2", app.getMessage("helpdesk.label.resolutionStateOptions")+" *"));
        addChild(resolutionStateOptions);
        addChild(new Label("l3", app.getMessage("helpdesk.label.contactedByOptions")+" *"));
        addChild(contactedByOptions);
        addChild(new Label("l4", app.getMessage("helpdesk.label.incidentTypeOptions")+" *"));
        addChild(incidentTypeOptions);
        addChild(new Label("l5", app.getMessage("helpdesk.label.property1Label")));
        addChild(property1Label);
        addChild(new Label("l11", app.getMessage("helpdesk.label.property1Options")));
        addChild(property1Options);
        addChild(new Label("l6", app.getMessage("helpdesk.label.property2Label")));
        addChild(property2Label);
        addChild(new Label("l12", app.getMessage("helpdesk.label.property2Options")));
        addChild(property2Options);
        addChild(new Label("l7", app.getMessage("helpdesk.label.property3Label")));
        addChild(property3Label);
        addChild(new Label("l13", app.getMessage("helpdesk.label.property3Options")));
        addChild(property3Options);
        addChild(new Label("l8", app.getMessage("helpdesk.label.property4Label")));
        addChild(property4Label);
        addChild(new Label("l14", app.getMessage("helpdesk.label.property4Options")));
        addChild(property4Options);
        addChild(new Label("l9", app.getMessage("helpdesk.label.property5Label")));
        addChild(property5Label);
        addChild(new Label("l15", app.getMessage("helpdesk.label.property5Options")));
        addChild(property5Options);
        addChild(new Label("l10", app.getMessage("helpdesk.label.property6Label")));
        addChild(property6Label);
        addChild(new Label("l16", app.getMessage("helpdesk.label.property6Options")));
        addChild(property6Options);
        addChild(new Label("l17", ""));
        Panel buttonPanel = new Panel("p1");
        buttonPanel.addChild(submit);
//        buttonPanel.addChild(cancel);
        addChild(buttonPanel);
    }

    public void onRequest(Event evt) {
        try {
            // populate form
            Application app = Application.getInstance();
            HelpdeskHandler handler = (HelpdeskHandler)app.getModule(HelpdeskHandler.class);
            IncidentSettings settings = handler.getIncidentSettings();
            severityOptions.setValue(settings.getSeverityOptions());
            resolutionStateOptions.setValue(settings.getResolutionStateOptions());
            contactedByOptions.setValue(settings.getContactedByOptions());
            incidentTypeOptions.setValue(settings.getIncidentTypeOptions());
            property1Label.setValue(settings.getProperty1Label());
            property2Label.setValue(settings.getProperty2Label());
            property3Label.setValue(settings.getProperty3Label());
            property4Label.setValue(settings.getProperty4Label());
            property5Label.setValue(settings.getProperty5Label());
            property6Label.setValue(settings.getProperty6Label());
            property1Options.setValue(settings.getProperty1Options());
            property2Options.setValue(settings.getProperty2Options());
            property3Options.setValue(settings.getProperty3Options());
            property4Options.setValue(settings.getProperty4Options());
            property5Options.setValue(settings.getProperty5Options());
            property6Options.setValue(settings.getProperty6Options());
        }
        catch (HelpdeskException e) {
            // ignore, module already logs
        }
    }

    public Forward onValidate(Event evt) {

        try {
            Application app = Application.getInstance();
            HelpdeskHandler handler = (HelpdeskHandler)app.getModule(HelpdeskHandler.class);
            IncidentSettings settings = handler.getIncidentSettings();

            // populate settings object
            settings.setSeverityOptions((String)severityOptions.getValue());
            settings.setResolutionStateOptions((String)resolutionStateOptions.getValue());
            settings.setContactedByOptions((String)contactedByOptions.getValue());
            settings.setIncidentTypeOptions((String)incidentTypeOptions.getValue());
            settings.setProperty1Label((String)property1Label.getValue());
            settings.setProperty2Label((String)property2Label.getValue());
            settings.setProperty3Label((String)property3Label.getValue());
            settings.setProperty4Label((String)property4Label.getValue());
            settings.setProperty5Label((String)property5Label.getValue());
            settings.setProperty6Label((String)property6Label.getValue());
            settings.setProperty1Options((String)property1Options.getValue());
            settings.setProperty2Options((String)property2Options.getValue());
            settings.setProperty3Options((String)property3Options.getValue());
            settings.setProperty4Options((String)property4Options.getValue());
            settings.setProperty5Options((String)property5Options.getValue());
            settings.setProperty6Options((String)property6Options.getValue());

            // save settings
            handler.updateIncidentSettings(settings);

            return new Forward(FORWARD_SUCCESS);
        }
        catch (HelpdeskException e) {
            Log.getLog(getClass()).error("Error updating incident settings", e);
            return new Forward(FORWARD_FAILURE);
        }
    }

}

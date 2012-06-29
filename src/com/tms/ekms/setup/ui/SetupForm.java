package com.tms.ekms.setup.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.stdui.*;
import kacang.stdui.FormField;
import kacang.Application;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.ekms.setup.model.SetupException;

import java.util.*;

public class SetupForm extends Form {

    private List defaultPropertyList;

    public void init() {
        setMethod("POST");
        initFields();
    }

    public void onRequest(Event event) {
        super.onRequest(event);
        initFields();
    }

    protected void initFields() {
        removeChildren();

        // load properties from module
        defaultPropertyList = new ArrayList();
        Map propertyMap = new HashMap();
        Application application = Application.getInstance();
        SetupModule setup = (SetupModule)application.getModule(SetupModule.class);
        try {
            propertyMap = setup.getAll();
        } catch (SetupException e) {
            throw new RuntimeException("Error loading setup properties: " + e.toString());
        }

        // create form fields
        Panel defPanel = new Panel("defaultPanel");
        defPanel.setColumns(2);
        for (Iterator i=propertyMap.keySet().iterator(); i.hasNext();) {
            String property = (String)i.next();
            String value = (String)propertyMap.get(property);
            String label = application.getMessage("setup.label." + property, property);

            defaultPropertyList.add(property);
            Label lbl = new Label(property + "Label", label);
            TextField txt = new TextField(property, value);
            defPanel.addChild(lbl);
            defPanel.addChild(txt);
            labels.add(lbl);
            textfields.add(txt);
            
        }
        Label lbl = new Label("blankLabel");
        Button b= new Button("submitButton", application.getMessage("general.label.save", "Save"));
        defPanel.addChild(lbl);
        defPanel.addChild(b);
        addChild(defPanel);
        button = b;
    }
    
    private Button button; 
    public Button getButton() { 
    	return button;
    }
    
    private List labels = new ArrayList();
    public List getLabels() {
    	return labels;
    }
    private List textfields = new ArrayList();
    public List getTextFields() {
    	return textfields;
    }
    


    public Forward onValidate(Event event) {
        Map propertyMap = new HashMap();

        // process default
        for (int i=0; i<defaultPropertyList.size(); i++) {
            String propName = (String)defaultPropertyList.get(i);
            FormField field = (FormField)getChild("defaultPanel").getChild(propName);
            String value = (String)field.getValue();
            propertyMap.put(propName, value);
        }

        // save property map
        SetupModule setup = (SetupModule)Application.getInstance().getModule(SetupModule.class);
        try {
            setup.saveAll(propertyMap);
        } catch (SetupException e) {
            throw new RuntimeException("Error saving setup properties: " + e.toString());
        }

        initFields();
        return super.onValidate(event);
    }


    public String getDefaultTemplate() {
        return "cms/admin/setupForm";
    }

}

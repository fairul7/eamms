package com.tms.ekms.setup.ui;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;

import java.util.Map;
import java.util.HashMap;

import com.tms.ekms.setup.model.SetupModule;
import com.tms.ekms.setup.model.SetupException;

public class SetupWidget extends LightWeightWidget {

    private Map propertyMap = new HashMap();

    public String getDefaultTemplate() {
        return null;
    }

    public void onRequest(Event event) {
        Application application = Application.getInstance();
        SetupModule setup = (SetupModule)application.getModule(SetupModule.class);
        try {
            propertyMap = setup.getAll();
        } catch (SetupException e) {
        }
    }

    public Map getPropertyMap() {
        return propertyMap;
    }
}

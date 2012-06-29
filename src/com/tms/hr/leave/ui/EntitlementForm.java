package com.tms.hr.leave.ui;

import com.tms.collab.formwizard.ui.validator.ValidatorIsInteger;
import com.tms.hr.employee.model.EmployeeModule;
import com.tms.hr.leave.model.LeaveModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorRange;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Collection;
import java.util.Calendar;

public class EntitlementForm extends Form {

    protected LeaveTypeSelectBox leaveType;
    protected SelectBox serviceClass;
    protected TextField serviceYears;
    protected TextField entitlement;
    protected Button addButton;

    public EntitlementForm() {
        super();
    }

    public EntitlementForm(String name) {
        super(name);
    }

    public void init() {
        initForm();
    }

    public void onRequest(Event evt) {
        initForm();
    }
    
    
    public String getDefaultTemplate() {
    	return "leave/entitlementForm";
    }

    public void initForm() {
        setMethod("POST");
        removeChildren();

        Application application = Application.getInstance();

        leaveType = new LeaveTypeSelectBox("leaveType");

        serviceClass = new SelectBox("serviceClass");
        try {
            EmployeeModule em = (EmployeeModule)application.getModule(EmployeeModule.class);
            Collection serviceList = em.getAllServiceClass();
            serviceClass.setOptions(serviceList, "serviceCode", "serviceDesc");
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving service classes", e);
        }

        serviceYears = new TextField("serviceYears");
        //serviceYears.addChild(new ValidatorIsNumeric("v1"));
        serviceYears.addChild(new ValidatorRange("v1","",new Double(1.0),new Double(100)));
        serviceYears.setSize("5");

        entitlement = new TextField("entitlement");
        //entitlement.addChild(new ValidatorIsNumeric("v1"));
        serviceYears.addChild(new ValidatorRange("v2","",new Double(1.0),new Double(100)));
        entitlement.addChild(new ValidatorNotEmpty("validEntitlement"));
        entitlement.addChild(new ValidatorIsInteger("validNumberEntitlement"));
        entitlement.setSize("5");

        addButton = new Button("addButton", application.getMessage("general.label.add", "Add"));

        addChild(leaveType);
        addChild(serviceClass);
        addChild(new Label("l1", application.getMessage("leave.label.serviceYears", "Service Years")+" *"));
        addChild(serviceYears);
        addChild(new Label("l2", application.getMessage("leave.label.entitlement", "Entitlement")+" *"));
        addChild(entitlement);
        addChild(addButton);

    }

    public Forward onValidate(Event evt) {
        try {
            Application application = Application.getInstance();
            LeaveModule lm = (LeaveModule)application.getModule(LeaveModule.class);

            Collection tmp = (Collection)serviceClass.getValue();
            String serviceClassId = (String)tmp.iterator().next();
            tmp = (Collection)leaveType.getValue();
            String leaveType = (String)tmp.iterator().next();
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int servYears = Integer.parseInt(serviceYears.getValue().toString());
            int days = Integer.parseInt(entitlement.getValue().toString());

            lm.updateLeaveEntitlement(serviceClassId, leaveType, year, servYears, days);

            // reset fields
            serviceYears.setValue("");
            entitlement.setValue("");
            
            return new Forward("success");
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving leave types", e);
            return new Forward("failure");
        }
    }

}

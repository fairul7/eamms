package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimConfigTypeModule;
import com.tms.hr.claim.model.ClaimTypeDepartObject;
import com.tms.hr.claim.model.ClaimTypeObject;
import com.tms.hr.employee.model.DepartmentDataObject;
import com.tms.hr.employee.model.EmployeeModule;

import kacang.Application;

import kacang.stdui.*;

import kacang.ui.Event;
import kacang.ui.Forward;

import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: cheewei
 * Date: Dec 9, 2005
 * Time: 9:56:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClaimConfigTypeDepartForm extends Form {
    private Label typeLabel;
    private Label typeLabelText;
    private Label departmentLabel;
    private SelectBox departmentBox;
    private String id; //id for type
	private Panel buttonPanel;
    private Button submitButton;
	private Button cancel;

	public static final String FORWARD_CANCEL = "cancel";

    public void init() {
    }

    
    public String getDefaultTemplate() {
    	return "claims/config_typedeptedit";
    }
    
    
    public void onRequest(Event evt) {
        setColumns(2);
        setMethod("Post");

        Application app = Application.getInstance();
        ClaimConfigTypeModule module = (ClaimConfigTypeModule) app.getModule(ClaimConfigTypeModule.class);
        EmployeeModule employeeModule = (EmployeeModule) app.getModule(EmployeeModule.class);

        typeLabel = new Label("typeLabel",
                app.getMessage("claims.type.name", "Type"));
        addChild(typeLabel);

        if (getId() != null) {
            ClaimTypeObject object = module.retrieveType(getId());

            typeLabelText = new Label("typeLabelText", object.getTypeName());
        } else {
            typeLabelText = new Label("typeLabelText",
                    app.getMessage("claims.label.notavailable"));
        }

        addChild(typeLabelText);

        departmentLabel = new Label("departmentLabel",
                app.getMessage("claims.label.department"));
        addChild(departmentLabel);

        departmentBox = new SelectBox("departmentBox");
        departmentBox.setRows(10);
        departmentBox.setMultiple(true);

        //get list of all departments
        Map departmentMap = new SequencedHashMap();

        try {
            Collection departmentCol = employeeModule.getDepartmentList();
            Collection allDepartmentCol = module.retrieveAllTypeDepartment();

            departmentMap.put("-1",
                "---" + app.getMessage("claims.label.department") + "---");

            if (departmentCol != null) {
                for (Iterator iterator = departmentCol.iterator();
                        iterator.hasNext();) {
                    DepartmentDataObject o = (DepartmentDataObject) iterator.next();

                    departmentMap.put(o.getDeptCode(), o.getDeptDesc());
                }

                allDepartmentCol = null;
                departmentCol = null;
            }
        } catch (Exception e) {
            Log.getLog(getClass()).warn("cannot retrieve department list");
        }

        departmentBox.setOptionMap(departmentMap);

        //retrieve from database to know what department current type is on
        ClaimTypeDepartObject object = module.selectDepartmentType(getId());

        if (object == null) {
            departmentBox.setSelectedOption("-1");
        } else {
            departmentBox.setSelectedOption(object.getDepartmentid());
        }

        addChild(departmentBox);

        addChild(new Label("a", ""));

		buttonPanel = new Panel("buttonPanel");

        submitButton = new Button("submitButton", app.getMessage("claims.category.submit"));
        buttonPanel.addChild(submitButton);

		cancel = new Button("cancel", app.getMessage("claims.category.cancel"));
		buttonPanel.addChild(cancel);

		addChild(buttonPanel);
    }

    public Forward onSubmit(Event evt) {
        Application app = Application.getInstance();
        ClaimConfigTypeModule module = (ClaimConfigTypeModule) app.getModule(ClaimConfigTypeModule.class);
        super.onSubmit(evt);

        String buttonClicked = findButtonClicked(evt);

        if (buttonClicked.equals(submitButton.getAbsoluteName())) {
            ClaimTypeDepartObject object = new ClaimTypeDepartObject();

            Map selected = new SequencedHashMap();
            selected = departmentBox.getSelectedOptions();



            module.deleteTypeDepart(getId());


            for (Iterator iterator = selected.entrySet().iterator();
                        iterator.hasNext();) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    String key = (String) entry.getKey();
                  //  String value = (String) entry.getValue();

                 object.setTypeid(getId());
                 object.setDepartmentid(key);

                 module.addTypeDepart(object);
            }

            return new Forward("submit");

        } else if (buttonClicked.equals(cancel.getAbsoluteName())) {
			return new Forward (FORWARD_CANCEL);
		}

        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

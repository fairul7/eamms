package com.tms.hr.orgChart.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject;
import com.tms.hr.orgChart.model.OrgChartHandler;
import com.tms.hr.orgChart.model.OrgSetup;

public class AssociateDepartmentCountryForm extends Form {
	public static final String FORWARD_SAVED = "saved";
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_ASSOCIATION_EXIST = "association_exist";
    public static final String FORWARD_EMPTY_SELECTBOX = "empty_selectbox";
    private SelectBox countryList;
    private SelectBox deptList;
    private Button btnSave;
    private Button btnCancel;
    
    public void init() {
    	Application app = Application.getInstance();
    	setMethod("POST");
        setColumns(2);
        
    	countryList = new SelectBox("countryList");
    	countryList.setMultiple(false);
    	countryList.setOptionMap(getOptionList(OrgChartHandler.TYPE_COUNTRY));
    	//countryList.addChild(new ValidatorSelectBoxNotEmpty("countryListVNE", app.getMessage("orgChart.general.warn.empty", "Required field left empty")));
    	
    	deptList = new SelectBox("deptList");
    	deptList.setMultiple(false);
    	deptList.setOptionMap(getOptionList(OrgChartHandler.TYPE_DEPT));
    	//deptList.addChild(new ValidatorSelectBoxNotEmpty("deptListVNE", app.getMessage("orgChart.general.warn.empty", "Required field left empty")));
    	
    	btnSave = new Button("btnSave", app.getMessage("general.label.save","Save"));
        btnCancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("general.label.cancel", "Cancel"));
        
        Label lblDept = new Label("lblDept", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.department.label.department", "Department")+" *" + "</span>");
        lblDept.setAlign("right");
        addChild(lblDept);
        addChild(deptList);
        Label lblCountry = new Label("lblCountry", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.country.label.country", "Country")+" *" + "</span>");
        lblCountry.setAlign("right");
        addChild(lblCountry);
        addChild(countryList);
        
        Panel panel = new Panel("btnPanel");
        panel.setColspan(2);
        panel.setAlign(Panel.ALIGH_MIDDLE);
        panel.addChild(btnSave);
        panel.addChild(btnCancel);
        addChild(panel);
    }
    
    private Map getOptionList(String type) {
    	OrgChartHandler module = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
    	Collection setupCol = module.findAllSetup(type, null, 0, -1, "code", false, false);
    	Map setupList = new SequencedHashMap();
    	
    	if(OrgChartHandler.TYPE_COUNTRY.equals(type)) {
    		setupList.put("", Application.getInstance().getMessage("orgChart.general.label.selectCountry", "---Select Country---"));
    	}
    	else if(OrgChartHandler.TYPE_DEPT.equals(type)) {
    		setupList.put("", Application.getInstance().getMessage("orgChart.general.label.selectDept", "---Select Dept---"));
    	}
    	
    	if(setupCol != null) 
    	{
    		for(Iterator i=setupCol.iterator(); i.hasNext();) {
    			OrgSetup setup = (OrgSetup) i.next();
    			setupList.put(setup.getCode(), setup.getShortDesc());
    		}
    	}
    	
    	return setupList;
    }
    
    public Forward onSubmit(Event event) {
    	Forward forward = super.onSubmit(event);
        
        String selectedCountry = (String) countryList.getSelectedOptions().keySet().iterator().next();
        String selectedDept = (String) deptList.getSelectedOptions().keySet().iterator().next();
        
        if(!"".equals(selectedCountry) &&
        		!"".equals(selectedDept)) {
        	OrgChartHandler module = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
        	int totalMappingRecord = module.countDepartmentCountryAssociativity(null, selectedDept, selectedCountry);

       		if(totalMappingRecord > 0) {
       			this.setInvalid(true);
       			return new Forward(FORWARD_ASSOCIATION_EXIST);
       		}
        }
        else {
        	if("".equals(selectedCountry)) {
        		countryList.setInvalid(true);
        		this.setInvalid(true);
        	}
        	if("".equals(selectedDept)) {
        		deptList.setInvalid(true);
        		this.setInvalid(true);
        	}
        	return new Forward(FORWARD_EMPTY_SELECTBOX);
        }
        
        return forward;
    }
    
    public Forward onValidate(Event evt) {
    	OrgChartHandler module = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
    	String selectedCountry = (String) countryList.getSelectedOptions().keySet().iterator().next();
        String selectedDept = (String) deptList.getSelectedOptions().keySet().iterator().next();
        
        DepartmentCountryAssociativityObject obj = new DepartmentCountryAssociativityObject();
        obj.setCountryCode(selectedCountry);
        obj.setDeptCode(selectedDept);
        
        if(module.insertDepartmentCountryAssociativity(obj)) {
        	return new Forward(FORWARD_SAVED);
        }
        else {
        	return new Forward(FORWARD_ERROR);
        }
    }

	public Button getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}

	public Button getBtnSave() {
		return btnSave;
	}

	public void setBtnSave(Button btnSave) {
		this.btnSave = btnSave;
	}

	public SelectBox getCountryList() {
		return countryList;
	}

	public void setCountryList(SelectBox countryList) {
		this.countryList = countryList;
	}

	public SelectBox getDeptList() {
		return deptList;
	}

	public void setDeptList(SelectBox deptList) {
		this.deptList = deptList;
	}
}
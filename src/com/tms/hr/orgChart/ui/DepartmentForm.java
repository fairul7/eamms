package com.tms.hr.orgChart.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.Validator;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.hr.orgChart.model.OrgChartHandler;
import com.tms.hr.orgChart.model.OrgSetup;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 15, 2006
 * Time: 4:22:38 PM
 */
public class DepartmentForm extends Form {
    public static final String FORWARD_SAVED = "saved";
    public static final String FORWARD_UPDATED = "updated";
    public static final String FORWARD_ERROR = "error";
    private String code;
    private TextField txtDeptCode;
    private TextField txtShortDesc;
    private TextField txtLongDesc;
    private CheckBox chkActive;
    private SelectBox sbdeptCode;

    private Button btnSave;
    private Button btnCancel;

    public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void init(){
        Application app = Application.getInstance();
        setMethod("POST");
        setColumns(2);

        txtDeptCode = new TextField("txtDeptCode");
        txtDeptCode.setSize("25");
        txtDeptCode.addChild(new ValidateDeptCode("vdc", app.getMessage("orgChart.general.warn.codeKey")));
        txtShortDesc = new TextField("txtShortDesc");
        txtShortDesc.setSize("25");
        txtShortDesc.addChild(new ValidatorNotEmpty("shortDescVNE", app.getMessage("orgChart.general.warn.empty")));
        txtLongDesc = new TextField("txtLongDesc");
        txtLongDesc.setSize("80");
        chkActive = new CheckBox("chkActive");
        sbdeptCode = new SelectBox("sbdeptCode");       
        
        btnSave = new Button("btnSave", app.getMessage("general.label.save","Save"));
        btnCancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("general.label.cancel", "Cancel"));
        
        Label lblDeptCode = new Label("lblDeptCode", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.department.label.deptCode", "Dept Code")+" *</span>");
        lblDeptCode.setAlign("right");
        addChild(lblDeptCode);
        addChild(txtDeptCode);
        
        Label lblShortDesc = new Label("lblShortDesc", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.general.label.shortDesc", "Short Description")+" *</span>");
        lblShortDesc.setAlign("right");
        addChild(lblShortDesc);
        addChild(txtShortDesc);
        
        Label lblLongDesc = new Label("lblLongDesc", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.general.label.longDesc", "Long Description") + "</span>");
        lblLongDesc.setAlign("right");
        addChild(lblLongDesc);
        addChild(txtLongDesc);
        
        Label lblDept = new Label("lblDept", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.general.label.department", "Section") + "</span>");
        lblDept.setAlign("right");
        addChild(lblDept);
        addChild(sbdeptCode);
        
        Label lblActivate = new Label("lblActivate", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.general.label.activate", "Activate") + "</span>");
        lblActivate.setAlign("right");
        addChild(lblActivate);
        addChild(chkActive);
        
        Panel panel = new Panel("btnPanel");
        panel.setColspan(2);
        panel.setAlign(Panel.ALIGH_MIDDLE);
        panel.addChild(btnSave);
        panel.addChild(btnCancel);
        addChild(panel);
    }
    public void onRequest(Event evt) {
    	super.onRequest(evt);
    	removeChildren();
    	init();
    	User user = ((SecurityService)Application.getInstance().getService(SecurityService.class)).getCurrentUser(evt.getRequest());
        Application application = Application.getInstance();
        ContentPublisher cp = (ContentPublisher)application.getModule(ContentPublisher.class);
        String permission = false ? ContentManager.USE_CASE_VIEW : null;
       
        try {
			Collection co= cp.viewList(null, null, null, "com.tms.cms.section.Section_Sections", Boolean.FALSE, null, false, 0, -1, permission, user.getId());
			for (Iterator i = co.iterator(); i.hasNext();)
	        {
	        	ContentObject cobj = (ContentObject) i.next();
	        	sbdeptCode.setOptions(cobj.getId()+"="+cobj.getName());
	        }
		} catch (ContentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!(code == null || "".equals(code)))
        {
			OrgChartHandler oc = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
			OrgSetup dept=oc.selectDeptSetup(code);
			txtDeptCode.setValue(dept.getCode());
			txtShortDesc.setValue(dept.getShortDesc());
			txtLongDesc.setValue(dept.getLongDesc());
			chkActive.setChecked(dept.isActive());
			sbdeptCode.setSelectedOption(dept.getDeptSectionCode());
        }
    }
    
    /*public Forward onSubmit(Event evt) {
    	Forward fwd=super.onSubmit(evt);
    	OrgChartHandler oc = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
    	try{
    	if(!(code == null || "".equals(code)))
        {
    		if(!txtDeptCode.getValue().equals(code)){
    			if(oc.codeExist(OrgChartHandler.TYPE_DEPT, (String)txtDeptCode.getValue())){
            		txtDeptCode.setInvalid(true);
            		txtDeptCode.setMessage(Application.getInstance().getMessage("orgChart.general.warn.codeKey"));
            		this.setInvalid(true);
            	}
    		}
        }else{
        	if(oc.codeExist(OrgChartHandler.TYPE_DEPT, (String)txtDeptCode.getValue())){
        		txtDeptCode.setInvalid(true);
        		txtDeptCode.setMessage(Application.getInstance().getMessage("orgChart.general.warn.codeKey"));
        		this.setInvalid(true);
        	}
        }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return fwd;
    }*/
    public Forward onValidate(Event evt) {
        String action = findButtonClicked(evt);
        Forward fwd= new Forward();
        if(action.equals(btnSave.getAbsoluteName())){
            OrgChartHandler oc = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
            OrgSetup dept = new OrgSetup();
            if(!(code == null || "".equals(code)))
            {
            	dept.setCode((String) txtDeptCode.getValue());
                dept.setShortDesc((String) txtShortDesc.getValue());
                dept.setLongDesc((String) txtLongDesc.getValue());
                dept.setActive(chkActive.isChecked());
                List list = (List) sbdeptCode.getValue();
                dept.setDeptSectionCode((String)list.get(0));
                oc.updateDeptSetup(dept);
                fwd=new Forward(FORWARD_UPDATED);
            }
            else{
            dept.setCode((String) txtDeptCode.getValue());
            dept.setShortDesc((String) txtShortDesc.getValue());
            dept.setLongDesc((String) txtLongDesc.getValue());
            dept.setActive(chkActive.isChecked());
            List list = (List) sbdeptCode.getValue();
            dept.setDeptSectionCode((String)list.get(0));
            oc.saveDeptSetup(dept);
            fwd=new Forward(FORWARD_SAVED);
            }

            //reset
            txtDeptCode.setValue("");
            txtShortDesc.setValue("");
            txtLongDesc.setValue("");
            chkActive.setChecked(false);
            sbdeptCode.setSelectedOption("");
            return fwd;
        }else return new Forward(FORWARD_ERROR);

    }

    // make sure deptCode is unique
    private class ValidateDeptCode extends Validator{

        public ValidateDeptCode(String name, String text){
            super(name);
            setText(text);
        }

        public boolean validate(FormField ff) {
            String deptCode = (String) ff.getValue();
            if(deptCode == null || deptCode.equals("")){
                return false;
            }else{
                OrgChartHandler oc = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
                try {
                	if(!(code == null || "".equals(code)))
                    {
                		if(!deptCode.equals(code)){
                			return !oc.codeExist(OrgChartHandler.TYPE_DEPT, deptCode);
                		}
                    }else{
                    	return !oc.codeExist(OrgChartHandler.TYPE_DEPT, deptCode);
                    }
                    return true;
                } catch (DaoException e) {
                    return false;
                }
            }
        }
    }
    
}

package com.tms.sam.po.ui;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Panel;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.sam.po.model.DepartmentModule;
import com.tms.sam.po.model.DepartmentObject;

public class DepartmentForm extends Form {
	private String deptID = "";
	private TextField txtDptCode;
	private TextField txtDpt;
	private SingleUserSelectBox txtHOD;
	private Button submit;
	private Panel buttonPanel;
	public static final String FORWARD_SUCCESS = "success";
	public static final String FORWARD_FAILURE = "failure";
	private DepartmentObject dept = new DepartmentObject();
	public void init(){
		 setMethod("POST");
	 	 setColumns(2);
	 	 Application app = Application.getInstance();
	 	 
	 	 txtDpt = new TextField("txtDpt");
	 	 ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", "Must not be empty");
	 	 txtDpt.addChild(vne);
	 	 
	 	 txtDptCode = new TextField("txtDptCode");
	 	 ValidatorNotEmpty validDptCode = new ValidatorNotEmpty("validDptCode", "Must not be empty");
	 	 txtDptCode.addChild(validDptCode);
	 	 
	 	 txtHOD = new SingleUserSelectBox("txtHOD");
	 	 ValidatorNotEmpty txtVne = new ValidatorNotEmpty("txtVne", "Must not be empty");
	 	 txtHOD.addChild(txtVne);
	 	 
	 	 submit = new Button("submit", app.getMessage("po.label.submit", "Submit"));   
		
		 buttonPanel = new Panel("buttonPanel");
		 buttonPanel.addChild(submit);
		
	 	 addChild(txtDpt);
	 	 addChild(txtDptCode);
	 	 addChild(txtHOD);
	 	 addChild(submit);
	 	 addChild(buttonPanel);
	 	 
	 	 txtHOD.init();
	 	 //SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
	 	 //ss.getUsers(null,0,-1,)
	}
	  
	public void onRequest(Event event){
		if(!deptID.equals("") && event.getRequest().getParameter("id")!=null){
			DepartmentObject obj = null;
			Application app = Application.getInstance();
			DepartmentModule module = (DepartmentModule) app.getModule(DepartmentModule.class);
			obj = module.getDepartment(deptID);
			txtDpt.setValue(obj.getDeptName());
			
			txtHOD.setIds(new String[] {obj.getUserID()});
			txtHOD.init();
		    //txtHOD.setValue(obj);
		    //txtHOD.setValue(obj.getUserID());
		    txtDptCode.setValue(obj.getDeptCode());
		}else{
			 init();
			 deptID="";
		}
		
	}
	  
	public String getTemplate() {
		  return "po/department";
	}
	
	public Forward onValidate(Event event){
		  super.onValidate(event);
		  String button = findButtonClicked(event);
		  button = (button == null)? "" : button;
		  if(button.endsWith("reset")){
	        	init();
	      }
		  
		  boolean querySuccess = false;
		  Application app = Application.getInstance();
		  
		  DepartmentModule module = (DepartmentModule) app.getModule(DepartmentModule.class);
	    
	      dept.setDeptName((String) txtDpt.getValue());
	      dept.setUserID(txtHOD.getId());
	      dept.setDeptCode((String) txtDptCode.getValue());
	      
	      if(!deptID.equals("")){
	    	  dept.setDeptID(deptID);
	      }else{
	    	  String deptID = UuidGenerator.getInstance().getUuid();
			  dept.setDeptID(deptID);
	      }
	      querySuccess = module.addDept(dept);
	   
	      init();
	      
	      if(querySuccess) {
	            return new Forward(FORWARD_SUCCESS);
	      }
	      else {
	            return new Forward(FORWARD_FAILURE);
	      }
	      
	      
	}

	// === [ getters/setters ] =================================================
	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}

	public TextField getTxtDpt() {
		return txtDpt;
	}

	public void setTxtDpt(TextField txtDpt) {
		this.txtDpt = txtDpt;
	}

	public TextField getTxtDptCode() {
		return txtDptCode;
	}

	public void setTxtDptCode(TextField txtDptCode) {
		this.txtDptCode = txtDptCode;
	}

	public SingleUserSelectBox getTxtHOD() {
		return txtHOD;
	}

	public void setTxtHOD(SingleUserSelectBox txtHOD) {
		this.txtHOD = txtHOD;
	}

	public Panel getButtonPanel() {
		return buttonPanel;
	}

	public void setButtonPanel(Panel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}

	public String getDeptID() {
		return deptID;
	}

	public void setDeptID(String deptID) {
		this.deptID = deptID;
	}

}

package com.tms.ekms.manpowertemp.ui;

import java.util.Date;
import com.tms.ekms.manpowertemp.model.LeaveTypeObject;
import com.tms.ekms.manpowertemp.model.ManpowerModule;
import com.tms.fms.widgets.BoldLabel;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

public class LeaveTypeSetup extends Form{
	Label lbltype;
	TextField type;
	Label lbltypeName;
	TextField typeName;
	Label lbltypeDescription;
	TextBox typeDescription;
	
	Button btnsubmit;
	Button btncancel;
	
	public String action;
	public String id;
	
	private String cancelUrl="";
	
	public String getCancelUrl(){
		return cancelUrl;
	}
	
	public void setCancelUrl(String cancelUrl){
		this.cancelUrl=cancelUrl;
	}
	
	public void init() {
		Application app = Application.getInstance();
	    String msgNotEmpty  = "Mandatory Field";
		setMethod("POST");
        setColumns(2);
        
        lbltype= new BoldLabel("lbltype");
        lbltype.setAlign("right");
        lbltype.setText(app.getMessage("fms.request.label.leaveType") + " *");
        addChild(lbltype);
        type= new TextField("type");
        type.setSize("50");
        type.setMaxlength("255");
        type.addChild(new ValidatorNotEmpty("NameNotEmpty",msgNotEmpty));
        addChild(type);
        
        lbltypeName= new BoldLabel("lbltypeName");
        lbltypeName.setAlign("right");
        lbltypeName.setText(app.getMessage("fms.request.label.leaveTypeName") + " *");
        addChild(lbltypeName);
        typeName= new TextField("typeName");
        typeName.setSize("50");
        typeName.setMaxlength("255");
        typeName.addChild(new ValidatorNotEmpty("typeNameNotEmpty",msgNotEmpty));
        addChild(typeName);
        
        lbltypeDescription= new BoldLabel("lbltypeDescription");
        lbltypeDescription.setAlign("right");
        lbltypeDescription.setText(app.getMessage("fms.request.label.leaveTypeDescription"));
        addChild(lbltypeDescription);
        typeDescription= new TextBox("typeDescription");
        typeDescription.setCols("50");
        typeDescription.setRows("4");
        addChild(typeDescription);
        
        addChild(new Label("lbl","\t"));
        btnsubmit= new Button("btnsubmit","Submit");
       
        addChild(new Label("lbl","\t"));
        btncancel=new Button(Form.CANCEL_FORM_ACTION,"Cancel");
       
        Panel btnPanel =new Panel("btnpanel");
        btnPanel. addChild(btnsubmit);
        btnPanel. addChild(btncancel);
        addChild(btnPanel);
	}
	
	public void onRequest(Event evt) {
		super.onRequest(evt);
		//init();
		if(evt.getParameter("action")!=null){
			if(evt.getParameter("action").equals("edit")){
				populateData(evt.getParameter("id"));
				setAction("edit");
				setId(evt.getParameter("id"));
			}else{
				init();
				setAction("add");
				setId("");
			}
		}else{
			init();
			setAction("add");
			setId("");
		}
    }
	
	public void populateData(String id){
		ManpowerModule mod=(ManpowerModule)Application.getInstance().getModule(ManpowerModule.class);
		LeaveTypeObject object = new LeaveTypeObject();
		try{
			object = mod.getLeaveType(id);
			if (object != null && object.getId() != null) {
			    if(object.getLeaveType()!=null){
			    	type.setValue(object.getLeaveType().toString());
			    }else{
			    	type.setValue("");
			    }
			    if(object.getLeaveTypeName()!=null){
			    	typeName.setValue(object.getLeaveTypeName().toString());
			    }else{
			    	typeName.setValue("");
			    	 System.out.println(""+typeName.getValue());
			    }
			    if(object.getDescription()!=null){
			    	typeDescription.setValue(object.getDescription().toString());
			    }else{
			    	typeDescription.setValue("");
			    }
			}
			}catch(Exception e){
			Log.getLog(getClass()).error(e.toString());
		}
	}
/*	
	public Forward actionPerformed(Event event) {
		Forward forward = new Forward();
		if (btncancel.getAbsoluteName().equals(findButtonClicked(event))) {
			init();
			return new Forward(CANCEL_FORM_ACTION);
		}
		forward = super.actionPerformed(event);
		return forward;
	}*/
	
	public Forward onValidate(Event evt) {
		 	Forward forw=null;
	        ManpowerModule mod=(ManpowerModule)Application.getInstance().getModule(ManpowerModule.class);
	        	if (getAction().equals("edit")){
	                //System.out.println("testing-----edit----");
	                LeaveTypeObject leaveObject=new  LeaveTypeObject();
	                leaveObject.setUpdatedBy(getWidgetManager().getUser().getId());
	                leaveObject.setUpdatedDate(new Date());
	                leaveObject.setId(getId());
	                leaveObject.setLeaveType(type.getValue().toString());
	                leaveObject.setLeaveTypeName(typeName.getValue().toString());
	                leaveObject.setDescription(typeDescription.getValue().toString());
	                mod.updateLeaveType(leaveObject);
					forw = new Forward("edit");
	        }else {
				   // System.out.println("testing-----button add----");
					LeaveTypeObject lobj=new  LeaveTypeObject();
					lobj.setCreatedBy(getWidgetManager().getUser().getId());
					lobj.setCreatedDate(new Date());
					lobj.setId(UuidGenerator.getInstance().getUuid());
					lobj.setLeaveType(type.getValue().toString());
					lobj.setLeaveTypeName(typeName.getValue().toString());
	                System.out.println(""+lobj.getDescription());
	                lobj.setDescription(typeDescription.getValue().toString());
					mod.addLeaveType(lobj);
					forw = new Forward("save");
				}
	        	
	        return forw;   
	 }

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	 
	 /*public String getDefaultTemplate() {
 		return "secmanpower/leaveTypeTemplate";
 	   }*/

}

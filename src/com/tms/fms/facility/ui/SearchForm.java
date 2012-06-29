package com.tms.fms.facility.ui;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;

public class SearchForm extends Form{
	private TextField tfItemCode;
	private Button btnSubmit;
	
	private String itemCode="";
	
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public void init(){
		super.init();
		removeChildren();
	}
	
	public void onRequest(Event event) {
		// clear search form if not currently doing table paging
		String tablePage = (String) event.getRequest().getParameter("page");
		if (tablePage == null) {
			initForm();
		}
    }

	public void initForm() {
		setMethod("post");
	    setColumns(2);
	    
		addChild(new Label("lb1", Application.getInstance().getMessage("fms.facility.form.itemCode", "Item Code")));
	    tfItemCode = new TextField("tfItemCode");
    	tfItemCode.setSize("25");
    	tfItemCode.setMaxlength("255");
	    addChild(tfItemCode);
	    
	    addChild(new Label("lbbutton", ""));
		Panel pnButton = new Panel("pnButton");
		btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("fms.facility.search", "Search"));
    	pnButton.addChild(btnSubmit);
    	addChild(pnButton);
    	
    	itemCode = "";
	}
	
	public Forward onSubmit(Event evt) {
	    return super.onSubmit(evt);
	}
	
	public Forward onValidate(Event event) {
		itemCode=tfItemCode.getValue().toString();
		return super.onValidate(event);
	}	
	
	public String getDefaultTemplate() {
		return "fms/facility/searchForm";
	}
}

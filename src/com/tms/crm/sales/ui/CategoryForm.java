package com.tms.crm.sales.ui;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.crm.sales.misc.MyUtil;
import com.tms.crm.sales.model.CategoryModule;
import com.tms.crm.sales.model.CategoryObject;

public class CategoryForm extends Form{
	protected TextField categoryName;
	/*protected SelectBox isArchived;*/  
	protected Button submit;
	protected Button cancel;

	private Label lbCategoryName;
	/*private Label lbIsArchived;*/
	
	private String categoryID;
	
	private String type; // possible values: "View", "Add", "Edit"
	public static final String FORWARD_CANCEL = "cancel";
	
	public void init() {
		if (!MyUtil.isValidChoice(type, new String[] {"View", "Add", "Edit"})) {
			type = "Add";
			System.out.println("Error!!! Wrong type passed. CategoryForm");
		}
	}   
	
	public void initForm() {
		removeChildren();
		setMethod("POST");
    	setColumns(2);
    	
    	addChild(new Label("lb1", Application.getInstance().getMessage("sfa.label.categoryName","Category Name")+":"));
		if (type.equals("View")) {
			lbCategoryName = new Label("lbCategoryName", "");
			addChild(lbCategoryName);
		} else {
			categoryName = new TextField("categoryName");
	    	categoryName.setMaxlength("255");
	    	categoryName.setSize("40");
			ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", Application.getInstance().getMessage("sfa.label.mustnotbeempty","Must not be empty"));
			categoryName.addChild(vne);
			addChild(categoryName);
		}
    	
		/*addChild(new Label("lb2", Application.getInstance().getMessage("sfa.label.archived","Archived")+":"));
		if (type.equals("View")) {
			lbIsArchived = new Label("lbIsArchived", "");
			addChild(lbIsArchived);
		} else {
			isArchived = new SelectBox("isArchived");
			isArchived.addOption("0", Application.getInstance().getMessage("sfa.label.no","No"));
			isArchived.addOption("1", Application.getInstance().getMessage("sfa.label.yes","Yes"));
			addChild(isArchived);
		}*/
		
		
		if (!type.equals("View") && (!type.equals("Edit"))) {
			submit = new Button("submit", Application.getInstance().getMessage("sfa.label.submit","Submit"));
			addChild(submit);
		} else if (type.equals("Edit")) {
			submit = new Button("submit", Application.getInstance().getMessage("sfa.label.update","Update"));
			addChild(submit);
			cancel = new Button("cancel", Application.getInstance().getMessage("sfa.label.cancel","Cancel"));
			addChild(cancel);
		}
		
		
	}
	
	public void onRequest(Event evt) {
		initForm();
		
		if (type.equals("View")) {
			populateView();
		} else if (type.equals("Edit")) {
			populateEdit();
		}
	}
	
	public Forward actionPerformed(Event event) {
		Forward forward;
        if(cancel.getAbsoluteName().equals(findButtonClicked(event)))
            forward = new Forward(FORWARD_CANCEL);
        else
            forward = super.actionPerformed(event);
        return forward;
	}
	
	public Forward onValidate(Event evt) {
		Forward myForward = null;

		if (type.equals("Edit")) {
			if (cancel.getAbsoluteName().equals(findButtonClicked(evt))) {
				myForward= new Forward(FORWARD_CANCEL);
			}
		}
		if (type.equals("Add")) {
			myForward = addCategory();
		} else if (type.equals("Edit") && !cancel.getAbsoluteName().equals(findButtonClicked(evt))) {
			myForward = editCategory();
		}
		initForm();
		return myForward;
	}
	
	private Forward addCategory() {
		Application application = Application.getInstance();
		CategoryModule module    = (CategoryModule) application.getModule(CategoryModule.class);
		
		CategoryObject obj = new CategoryObject();
		UuidGenerator uuid = UuidGenerator.getInstance();
		categoryID = uuid.getUuid();
		obj.setCategoryID(categoryID);
		obj.setCategoryName((String) categoryName.getValue());
		/*obj.setIsArchived(MyUtil.getSingleValue_SelectBox(isArchived));*/
		
		if (!module.isUnique(obj)) {
			return new Forward("categoryDuplicate");
		}
		
		module.addCategory(obj); 
		
		return new Forward("categoryAdded");
	}
	
	private Forward editCategory() {
		Application application = Application.getInstance();
		CategoryModule module    = (CategoryModule) application.getModule(CategoryModule.class);
		
		CategoryObject obj = module.getCategory(categoryID);
		obj.setCategoryName((String) categoryName.getValue());
		/*obj.setIsArchived(MyUtil.getSingleValue_SelectBox(isArchived));*/
		
		if (!module.isUnique(obj)) {
			return new Forward("categoryDuplicate");
		}
		
		module.updateCategory(obj);
		
		return new Forward("categoryUpdated");
	}
	
	public void populateView() {
		Application application = Application.getInstance();
		CategoryModule module    = (CategoryModule) application.getModule(CategoryModule.class);
		CategoryObject obj      = module.getCategory(categoryID);
		
		lbCategoryName.setText(obj.getCategoryName());
		/*lbIsArchived.setText((String) DisplayConstants.getYesNoMap().get(obj.getIsArchived()));*/
	}
	
	public void populateEdit() {
		Application application = Application.getInstance();
		CategoryModule module    = (CategoryModule) application.getModule(CategoryModule.class);
		CategoryObject obj      = module.getCategory(categoryID);
		
		categoryName.setValue(String.valueOf(obj.getCategoryName()));
		/*isArchived.setSelectedOptions(new String[] { obj.getIsArchived() });*/
	}
	
	public String getDefaultTemplate() {
		return "sfa/categoryForm";
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

	public TextField getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(TextField categoryName) {
		this.categoryName = categoryName;
	}

	/*public SelectBox getIsArchived() {
		return isArchived;
	}

	public void setIsArchived(SelectBox isArchived) {
		this.isArchived = isArchived;
	}*/

	public Label getLbCategoryName() {
		return lbCategoryName;
	}

	public void setLbCategoryName(Label lbCategoryName) {
		this.lbCategoryName = lbCategoryName;
	}

	/*public Label getLbIsArchived() {
		return lbIsArchived;
	}

	public void setLbIsArchived(Label lbIsArchived) {
		this.lbIsArchived = lbIsArchived;
	}*/

	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}

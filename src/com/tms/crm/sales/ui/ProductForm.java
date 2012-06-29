/*
 * Created on Feb 26, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.crm.sales.misc.MyUtil;
import com.tms.crm.sales.model.CategoryModule;
import com.tms.crm.sales.model.CategoryObject;
import com.tms.crm.sales.model.Product;
import com.tms.crm.sales.model.ProductModule;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ProductForm extends Form {
	protected SelectBox category;
	protected TextField tf_ProductName;
	protected SelectBox sel_IsArchived;
	protected Button submit;
	protected Button cancel;

	private Label lbProductName;
	private Label lbIsArchived;
	private Label lbCategory;
	private String productID;
	
	private String type; // possible values: "View", "Add", "Edit"
	public static final String FORWARD_CANCEL = "cancel";


	/* Step 1: Initialization */
	public void init() {
		if (!MyUtil.isValidChoice(type, new String[] {"View", "Add", "Edit"})) {
			type = "Add";
			System.out.println("Error!!! Wrong type passed. ProductForm");
		}
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String string) {
		type = string;
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public String getProductID() {
		return productID;
	}
	
	public void setProductID(String string) {
		productID = string;
	}
	
	
	/* Step 3: Form display and processing */
	public void initForm() {
		removeChildren();
		setColumns(2);
		setMethod("POST");
		CategoryModule module = (CategoryModule) Application.getInstance().getModule(CategoryModule.class);
		addChild(new Label("lb1", Application.getInstance().getMessage("sfa.label.productName","Product Name")+":"));
		if (type.equals("View")) {
			lbProductName = new Label("lbProductName", "");
			addChild(lbProductName);
		} else {
			tf_ProductName = new TextField("tf_ProductName");
			tf_ProductName.setMaxlength("255");
			tf_ProductName.setSize("40");
			ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", Application.getInstance().getMessage("sfa.label.mustnotbeempty","Must not be empty"));
			tf_ProductName.addChild(vne);
			addChild(tf_ProductName);
		}
		
		addChild(new Label("lb2", Application.getInstance().getMessage("sfa.label.archived","Archived")+":"));
		if (type.equals("View")) {
			lbIsArchived = new Label("lbIsArchived", "");
			addChild(lbIsArchived);
			
			lbCategory = new Label("lbCategory", "");
			addChild(lbCategory);
		} else {
			sel_IsArchived = new SelectBox("sel_IsArchived");
			sel_IsArchived.addOption("0", Application.getInstance().getMessage("sfa.label.no","No"));
			sel_IsArchived.addOption("1", Application.getInstance().getMessage("sfa.label.yes","Yes"));
			addChild(sel_IsArchived);
			
			category = new SelectBox("category");
			category.addOption("", Application.getInstance().getMessage("sfa.label.pleaseSelect","--Please Select--"));
            Collection cat = module.getCategory();
            for( Iterator i=cat.iterator(); i.hasNext(); ){
            	CategoryObject o = (CategoryObject) i.next();
            	category.addOption(o.getCategoryID(), o.getCategoryName());
            }
            
            addChild(category);
		}
		
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
			myForward = addProduct();
		} else if (type.equals("Edit")) {
			myForward = editProduct();
		}
		initForm();
		return myForward;
	}
	
	public Forward onSubmit(Event event) {
    	 Forward forward = super.onSubmit(event);
    	
		 String button = findButtonClicked(event);
		 button = (button == null)? "" : button;
		 
		 if (button.endsWith("submit")) {
			 String locationSelectedOption = (String) category.getSelectedOptions().keySet().iterator().next();
		     if("".equals(locationSelectedOption)) {
		    	 category.setInvalid(true);
		         this.setInvalid(true);
		     } 
		 }
		 
		 return forward;
		 
	 }
	private Forward addProduct() {
		Application application = Application.getInstance();
		ProductModule module    = (ProductModule) application.getModule(ProductModule.class);
		
		Product pro = new Product();
		UuidGenerator uuid = UuidGenerator.getInstance();
		productID = uuid.getUuid();
		pro.setCategory(category.getSelectedOptions().keySet().iterator().next().toString());
		pro.setProductID(productID);
		pro.setProductName((String) tf_ProductName.getValue());
		pro.setIsArchived(MyUtil.getSingleValue_SelectBox(sel_IsArchived));
		
		if (!module.isUnique(pro)) {
			return new Forward("productDuplicate");
		}
		
		module.addProduct(pro);
		
		return new Forward("productAdded");
	}
	
	private Forward editProduct() {
		Application application = Application.getInstance();
		ProductModule module    = (ProductModule) application.getModule(ProductModule.class);
		
		Product pro = module.getProduct(productID);
		pro.setCategory(category.getSelectedOptions().keySet().iterator().next().toString());
		pro.setProductName((String) tf_ProductName.getValue());
		pro.setIsArchived(MyUtil.getSingleValue_SelectBox(sel_IsArchived));
		
		if (!module.isUnique(pro)) {
			return new Forward("productDuplicate");
		}
		
		module.updateProduct(pro);
		
		return new Forward("productUpdated");
	}
	
	public void populateView() {
		Application application = Application.getInstance();
		ProductModule module    = (ProductModule) application.getModule(ProductModule.class);
		Product pro             = module.getProduct(productID);
		
		lbProductName.setText(pro.getProductName());
		lbIsArchived.setText((String) DisplayConstants.getYesNoMap().get(pro.getIsArchived()));
		lbCategory.setTemplate(pro.getCategory());
	}
	
	public void populateEdit() {
		Application application = Application.getInstance();
		ProductModule module    = (ProductModule) application.getModule(ProductModule.class);
		Product pro             = module.getProduct(productID);
		
		category.setSelectedOptions(new String[] { pro.getCategory() });
		tf_ProductName.setValue(String.valueOf(pro.getProductName()));
		sel_IsArchived.setSelectedOptions(new String[] { pro.getIsArchived() });
	}
	
	public String getDefaultTemplate() {
		return "sfa/Product_Form";
	}
}

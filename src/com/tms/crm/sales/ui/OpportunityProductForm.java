/*
 * Created on Jan 19, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import java.util.*;

import kacang.stdui.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import com.tms.crm.sales.misc.*;
import com.tms.crm.sales.model.*;
import com.tms.crm.sales.ui.ValidatorIsFloat;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class OpportunityProductForm extends Form {
	public static final String FORWARD_CANCEL ="cancel";
	public static final String FORWARD_CLOSED = "closed";
	 
    protected SelectBox sel_ProductID;
	protected TextField tf_OpValue;
	protected TextField tf_OpDesc;
	protected Button submit;
	protected Button cancel;


	private Label lbProductID;
	private Label lbOpValue;
	private Label lbOpDesc;
	
	private String opportunityID;
	private int    productSeq;
	
	private String type; // possible values: "View", "Add", "Edit"
	
	protected SelectBox category;
	private Label lbCategory;
	private String categorySelected; 
	private OpportunityProduct op;
	private String state="";
	
	/* Step 1: Initialization */
	public void init() {
		if (!MyUtil.isValidChoice(type, new String[] {"View", "Add", "Edit"})) {
			type = "Add";
			System.out.println("Error!!! Wrong type passed. OpportunityProductForm");
		}
		
		initForm();
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String string) {
		type = string;
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public String getOpportunityID() {				// for: ALL
		return opportunityID;
	}
	
	public void setOpportunityID(String string) {	// for: ALL
		opportunityID = string;
	}
	
	public String getProductSeq() {					// for: EDIT
		return String.valueOf(productSeq);
	}
	
	public void setProductSeq(String string) {		// for: EDIT
		productSeq = Integer.parseInt(string);
	}
	
	
	/* Step 3: Form display and processing */
	public void initForm() {
		removeChildren();
		setColumns(2);
		setMethod("POST");
		op = new OpportunityProduct();
		addChild(new Label("lb1", Application.getInstance().getMessage("sfa.label.product","Product")+":"));
		if (type.equals("View")) {
			lbProductID = new Label("lbProductID", "");
			addChild(lbProductID);
			
			lbCategory = new Label("lbCategory","");
			addChild(lbCategory);
			
		} else {
			CategoryModule module = (CategoryModule) Application.getInstance().getModule(CategoryModule.class);
			category = new SelectBox("category");
			category.addOption("", Application.getInstance().getMessage("sfa.label.pleaseSelect","--Please Select--"));
            Collection cat = module.getCategory();
            for( Iterator i=cat.iterator(); i.hasNext(); ){
            	CategoryObject o = (CategoryObject) i.next();
            	category.addOption(o.getCategoryID(), o.getCategoryName());
            }
            category.setOnChange("submit()");
            addChild(category);
		  
			sel_ProductID = new SelectBox("sel_ProductID");
			sel_ProductID.addOption("", Application.getInstance().getMessage("sfa.label.pleaseSelect","--Please Select--"));
			addChild(sel_ProductID);
		}
		
		addChild(new Label("lb2", Application.getInstance().getMessage("sfa.label.value","Value")+": *"));
		if (type.equals("View")) {
			lbOpValue = new Label("lbOpValue", "");
			addChild(lbOpValue);
		} else {
			tf_OpValue = new TextField("tf_OpValue");
			tf_OpValue.setMaxlength("20");
			tf_OpValue.setSize("20");
			ValidatorIsFloat vInt = new ValidatorIsFloat("vInt", Application.getInstance().getMessage("sfa.label.mustbeaninteger","Must be an integer"));
			tf_OpValue.addChild(vInt);
			addChild(tf_OpValue);
		}
		
		addChild(new Label("lb3", Application.getInstance().getMessage("sfa.label.description","Description")+":"));
		if (type.equals("View")) {
			lbOpDesc = new Label("lbOpDesc", "");
			addChild(lbOpDesc);
		} else {
			tf_OpDesc = new TextField("tf_OpDesc");
			tf_OpDesc.setMaxlength("255");
			addChild(tf_OpDesc);
		}
		
		if (!type.equals("View") && (!type.equals("Edit"))) {
			submit = new Button("submit", Application.getInstance().getMessage("sfa.label.submit","Submit"));
		} else {
			submit = new Button("submit", Application.getInstance().getMessage("sfa.label.update","Update"));
;		}
		addChild(submit);
		cancel = new Button("cancel",Application.getInstance().getMessage("sfa.label.cancel","Cancel"));
		addChild(cancel);
	}
	
public void populateCategory(String selected){
		
		CategoryModule module = (CategoryModule) Application.getInstance().getModule(CategoryModule.class);
		 Collection cat = module.getCategory();
         for( Iterator i=cat.iterator(); i.hasNext(); ){
         	CategoryObject o = (CategoryObject) i.next();
         	category.addOption(o.getCategoryName(), o.getCategoryName());
         }
         category.setOnChange("submit()");
        
         if(!"".equals(selected)){
         	category.setSelectedOptions(new String[] {selected});
         }
	}
	public void populateProduct(String categorySelected){
		
		 ProductModule pro    = (ProductModule) Application.getInstance().getModule(ProductModule.class);
		 Collection listing = pro.getProducts(categorySelected);
		 for( Iterator i=listing.iterator(); i.hasNext(); ){
		     Product o = (Product) i.next();
		     sel_ProductID.addOption(o.getProductID(), o.getProductName());
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
	
	public Forward onValidate(Event evt) {
		Forward myForward = null;
        if(submit.getAbsoluteName().equals(findButtonClicked(evt))){
            if (type.equals("Add")) {
                myForward = addOpportunityProduct();
            } else if (type.equals("Edit")) {
                myForward = editOpportunityProduct();
            }
        }/*else if(cancel.getAbsoluteName().equals(findButtonClicked(evt))){
            if(type.equals("Edit")){
                type="Add";
            }
            myForward = new Forward(FORWARD_CANCEL);
        }*/
		initForm();
		return myForward;
	}


    public Forward onSubmit(Event event) {
        Forward myForward;
        if(cancel.getAbsoluteName().equals(findButtonClicked(event))){
        	if (state.equals("closed")) {
	       		 state="";
	       		 return new Forward(FORWARD_CLOSED);
	       	 }else{
	       		 if(type.equals("Edit")){
	                 type="Add";
	             }
	             myForward = new Forward(FORWARD_CANCEL);
	             initForm();
	             return myForward;
	       	 }
           
        }
        /*
        String button = findButtonClicked(event);
		 button = (button == null)? "" : button;
		 OpportunityProduct op = new OpportunityProduct();
			
		 if (button.endsWith("submit")) {
			 String locationSelectedOption = (String) category.getSelectedOptions().keySet().iterator().next();
		     if("".equals(locationSelectedOption)) {
		    	 category.setInvalid(true);
		         this.setInvalid(true);
		     } 
		     
			 String product = (String) sel_ProductID.getSelectedOptions().keySet().iterator().next();
		     if("".equals(product)) {
		    	 sel_ProductID.setInvalid(true);
		         this.setInvalid(true);
		     } 
		 }
	 
        return super.onSubmit(event); */   //To change body of overridden methods use File | Settings | File Templates.
    	
    	 Forward forward = super.onSubmit(event);
     	
		 String button = findButtonClicked(event);
		 button = (button == null)? "" : button;
		 
		 if (button.endsWith("submit")) {
			 String selectCategory = (String) category.getSelectedOptions().keySet().iterator().next();
		     if("".equals(selectCategory)) {
		    	 category.setInvalid(true);
		         this.setInvalid(true);
		     } 
		     
		     String selectProduct = (String) sel_ProductID.getSelectedOptions().keySet().iterator().next();
		     if("".equals(selectProduct)) {
		    	 sel_ProductID.setInvalid(true);
		         this.setInvalid(true);
		     } 
		 }
		 
		 if (!submit.getAbsoluteName().equals(button)) {
			 categorySelected = (String) category.getSelectedOptions().keySet().iterator().next();
			 op.setCategory(categorySelected);
		    	if (categorySelected!=null || !categorySelected.equals(""))
		            {
		            	initForm();
		            	category.setSelectedOptions(new String[] {categorySelected});
		            	populateProduct(categorySelected);
		            	op.setCategory(categorySelected);
		            }
		            setInvalid(true);
		        }
		 
		 return forward;
    }

	private Forward addOpportunityProduct() {
		Application application         = Application.getInstance();
		OpportunityProductModule module = (OpportunityProductModule) application.getModule(OpportunityProductModule.class);
		String userId                   = getWidgetManager().getUser().getId();
		
		
		op.setOpportunityID(opportunityID);
		op.setProductID(MyUtil.getSingleValue_SelectBox(sel_ProductID));
		op.setOpValue(Double.parseDouble((String) tf_OpValue.getValue()));
		op.setOpDesc((String) tf_OpDesc.getValue());
		op.setModifiedDate(DateUtil.getToday());
		op.setModifiedBy(userId);
		
		module.addOpportunityProduct(op);

        OpportunityModule opModule = (OpportunityModule) application.getModule(OpportunityModule.class);
        Opportunity opp = opModule.getOpportunity(opportunityID);
        OpportunityContactModule ocm = (OpportunityContactModule) Application.getInstance().getModule(OpportunityContactModule.class);

        // set status to open if conditions are right

        if (opp.getOpportunityStatus().equals(Opportunity.STATUS_INCOMPLETE)) {
            boolean hasPartner = (opp.getHasPartner().equals("1") ? true : false);
            boolean completed = true;
            if(opp.getCompanyID()==null || opp.getCompanyID().trim().length()==0||ocm.countOpportunityContacts(opportunityID,OpportunityContact.COMPANY_CONTACT)==0)
                completed = false;
            else if(hasPartner && ocm.countOpportunityContacts(opportunityID,OpportunityContact.PARTNER_CONTACT)==0){
                completed = false;
            }
            if(completed){
                opp.setOpportunityStatus(Opportunity.STATUS_OPEN);
                opModule.updateOpportunity(opp);
            }
        }

		return new Forward("opportunityProductAdded");
	}
	
	private Forward editOpportunityProduct() {
		Application application         = Application.getInstance();
		OpportunityProductModule module = (OpportunityProductModule) application.getModule(OpportunityProductModule.class);
		String userId                   = getWidgetManager().getUser().getId();
		
		OpportunityProduct op = module.getOpportunityProduct(opportunityID, productSeq);
		op.setProductID(MyUtil.getSingleValue_SelectBox(sel_ProductID));
		op.setOpValue(Double.parseDouble((String) tf_OpValue.getValue()));
		op.setOpDesc((String) tf_OpDesc.getValue());
		op.setModifiedDate(DateUtil.getToday());
		op.setModifiedBy(userId);
		op.setCategory(MyUtil.getSingleValue_SelectBox(category));
		module.updateOpportunityProduct(op);
		
		return new Forward("opportunityProductUpdated");
	}
	
	public void populateView() {
		Application application         = Application.getInstance();
		OpportunityProductModule module = (OpportunityProductModule) application.getModule(OpportunityProductModule.class);
		ProductModule proModule         = (ProductModule) application.getModule(ProductModule.class);
		OpportunityProduct op           = module.getOpportunityProduct(opportunityID, productSeq);
		
		lbProductID.setText((String) proModule.getProductMap(true).get(op.getProductID()));
		lbCategory.setText(op.getCategoryName());
		lbOpValue.setText(String.valueOf(op.getOpValue()));
		lbOpDesc.setText(op.getOpDesc());
	}
	
	public void populateEdit() {
		Application application         = Application.getInstance();
		OpportunityProductModule module = (OpportunityProductModule) application.getModule(OpportunityProductModule.class);
		OpportunityProduct op           = module.getOpportunityProduct(opportunityID, productSeq);
		category.setSelectedOptions(new String[] {op.getCategory()});
       
		populateProduct(op.getCategory());
		
		sel_ProductID.setSelectedOptions(new String[] { op.getProductID() });
		tf_OpValue.setValue(String.valueOf(op.getOpValue()));
		tf_OpDesc.setValue(op.getOpDesc());
	}
	
	public String getDefaultTemplate() {
		return "sfa/OpportunityProduct_Form";
	}

    public Button getCancel() {
        return cancel;
    }

    public void setCancel(Button cancel) {
        this.cancel = cancel;
    }

	public SelectBox getCategory() {
		return category;
	}

	public void setCategory(SelectBox category) {
		this.category = category;
	}

	public String getCategorySelected() {
		return categorySelected;
	}

	public void setCategorySelected(String categorySelected) {
		this.categorySelected = categorySelected;
	}

	public Label getLbCategory() {
		return lbCategory;
	}

	public void setLbCategory(Label lbCategory) {
		this.lbCategory = lbCategory;
	}

	public Label getLbOpDesc() {
		return lbOpDesc;
	}

	public void setLbOpDesc(Label lbOpDesc) {
		this.lbOpDesc = lbOpDesc;
	}

	public Label getLbOpValue() {
		return lbOpValue;
	}

	public void setLbOpValue(Label lbOpValue) {
		this.lbOpValue = lbOpValue;
	}

	public Label getLbProductID() {
		return lbProductID;
	}

	public void setLbProductID(Label lbProductID) {
		this.lbProductID = lbProductID;
	}

	public OpportunityProduct getOp() {
		return op;
	}

	public void setOp(OpportunityProduct op) {
		this.op = op;
	}

	public SelectBox getSel_ProductID() {
		return sel_ProductID;
	}

	public void setSel_ProductID(SelectBox sel_ProductID) {
		this.sel_ProductID = sel_ProductID;
	}

	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}

	public TextField getTf_OpDesc() {
		return tf_OpDesc;
	}

	public void setTf_OpDesc(TextField tf_OpDesc) {
		this.tf_OpDesc = tf_OpDesc;
	}

	public TextField getTf_OpValue() {
		return tf_OpValue;
	}

	public void setTf_OpValue(TextField tf_OpValue) {
		this.tf_OpValue = tf_OpValue;
	}

	public void setProductSeq(int productSeq) {
		this.productSeq = productSeq;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
    
}

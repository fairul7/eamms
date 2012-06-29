package com.tms.assetmanagement.ui;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorEmail;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.assetmanagement.model.AssetModule;
import com.tms.assetmanagement.model.DataItems;
import com.tms.assetmanagement.ui.ValidatorIsInteger;
import com.tms.assetmanagement.ui.ValidatorIsNumeric;
import com.tms.assetmanagement.ui.ValidatorTwoDecimal;

public class AssetItemForm  extends Form {
	
	protected SelectBox selbxCategory;
	protected TextField txtfdName;
	protected TextField txtfdQty;
	protected TextField txtfdUnitPrice;
	protected TextField txtfdNotification;
	protected TextBox txtbxDescription;
	protected DatePopupField datePurchased;
	protected Panel panelBtn;
	protected Button btnCost;
	protected Button btnSubmit;
	protected Button btnReset;
	protected Button btnCancel;
	protected Button btnUpdate;
	protected Label lblCost;
	protected Radio radioNotifyNo;
	protected Radio radioNotifyYes;
	
	private String strItemid = "";
	private String isEditMode = "false";
	private String strHideNotificationField;
	private String[] zeroDepreciationCategory;
	  
	public String getDefaultTemplate(){		
		return "assetmanagement/tempAssetItemForm";		
	}
	
	public void init(){
			
		super.init();
		setMethod("post");
		
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		
		//initialize array size
		int iSizeArray;
		try{
			iSizeArray = mod.getZeroDepRate().size();
		}catch (Exception e){
			iSizeArray = 0;
		}
		zeroDepreciationCategory = new String[iSizeArray];
		
		lblCost = new Label("lblCost");
		lblCost.setText("0");
		addChild(lblCost);		
			
		//Category lists
		selbxCategory = new SelectBox("selbxCategory");
		Map mapCategory = new SequencedHashMap();        
        Collection listCategory = mod.listCategoryType();
		
        mapCategory.put("-1", "-- " + Application.getInstance().getMessage("asset.label.selectCategory") + " --");
        if (listCategory != null && listCategory.size() >0 ){
        	int a=0;
        	for (Iterator iterator = listCategory.iterator(); iterator.hasNext();) {		     
        		Map tempMap = (Map)iterator.next();
        		
        		if(tempMap.get("categoryName")!= null)
        			mapCategory.put(tempMap.get("categoryId"), tempMap.get("categoryName"));	
        		if (tempMap.get("depreciation")!= null){
        			if(Float.parseFloat(tempMap.get("depreciation").toString()) == 0){
        				zeroDepreciationCategory[a] = tempMap.get("categoryId").toString();        			
        				a++;}       	
        			
        		}        		
        	}        	
        }
		 selbxCategory.setOptionMap(mapCategory);
		 addChild(selbxCategory);
		 
		 txtfdName = new TextField("txtfdName");
		 txtfdName.addChild(new ValidatorNotEmpty("vEmpty",Application.getInstance().getMessage("asset.message.vNotEmpty")));
		 txtfdName.setSize("50");
		 txtfdName.setMaxlength("99");
		 addChild(txtfdName);
		 		 
		 txtfdQty = new TextField("txtfdQty");
		 txtfdQty.addChild(new ValidatorNotEmpty("vEmpty",Application.getInstance().getMessage("asset.message.vNotEmpty")));
		 txtfdQty.addChild(new ValidatorIsInteger("vInteger"));
		 txtfdQty.setSize("10");
		 txtfdQty.setAlign("Left");
		 txtfdQty.setMaxlength("10");
		 txtfdQty.setOnChange("javascript:getCalculateTotalCost()");
		 addChild(txtfdQty);
		 
		 txtfdUnitPrice = new TextField("txtfdUnitPrice");
		 txtfdUnitPrice.addChild(new ValidatorIsNumeric("vNumeric"));
		 txtfdUnitPrice.addChild(new ValidatorNotEmpty("vEmpty",Application.getInstance().getMessage("asset.message.vNotEmpty")));
		 txtfdUnitPrice.addChild(new ValidatorTwoDecimal("vTwoDecimal"));
		 txtfdUnitPrice.setSize("20");
		 txtfdUnitPrice.setMaxlength("20");
		 txtfdUnitPrice.setOnChange("javascript:getCalculateTotalCost()");
		 addChild(txtfdUnitPrice);
	 
		 txtfdNotification = new TextField("txtfdNotification");
		 txtfdNotification.setValue("0");
		 txtfdNotification.setSize("10");
		 addChild(txtfdNotification);
		 
		 txtbxDescription = new TextBox("txtbxDescription");
		 addChild(txtbxDescription);
		 
		 datePurchased = new DatePopupField("datePurchased");
		 datePurchased.setDate(new java.util.Date());
		 addChild(datePurchased);
		 
		 radioNotifyNo = new Radio("radioNotifyNo");
		 radioNotifyNo.setGroupName("notification");
		 radioNotifyNo.setChecked(true);
		 addChild(radioNotifyNo);
		 
		 radioNotifyYes = new Radio("radioNotifyYes");
		 radioNotifyYes.setGroupName("notification");
		 addChild(radioNotifyYes);
		 
		 btnUpdate =  new Button ("btnUpdate",  Application.getInstance().getMessage("asset.label.btnUpdate", "Update"));
		 btnReset = new Button ("btnReset",  Application.getInstance().getMessage("asset.label.btnReset", "Reset"));
		 btnCancel = new Button (Form.CANCEL_FORM_ACTION,  Application.getInstance().getMessage("asset.label.btnCancel", "Cancel"));
		 btnSubmit =  new Button ("btnSubmit",  Application.getInstance().getMessage("asset.label.btnSubmit", "Submit"));		
		 btnCost = new Button ("btnCost", Application.getInstance().getMessage("asset.label.btnCost", "Calculate Total Cost"));
		 addChild(btnCost);									
			 
		 panelBtn = new Panel("panelBtn");	
	}

 	public void onRequest(Event event){	
		super.onRequest(event);	
		
		lblCost.setText("0");		
	
		if ("true".equals(isEditMode)) {//Edit
			removeChildren();
			init();
			 panelBtn.addChild(btnUpdate);
			 panelBtn.addChild(btnReset);
			 panelBtn.addChild(btnCancel);
			 strHideNotificationField = "false";
			 
		} else { //Add 
			init();
			 panelBtn.addChild(btnSubmit);			
			 panelBtn.addChild(btnCancel);
			 strHideNotificationField = "true";
		}
		addChild(panelBtn);
		
		getAssetItemObj();		
	}
	
	public Forward onSubmit(Event event){
		
		Forward result= super.onSubmit(event);			
		    
		if(btnCancel.getAbsoluteName().equals(findButtonClicked(event)))
	      	return new Forward(Form.CANCEL_FORM_ACTION);		 
		
		return result;
	}	
	
	public Forward onValidate(Event event){
		
		super.onValidate(event);
		 
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		 
		//reset data
		if(btnReset.getAbsoluteName().equals(findButtonClicked(event)))
			getAssetItemObj();   	     
		
		 
		String strKey = (String)((List) selbxCategory.getValue()).get(0);   
		if ("-1".equals(strKey))  			 
		{
			selbxCategory.setInvalid(true);		
			return new Forward("UnselectCategory");
 		}
 		 
		 //insert data
		if (btnSubmit.getAbsoluteName().equals(findButtonClicked(event))){     	
			DataItems objdataItems = setAssetItemObj("strAddMode");
     	  	 		 
			//Checking for Monthly Depreciation Charges
     		Map mapCategory = mod.retrieveCategory(objdataItems.getCategoryId());
     		if (mapCategory != null){
     			float fDepreciation = Float.parseFloat(mapCategory.get("depreciation").toString());
				double dMonthlyDep =(objdataItems.getItemCost() * fDepreciation /100)/12;
				
				if(dMonthlyDep > 0){
					if(dMonthlyDep - objdataItems.getItemQty() > 0){
						 //Checking for duplicate items
			     		 if (mod.isUniqueItems(objdataItems)){
			     			 mod.insertItems(objdataItems );			     
			     		 }
			     		 else 
			     			 return new Forward("Duplicated");
			     	}
					else
						return new Forward("ErrorInGenerateMonthlyDep");
				}
				else if (fDepreciation == 0){//zero depreciation rate
				 //one unit is allowed for zero-depreciation rate
					 if (objdataItems.getItemQty() != 1){
						 txtfdQty.setInvalid(true);
						 return new Forward("errorUnit");					
					 }
					 //Checking for duplicate items
		     		 if (mod.isUniqueItems(objdataItems)){
		     			 mod.insertItems(objdataItems );		     	
		     		 }
		     		 else 
		     			 return new Forward("Duplicated");
				}
     		}  	   
     		     		 
       	  if (radioNotifyYes.isChecked()){
       		  return new Forward("setupNotification");
       	  }
     	 resetDefaultValue();
     		 
     		return new Forward("Insert"); 
     	  }
     	 
     	  
     	// update data
     	  if(btnUpdate.getAbsoluteName().equals(findButtonClicked(event))){
     		  
     		 DataItems objdataItems = setAssetItemObj("strEditMode");  
     		 
     	 	//Checking for Monthly Depreciation Charges
       		Map mapCategory = mod.retrieveCategory(objdataItems.getCategoryId());
       		if (mapCategory != null){
       			float fDepreciation = Float.parseFloat(mapCategory.get("depreciation").toString());
  				double dMonthlyDep =(objdataItems.getItemCost() * fDepreciation /100)/12;
  				// ensure dMonthlyDep must has sufficient amount to generate monthly Depreciation charges
  				if(dMonthlyDep > 0){//zero depreciation rate
  					if(dMonthlyDep - objdataItems.getItemQty() > 0){
  						mod.updateItems(objdataItems);  			     	
  			     	}
  					else
  						return new Forward("ErrorInGenerateMonthlyDep");
  				}
  				else if (fDepreciation == 0){//zero depreciation rate  				
  					 //one unit is allowed for zero-depreciation rate
					 if (objdataItems.getItemQty() != 1){
						 txtfdQty.setInvalid(true);
						 return new Forward("errorUnit");					
					 }	 
  					mod.updateItems(objdataItems);  		     	
  				}
       		}  
   			return new Forward("Update");
     	  }     	
      	 
     	 //Cancel data
     	 if(btnCancel.getAbsoluteName().equals(findButtonClicked(event)))
      		return new Forward(Form.CANCEL_FORM_ACTION);
      	 
		 return null;
	}
	
	
	public DataItems setAssetItemObj(String mode){
			
		DataItems objItem = new DataItems();	
		 if (mode.equals("strAddMode")){
			
			 UuidGenerator uuid = UuidGenerator.getInstance();
		 	 objItem.setItemId((String)uuid.getUuid());
		 }
		 else  if(mode.equals("strEditMode"))
			objItem.setItemId(getStrItemid()); 	
		 
	     objItem.setCategoryId((String)selbxCategory.getSelectedOptions().keySet().iterator().next());
	     objItem.setItemName((String)txtfdName.getValue());
		 objItem.setDatePurchased((java.util.Date)datePurchased.getDate());
		 objItem.setItemQty(Float.parseFloat((String)txtfdQty.getValue()));
		 double dUnitCost = get2DecimalPoints(Double.parseDouble((String)txtfdUnitPrice.getValue()));
		 objItem.setItemUnitPrice(dUnitCost);
		 double dTotalcost= Double.parseDouble((String)txtfdQty.getValue())* dUnitCost;
		 objItem.setItemCost(dTotalcost);
		 objItem.setItemDescription((String)txtbxDescription.getValue());
		 
 		 return objItem;
	}
	
	public void getAssetItemObj(){
		
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		 
		if (getStrItemid().toString() != null && !"".equals(getStrItemid().toString())){

			 Map mapRow;		
			 mapRow = (Map) mod.retrieveItems(getStrItemid());

			if (mapRow != null){
				  selbxCategory.setSelectedOption((String)mapRow.get("categoryId"));
				  txtfdName.setValue(mapRow.get("itemName"));
				  txtfdQty.setValue(new DecimalFormat("0").format(mapRow.get("itemQty")));
				  txtfdUnitPrice.setValue(new DecimalFormat("0.00").format(mapRow.get("itemUnitPrice")));				 
				  lblCost.setText(new DecimalFormat("0.00").format(mapRow.get("itemCost")));
				  txtfdNotification.setValue("0");
				  txtbxDescription.setValue(mapRow.get("itemDescription"));
				  datePurchased.setValue((java.util.Date)mapRow.get("datePurchased"));			
			}
		}		 
	}
	
	public void resetDefaultValue(){
    	 //Reset to empty 		
		  lblCost.setText("0");	
		  selbxCategory.setSelectedOption("-1");
   	  	  txtfdName.setValue("");
		  txtfdQty.setValue("");
		  txtfdUnitPrice.setValue("");
		  txtfdNotification.setValue("0");
		  txtbxDescription.setValue("");
		  datePurchased.setDate(new java.util.Date());	
	}

  public double get2DecimalPoints(double dvalue){
		  
		  BigDecimal bd = new BigDecimal(dvalue);
			bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
		 return  bd.doubleValue();
	  }

	
	public String getStrHideNotificationField() {
		return strHideNotificationField;
	}

	public void setStrHideNotificationField(String strHideNotificationField) {
		this.strHideNotificationField = strHideNotificationField;
	}

	public String getIsEditMode() {
		return isEditMode;
	}

	public void setIsEditMode(String isEditMode) {
		this.isEditMode = isEditMode;
	}

	public String getStrItemid() {
		return strItemid;
	}

	public void setStrItemid(String strItemid) {
		this.strItemid = strItemid;
	}

	public Button getBtnCost() {
		return btnCost;
	}

	public void setBtnCost(Button btnCost) {
		this.btnCost = btnCost;
	}

	public DatePopupField getDatePurchased() {
		return datePurchased;
	}

	public void setDatePurchased(DatePopupField datePurchased) {
		this.datePurchased = datePurchased;
	}

	public SelectBox getSelbxCategory() {
		return selbxCategory;
	}

	public void setSelbxCategory(SelectBox selbxCategory) {
		this.selbxCategory = selbxCategory;
	}

	public TextBox getTxtbxDescription() {
		return txtbxDescription;
	}

	public void setTxtbxDescription(TextBox txtbxDescription) {
		this.txtbxDescription = txtbxDescription;
	}

	public TextField getTxtfdName() {
		return txtfdName;
	}

	public void setTxtfdName(TextField txtfdName) {
		this.txtfdName = txtfdName;
	}

	public TextField getTxtfdNotification() {
		return txtfdNotification;
	}

	public void setTxtfdNotification(TextField txtfdNotification) {
		this.txtfdNotification = txtfdNotification;
	}

	public TextField getTxtfdQty() {
		return txtfdQty;
	}

	public void setTxtfdQty(TextField txtfdQty) {
		this.txtfdQty = txtfdQty;
	}

	public TextField getTxtfdUnitPrice() {
		return txtfdUnitPrice;
	}

	public void setTxtfdUnitPrice(TextField txtfdUnitPrice) {
		this.txtfdUnitPrice = txtfdUnitPrice;
	}


	public Panel getPanelBtn() {
		return panelBtn;
	}

	public void setPanelBtn(Panel panelBtn) {
		this.panelBtn = panelBtn;
	}
	
	public Label getLblCost() {
		return lblCost;
	}

	public void setLblCost(Label lblCost) {
		this.lblCost = lblCost;
	}

	public Radio getRadioNotifyNo() {
		return radioNotifyNo;
	}

	public void setRadioNotifyNo(Radio radioNotifyNo) {
		this.radioNotifyNo = radioNotifyNo;
	}

	public Radio getRadioNotifyYes() {
		return radioNotifyYes;
	}

	public void setRadioNotifyYes(Radio radioNotifyYes) {
		this.radioNotifyYes = radioNotifyYes;
	}

	public String[] getZeroDepreciationCategory() {
		return zeroDepreciationCategory;
	}

	public void setZeroDepreciationCategory(String[] zeroDepreciationCategory) {
		this.zeroDepreciationCategory = zeroDepreciationCategory;
	}



}

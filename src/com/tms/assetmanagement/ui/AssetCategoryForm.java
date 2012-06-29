package com.tms.assetmanagement.ui;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.StringTokenizer;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.assetmanagement.model.AssetModule;
import com.tms.assetmanagement.model.DataCategory;
import com.tms.assetmanagement.ui.ValidatorIsInteger;
import com.tms.assetmanagement.ui.ValidatorIsNumeric;

public class AssetCategoryForm extends Form {
	
	protected Label lblName;
	protected Label lblDepreciation;	
	protected TextField txtfdName;
	protected TextField txtfdDepreciation;
	protected Button btnSubmit;
	protected Button btnUpdate;
	protected String strCategoryId;	
	
	
	public void init(){
			
		super.init();
		setMethod("post");
		setColumns(2);
		
		lblName = new Label("lblName","<span class='classRowLabel'>" + Application.getInstance().getMessage("asset.label.categoryName")+ "&nbsp;*</span>");
		lblName.setAlign("right");
		addChild(lblName);
		
		txtfdName = new TextField("txtfdName");		
		txtfdName.addChild(new ValidatorNotEmpty("vEmpty",Application.getInstance().getMessage("asset.message.vNotEmpty")));
		txtfdName.setSize("50");
		txtfdName.setMaxlength("99");
		addChild(txtfdName);
		
		lblDepreciation = new Label("lblDepreciation","<span class='classRowLabel'>" + Application.getInstance().getMessage("asset.label.depreciation")+ "&nbsp;*</span>");
		lblDepreciation.setAlign("right");
		addChild(lblDepreciation);
		
		txtfdDepreciation = new TextField("txtfdDepreciation"); 
		txtfdDepreciation.addChild(new ValidatorNotEmpty("vEmpty",Application.getInstance().getMessage("asset.message.vNotEmpty")));
		//txtfdDepreciation.addChild(new ValidatorIsNumeric("vNumeric",Application.getInstance().getMessage("asset.message.vIsNumeric")));
		txtfdDepreciation.addChild(new ValidatorIsInteger("vInteger",Application.getInstance().getMessage("asset.message.vInteger")));
		txtfdDepreciation.setSize("10");
		txtfdDepreciation.setMaxlength("3");
		addChild(txtfdDepreciation);		
		
		addChild(new Label("space1")); 
		
		btnUpdate = new Button("btnUpdate",Application.getInstance().getMessage("asset.label.btnUpdate"));
		btnUpdate.setHidden(true);
		addChild(btnUpdate);
		
		addChild(new Label("space2"));
		btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("asset.label.btnSubmit"));
		addChild(btnSubmit);
		
	}
	
	public void onRequest(Event event){
		removeChildren();
		init();
		
		super.onRequest(event);
		
		 Application app = Application.getInstance(); 
		 AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		
		 if (strCategoryId != null){
			 
			 //hide Submit and display Update But (Edit)
			 btnSubmit.setHidden(true);
			 btnUpdate.setHidden(false);
			 
			 	Map mapRow;			
				mapRow = (Map) mod.retrieveCategory(strCategoryId);
					
				if (mapRow != null){
					txtfdName.setValue(mapRow.get("categoryName"));				
				 	txtfdDepreciation.setValue( new DecimalFormat("0").format(mapRow.get("depreciation")));
					}	
				else
				{
					 btnSubmit.setHidden(false);
					 btnUpdate.setHidden(true);
				}
		 }			 
	}
		
	public Forward onValidate(Event event){
	
		super.onValidate(event);
		
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		
		DataCategory objCategory = new DataCategory();				
		String buttonClicked = findButtonClicked(event); 
		
		//check the depreciation is in the range 0-100
		String strDepreciation = txtfdDepreciation.getValue().toString();		    	 
		if (strDepreciation.length() > 3)		
			strDepreciation= strDepreciation.substring(0,3);
			
		for (int i=0 ; i<strDepreciation.length(); i++){
			boolean IsDigit =  Character.isDigit(strDepreciation.charAt(i));			
			if(!IsDigit){
				txtfdDepreciation.setInvalid(true);
				return new Forward("inValidDep");
			}else{
				int iDepreciation = Integer.parseInt(strDepreciation);
		     	if (!(iDepreciation >= 0 && iDepreciation <= 100)){
		     		txtfdDepreciation.setInvalid(true);
		     		return new Forward("inValidDep");
		     	}     	
			}
		}		
	 	     	        	
     	//insert records
     	if (buttonClicked.equals(btnSubmit.getAbsoluteName())) {     	         
     		UuidGenerator uuid = UuidGenerator.getInstance();	        	
     		objCategory.setCategoryId((String)uuid.getUuid());
     		objCategory.setCategoryName((String)txtfdName.getValue());
     		objCategory.setDepreciation(Float.parseFloat((String)txtfdDepreciation.getValue()));
     		
     		// Checking for Duplicated Category Name	        		        	
     		if (mod.isUniqueCategory(objCategory))  			       
     			mod.insertCategory(objCategory);				
     		else
     		{
     			txtfdName.setInvalid(true);
     			return new Forward("Duplicated");
     		}				
     	}	        
     	//update records
     	if (buttonClicked.equals(btnUpdate.getAbsoluteName())) {
     		
     		objCategory.setCategoryId(strCategoryId);
     		objCategory.setCategoryName((String)txtfdName.getValue());
     		objCategory.setDepreciation(Float.parseFloat((String)txtfdDepreciation.getValue()));	        	
     		mod.updateCategory(objCategory);
	        	
     		//hide Update Button	        	 
     		btnUpdate.setHidden(true);
     		btnSubmit.setHidden(false);
     	}
	        
     	txtfdName.setValue("");
     	txtfdDepreciation.setValue("");
	        
     	return null;	        
	}


	public String getStrCategoryId() {
		return strCategoryId;
	}

	public void setStrCategoryId(String strCategoryId) {
		this.strCategoryId = strCategoryId;
	}

		
}

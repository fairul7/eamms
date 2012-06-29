package com.tms.assetmanagement.ui;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.assetmanagement.model.AssetModule;
import com.tms.assetmanagement.model.DataDisposal;
import com.tms.assetmanagement.ui.ValidatorIsInteger;
import com.tms.assetmanagement.ui.ValidatorIsNumeric;

public class AssetDisposalForm extends Form {
	
	protected popUpSingleSelectBx singleSelectBx;
	protected DatePopupField dateDisposal;
	protected TextField txtfdQty;
	protected TextBox txtbxReasonD;
	protected Button btnSubmit;
	protected Button btnCancel;
	protected Panel panelBtn;

	private Map mapItem = null;
	private String strAvailableQuantity="0";
	private String strPrice= "0";
	private String itemid = "";

	public String getDefaultTemplate(){			
		return "assetmanagement/tempDisposalForm";
	}

	public void init(){		
		super.init();	
		singleSelectBx = new popUpSingleSelectBx("bx");
		singleSelectBx.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
		addChild(singleSelectBx);		
		singleSelectBx.init();
		
		dateDisposal = new DatePopupField ("dateDispodal");
		dateDisposal.setDate( new java.util.Date());
		addChild(dateDisposal);
		
		txtfdQty = new TextField("txtfdQty");
		txtfdQty.addChild(new ValidatorNotEmpty("vEmpty",Application.getInstance().getMessage("asset.message.vNotEmpty")));			
		txtfdQty.addChild(new ValidatorIsInteger("vInteger"));
		txtfdQty.setSize("10");
		txtfdQty.setMaxlength("10");
		txtfdQty.setOnChange("javascript:calculateDisposalCostAdd()");
		addChild(txtfdQty);	  
				  
		txtbxReasonD = new TextBox("txtbxReasonD");
		txtbxReasonD.setCols("25");
		txtbxReasonD.setRows("5");
		addChild(txtbxReasonD);
			  
		btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("asset.label.btnSubmit") );		
		btnCancel = new Button (Form.CANCEL_FORM_ACTION,  Application.getInstance().getMessage("asset.label.btnCancel"));
		panelBtn = new Panel("panelBtn");	
		panelBtn.addChild(btnSubmit);	
		panelBtn.addChild(btnCancel);
		addChild(panelBtn);			
	}
	 public Forward onSubmit(Event evt){
		 
		 Application app = Application.getInstance(); 
		 AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);	
		 if (itemid.equals("") || itemid == null ){
			String[] listItemid = singleSelectBx.getIds();
			if(listItemid.length > 0){
				itemid = listItemid[0];				
			}
		 }
		 if (itemid != null && !itemid.equals("")){		
			 mapItem =  mod.retrieveItems(itemid);				
			 if(mapItem != null){
				int iTotalQty = (int)Math.ceil(Double.parseDouble(mapItem.get("itemQty").toString()));
				int iDisposalQty = mod.iGetDisposalUnitByItemId(mapItem.get("itemId").toString());
				strAvailableQuantity = new Integer(iTotalQty - iDisposalQty).toString();
				strPrice = mapItem.get("itemUnitPrice").toString();
			}			
		}
		 return super.onSubmit(evt);
	 }
	public void onRequest(Event event){
		super.onRequest(event);	
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);	
		//clear data
		String[] listItemid = singleSelectBx.getIds();
		if(listItemid.length == 0){
			strAvailableQuantity = "0";
			strPrice= "0";
		}
		
		itemid = "";		
		try{
			itemid = event.getRequest().getSession().getAttribute("item_id").toString();
		}catch(Exception e){			
		}	
		
		if (itemid != null && !itemid.equals("")){		
			mapItem =  mod.retrieveItems(itemid);				
			if(mapItem != null){
				int iTotalQty = (int)Math.ceil(Double.parseDouble(mapItem.get("itemQty").toString()));
				int iDisposalQty = mod.iGetDisposalUnitByItemId(mapItem.get("itemId").toString());
				strAvailableQuantity = new Integer(iTotalQty - iDisposalQty).toString();
				strPrice = mapItem.get("itemUnitPrice").toString();
			}			
		 	event.getRequest().getSession().setAttribute("item_id", null);
		}else
		{
			removeChildren();
			init();
		}		
	}
	
	  
	public Forward onValidate(Event event){
		super.onValidate(event);
		
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);

		double dDisposalCost;
		Date purchasedDate = null;		
		if (mapItem != null){
			int iTotalQty = (int)Math.ceil(Double.parseDouble(mapItem.get("itemQty").toString()));
			int iDisposalQty = mod.iGetDisposalUnitByItemId(mapItem.get("itemId").toString());
			strAvailableQuantity = new Integer(iTotalQty - iDisposalQty).toString();
			int iAvailableUnit = Integer.parseInt(strAvailableQuantity);
			int iDisposalUnit = Integer.parseInt(txtfdQty.getValue().toString());
			if(!((iAvailableUnit-iDisposalUnit)>=0)){
				txtfdQty.setInvalid(true);
				return new Forward("InvalidQty");
			}
			purchasedDate = (Date)mapItem.get("datePurchased");
	
			if (!checkDisposalDate(purchasedDate)){
				dateDisposal.setInvalid(true);
				return new Forward("InvalidDate");
			}
			if (btnSubmit.getAbsoluteName().equals(findButtonClicked(event))){						
				dDisposalCost = Double.parseDouble((String)txtfdQty.getValue()) * Double.parseDouble(mapItem.get("itemUnitPrice").toString());
				DataDisposal disposalObject = setDisposalObj(dDisposalCost, mapItem.get("itemId").toString());
				mod.insertDisposal(disposalObject);
				resetDefaultValue();
				return new Forward("Insert");
			}				
		}				
		  //Cancel data
	     	 if(btnCancel.getAbsoluteName().equals(findButtonClicked(event)))
	      		return new Forward(Form.CANCEL_FORM_ACTION);	      	 

		 return null;
	}
	
	//Disposal Date must be greater Or equal to PurchasedDate
	public boolean checkDisposalDate(java.util.Date purchasedDate){
		
		java.util.Date idateDisposal = (java.util.Date) dateDisposal.getDate();
		if(idateDisposal.after(purchasedDate))
			return true;
		else
			return false;			
	}
	
	public DataDisposal setDisposalObj (double dDisposalCost, String itemId){
		
		DataDisposal objDisposal = new DataDisposal();	
			
		UuidGenerator uuid = UuidGenerator.getInstance();
		objDisposal.setDisposalId((String)uuid.getUuid());
		objDisposal.setItemId(itemId);	
		objDisposal.setDateDisposal((java.util.Date)dateDisposal.getDate());
		objDisposal.setDisposalCost(dDisposalCost);
		objDisposal.setDisposalReason((String)txtbxReasonD.getValue());
		objDisposal.setDisposalQty(Float.parseFloat((String)txtfdQty.getValue()));			
			 
		return objDisposal;
	}
	
	public void resetDefaultValue(){

		 dateDisposal.setDate(new java.util.Date());
		 txtfdQty.setValue("");	
		 txtbxReasonD.setValue("");		 
	 }
	
	public Button getBtnCancel() {
			return btnCancel;
		}

	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}

	public DatePopupField getDateDisposal() {
		return dateDisposal;
	}

	public void setDateDisposal(DatePopupField dateDisposal) {
		this.dateDisposal = dateDisposal;
	}

	public Panel getPanelBtn() {
		return panelBtn;
	}

	public void setPanelBtn(Panel panelBtn) {
		this.panelBtn = panelBtn;
	}

	public popUpSingleSelectBx getSingleSelectBx() {
		return singleSelectBx;
	}

	public void setSingleSelectBx(popUpSingleSelectBx singleSelectBx) {
		this.singleSelectBx = singleSelectBx;
	}

	public TextBox getTxtbxReasonD() {
		return txtbxReasonD;
	}

	public void setTxtbxReasonD(TextBox txtbxReasonD) {
		this.txtbxReasonD = txtbxReasonD;
	}

	public TextField getTxtfdQty() {
		return txtfdQty;
	}

	public void setTxtfdQty(TextField txtfdQty) {
		this.txtfdQty = txtfdQty;
	}

	public String getStrAvailableQuantity() {
		return strAvailableQuantity;
	}

	public void setStrAvailableQuantity(String strAvailableQuantity) {
		this.strAvailableQuantity = strAvailableQuantity;
	}

	public String getStrPrice() {
		return strPrice;
	}

	public void setStrPrice(String strPrice) {
		this.strPrice = strPrice;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

			
}

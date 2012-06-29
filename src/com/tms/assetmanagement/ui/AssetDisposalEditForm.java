package com.tms.assetmanagement.ui;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
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

import com.tms.assetmanagement.model.AssetModule;
import com.tms.assetmanagement.model.DataDisposal;
import com.tms.assetmanagement.ui.ValidatorIsInteger;
import com.tms.assetmanagement.ui.ValidatorIsNumeric;

public class AssetDisposalEditForm extends Form {
	
	protected DatePopupField dateDisposal;
	protected TextField txtfdDisposalQty;
	protected TextBox txtbxReasonD;
	protected Button btnSubmit;
	protected Button btnCancel;
	protected Button btnReset;
	protected Button btnUpdate;
	protected Panel panelBtn;
	protected Label lblCost;
	
	private String strDisposalid = "";
	private Map mapDisposal, mapItemFound;
	private String strAvailableUnit = "0";
	private String strUnitPrice="0";
	
	public String getDefaultTemplate(){				
		return "assetmanagement/tempDisposalEditForm";
	}
	
	public void init(){		
		super.init();	
		
		dateDisposal = new DatePopupField ("dateDispodal");
		dateDisposal.setDate( new java.util.Date());
		addChild(dateDisposal);
		
		txtfdDisposalQty = new TextField("txtfdDisposalQty");
		txtfdDisposalQty.addChild(new ValidatorNotEmpty("vEmpty",Application.getInstance().getMessage("asset.message.vNotEmpty")));
		txtfdDisposalQty.addChild(new ValidatorIsInteger("vInteger"));
		txtfdDisposalQty.setSize("10");
		txtfdDisposalQty.setMaxlength("10");
		txtfdDisposalQty.setOnChange("javascript:calculateDisposalCost()");
		addChild(txtfdDisposalQty);		  
		
		txtbxReasonD = new TextBox("txtbxReasonD");
		// txtbxReasonD.addChild(new ValidatorNotEmpty("vEmpty",Application.getInstance().getMessage("asset.message.vNotEmpty")));	
		txtbxReasonD.setCols("25");
		txtbxReasonD.setRows("5");
		addChild(txtbxReasonD);
				
		lblCost = new Label("lblCost");
		lblCost.setText("0");
		addChild(lblCost);
		
		btnCancel = new Button (Form.CANCEL_FORM_ACTION,  Application.getInstance().getMessage("asset.label.btnCancel"));
		btnReset = new Button("btnReset", Application.getInstance().getMessage("asset.label.btnReset") );		  
		btnUpdate = new Button("btnUpdate", Application.getInstance().getMessage("asset.label.btnUpdate") );
			
		panelBtn = new Panel("panelBtn");		  
		panelBtn.addChild(btnUpdate);
		panelBtn.addChild(btnReset);
		panelBtn.addChild(btnCancel);
		addChild(panelBtn);		
	}	
	
	public void onRequest(Event event){
		
		super.onRequest(event);
		getDisposalObj();
		mapItemFound = (Map)getItemMap();
		strAvailableUnit = new Integer( checkAvailableQty(mapItemFound.get("itemQty").toString())).toString();
		strUnitPrice  = mapItemFound.get("itemUnitPrice").toString();		
	}
		  
	public Forward onValidate(Event event){
	
		super.onValidate(event);		
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);		
		
		Date purchasedDate;
		int iQty;
		double dDisposalCost;
		Map mapItemFound = (Map)getItemMap();
		
		if(btnReset.getAbsoluteName().equals(findButtonClicked(event)))
			getDisposalObj();
		    
		if(mapItemFound!= null){	
			purchasedDate = (java.util.Date)mapItemFound.get("datePurchased");
			if (!checkDisposalDate(purchasedDate)){
				dateDisposal.setInvalid(true);
				return new Forward("InvalidDate");
			}				
			
			int iAvailableUnit = Integer.parseInt(strAvailableUnit);
			int iDisposalUnit = Integer.parseInt(txtfdDisposalQty.getValue().toString());
			if(!((iAvailableUnit-iDisposalUnit)>=0)){
				txtfdDisposalQty.setInvalid(true);
				return new Forward("InvalidQty");
			}

		  //Update Data
		  if (btnUpdate.getAbsoluteName().equals(findButtonClicked(event))){					
			
			  dDisposalCost = Double.parseDouble((String)txtfdDisposalQty.getValue()) * Double.parseDouble(mapItemFound.get("itemUnitPrice").toString());
			  lblCost.setText(new DecimalFormat("0.00").format(dDisposalCost));
			  DataDisposal disposalObject = setDisposalObj("EditMode", dDisposalCost);
			  mod.updateDisposal(disposalObject);
		  
			  return new Forward("Update");
		  }   
		}
	 	      	
		if(btnCancel.getAbsoluteName().equals(findButtonClicked(event)))
		    return new Forward(Form.CANCEL_FORM_ACTION);
		      	 
		 return null;
	}
	
	public Map getItemMap(){
		
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		Map mapItem = null;
		
		mapItem = mod.retrieveItems( mapDisposal.get("itemId").toString(),(String)mapDisposal.get("categoryId").toString());			
		return mapItem;
	}
	
	//Check available Qty
	public int checkAvailableQty(String strTotalQty){
		
		int iDisposalQtyFound;		
		iDisposalQtyFound = iDisposalQuantity((String)mapDisposal.get("itemId"),(String)mapDisposal.get("categoryId"));
		int iTotalQty = (int)Math.ceil(Double.parseDouble(strTotalQty.toString()));
		int iAvailableQty = iTotalQty - iDisposalQtyFound;		
		return iAvailableQty;
	}
	
//	 get the disposal Quantity
	public int iDisposalQuantity(String itemid, String categoryid){
		
		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);			
		int iDisposalQty = 0;		

		Collection colDisposallist = mod.retrieveDisposal(itemid, categoryid);		
		if (colDisposallist != null && colDisposallist.size() > 0){			
			if (getStrDisposalid().toString() != null && !"".equals(getStrDisposalid().toString())){
				for(Iterator iiterator = colDisposallist.iterator(); iiterator.hasNext();){
					Map tempDisposal = (Map) iiterator.next();
					//don't sum up the selected disposal item's quantity when it is Edit Mode
					String strSelectedDisposalId = getStrDisposalid().toString();
					if (tempDisposal.get("disposalQty") != null && !strSelectedDisposalId.equals(tempDisposal.get("disposalId").toString()))
						iDisposalQty += (int)Math.ceil(Double.parseDouble(tempDisposal.get("disposalQty").toString()));								
				}
			}
		}
		return iDisposalQty;
	}

	//Disposal Date must be greater Or equal to PurchasedDate
	public boolean checkDisposalDate( java.util.Date purchasedDate){
		
		java.util.Date idateDisposal = (java.util.Date) dateDisposal.getDate();
		if(idateDisposal.after(purchasedDate))
			return true;
		else
			return false;
	}

	public DataDisposal setDisposalObj(String mode , double dDisposalCost){
		
		DataDisposal objDisposal = new DataDisposal();

		objDisposal.setDisposalId(getStrDisposalid());
		objDisposal.setDateDisposal((java.util.Date)dateDisposal.getDate());
		objDisposal.setDisposalCost(dDisposalCost);
		objDisposal.setDisposalReason((String)txtbxReasonD.getValue());
		objDisposal.setDisposalQty(Float.parseFloat((String)txtfdDisposalQty.getValue()));
						 
		return objDisposal;
	}
	
	public void getDisposalObj(){
		
		 Application app = Application.getInstance(); 
		 AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		 
		if (getStrDisposalid().toString() != null && !"".equals(getStrDisposalid().toString())){

			mapDisposal = (Map) mod.retrieveDisposal(getStrDisposalid());
			if (mapDisposal != null){		 		
				  dateDisposal.setDate((java.util.Date)mapDisposal.get("dateDisposal"));
				  txtfdDisposalQty.setValue(new DecimalFormat("0").format(mapDisposal.get("disposalQty")));
				  lblCost.setText(new DecimalFormat("0.00").format(mapDisposal.get("disposalCost")));
				  txtbxReasonD.setValue(mapDisposal.get("disposalReason"));					
			}
		}
		 
	}
	 

	
	public Label getLblCost() {
		return lblCost;
	}

	public void setLblCost(Label lblCost) {
		this.lblCost = lblCost;
	}

	public Map getMapDisposal() {
		return mapDisposal;
	}

	public void setMapDisposal(Map mapDisposal) {
		this.mapDisposal = mapDisposal;
	}

	public String getStrDisposalid() {
		return strDisposalid;
	}


	public void setStrDisposalid(String strDisposalid) {
		this.strDisposalid = strDisposalid;
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

	public TextBox getTxtbxReasonD() {
		return txtbxReasonD;
	}

	public void setTxtbxReasonD(TextBox txtbxReasonD) {
		this.txtbxReasonD = txtbxReasonD;
	}

	public String getStrAvailableUnit() {
		return strAvailableUnit;
	}

	public void setStrAvailableUnit(String strAvailableUnit) {
		this.strAvailableUnit = strAvailableUnit;
	}

	public TextField getTxtfdDisposalQty() {
		return txtfdDisposalQty;
	}

	public void setTxtfdDisposalQty(TextField txtfdDisposalQty) {
		this.txtfdDisposalQty = txtfdDisposalQty;
	}

	public String getStrUnitPrice() {
		return strUnitPrice;
	}

	public void setStrUnitPrice(String strUnitPrice) {
		this.strUnitPrice = strUnitPrice;
	}


	
}

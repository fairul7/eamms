package com.tms.sam.po.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.CheckBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.sam.po.model.PurchaseOrderModule;
import com.tms.sam.po.model.PurchaseOrderObject;
import com.tms.sam.po.model.PurchaseRequestObject;

public class ClosedPurchaseOrder extends Form{
	private String ppID="";
	private String supplierID = "";
	private int count;
	private CheckBox paid;
	private TextField referenceNo;
	private TextField beneficiary;
	private TextField amount;
	private DatePopupField datePaid;
	private Radio cash;
	private Radio cheque;
	private Radio bankDraft;
	private Radio telegraphic;
	private ButtonGroup radioGroup;
	private Button close;
	private Button cancel;
	private Panel btnPanel;
	private PurchaseOrderObject po;
	private String referenceN;
	private String bficiary;
	private String type;
	private String dateP;
	private String amountP;
	
	public static final String FORWARD_BACK = "back";
	public static final String FORWARD_ERROR= "error";
	public void init(){
		setMethod("POST");
		setColumns(2);
		Application app = Application.getInstance();
		
		paid = new CheckBox("paid");
		
		referenceNo = new TextField("referenceNo");
		referenceNo.setSize("10");
		referenceNo.addChild(new ValidatorNotEmpty("rNo"));
		
		beneficiary = new TextField("beneficiary");
		beneficiary.setSize("10");
		beneficiary.addChild(new ValidatorNotEmpty("bficiary"));
		
		amount = new TextField("amount");
		amount.setSize("10");
		amount.addChild(new ValidatorIsNumeric("amountPaid"));
		
		datePaid = new DatePopupField("datePaid");
		
		cash = new Radio("cash");
		cash.setText(app.getMessage("po.label.cash", "Cash"));
		cash.setChecked(true);
		  
		cheque = new Radio("cheque");
		cheque.setText(app.getMessage("po.label.cheque", "Cheque"));
			
		bankDraft = new Radio("bankDraft");
		bankDraft.setText(app.getMessage("po.label.bankDraft", "Bank Draft"));
		
		telegraphic = new Radio("telegraphic");
		telegraphic.setText(app.getMessage("po.label.telegraphic", "Telegraphic"));
		
		close = new Button("close", app.getMessage("po.label.close", "Close this Order"));
		cancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("po.label.cancel"));
		
		btnPanel = new Panel("btnPanel");
		btnPanel.addChild(close);
		btnPanel.addChild(cancel);
		
		radioGroup = new ButtonGroup("radioGroup");
		radioGroup.setType(ButtonGroup.RADIO_TYPE);
		radioGroup.setColumns(4);
		radioGroup.addButton(cash);
		radioGroup.addButton(cheque);
		radioGroup.addButton(bankDraft);
		radioGroup.addButton(telegraphic);
		
		addChild(paid);
		addChild(referenceNo);
		addChild(beneficiary);
		addChild(amount);
		addChild(datePaid);
		addChild(radioGroup);
		addChild(btnPanel);
	}
	
	@Override
	public void onRequest(Event evt) {
		init();
		PurchaseOrderModule poModule = (PurchaseOrderModule)Application.getInstance().getModule(PurchaseOrderModule.class);
		po = poModule.getClosedPurchaseOrder(ppID, supplierID, count);
		referenceN ="";
		bficiary ="";
		type = "";
		dateP ="";
		amountP ="";
		DateFormat dmyDateFmt = new SimpleDateFormat(Application.getInstance().getProperty("globalDateLong"));
		  if(po!=null){
			  referenceNo.setHidden(true);
			  beneficiary.setHidden(true);
			  amount.setHidden(true);
			  datePaid.setHidden(true);
			  radioGroup.setHidden(true);
			  
			  referenceN =po.getReferenceNo();
			  bficiary =po.getBeneficiary();
			  type = po.getTypeOfPayment();
			  dateP =dmyDateFmt.format(po.getDatePaid());
			  amountP =String.valueOf(po.getAmount());
			  
			  paid.setChecked(po.isPaid());
		  }
	}
	
	@Override
	public Forward onSubmit(Event evt) {
		  Forward forward = super.onSubmit(evt);
		  if(!paid.isChecked()){
			  paid.setInvalid(true);
			  this.setInvalid(true);
		  }
			 
		  
		  return forward;
	}
	
	@Override
	public Forward onValidate(Event evt) {
		PurchaseOrderObject obj = new PurchaseOrderObject();
		obj.setReferenceNo(referenceNo.getValue().toString());
		obj.setBeneficiary(beneficiary.getValue().toString());
		obj.setPaid(paid.isChecked());
		String selected = (String)radioGroup.getValue();
		if(selected.endsWith("cash")){
			obj.setTypeOfPayment("cash");
		}else if(selected.endsWith("cheque")){
			obj.setTypeOfPayment("cheque");
		}else if(selected.endsWith("bankDraft")){
			obj.setTypeOfPayment("bankDraft");
		}else if(selected.endsWith("telegraphic")){
			obj.setTypeOfPayment("telegraphic");
		}
		
		obj.setSupplierID(supplierID);
		obj.setCount(count);
		obj.setPpID(ppID);
		obj.setDatePaid(datePaid.getDate());
		obj.setAmount(Double.parseDouble(amount.getValue().toString()));
		
		PurchaseRequestObject prObj = new PurchaseRequestObject();
    	prObj.setPpID(ppID);
    	prObj.setStatus("Closed");
    	prObj.setRank(12);
    	
		PurchaseOrderModule module = (PurchaseOrderModule)Application.getInstance().getModule(PurchaseOrderModule.class);
    	module.insertClosedPurchaseOrder(obj, prObj);
    	
    	
		return new Forward(FORWARD_BACK);
	}

	@Override
	public String getDefaultTemplate() {
		return "po/closedPurchaseOrder";
	}

	public TextField getAmount() {
		return amount;
	}

	public void setAmount(TextField amount) {
		this.amount = amount;
	}

	public Radio getBankDraft() {
		return bankDraft;
	}

	public void setBankDraft(Radio bankDraft) {
		this.bankDraft = bankDraft;
	}

	public TextField getBeneficiary() {
		return beneficiary;
	}

	public void setBeneficiary(TextField beneficiary) {
		this.beneficiary = beneficiary;
	}

	public Panel getBtnPanel() {
		return btnPanel;
	}

	public void setBtnPanel(Panel btnPanel) {
		this.btnPanel = btnPanel;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public Radio getCash() {
		return cash;
	}

	public void setCash(Radio cash) {
		this.cash = cash;
	}

	public Radio getCheque() {
		return cheque;
	}

	public void setCheque(Radio cheque) {
		this.cheque = cheque;
	}

	public Button getClose() {
		return close;
	}

	public void setClose(Button close) {
		this.close = close;
	}

	public DatePopupField getDatePaid() {
		return datePaid;
	}

	public void setDatePaid(DatePopupField datePaid) {
		this.datePaid = datePaid;
	}

	public CheckBox getPaid() {
		return paid;
	}

	public void setPaid(CheckBox paid) {
		this.paid = paid;
	}

	public ButtonGroup getRadioGroup() {
		return radioGroup;
	}

	public void setRadioGroup(ButtonGroup radioGroup) {
		this.radioGroup = radioGroup;
	}

	public TextField getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(TextField referenceNo) {
		this.referenceNo = referenceNo;
	}

	public Radio getTelegraphic() {
		return telegraphic;
	}

	public void setTelegraphic(Radio telegraphic) {
		this.telegraphic = telegraphic;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getPpID() {
		return ppID;
	}

	public void setPpID(String ppID) {
		this.ppID = ppID;
	}

	public String getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}

	public String getAmountP() {
		return amountP;
	}

	public void setAmountP(String amountP) {
		this.amountP = amountP;
	}

	public String getBficiary() {
		return bficiary;
	}

	public void setBficiary(String bficiary) {
		this.bficiary = bficiary;
	}

	public String getDateP() {
		return dateP;
	}

	public void setDateP(String dateP) {
		this.dateP = dateP;
	}

	public PurchaseOrderObject getPo() {
		return po;
	}

	public void setPo(PurchaseOrderObject po) {
		this.po = po;
	}

	public String getReferenceN() {
		return referenceN;
	}

	public void setReferenceN(String referenceN) {
		this.referenceN = referenceN;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}

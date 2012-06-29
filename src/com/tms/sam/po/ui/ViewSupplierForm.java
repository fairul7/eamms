package com.tms.sam.po.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Hidden;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.sam.po.model.AttachmentObject;
import com.tms.sam.po.model.SupplierModule;
import com.tms.sam.po.model.SupplierObject;
import com.tms.sam.po.setting.model.CurrencyObject;
import com.tms.sam.po.setting.model.ItemObject;
import com.tms.sam.po.setting.model.SettingModule;

public class ViewSupplierForm extends Form {
	
	private Label txtSupplier;
	private Label txtCompany;
	private Label txtTelephone;
	private Label attached;
	
	private TextField txtQuotation;
	private TextField totalQuotation;
	private SelectBox currency;
	
	private CheckBox responded;
	private CheckBox recommended;
	
	private AttachmentInfo attachment;
	
	private DatePopupField txtDateReceived;
	private DatePopupField txtDateSent;
	private DatePopupField txtDeliveryDate;
	
	private Collection txtAttachment;
	private Map attachmentMap;
    private Panel buttonPanel;
	private Button submit;
	private Button cancel;
    private Button evaluate;
    
	private String supplierID = "";
	private String ppID = "";
	private int count = 0;
	private String[] itemCode;
	private String[] itemDesc;
	private Label[] qty;
	private Hidden[] hide;
	private TextField[] amount;
	private String[] unitOfMeasure;
	private TextField[] unitPrice;
	
	private int size = 0;
	public static final String FORWARD_BACK = "back";	
    public static final String FORWARD_EVALUATE = "evaluate";
    
	public void init(){
		 setMethod("POST");
		 setColumns(2);
		 Application app = Application.getInstance();
		 SettingModule module = (SettingModule) Application.getInstance().getModule(SettingModule.class);
		 
		 txtSupplier = new Label("txtSupplier", "");
		 txtCompany = new Label("txtCompany", "");
		 txtTelephone = new Label("txtTelephone", "");
		 
		 txtDateSent = new DatePopupField("txtDateSent");
		 txtDateSent.setOptional(true);
		
		 txtDateReceived = new DatePopupField("txtDateReceived");
		 txtDateReceived.setOptional(true);
		 
		 txtDeliveryDate = new DatePopupField("txtDeliveryDate");
		 
		 txtQuotation = new TextField("txtQuotation");
		 attached = new Label("attached", "Attached");
         attachment = new AttachmentInfo("attachment");
         totalQuotation = new TextField("totalQuotation");
         ValidatorIsNumeric vne = new ValidatorIsNumeric("vne", "Must be a number and must not be empty");
         vne.setOptional(true);
         totalQuotation.addChild(vne);
         
         responded = new CheckBox("responded");
         responded.setChecked(true);
         recommended = new CheckBox("recommended");
         
         
         currency = new SelectBox("currency");
        
         Collection cat = module.getCurrency();
         for( Iterator i=cat.iterator(); i.hasNext(); ){
         	CurrencyObject o = (CurrencyObject) i.next();
         	currency.addOption(o.getCurrencyID(), o.getCurrency());
         }
         
         submit = new Button("submit", app.getMessage("po.label.submit", "Submit"));  
         
         cancel = new Button(Form.CANCEL_FORM_ACTION, "cancel");
         cancel.setText(app.getMessage("po.label.cancel", "Cancel"));
         cancel.setOnClick("return back()");
         
                  
         evaluate = new Button("evaluate", app.getMessage("po.label.evaluate", "Evaluate"));
         evaluate.setOnClick("return evaluatefunc()");
         
         buttonPanel = new Panel("buttonPanel");
         buttonPanel.addChild(submit);
         buttonPanel.addChild(cancel);
         buttonPanel.addChild(evaluate);
         
         addChild(responded);
         addChild(recommended);
         addChild(currency);
         addChild(txtSupplier);
         addChild(txtCompany);
         addChild(txtTelephone);         
         addChild(txtDateSent);  
         addChild(txtDateReceived); 
         addChild(txtDeliveryDate);
         addChild(txtQuotation);         
         addChild(attachment);     
         addChild(totalQuotation);
         addChild(buttonPanel);
         addChild(attached);
           
	}
	
	public String getTemplate() {
		 return "po/viewSupplier";
	}
	
	public void onRequest(Event event){
		  init();
		  SupplierObject oneRequest = null;
		  HttpSession session;
	      session = event.getRequest().getSession();
		  
		  attachmentMap = new SequencedHashMap();
		  txtAttachment = null;
		  count = Integer.parseInt(event.getRequest().getParameter("count").toString());
		  ppID = event.getRequest().getParameter("ppID");
		  NumberFormat decimal = new DecimalFormat(".00");
		  
		  if(! "".equals(supplierID)) {
					 
			  SupplierModule module = (SupplierModule)Application.getInstance().getModule(SupplierModule.class);
			  oneRequest = module.singleRequest(supplierID, count, ppID);
			  txtAttachment = module.getAttachment(supplierID, count, ppID);
			  if(txtAttachment.size()!=0){
				  for(Iterator i = txtAttachment.iterator();i.hasNext();){
					  Map map = (Map)i.next() ;
					  String attach  = map.get("newFileName").toString();
					  String attachID = map.get("attachmentID").toString();
					  attachmentMap.put(attach,attachID);
				  }
			  }else{
				  attached.setHidden(true);
			  }
			  
			  event.getRequest().getSession().setAttribute("supplierID", oneRequest.getSupplierID().toString());
			  if(oneRequest.getTotalQuotation()!=0){
				  totalQuotation.setValue(decimal.format(oneRequest.getTotalQuotation()));
			  }
			  
			  if(oneRequest.getDateReceived()!=null){
				  txtDateReceived.setDate(oneRequest.getDateReceived());
			  }
			  
			 
			  Collection supplierItems = module.getSupplierItems(supplierID, ppID, count);
			  
			  txtSupplier.setText(oneRequest.getLastKnownSuppName());
			  txtCompany.setText(oneRequest.getLastKnownCompany());
			  txtTelephone.setText(oneRequest.getLastKnownTelephone());
			  
			  if(oneRequest.getDateSent()!= null){
				  txtDateSent.setDate(oneRequest.getDateSent());
			  }
			  
			  if(oneRequest.getDateDelivery()!= null){
				  txtDeliveryDate.setDate(oneRequest.getDateDelivery());
			  }
			  
			  String recommend = oneRequest.getRecommended();
			  if(recommend.equals("Yes")){
				  recommended.setChecked(true);
			  }else{
				  recommended.setChecked(false);
			  }
			  
			  String response = oneRequest.getResponded();
			  if(response.equals("Yes")){
				  responded.setChecked(true);
			  }else{
				  responded.setChecked(false);
			  }
			  currency.setSelectedOption(oneRequest.getCurrencyUsed());
			  
			  if(supplierItems.size()!=0){
				  size = supplierItems.size();
				  itemCode = new String[size];
				  itemDesc = new String[size];
				  qty = new Label[size];
				  hide = new Hidden[size];
				  amount = new TextField[size];
				  unitOfMeasure = new String[size];
				  unitPrice = new TextField[size];
				  
				  
				  for(int count=0; count<size; count++){
			    	  itemCode[count]= new String("itemCode" + count);
			    	  itemDesc[count]= new String("itemDesc" + count);
			    	  unitOfMeasure[count]= new String("unitOfMeasure" + count);
			    	  qty[count]= new Label("qty" + count);
			    	  hide[count] = new Hidden("hide" + count);
			    	  amount[count]= new TextField("amount" + count);
			    	  amount[count].setSize("10");
			    	  unitPrice[count]= new TextField("unitPrice" + count);
			    	  unitPrice[count].setSize("10");
			    	  unitPrice[count].setOnChange("calculateAmount("+count+")");
			      }		
				  
				  int index =0;
				  for(Iterator itr= supplierItems.iterator(); index<size && itr.hasNext(); ) {
					  ItemObject obj = (ItemObject)itr.next();
					  itemCode[index]=obj.getItemCode();
					  itemDesc[index]=obj.getItemDesc();
					  qty[index].setText(Double.toString(obj.getMinQty()));
					  unitOfMeasure[index]=obj.getUnitOfMeasure();
					  hide[index].setValue(obj.getMinQty());
					  unitPrice[index].setValue(decimal.format(obj.getUnitPrice()));
					  amount[index].setValue(decimal.format(obj.getUnitPrice()*Double.parseDouble(qty[index].getText().toString())));
					  addChild(qty[index]);
					  addChild(unitPrice[index]);
					  addChild(amount[index]);
					  addChild(hide[index]);
					  index++;
				  }
				
			  }
			  
			
			  txtQuotation.setValue(oneRequest.getQuotationDetails());
		  }	  
		 
		  
		  session.removeAttribute("attachmentMap");
		  session.removeAttribute("purchaseID");
	}

	public Forward onSubmit(Event event) {
			 Forward forward = super.onSubmit(event);
			 SupplierObject oneRequest = null;
			 SupplierModule module = (SupplierModule)Application.getInstance().getModule(SupplierModule.class);
			 oneRequest = module.singleRequest(supplierID, count, ppID);
			 String button = findButtonClicked(event);
			 button = (button == null)? "" : button;
			 
			 if (button.endsWith("submit") || button.endsWith("print") ) {
		          	
			        if("".equals(totalQuotation.getValue().toString())){
			        	totalQuotation.setInvalid(true);
			            this.setInvalid(true);
			        }
			        
			        if("".equals(currency.getSelectedOptions().keySet().iterator().next().toString())){
			        	currency.setInvalid(true);
			        	this.setInvalid(true);
			        }
			 }
			 
			 Date checkDate = txtDateReceived.getDate();
		     Calendar calendar = Calendar.getInstance();
		     if(checkDate!=null){
		    	 calendar.setTime(checkDate);
			     boolean isDate=false;        
			        
			     if (txtDateSent.getDate().after(checkDate)) {
			            isDate=true;
			     }
			        
			     if(isDate) {
			      	txtDateReceived.setInvalid(true);
			        this.setInvalid(true);
			     }
		     }
		    
			 
			 return forward;
	}
	  
	public Forward onValidate(Event event){
		String button = findButtonClicked(event);
		button = (button == null)? "" : button;
		
		if(button.endsWith("submit")){
	    	saveInfo(event);
			return new Forward(FORWARD_BACK);
	    }
		
		return null;
	}

	public void saveInfo(Event evt){
		  String ppID ="";
		
		  SupplierModule module = (SupplierModule)Application.getInstance().getModule(SupplierModule.class);
		  ppID = module.getPurchaseID(supplierID);
		  
		  SupplierObject obj = new SupplierObject();
		  AttachmentObject aObj = new AttachmentObject();
		  ItemObject iObj = new ItemObject();
		  Collection attachment = new ArrayList();
		  Collection items = new ArrayList();
		  Map attachmentMap;
		  Date now = new Date();
		   //attachment object
	      HttpSession session;
	      session = evt.getRequest().getSession();
	      Application app = Application.getInstance();
	      User currentUser;  
		  currentUser =app.getCurrentUser();
	      attachmentMap = (Map) session.getAttribute("attachmentMap");
	      if(attachmentMap==null) {
	            attachmentMap = new SequencedHashMap();
	            //session.setAttribute("attachmentMap", attachmentMap);
	      } 
	      if(!attachmentMap.isEmpty() ){
	       	String path ="/purchaseOrdering"+ "/" + currentUser.getId() + "/temp/";
	       	for(Iterator i=attachmentMap.keySet().iterator(); i.hasNext(); ){
	       		String attachmentID = UuidGenerator.getInstance().getUuid();
	       		aObj = new AttachmentObject();
	       		aObj.setAttachmentID(attachmentID);
        		aObj.setSupplierID(supplierID);
	       		aObj.setPpID(ppID);
        		aObj.setUserID(currentUser.getId());
        		aObj.setCounting(count);
	        		
	       		aObj.setPath(path);
	       		aObj.setNewFileName(i.next().toString());
	       		attachment.add(aObj);
	       		//System.out.println(aObj.getPath());
	       	}
	      }
	      NumberFormat decimal = new DecimalFormat(".00");
	      
	      for(int i=0;i<size;i++){
			  iObj = new ItemObject();
			  iObj.setSupplierID(supplierID);
			  iObj.setItemCode(itemCode[i]);
			  iObj.setCounting(count);
			  iObj.setPpID(ppID);
			  if(unitPrice[i].getValue().toString()!=null && !unitPrice[i].getValue().toString().equals("")){
				  iObj.setUnitPrice(Double.parseDouble(unitPrice[i].getValue().toString()));
			  }
			  
			  items.add(iObj);
		  }
	      
	      obj.setSupplierID(supplierID);
		  obj.setDateReceived(txtDateReceived.getDate());
		  
		  obj.setDateSent(txtDateSent.getDate());
		  obj.setCurrencyUsed(currency.getSelectedOptions().keySet().iterator().next().toString());
		  obj.setPpID(ppID);
		  obj.setCounting(count);
		  obj.setItemID(items);
		  obj.setDateDelivery(txtDeliveryDate.getDate());
	      obj.setTotalQuotation(Double.parseDouble(totalQuotation.getValue().toString()));
	      obj.setAttachmentObject(attachment);
	      obj.setQuotationDetails(txtQuotation.getValue().toString());
	      obj.setLastUpdateBy(Application.getInstance().getCurrentUser().getId());
	      obj.setLastUpdateDate(now);
	      if(responded.isChecked()){
	    	  obj.setResponded("Yes");
	      }else{
	    	  obj.setResponded("No");
	      }
	      
	      if(recommended.isChecked()){
	    	  obj.setRecommended("Yes");
	      }else{
	    	  obj.setRecommended("No");
	      }
	      
	      module.updateSupplierInfo(obj);
	      session.removeAttribute("attachmentMap");
	}
	  
	// === [ getters/setters ] =================================================
	public Map getAttachmentMap() {
		return attachmentMap;
	}

	public void setAttachmentMap(Map attachmentMap) {
		this.attachmentMap = attachmentMap;
	}

	public String getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}

	public Collection getTxtAttachment() {
		return txtAttachment;
	}

	public void setTxtAttachment(Collection txtAttachment) {
		this.txtAttachment = txtAttachment;
	}

	public Label getTxtCompany() {
		return txtCompany;
	}

	public void setTxtCompany(Label txtCompany) {
		this.txtCompany = txtCompany;
	}

		
	public DatePopupField getTxtDateSent() {
		return txtDateSent;
	}

	public void setTxtDateSent(DatePopupField txtDateSent) {
		this.txtDateSent = txtDateSent;
	}

	public TextField getTxtQuotation() {
		return txtQuotation;
	}

	public void setTxtQuotation(TextField txtQuotation) {
		this.txtQuotation = txtQuotation;
	}

	public Label getTxtSupplier() {
		return txtSupplier;
	}

	public void setTxtSupplier(Label txtSupplier) {
		this.txtSupplier = txtSupplier;
	}

	public Label getTxtTelephone() {
		return txtTelephone;
	}

	public void setTxtTelephone(Label txtTelephone) {
		this.txtTelephone = txtTelephone;
	}

	public AttachmentInfo getAttachment() {
		return attachment;
	}

	public void setAttachment(AttachmentInfo attachment) {
		this.attachment = attachment;
	}

	public DatePopupField getTxtDateReceived() {
		return txtDateReceived;
	}

	public void setTxtDateReceived(DatePopupField txtDateReceived) {
		this.txtDateReceived = txtDateReceived;
	}


	public TextField getTotalQuotation() {
		return totalQuotation;
	}

	public void setTotalQuotation(TextField totalQuotation) {
		this.totalQuotation = totalQuotation;
	}

	public Panel getButtonPanel() {
		return buttonPanel;
	}

	public void setButtonPanel(Panel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}

	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}

	public Label getAttached() {
		return attached;
	}

	public void setAttached(Label attached) {
		this.attached = attached;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public Button getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(Button evaluate) {
		this.evaluate = evaluate;
	}

	public SelectBox getCurrency() {
		return currency;
	}

	public void setCurrency(SelectBox currency) {
		this.currency = currency;
	}

	public CheckBox getRecommended() {
		return recommended;
	}

	public void setRecommended(CheckBox recommended) {
		this.recommended = recommended;
	}

	public CheckBox getResponded() {
		return responded;
	}

	public void setResponded(CheckBox responded) {
		this.responded = responded;
	}

	public String[] getItemCode() {
		return itemCode;
	}

	public void setItemCode(String[] itemCode) {
		this.itemCode = itemCode;
	}

	public String[] getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String[] itemDesc) {
		this.itemDesc = itemDesc;
	}
	
	public Label[] getQty() {
		return qty;
	}

	public void setQty(Label[] qty) {
		this.qty = qty;
	}

	public String[] getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String[] unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public TextField[] getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(TextField[] unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public TextField[] getAmount() {
		return amount;
	}

	public void setAmount(TextField[] amount) {
		this.amount = amount;
	}

	public Hidden[] getHide() {
		return hide;
	}

	public void setHide(Hidden[] hide) {
		this.hide = hide;
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

	public DatePopupField getTxtDeliveryDate() {
		return txtDeliveryDate;
	}

	public void setTxtDeliveryDate(DatePopupField txtDeliveryDate) {
		this.txtDeliveryDate = txtDeliveryDate;
	}
	
}

package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.fms.facility.model.FacilityDao;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.FacilityObject;
import com.tms.fms.facility.model.SetupModule;

public class CheckOutDetailsForm extends Form {
	
	private String groupId;
	private String date="";
	private String requestor="";
	private String location="";
	private String purpose="";
	
	private Label lbTakenBy[];
	private TextField takenBy[];
	private TextField search;
	protected Button update;
	protected Button cancel;
	protected Button btnSearch;
	protected Panel buttonPanel;
	
	private Collection itemList=new ArrayList();
	public CheckOutDetailsForm() {}

	public CheckOutDetailsForm(String s) {super(s);}

	public void onRequest(Event event) {
		
		populateFields("");
	}
	
	@Override
	public String getDefaultTemplate() {
		return "fms/facility/checkOutDetailsForm";
	}
	
	public void initForm(boolean isUpdate) {
		setMethod("post");
		Application app = Application.getInstance();
		buttonPanel = new Panel("buttonPanel");
		update = new Button("update", app.getMessage("fms.facility.update"));
		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		//if (isUpdate){
		//	buttonPanel.addChild(update);
		//}
		buttonPanel.addChild(cancel);
		addChild(buttonPanel);
	}

	public void populateFields(String src) {
		
		try {
			FacilityDao dao = (FacilityDao) Application.getInstance().getModule(FacilityModule.class).getDao();
			itemList = new ArrayList();
			itemList=dao.checkOutListByGroupId(groupId, src);
			if(itemList.size()>0){
				boolean update = false;
				FacilityObject fo=(FacilityObject)itemList.iterator().next();
				date=DateUtil.formatDate(SetupModule.DATE_TIME_FORMAT, fo.getCheckout_date());
				requestor=fo.getRequestedBySpecial();
				location=fo.getLocation();
				purpose=fo.getPurpose();
				
				int i = 0;
				takenBy = new TextField[itemList.size()];
				lbTakenBy = new Label[itemList.size()];
				
				for (Iterator it = itemList.iterator(); it.hasNext();i++){
					FacilityObject fotb = (FacilityObject) it.next();
					
					takenBy[i] = new TextField("takenBy" + i);
					takenBy[i].setSize("10");
					lbTakenBy[i] = new Label("lbTakenBy" + i);
					
					if (!"".equals(fotb.getTakenBy()) && fotb.getTakenBy()!=null) {
						//lbTakenBy[i].setText(fotb.getTakenBy());
						takenBy[i].setValue(fotb.getTakenBy());
						//takenBy[i].setHidden(true);
					} //else {
						
					//	lbTakenBy[i].setText("");
					//}
						
					addChild(takenBy[i]);
					addChild(lbTakenBy[i]);
				}
				update = true;
				
				search = new TextField("Search");
				if (src!=null && !"".equals(src)){
					search.setValue(src);
				}
				search.setSize("20");
				addChild(search);
				
				btnSearch = new Button("btnSearch", Application.getInstance().getMessage("fms.facility.search"));
				addChild(btnSearch);
				
				initForm(update);
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.toString());
		}
	}

	public Forward onSubmit(Event evt) {
		String buttonName = findButtonClicked(evt);
		kacang.ui.Forward result = super.onSubmit(evt);
		if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
			return new Forward(Form.CANCEL_FORM_ACTION, "checkOutListing.jsp", true);
		} else if (buttonName != null && update.getAbsoluteName().equals(buttonName)) {
	    	return result;
	    } else if (buttonName != null && btnSearch.getAbsoluteName().equals(buttonName)) {
	    	String src = "";
	    	src = (String)search.getValue();
	    	//String fwd = "checkOutDetails.jsp?groupId=" + groupId;
	    	
	    	populateFields(src);
	    	return new Forward("SEARCH");
	    }
		else {return result;}
	}
	
	@Override
	public Forward onValidate(Event evt) {
		try {
			if (itemList != null){
				FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
				FacilityObject o = new FacilityObject();
				int i = 0;
				for (Iterator it = itemList.iterator(); it.hasNext();i++){
					FacilityObject fotb = (FacilityObject) it.next();
					
					//if (takenBy[i].getValue() != null && !"".equals(takenBy[i].getValue())){
						
						o.setId(fotb.getId());
						o.setTakenBy((String)takenBy[i].getValue());
						o.setUpdatedby(Application.getInstance().getCurrentUser().getId());
						o.setUpdatedby_date(new Date());
						mod.updateCheckOutTakenBy(o);
					//}
				}
			}
			
			//String fwd = "checkOutDetails.jsp?groupId=" + groupId;
			
			return new Forward("UPDATED");
		} catch (Exception e) {
			return new Forward("FAILED");
		}
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the itemList
	 */
	public Collection getItemList() {
		return itemList;
	}

	/**
	 * @param itemList the itemList to set
	 */
	public void setItemList(Collection itemList) {
		this.itemList = itemList;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the purpose
	 */
	public String getPurpose() {
		return purpose;
	}

	/**
	 * @param purpose the purpose to set
	 */
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	/**
	 * @return the requestor
	 */
	public String getRequestor() {
		return requestor;
	}

	/**
	 * @param requestor the requestor to set
	 */
	public void setRequestor(String requestor) {
		this.requestor = requestor;
	}

	/**
	 * @return the cancel
	 */
	public Button getCancel() {
		return cancel;
	}

	/**
	 * @param cancel the cancel to set
	 */
	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public TextField[] getTakenBy() {
		return takenBy;
	}

	public void setTakenBy(TextField[] takenBy) {
		this.takenBy = takenBy;
	}

	public Button getUpdate() {
		return update;
	}

	public void setUpdate(Button update) {
		this.update = update;
	}

	public Label[] getLbTakenBy() {
		return lbTakenBy;
	}

	public void setLbTakenBy(Label[] lbTakenBy) {
		this.lbTakenBy = lbTakenBy;
	}

	public Panel getButtonPanel() {
		return buttonPanel;
	}

	public void setButtonPanel(Panel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}

	public TextField getSearch() {
		return search;
	}

	public void setSearch(TextField search) {
		this.search = search;
	}

	public Button getBtnSearch() {
		return btnSearch;
	}

	public void setBtnSearch(Button btnSearch) {
		this.btnSearch = btnSearch;
	}
	
}

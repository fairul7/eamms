package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.tms.fms.engineering.model.EngineeringDao;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.FacilityObject;
import com.tms.fms.util.WidgetUtil;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Radio;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;

public class ScanDefectedTRForm extends Form {
	
	private Button btnScan;
	private Button btnSync;
	private Button btnClr;
	private Button btnScanHOURequest;
	private Button btnSyncHOURequest;
	private Button btnClrHOURequest;
	private String requestId;
	private TextField title;
	private TextField title2;
	private Collection list;
	private Collection HOUDefectedList;
	
	public void init() {
		Application app = Application.getInstance();
		
		title = new TextField("title");
		title.setSize("50");
		title.setMaxlength("255");
		addChild(title);
		title2 = new TextField("title2");
		title2.setSize("50");
		title2.setMaxlength("255");
		addChild(title2);
		
		btnScan = new Button("btnScan", "Scan");
		addChild(btnScan);
		btnSync = new Button("btnSync", "Sync");
		addChild(btnSync);
		btnScanHOURequest = new Button("btnScanHOURequest", "Scan");
		addChild(btnScanHOURequest);
		btnSyncHOURequest = new Button("btnSyncHOURequest", "Sync");
		addChild(btnSyncHOURequest);
		btnClr = new Button("btnClr", "Clear");
		addChild(btnClr);
		btnClrHOURequest = new Button("btnClrHOURequest", "Clear");
		addChild(btnClrHOURequest);
	}
	
	public Forward onValidate(Event evt) {
		String buttonName = findButtonClicked(evt);
		EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		EngineeringDao eDao=(EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
		if(buttonName != null && btnScan.getAbsoluteName().equals(buttonName)){
			//String checkedRadio="";
			requestId = (String)title.getValue();
			if(null!=requestId && !requestId.equals(""))
				list = module.scanDefectedTransportRequest(requestId);
			else
			{
				Collection reqIdList = module.getRequestIdLinkToTR();
				Collection tempList = null;
				list = new ArrayList();
				for (Iterator iterIds = reqIdList.iterator(); iterIds.hasNext();) {
					String ids = String.valueOf(iterIds.next());
					tempList = module.scanDefectedTransportRequest(ids);
					if(tempList.size()>0)
						list.addAll(tempList);
				}
			}
			
			if(list.size()>0)
				return new Forward("refresh");
			else
				return new Forward("zero");
		}
		if(buttonName != null && btnSync.getAbsoluteName().equals(buttonName)){
			requestId = (String)title.getValue();
			if(null!=requestId && !requestId.equals("")){
				EngineeringRequest er = module.getRequest(requestId);
				module.createTransportRequest(er);
				list = module.scanDefectedTransportRequest(requestId);
				return new Forward("refreshSync");
			}
			
		}
		
		if(buttonName != null && btnClr.getAbsoluteName().equals(buttonName)){
			list=null;
			return new Forward("cleared");
		}
		
		if(buttonName != null && btnScanHOURequest.getAbsoluteName().equals(buttonName)){
			//Collection HOUDefList =null;
			requestId = (String)title2.getValue();
			if(null!=requestId && !requestId.equals("")){
				HOUDefectedList = module.getDefectedUnitHeadRequest(requestId);
				//list=null;
				if(HOUDefectedList.size()>0)
					return new Forward("refreshFR");
				else
					return new Forward("zero");
			}
				
			//return new Forward("refreshFR");
		}
		if(buttonName != null && btnSyncHOURequest.getAbsoluteName().equals(buttonName)){
			requestId = (String)title2.getValue();
			if(null!=requestId && !requestId.equals("")){
				HOUDefectedList = module.getDefectedUnitHeadRequest(requestId);
				
				for (Iterator iterator = HOUDefectedList.iterator(); iterator.hasNext();) {
					try {
						FacilityObject fo = (FacilityObject) iterator.next();
						String rCId = fo.getFacilityId();
						eDao.deleteRateCardByReqIdFromRequestUnit(requestId,rCId); 
						//requests.add(i);
					} catch (Exception e) {
					}
				}
				HOUDefectedList = module.getDefectedUnitHeadRequest(requestId);	
				return new Forward("refreshFRSync");
			}
		}
		if(buttonName != null && btnClrHOURequest.getAbsoluteName().equals(buttonName)){
			HOUDefectedList = null;
			return new Forward("cleared");
		}
		return null;
	}
	
	public void onRequest(Event evt) {
		init();
	}
	
	public String getDefaultTemplate() {
		return "/fms/engineering/scanDefectedTR";
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Button getBtnScan() {
		return btnScan;
	}

	public void setBtnScan(Button btnScan) {
		this.btnScan = btnScan;
	}

	public Button getBtnSync() {
		return btnSync;
	}

	public void setBtnSync(Button btnSync) {
		this.btnSync = btnSync;
	}

	public Collection getList() {
		return list;
	}

	public void setList(Collection list) {
		this.list = list;
	}

	public TextField getTitle() {
		return title;
	}

	public void setTitle(TextField title) {
		this.title = title;
	}

	public Button getBtnScanHOURequest() {
		return btnScanHOURequest;
	}

	public void setBtnScanHOURequest(Button btnScanHOURequest) {
		this.btnScanHOURequest = btnScanHOURequest;
	}

	public Button getBtnSyncHOURequest() {
		return btnSyncHOURequest;
	}

	public void setBtnSyncHOURequest(Button btnSyncHOURequest) {
		this.btnSyncHOURequest = btnSyncHOURequest;
	}

	public Collection getHOUDefectedList() {
		return HOUDefectedList;
	}

	public void setHOUDefectedList(Collection defectedList) {
		HOUDefectedList = defectedList;
	}

	public TextField getTitle2() {
		return title2;
	}

	public void setTitle2(TextField title2) {
		this.title2 = title2;
	}

	public Button getBtnClr() {
		return btnClr;
	}

	public void setBtnClr(Button btnClr) {
		this.btnClr = btnClr;
	}

	public Button getBtnClrHOURequest() {
		return btnClrHOURequest;
	}

	public void setBtnClrHOURequest(Button btnClrHOURequest) {
		this.btnClrHOURequest = btnClrHOURequest;
	}
	
}

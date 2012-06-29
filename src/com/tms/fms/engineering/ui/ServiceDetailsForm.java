package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.Radio;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.ManpowerService;
import com.tms.fms.engineering.model.OtherService;
import com.tms.fms.engineering.model.PostProductionService;
import com.tms.fms.engineering.model.ScpService;
import com.tms.fms.engineering.model.Service;
import com.tms.fms.engineering.model.StudioService;
import com.tms.fms.engineering.model.TvroService;
import com.tms.fms.engineering.model.VtrService;
import com.tms.fms.util.WidgetUtil;
import com.tms.fms.widgets.CollapsiblePanel;

public class ServiceDetailsForm extends Form {
	
	protected String serviceId;
	protected String requestId;
	protected Service service=new Service();
	protected Button add;
	protected Button delete;
	protected boolean viewMode=true;		
	protected boolean removeLink=false;	
	protected CollapsiblePanel panel;
	protected HashMap ADD_PAGES=new HashMap();
	protected HashMap EDIT_PAGES=new HashMap();
	protected HashMap VIEW_PAGES=new HashMap();
	protected Collection services=new ArrayList();
	protected CheckBox[] checkBoxes; 
	protected Radio[] feedType=new Radio[3];
	public static String SERVICE_SCPMCP="1";
	public static String SERVICE_POSTPRODUCTION="2";
	public static String SERVICE_VTR="3";
	public static String SERVICE_MANPOWER="4";
	public static String SERVICE_STUDIO="5";
	public static String SERVICE_OTHER="6";
	public static String SERVICE_TVRO="7";
	protected boolean modifyMode=false;
	
	public ServiceDetailsForm() {}

	public ServiceDetailsForm(String s) {super(s);}
	
	public void init() {
		ADD_PAGES.put(SERVICE_SCPMCP,"window.open('scpServiceAdd.jsp?serviceId="+serviceId+"', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=430');");
		//ADD_PAGES.put(SERVICE_POSTPRODUCTION,"myForm = window.open('postProductionAdd.jsp?serviceId="+serviceId+"', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=250'); if (myForm != null) { myForm.focus(); }");
		ADD_PAGES.put(SERVICE_POSTPRODUCTION,"window.open('postProductionAdd.jsp?serviceId="+serviceId+"', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=430');");
		ADD_PAGES.put(SERVICE_VTR,"window.open('vtrServiceAdd.jsp?serviceId="+serviceId+"', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=520');");
		ADD_PAGES.put(SERVICE_MANPOWER,"window.open('manpowerServiceAdd.jsp?serviceId="+serviceId+"', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=430');");
		ADD_PAGES.put(SERVICE_STUDIO,"window.open('studioServiceAdd.jsp?serviceId="+serviceId+"', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=430');");
		ADD_PAGES.put(SERVICE_OTHER,"window.open('otherServiceAdd.jsp?serviceId="+serviceId+"', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=430');");
		ADD_PAGES.put(SERVICE_TVRO,"window.open('tvroServiceAdd.jsp?serviceId="+serviceId+"', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=430');");
		
		EDIT_PAGES.put(SERVICE_SCPMCP,"window.open('scpServiceEdit.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=430');");
		EDIT_PAGES.put(SERVICE_POSTPRODUCTION,"window.open('postProductionEdit.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=430');");
		EDIT_PAGES.put(SERVICE_VTR,"window.open('vtrServiceEdit.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=520');");
		EDIT_PAGES.put(SERVICE_MANPOWER,"window.open('manpowerServiceEdit.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=430');");
		EDIT_PAGES.put(SERVICE_STUDIO,"window.open('studioServiceEdit.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=430');");
		EDIT_PAGES.put(SERVICE_OTHER,"window.open('otherServiceEdit.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=430');");
		EDIT_PAGES.put(SERVICE_TVRO,"window.open('tvroServiceEdit.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=430');");
		
		VIEW_PAGES.put(SERVICE_SCPMCP,"window.open('scpServiceView.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=400');");
		VIEW_PAGES.put(SERVICE_POSTPRODUCTION,"window.open('postProductionView.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=400');");
		VIEW_PAGES.put(SERVICE_VTR,"window.open('vtrServiceView.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=400');");
		VIEW_PAGES.put(SERVICE_MANPOWER,"window.open('manpowerServiceView.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=400');");
		VIEW_PAGES.put(SERVICE_STUDIO,"window.open('studioServiceView.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=400');");
		VIEW_PAGES.put(SERVICE_OTHER,"window.open('otherServiceView.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=400');");
		VIEW_PAGES.put(SERVICE_TVRO,"window.open('tvroServiceView.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=400');");
	}

	private void populateService(){
		setMethod("post");
		init();
		panel= new CollapsiblePanel("collapsiblePanel",Application.getInstance().getMessage("fms.request.label.serviceType")+": "+service.getDisplayTitle());
		panel.setCollapsed(false);
		addChild(panel);
		add=new Button("add",Application.getInstance().getMessage("fms.facility.add"));
		delete=new Button("delete",Application.getInstance().getMessage("fms.facility.delete"));
		delete.setOnClick("javascript:return confirm('"+Application.getInstance().getMessage("fms.facility.msg.confirmDelete")+"');");
		add.setOnClick((String)ADD_PAGES.get(serviceId));
		if(viewMode){
			add.setHidden(true);
			delete.setHidden(true);
		}
		addChild(add);
		addChild(delete);
		EngineeringModule module=(EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		if(SERVICE_SCPMCP.equals(serviceId)){
			services=module.getScpServiceIncludeInvalidRateCard(requestId, serviceId);
			checkBoxes=new CheckBox[services.size()];
			int i=0;
			for(Iterator itr=services.iterator();itr.hasNext();i++){
				ScpService s=(ScpService)itr.next();
				s.setBlockBooking(s.getBlockBooking().equals("1")?"Yes":"No");
				if(!removeLink)
					s.setFacility(getEditPageLink(serviceId, s.getId(), s.getFacility()));
				
				checkBoxes[i]=new CheckBox("deleteKey_"+serviceId+"_"+i);
				checkBoxes[i].setValue(s.getId());
				checkBoxes[i].setGroupName(serviceId);
				addChild(checkBoxes[i]);
			}
		}else if(SERVICE_POSTPRODUCTION.equals(serviceId)){
			services=module.getPostProductionServiceIncludeInvalidRateCard(requestId, serviceId);
			checkBoxes=new CheckBox[services.size()];
			int i=0;
			for(Iterator itr=services.iterator();itr.hasNext();i++){
				PostProductionService s=(PostProductionService)itr.next();
				s.setBlockBooking(s.getBlockBooking().equals("1")?"Yes":"No");
				if(!removeLink)
					s.setFacility(getEditPageLink(serviceId, s.getId(), s.getFacility()));
				
				checkBoxes[i]=new CheckBox("deleteKey_"+serviceId+"_"+i);
				checkBoxes[i].setValue(s.getId());
				checkBoxes[i].setGroupName(serviceId);
				addChild(checkBoxes[i]);
			}
		}else if(SERVICE_VTR.equals(serviceId)){
			services=module.getVtrServiceIncludeInvalidRateCard(requestId, serviceId);
			checkBoxes=new CheckBox[services.size()];
			int i=0;
			for(Iterator itr=services.iterator();itr.hasNext();i++){
				VtrService s=(VtrService)itr.next();
				s.setBlockBooking(s.getBlockBooking().equals("1")?"Yes":"No");
				s.setServiceNameLink((getEditPageLink(serviceId, s.getId(), s.getServiceName())));
				if(!removeLink)
					s.setFacility((getEditPageLink(serviceId, s.getId(), s.getFacility())));
				
				checkBoxes[i]=new CheckBox("deleteKey_"+serviceId+"_"+i);
				checkBoxes[i].setValue(s.getId());
				checkBoxes[i].setGroupName(serviceId);
				addChild(checkBoxes[i]);
			}
		}else if(SERVICE_OTHER.equals(serviceId)){
			services=module.getOtherServiceIncludeInvalidRateCard(requestId, serviceId);
			checkBoxes=new CheckBox[services.size()];
			int i=0;
			for(Iterator itr=services.iterator();itr.hasNext();i++){
				OtherService s=(OtherService)itr.next();
				s.setBlockBooking(s.getBlockBooking().equals("1")?"Yes":"No");
				if(!removeLink)
					s.setFacility(getEditPageLink(serviceId, s.getId(), s.getFacility()));
				
				checkBoxes[i]=new CheckBox("deleteKey_"+serviceId+"_"+i);
				checkBoxes[i].setValue(s.getId());
				checkBoxes[i].setGroupName(serviceId);
				addChild(checkBoxes[i]);
			}
		}else if(SERVICE_STUDIO.equals(serviceId)){
			services=module.getStudioServiceIncludeInvalidRateCard(requestId, serviceId);
			checkBoxes=new CheckBox[services.size()];
			int i=0;
			for(Iterator itr=services.iterator();itr.hasNext();i++){
				StudioService s=(StudioService)itr.next();
				s.setBlockBooking(s.getBlockBooking().equals("1")?"Yes":"No");
				if(!removeLink)
					s.setFacility(getEditPageLink(serviceId, s.getId(), s.getFacility()));
				
				checkBoxes[i]=new CheckBox("deleteKey_"+serviceId+"_"+i);
				checkBoxes[i].setValue(s.getId());
				checkBoxes[i].setGroupName(serviceId);
				addChild(checkBoxes[i]);
			}
		}else if(SERVICE_MANPOWER.equals(serviceId)){
			services=module.getManpowerServiceIncludeInvalidRateCard(requestId, serviceId);
			checkBoxes=new CheckBox[services.size()];
			int i=0;
			for(Iterator itr=services.iterator();itr.hasNext();i++){
				ManpowerService s=(ManpowerService)itr.next();
				s.setBlockBooking(s.getBlockBooking().equals("1")?"Yes":"No");
				if(!removeLink)
					s.setCompetencyName(getEditPageLink(serviceId, s.getId(), s.getCompetencyName()));
				
				checkBoxes[i]=new CheckBox("deleteKey_"+serviceId+"_"+i);
				checkBoxes[i].setValue(s.getId());
				checkBoxes[i].setGroupName(serviceId);
				addChild(checkBoxes[i]);
			}
		}else if(SERVICE_TVRO.equals(serviceId)){
			services=module.getTvroServiceIncludeInvalidRateCard(requestId, serviceId);
			checkBoxes=new CheckBox[services.size()];
			int i=0;
			for(Iterator itr=services.iterator();itr.hasNext();i++){
				TvroService s=(TvroService)itr.next();
				s.setBlockBooking(s.getBlockBooking().equals("1")?"Yes":"No");
				if(!removeLink) {
					String title = s.getFeedTitle();
					if (s.getFacility() == null) {
						title = null;
					}
					s.setFeedTitle(getEditPageLink(serviceId, s.getId(), title));
				}
				
				checkBoxes[i]=new CheckBox("deleteKey_"+serviceId+"_"+i);
				checkBoxes[i].setValue(s.getId());
				checkBoxes[i].setGroupName(serviceId);
				addChild(checkBoxes[i]);
			}
			String[] feedNames=new String[]{Application.getInstance().getMessage("fms.facility.label.localFeed"),
					Application.getInstance().getMessage("fms.facility.label.foreignFeed"),
					Application.getInstance().getMessage("fms.facility.label.visualFeed")};
			for(int j=0;j<feedType.length;j++){
				feedType[j]=new Radio("feedType"+j);
				feedType[j].setGroupName("feedType");
				feedType[j].setText(feedNames[j]);
				feedType[j].setValue(j);
				if(service.getFeedType()!=null){
					try{
						if(j==Integer.parseInt(service.getFeedType())){
							feedType[j].setChecked(true);
						}else{
							feedType[j].setChecked(false);
						}
					}catch(Exception e){}
				}else{
					if(j==0){
						feedType[j].setChecked(true);
					}else{
						feedType[j].setChecked(false);
					}
				}
				
				addChild(feedType[j]);
			}
		}
	}
	
	public String getDefaultTemplate() {
		if(SERVICE_SCPMCP.equals(serviceId)){
			return "fms/engineering/scpTemplate";
		}else if(SERVICE_VTR.equals(serviceId)){
			return "fms/engineering/vtrTemplate";
		}else if(SERVICE_POSTPRODUCTION.equals(serviceId)){
			return "fms/engineering/postProductionTemplate";
		}else if(SERVICE_OTHER.equals(serviceId)){
			return "fms/engineering/otherServiceTemplate";
		}else if(SERVICE_TVRO.equals(serviceId)){
			return "fms/engineering/tvroServiceTemplate";
		}else if(SERVICE_STUDIO.equals(serviceId)){
			return "fms/engineering/studioTemplate";
		}else if(SERVICE_MANPOWER.equals(serviceId)){
			return "fms/engineering/manpowerTemplate";
		}else{
			return "fms/engineering/serviceDetailsFormTemplate";
		}
		
	}
	
	public void onRequest(Event arg0) {
		populateService();
	}
	
	public Forward onSubmit(Event arg0) {
		try{
			String buttonName = findButtonClicked(arg0);
			if (buttonName != null){
				if(SERVICE_TVRO.equals(serviceId)){
					String feedValue=WidgetUtil.getRadioValue(feedType);
					if(feedValue!=null){
						EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
						module.updateRequestService(feedValue,requestId,serviceId);
					}
				}
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return super.onSubmit(arg0);
	}
	
	public Forward onValidate(Event event) {
		String buttonName = findButtonClicked(event);
		
		if (buttonName != null && delete.getAbsoluteName().equals(buttonName)) {
			Collection col =WidgetUtil.getCheckBoxValue(checkBoxes);
			EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			for(Iterator itr=col.iterator();itr.hasNext();){
				String key=(String)itr.next();
				
				try{
					Collection colgetAssignment = module.checkAssignmentByServiceId(key);
					String assignmentId = null;
					if(colgetAssignment.size()>0){
						EngineeringRequest eReq = (EngineeringRequest)colgetAssignment.iterator().next();
						assignmentId = eReq.getAssignmentId();
					}
					module.deleteService(key, requestId, serviceId);
					module.deleteAssignmentByServiceId(key);	//sini woi
					if(assignmentId != null) 
						module.deleteEquipmentAssignment(serviceId);				
					module.insertRequestUnit(requestId, serviceId);
				}catch(Exception er){}
			}
			String urlPage = "requestDetails.jsp";					
			if(modifyMode)
				urlPage = "requestServiceModify.jsp";
			
			return new Forward("DELETED",urlPage,true);
		}
		return new Forward("");
	}

	private String getEditPageLink(String serviceId,String id,String label){
		String link="";
		
		if (label == null) {
			label = "<i>-- missing rate card --</i>";
		}
		
		if(viewMode){
			//link=label;
			link=(String)VIEW_PAGES.get(serviceId);
			link=link.replaceFirst("View.jsp", "View.jsp?id="+id+"&serviceId="+serviceId);
			link= "<a class='collapsiblePanelBar' onClick=\""+link+"\">"+label+"</a>";
		}else{
			link=(String)EDIT_PAGES.get(serviceId);
			link=link.replaceFirst("Edit.jsp", "Edit.jsp?id="+id+"&serviceId="+serviceId);
			link= "<a class='collapsiblePanelBar' onClick=\""+link+"\">"+label+"</a>";
		}
		return link;
	}
	
	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Button getAdd() {
		return add;
	}

	public void setAdd(Button add) {
		this.add = add;
	}

	public CollapsiblePanel getPanel() {
		return panel;
	}

	public void setPanel(CollapsiblePanel panel) {
		this.panel = panel;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public Collection getServices() {
		return services;
	}

	public void setServices(Collection services) {
		this.services = services;
	}

	public CheckBox[] getCheckBoxes() {
		return checkBoxes;
	}

	public void setCheckBoxes(CheckBox[] checkBoxes) {
		this.checkBoxes = checkBoxes;
	}

	public Button getDelete() {
		return delete;
	}

	public void setDelete(Button delete) {
		this.delete = delete;
	}

	public Radio[] getFeedType() {
		return feedType;
	}

	public void setFeedType(Radio[] feedType) {
		this.feedType = feedType;
	}

	public boolean isRemoveLink() {
		return removeLink;
	}

	public void setRemoveLink(boolean removeLink) {
		this.removeLink = removeLink;
	}

	public boolean isModifyMode() {
		return modifyMode;
	}

	public void setModifyMode(boolean modifyMode) {
		this.modifyMode = modifyMode;
	}

		
}

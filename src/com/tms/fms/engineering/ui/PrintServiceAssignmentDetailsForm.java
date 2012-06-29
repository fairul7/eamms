package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.Application;
import kacang.model.Module;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.Radio;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.engineering.model.Assignment;
import com.tms.fms.engineering.model.EngineeringModule;
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

public class PrintServiceAssignmentDetailsForm extends Form {
	
	protected String serviceId;
	protected String requestId;
	protected Service service=new Service();
	protected boolean viewMode=true;
	protected CollapsiblePanel panel;
	protected Collection assignments=new ArrayList();
	public static String SERVICE_SCPMCP="1";
	public static String SERVICE_POSTPRODUCTION="2";
	public static String SERVICE_VTR="3";
	public static String SERVICE_MANPOWER="4";
	public static String SERVICE_STUDIO="5";
	public static String SERVICE_OTHER="6";
	public static String SERVICE_TVRO="7";
	public PrintServiceAssignmentDetailsForm() {}

	public PrintServiceAssignmentDetailsForm(String s) {super(s);}

	

	private void populateService(){
		setMethod("post");
		panel= new CollapsiblePanel("collapsiblePanel",Application.getInstance().getMessage("fms.request.label.serviceType")+": "+service.getDisplayTitle());
		panel.setCollapsed(false);
		panel.setDisplayCollapseButton(false);
		addChild(panel);
		//EngineeringModule module=(EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		
			int i=0;
			for(Iterator<Assignment> itr=assignments.iterator();itr.hasNext();i++){
				Assignment assignment=(Assignment)itr.next();
				//s.setFacility(getEditPageLink(serviceId, s.getId(), s.getFacility()));
			}
		
	}
	
	public String getDefaultTemplate() {
		if (ServiceDetailsForm.SERVICE_MANPOWER.equals(service.getServiceId())){
			return "fms/engineering/printServiceAssignmentManpowerTemp";
		} else {
			return "fms/engineering/printServiceAssignmentTemp";
		}
	}
	
	public void onRequest(Event arg0) {
		populateService();
	}
	
	/*public Forward onSubmit(Event arg0) {
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
	}*/
	
	public Forward onValidate(Event event) {
		String buttonName = findButtonClicked(event);
		
		
		return new Forward("");
	}

	private String getEditPageLink(String serviceId,String id,String label){
		String link="";
		/*if(viewMode){
			//link=label;
			link=(String)VIEW_PAGES.get(serviceId);
			link=link.replaceFirst("View.jsp", "View.jsp?id="+id+"&serviceId="+serviceId);
			link= "<a class='collapsiblePanelBar' onClick=\""+link+"\">"+label+"</a>";
		}else{
			link=(String)EDIT_PAGES.get(serviceId);
			link=link.replaceFirst("Edit.jsp", "Edit.jsp?id="+id+"&serviceId="+serviceId);
			link= "<a class='collapsiblePanelBar' onClick=\""+link+"\">"+label+"</a>";
		}*/
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

	public Collection getAssignments() {
		return assignments;
	}

	public void setAssignments(Collection assignments) {
		this.assignments = assignments;
	}



}

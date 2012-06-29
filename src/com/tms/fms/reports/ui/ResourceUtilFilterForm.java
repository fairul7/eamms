package com.tms.fms.reports.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;
import org.pdfbox.util.operator.SetMoveAndShow;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.reports.model.ReportsFmsModule;
import com.tms.fms.reports.ui.EngineeringReqReportTable.EngineeringReqReportTableModel;
import com.tms.fms.setup.model.ProgramObject;
import com.tms.fms.util.WidgetUtil;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.ComboSelectBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TableFilter;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class ResourceUtilFilterForm extends Form{
	public DatePopupField assignmentDateFrom;
	public DatePopupField assignmentDateTo;
	public SelectBox sbProgram;
	public SelectBox sbStatus, requestStatus;
	public ComboSelectBox comboSType;
	public Button showBtn;
	public Button exportBtn;
	public Panel pnlButton;
	public SelectBox pageSizeSelectBox;
	public CheckBox programChbx;
	public CheckBox nonprogramChbx;

	public void init() {
		setMethod("POST");
		removeChildren();
		
		comboSType = new ComboSelectBox("comboSType");
		addChild(comboSType);
		comboSType.init();
		initServiceType();
		
		assignmentDateFrom = new DatePopupField("assignmentDateFrom");
		assignmentDateFrom.setOptional(true);
		addChild(assignmentDateFrom);
					
		assignmentDateTo = new DatePopupField("assignmentDateTo");
		assignmentDateTo.setOptional(true);
		addChild(assignmentDateTo);
		
		programChbx = new CheckBox("programChbx");
		addChild(programChbx);
		
		nonprogramChbx = new CheckBox("nonprogramChbx");
		addChild(nonprogramChbx);

		SequencedHashMap statusMap=new SequencedHashMap();
		statusMap.put("-1",Application.getInstance().getMessage("fms.facility.table.status"));
		statusMap.putAll(EngineeringModule.RESOURCE_UTIL_MAP);
		sbStatus=new SelectBox("sbStatus");
		sbStatus.setOptionMap(statusMap);
		addChild(sbStatus);
		
		requestStatus = new SelectBox("requestStatus");
		SequencedHashMap reqStatusMap = new SequencedHashMap();
		reqStatusMap.put("-1",Application.getInstance().getMessage("fms.facility.table.status"));
		reqStatusMap.putAll(EngineeringModule.FC_HEAD_STATUS_MAP);
		requestStatus.setOptionMap(reqStatusMap);
		addChild(requestStatus);
		
		sbProgram = new SelectBox("sbProgram");
		sbProgram.setOptions("-1=" + "--Please Select--");
	    try {
	    	ReportsFmsModule module = (ReportsFmsModule) Application.getInstance().getModule(ReportsFmsModule.class);
             
            Collection listPrograms = module.getProgram();
		    if (listPrograms.size() > 0) {
		    	for (Iterator i=listPrograms.iterator(); i.hasNext();) {
		    		ProgramObject progObj = (ProgramObject)i.next();
		        	sbProgram.setOptions(progObj.getProgramId()+"="+ progObj.getProgramName());
		        }
		    }
		}catch (Exception e) {
		    Log.getLog(getClass()).error(e.toString());
		}
		addChild(sbProgram);
		
		pnlButton = new Panel("pnlButton");
		showBtn = new Button("showBtn",Application.getInstance().getMessage("table.label.show","Show"));
		exportBtn = new Button("exportBtn", Application.getInstance().getMessage("fms.report.message.export","Export To Excel"));
		pnlButton.addChild(showBtn);
		pnlButton.addChild(exportBtn);
		addChild(pnlButton);
		
		 
         SequencedHashMap pageMap=new SequencedHashMap();
         pageMap.put("5", "5");
         pageMap.put("10", "10");
         pageMap.put("20", "20");
         pageMap.put("30", "30");
         pageMap.put("40", "40");
         pageMap.put("50", "50");
         pageMap.put("100", "100");
         pageSizeSelectBox=new SelectBox("pageSizeSelectBox");
         pageSizeSelectBox.setOptionMap(pageMap);
         addChild(pageSizeSelectBox);
         
         pageSizeSelectBox.setSelectedOption("20");
		
	}
	
	public void onRequest(Event event) {
//		init();
	}
	
	public void initServiceType() {
        Map map = getServiceTypeMp();
        if (!(map.isEmpty()))
        	comboSType.setLeftValues(map);
    }
	
	private Map getServiceTypeMp() {
        Map map = new SequencedHashMap();
        Collection list = new ArrayList();
        for(Iterator itr=EngineeringModule.SERVICES_MAP.keySet().iterator();itr.hasNext();){
			String key=(String)itr.next();
			map.put(key, (String)EngineeringModule.SERVICES_MAP.get(key));
		}
        return map;
    }
	
	public String getRequestType() {
		
		String returnValue = "";
		
		if(nonprogramChbx.isChecked()){
			returnValue="nonprogram";
		}
		
		if(programChbx.isChecked()){
			returnValue="program";
		}
		
		if(nonprogramChbx.isChecked()&&programChbx.isChecked()){
			returnValue="all";
		}
		
		return returnValue;
	}
	

	public Forward onValidate(Event evt) {
		Forward forward=super.onValidate(evt);
        String buttonClicked = findButtonClicked(evt);
        String resourceServiceTypeId="";
        if(showBtn.getAbsoluteName().equals(buttonClicked)){
        	Map serviceType = comboSType.getRightValues();
    		if(serviceType!=null && serviceType.size()>0){
    			for (Iterator iterator = serviceType.keySet().iterator(); iterator.hasNext();) {
    				String key = (String) iterator.next();
    				
    				if(serviceType.size()==1){
    					resourceServiceTypeId+=key;
    				}else{
    					resourceServiceTypeId+=key+"#";
    				}
    				
    			}
    			
    			
    		}
    		evt.getRequest().getSession().setAttribute("pageSize", WidgetUtil.getSbValue(pageSizeSelectBox));
    		evt.getRequest().getSession().setAttribute("fromDate", assignmentDateFrom.getDate());
			evt.getRequest().getSession().setAttribute("toDate", assignmentDateTo.getDate());
			evt.getRequest().getSession().setAttribute("program", WidgetUtil.getSbValue(sbProgram));
			evt.getRequest().getSession().setAttribute("serviceType", resourceServiceTypeId);
			evt.getRequest().getSession().setAttribute("status", WidgetUtil.getSbValue(sbStatus));
			evt.getRequest().getSession().setAttribute("programType", getRequestType());
			evt.getRequest().getSession().setAttribute("resource_requestStatus", WidgetUtil.getSbValue(requestStatus));
        }
        
        if(exportBtn.getAbsoluteName().equals(buttonClicked)){
        	Map serviceType = comboSType.getRightValues();
    		if(serviceType!=null && serviceType.size()>0){
    			for (Iterator iterator = serviceType.keySet().iterator(); iterator.hasNext();) {
    				String key = (String) iterator.next();
    				
    				if(serviceType.size()==1){
    					resourceServiceTypeId+=key;
    				}else{
    					resourceServiceTypeId+=key+"#";
    				}
    				
    			}
    			
    			
    		}
    		
    		evt.getRequest().getSession().setAttribute("exportfromDate", assignmentDateFrom.getDate());
			evt.getRequest().getSession().setAttribute("exporttoDate", assignmentDateTo.getDate());
			evt.getRequest().getSession().setAttribute("exportprogram", WidgetUtil.getSbValue(sbProgram));
			evt.getRequest().getSession().setAttribute("exportserviceType", resourceServiceTypeId);
			evt.getRequest().getSession().setAttribute("exportstatus", WidgetUtil.getSbValue(sbStatus));
			evt.getRequest().getSession().setAttribute("programType", getRequestType());
			evt.getRequest().getSession().setAttribute("resource_requestStatus", WidgetUtil.getSbValue(requestStatus));
			return new Forward("export");
        }
        return forward;
	}
	
	public String getDefaultTemplate() {
		return "fms/reports/templateResourceUtilFilter";
	}

	public SelectBox getPageSizeSelectBox() {
		return pageSizeSelectBox;
	}

	public void setPageSizeSelectBox(SelectBox pageSizeSelectBox) {
		this.pageSizeSelectBox = pageSizeSelectBox;
	}
	
}

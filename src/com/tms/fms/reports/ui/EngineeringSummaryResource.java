package com.tms.fms.reports.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;


import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.reports.model.ReportsFmsModule;
import com.tms.fms.reports.model.ResourcesObject;
import com.tms.fms.setup.model.ProgramObject;
import com.tms.fms.util.WidgetUtil;
import com.tms.fms.validator.ValidatorSelectBox;


public class EngineeringSummaryResource extends Form
{ 
    
    public static final String REPORT_TEMPLATE = "fms/reports/engSummary";
    
    protected DatePopupField startDate;
	protected DatePopupField endDate;
	protected SelectBox sbProgram;
	protected SelectBox sbServiceType;
	protected SelectBox sbDepartment;
	
    protected Button submit;
    protected Button exportExcel;
    
    protected Map report;
    protected Map freeTime;
    
    private String dutyMode = null;    
    protected Date sDate;
    protected Date eDate;    
    private String defaultWorkProfile;     
    private String serviceType;
    
    private int maxfacility;
   
	public void init(){
		
			initForm();	
	}
	
	public void initForm()
    {
		removeChildren();
        super.init();
        setMethod("POST");
        setInvalid(false);
        
        startDate = new DatePopupField("startDate");
        startDate.setDate(new Date());
        endDate = new DatePopupField("endDate");
        endDate.setDate(new Date());
        submit = new Button("submit", Application.getInstance().getMessage("table.label.show","Generate Report"));
        exportExcel = new Button("reset", Application.getInstance().getMessage("fms.report.message.export","Export"));
      
      
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
		
		sbServiceType = new SelectBox("sbServiceType");
		sbServiceType.addChild(new ValidatorSelectBox("sbServiceTypeValidate","","-1"));
		sbServiceType.addOption("-1", "-- Please Select --");		
		int i=0;
		for(Iterator itr=EngineeringModule.SERVICES_MAP.keySet().iterator();itr.hasNext();i++){
			String key=(String)itr.next();
			sbServiceType.addOption(key, (String)EngineeringModule.SERVICES_MAP.get(key));
		}	
		addChild(sbServiceType);
		
		
		sbDepartment = new SelectBox("sbDepartment");        
		Map cmap = null;
        try {
            cmap = ((FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class)).getFMSDepartments();
        }
        catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        sbDepartment.addOption("-1", "-- Please Select --");
        for (Iterator it = cmap.keySet().iterator(); it.hasNext();) {
            String key = (String) it.next();
            sbDepartment.addOption(key, (String) cmap.get(key));            
        }
        addChild(sbDepartment);
        
        addChild(startDate);
        addChild(endDate);
        addChild(submit);
        addChild(exportExcel);
        

        report = new SequencedHashMap();
    }

    public Forward onSubmit(Event event)
    {
        Forward forward = super.onSubmit(event);
           
        if(startDate.getDate().after(endDate.getDate()))
        {
            setInvalid(true);
            startDate.setInvalid(true);
            endDate.setInvalid(true);
        }
        
        return forward;
    }

    public Forward onValidate(Event event)
    {
    	setDefaultWorkProfile("");    	
        report = new SequencedHashMap();                              
        Date fromDate = getDateFromStartTime(startDate.getDate());
        Date toDate = getDateToEndTime(endDate.getDate());
        Collection facilityDetails = new ArrayList();
        HashMap facilityMap = new HashMap();
            
        if(WidgetUtil.getSbValue(getSbServiceType()) != null || !"-1".equals(WidgetUtil.getSbValue(getSbServiceType()))){
        	serviceType = WidgetUtil.getSbValue(getSbServiceType()); 
        	String serviceId = WidgetUtil.getSbValue(getSbServiceType());        	
        	String programId = WidgetUtil.getSbValue(getSbProgram());        	
        	String departmentId = WidgetUtil.getSbValue(getSbDepartment());        	
        	ReportsFmsModule module = (ReportsFmsModule) Application.getInstance().getModule(ReportsFmsModule.class);	
        	
        	try{
        		
        		Collection facilities = module.getFacilityResource(serviceId, programId, departmentId, fromDate, toDate);
        		        		
        		for(Iterator it = facilities.iterator(); it.hasNext(); ){
        			HashMap map = (HashMap) it.next();
        			String id = (String)map.get("facilityId");
        			String name = (String)map.get("name");    
        			
        			facilityMap = new HashMap();
        			facilityMap.put(id, name);        			 
            		facilityDetails = new ArrayList();
            		
            		Collection facilitiesSeparatedByDate = module.getFacilityResourceDate(serviceId, programId, departmentId, fromDate, toDate);            		
            		for(Iterator itF = facilitiesSeparatedByDate.iterator(); itF.hasNext(); ){
            			HashMap mapF = (HashMap) itF.next();
            			String facilityId = (String)mapF.get("facilityId");        			
            			Date bookDate = (Date)mapF.get("bookDate");
            			//int bookItem = module.getCountFacilityByDate(serviceId, facilityId, bookDate);        			
            			int hour = 0;
            			int minutes = 0;
            			int requestNo = 0;
            			int average = 0;
            			int modMinutes = 0;
            			
            			ResourcesObject ro = new ResourcesObject();               		
            			Collection hourRequest = module.getHoursRequestAverage(bookDate, serviceId, facilityId);
            			                		
            			for(Iterator ith = hourRequest.iterator(); ith.hasNext(); ){
            				HashMap mapreq = (HashMap) ith.next();
            				hour = (Integer)mapreq.get("hour");
            				minutes = (Integer)mapreq.get("minutes");
            				requestNo = (Integer)mapreq.get("requestNo");
            				average = (Integer)mapreq.get("average");	// average total (hour + minute)/requestNo
            				modMinutes = (Integer)mapreq.get("modMinutes");
            				
            				ro.setFacilityId(facilityId);
            				ro.setBookDate(bookDate);
            				ro.setHour(hour);
            				ro.setMinutes(minutes);
            				ro.setRequestNo(requestNo);
            				ro.setAverage(average); 
            				ro.setModMinutes(modMinutes);
            			}            			
            			facilityDetails.add(ro);            			
            		}            		
            		report.put(facilityMap, facilityDetails);
        		}        		
        		maxfacility = report.values().size();
        		
        	}catch(Exception er){
        		
        	}
        }
		
        sDate = startDate.getDate();
        eDate = endDate.getDate();
        
        if(exportExcel.getAbsoluteName().equals(findButtonClicked(event))){
        	
        	return new Forward("export");
        }
        
        return new Forward();
    	//return SearchDuty();
    	    	
    }

    public void onRequest(Event event)
    {	
    	
    }

    
    protected Date getDateToEndTime(Date xDate){
    	    	
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(xDate);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59); 
        xDate = endTime.getTime();
        
        return xDate;      
    }
    
    protected Date getDateFromStartTime(Date xDate){
    	
        Calendar start = Calendar.getInstance();
        start.setTime(xDate);
        start.set(Calendar.HOUR_OF_DAY, 00);
        start.set(Calendar.MINUTE, 00);
        start.set(Calendar.SECOND, 00);
        start.set(Calendar.MILLISECOND, 000000);
        xDate = start.getTime();
        
        return xDate;      
    }
    
    public String getDefaultTemplate()
    {  
    	return REPORT_TEMPLATE;
    }

    protected boolean withinRange(Date date, Date rangeStart, Date rangeEnd)
    {
        if((date.after(rangeStart) || date.equals(rangeStart)) && (date.before(rangeEnd) || date.equals(rangeEnd)))
            return true;
        return false;
    }

	protected boolean remove(Date startDate, Date endDate, Date compareStart, Date compareEnd)
	{
        if((startDate.before(compareStart) || startDate.equals(compareStart)))
			return true;
		return false;
	}
	
	
    /* Getters and Setters */
  
    public DatePopupField getStartDate() {
		return startDate;
	}

	public void setStartDate(DatePopupField startDate) {
		this.startDate = startDate;
	}

	public DatePopupField getEndDate() {
		return endDate;
	}

	public void setEndDate(DatePopupField endDate) {
		this.endDate = endDate;
	}

	public Button getSubmit()
    {
        return submit;
    }

    public void setSubmit(Button submit)
    {
        this.submit = submit;
    }


    public Map getReport()
    {
        return report;
    }

    public void setReport(Map report)
    {
        this.report = report;
    }

	public String getDefaultWorkProfile() {
		return defaultWorkProfile;
	}

	public void setDefaultWorkProfile(String defaultWorkProfile) {
		this.defaultWorkProfile = defaultWorkProfile;
	}

	public Date getSDate() {
		return sDate;
	}

	public void setSDate(Date date) {
		sDate = date;
	}

	public Date getEDate() {
		return eDate;
	}

	public void setEDate(Date date) {
		eDate = date;
	}

	public String getDutyMode() {
		return dutyMode;
	}

	public void setDutyMode(String dutyMode) {
		this.dutyMode = dutyMode;
	}

	public SelectBox getSbServiceType() {
		return sbServiceType;
	}

	public void setSbServiceType(SelectBox sbServiceType) {
		this.sbServiceType = sbServiceType;
	}

	public SelectBox getSbDepartment() {
		return sbDepartment;
	}

	public void setSbDepartment(SelectBox sbDepartment) {
		this.sbDepartment = sbDepartment;
	}

	public SelectBox getSbProgram() {
		return sbProgram;
	}

	public void setSbProgram(SelectBox sbProgram) {
		this.sbProgram = sbProgram;
	}

	public int getMaxfacility() {
		return maxfacility;
	}

	public void setMaxfacility(int maxfacility) {
		this.maxfacility = maxfacility;
	}

	public Button getExportExcel() {
		return exportExcel;
	}

	public void setExportExcel(Button exportExcel) {
		this.exportExcel = exportExcel;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	

}


package com.tms.fms.transport.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.ekms.manpowertemp.model.ManpowerAssignmentObject;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.facility.model.SetupDao;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.facility.model.WorkingProfile;
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.transport.model.TransportModule;


public class DutyRosterForm extends Form
{
	public static final String MY_DUTY = "myduty";
	public static final String SEARCH_DUTY = "searchduty";
	public static final String EDIT_DUTY = "editduty";
	
	
    private Collection dateHeader;
    private Collection assignments;    
    private Date date;   
    
    public static final String SEARCH_TEMPLATE = "fms/viewDuty";
    
    public static final String MY_TEMPLATE = "fms/myDuty";
    ////
    
    private RosterUserSelectBox manpowers;
    protected DatePopupField startDate;
	protected DatePopupField endDate;

    protected Button submit;
    protected Button reset;
    protected Button cancel;

    protected Map report;
    protected Map freeTime;
    
    private String dutyMode = null;
    
    protected Date sDate;
    protected Date eDate;    
    private String defaultWorkProfile;
    
    private Collection holidays;
    
    private boolean hadPermission;
    
    private String department;
    ////
    

    
	public void init(){
		
			initForm();	
	}
	
	public void initForm()
    {
		removeChildren();
		checkPermission();
        super.init();
        setMethod("POST");
        setInvalid(false);
        
        startDate = new DatePopupField("startDate");
        startDate.setDate(new Date());
        endDate = new DatePopupField("endDate");
        endDate.setDate(new Date());
        submit = new Button("submit", Application.getInstance().getMessage("fms.tran.submit","Generate Report"));
        reset = new Button("reset", Application.getInstance().getMessage("general.label.reset","Reset"));
        cancel = new Button("cancel",Application.getInstance().getMessage("general.label.cancel","Cancel"));
        
      //userSelectBox
        if(hadPermission){
			manpowers = new RosterUserSelectBox("manpowers");
			manpowers.setSortable(false);
		    addChild(manpowers);
		    manpowers.init();
        }

        addChild(startDate);
        addChild(endDate);
        addChild(submit);
        addChild(reset);
        addChild(cancel);


        report = new SequencedHashMap();
    }

    public Forward onSubmit(Event event)
    {
        Forward forward = null;
        String button = findButtonClicked(event);
        if(cancel.getAbsoluteName().equals(button))
            forward = new Forward("Cancel");
        else if(reset.getAbsoluteName().equals(button))
        	initForm();
        else
        {
            forward = super.onSubmit(event);
            if(hadPermission){
	            if(manpowers.getIds().length <= 0)
	            {
	                setInvalid(true);
	                manpowers.setInvalid(true);
	            }
            }
            if(startDate.getDate().after(endDate.getDate()))
            {
                setInvalid(true);
                startDate.setInvalid(true);
                endDate.setInvalid(true);
            }
        }
        return forward;
    }

    public Forward onValidate(Event event)
    {
    	if(SEARCH_DUTY.equals(dutyMode))
    		return SearchDuty();
    	
    	if(dutyMode.equals(MY_DUTY))
    		return MyDuty();
    	
    	return new Forward();
    }

    public void onRequest(Event event)
    {	
    	if(department == null)
    		initForm();
        //if (!isInvalid()) {
        	if(dutyMode.equals(SEARCH_DUTY))
        		SearchDuty();
        	
        	if(dutyMode.equals(MY_DUTY))
        		MyDuty();
        //}
        
    }

    protected Forward MyDuty()    
    {
    	
        report = new SequencedHashMap();
        setDefaultWorkProfile("");
                   
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);			
            TransportModule TM = (TransportModule) Application.getInstance().getModule(TransportModule.class);		
            
            Date dateAssignment = getDateToEndTime(new Date());      
            Date endOfAssg = new Date();
            
                try
                {
                	String userId = getWidgetManager().getUser().getId();
                    User user = service.getUser(userId);
                    sDate = getDateFromStartTime(new Date());
                    eDate = getDateToEndTime(new Date());
                    
                    //for assignment
    		        Calendar calAssignment = Calendar.getInstance();
    		        calAssignment.setTime(dateAssignment);
    		        calAssignment.add(Calendar.DATE, 2);		//normal user is restricted to view assignment < 2day
    		        dateAssignment = calAssignment.getTime();
                        		        
                    //set end date to end of month
    		        Calendar end = Calendar.getInstance();
    		        end.setTime(eDate);    		            		        
    		        int lastDate = end.getActualMaximum(Calendar.DATE);
    		        int now = end.get(Calendar.DAY_OF_MONTH);
    		        int addDays = lastDate - now;
    		        
    		        if(addDays < 7)
    		        	addDays = 7;
    		        	
    		        end.add(Calendar.DATE, addDays);
    		        eDate = end.getTime();	    		            		        
    		       
    		        //get HOD permission 
                    Application app = Application.getInstance();
                    String department = null;
                    String unit = null;
                    boolean transApproval = false;
                    FMSDepartmentManager FDM = (FMSDepartmentManager) app.getModule(FMSDepartmentManager.class);   
                   
            		Map map = new HashMap();
            		           			
        			try{        				
        				map =  FDM.getExistingUserDeptUnit(user.getId());        				
        				for(Iterator it = map.keySet().iterator(); it.hasNext(); ){
        					it.next();
        					unit = (String)map.get("unit");
        					department = (String)map.get("department");        					
        				}
        			}catch(Exception er){Log.getLog(getClass()).error("Error on unit/department");}
                	
                	             	                	
            		transApproval = FDM.userIsHOD(userId, department);	    
            	    if(transApproval){            	    	
            	    	//hod can view 30days assignment
            	    	dateAssignment = end.getTime();
            	    }else{
            	    	//get HOU permission 
            	    	if(FDM.userHadPermission(userId, unit))
            	    		dateAssignment = end.getTime();
            	    }
            	    
            	    endOfAssg = dateAssignment;
                	
					
					Collection duties = new ArrayList();
					Collection duty = TM.getManpowerAssignment(userId, sDate, endOfAssg,department);
					ManpowerAssignmentObject manObj = new ManpowerAssignmentObject();
					for (Iterator it = duty.iterator(); it.hasNext(); ){
						manObj = (ManpowerAssignmentObject) it.next();
						
						if (manObj.getEndDate() != null) {	//if got assignment
							if((manObj.getEndDate().after(sDate) || manObj.getEndDate().equals(sDate)) &&
									(manObj.getStartDate().before(eDate) || manObj.getStartDate().equals(eDate))){
								
								duties.add(manObj);
							}
						} else {	
							duties.add(manObj);
						}
					}

                    report.put(user, duties);
                }
                catch (SecurityException e)
                {
                    Log.getLog(getClass()).error(e.getMessage(), e);
                }
				catch (DaoException e)
				{
					Log.getLog(getClass()).error(e.getMessage(), e);
				}
				
				startDate.setDate(sDate);
				endDate.setDate(eDate);
				
				SetupDao dao = (SetupDao) Application.getInstance().getModule(SetupModule.class).getDao();
	            Collection defCol = new ArrayList();
	    		try {
	    			defCol = dao.selectWorkingProfile(null, null, false, 0, -1);
	    			for(Iterator it = defCol.iterator(); it.hasNext(); ){
	    				WorkingProfile wP = (WorkingProfile) it.next();
	    				if(wP.isDefaultProfile()){
	    					setDefaultWorkProfile(wP.getName());
	    				}
	    			}
	    		} catch (DaoException e) {
	    			Log.getLog(getClass()).error(e.toString());			
	    		}
        
        return new Forward();
    }


    protected Forward SearchDuty()
    {    	
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
		TransportModule TM = (TransportModule) Application.getInstance().getModule(TransportModule.class);	
		FMSRegisterManager FRM = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
    	
    	//initForm();
    	setDefaultWorkProfile("");    	
        report = new SequencedHashMap();                              
        Date dateFromUser = endDate.getDate();        
        eDate = getDateToEndTime(dateFromUser);
        String[] manpower = null;
        
        
        if(!hadPermission){
        	String currentUser[] = {getWidgetManager().getUser().getId()};
        	manpower = currentUser;
        }else
        	manpower = manpowers.getIds();
        
        
        if(manpower.length > 0)
        {
            String[] ids = manpower;
			Calendar end = Calendar.getInstance();
			
			sDate = getDateFromStartTime(startDate.getDate());
            for(int i = 0; i < ids.length; i++)
            {
                try
                {
                    String userId = getWidgetManager().getUser().getId();
                    String userSelected = ids[i];
                    User user = service.getUser(userSelected);
                    String workinghour = "-";
                    
                    ManpowerAssignmentObject MaObj = FRM.getWorkingProfileUser(userId);                    
                    if(MaObj != null){
                    	String sTime = MaObj.getStartTime();
                    	String eTime = MaObj.getEndTime();
                    	
                    	workinghour = sTime +" - "+eTime;
                    }                    	
                    
                    user.setProperty("workinghour", workinghour);                                        
					
					if(eDate.after(new Date())){						
						
	    		        end.setTime(new Date());    		        
	    		        end.add(Calendar.DATE, 2);	//normal user is restricted to view assignment < 2day
	    		        eDate = getDateToEndTime(end.getTime());	    		        	    		        
	    		        
	    		        //get HOD permission 
	                    Application app = Application.getInstance();
	                    
	                    String unit = null;
	                    boolean transApproval = false;	                    
	                    FMSDepartmentManager FDM = (FMSDepartmentManager) app.getModule(FMSDepartmentManager.class);   
	                   	            		
	        			try{        				
	        				Map map =  FDM.getExistingUserDeptUnit(user.getId());        				
	        				for(Iterator it = map.keySet().iterator(); it.hasNext(); ){
	        					it.next();
	        					unit = (String)map.get("unit");
	        					department = (String)map.get("department");        					
	        				}
	        			}catch(Exception er){Log.getLog(getClass()).error("Error on unit/department");}
	                	
	                	             	                	
	            		transApproval = FDM.userIsHOD(userId, department);	    
	            	    if(transApproval){            	    	
	            	    	//hod can view 30days assignment
	            	    	eDate = getDateToEndTime(dateFromUser);	
	            	    }else{
	            	    	//get HOU permission 
	            	    	if(FDM.userHadPermission(userId, unit))
	            	    		eDate = getDateToEndTime(dateFromUser);	
	            	    }
	    		        ////
				    					
				    	    
	                }					
					
					try{
			    		department = FRM.getUserDepartment(userSelected);	//find dept for selected user
			    	}catch(Exception e){}
					
					// get holiday
					try {
						Calendar datenow = Calendar.getInstance();
						datenow.setTime(sDate);
						String yr = String.valueOf(datenow.get(Calendar.YEAR));
						String mm = String.valueOf(datenow.get(Calendar.MONTH)+ 1);	//add 1 bcoz Calendar.MONTH starts from 0
						
						holidays = TM.selectHoliday(yr, mm);
					} catch(Exception er) {
						Log.getLog(getClass()).error(er);
					}

					// get duties
					Collection duties = new ArrayList();
					Collection duty = TM.getManpowerAssignment(userSelected, sDate, eDate,department);
					
					if(duty.size() > 0){
						for (Iterator it = duty.iterator(); it.hasNext(); ){
							ManpowerAssignmentObject manObj = (ManpowerAssignmentObject) it.next();
																					
							if (manObj.getEndDate() != null) {	//if got assignment date
								if((manObj.getEndDate().after(sDate) || manObj.getEndDate().equals(sDate)) &&
										(manObj.getStartDate().before(eDate) || manObj.getStartDate().equals(eDate))){								
									duties.add(manObj);
								}
							} else {			//get working profile
								duties.add(manObj);
							}
						}								
					}
						

                    report.put(user, duties);
                }
                catch (SecurityException e)
                {
                    Log.getLog(getClass()).error(e.getMessage(), e);
                }
				catch (DaoException e)
				{
					Log.getLog(getClass()).error(e.getMessage(), e);
				}
            }
            
            SetupDao dao = (SetupDao) Application.getInstance().getModule(SetupModule.class).getDao();
            Collection defCol = new ArrayList();
    		try {
    			defCol = dao.selectWorkingProfile(null, null, false, 0, -1);
    			for(Iterator it = defCol.iterator(); it.hasNext(); ){
    				WorkingProfile wP = (WorkingProfile) it.next();
    				if(wP.isDefaultProfile()){
    					setDefaultWorkProfile(wP.getName());
    				}
    			}
    		} catch (DaoException e) {
    			Log.getLog(getClass()).error(e.toString());			
    		}
        } 
		
        return new Forward();
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
    	String template = "";
    	if(dutyMode.equals(SEARCH_DUTY))
    		template = SEARCH_TEMPLATE;
    	
    	if(dutyMode.equals(MY_DUTY))
    		template = MY_TEMPLATE;
    	
    	return template;
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
	
	protected boolean checkPermission(){
		
		hadPermission = false;
		Application app = Application.getInstance();
		FMSDepartmentManager manager = (FMSDepartmentManager) app.getModule(FMSDepartmentManager.class);
		FMSRegisterManager reg = (FMSRegisterManager) app.getModule(FMSRegisterManager.class);
		
		String userId =  app.getCurrentUser().getId();
		String unitId = "";
		try{
			//Map map = manager.getExistingUserDeptUnit(userId);
			
			User user =  reg.getUser(userId);
			unitId = (String) user.getProperty("unit");			
			
			if(!(null == unitId || "".equals(unitId))){								
				hadPermission = manager.userHadPermission(userId, unitId);				
			}
			
		}catch(Exception se){Log.getLog(getClass()).error(se);}
		
		
		
		
		
		return hadPermission;
	}

    /* Getters and Setters */
    public Button getCancel()
    {
        return cancel;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

    
    public Button getReset()
    {
        return reset;
    }

    public void setReset(Button reset)
    {
        this.reset = reset;
    }
   

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

    public Map getFreeTime()
    {
        return freeTime;
    }

    public void setFreeTime(Map freeTime)
    {
        this.freeTime = freeTime;
    }

	public Collection getDateHeader() {
		return dateHeader;
	}

	public void setDateHeader(Collection dateHeader) {
		this.dateHeader = dateHeader;
	}

	public Collection getAssignments() {
		return assignments;
	}

	public void setAssignments(Collection assignments) {
		this.assignments = assignments;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public RosterUserSelectBox getManpowers() {
		return manpowers;
	}

	public void setManpowers(RosterUserSelectBox manpowers) {
		this.manpowers = manpowers;
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

	public Collection getHolidays() {
		return holidays;
	}

	public void setHolidays(Collection holidays) {
		this.holidays = holidays;
	}

	public boolean isHadPermission() {
		return hadPermission;
	}

	public void setHadPermission(boolean hadPermission) {
		this.hadPermission = hadPermission;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}



}


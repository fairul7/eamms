package com.tms.collab.project.ui;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.collab.project.Milestone;
import com.tms.collab.project.Project;
import com.tms.collab.project.Report;
import com.tms.collab.project.ReportCost;
import com.tms.collab.project.ReportDefects;
import com.tms.collab.project.ReportMilestone;
import com.tms.collab.project.ReportProject;
import com.tms.collab.project.ReportRole;
import com.tms.collab.project.ReportTask;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.project.WormsUtil;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.timesheet.TimeSheetUtil;
import com.tms.collab.timesheet.model.TimeSheetModule;

import kacang.services.security.User;

public class ProjectPerformanceMetricReport extends Form {
    protected String projectId;
    protected Collection taskList;
    protected Collection defectsList;
    protected Collection defects;
    protected Collection costs;
    protected Task[] task;
    protected Task[] noTSTask;
    protected Project project;
    protected Report report;
    protected boolean defectList=false;
    protected boolean costList=false;
    protected Date reportDate;
    protected TextField reportName;
    protected TextBox currentHighlights;
    protected ValidatorNotEmpty validReportName;
    protected CheckBox summary;
    protected CheckBox schedule;
    protected CheckBox effort;
    protected CheckBox defect;
    protected CheckBox cost;   
    protected DatePopupField date;
    protected Button submit;
    protected Button save;
    protected Button savePrint;
    protected double totalHourSpent;
    protected double estimatedHourSpent;
    protected double hourSpent;
    public String getDefaultTemplate() {
        return "project/projectPerformanceMetricReport";
    }

    public void init() {
        super.init();
        setMethod("POST");
        summary= new CheckBox("summary",Application.getInstance().getMessage("project.label.projectSummary","Project Summary"));
        summary.setChecked(true);
        summary.setOnClick("javascript:togglePanelVisibility('summaryA')");
        addChild(summary);
        schedule= new CheckBox("schedule",Application.getInstance().getMessage("project.label.scheduleVariance","Schedule Variance"));
        schedule.setChecked(true);
        schedule.setOnClick("javascript:togglePanelVisibility('scheduleB')");
        addChild(schedule);
        effort= new CheckBox("effort",Application.getInstance().getMessage("project.label.effortVariance","Effort Variance"));
        effort.setChecked(true);
        effort.setOnClick("javascript:togglePanelVisibility('effortC')");
        addChild(effort);
        defect= new CheckBox("defect",Application.getInstance().getMessage("project.label.defectVariance","Defect Variance"));
        defect.setChecked(true);
        defect.setOnClick("javascript:togglePanelVisibility('defectD')");
        addChild(defect);
        cost= new CheckBox("cost",Application.getInstance().getMessage("project.label.projectCost","Project Cost"));
        cost.setChecked(true);
        cost.setOnClick("javascript:togglePanelVisibility('costE')");
        addChild(cost);
        reportName = new TextField("reportName");
        reportName.setSize("20");
        validReportName= new ValidatorNotEmpty("validReportName");
        reportName.addChild(validReportName);
        addChild(reportName);
        currentHighlights = new TextBox("currentHighlights");
        addChild(currentHighlights);
        date = new DatePopupField("date");
        date.setDate(new Date());
        date.setOptional(false);
        addChild(date);
        submit = new Button("submit");
        submit.setText(Application.getInstance().getMessage("project.label.submit","Submit"));
        addChild(submit);
        save = new Button("save");
        save.setText(Application.getInstance().getMessage("project.label.save","Save"));
        addChild(save);
        savePrint = new Button("savePrint");
        savePrint.setText(Application.getInstance().getMessage("project.label.savePrint","Save & Print"));
        addChild(savePrint);
    }

    public void onRequest(Event ev) {
        removeChildren();
        init();
        reportDate=new Date();
        summary.setChecked(true);
        schedule.setChecked(true);
        effort.setChecked(true);
        defect.setChecked(true);
        cost.setChecked(true);
        task=new Task[0];
        noTSTask=null;
        totalHourSpent=0;
        estimatedHourSpent=0;
        hourSpent=0;
        TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
        TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
        WormsHandler wm = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
        try {
        	defects = wm.selectDefectsType();
            project = wm.getProject(projectId);
            project.setRoles(wm.selectProjectUsers(projectId));
            project.setMilestones(wm.selectMilestonesByProjectReport(project, true, date.getDate()));
            project.setActStartDate(wm.selectProjectActualStart(projectId, date.getDate()));
            project.setActEndDate(wm.selectProjectActualEnd(projectId, date.getDate()));
            taskList = mod.getTaskListByProjectReport(projectId);
            project.setProjectStartDate(wm.getProjectStart(project.getProjectId()));
            project.setProjectEndDate(wm.getProjectEnd(project.getProjectId()));
            if(WormsUtil.before(project.getProjectStartDate(),project.getProjectEndDate())){
            	project.setEstDuration(Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getProjectStartDate(),project.getProjectEndDate())));				
			}
			else
			{
				project.setEstDuration("-"+Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getProjectStartDate(),project.getProjectEndDate())));					
			}
            
			if("Not Started".equals(project.getActualStartDate())){
				project.setStartVarians("Not Started");	
				
			}else{
				if(WormsUtil.before(project.getProjectStartDate(), project.getActStartDate())){
					project.setStartVarians(Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getProjectStartDate(), project.getActStartDate())-1));	
				}else{
					project.setStartVarians("-"+Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getProjectStartDate(), project.getActStartDate())-1));
				}												
			}if("Ongoing".equals(project.getActualEndDate())&&!"Not Started".equals(project.getActualStartDate())){
				if(WormsUtil.before(project.getProjectEndDate(),new Date())){
				project.setEndVarians(Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getProjectEndDate(),new Date())-1));
				}
				else
				{
					project.setEndVarians("-"+Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getProjectEndDate(),new Date())-1));	
				}
				if(WormsUtil.before(project.getActStartDate(),new Date())){
					project.setActDuration(Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getActStartDate(),new Date())));
				}
				else
				{
					project.setActDuration("-"+Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getActStartDate(),new Date())));	
				}
			}else if("Not Started".equals(project.getActualEndDate())&&"Not Started".equals(project.getActualStartDate())){
				project.setEndVarians("Not Started");
				project.setActDuration("Not Started");
			}else{
				if(WormsUtil.before(project.getProjectEndDate(), project.getActEndDate())){
				project.setEndVarians(Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getProjectEndDate(), project.getActEndDate())));
				}else
				{
					project.setEndVarians("-"+Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getProjectEndDate(), project.getActEndDate())));	
				}
				
				if(WormsUtil.before(project.getActStartDate(),project.getActEndDate())){
					project.setActDuration(Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getActStartDate(),project.getActEndDate())));
				}
				else
				{
					project.setActDuration("-"+Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getActStartDate(),project.getActEndDate())));	
				}
			}
			
			
            reportName.setValue(project.getProjectName()+"-"+new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            float total = 0;
            for (Iterator i = project.getMilestones().iterator(); i.hasNext();)
            {
                Milestone milestone = (Milestone) i.next();
                
                Collection taskId = new ArrayList();
                for (Iterator it = milestone.getTasks().iterator(); it.hasNext();)
                {
                    Task task = (Task) it.next();
                    taskId.add(task.getId());
                }
                if(taskId.size() > 0)
                {
                    float progress = tm.getAverageProgress((String[]) taskId.toArray(new String[] {}));
                    if(progress == -1)
                        progress = 0;
                    total += (progress*milestone.getMilestoneProgress())/100;
                    /*properties.put(milestone.getMilestoneId(), new Float(progress));*/
                }

                
            }
            project.setProjectStatus(new Float(total));
            DecimalFormat format = new DecimalFormat("##0.00");
            if (taskList!=null && taskList.size()>0) {
                task = new Task[taskList.size()];
                int iCounter = 0;

                for (Iterator i=taskList.iterator();i.hasNext();) {
                    HashMap hm = (HashMap)i.next();
                    task[iCounter] = tm.getTask((String)hm.get("task"));
     
                    Collection assigneeList = task[iCounter].getAttendees();
       
                                        
                   
					String[][] userList = new String[assigneeList.size()][2];

					double totalMandaysSpent = 0;
					double totalHoursSpent = 0;					
					double est=task[iCounter].getEstimationMandays()*task[iCounter].getAttendees().size();
					estimatedHourSpent+=(Double.parseDouble(format.format(est)));
					
	                int j=0;
	                // Calculate total hour spent by each user on this task
					for(Iterator itr=assigneeList.iterator(); itr.hasNext(); j++) {
						Assignee assignee = (Assignee)itr.next();


						double totalHourSpentThisUser = mod.getTotalHourReport(assignee.getUserId(), null, task[iCounter].getId(), date.getDate(),true);
						totalHourSpentThisUser += mod.getTotalHourReportAdjust(assignee.getUserId(), null, task[iCounter].getId(), date.getDate(),false);
						

						totalHoursSpent += totalHourSpentThisUser;

					}

					totalMandaysSpent = totalHoursSpent/TimeSheetUtil.WORKING_HOUR_PER_DAY;
					totalMandaysSpent=Double.parseDouble(format.format((totalMandaysSpent)));
					hourSpent+=totalMandaysSpent;
					task[iCounter].setUserList(userList);
					task[iCounter].setTotalMandaysSpent(totalMandaysSpent);
					task[iCounter].setTotalAssignee(userList.length);
                    
                    
                    iCounter++;
                }
            }

            totalHourSpent = mod.getTotalHourSpentForProject(projectId, date.getDate());
            defectList=false;
            costList=false;
            report=wm.getLatestReport(projectId,true);
            if(report!=null){
            if(report.getDefects().size()>0){
            	defectList=true;
            	defectsList=report.getDefects();
            }else
            	defectList=false;
            if(report.getCost().size()>0){
            	costList=true;
            	costs=report.getCost();
            }else
            	costList=false;
            }
        }
        catch(Exception e) {
           Log.getLog(getClass()).error("error = "+e.toString());
        }
    }
    
    public Forward onSubmit(Event evt) {
    	Forward forward = super.onSubmit(evt);
    	String buttonClicked = findButtonClicked(evt);
    	if (buttonClicked.equals(submit.getAbsoluteName())) {
    		WormsHandler wm = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
    		TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
    		TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
    		try
    		{
    			totalHourSpent=0;
    	        estimatedHourSpent=0;
    	        hourSpent=0;
    			reportName.setValue(project.getProjectName()+"-"+new SimpleDateFormat("dd/MM/yyyy").format(date.getDate()));	
    		reportDate=date.getDate();
    		project.setActStartDate(wm.selectProjectActualStart(projectId, date.getDate()));
            project.setActEndDate(wm.selectProjectActualEnd(projectId, date.getDate()));
            if(WormsUtil.before(project.getProjectStartDate(),project.getProjectEndDate())){
            	project.setEstDuration(Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getProjectStartDate(),project.getProjectEndDate())));				
			}
			else
			{
				project.setEstDuration("-"+Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getProjectStartDate(),project.getProjectEndDate())));					
			}
            
			if("Not Started".equals(project.getActualStartDate())){
				project.setStartVarians("Not Started");	
				
			}else{
				if(WormsUtil.before(project.getProjectStartDate(), project.getActStartDate())){
					project.setStartVarians(Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getProjectStartDate(), project.getActStartDate())));	
				}else{
					project.setStartVarians("-"+Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getProjectStartDate(), project.getActStartDate())));
				}												
			}if("Ongoing".equals(project.getActualEndDate())&&!"Not Started".equals(project.getActualStartDate())){
				if(WormsUtil.before(project.getProjectEndDate(),new Date())){
				project.setEndVarians(Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getProjectEndDate(),new Date())));
				}
				else
				{
					project.setEndVarians("-"+Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getProjectEndDate(),new Date())));	
				}
				if(WormsUtil.before(project.getActStartDate(),new Date())){
					project.setActDuration(Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getActStartDate(),new Date())));
				}
				else
				{
					project.setActDuration("-"+Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getActStartDate(),new Date())));	
				}
			}else if("Not Started".equals(project.getActualEndDate())&&"Not Started".equals(project.getActualStartDate())){
				project.setEndVarians("Not Started");
				project.setActDuration("Not Started");
			}else{
				if(WormsUtil.before(project.getProjectEndDate(), project.getActEndDate())){
				project.setEndVarians(Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getProjectEndDate(), project.getActEndDate())));
				}else
				{
					project.setEndVarians("-"+Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getProjectEndDate(), project.getActEndDate())));	
				}
				
				if(WormsUtil.before(project.getActStartDate(),project.getActEndDate())){
					project.setActDuration(Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getActStartDate(),project.getActEndDate())));
				}
				else
				{
					project.setActDuration("-"+Integer.toString(WormsUtil.getWorkingDays(project.getProjectWorking(), project.getActStartDate(),project.getActEndDate())));	
				}
			}
    		project.setMilestones(wm.selectMilestonesByProjectReport(project, true, date.getDate()));
            taskList = mod.getTaskListByProjectReport(projectId);
            float total = 0;
            for (Iterator i = project.getMilestones().iterator(); i.hasNext();)
            {
                Milestone milestone = (Milestone) i.next();
                
                Collection taskId = new ArrayList();
                for (Iterator it = milestone.getTasks().iterator(); it.hasNext();)
                {
                    Task task = (Task) it.next();
                    taskId.add(task.getId());
                }
                if(taskId.size() > 0)
                {
                    float progress = tm.getAverageProgress((String[]) taskId.toArray(new String[] {}));
                    if(progress == -1)
                        progress = 0;
                    total += (progress*milestone.getMilestoneProgress())/100;
                    /*properties.put(milestone.getMilestoneId(), new Float(progress));*/
                }

                
            }
            project.setProjectStatus(new Float(total));

            
            DecimalFormat format = new DecimalFormat("##0.00");
            if (taskList!=null && taskList.size()>0) {
                task = new Task[taskList.size()];
                int iCounter = 0;

                for (Iterator i=taskList.iterator();i.hasNext();) {
                    HashMap hm = (HashMap)i.next();
                    task[iCounter] = tm.getTask((String)hm.get("task"));
     
                    Collection assigneeList = task[iCounter].getAttendees();
       
                                        
                   
					String[][] userList = new String[assigneeList.size()][2];

					double totalMandaysSpent = 0;
					double totalHoursSpent = 0;	
					double est=task[iCounter].getEstimationMandays()*task[iCounter].getAttendees().size();
					estimatedHourSpent+=(Double.parseDouble(format.format(est)));


	                int j=0;
	                // Calculate total hour spent by each user on this task
					for(Iterator itr=assigneeList.iterator(); itr.hasNext(); j++) {
						Assignee assignee = (Assignee)itr.next();


						double totalHourSpentThisUser = mod.getTotalHourReport(assignee.getUserId(), null, task[iCounter].getId(), date.getDate(),true);
						totalHourSpentThisUser += mod.getTotalHourReportAdjust(assignee.getUserId(), null, task[iCounter].getId(), date.getDate(),false);
						

						totalHoursSpent += totalHourSpentThisUser;

					}

					totalMandaysSpent = totalHoursSpent/TimeSheetUtil.WORKING_HOUR_PER_DAY;
					totalMandaysSpent=Double.parseDouble(format.format((totalMandaysSpent)));
					hourSpent+=totalMandaysSpent;
					task[iCounter].setUserList(userList);
					task[iCounter].setTotalMandaysSpent(totalMandaysSpent);
					task[iCounter].setTotalAssignee(userList.length);
                    
                    
                    iCounter++;
                }
            }
            totalHourSpent = mod.getTotalHourSpentForProject(projectId, date.getDate());
            this.setInvalid(true);
    		}
            catch(Exception e) {
               Log.getLog(getClass()).error("error = "+e.toString());
            }
    	}
    	
		return forward;
	}

	public Forward onValidate(Event evt) {
        Forward forward = super.onValidate(evt);
        WormsHandler wm = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
        DecimalFormat format = new DecimalFormat("#0.00");
        Collection rolesList = new ArrayList();
        Collection milestonesList = new ArrayList();
        Collection taskList = new ArrayList();
        Collection defectsList = new ArrayList();
        Collection costsList = new ArrayList();
        try{
        String buttonClicked = findButtonClicked(evt);
        UuidGenerator uuid=UuidGenerator.getInstance();
        Report report= new Report();
        String reportId=uuid.getUuid();
        report.setReportId(reportId);
        report.setReportName(reportName.getValue().toString());
        report.setReportDate(reportDate);
        report.setCreatedBy(evt.getWidgetManager().getUser().getId());
        report.setProjectId(project.getProjectId());
        if(summary.isChecked()){
        	ReportProject rp= new ReportProject();
        	rp.setReportId(reportId);
        	rp.setProjectId(project.getProjectId());
        	rp.setProjectName(project.getProjectName());
        	rp.setProjectSummary(project.getProjectSummary());
        	rp.setProjectStatus(project.getProjectStatus());
        	rp.setProjectValue(project.getProjectCurrencyValue());
        	rp.setProjectStartDate(project.getProjectStartDate());
        	rp.setProjectEndDate(project.getProjectEndDate());
        	rp.setActualProjectStartDate(project.getActualStartDate());
        	rp.setActualProjectEndDate(project.getActualEndDate());
        	rp.setStartVariance(project.getStartVarians());
        	rp.setEndVariance(project.getEndVarians());
        	rp.setClientName(project.getClientName());
        	rp.setCurrentHighlights(currentHighlights.getValue().toString());
        	rp.setActDuration(project.getActDuration());
        	rp.setEstDuration(project.getEstDuration());
        	report.setProjects(rp);
        	for (Iterator i = project.getRoles().iterator(); i.hasNext();)
            {                
				try {
					HashMap role = (HashMap) i.next();
	                Collection involved;
					involved = wm.selectRolePersonnel(role.get("roleId").toString());
					String user="";
	                for(Iterator j = involved.iterator(); j.hasNext();)
	                {
	                	User roleuser = (User) j.next();
	                	user+=roleuser.getProperty("firstName").toString()+" "+roleuser.getProperty("lastName").toString()+", ";
	                }
	                if(user.length()>0){
	                	user=user.substring(0,user.length()-2);
	                }
	                else{
	                	user="-";
	                }
	                ReportRole rr= new ReportRole();
	                rr.setReportId(reportId);
	                rr.setRoleId(uuid.getUuid());
	                rr.setRoleName(role.get("roleName").toString());
	                rr.setUser(user);
	                rolesList.add(rr);
				} catch (WormsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            }
        	report.setRoles(rolesList);
                        
        }
        if(schedule.isChecked()){
        	for (Iterator i = project.getMilestones().iterator(); i.hasNext();)
            {                
				Milestone milestone = (Milestone) i.next();	                
	            ReportMilestone rm= new ReportMilestone();
	            rm.setReportId(reportId);
	            rm.setMilestoneId(uuid.getUuid());
	            rm.setMilestoneName(milestone.getMilestoneName());
	            rm.setMilestoneOrder(milestone.getMilestoneOrder());
	            rm.setVariance(milestone.getVariance());
	            rm.setActStartDate(milestone.getActualStartDate());            
	            rm.setActEndDate(milestone.getActualEndDate());
	            if(milestone.getStartDate()==null){
	            	rm.setEstStartDate("");
	            }else
	            rm.setEstStartDate(new SimpleDateFormat("ddMMMyyyy").format(milestone.getStartDate()));
	            if(milestone.getEndDate()==null){
	            	rm.setEstEndDate("");
	            }else
	            rm.setEstEndDate(new SimpleDateFormat("ddMMMyyyy").format(milestone.getEndDate()));
	            rm.setActVariance(milestone.getActualDuration());
	            rm.setEstVariance(milestone.getDuration());
	            milestonesList.add(rm);
				                
            }    
        	report.setMilestones(milestonesList);
        }
        if(effort.isChecked()){       	
        	for (int i=0; i<task.length;i++)
            {             
        		ReportTask rt= new ReportTask();
        		rt.setReportId(reportId);
        		rt.setTaskId(uuid.getUuid());
        		rt.setTaskName(task[i].getTitle());
        		double estMandays=task[i].getEstimationMandays()*task[i].getTotalAssignee();
        		rt.setEstimatedMandays(estMandays);        		
        		rt.setActualMandays(format.format(task[i].getTotalMandaysSpent()));
        		double variance=task[i].getTotalMandaysSpent()-estMandays;
        		rt.setVariance(format.format(variance));
        		rt.setTaskOrder(i+1);
        		taskList.add(rt);
            }   
        	ReportTask rt= new ReportTask();
    		rt.setReportId(reportId);
    		rt.setTaskId(uuid.getUuid());
    		rt.setTaskName("Total");
    		rt.setEstimatedMandays(estimatedHourSpent);
    		rt.setActualMandays(format.format(hourSpent));
    		double variance=hourSpent-estimatedHourSpent;
    		rt.setVariance(format.format(variance));
    		rt.setTaskOrder(task.length+1);
    		taskList.add(rt);
        	report.setTasks(taskList);
        }
        if(defect.isChecked()){
        	ReportDefects rd= new ReportDefects();
        	int count=1;
        	for (Iterator i = defects.iterator(); i.hasNext();)
            {                
				HashMap map = (HashMap) i.next();	                
	            rd= new ReportDefects();
	            rd.setReportId(reportId);
	            rd.setDefectTypeId(uuid.getUuid());
	            rd.setDefectTypeName(map.get("defects_Name").toString());
	            rd.setResolved(Integer.parseInt(evt.getParameter("insert_resolved_"+map.get("defects_Name").toString()+"_Hidden")));
	            rd.setUnresolved(Integer.parseInt(evt.getParameter("insert_unresolved_"+map.get("defects_Name").toString()+"_Hidden")));
	            rd.setTotal(Integer.parseInt(evt.getParameter("total_"+map.get("defects_Name").toString()+"_Hidden")));
	            double severe;
	            if(rd.getTotal()==0||Integer.parseInt(evt.getParameter("total_Total_Hidden"))==0){
	            	severe=0.0;
	            }else{
	            severe=(Double.parseDouble(evt.getParameter("total_"+map.get("defects_Name").toString()+"_Hidden"))*100)/Double.parseDouble(evt.getParameter("total_Total_Hidden"));
	            System.out.println("Severity:"+severe);
	            }
	            rd.setSeverity(severe);
	            rd.setDefectOrder(count);
	            defectsList.add(rd);
	            count++;
            }   
        	rd= new ReportDefects();
            rd.setReportId(reportId);
            rd.setDefectTypeId(uuid.getUuid());
            rd.setDefectTypeName("Total");
            rd.setResolved(Integer.parseInt(evt.getParameter("resolved_Total_Hidden")));
            rd.setUnresolved(Integer.parseInt(evt.getParameter("unresolved_Total_Hidden")));
            rd.setTotal(Integer.parseInt(evt.getParameter("total_Total_Hidden")));
            rd.setSeverity(100.00);
            rd.setDefectOrder(count);
            defectsList.add(rd);
            count++;
            report.setDefects(defectsList);
        }
        if(cost.isChecked()){
        	ReportCost rc= new ReportCost();
        	rc.setReportId(reportId);
        	rc.setCostId(uuid.getUuid());
        	rc.setCostName(Application.getInstance().getMessage("project.label.projectManagement","Project Management"));
        	rc.setEstimated(evt.getParameter("insertEstimated_projectManagement_Hidden"));
        	rc.setActual(evt.getParameter("insertActual_projectManagement_Hidden"));
        	rc.setVariance(evt.getParameter("insertvariance_projectManagement_Hidden"));
        	rc.setCostOrder(1);
        	costsList.add(rc);
        	rc= new ReportCost();
        	rc.setReportId(reportId);
        	rc.setCostId(uuid.getUuid());
        	rc.setCostName(Application.getInstance().getMessage("project.label.development","Development"));
        	rc.setEstimated(evt.getParameter("insertEstimated_development_Hidden"));
        	rc.setActual(evt.getParameter("insertActual_development_Hidden"));
        	rc.setVariance(evt.getParameter("insertvariance_development_Hidden"));
        	rc.setCostOrder(2);
        	costsList.add(rc);
        	rc= new ReportCost();
        	rc.setReportId(reportId);
        	rc.setCostId(uuid.getUuid());
        	rc.setCostName(Application.getInstance().getMessage("project.label.travel","Travel"));
        	rc.setEstimated(evt.getParameter("insertEstimated_travel_Hidden"));
        	rc.setActual(evt.getParameter("insertActual_travel_Hidden"));
        	rc.setVariance(evt.getParameter("insertvariance_travel_Hidden"));
        	rc.setCostOrder(3);
        	costsList.add(rc);
        	rc= new ReportCost();
        	rc.setReportId(reportId);
        	rc.setCostId(uuid.getUuid());
        	rc.setCostName(Application.getInstance().getMessage("project.label.hardwareSale","Hardware Sale"));
        	rc.setEstimated(evt.getParameter("insertEstimated_hardwareSales_Hidden"));
        	rc.setActual(evt.getParameter("insertActual_hardwareSales_Hidden"));
        	rc.setVariance(evt.getParameter("insertvariance_hardwareSales_Hidden"));       	
        	rc.setCostOrder(4);
        	costsList.add(rc);
        	rc= new ReportCost();
        	rc.setReportId(reportId);
        	rc.setCostId(uuid.getUuid());
        	rc.setCostName(Application.getInstance().getMessage("project.label.softwareSale","Software Sale"));
        	rc.setEstimated(evt.getParameter("insertEstimated_softwareSales_Hidden"));
            rc.setActual(evt.getParameter("insertActual_softwareSales_Hidden"));
            rc.setVariance(evt.getParameter("insertvariance_softwareSales_Hidden"));	
        	rc.setCostOrder(5);
        	costsList.add(rc);
        	rc= new ReportCost();
        	rc.setReportId(reportId);
        	rc.setCostId(uuid.getUuid());
        	rc.setCostName(Application.getInstance().getMessage("project.label.others","Others"));
        	rc.setEstimated(evt.getParameter("insertEstimated_others_Hidden"));
        	rc.setActual(evt.getParameter("insertActual_others_Hidden"));
        	rc.setVariance(evt.getParameter("insertvariance_others_Hidden"));
        	rc.setCostOrder(6);
        	costsList.add(rc);
        	rc= new ReportCost();
        	rc.setReportId(reportId);
        	rc.setCostId(uuid.getUuid());
        	rc.setCostName(Application.getInstance().getMessage("project.label.total","Total"));
        	rc.setEstimated(evt.getParameter("total_Estimated_Hidden"));
        	rc.setActual(evt.getParameter("total_Actual_Hidden"));
        	rc.setVariance(evt.getParameter("total_variance_Hidden"));
        	rc.setCostOrder(7);
        	costsList.add(rc);
        	report.setCost(costsList);
        }
        wm.addReport(report);
        if (buttonClicked.equals(save.getAbsoluteName())) {
        	return new Forward("save","projectPerformaceMetricReport.jsp?projectId="+projectId,true);
        }
        else if (buttonClicked.equals(savePrint.getAbsoluteName())) {
        	return new Forward("saveprint","projectPerformaceMetricReport.jsp?projectId="+projectId+"&reportId="+reportId,true);
        }
        }catch(Exception e){
        	Log.getLog(getClass()).error(e.toString(), e);
        }
        
        return forward;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Task[] getTask() {
        return task;
    }

    public void setTask(Task[] task) {
        this.task = task;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setTotalHourSpent(double totalHourSpent) {
        this.totalHourSpent = totalHourSpent;
    }

    public double getTotalHourSpent() {
        return totalHourSpent;
    }

    public void setEstimatedHourSpent(double estimatedHourSpent) {
        this.estimatedHourSpent = estimatedHourSpent;
    }

    public double getEstimatedHourSpent() {
        return estimatedHourSpent;
    }

    public void setNoTSTask(Task[] noTSTask) {
        this.noTSTask = noTSTask;
    }

    public Task[] getNoTSTask() {
        return noTSTask;
    }

	public Collection getDefects() {
		return defects;
	}

	public void setDefects(Collection defects) {
		this.defects = defects;
	}

	public double getHourSpent() {
		return hourSpent;
	}

	public void setHourSpent(double hourSpent) {
		this.hourSpent = hourSpent;
	}

	public boolean isCostList() {
		return costList;
	}

	public void setCostList(boolean costList) {
		this.costList = costList;
	}

	public boolean isDefectList() {
		return defectList;
	}

	public void setDefectList(boolean defectList) {
		this.defectList = defectList;
	}

	public CheckBox getCost() {
		return cost;
	}

	public void setCost(CheckBox cost) {
		this.cost = cost;
	}

	public Collection getCosts() {
		return costs;
	}

	public void setCosts(Collection costs) {
		this.costs = costs;
	}

	public Collection getDefectsList() {
		return defectsList;
	}

	public void setDefectsList(Collection defectsList) {
		this.defectsList = defectsList;
	} 
	
	
	
}



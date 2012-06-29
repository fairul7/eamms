package com.tms.collab.timesheet.ui;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.timesheet.TimeSheetUtil;
import com.tms.collab.timesheet.model.TimeSheetModule;

public class TimeSheetNonProjectViewInMandaysReport extends TimeSheetNonProjectView {
    //Date filters
    private DatePopupField fromDate;
    private DatePopupField toDate;
    private Button submit;

    public void init()
    {
        super.init();
        setMethod("POST");

        fromDate = new DatePopupField("fromDate");
        toDate = new DatePopupField("toDate");
        submit = new Button("submit", Application.getInstance().getMessage("timesheet.label.submit"));

        //Initializing dates to one month before
        Calendar cal = Calendar.getInstance();
        toDate.setDate(cal.getTime());
        cal.add(Calendar.DAY_OF_YEAR, -30);
        fromDate.setDate(cal.getTime());

        addChild(fromDate);
        addChild(toDate);
        addChild(submit);
    }

	public String getDefaultTemplate()
    {
        return "timesheet/tsnonprojectviewInMandaysReport";
    }

	public void onRequest(Event ev)
    {
		super.onRequest(ev);
        calculate();
	}

    public Forward onValidate(Event event)
    {
        calculate();
        return super.onValidate(event);
    }

    protected void calculate()
    {
    	DecimalFormat format = new DecimalFormat("##0.00");
        TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
		SecurityService securityService = (SecurityService) Application.getInstance().getService(SecurityService.class);
		
        DateFormat dateStyle = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startStringDate = fromDate.getYear()+"-"+(fromDate.getMonth()+1)+"-"+fromDate.getDayOfMonth()+" 00:00:00";
        try {
			fromDate.setDate(dateStyle.parse(startStringDate));
		} catch (ParseException e) {

		}
        String endStringDate = toDate.getYear()+"-"+(toDate.getMonth()+1)+"-"+toDate.getDayOfMonth()+" 23:59:59";
        try{
        	toDate.setDate(dateStyle.parse(endStringDate));
        } catch(ParseException e){

        }



		if(task != null) {
			for(int i=0; i<task.length; i++) {
				try {
					Collection assigneeList = task[i].getAttendees();
					String[][] userList = new String[assigneeList.size()][2];
					String[] remarksList = new String[assigneeList.size()];
					String remarks = "";
					double[] totalMandaysSpentEachUser = new double[assigneeList.size()];
					double totalMandaysSpent = 0;
					double totalHoursSpent = 0;

					// Calculate total estimated manday for this task
					CalendarModule calModule = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
					CalendarEvent event = calModule.getCalendarEvent(task[i].getId());
	                Calendar start = Calendar.getInstance();
	                Calendar end = Calendar.getInstance();
	                if (event.getStartDate()!=null)
	                    start.setTime(event.getStartDate());
	                if (event.getEndDate()!=null)
	                    end.setTime(event.getEndDate());
	                

	                remarks = mod.getMandayReportRemarks(task[i].getId(), null);

	                int j=0;
	                // Calculate total hour spent by each user on this task
					for(Iterator itr=assigneeList.iterator(); itr.hasNext(); j++) {
						Assignee assignee = (Assignee)itr.next();

						try{
							userList[j][0] = securityService.getUser(assignee.getUserId()).getId();
							userList[j][1] = securityService.getUser(assignee.getUserId()).getName();
							}catch(Exception error) {
								try{
								userList[j][0] = assignee.getUserId();
								userList[j][1] = assignee.getName();
								}catch(Exception e) {
									userList[j][0] = assignee.getUserId();
									userList[j][1] = "Unknown Deleted User";	
								}
							}

						double totalHourSpentThisUser = mod.getTotalHour(assignee.getUserId(), null, task[i].getId(), fromDate.getDate(), toDate.getDate(), false);
						totalHourSpentThisUser += mod.getTotalHourAdjust(assignee.getUserId(), null, task[i].getId(), fromDate.getDate(), toDate.getDate(), true);
						totalMandaysSpentEachUser[j] = totalHourSpentThisUser/TimeSheetUtil.WORKING_HOUR_PER_DAY;

						totalHoursSpent += totalHourSpentThisUser;

						String userSpecificRemarks = mod.getMandayReportRemarks(task[i].getId(), assignee.getUserId());
						if(remarks != null) {
							remarksList[j] = userSpecificRemarks;
						}
						else {
							remarksList[j] = "";
						}
					}

					totalMandaysSpent = totalHoursSpent/TimeSheetUtil.WORKING_HOUR_PER_DAY;
					totalMandaysSpent=Double.parseDouble(format.format(totalMandaysSpent));
					task[i].setUserList(userList);
					task[i].setTotalMandaysSpentEachUser(totalMandaysSpentEachUser);
					task[i].setTotalMandaysSpent(totalMandaysSpent);
					task[i].setTotalAssignee(userList.length);
					task[i].setUserSpecificRemarks(remarksList);
					task[i].setRemarks(remarks);
				}
				catch(Exception error) {
					Log.getLog(getClass()).error("Exception caught while setting information for task " + task[i].getId() + ": " + error, error);
				}
			}
		}

		if(noTSTask != null) {
			for(int i=0; i<noTSTask.length; i++) {
				try {
					//start
					Collection assigneeList = noTSTask[i].getAttendees();
					String[][] userList = new String[assigneeList.size()][2];
					double[] totalMandaysSpentEachUser = new double[assigneeList.size()];
					double totalHoursSpent = 0;
					String[] remarksList = new String[assigneeList.size()];
					double totalMandaysSpent = 0;
					//end

					String remarks = "";

					// Calculate total estimated manday for this task
					CalendarModule calModule = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
					CalendarEvent event = calModule.getCalendarEvent(noTSTask[i].getId());
	                Calendar start = Calendar.getInstance();
	                Calendar end = Calendar.getInstance();
	                if (event.getStartDate()!=null)
	                    start.setTime(event.getStartDate());
	                if (event.getEndDate()!=null)
	                    end.setTime(event.getEndDate());
					
					//start
					 int j=0;
					for(Iterator itr=assigneeList.iterator(); itr.hasNext(); j++) {
						Assignee assignee = (Assignee)itr.next();

						try{
							userList[j][0] = securityService.getUser(assignee.getUserId()).getId();
							userList[j][1] = securityService.getUser(assignee.getUserId()).getName();
							}catch(Exception error) {
								try{
								userList[j][0] = assignee.getUserId();
								userList[j][1] = assignee.getName();
								}catch(Exception e) {
									userList[j][0] = assignee.getUserId();
									userList[j][1] = "Unknown Deleted User";	
								}
							}


						double totalHourSpentThisUser = mod.getTotalHour(assignee.getUserId(), null, noTSTask[i].getId(), fromDate.getDate(), toDate.getDate(), false);
						totalHourSpentThisUser += mod.getTotalHourAdjust(assignee.getUserId(), null, noTSTask[i].getId(), fromDate.getDate(), toDate.getDate(), true);
						totalMandaysSpentEachUser[j] = totalHourSpentThisUser/TimeSheetUtil.WORKING_HOUR_PER_DAY;
						totalHoursSpent += totalHourSpentThisUser;





   						String userSpecificRemarks = mod.getMandayReportRemarks(noTSTask[i].getId(), assignee.getUserId());
						if(remarks != null) {
							remarksList[j] = userSpecificRemarks;
						}
						else {
							remarksList[j] = "";
						}

					}

						totalMandaysSpent = totalHoursSpent/TimeSheetUtil.WORKING_HOUR_PER_DAY;
						totalMandaysSpent=Double.parseDouble(format.format(totalMandaysSpent));


				    noTSTask[i].setUserList(userList);
				    noTSTask[i].setTotalMandaysSpentEachUser(totalMandaysSpentEachUser);
					noTSTask[i].setTotalMandaysSpent(totalMandaysSpent);
					noTSTask[i].setTotalAssignee(userList.length);

					//end


					remarks = mod.getMandayReportRemarks(noTSTask[i].getId(), null);
					noTSTask[i].setRemarks(remarks);
					noTSTask[i].setUserSpecificRemarks(remarksList);

				}
				catch(Exception error) {
					Log.getLog(getClass()).error("Exception caught while setting information for no-timesheet-task " + noTSTask[i].getId() + ": " + error, error);
				}
			}
		}
    }

    public DatePopupField getFromDate()
    {
        return fromDate;
    }

    public void setFromDate(DatePopupField fromDate)
    {
        this.fromDate = fromDate;
    }

    public DatePopupField getToDate()
    {
        return toDate;
    }

    public void setToDate(DatePopupField toDate)
    {
        this.toDate = toDate;
    }

    public Button getSubmit()
    {
        return submit;
    }

    public void setSubmit(Button submit)
    {
        this.submit = submit;
    }
}

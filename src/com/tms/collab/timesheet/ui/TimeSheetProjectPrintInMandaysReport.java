package com.tms.collab.timesheet.ui;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.ui.Event;
import kacang.util.Log;

import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.project.WormsUtil;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.timesheet.TimeSheetUtil;
import com.tms.collab.timesheet.model.TimeSheetModule;

public class TimeSheetProjectPrintInMandaysReport extends TimeSheetProjectView {
	private String checkedItemsString = "";

    private Date start;
    private Date end;

	private String startDate = "";
    private String endDate = "";

	public String getDefaultTemplate() {
        return "timesheet/tsprojectprintInMandaysReport";
    }

	public void onRequest(Event ev) {
		super.onRequest(ev);
		DecimalFormat format = new DecimalFormat("##0.00");
		DateFormat dateStyle = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		startDate +=" 00:00:00";
		endDate +=" 23:59:59";

        //startDate = startDate.replaceAll("-", "/");
        //endDate = endDate.replaceAll("-", "/");
        //Setting date range

		/*Calendar rangeStart = Calendar.getInstance();
        rangeStart.add(Calendar.DAY_OF_YEAR, -30);
        Calendar rangeEnd = Calendar.getInstance();
       */
        if(!(endDate == null || "".equals(endDate)))
        {
        	//rangeEnd.setTime(new Date(endDate));
              try {

				//rangeEnd.setTime(dateStyle.parse(endDate));
			     end= dateStyle.parse(endDate);
              } catch (ParseException e) {
                end = new Date();
			}
        }
        if(!(startDate == null || "".equals(startDate)))
        { // rangeStart.setTime(new Date(startDate));
            try{

            	//rangeStart.setTime(dateStyle.parse(startDate));
            	start = dateStyle.parse(startDate);
            }
            catch(ParseException e){
            	start = new Date();
            }
        }
        //Render purposes
       // start = rangeStart.getTime();
       // end = rangeEnd.getTime();

        System.out.println("start"+start+"end"+end);

		TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
		SecurityService securityService = (SecurityService) Application.getInstance().getService(SecurityService.class);

		/*
		checkedItemsString is composed in the form of
		|task1>user1>user3|task2|task4>user2
		which denotes printing of
			task1
				user1
				user3
			task2
			task4
				user2
		*/
		StringTokenizer tokenizedTasks = new StringTokenizer(checkedItemsString, "|");
		// Each element in selectedTasksMap has pair of {taskId, taskElementId}
		HashMap selectedTasksMap = new HashMap();
		// Each element in selectedUsersInEachTask has pair of {userId, taskElementId}
		// The array index of selectedUsersInEachTask also indicates the corresponding taskElementId
		int totalSize = 0;
		if(task != null) {
			totalSize += task.length;
		}
		if(noTSTask != null) {
			totalSize += noTSTask.length;
		}
		HashMap[] selectedUsersInEachTask = new HashMap[totalSize];

		for(int i=0; tokenizedTasks.hasMoreTokens(); i++) {
			String rawSingleTaskStr = tokenizedTasks.nextToken();
			// Example of rawSingleTaskStr: task1>user1>user3
			if(!"".equals(rawSingleTaskStr)) {
				StringTokenizer tokenizedTaskElements = new StringTokenizer(rawSingleTaskStr, ">");
				HashMap temp = null;
				for(int j=0; tokenizedTaskElements.hasMoreTokens(); j++) {
					String singleTaskElement = tokenizedTaskElements.nextToken();
					// Example of singleTaskElement: task1 if j=0, user1 if j=1
					if(!"".equals(singleTaskElement)) {
						if(j==0) {
							selectedTasksMap.put(singleTaskElement, String.valueOf(i));
						}
						else {
							if(temp == null) {
								temp = new HashMap();
								temp.put(singleTaskElement, String.valueOf(i));
							}
							else {
								temp.put(singleTaskElement, String.valueOf(i));
							}
						}
					}
					else {
						j--;
					}
				}
				selectedUsersInEachTask[i] = temp;
			}
			else {
				i--;
			}
		}

		if(task != null) {
			for(int i=0; i<task.length; i++) {
				if(selectedTasksMap.containsKey(task[i].getId())) {
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
		                Calendar calStart = Calendar.getInstance();
		                Calendar calEnd = Calendar.getInstance();
		                if (event.getStartDate()!=null)
		                    calStart.setTime(event.getStartDate());
		                if (event.getEndDate()!=null)
		                    calEnd.setTime(event.getEndDate());

		                remarks = mod.getMandayReportRemarks(task[i].getId(), null);

		                int j=0;
		                // Calculate total hour spent by each user on this task
						for(Iterator itr=assigneeList.iterator(); itr.hasNext(); j++) {
							Assignee assignee = (Assignee)itr.next();
							int taskElementIdInMap = Integer.parseInt(selectedTasksMap.get(task[i].getId()).toString());
							if(selectedUsersInEachTask[taskElementIdInMap] != null) {
								if(selectedUsersInEachTask[taskElementIdInMap].containsKey(assignee.getUserId())) {
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
								}
								double totalHourSpentThisUser = mod.getTotalHour(assignee.getUserId(), null, task[i].getId(), start, end, false);
								totalHourSpentThisUser += mod.getTotalHourAdjust(assignee.getUserId(), null, task[i].getId(), start, end, true);
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
							else {
								double totalHourSpentThisUser = mod.getTotalHour(assignee.getUserId(), null, task[i].getId(), start, end, false);
								totalHourSpentThisUser += mod.getTotalHourAdjust(assignee.getUserId(), null, task[i].getId(), start, end, true);
								totalHoursSpent += totalHourSpentThisUser;
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
				else {
					task[i] = null;
				}
			}
		}

		if(noTSTask != null) {

			for(int i=0; i<noTSTask.length; i++) {
				if(selectedTasksMap.containsKey(noTSTask[i].getId())) {
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
		                Calendar calStart = Calendar.getInstance();
		                Calendar calEnd = Calendar.getInstance();
		                if (event.getStartDate()!=null)
		                    calStart.setTime(event.getStartDate());
		                if (event.getEndDate()!=null)
		                    calEnd.setTime(event.getEndDate());

//						start
						 int j=0;

						for(Iterator itr=assigneeList.iterator(); itr.hasNext(); j++) {
							Assignee assignee = (Assignee)itr.next();

							int taskElementIdInMap = Integer.parseInt(selectedTasksMap.get(noTSTask[i].getId()).toString());
							if(selectedUsersInEachTask[taskElementIdInMap] != null) {
								if(selectedUsersInEachTask[taskElementIdInMap].containsKey(assignee.getUserId())) {

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

								}

							double totalHourSpentThisUser = mod.getTotalHour(assignee.getUserId(), null, noTSTask[i].getId(), start, end, false);
							totalHourSpentThisUser += mod.getTotalHourAdjust(assignee.getUserId(), null, noTSTask[i].getId(), start, end, true);
							totalMandaysSpentEachUser[j] = totalHourSpentThisUser/TimeSheetUtil.WORKING_HOUR_PER_DAY;
							totalHoursSpent += totalHourSpentThisUser;






	   						String userSpecificRemarks = mod.getMandayReportRemarks(noTSTask[i].getId(), assignee.getUserId());
							System.out.println("userREmarks=="+userSpecificRemarks);
	   						if(remarks != null) {
								remarksList[j] = userSpecificRemarks;
							}
							else {
								remarksList[j] = "";
							}
						}

							else {
								double totalHourSpentThisUser = mod.getTotalHour(assignee.getUserId(), null, noTSTask[i].getId(), start, end, false);
								totalHourSpentThisUser += mod.getTotalHourAdjust(assignee.getUserId(), null, noTSTask[i].getId(), start, end, true);
								totalHoursSpent += totalHourSpentThisUser;
							}
						}
							totalMandaysSpent = totalHoursSpent/TimeSheetUtil.WORKING_HOUR_PER_DAY;
							totalMandaysSpent=Double.parseDouble(format.format(totalMandaysSpent));


					    noTSTask[i].setUserList(userList);
					    noTSTask[i].setTotalMandaysSpentEachUser(totalMandaysSpentEachUser);
						noTSTask[i].setTotalMandaysSpent(totalMandaysSpent);
						noTSTask[i].setTotalAssignee(userList.length);
						noTSTask[i].setUserSpecificRemarks(remarksList);
						//end

						remarks = mod.getMandayReportRemarks(noTSTask[i].getId(), null);
						noTSTask[i].setRemarks(remarks);
					}
					catch(Exception error) {
						Log.getLog(getClass()).error("Exception caught while setting information for no-timesheet-task " + noTSTask[i].getId() + ": " + error, error);
					}
				}
				else {
					noTSTask[i] = null;
				}
			}
		}
	}

	public String getCheckedItemsString() {
		return checkedItemsString;
	}

	public void setCheckedItemsString(String checkedItemsString) {
		this.checkedItemsString = checkedItemsString;
	}

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    public Date getStart()
    {
        return start;
    }

    public void setStart(Date start)
    {
        this.start = start;
    }

    public Date getEnd()
    {
        return end;
    }

    public void setEnd(Date end)
    {
        this.end = end;
    }
}

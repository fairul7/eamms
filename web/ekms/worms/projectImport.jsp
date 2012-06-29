<%@ page import="kacang.ui.WidgetManager,
				 kacang.stdui.Form,
				 kacang.stdui.FileUpload,
				 com.tapsterrock.mpp.MPPFile,
				 com.tapsterrock.mpx.Task,
				 com.tms.collab.project.Project,
				 kacang.util.UuidGenerator,
				 com.tms.collab.project.Milestone,
				 com.tms.collab.taskmanager.model.Assignee,
				 com.tms.collab.calendar.model.CalendarModule,
				 java.util.*,
				 com.tms.collab.project.WormsHandler,
				 com.tms.collab.taskmanager.model.TaskManager,
				 com.tapsterrock.mpx.MPXDuration,
				 com.tapsterrock.mpx.ResourceAssignment,
				 kacang.services.security.SecurityException,
				 kacang.util.Log,
				 com.tms.collab.project.WormsUtil"%>
<%@ page import="kacang.model.DaoQuery"%>
<%@ page import="kacang.model.operator.OperatorEquals"%>
<%@ page import="kacang.model.operator.DaoOperator"%>
<%@ page import="com.tms.collab.taskmanager.model.TaskCategory"%>
<%@ page import="com.tms.collab.messaging.model.*"%>
<%@ page import="com.tms.collab.calendar.model.Attendee"%>
<%@ page import="com.tms.collab.calendar.ui.UserUtil"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.tms.util.FormatUtil"%>
<%@ include file="/common/header.jsp" %>
<%!
	public com.tms.collab.taskmanager.model.Task generateTask(String title, User user, Collection assignments, Date startDate, Date endDate)
	{
		/* Forumulating attendee */
		Collection attendeeList = new TreeSet();
		Collection workingDay = new ArrayList();
		workingDay.add("2");
		workingDay.add("3");
		workingDay.add("4");
		workingDay.add("5");
		workingDay.add("6");
        if((assignments != null) && (assignments.size() > 0))
		{
			/* Assigning Resources */
			SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            boolean bHasAssignee=false;
            for (Iterator ires = assignments.iterator(); ires.hasNext();)
			{
				ResourceAssignment assignee = (ResourceAssignment) ires.next();
				try
				{
					Collection list = service.getUsersByUsername(assignee.getResource().getName());
					if(list.size() > 0)
					{
						User obj = (User) list.iterator().next();
						Assignee att = new Assignee();
						att.setUserId(obj.getId());
						att.setProperty("username", obj.getUsername());
						att.setProperty("firstName", obj.getProperty("firstName"));
						att.setProperty("lastName", obj.getProperty("lastName"));
						att.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
						att.setProgress(0);
						att.setTaskStatus(Assignee.TASK_STATUS_NOT_STARTED);
						attendeeList.add(att);
                        bHasAssignee=true;
                    }
				}
				catch (SecurityException e)
				{
					Log.getLog(getClass()).error("Error while attempting to retrieve user from project resource", e);
				}
			}

            if (!bHasAssignee) {
                Assignee att = new Assignee();
                att.setUserId(user.getId());
                att.setProperty("username", user.getUsername());
                att.setProperty("firstName", user.getProperty("firstName"));
                att.setProperty("lastName", user.getProperty("lastName"));
                att.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
                att.setProgress(0);
                att.setTaskStatus(Assignee.TASK_STATUS_NOT_STARTED);
                attendeeList.add(att);
            }
        }
        else
		{
			Assignee att = new Assignee();
			att.setUserId(user.getId());
			att.setProperty("username", user.getUsername());
			att.setProperty("firstName", user.getProperty("firstName"));
			att.setProperty("lastName", user.getProperty("lastName"));
			att.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
			att.setProgress(0);
			att.setTaskStatus(Assignee.TASK_STATUS_NOT_STARTED);
			attendeeList.add(att);
		}


        /* Assembling object */
		com.tms.collab.taskmanager.model.Task task = new com.tms.collab.taskmanager.model.Task();
		task.setId(com.tms.collab.taskmanager.model.Task.class.getName() + "_" + UuidGenerator.getInstance().getUuid());
		task.setEventId(task.getId());
		task.setCompleted(false);
		task.setDescription(title);
        task.setDueDate(endDate);
        task.setStartDate(startDate);
        task.setUserId(user.getId());
        task.setAssigner(user.getName());
        task.setAssignerId(user.getId());
        task.setCategoryId("com.tms.collab.taskmanager.category.general");
		task.setReassign(false);
		task.setClassification(CalendarModule.CLASSIFICATION_PUBLIC);
        task.setCreationDate(new Date());
        task.setEndDate(endDate);
        task.setTitle(title);
        task.setAttendees(attendeeList);
        int totalMandaysEstimated = WormsUtil.getWorkingDays(workingDay, startDate, endDate);
		task.setEstimation(totalMandaysEstimated);
		task.setEstimationType("Mandays");
		return task;
	}

	public Milestone generateMilestone(String name, String projectId)
	{
		Milestone milestone = new Milestone();
		milestone.setMilestoneId(UuidGenerator.getInstance().getUuid());
		milestone.setProjectId(projectId);
		milestone.setMilestoneName(name);
		milestone.setMilestoneProgress(0);
		milestone.setMilestoneDescription("");
		milestone.setMeetings(new ArrayList());
		return milestone;
	}

    public void sendNotification(com.tms.collab.taskmanager.model.Task t, User user, HttpServletRequest req) {
        try{
            String category="";
            if (t.getCategory()==null || t.getCategory().equals("")) {
                TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                TaskCategory cat = tm.getCategory(t.getCategoryId());
                category=cat.getName();
            }
            else{
                category=t.getCategory();
            }
            MessagingModule mm;
            SmtpAccount smtpAccount;
            Message message;

            mm = Util.getMessagingModule();
            String userId = user.getId();
            smtpAccount = mm.getSmtpAccountByUserId(user.getId());

            // construct the message to send
            message = new Message();
            message.setMessageId(UuidGenerator.getInstance().getUuid());
            // setMessageProperties(message,event,newEvent,evt);
            IntranetAccount intranetAccount;

            Application app = Application.getInstance();
            intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(user.getId());
            message.setFrom(intranetAccount.getFromAddress());

            Collection col = t.getAttendees();
            List memoList,emailList;
            memoList = new ArrayList(col.size());
            emailList = new ArrayList(col.size());
            for(Iterator i=col.iterator();i.hasNext();){
                Attendee att = (Attendee)i.next();
                if(!userId.equals(att.getUserId())){
                    intranetAccount = mm.getIntranetAccountByUserId(att.getUserId());
                    if(intranetAccount!=null){
                        String add = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
                        memoList.add(add);
                    }
                }
            }

            if(memoList.size()>0)
                message.setToIntranetList(memoList);
            message.setSubject(app.getMessage("taskmanager.label.newTask", "New Task") + ": " + t.getTitle());
            String temp = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">"+
                    "</head><body><style>.contentBgColorMail {background-color: #FFFFFF; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt}.contentFont {font-family: Arial, Helvetica, sans-serif; font-size: 8.5pt}"+
                    ".contentTitleFont {font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 7.5pt; color: #FFFFFF; background-color: #003366}"+
                    ".contentStrapColor {background-color: #E6E6CA; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt}</style>"+
                    "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr valign=\"MIDDLE\">"+
                    "<td height=\"22\" bgcolor=\"#003366\" class=\"contentBgColorMail\"><b><font color=\"#000000\" class=\"contentBgColorMail\">"+
                    "<b><U>To Do Task Details</U></b></font></b></td><td align=\"right\" bgcolor=\"#003366\" class=\"contentBgColorMail\">&nbsp;</td>"+
                    "</tr><tr><td colspan=\"2\" valign=\"TOP\" bgcolor=\"#EFEFEF\" class=\"contentBgColorMail\">&nbsp;</td>"+
                    "</tr><tr><td colspan=\"2\" valign=\"TOP\"><table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"1\">"+
                    "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>"+
                    "<b>" + app.getMessage("taskmanager.label.title", "Title") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">"+ t.getTitle()+"</td></tr>"+
                    "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>"+
                    "<b>" + app.getMessage("taskmanager.label.category", "Category") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">"+ category+"</td></tr>"+
                    "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>"+
                    "<b>" + app.getMessage("taskmanager.label.AssignedBy", "Assigned By") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">"+ t.getUserName()+"</td></tr>"+
                    "<tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("taskmanager.label.dueDate", "Due Date") + "</b></td>"+
                    "<td class=\"contentBgColorMail\">" + new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()).format(t.getDueDate())+"</td>"+
                    "</tr><tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("taskmanager.label.description", "Description") + "</b></td><td class=\"contentBgColorMail\">"+
                    t.getDescription() + "</td></tr>";
            String notes = "";
            if(notes!=null&&notes.trim().length()>0){
                temp += "<tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("taskmanager.label.notes", "Notes") + "</b></td>"+
                        "<td class=\"contentBgColorMail\">" + notes + "</td></tr>";

            }

            String footer = "</table></td></tr><tr><td colspan=\"2\" valign=\"TOP\" bgcolor=\"#CCCCCC\" class=\"contentBgColorMail\">&nbsp;</td>"+
                    "</tr></table><p>&nbsp; </p></body></html>";



            String link = "<tr><td></td><td class=\"contentBgColorMail\" align=\"left\" valign=\"top\"  nowrap><a href=\"http://"+ req.getServerName()+":"+req.getServerPort()+req.getContextPath()+
                    "/ekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId="+t.getId()+"\">" + app.getMessage("taskmanager.label.clickToView", "Click here to view") + "</a>"+
                    "</td></tr>";
            message.setBody(temp+link+footer);
            message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
            message.setDate(new Date());
            mm.sendMessage(smtpAccount, message, user.getId(), false);

        }catch (Exception e){
            Log.getLog(getClass()).error(e);
        }
    }
%>
<%
	/* Processing Form Action */
	if("Upload File".equals(request.getParameter("button*upload.form.action")))
	{
		WidgetManager manager = WidgetManager.getWidgetManager(request);
		WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
		TaskManager tManager = (TaskManager) Application.getInstance().getModule(TaskManager.class);
		CalendarModule module = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
		Form form = (Form) manager.getWidget("upload.form");
		FileUpload upload = (FileUpload) form.getChild("file");

		if(!(upload.getValue() == null || "".equals(upload.getValue())))
		{
			/* Building Microsoft Project DOM */
			try
			{
				MPPFile file = new MPPFile(upload.getInputStream(request));

				/* Formulating Project Object */
				User user = manager.getUser();
				Project project = new Project();

				project.setProjectId(UuidGenerator.getInstance().getUuid());
				project.setProjectName(file.getProjectHeader().getProjectTitle());
				project.setProjectDescription("");
				project.setProjectCategory("Imported");
				project.setOwner(user);
				project.setFiles(new ArrayList());
				project.setRoles(new ArrayList());
				project.setArchived(false);
                project.setProjectWorkingDays("2,3,4,5,6");
                handler.addProject(project);

                // create general milestone
                Milestone generalMilestone = generateMilestone("General",project.getProjectId());
                handler.addMilestone(generalMilestone);

                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorEquals("name", project.getProjectName(), DaoOperator.OPERATOR_AND));
                Collection list = tManager.getCategories(query, 0, 1, null, false);
                TaskCategory category = new TaskCategory();
                category.setId(TaskManager.DEFAULT_CATEGORY_ID);
                if(list.size() > 0)
                    {
                        category = (TaskCategory) list.iterator().next();
                    }

                Task superParent = null;
                for (Iterator i= file.getAllTasks().iterator(); i.hasNext();) {
                    Task tempTask = (Task)i.next();
                    if (tempTask.getUniqueID().equals(new Integer("0"))) {
                        superParent = tempTask; //tempTask;
                        break;
                    }
                }

                Task parent = null;
                boolean hasParent=false;
                for (Iterator i= file.getAllTasks().iterator(); i.hasNext();) {
                    Task tempTask = (Task)i.next();
                    if (tempTask.getName().equals(superParent.getName()) && !tempTask.getUniqueID().equals(superParent.getUniqueID())) {
                        parent = tempTask; //tempTask;
                        hasParent = true;
                        break;
                    }
                }

                if (!hasParent) {
                    parent = superParent;
                }

                boolean hasGeneralTask = false;

                /* Extracting Nodes */
				for (Iterator i = file.getAllTasks().iterator(); i.hasNext();)
				{
					Task obj = (Task) i.next();
					if(obj.getID().intValue() > 0)
					{
						if(obj.getChildTasks().size() > 0)
						{
                            boolean isMilestone=true;
                            for (Iterator it = obj.getChildTasks().iterator(); it.hasNext();)
                            {
                                Task task = (Task) it.next();
                                if (task.getID().intValue()>0 && task.getChildTasks().size()>0) {
                                    isMilestone=false;
                                }
                            }

                            if (isMilestone) {

                            Milestone milestone = generateMilestone(obj.getName(), project.getProjectId());
							handler.addMilestone(milestone);

							/* Looping children tasks */
							for (Iterator it = obj.getChildTasks().iterator(); it.hasNext();)
							{
								Task task = (Task) it.next();
								MPXDuration duration = task.getDuration();
								//if(!task.getMilestone().booleanValue())
								{
									Calendar startDate = Calendar.getInstance();
									Calendar endDate = Calendar.getInstance();

                                    if(!task.getMilestone().booleanValue()) {
                                    startDate.setTime(task.getStart());
									startDate.set(Calendar.HOUR, startDate.getMinimum(Calendar.HOUR));
									startDate.set(Calendar.MINUTE, startDate.getMinimum(Calendar.MINUTE));
									startDate.set(Calendar.SECOND, startDate.getMinimum(Calendar.SECOND));

									endDate.setTime(task.getStart());
									endDate.add(Calendar.DAY_OF_YEAR, (new Double(duration.getDuration()).intValue() - 1));
									endDate.set(Calendar.HOUR, endDate.getMaximum(Calendar.HOUR));
									endDate.set(Calendar.MINUTE, endDate.getMaximum(Calendar.MINUTE));
									endDate.set(Calendar.SECOND, endDate.getMaximum(Calendar.SECOND));
                                    }

                                    else {
                                        startDate.setTime(task.getStart());
                                        startDate.set(Calendar.HOUR, startDate.getMinimum(Calendar.HOUR));
                                        startDate.set(Calendar.MINUTE, startDate.getMinimum(Calendar.MINUTE));
                                        startDate.set(Calendar.SECOND, startDate.getMinimum(Calendar.SECOND));

                                        endDate.setTime(task.getStart());
                                        endDate.add(Calendar.DAY_OF_YEAR, (new Double(1.0).intValue() - 1));
                                        endDate.set(Calendar.HOUR, endDate.getMaximum(Calendar.HOUR));
                                        endDate.set(Calendar.MINUTE, endDate.getMaximum(Calendar.MINUTE));
                                        endDate.set(Calendar.SECOND, endDate.getMaximum(Calendar.SECOND));
                                    }
                                    com.tms.collab.taskmanager.model.Task ekpTask = generateTask(task.getName(), user, task.getResourceAssignments(), startDate.getTime(), endDate.getTime());
                                    ekpTask.setCategoryId(category.getId());
                                    module.addCalendarEvent(ekpTask.getClass().getName(), ekpTask, ekpTask.getUserId(),true);
									tManager.addTask(ekpTask);
									handler.addMilestoneTask(milestone.getMilestoneId(), ekpTask.getId());
                                    sendNotification(ekpTask,user,request);
                                }
							}
                            }

                            else {

                                // not a milestone
                                if (obj.getParentTask().getUniqueID().equals(parent.getUniqueID())) {

                                    MPXDuration duration = obj.getDuration();
                                    // add to general milestone
                                    Calendar startDate = Calendar.getInstance();
                                    Calendar endDate = Calendar.getInstance();

                                    startDate.setTime(obj.getStart());
                                    startDate.set(Calendar.HOUR, startDate.getMinimum(Calendar.HOUR));
                                    startDate.set(Calendar.MINUTE, startDate.getMinimum(Calendar.MINUTE));
                                    startDate.set(Calendar.SECOND, startDate.getMinimum(Calendar.SECOND));

                                    endDate.setTime(obj.getStart());
                                    if(!obj.getMilestone().booleanValue())
                                        endDate.add(Calendar.DAY_OF_YEAR, (new Double(duration.getDuration()).intValue() - 1));
                                    else
                                        endDate.add(Calendar.DAY_OF_YEAR, (new Double(1.0).intValue() - 1));
                                    endDate.set(Calendar.HOUR, endDate.getMaximum(Calendar.HOUR));
                                    endDate.set(Calendar.MINUTE, endDate.getMaximum(Calendar.MINUTE));
                                    endDate.set(Calendar.SECOND, endDate.getMaximum(Calendar.SECOND));

                                    com.tms.collab.taskmanager.model.Task ekpTask = generateTask(obj.getName(), user, obj.getResourceAssignments(), startDate.getTime(), endDate.getTime());
                                    ekpTask.setCategory(category.getId());
                                    module.addCalendarEvent(ekpTask.getClass().getName(), ekpTask, ekpTask.getUserId(),true);
                                    tManager.addTask(ekpTask);
                                    handler.addMilestoneTask(generalMilestone.getMilestoneId(), ekpTask.getId());
                                    hasGeneralTask=true;
                                }
                            }

                        }
                        else {
                            // not a milestone
                            if (obj.getParentTask().getUniqueID().equals(parent.getUniqueID())) {

                                MPXDuration duration = obj.getDuration();
                                // add to general milestone
                                Calendar startDate = Calendar.getInstance();
                                Calendar endDate = Calendar.getInstance();

                                startDate.setTime(obj.getStart());
                                startDate.set(Calendar.HOUR, startDate.getMinimum(Calendar.HOUR));
                                startDate.set(Calendar.MINUTE, startDate.getMinimum(Calendar.MINUTE));
                                startDate.set(Calendar.SECOND, startDate.getMinimum(Calendar.SECOND));

                                endDate.setTime(obj.getStart());
                                if(!obj.getMilestone().booleanValue())
                                    endDate.add(Calendar.DAY_OF_YEAR, (new Double(duration.getDuration()).intValue() - 1));
                                else
                                    endDate.add(Calendar.DAY_OF_YEAR, (new Double(1.0).intValue() - 1));
                                
                                endDate.set(Calendar.HOUR, endDate.getMaximum(Calendar.HOUR));
                                endDate.set(Calendar.MINUTE, endDate.getMaximum(Calendar.MINUTE));
                                endDate.set(Calendar.SECOND, endDate.getMaximum(Calendar.SECOND));

                                com.tms.collab.taskmanager.model.Task ekpTask = generateTask(obj.getName(), user, obj.getResourceAssignments(), startDate.getTime(), endDate.getTime());
                                ekpTask.setCategory(category.getId());
                                module.addCalendarEvent(ekpTask.getClass().getName(), ekpTask, ekpTask.getUserId(),true);
                                tManager.addTask(ekpTask);
                                handler.addMilestoneTask(generalMilestone.getMilestoneId(), ekpTask.getId());
                                hasGeneralTask=true;
                            }

                        }
                    }
				}

                if (!hasGeneralTask)
                    handler.deleteMilestone(generalMilestone.getMilestoneId());
            %>
			<script>
				alert("Project Uploaded");
				document.location="/ekms/worms/projectOpen.jsp?projectId=<%= project.getProjectId() %>";
			</script>
			<%
			}
			catch(Exception e)
			{
			%>
			<script>alert("Error Processing File. Please Ensure That A Valid Microsoft Project File Is Uploaded");</script>
			<%
			}
		}
		else
		{
			%>
			<script>
				alert("Please Upload A Project File");
			</script>
			<%
		}
	}
%>
<x:permission permission="com.tms.worms.project.Project.add" module="com.tms.collab.project.WormsHandler" url="/ekms/index.jsp"/>
<x:config>
	<page name="upload">
		<form name="form" columns="2">
			<label name="label" text="Microsoft Project File (.mpp)"/>
			<fileupload name="file"/>
			<label name="empty" text=""/>
			<button name="action" text="Upload File"/>
		</form>
	</page>
</x:config>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='project.label.projects'/> > <fmt:message key='project.label.uploadMPPFile'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
	<tr>
		<td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
			<table cellpadding="2" cellspacing="1" width="90%" align="center">
				<tr><td><x:display name="upload"/></td></tr>
				<tr><td>&nbsp;</td></tr>
			</table>
		</td>
	</tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
</table>
<jsp:include page="includes/footer.jsp"/>
<%@ include file="/ekms/includes/footer.jsp" %>

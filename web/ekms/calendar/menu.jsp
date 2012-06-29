<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 com.tms.collab.calendar.model.CalendarModule,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 com.tms.collab.timesheet.TimeSheetUtil,
                 com.tms.collab.timesheet.model.TimeSheetModule,
                 com.tms.collab.project.WormsHandler,
                 java.util.Collection"%>
<%@ include file="/common/header.jsp"%>
<c:set var="dailyUrl">
    <c:url value="/ekms/calendar/calendar.jsp" />?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=calendarPage.calendarView&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=view&view=daily
</c:set>
<c:set var="weeklyUrl">
    <c:url value="/ekms/calendar/calendar.jsp" />?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=calendarPage.calendarView&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=view&view=weekly
</c:set>
<c:set var="monthlyUrl">
    <c:url value="/ekms/calendar/calendar.jsp" />?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=calendarPage.calendarView&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=view&view=monthly
</c:set>
<%
    Application app = Application.getInstance();
    SecurityService service = (SecurityService) app.getService(SecurityService.class);
    String userId = service.getCurrentUser(request).getId();
    ArrayList items = new ArrayList();
    //Determine permissions
    boolean calendarView = service.hasPermission(userId, CalendarModule.PERMISSION_CALENDARING, null, null);
    if (calendarView) {
        //Generating calendar menu
        items.add(new MenuItem(app.getMessage("calendar.label.calendar"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("calendar.label.newAppointment"), "/ekms/calendar/appointmentform.jsp?init=1", null, null, null, null));
        items.add(new MenuItem(app.getMessage("calendar.label.newEvent"), "/ekms/calendar/eventform.jsp?init=1", null, null, null, null));
        items.add(new MenuItem(app.getMessage("calendar.label.newMeeting"), "/ekms/calendar/emeetingform.jsp?init=1", null, null, null, null));
        items.add(new MenuItem(app.getMessage("calendar.label.dailyView"), (String)pageContext.findAttribute("dailyUrl"), null, null, null, null));
        items.add(new MenuItem(app.getMessage("calendar.label.weeklyView"), (String)pageContext.findAttribute("weeklyUrl"), null, null, null, null));
        items.add(new MenuItem(app.getMessage("calendar.label.monthlyView"), (String)pageContext.findAttribute("monthlyUrl"), null, null, null, null));
        items.add(new MenuItem(app.getMessage("calendar.label.listingView"), "/ekms/calendar/calendarEventList.jsp", null, null, null, null));
        //Generating task menu
        items.add(new MenuItem(app.getMessage("calendar.label.taskManager"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("calendar.label.newTask"), "/ekms/calendar/todotaskform.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("calendar.label.viewAllTasks"), "/ekms/taskmanager/taskmanager.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("calendar.label.newCategory"), "/ekms/taskmanager/taskcatform.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("calendar.label.categoryListing"), "/ekms/taskmanager/taskcategory.jsp", null, null, null, null));
    }

    // add in for timesheet module
    boolean isTimeSheet = false;
    boolean viewTimeSheet = false;
    boolean projectTimeSheet = false;
    boolean nonProjectTimeSheet = false;
    String ts = Application.getInstance().getProperty("com.tms.collab.timesheet");
    if (ts!=null && ts.equals("true")) {
        isTimeSheet = true;
        viewTimeSheet = service.hasPermission(userId,TimeSheetUtil.TIMESHEET_PERMISSION,TimeSheetModule.class.getName(),null);
        if (!viewTimeSheet) {
            try {
                WormsHandler handler = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);
                Collection col = handler.getProjectsByOwner(userId);
                Collection col2 = handler.getNonProjectsByOwner(userId);
                if (col!=null && col.size()>0) {
                	projectTimeSheet=true;
                }
                if (col2!=null && col2.size()>0) {
                	nonProjectTimeSheet=true;
                }
            }
            catch(Exception e) {

            }
        }
    }
    if (isTimeSheet) {
        items.add(new MenuItem(app.getMessage("timesheet.label.myTimesheet"),null,null,null,null,null));
        items.add(new MenuItem(app.getMessage("timesheet.menu.add"),"/ekms/timesheet/TimeSheetPForm.jsp",null,null,null,null));
        items.add(new MenuItem(app.getMessage("timesheet.menu.my"),"/ekms/timesheet/TimeSheetViewForm.jsp",null,null,null,null));
        items.add(new MenuItem(app.getMessage("timesheet.menu.viewByDate"),"/ekms/timesheet/TimeSheetTableView.jsp",null,null,null,null));
        if (viewTimeSheet||projectTimeSheet||nonProjectTimeSheet) {
        	items.add(new MenuItem(app.getMessage("timesheet.label.tsmanager"),null,null,null,null,null));
        	if (viewTimeSheet||projectTimeSheet) {
            items.add(new MenuItem(app.getMessage("timesheet.menu.projectTask"),"/ekms/timesheet/TimeSheetVPForm.jsp",null,null,null,null));
        	}if (viewTimeSheet||nonProjectTimeSheet) {
            items.add(new MenuItem(app.getMessage("timesheet.menu.nonprojectTask"),"/ekms/timesheet/TimeSheetVNPForm.jsp",null,null,null,null));
        	}if (viewTimeSheet||projectTimeSheet) {
            items.add(new MenuItem(app.getMessage("timesheet.menu.viewmonthlyreport"),"/ekms/timesheet/timesheetVMRForm.jsp",null,null,null,null));
        	}
        }
    }
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>
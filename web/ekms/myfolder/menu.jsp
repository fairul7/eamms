<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 com.tms.collab.myfolder.model.MyFolderModule,
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

//	boolean quotaView = service.hasPermission(userId, MyFolderModule.PERMISSION_QUOTA, null, null);
	boolean delUserFolderView = service.hasPermission(userId, MyFolderModule.PERMISSION_DEL_USER_FOLDER, null, null);
	
	items.add(new MenuItem(app.getMessage("mf.label.myFolder"), null, null, null, null, null));
	items.add(new MenuItem(app.getMessage("mf.label.myFolder"), "/ekms/myfolder/myFolder.jsp", null, null, null, null));
	items.add(new MenuItem(app.getMessage("mf.label.createFolder"), "/ekms/myfolder/createFolder.jsp", null, null, null, null));
	items.add(new MenuItem(app.getMessage("mf.label.sharedFolder"), "/ekms/myfolder/sharedFolder.jsp", null, null, null, null));
	
	
//	if(delUserFolderView || quotaView){
//		items.add(new MenuItem(app.getMessage("mf.label.myfolder.admin"), null, null, null, null, null));
//	}
	
	if(delUserFolderView){
		items.add(new MenuItem(app.getMessage("mf.label.deletedUserFolder"), "/ekms/myfolder/deletedUserFolder.jsp", null, null, null, null));
	}
	
//	if(quotaView){
//		items.add(new MenuItem(app.getMessage("mf.label.mfQuota"), "/ekms/myfolder/mfQuota.jsp", null, null, null, null));
//	}
	
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>
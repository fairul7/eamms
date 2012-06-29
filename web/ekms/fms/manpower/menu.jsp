<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 java.util.Collection,
                 java.util.Iterator,
                 kacang.model.DaoQuery"%>
<%@ include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();

    items.add(new MenuItem(app.getMessage("fms.manpower.menu.manpower"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.manpower.menu.setup"), "/ekms/fms/manpower/holidayLeaveSetup.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.manpower.menu.leaveTypeSetup"), "/ekms/fms/manpower/leaveType.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.manpower.menu.leaveTypeListing"), "/ekms/fms/manpower/leaveTypeView.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.manpower.menu.setup.leaveListing"), "/ekms/fms/manpower/leaveView.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.manpower.menu.setup.holidayListing"), "/ekms/fms/manpower/holidayView.jsp", null, null, null, null));
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>
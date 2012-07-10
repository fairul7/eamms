<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 java.util.Collection,
                 java.util.Iterator,
                 kacang.model.DaoQuery "%>
<%@ include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
%>

<%
	items.add(new MenuItem(app.getMessage("com.tms.workflow.myTask"), "index.jsp?addr=myTask", null, null, null, null));
	items.add(new MenuItem(app.getMessage("com.tms.workflow.staffWorkLoad"), "index.jsp?addr=workLoad", null, null, null, null));
%>

<%
    boolean woSubmit = service.hasPermission(userId, "com.tms.workflow.workOrder.permission.woSubmit", null, null);
	boolean woAssigned = service.hasPermission(userId, "com.tms.workflow.workOrder.permission.woAssigned", null, null);
	boolean woAll = service.hasPermission(userId, "com.tms.workflow.workOrder.permission.woAll", null, null);

	if(woSubmit || woAssigned || woAll)
	{
	    items.add(new MenuItem(app.getMessage("com.tms.workflow.workOrder.wo"), null, null, null, null, null));
	}
	
	if(woSubmit)
	{
	    items.add(new MenuItem(app.getMessage("com.tms.workflow.workOrder.woNew"), "index.jsp?addr=wo01", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("com.tms.workflow.workOrder.woMyList"), "index.jsp?addr=wo02", null, null, null, null));
	}
	
    if(woAssigned)
    {
        items.add(new MenuItem(app.getMessage("com.tms.workflow.workOrder.woAssignedToMe"), "index.jsp?addr=wo03", null, null, null, null));
    }
    
    if(woAll)
    {
        items.add(new MenuItem(app.getMessage("com.tms.workflow.workOrder.woAllList"), "index.jsp?addr=wo04", null, null, null, null));
    }
%>

<%    
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>



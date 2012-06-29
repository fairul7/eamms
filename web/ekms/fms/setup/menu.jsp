<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 java.util.Collection,
                 java.util.Iterator,
                 kacang.model.DaoQuery,
                 com.tms.fms.setup.model.*"%>
<%@ include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();

    items.add(new MenuItem(app.getMessage("fms.setup.menu.systemAdministrator"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.setup.menu.programSetup"), "/ekms/fms/setup/progManagement.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.setup.menu.programListing"), "/ekms/fms/setup/programListing.jsp", null, null, null, null));
    
    request.setAttribute(MenuGenerator.MENU_FILE, items);
    
%>
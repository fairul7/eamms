<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem"%>
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
    //Determine permissions
	boolean manageOrgChart = service.hasPermission(userId, "orgChart.permission.manage", null, null);
    //Generating orgChart menu
    items.add(new MenuItem(app.getMessage("orgChart.menu.label.orgChart"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("orgChart.menu.label.searchOrgChart", "People Search"), "/ekms/orgChart/searchHierachy.jsp", null, null, null, null));    
    if (manageOrgChart) {
        items.add(new MenuItem("<span><table style='display:inline;' border=0 cellpadding=0 cellspacing=0><tr><td><font color='ffffff' size='2'><b>"+app.getMessage("orgChart.permission.manage")+"</b></font></td></tr></table></span>", null, null, null, null, null));
        //items.add(new MenuItem("<span><font color='ffffff' size='2'><b>"+app.getMessage("orgChart.permission.manage")+"</b></font></span>", null, null, null, null, null));
	    
	    items.add(new MenuItem(app.getMessage("orgChart.menu.label.hierachyManagement"), "/ekms/orgChart/listHierachy.jsp", null, null, null, null));    
	    items.add(new MenuItem(app.getMessage("orgChart.menu.label.setupCountry"), "/ekms/orgChart/setupCountry.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("orgChart.menu.label.setupDepartment"), "/ekms/orgChart/setupDepartment.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("orgChart.menu.label.setupTitle"), "/ekms/orgChart/setupTitle.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("orgChart.menu.label.setupStation"), "/ekms/orgChart/setupStation.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("orgChart.association.label.associateDepartmentCountryMapping"), "/ekms/orgChart/associateDepartmentCountry.jsp", null, null, null, null));
	    //items.add(new MenuItem(app.getMessage("orgChart.menu.label.syncLDAP"), "/ekms/orgChart/setupLDAP.jsp", null, null, null, null));
    }

    request.setAttribute(MenuGenerator.MENU_FILE, items);
    request.setAttribute("Title", app.getMessage("orgChart.menu.label.orgChart", "Organization Chart"));
%>
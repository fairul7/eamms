<%@ page import="kacang.ui.menu.MenuGenerator,
				 kacang.Application,
				 kacang.ui.menu.MenuItem,
				 java.util.ArrayList,
				 kacang.services.security.User,
				 kacang.services.security.SecurityService"%>
<%
    SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
	try
	{
		if(service.hasPermission(userId, "com.tms.hr.competency.Competency.view", "com.tms.hr.competency.CompetencyHandler", null))
		{
			items.add(new MenuItem(Application.getInstance().getMessage("project.label.competencies"), null, null, null, null, null));
			items.add(new MenuItem(Application.getInstance().getMessage("project.label.managecompetencies"), "/ekms/worms/competency.jsp", null, null, null, null));
            items.add(new MenuItem(Application.getInstance().getMessage("com.tms.hr.competency.userCompetencies"), "/ekms/worms/competencyUsersList.jsp", null, null, null, null));

		}
		items.add(new MenuItem(Application.getInstance().getMessage("project.label.projects"), null, null, null, null, null));
		items.add(new MenuItem(Application.getInstance().getMessage("project.label.myProjects"), "/ekms/worms/", null, null, null, null));
		if(service.hasPermission(userId, "com.tms.worms.project.Project.administer", "com.tms.worms.WormsHandler", null))
		{
			items.add(new MenuItem(Application.getInstance().getMessage("project.label.projectTemplates"), "/ekms/worms/template.jsp", null, null, null, null));
            items.add(new MenuItem("Project Import", "/ekms/worms/projectImport.jsp", null, null, null, null));
        }
		if(service.hasPermission(userId, "com.tms.worms.project.Project.view", "com.tms.worms.WormsHandler", null))
		{
			items.add(new MenuItem(Application.getInstance().getMessage("project.label.manageProjects"), "/ekms/worms/project.jsp", null, null, null, null));
		}
		items.add(new MenuItem(Application.getInstance().getMessage("project.label.moles"), "", null, null, null, null));
		items.add(new MenuItem(Application.getInstance().getMessage("project.label.resourcemonitor"), "/ekms/worms/moles/resourceMonitor.jsp", null, null, null, null));
		if(service.hasPermission(userId, "com.tms.worms.project.Project.administer", "com.tms.worms.WormsHandler", null))
		{
		items.add(new MenuItem(Application.getInstance().getMessage("project.label.metricReport"), "/ekms/worms/viewReport.jsp", null, null, null, null));
		}
	}
	catch(SecurityException e)
	{
		items = null;
	}

	request.setAttribute(MenuGenerator.MENU_FILE, items);
%>


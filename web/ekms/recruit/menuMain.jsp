<%@ page import="java.util.Collection,
				 java.util.ArrayList,
                 kacang.Application,
                 kacang.ui.menu.MenuItem,
				 kacang.ui.menu.ApplicationMenuGenerator,
                 kacang.services.security.*,
                 kacang.services.security.SecurityException,
                 com.tms.hr.recruit.model.*"%>
<%
	Application app = Application.getInstance();
	SecurityService security = (SecurityService) app.getService(SecurityService.class);
	String userId = security.getCurrentUser(request).getId();
	
    RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
	
	Collection items = new ArrayList();
	boolean recruitHod = rm.validateHod(userId);
	boolean recruitAdmin= security.hasPermission(userId, "recruit.permission.recruitAdmin", null, null);
	boolean recruitInterviewer= security.hasPermission(userId, "recruit.permission.recruitInterviewer", null, null);
	
	if(recruitHod) {
		items.add(new MenuItem(app.getMessage("recruit.menu.label.recruit", "Recruitment"), "/ekms/recruit/vacancyList.jsp", null, null, "/ekms/images/menu/ic_chart.gif", null));	
		recruitInterviewer = false;
	}
	
	else if(recruitAdmin) {
		items.add(new MenuItem(app.getMessage("recruit.menu.label.recruit", "Recruitment"), "/ekms/recruit/jobOfferList.jsp", null, null, "/ekms/images/menu/ic_chart.gif", null));
		recruitInterviewer = false;
	}
	
	else if(recruitInterviewer) {
		items.add(new MenuItem(app.getMessage("recruit.menu.label.recruit", "Recruitment"), "/ekms/recruit/interviewerRemarkList.jsp", null, null, "/ekms/images/menu/ic_chart.gif", null));
	}
	
	
	 
    request.setAttribute(ApplicationMenuGenerator.REQUEST_ATTRIBUTE, items);
%>

<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 com.tms.hr.recruit.model.*"%>
                 
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	
	RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
	
	ArrayList items = new ArrayList();
	
    //Determine permissions
	boolean recruitAdmin= service.hasPermission(userId, "recruit.permission.recruitAdmin", null, null);
	boolean recruitInterviewer= service.hasPermission(userId, "recruit.permission.recruitInterviewer", null, null);
	boolean recruitHod = rm.validateHod(userId);
	
    //Generating recruitment menu    	 
    if (recruitHod) {
       items.add(new MenuItem(app.getMessage("recruit.menu.header.label.manageRecruitment"), null, null, null, null, null));
	   items.add(new MenuItem(app.getMessage("recruit.menu.label.addVacancy"), "/ekms/recruit/vacancy.jsp", null, null, null, null));    
	   items.add(new MenuItem(app.getMessage("recruit.menu.label.vacancyListing"), "/ekms/recruit/vacancyList.jsp", null, null, null, null));
	   items.add(new MenuItem(app.getMessage("recruit.menu.label.intervieweeListing"), "/ekms/recruit/intervieweeList.jsp", null, null, null, null));
	   items.add(new MenuItem(app.getMessage("recruit.menu.label.jobOfferListing"), "/ekms/recruit/jobOfferList.jsp", null, null, null, null));
	   //items.add(new MenuItem(app.getMessage("recruit.menu.label.carrer"), "/ekms/recruit/careerList.jsp", null, null, null, null));
   } 
     
    if (recruitAdmin) {
        items.add(new MenuItem(app.getMessage("recruit.menu.header.label.manageJobOffer"), null, null, null, null, null));
	    items.add(new MenuItem(app.getMessage("recruit.menu.label.jobOfferListing"), "/ekms/recruit/jobOfferList.jsp", null, null, null, null));    
    }
    
    if (recruitAdmin || recruitHod) {
	    items.add(new MenuItem(app.getMessage("recruit.menu.header.label.manageTemplate"), null, null, null, null, null));
	    items.add(new MenuItem(app.getMessage("recruit.menu.label.addVacancyTemplate"), "/ekms/recruit/vacancyTemp.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("recruit.menu.label.vacancyTemplateListing"), "/ekms/recruit/vacancyTempList.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("recruit.menu.label.globalSetup"), "/ekms/recruit/setupMessageBody.jsp", null, null, null, null));
	    
	    items.add(new MenuItem(app.getMessage("recruit.menu.header.label.report"), null, null, null, null, null));
	    items.add(new MenuItem(app.getMessage("recruit.menu.label.viewVacancyReport"), "/ekms/recruit/vacancyReportList.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("recruit.menu.label.viewAuditTrail"), "/ekms/recruit/auditTrailList.jsp", null, null, null, null));
    }
    
    if (recruitInterviewer) {
        items.add(new MenuItem(app.getMessage("recruit.menu.header.label.manageRemark"), null, null, null, null, null));
	    items.add(new MenuItem(app.getMessage("recruit.menu.label.interviewerRemarkListing"), "/ekms/recruit/interviewerRemarkList.jsp", null, null, null, null));    
    }
    
    request.setAttribute(MenuGenerator.MENU_FILE, items);
    request.setAttribute("Title", app.getMessage("recruit.menu.label.recruit", "Recruitment"));
%>
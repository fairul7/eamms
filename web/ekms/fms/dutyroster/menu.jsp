<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 java.util.Collection,
                 java.util.Iterator,
                 kacang.model.DaoQuery,
                 com.tms.fms.transport.model.*,
                 com.tms.fms.register.model.FMSRegisterManager,
                 com.tms.fms.engineering.model.EngineeringModule"%>
<%@ include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	FMSRegisterManager module = (FMSRegisterManager) app.getModule(FMSRegisterManager.class);
	EngineeringModule emodule = (EngineeringModule) app.getModule(EngineeringModule.class);
	
	String userId = service.getCurrentUser(request).getId();
	String department = module.getUserDepartment(userId);
	ArrayList items = new ArrayList();
	
	boolean uploadDuty = service.hasPermission(userId, "com.tms.fms.transport.uploadDuty", null, null);
	boolean manpowerView = service.hasPermission(userId, "com.tms.fms.facility.permission.fmsAdmin", null, null);
%>

<%
	items.add(new MenuItem(app.getMessage("fms.label.transport.dutyRoster"), null, null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.label.engineering.myDutyRoster"), "/ekms/fms/dutyroster/mydutyroster.jsp", null, null, null, null));
    items.add(new MenuItem(app.getMessage("fms.label.engineering.searchRoster"), "/ekms/fms/dutyroster/searchroster.jsp", null, null, null, null));
    
    if (emodule.ENGINEERING_DEPARTMENT_ID.equals(department)){
    	items.add(new MenuItem(app.getMessage("fms.label.engineering.myAssignment"), "/ekms/fms/dutyroster/myEngineeringAssignment.jsp", null, null, null, null));
    } else {
    	items.add(new MenuItem(app.getMessage("fms.label.engineering.myAssignment"), "/ekms/fms/dutyroster/myAssignment.jsp", null, null, null, null));
    }
    if(uploadDuty){
    items.add(new MenuItem(app.getMessage("com.tms.fms.transport.uploadDuty"), "/ekms/fms/dutyroster/ImportDutyRoster.jsp", null, null, null, null));
    }
    
    
    if(manpowerView){
	    items.add(new MenuItem(app.getMessage("fms.facility.menu.manpowerSetup"), "/ekms/fms/dutyroster/manpowerListing.jsp", null, null, null, null));		
		items.add(new MenuItem(app.getMessage("fms.facility.menu.workingProfileDuration"), "/ekms/fms/dutyroster/workingProfileDuration.jsp", null, null, null, null));        
	    items.add(new MenuItem(app.getMessage("fms.label.engineering.manpowerHoliday"), "/ekms/fms/dutyroster/leaveView.jsp", null, null, null, null));
    }
%>


<%
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>
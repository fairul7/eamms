<%@ page import="kacang.Application,
                 kacang.services.security.SecurityService,
                 kacang.ui.menu.MenuGenerator,
                 kacang.ui.menu.MenuItem,
                 java.util.ArrayList,
                 com.tms.collab.isr.permission.model.PermissionModel,
                 com.tms.collab.isr.permission.model.ISRGroup"%>
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	SecurityService security = (SecurityService) app.getService(SecurityService.class);
	String userId = security.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();
	PermissionModel permissionModel = (PermissionModel) app.getModule(PermissionModel.class);
	boolean systemLevelPermissionAccessibility = security.hasPermission(userId, "isr.permission.managePermission", null, null);
	
    //Generating ISR menu
    items.add(new MenuItem(app.getMessage("isr.menu.shortModuleName"), null, null, null, null, null));
    if(permissionModel.hasPermission(userId, ISRGroup.PERM_NEW_REQUEST)){
    	items.add(new MenuItem(app.getMessage("isr.menu.newRequest"), "/ekms/isr/newRequest.jsp", null, null, null, null));    
    }
    items.add(new MenuItem(app.getMessage("isr.menu.viewRequest"), "/ekms/isr/viewRequestListing.jsp", null, null, null, null)); 
    if(permissionModel.hasPermission(userId, ISRGroup.PERM_FORCE_CLOSURE_MANAGEMENT)){
    	items.add(new MenuItem(app.getMessage("isr.menu.forceClosure"), "/ekms/isr/forceClosure.jsp", null, null, null, null));    
    }    
    
    if(permissionModel.hasPermission(userId, ISRGroup.PERM_GENERAL_REPORTS)){
        items.add(new MenuItem(app.getMessage("isr.menu.generalReports"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("isr.menu.statusReport"), "/ekms/isr/statusReport.jsp", null, null, null, null)); 
        items.add(new MenuItem(app.getMessage("isr.menu.priorityReport"), "/ekms/isr/priorityReport.jsp", null, null, null, null)); 
        items.add(new MenuItem(app.getMessage("isr.menu.commonStats"), "/ekms/isr/commonStatsReport.jsp", null, null, null, null));     	
    }
    
    if(permissionModel.hasPermission(userId, ISRGroup.PERM_DEPARTMENT_REPORTS)){
        items.add(new MenuItem(app.getMessage("isr.menu.departmentReports"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("isr.menu.staffReport"), "/ekms/isr/staffReport.jsp", null, null, null, null)); 
        items.add(new MenuItem(app.getMessage("isr.menu.timeToResolve"), "/ekms/isr/timeOfResolveReport.jsp", null, null, null, null));     	
    }
    
    if(permissionModel.hasPermission(userId, ISRGroup.PERM_ACCESS_SETUP) ||
    		permissionModel.hasPermission(userId, ISRGroup.PERM_ACCESS_GROUP_PERMISSION) ||
    		systemLevelPermissionAccessibility){
    	items.add(new MenuItem(app.getMessage("isr.menu.setups"), null, null, null, null, null));
    	items.add(new MenuItem(app.getMessage("isr.menu.permission"), "/ekms/isr/permission.jsp", null, null, null, null)); 
    }
    
    if(permissionModel.hasPermission(userId, ISRGroup.PERM_ACCESS_SETUP)){
        items.add(new MenuItem(app.getMessage("isr.menu.globalSettings"), "/ekms/isr/globalSettings.jsp", null, null, null, null)); 
        items.add(new MenuItem(app.getMessage("isr.menu.emailSettings"), "/ekms/isr/emailSettings.jsp", null, null, null, null)); 
    }
    
    request.setAttribute(MenuGenerator.MENU_FILE, items);
    request.setAttribute("Title", app.getMessage("isr.module.name", "Internal Service Request"));
%>
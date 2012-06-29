<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 kacang.ui.WidgetManager,
                 kacang.ui.Widget,com.tms.fms.engineering.model.EngineeringModule"%>
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	ArrayList items = new ArrayList();

    //Determine permissions
	boolean userView = service.hasPermission(userId, SecurityService.PERMISSION_USER_VIEW, null, null);
	boolean groupView = service.hasPermission(userId, SecurityService.PERMISSION_GROUP_VIEW, null, null);
	boolean settingsView = service.hasPermission(userId, "com.tms.cms.SystemSettings", null, null);
    boolean logView = service.hasPermission(userId, "kacang.services.log.Entry.view", null, null);
    boolean taskView = service.hasPermission(userId, "com.tms.cms.ScheduledTasks", null, null);
    boolean manageContent = service.hasPermission(userId, "com.tms.cms.ManageContent", null, null);
	boolean portletView = service.hasPermission(userId, "com.tms.portlet.Portlet.view", null, null);
	boolean themeView = service.hasPermission(userId, "com.tms.portlet.Theme.view", null, null);
	boolean quotaView = service.hasPermission(userId, "com.tms.collab.messaging.Quota", null, null);

	boolean myFolderQuota = service.hasPermission(userId, "com.tms.collab.myfolder.quota", null, null);
	boolean isFMSAdmin=EngineeringModule.isFMSAdmin(userId);

    //Generating menu
    if (userView) {
        items.add(new MenuItem(app.getMessage("security.label.users"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("security.label.userListing"), "/ekms/sysadmin/user.jsp?cn=user.userPortlet.userForm.viewUser&et=action&button*user.userPortlet.userForm.addUser.cancelButton=Cancel&new=2", null, null, null, null));
        items.add(new MenuItem(app.getMessage("security.label.newUser"), "/ekms/sysadmin/user.jsp?cn=user.userPortlet.userForm.viewUser&et=action&button*user.userPortlet.userForm.viewUser.add=Add&new=1", null, null, null, null));
        items.add(new MenuItem(app.getMessage("security.label.userReport"), "/ekms/sysadmin/userReport.jsp", null, null, null, null));

        Widget w = (Widget)WidgetManager.getWidgetManager(request).getWidget("user.userPortlet.userForm.editUser");
        if (w != null && !w.isHidden() && manageContent && "/ekms/sysadmin/user.jsp".equals(request.getRequestURI())) {
            items.add(new MenuItem(app.getMessage("cms.label.contentPermissions"), "javascript:contentSecurityPopup()", null, null, null, null));
        }
    }
    if (groupView) {
        items.add(new MenuItem(app.getMessage("security.label.groups"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("security.label.groupListing"), "/ekms/sysadmin/group.jsp?cn=group.groupPortlet.groupForm.viewGroup&et=action&button*group.groupPortlet.groupForm.addGroup.cancelButton=Cancel&new=2", null, null, null, null));
        items.add(new MenuItem(app.getMessage("security.label.newGroup"), "/ekms/sysadmin/group.jsp?cn=group.groupPortlet.groupForm.viewGroup&et=action&button*group.groupPortlet.groupForm.viewGroup.add=Add&new=1", null, null, null, null));
        items.add(new MenuItem(app.getMessage("security.label.permissionReport"), "/ekms/sysadmin/groupPermissionReport.jsp", null, null, null, null));

		Widget w = (Widget)WidgetManager.getWidgetManager(request).getWidget("group.groupPortlet.groupForm.editGroup");
        if (w != null && !w.isHidden() && manageContent && "/ekms/sysadmin/group.jsp".equals(request.getRequestURI())) {
            items.add(new MenuItem(app.getMessage("cms.label.contentPermissions"), "javascript:contentSecurityGroupPopup()", null, null, null, null));
        }
    }

    items.add(new MenuItem(app.getMessage("general.label.systemAdministration"), null, null, null, null, null));
    
   
    
    if (settingsView) {
        items.add(new MenuItem(app.getMessage("siteadmin.label.systemSettings"), "/ekms/sysadmin/systemSettings.jsp", null, null, null, null));
    }
    if (logView) {
        items.add(new MenuItem(app.getMessage("general.label.systemLogs"), "/ekms/sysadmin/logs.jsp", null, null, null, null));
    }
    items.add(new MenuItem(app.getMessage("ekplog.label.ekpLogs"), "/ekms/sysadmin/ekpLogs.jsp", null, null, null, null));
    if (taskView) {
        items.add(new MenuItem(app.getMessage("siteadmin.label.scheduledTasks"), "/ekms/sysadmin/scheduledTasks.jsp", null, null, null, null));
    }
    if(portletView) {
		items.add(new MenuItem(app.getMessage("portlet.message.portletRegistry"), "/ekms/sysadmin/portlets.jsp", null, null, null, null));
    }
	if(themeView) {
		items.add(new MenuItem(app.getMessage("portlet.label.themesRegistry"), "/ekms/sysadmin/themes.jsp", null, null, null, null));
    }
	if(quotaView) {
		items.add(new MenuItem(app.getMessage("messaging.label.messagingQuota"), "/ekms/sysadmin/messagingQuota.jsp", null, null, null, null));
    }
    if(myFolderQuota){
		items.add(new MenuItem(app.getMessage("mf.label.mfQuota"), "/ekms/sysadmin/mfQuota.jsp", null, null, null, null));
	}
    if(isFMSAdmin){
		items.add(new MenuItem(app.getMessage("fms.label.fmsAdministration"), null, null, null, null, null));    
	    items.add(new MenuItem(app.getMessage("fms.label.RegistrationApplication"), "/ekms/sysadmin/fmsRegister.jsp", null, null, null, null));    
	    //items.add(new MenuItem(app.getMessage("fms.label.departmentSetup"), "/ekms/sysadmin/fmsDepartmentSetup.jsp", null, null, null, null));    
	    items.add(new MenuItem(app.getMessage("fms.label.departmentListing"), "/ekms/sysadmin/fmsDepartmentList.jsp", null, null, null, null));    
		//items.add(new MenuItem(app.getMessage("fms.label.unitSetup"), "/ekms/sysadmin/fmsUnitSetup.jsp", null, null, null, null));    
	    items.add(new MenuItem(app.getMessage("fms.label.unitListing"), "/ekms/sysadmin/fmsUnitList.jsp", null, null, null, null));
	    //items.add(new MenuItem(app.getMessage("fms.setup.menu.programSetup"), "/ekms/sysadmin/progManagement.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.setup.menu.programListing"), "/ekms/sysadmin/programListing.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.facility.menu.competencySetup"), "/ekms/sysadmin/competency.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.manpower.menu.leaveTypeListing"), "/ekms/sysadmin/leaveTypeView.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.facility.menu.workingProfileListing"), "/ekms/sysadmin/workingProfileListing.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.tran.setup.menu.outsourcePanelSetup"), "/ekms/sysadmin/OutsourcePanel.jsp", null, null, null, null));

	    items.add(new MenuItem(app.getMessage("fms.manpower.menu.setup.holidayListing"), "/ekms/sysadmin/holidayView.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.facility.label.globalAssignmentListing"), "/ekms/sysadmin/globalAssignment.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.facility.label.revalidateBooking"), "/ekms/sysadmin/revalidateBooking.jsp", null, null, null, null));
	    items.add(new MenuItem(app.getMessage("fms.administration.menu.abwAdminEmailSetup"), "/ekms/sysadmin/abwEmailsSetup.jsp", null, null, null, null));
    }
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>
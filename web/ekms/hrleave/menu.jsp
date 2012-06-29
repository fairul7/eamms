<%@ page import="kacang.ui.menu.MenuGenerator,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 java.util.ArrayList,
                 kacang.ui.menu.MenuItem,
                 kacang.ui.Event,
                 java.util.Calendar,
                 com.tms.hr.leave.model.LeaveModule,
                 kacang.services.security.User"%>
<%@ include file="/common/header.jsp"%>
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
    User user = service.getCurrentUser(request);
	String userId = user.getId();
	ArrayList items = new ArrayList();

    //Calculations and determine permissions
    LeaveModule handler = (LeaveModule) Application.getInstance().getModule(LeaveModule.class);
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    Calendar start = Calendar.getInstance();
    start.set(2000, Calendar.JANUARY, 1);
    Calendar end = Calendar.getInstance();
    end.set(year+1, Calendar.DECEMBER, 31);
    boolean isApprover = handler.viewEmployeeListForApprover(userId).length > 0;
    int approverCount = handler.countLeaveToApproveList(start.getTime(), end.getTime(), user);
    approverCount += handler.countCreditLeaveToApproveList(start.getTime(), end.getTime(), user);
    boolean leaveUser = service.hasPermission(userId, "com.tms.cms.leave.Leaves", null, null);
    boolean leaveManager = service.hasPermission(userId, LeaveModule.PERMISSION_MANAGE_LEAVE, null, null);
    boolean employeeManager = service.hasPermission(userId, "com.tms.hr.employee.ManageEmployee", null, null);

    //Generating leave menu
    items.add(new MenuItem(app.getMessage("leave.label.LeaveApp"), null, null, null, null, null));
    if (leaveUser) {
        items.add(new MenuItem(app.getMessage("leave.label.LeaveSummary"), "/ekms/hrleave/viewLeaveSummary.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("leave.label.ApplyLeave"), "/ekms/hrleave/applyLeave.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("leave.label.ApplyCreditLeave"), "/ekms/hrleave/applyCreditLeave.jsp", null, null, null, null));
        if (leaveManager || (approverCount > 0)) {
            items.add(new MenuItem(app.getMessage("leave.label.Approve") + " (" + approverCount + ")", "/ekms/hrleave/viewApproveList.jsp", null, null, null, null));
        }
        if (leaveManager || isApprover) {
            items.add(new MenuItem(app.getMessage("leave.label.StaffLeave"), "/ekms/hrleave/viewLeaveReport.jsp", null, null, null, null));
            items.add(new MenuItem(app.getMessage("leave.label.leaveMonitor"), "/ekms/hrleave/leaveMonitor.jsp", null, null, null, null));
        }
        
    

    }

    if(!leaveManager)
    items.add(new MenuItem(app.getMessage("leave.label.Holidays"), "/ekms/hrleave/holidays.jsp", null, null, null, null));

    //Generating administration menu
    if (leaveManager) {
        items.add(new MenuItem(app.getMessage("leave.label.Administration"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("leave.label.ApplyLeave"), "/ekms/hrleave/applyStaffLeave.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("leave.label.ApplyCreditLeave"), "/ekms/hrleave/applyStaffCreditLeave.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("leave.label.adjustments"), "/ekms/hrleave/applyAdjustment.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("leave.label.Holidays"), "/ekms/hrleave/holidays.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("leave.label.entitlement"), "/ekms/hrleave/serviceEntitlement.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("leave.label.leaveTypes"), "/ekms/hrleave/leaveTypeSetup.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("leave.label.Global"), "/ekms/hrleave/globalSettings.jsp", null, null, null, null));

    }

    //Generating HR employee menu
    if (employeeManager) {
        items.add(new MenuItem(app.getMessage("leave.label.HRMenu"), null, null, null, null, null));
        items.add(new MenuItem(app.getMessage("leave.label.Employees"), "/ekms/hrleave/employeeSetup.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("leave.label.Hierarchy"), "/ekms/hrleave/employeeHierarchy.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("leave.label.departmentType"), "/ekms/hrleave/departmentType.jsp", null, null, null, null));
        items.add(new MenuItem(app.getMessage("leave.label.serviceClassification"), "/ekms/hrleave/serviceClassification.jsp", null, null, null, null));

    }
    request.setAttribute(MenuGenerator.MENU_FILE, items);
%>
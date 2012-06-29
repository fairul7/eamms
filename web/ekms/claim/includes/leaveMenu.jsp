<%@ page import="com.tms.hr.leave.model.LeaveModule,
                 kacang.Application,
                 java.util.Calendar,
                 kacang.services.security.SecurityService,
                 kacang.services.security.User,
                 kacang.util.Log"%>
<%@include file="/common/header.jsp" %>
<%
    SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
    User currentUser = service.getCurrentUser(request);
%>

<%
    LeaveModule handler = (LeaveModule) Application.getInstance().getModule(LeaveModule.class);
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    Calendar start = Calendar.getInstance();
    start.set(2000, Calendar.JANUARY, 1);
    Calendar end = Calendar.getInstance();
    end.set(year+1, Calendar.DECEMBER, 31);
    boolean isApprover = handler.viewEmployeeListForApprover(currentUser.getId()).length > 0;
    int approverCount = handler.countLeaveToApproveList(start.getTime(), end.getTime(), currentUser);
    approverCount += handler.countCreditLeaveToApproveList(start.getTime(), end.getTime(), currentUser);
    boolean isLeaveManager = service.hasPermission(currentUser.getId(), "com.tms.cms.leave.ManageLeaves", "com.tms.cms.leave.model.LeaveModule", null);
%>

<x:permission module="com.tms.cms.leave.model.LeaveModule" permission="com.tms.cms.leave.Leaves">
    <c:set var="link"><c:url value="/ekms/hrleave/viewLeaveSummary.jsp"/></c:set>
    <c:set var="text"><fmt:message key='leave.label.LeaveSummary'/></c:set>
    <%@include file="/ekms/claim/includes/menuItem.jsp" %>

    <c:set var="link"><c:url value="/ekms/hrleave/applyLeave.jsp"/></c:set>
    <c:set var="text"><fmt:message key='leave.label.ApplyLeave'/></c:set>
    <%@include file="/ekms/claim/includes/menuItem.jsp" %>

    <c:set var="link"><c:url value="/ekms/hrleave/applyCreditLeave.jsp"/></c:set>
    <c:set var="text"><fmt:message key='leave.label.ApplyCreditLeave'/></c:set>
    <%@include file="/ekms/claim/includes/menuItem.jsp" %>

    <%
        if(isLeaveManager || (approverCount > 0)) {
    %>
    <c:set var="html">
        <a href="<c:url value="/ekms/hrleave/viewApproveList.jsp"/>"><font color="FFFFFF" class="menuFont"><b><fmt:message key='leave.label.Approve'/></b>
        (<%= approverCount %>)</font></a>
    </c:set>
    <%@include file="/ekms/claim/includes/menuItem.jsp" %>
    <%
        }
        if (isLeaveManager || isApprover) {
    %>
        <c:set var="link"><c:url value="/ekms/hrleave/viewLeaveReport.jsp"/></c:set>
        <c:set var="text"><fmt:message key='leave.label.StaffLeave'/></c:set>
        <%@include file="/ekms/claim/includes/menuItem.jsp" %>
    <%
        }
        if(isLeaveManager) {
    %>
        <c:set var="title"><fmt:message key='leave.label.Administration'/></c:set>
        <%@include file="/ekms/claim/includes/menuHeader.jsp" %>

        <c:set var="link"><c:url value="/ekms/hrleave/applyStaffLeave.jsp"/></c:set>
        <c:set var="text"><fmt:message key='leave.label.ApplyLeave'/></c:set>
        <%@include file="/ekms/claim/includes/menuItem.jsp" %>

        <c:set var="link"><c:url value="/ekms/hrleave/applyStaffCreditLeave.jsp"/></c:set>
        <c:set var="text"><fmt:message key='leave.label.ApplyCreditLeave'/></c:set>
        <%@include file="/ekms/claim/includes/menuItem.jsp" %>

        <c:set var="link"><c:url value="/ekms/hrleave/applyAdjustment.jsp"/></c:set>
        <c:set var="text"><fmt:message key='leave.label.adjustments'/></c:set>
        <%@include file="/ekms/claim/includes/menuItem.jsp" %>

        <c:set var="link"><c:url value="/ekms/hrleave/holidays.jsp"/></c:set>
        <c:set var="text"><fmt:message key='leave.label.Holidays'/></c:set>
        <%@include file="/ekms/claim/includes/menuItem.jsp" %>

        <c:set var="link"><c:url value="/ekms/hrleave/serviceEntitlement.jsp"/></c:set>
        <c:set var="text"><fmt:message key='leave.label.entitlement'/></c:set>
        <%@include file="/ekms/claim/includes/menuItem.jsp" %>

        <c:set var="link"><c:url value="/ekms/hrleave/leaveTypeSetup.jsp"/></c:set>
        <c:set var="text"><fmt:message key='leave.label.leaveTypes'/></c:set>
        <%@include file="/ekms/claim/includes/menuItem.jsp" %>

        <c:set var="link"><c:url value="/ekms/hrleave/globalSettings.jsp"/></c:set>
        <c:set var="text"><fmt:message key='leave.label.Global'/></c:set>
        <%@include file="/ekms/claim/includes/menuItem.jsp" %>

        <x:permission module="com.tms.hr.employee.model.EmployeeModule" permission="com.tms.hr.employee.ManageEmployee">
            <c:set var="title"><fmt:message key='leave.label.HRMenu'/></c:set>
            <%@include file="/ekms/claim/includes/menuHeader.jsp" %>

            <c:set var="link"><c:url value="/ekms/hrleave/employeeSetup.jsp"/></c:set>
            <c:set var="text"><fmt:message key='leave.label.Employees'/></c:set>
            <%@include file="/ekms/claim/includes/menuItem.jsp" %>

            <c:set var="link"><c:url value="/ekms/hrleave/employeeHierarchy.jsp"/></c:set>
            <c:set var="text"><fmt:message key='leave.label.Hierarchy'/></c:set>
            <%@include file="/ekms/claim/includes/menuItem.jsp" %>

            <c:set var="link"><c:url value="/ekms/hrleave/departmentType.jsp"/></c:set>
            <c:set var="text"><fmt:message key='leave.label.departmentType'/></c:set>
            <%@include file="/ekms/claim/includes/menuItem.jsp" %>

            <c:set var="link"><c:url value="/ekms/hrleave/serviceClassification.jsp"/></c:set>
            <c:set var="text"><fmt:message key='leave.label.serviceClassification'/></c:set>
            <%@include file="/ekms/claim/includes/menuItem.jsp" %>
        </x:permission>
    <%
        }
    %>
</x:permission>
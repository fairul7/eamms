<%@ page import="kacang.Application,
                 java.util.*,
                 com.tms.hr.employee.model.EmployeeModule,
                 com.tms.hr.employee.model.DepartmentDataObject,
                 kacang.ui.WidgetManager,
                 java.io.PrintWriter,
                 kacang.services.security.User,
                 com.tms.hr.employee.model.EmployeeException,
                 kacang.model.DataObjectNotFoundException,
                 kacang.util.Log,
                 kacang.stdui.DatePopupField,
                 com.tms.hr.leave.ui.LeaveTypeSelectBox,
                 com.tms.hr.leave.model.*"%>
<%@ include file="/common/header.jsp" %>

<x:permission var="hasPermission" module="<%= LeaveModule.class.getName()%>" permission="<%= LeaveModule.PERMISSION_MANAGE_LEAVE %>"/>  

<c:if test="${!hasPermission}">
    <fmt:message key="general.label.noPermissions"/>
</c:if>
<c:if test="${hasPermission}">

<!-- Declare widgets -->
<x:config>
    <page name="hrLeavePortlet">
        <form name="form" columns="2">
            <label name="l1" text="From"/>
            <kacang.stdui.DatePopupField name="from" optional="false"/>
            <label name="l2" text="To"/>
            <kacang.stdui.DatePopupField name="to" optional="false"/>
            <label name="l3" text="Type"/>
            <com.tms.hr.leave.ui.LeaveTypeSelectBox name="type"/>
            <label name="l4" text=""/>
            <button name="submit" text="Submit"/>
        </form>
    </page>
</x:config>

    <table width="100%">
    <tr>
        <td nowrap>
            <!-- Display widget -->
            <x:display name="hrLeavePortlet.form"/>
            <hr size="1">
        </td>
    </tr>
    </table>

<!-- Determine date range and type -->
<%
    try {
        WidgetManager wm = WidgetManager.getWidgetManager(request);
        DatePopupField date1 = (DatePopupField)wm.getWidget("hrLeavePortlet.form.from");
        DatePopupField date2 = (DatePopupField)wm.getWidget("hrLeavePortlet.form.to");
        LeaveTypeSelectBox sb = (LeaveTypeSelectBox)wm.getWidget("hrLeavePortlet.form.type");

        // determine start date
        Calendar startCal = Calendar.getInstance();
        Date sDate = date1.getDate();
        if (sDate != null)
            startCal.setTime(sDate);

        // determine end date
        Calendar endCal = Calendar.getInstance();
        Date eDate = date2.getDate();
        if (eDate != null)
            endCal.setTime(eDate);

        String leaveType = LeaveModule.DEFAULT_LEAVE_TYPE_ANNUAL;
        Map selected = sb.getSelectedOptions();
        if (selected != null && selected.size() > 0) {
            leaveType = selected.keySet().iterator().next().toString();
        }

        Collection entryList = getLeaveList(request, startCal.getTime(), endCal.getTime(), leaveType);
        pageContext.setAttribute("entryList", entryList);
    }
    catch (Exception e) {
        e.printStackTrace(new PrintWriter(out));
    }
%>


    <table width="100%">
    <c:if test="${!empty entryList}">
        <tr>
            <td class="classHeader">Name</td>
            <td class="classHeader">From</td>
            <td class="classHeader">To</td>
            <td class="classHeader">Days</td>
            <td class="classHeader">Status</td>
            <td class="classHeader">Approver</td>
            <td class="classHeader">Balance</td>
        </tr>
        <c:forEach items="${entryList}" var="entry">
        <tr class="classRow">
            <td class="classRow" valign="top"><c:out value='${entry.propertyMap.employeeName}'/></td>
            <td class="classRow" valign="top"><fmt:formatDate pattern="${globalDateLong}" value='${entry.startDate}'/></td>
            <td class="classRow" valign="top"><fmt:formatDate pattern="${globalDateLong}" value='${entry.endDate}'/></td>
            <td class="classRow" valign="top"><fmt:formatNumber pattern="0.#" value='${-entry.days}'/></td>
            <td class="classRow" valign="top"><c:out value='${entry.status}'/></td>
            <td class="classRow" valign="top"><c:out value='${entry.propertyMap.approverName}'/></td>
            <td class="classRow" valign="top"><fmt:formatNumber pattern="0.#" value='${entry.propertyMap.balance}'/></td>
        </tr>
        </c:forEach>
    </c:if>
    </table>

    <table width="100%">
    <tr>
        <td class="portletFooter" colspan="7">&nbsp;
        <c:if test="${param.hidePrint}">
        <input class="button" value="Print Version" type="button" onClick="window.open('<c:out value="/ekms/hrleave/leavePrint.jsp?hidePrint=true"/>','leavePrint','height=400,width=600,resizable=yes,scrollbars=yes,status=no,toolbar=no,menubar=yes,location=no')"/>
        </c:if>
        </td>
    </tr>
    </table>
</c:if>

<%!

    Collection getLeaveList(HttpServletRequest request, Date startDate, Date endDate, String leaveType) throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        int year = cal.get(Calendar.YEAR);
        WidgetManager wm = WidgetManager.getWidgetManager(request);

        // get leave entries
        LeaveModule lm = (LeaveModule)Application.getInstance().getModule(LeaveModule.class);
        EmployeeModule em = (EmployeeModule)Application.getInstance().getModule(EmployeeModule.class);
        if (leaveType != null && leaveType.trim().length() == 0) {
            leaveType = null;
        }
        Collection entryList = lm.viewLeaveList(startDate, endDate, null, null, leaveType, new String[] { LeaveModule.STATUS_APPROVED, LeaveModule.STATUS_CANCEL_SUBMITTED, LeaveModule.STATUS_CANCEL_APPROVED, LeaveModule.STATUS_CANCEL_REJECTED }, null, "startDate", false, 0, -1);

        // get users
        Collection userIdList = new HashSet();
        for (Iterator i=entryList.iterator(); i.hasNext();) {
            LeaveEntry entry = (LeaveEntry)i.next();
            userIdList.add(entry.getUserId());
        }
        String[] userIdArray = (String[])userIdList.toArray(new String[0]);
        Map employeeMap = lm.viewEmployeeMap(userIdArray);

        // get calculator
        LeaveCalculator calculator = LeaveModule.getLeaveCalculator(year);

        // assemble rows
        for (Iterator i=entryList.iterator(); i.hasNext();) {
            LeaveEntry entry = (LeaveEntry)i.next();

            try {
                // determine user name
                String userId = entry.getUserId();
                User u = (User)employeeMap.get(userId);
                if (u != null) {
                    entry.setProperty("employeeName", u.getName());
                }

                // determine approver
                Collection tmp = em.getEmployeeReportTo(userId);
                if (tmp != null && tmp.size() > 0) {
                    DepartmentDataObject ddo = (DepartmentDataObject)tmp.iterator().next();
                    String approver = ddo.getFullName();
                    entry.setProperty("approverName", approver);
                }

                // determine balance
                LeaveSummary ls = lm.viewLeaveSummary(calculator, entry.getLeaveType(), userId, year, false, wm.getUser());
                entry.setProperty("balance", new Float(ls.getBalance()));
            }
            catch (Exception e) {
                Log.getLog(getClass()).debug("Error retrieving leave entry " + entry.getId(), e);
                i.remove();
            }
        }

        return entryList;
    }

%>
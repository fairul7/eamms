<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>

<%@include file="/common/header.jsp"%>

<x:config>
    <page  name="tsFormEditPage">
        <com.tms.collab.timesheet.ui.TimeSheetFormEdit name="tsFormEdit"/>
    </page>
</x:config>

<c:if test="${forward.name=='success'}">
    <script>
        alert('<fmt:message key="timesheet.message.added"/>');
    </script>
    <c:redirect url="TimeSheetTableView.jsp"/>
</c:if>

<c:if test="${forward.name=='error'}">
    <script>
        alert('<fmt:message key="timesheet.error.adderror"/>');
    </script>
    <c:redirect url="TimeSheetTableView.jsp"/>
</c:if>

<c:if test="${forward.name=='cancel'}">
    <c:redirect url="TimeSheetTableView.jsp"/>
</c:if>

<c:if test="${forward.name=='select'}">
    <script>
        alert('<fmt:message key="timesheet.message.selectduration"/>');
    </script>
</c:if>
<c:if test="${forward.name=='date'}">
    <script>
        alert('<fmt:message key="timesheet.message.dateafter"/>');
    </script>
</c:if>

<c:if test="${!empty param.id}" >
    <x:set name="tsFormEditPage.tsFormEdit" property="timesheetId" value="${param.id}" />
</c:if>


<%@include file="/ekms/includes/header.jsp" %>
<table cellpadding="4" cellspacing="1" width="100%" border="0">
<tr>
    <td><x:display name="tsFormEditPage.tsFormEdit" /> </td>
</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>

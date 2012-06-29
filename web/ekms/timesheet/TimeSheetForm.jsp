<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
 <%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 25, 2005
  Time: 11:24:57 AM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/header.jsp"%>

<x:config>
    <page  name="tsFormPage">
        <com.tms.collab.timesheet.ui.TimeSheetForm name="tsForm"/>
    </page>
</x:config>

<c:if test="${!empty param.taskid}" >
    <x:set name="tsFormPage.tsForm" property="taskId" value="${param.taskid}" />
</c:if>
<c:choose>
<c:when test="${!empty param.set}">
    <x:set name="tsFormPage.tsForm" property="set" value="${true}"/>
</c:when>
<c:otherwise>
    <x:set name="tsFormPage.tsForm" property="set" value="${false}"/>
</c:otherwise>
</c:choose>
<c:if test="${forward.name=='submitadd'}">
    <script>
        alert('<fmt:message key="timesheet.message.added"/>');
    </script>

    <c:redirect url="TimeSheetForm.jsp?taskid=${widgets['tsFormPage.tsForm'].taskId}&projectid=${widgets['tsFormPage.tsForm'].projectId}&set=true"/>
</c:if>
<c:if test="${forward.name=='added'}">
    <script>
        alert('<fmt:message key="timesheet.message.added"/>');
    </script>

    <c:redirect url="TimeSheetTableView.jsp"/>
</c:if>
<c:if test="${forward.name=='form'}">
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




<%@include file="/ekms/includes/header.jsp" %>
<table cellpadding="4" cellspacing="1" width="100%" border="0">
<tr>
    <td><x:display name="tsFormPage.tsForm" /> </td>
</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>

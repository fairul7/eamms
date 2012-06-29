<%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 25, 2005
  Time: 3:57:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/header.jsp"%>

<x:config>
    <page  name="tsTablePage">
        <com.tms.collab.timesheet.ui.TimeSheetTable name="tsTable" />
    </page>
</x:config>

<c:if test="${!empty param.taskid}" >
    <x:set name="tsTablePage.tsTable" property="taskId" value="${param.taskid}" />
</c:if>
<c:if test="${!empty param.projectid}" >
    <x:set name="tsTablePage.tsTable" property="projectId" value="${param.projectid}" />
</c:if>
<c:if test="${!empty param.userid}">
    <x:set name="tsTablePage.tsTable" property="userId" value="${param.userid}"></x:set>
</c:if>

<c:if test="${!empty param.id}">
    <script>
        window.open("<c:url value="TimeSheetDet.jsp?id=${param.id}" />","detForm","height=200,width=300,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    </script>
</c:if>
<%@include file="/ekms/includes/header.jsp" %>

<table width="100%" cellpadding="4" cellspacing="1">
<tr>
<td><x:display name="tsTablePage.tsTable"/></td>
</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>
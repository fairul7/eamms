<%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 27, 2005
  Time: 10:40:16 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp"%>

<x:config>
    <page name="tsDetPage">
        <com.tms.collab.timesheet.ui.TimeSheetViewDet name="tsDet"/>
    </page>
</x:config>

<c:if test="${!empty param.taskid}">
    <x:set name="tsDetPage.tsDet" property="taskId" value="${param.taskid}"/>
</c:if>
<c:if test="${!empty param.projectid}">
    <x:set name="tsDetPage.tsDet" property="projectId" value="${param.projectid}"/>
</c:if>

<!-- display page -->
<%@ include file="/ekms/includes/header.jsp" %>
<x:display name="tsDetPage.tsDet"></x:display>
<%@ include file="/ekms/includes/footer.jsp" %>
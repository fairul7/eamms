<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.timesheet.ui.TimeSheetVTForm,
                 com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.collab.timesheet.ui.TimeSheetViewTaskForm"%>
 <%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: May 5, 2005
  Time: 11:07:05 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="tsViewPage">
        <com.tms.collab.timesheet.ui.TimeSheetViewTaskForm name="tsViewForm"/>
    </page>
</x:config>

<c:if test="${forward.name=='view'}">
    <%
        WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
        TimeSheetViewTaskForm f = (TimeSheetViewTaskForm)wm.getWidget("tsViewPage.tsViewForm");
        pageContext.setAttribute("taskid",f.getTaskId());
        pageContext.setAttribute("projectid",f.getProjectId());
    %>
    <c:redirect url="TimeSheetForm.jsp?taskid=${taskid}&projectid=${projectid}&set=true"/>
</c:if>

<c:if test="${!empty param.projectid}">
    <x:set name="tsViewPage.tsViewForm" property="projectId" value="${param.projectid}"/>
    <x:set name="tsViewPage.tsViewForm" property="set" value="${true}"/>
</c:if>

<jsp:include page="/ekms/init.jsp"/>
<c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
<modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
<link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">

<!-- display page -->

<table width="100%" cellpadding="0" cellspacing="0">
<tr>
    <td>
<x:display name="tsViewPage.tsViewForm"></x:display>
    </td>
</tr>
</table>

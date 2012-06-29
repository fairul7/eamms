<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="pageNonProject">
        <com.tms.collab.timesheet.ui.TimeSheetNonProjectView name="project"/>
        <com.tms.collab.timesheet.ui.TimeSheetNonTaskView name="task"/>
    </page>
</x:config>

<c:if test="${!empty param.projectid}">
    <x:set name="pageNonProject.project" property="projectId" value="${param.projectid}"/>
    <x:set name="pageNonProject.project" property="print" value="${false}"/>
</c:if>

<%--
<jsp:include page="/ekms/init.jsp"/>
<c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
<modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
<link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
--%>
<%@include file="/ekms/includes/header.jsp" %>
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
<td>
<x:display name="pageNonProject.project"/>
</td>
</tr>
<tr>
    <td><spacer type="block" height="1"></td>
</tr>
<tr>
    <td class="tableBackground">&nbsp;</td>
</tr>
<tr>
    <td>
<c:forEach items="${widgets['pageNonProject.project'].task}" var="t">
<x:set name="pageNonProject.task" property="header" value="${false}"/>
<x:set name="pageNonProject.task" property="taskId" value="${t.id}"></x:set>
<x:set name="pageNonProject.task" property="print" value="${false}"/>
<table width="100%" cellpadding="4" cellspacing="0" class="tableBackground">
<tr>
    <td colspan="3" class="contentBgColor" bgcolor="#efefef" >
    <b><fmt:message key="timesheet.label.task"/>:</b>
    <a href="#" onclick="window.open('../calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=<c:out value="${t.id}"/>')"><c:out value="${t.title}"/></a>
    </td>
</tr>
<tr>
    <td width="1%">&nbsp;</td>
    <td class="contentBgColor" bgcolor="#efefef">
    <b><fmt:message key="timesheet.label.startdate"/>:</b> <fmt:formatDate value="${t.startDate}" pattern="${globalDateLong}"/>
    &nbsp;&nbsp;&nbsp;
    <b><fmt:message key="timesheet.label.duedate"/>:</b> <fmt:formatDate value="${t.dueDate}" pattern="${globalDateLong}"/>
    </td>
    <td width="1%">&nbsp;</td>
</tr>
<tr>
    <td width="1%">&nbsp;</td>
    <td><x:display name="pageNonProject.task"/></td>
    <td width="1%">&nbsp;</td>
</tr>
</table>
</c:forEach>
<c:forEach items="${widgets['pageNonProject.project'].noTSTask}" var="ntst">
<x:set name="pageNonProject.task" property="header" value="${false}"/>
<x:set name="pageNonProject.task" property="taskId" value="${ntst.id}"></x:set>
<table width="100%" cellpadding="4" cellspacing="0" class="tableBackground">
<tr>
    <td colspan="3" class="contentBgColor" bgcolor="#efefef" >
    <b><fmt:message key="timesheet.label.task"/>:</b>
    <a href="#" onclick="window.open('../calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=<c:out value="${ntst.id}"/>')"><c:out value="${ntst.title}"/></a>
    </td>
</tr>
<tr>
    <td width="1%">&nbsp;</td>
    <td class="contentBgColor" bgcolor="#efefef">
    <b><fmt:message key="timesheet.label.startdate"/>:</b> <fmt:formatDate value="${ntst.startDate}" pattern="${globalDateLong}"/>
    &nbsp;&nbsp;&nbsp;
    <b><fmt:message key="timesheet.label.duedate"/>:</b> <fmt:formatDate value="${ntst.dueDate}" pattern="${globalDateLong}"/>
    </td>
    <td width="1%">&nbsp;</td>
</tr>
<tr>
    <td width="1%">&nbsp;</td>
    <td><x:display name="pageNonProject.task"/></td>
    <td width="1%">&nbsp;</td>
</tr>
</table>
</c:forEach>
    </td>
</tr>
</table>
<%@include file="/ekms/includes/footer.jsp" %>


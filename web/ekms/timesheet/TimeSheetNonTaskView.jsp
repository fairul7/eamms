<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp"%>

<x:config>
    <page name="tsNonTaskViewPage">
        <com.tms.collab.timesheet.ui.TimeSheetNonTaskView name="tsNonTaskView"/>
    </page>
</x:config>

<c:if test="${!empty param.taskid}">
    <x:set name="tsNonTaskViewPage.tsNonTaskView" property="taskId" value="${param.taskid}"/>
</c:if>
<c:if test="${!empty param.header}">
    <x:set name="tsNonTaskViewPage.tsNonTaskView" property="header" value="${false}"/>
</c:if>

<%--
<jsp:include page="/ekms/init.jsp"/>
<c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
<modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
<link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
--%>

<!-- display page -->
<%@include file="/ekms/includes/header.jsp" %>
<x:display name="tsNonTaskViewPage.tsNonTaskView"></x:display>
<%@include file="/ekms/includes/footer.jsp" %>
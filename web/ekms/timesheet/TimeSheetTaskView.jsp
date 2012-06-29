<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
 <%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 27, 2005
  Time: 3:40:04 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp"%>

<x:config>
    <page name="tsTaskViewPage">
        <com.tms.collab.timesheet.ui.TimeSheetTaskView name="tsTaskView"/>
    </page>
</x:config>

<c:if test="${!empty param.taskid}">
    <x:set name="tsTaskViewPage.tsTaskView" property="taskId" value="${param.taskid}"/>
</c:if>
<c:if test="${!empty param.header}">
    <x:set name="tsTaskViewPage.tsTaskView" property="header" value="${false}"/>
</c:if>

<%--
<jsp:include page="/ekms/init.jsp"/>
<c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
<modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
<link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
--%>

<!-- display page -->
<%@include file="/ekms/includes/header.jsp" %>
<x:display name="tsTaskViewPage.tsTaskView"></x:display>
<%@include file="/ekms/includes/footer.jsp" %>
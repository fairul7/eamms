<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ page import="com.tms.collab.taskmanager.ui.TaskSetDateForm"%>
<%@include file="/common/header.jsp"%>

<x:config >
    <page name="taskAdSetDateForm" >
        <com.tms.collab.taskmanager.ui.TaskAdSetDateForm name="adSetDateform"/>
    </page>
</x:config>

<c-rt:set var="success" value="<%=TaskSetDateForm.FORWARD_SUCCESS%>"></c-rt:set>
<c-rt:set var="cancel" value="<%=TaskSetDateForm.FORWARD_CANCEL%>"></c-rt:set>
<c-rt:set var="fail" value="<%=TaskSetDateForm.FORWARD_FAIL%>"></c-rt:set>

<c:if test="${forward.name == success}" >
    <script>
        alert("<fmt:message key='taskmanager.label.editCompletionDateSuccessfully'/>!");
        window.opener.location = "<c:url value="/ekms/calendar/calendar.jsp" ></c:url>";
        window.close();
    </script>
</c:if>

<c:if test="${cancel == forward.name}" >
    <script>
        window.close();
    </script>
</c:if>

<c:if test="${forward.name=='INVALID'}">
    <script>
        alert("<fmt:message key='taskmanager.label.editCompletionDateInvalid'/>");
    </script>
</c:if>

<c:if test="${forward.name=='INVALID_DATE'}">
    <script>
        alert("<fmt:message key='taskmanager.label.CompletionDateInvalid'/>");
    </script>
</c:if>



<c:if test="${!empty param.taskId}" >
    <x:set name="taskAdSetDateForm.adSetDateform" property="taskId" value="${param.taskId}" />
</c:if>

<c:if test="${!empty param.userId}" >
    <x:set name="taskAdSetDateForm.adSetDateform" property="userId" value="${param.userId}" />
</c:if>
<c:if test="${empty param.userId}">
    <x:set name="taskAdSetDateForm.adSetDateform" property="userId" value="" />
</c:if>



    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">


<html>
    <TITLE> <fmt:message key='taskmanager.label.taskAdSetDate'/>
    </TITLE>
    <body>
        <x:display name="taskAdSetDateForm.adSetDateform" />
    </body>
</html>

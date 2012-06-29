<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.collab.taskmanager.ui.ProgressForm"%>
<%@ page import="com.tms.collab.taskmanager.ui.TaskSetDateForm"%>
<%@include file="/common/header.jsp"%>

<x:config >
    <page name="taskSetDateForm" >
        <com.tms.collab.taskmanager.ui.TaskSetDateForm name="setDateform"/>
    </page>
</x:config>

<c-rt:set var="success" value="<%=TaskSetDateForm.FORWARD_SUCCESS%>"></c-rt:set>
<c-rt:set var="successStart" value="<%=TaskSetDateForm.FORWARD_SUCCESS_START%>"></c-rt:set>
<c-rt:set var="cancel" value="<%=TaskSetDateForm.FORWARD_CANCEL%>"></c-rt:set>
<c-rt:set var="fail" value="<%=TaskSetDateForm.FORWARD_FAIL%>"></c-rt:set>

<c:if test="${forward.name == success}" >
    <script>
        alert("<fmt:message key='taskmanager.label.completedDateSetSuccessfully'/>!");
        window.opener.location = "<c:url value="/ekms/calendar/calendar.jsp" ></c:url>";
        window.close();
    </script>
</c:if>
<c:if test="${forward.name == successStart}" >
    <script>
        alert("<fmt:message key='taskmanager.label.startDateSetSuccessfully'/>!");
        window.opener.location = "<c:url value="/ekms/calendar/calendar.jsp" ></c:url>";
        window.close();
    </script>
</c:if>

<c:if test="${cancel == forward.name}" >
    <script>
        window.close();
    </script>
</c:if>

<c:if test="${forward.name=='INVALID_DATE' }">
	<script>
        alert("<fmt:message key='taskmanager.label.CompletionDateInvalid'/>");
	</script>
</c:if>


<c:if test="${!empty param.taskId}" >
    <x:set name="taskSetDateForm.setDateform" property="taskId" value="${param.taskId}" />
</c:if>

<c:if test="${!empty param.taskEventType}" >
    <x:set name="taskSetDateForm.setDateform" property="taskEventType" value="${param.taskEventType}" />
</c:if>
<c:if test="${empty param.taskEventType}">
    <x:set name="taskSetDateForm.setDateform" property="taskEventType" value="" />
</c:if>



    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">


<html>
    <TITLE> <fmt:message key='taskmanager.label.taskSetDate'/>
    </TITLE>
    <body>
        <x:display name="taskSetDateForm.setDateform" />
    </body>
</html>

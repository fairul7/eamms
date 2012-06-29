<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ page import="com.tms.collab.taskmanager.ui.TaskCommentsForm"%>
<%@include file="/common/header.jsp"%>

<x:config >
    <page name="taskCommentsForm" >
        <com.tms.collab.taskmanager.ui.TaskCommentsForm name="commentsForm"/>
    </page>
</x:config>

<c-rt:set var="success" value="<%=TaskCommentsForm.FORWARD_SUCCESS%>"></c-rt:set>
<c-rt:set var="cancel" value="<%=TaskCommentsForm.FORWARD_CANCEL%>"></c-rt:set>
<c-rt:set var="fail" value="<%=TaskCommentsForm.FORWARD_FAIL%>"></c-rt:set>

<c:if test="${forward.name == success}" >
    <script>
        alert("<fmt:message key='taskmanager.label.commentsAddSuccessfully'/>!");
        window.opener.location = "<c:url value="/ekms/calendar/calendar.jsp" ></c:url>";
        window.close();
    </script>
</c:if>

<c:if test="${cancel == forward.name}" >
    <script>
        window.close();
    </script>
</c:if>



<c:if test="${!empty param.taskId}" >
    <x:set name="taskCommentsForm.commentsForm" property="taskId" value="${param.taskId}" />
</c:if>



    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">


<html>
    <TITLE> <fmt:message key='taskmanager.label.taskComments'/>
    </TITLE>
    <body>
        <x:display name="taskCommentsForm.commentsForm" />
    </body>
</html>

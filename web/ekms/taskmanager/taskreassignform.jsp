<%@ page import="com.tms.collab.taskmanager.ui.ReassignForm,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp"%>



<x:config >
    <page name="reassignformpage">
        <com.tms.collab.taskmanager.ui.ReassignForm name="reassignform"/>
    </page>
</x:config>

<c:if test="${!empty param.taskId}" >
    <x:set name="reassignformpage.reassignform" property="taskId" value="${param.taskId}" />
</c:if>

<c-rt:set var="reassigned" value="<%=ReassignForm.FORWARD_REASSIGNED%>"  />
<c:if test="${forward.name ==reassigned }">
    <script>
        alert("<fmt:message key='taskmanager.label.taskreassignedsuccessfully'/>!");
        window.opener.location = "<c:url value="/ekms/calendar/calendar.jsp" ></c:url>";
        window.close();
    </script>
</c:if>


<c-rt:set var="cancel" value="<%=ReassignForm.FORWARD_CANCEL%>"  />
<c:if test="${forward.name == cancel}" >
    <script>
        window.close();
    </script>
</c:if>



    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">


<html>
    <TITLE> <fmt:message key='taskmanager.label.taskReassignment'/>
    </TITLE>
    <body>
        <x:display name="reassignformpage.reassignform" ></x:display>
    </body>


</html>

<%@ page import="com.tms.collab.emeeting.ui.MMeetingActionItemsForm,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp"%>
<x:config>
    <page name="meetingActionItemsPage">
        <com.tms.collab.emeeting.ui.MMeetingActionItemsForm name="actionItemsForm" template="emeeting/mmeetingActionItems"/>
    </page>
</x:config>

<c:set var="eventId" value="${param.eventId}"/>
<c:if test="${empty param.eventId}">
    <c:set var="eventId" value="${widgets['meetingActionItemsPage.actionItemsForm'].eventId}"/>
</c:if>

<c:if test="${forward.name == 'refresh'}" >
     <x:set name="meetingActionItemsPage.actionItemsForm" property="eventId" value="${eventId}"/>
    <script>
        document.location = '<%=response.encodeURL(request.getRequestURI())%>';
        window.opener.location.reload();
    </script>
</c:if>
<c-rt:set var="forward_assign" value="<%= MMeetingActionItemsForm.FORWARD_ASSIGN_TASK%>"/>
<c-rt:set var="forward_edit" value="<%= MMeetingActionItemsForm.FORWARD_EDIT_TASK%>"/>
<c:set var="selectedItem" value="${widgets['meetingActionItemsPage.actionItemsForm']}"/>
<c:if test="${! empty selectedItem}">
    <c:if test="${forward.name == forward_assign}">
        <script>window.open("<c:url value="/mekms/calendar/"/>emeetingTask.jsp?itemId=<c:out value="${selectedItem.itemId}"/>&taskId=-1", "emeetingTask", "height=400,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");</script>
    </c:if>
    <c:if test="${forward.name == forward_edit}">
        <script>window.open("<c:url value="/mekms/calendar/"/>emeetingTask.jsp?itemId=<c:out value="${selectedItem.itemId}"/>&taskId=<c:out value="${selectedItem.item.taskId}"/>", "emeetingTask", "height=400,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");</script>
    </c:if>
</c:if>

<html>
<head>
    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
    <link rel="stylesheet" href="../images/default.css">
    <title><fmt:message key='calendar.label.meetingAgendaItems'/></title>
</head>
<body>
    <x:set name="meetingActionItemsPage.actionItemsForm" property="mobile" value="${true}"/>
    <x:set name="meetingActionItemsPage.actionItemsForm" property="eventId" value="${eventId}"/>
    <x:set name="meetingActionItemsPage.actionItemsForm" property="itemId" value="${param.itemId}"/>
    <x:display name="meetingActionItemsPage.actionItemsForm"/>
</body>
</html>

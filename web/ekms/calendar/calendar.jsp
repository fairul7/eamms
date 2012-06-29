<%@ page import="com.tms.portlet.ui.PortalServer,
                 com.tms.collab.calendar.ui.CalendarView"%>
<%@ include file="/common/header.jsp" %>

<x:config >
    <page name="calendarPage">
        <calendarview name="calendarView" view="daily"/>
    </page>
</x:config>

<c:if test="${! empty param.from}">

    <c:set var="from" value="task" scope="session" />

</c:if>

<c:if test="${forward.name=='Appointment deleted'}">
    <script>
        alert("<fmt:message key='calendar.message.appointmentDeleted'/>");
    </script>
    <x:set name="calendarPage.calendarView" property="view" value="<%=CalendarView.VIEW_DAILY%>" ></x:set>
</c:if>

<c:if test="${forward.name=='CalendarEvent deleted'}">
    <script>
        alert("<fmt:message key='calendar.message.eventDeleted'/>");
    </script>
    <x:set name="calendarPage.calendarView" property="view" value="<%=CalendarView.VIEW_DAILY%>" ></x:set>
</c:if>

<c:if test="${forward.name=='Task deleted'}">
    <script>
        alert("<fmt:message key='calendar.message.toDoTaskDeleted'/>");
    </script>
    <x:set name="calendarPage.calendarView" property="view" value="<%=CalendarView.VIEW_DAILY%>" ></x:set>
</c:if>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellpadding="5" cellspacing="0" valign="top">
    <tr valign="top"><td align="center" valign="top"><x:display name="calendarPage.calendarView" /></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>

<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.collab.calendar.model.CalendarEvent,
                 com.tms.collab.calendar.model.Appointment,
                 com.tms.collab.emeeting.Meeting,
                 com.tms.collab.taskmanager.model.Task"%>
<%@include file="/common/header.jsp" %>

<x:config >
    <page name="eventViewPage">
        <com.tms.collab.calendar.ui.CalendarEventView name="eventView"/>
    </page>
</x:config>

<c:if test="${!empty param.eventId}" >
    <x:set name="eventViewPage.eventView" property="eventId" value="${param.eventId}" />
    <x:set name="eventViewPage.eventView" property="hiddenAction" value="${true}" />
</c:if>

<html>
<title><fmt:message key='calendar.label.viewDetails'/></title>
<jsp:include page="/ekms/init.jsp"/>
<c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
<modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
<link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
<table border="0" cellpadding="0" cellspacing="0" width="100%"> <tr><td align="Center">
    <B>
        <%
            String eventId = request.getParameter("eventId");
            if(eventId!=null){
                if(eventId.startsWith(CalendarEvent.class.getName())){
                    out.print("Event");
                }
                else if(eventId.startsWith(Appointment.class.getName()))
                    out.print("Appointment");
                else if(eventId.startsWith(Meeting.class.getName()))
                    out.print("E-Meeting");
                else if(eventId.startsWith(Task.class.getName()))
                    out.print("To Do Task");
            }
        %>
    </B></td></tr>

</table>
<x:display name="eventViewPage.eventView" ></x:display>

<table border="0" cellpadding="0" cellspacing="0" width="100%"> <tr><td align="Center">
    <INPUT TYPE="BUTTON" CLASS="button" value="Back" onClick="history.back()"/>
</td></tr>
</table>

</html>

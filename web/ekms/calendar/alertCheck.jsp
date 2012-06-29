<%@ page import="kacang.Application,
                 com.tms.collab.calendar.model.CalendarUtil,
                 com.tms.collab.calendar.model.CalendarEvent"%>
<%@ include file="/common/header.jsp" %>

<c:set var="apptPortlet" value="${widgets['appointmentportletpage.appointmentportlet']}"/>

<c:if test="${!empty apptPortlet}">

<x:display name="appointmentportletpage.appointmentportlet" body="custom">

    <html>
    <head>
        <meta http-equiv="Refresh" content="30">
        <link rel="stylesheet" href="/ekms/images/default/default.css">
        <script>
        <!--
            function alertPopup(windowName, eventId, instanceId) {
                var url='<c:url value="/ekms/calendar/alertPopup.jsp"/>?eventId=' + eventId + '&instanceId=' + escape(instanceId);
                myWin = window.open(url, windowName, "height=300,width=400,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
                if (myWin) {
                    myWin.focus();
                }
            }
        //-->
        </script>
    </head>

    <body>

    <c-rt:set var="currentDate" value="<%= new java.util.Date() %>"/>
    <c-rt:set var="currentTime" value="<%= new Long(System.currentTimeMillis()) %>"/>
    <c:forEach items="${apptPortlet.appointments}" var="appointment">
        <c:if test="${!appointment.reminder}">
        <%
            CalendarEvent appt = (CalendarEvent)pageContext.findAttribute("appointment");
            boolean showAlert = CalendarUtil.checkForReminderAlerts(request, appt, null);
            if (showAlert) {
                String eventId = appt.getEventId();
                String windowName;
                try {
                    windowName = eventId.substring(appt.getClass().getName().length());
                    windowName = windowName.replace('-','x');
                }
                catch (Exception e) {
                    windowName = "alertCheck";
                }
        %>
                <script>
                <!--
                    alertPopup('<%= windowName%>','<c:out value="${appointment.eventId}"/>','<c:out value="${appointment.instanceId}"/>');
                //-->
                </script>
        <%
            }
        %>

<%-- // For debugging

            <li><c:out value="${sessionAlertMap}"/>
            <li><c:out value="${appointment.eventId}"/>
            <li><c:out value="${sessionAlertMap[appointment.eventId] != 'TRUE'}"/>
            <li><c:out value="${currentDate}"/>
            <li><c:out value="${appointment.startDate}"/>
            <li><c:out value="${appointment.reminderDate}"/>
            <li><c:out value="${currentTime < startTime && currentTime >= reminderTime}"/>
            <hr>
--%>
        </c:if>
    </c:forEach>

    </body>
    </html>

</x:display>

</c:if>



<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.calendar.ui.CalendarForm"%>
<%@include file="/common/header.jsp"%>

<!-- Page/Widget Definition -->
<x:config reloadable="${param.reload}">
    <page name="appointmentformpage">
        <calendarform name="AppointmentForm" prefix="com.tms.collab.calendar.model.CalendarEvent"/>
    </page>
</x:config>

<c:if test="${forward.name=='cancel'}" >
    <script>
        document.location = "calendar.jsp";
    </script>
</c:if>

<c:if test="${forward.name=='event added'}" >
    <script>
        alert("<fmt:message key='calendar.message.newEventAddedSuccessfully'/>");
        <%
            WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
            if(wm!=null){
                CalendarForm form = (CalendarForm)wm.getWidget("appointmentformpage.AppointmentForm");
        %>
        document.location = "<c:url value="/ekms/calendar/calendar.jsp" ></c:url>?cn=calendarPage.calendarView&et=select&eventId=<%out.print(form.getEvent().getEventId());}%>";
    </script>
</c:if>

<c:if test="${forward.name=='conflict exception'}">
    <%    session.setAttribute("edit",Boolean.FALSE);%>
    <c:redirect url="/ekms/calendar/conflicts.jsp" ></c:redirect>
</c:if>

<c:if test="${!empty param.init}">
    <x:set name="appointmentformpage.AppointmentForm" property="init" value="1"/>
</c:if>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellpadding="5" cellspacing="1">
    <tr><td class="calendarHeader"><fmt:message key='calendar.label.calendar'/> > <fmt:message key='calendar.label.addNewEvent'/></td></tr>  </table>
<table width="100%" border="0" cellpadding="0" cellspacing="0"        >
    <tr><td align="center"><x:display name="appointmentformpage.AppointmentForm" >
    </td></tr></x:display></table>
<table width="100%" border="0" cellpadding="5" cellspacing="0">
    <tr><td class="calendarFooter">&nbsp;</td></tr>  </table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>

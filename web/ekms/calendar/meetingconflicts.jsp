<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.calendar.ui.ConflictForm,
                 com.tms.collab.calendar.model.ConflictException,
                 com.tms.collab.calendar.model.CalendarEvent,
                 java.util.Collection,
                 com.tms.collab.emeeting.Meeting,
                 com.tms.collab.emeeting.ui.MeetingConflictForm,
                 java.util.Iterator"%>
<%@include file="/common/header.jsp" %>

<x:config >
    <page name="conflictpage">
        <com.tms.collab.emeeting.ui.MeetingConflictForm name="conflictform"/>
    </page>
</x:config>

<c:choose>
    <c:when test="${!empty param.new}">
        <x:set name="conflictpage.conflictform" property="newflag" value="1" ></x:set>
    </c:when>
    <c:otherwise>
        <x:set name="conflictpage.conflictform" property="newflag" value="0" ></x:set>
    </c:otherwise>
</c:choose>

<c:if test="${forward.name == 'added'}" >
    <script>
        document.location = "<c:url value="/ekms/calendar/editemeetingform.jsp?eventId=${widgets['conflictpage.conflictform'].meeting.event.eventId}&new=1" />";
    </script>
</c:if>

<c:if test="${forward.name == 'updated'}" >
    <script>
        <%      WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
            MeetingConflictForm cview = (MeetingConflictForm)wm.getWidget("conflictpage.conflictform");
            String header = cview.getHeader();
          %>
        alert("<%=header%> <fmt:message key='calendar.message.updatedSuccessfully'/>");
        document.location = "<c:url value="/ekms/calendar/calendar.jsp" ></c:url>?cn=calendarPage.calendarView&et=select&eventId=<%out.print(cview.getMeeting().getEventId());%>";
    </script>
</c:if>

<c:if test="${forward.name == 'cancel'}" >
    <%
        WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
        MeetingConflictForm cview = (MeetingConflictForm)wm.getWidget("conflictpage.conflictform");
        System.out.println(cview.toString());
        String id =cview.getMeeting().getEvent().getEventId();
        pageContext.setAttribute("id",id.substring(0,id.indexOf("_")));
    %>
    <c:redirect url="/ekms/calendar/emeetingform.jsp" ></c:redirect>
</c:if>

<c-rt:set var="viewall" value="<%=ConflictForm.FORWARD_VIEW_ALL%>"/>
<c:if test="${forward.name == viewall}" >
    <%
        WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
        if(wm!=null){
            ConflictForm cview = (ConflictForm)wm.getWidget("conflictpage.conflictform");
            if(cview!=null){
                session.setAttribute("conflictList",cview.getConflicts());
                session.setAttribute("userId","");
                session.setAttribute("resourceId","");
                session.setAttribute("resourceList",cview.getResourceConflicts());
            }
        }
    %>
    <script>
        window.open("conflictview.jsp","","resizable=yes,width=450,height=400,scrollbars=yes");
    </script>
</c:if>

<c-rt:set var="viewuser" value="<%=ConflictForm.FORWARD_VIEW_USER%>"/>
<c:if test="${forward.name==viewuser}" >
    <%
        WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
        if(wm!=null){
            ConflictForm cview = (ConflictForm)wm.getWidget("conflictpage.conflictform");
            if(cview!=null){
                session.setAttribute("conflictList",cview.getConflicts());
                session.setAttribute("userId",cview.getViewUserId());
                session.setAttribute("resourceId","");
                session.setAttribute("resourceList",null);
            }
        }
    %>
    <script>
        window.open("conflictview.jsp","","resizable=yes,width=450,height=400,scrollbars=yes");
    </script>
</c:if>

<c-rt:set var="viewresource" value="<%=ConflictForm.FORWARD_VIEW_RESOURCE%>"/>
<c:if test="${forward.name == viewresource}" >
    <%
        WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
        if(wm!=null){
            ConflictForm cview = (ConflictForm)wm.getWidget("conflictpage.conflictform");
            if(cview!=null){
/*
                session.setAttribute("conflictList",cview.getConflicts());
*/
                session.setAttribute("userId"," ");
                session.setAttribute("resourceId",cview.getViewResourceId());
                session.setAttribute("resourceList",cview.getResourceConflicts());
            }
        }
    %>
    <script>
        window.open("conflictview.jsp","","resizable=yes,width=450,height=400,scrollbars=yes");
    </script>
</c:if>

<%
    WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
    if(wm!=null){
        MeetingConflictForm cview = (MeetingConflictForm)wm.getWidget("conflictpage.conflictform");
        if(cview!=null){
            ConflictException exception = (ConflictException)session.getAttribute("conflict");
            Meeting d= ((Meeting)session.getAttribute("event"));
            if(d!=null)
            {
                cview.setEvent(d);
            }
            cview.setConflicts(exception.getConflictList());
            cview.setResourceConflicts(exception.getResourcesList());
            cview.setUpdate(((Boolean)session.getAttribute("edit")).booleanValue());
            cview.setRecurringEvents((Collection)session.getAttribute("recurringEvents"));
        }
    }
%>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<%--<table width="100%" border="0" cellpadding="5" cellspacing="1">
    <tr><td class="calendarHeader"><fmt:message key='calendar.label.addNewAppointment'/></td></tr>  </table>
    <table width="100%" border="0" cellpadding="0" cellspacing="0"        >
    <tr><td align="center"><x:display name="appointmentformpage.AppointmentForm" >
    </td></tr></x:display></table>
    <table width="100%" border="0" cellpadding="5" cellspacing="0">
    <tr><td class="calendarFooter">&nbsp;</td></tr>  </table>--%>
<x:display name="conflictpage.conflictform" ></x:display>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>

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
    <page name="meetingconflictpage">
        <com.tms.collab.emeeting.ui.MeetingConflictForm name="conflictform"/>
    </page>
</x:config>
<c:if test="${forward.name == 'added'}" >
    <script>
        <%
            WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
            MeetingConflictForm cview = (MeetingConflictForm)wm.getWidget("meetingconflictpage.conflictform");
            String header = cview.getHeader();
          %>
        alert("<%=header%> <fmt:message key='calendar.message.addedSuccessfully'/>");
        document.location = "<c:url value="/mekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=${widgets['conflictpage.conflictform'].meeting.event.eventId}" />";
    </script>
</c:if>
<c:if test="${forward.name == 'updated'}" >
    <script>
        <%
            WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
            MeetingConflictForm cview = (MeetingConflictForm)wm.getWidget("meetingconflictpage.conflictform");
            String header = cview.getHeader();
          %>
        alert("<%=header%> <fmt:message key='calendar.message.updatedSuccessfully'/>");
        document.location = "<c:url value="/mekms/calendar/calendar.jsp" ></c:url>?cn=calendarPage.calendarView&et=select&eventId=<%out.print(cview.getMeeting().getEventId());%>";
    </script>
</c:if>
<c:if test="${forward.name == 'cancel'}" >
    <script>history.back();</script>
</c:if>
<c-rt:set var="viewall" value="<%=ConflictForm.FORWARD_VIEW_ALL%>"/>
<c:if test="${forward.name == viewall}" >
    <%
          WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
          if(wm!=null){
              ConflictForm cview = (ConflictForm)wm.getWidget("meetingconflictpage.conflictform");
              if(cview!=null){
                  session.setAttribute("conflictList",cview.getConflicts());
                  session.setAttribute("userId","");
                  session.setAttribute("resourceId","");
                  session.setAttribute("resourceList",cview.getResourceConflicts());
              }
          }
    %>
    <script>window.open("conflictview.jsp","","resizable=yes,width=450,height=400,scrollbars=yes");</script>
</c:if>
<c-rt:set var="viewuser" value="<%=ConflictForm.FORWARD_VIEW_USER%>"/>
<c:if test="${forward.name==viewuser}" >
      <%
          WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
          if(wm!=null){
              ConflictForm cview = (ConflictForm)wm.getWidget("meetingconflictpage.conflictform");
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
<%
    WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
    if(wm!=null){
        MeetingConflictForm cview = (MeetingConflictForm)wm.getWidget("meetingconflictpage.conflictform");
        if(cview!=null){
            ConflictException exception = (ConflictException)session.getAttribute("conflict");
            Meeting d= ((Meeting)session.getAttribute("event"));
            if(d!=null)
                cview.setEvent(d);
            cview.setConflicts(exception.getConflictList());
            cview.setResourceConflicts(exception.getResourcesList());
            cview.setUpdate(((Boolean)session.getAttribute("edit")).booleanValue());
            cview.setRecurringEvents((Collection)session.getAttribute("recurringEvents"));
        }
    }
%>
<c:set var="headerCaption" scope="request"><fmt:message key="emeeting.label.conflictsDetected"/></c:set> 
<jsp:include page="../includes/mheader.jsp"/>
<table width="100%" border=0 cellPadding=2 cellSpacing=0>
    <tr valign="top"><td><x:display name="meetingconflictpage.conflictform"/></td></tr>
</table>
<jsp:include page="../includes/mfooter.jsp"/>
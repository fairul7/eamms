<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.calendar.ui.ConflictForm,
                 com.tms.collab.calendar.model.ConflictException,
                 com.tms.collab.calendar.model.CalendarEvent,
                 java.util.Collection,
                 com.tms.collab.calendar.model.Appointment,
                 com.tms.collab.emeeting.Meeting"%>
<%@include file="/common/header.jsp" %>


<x:config >
    <page name="conflictpage">
        <com.tms.collab.calendar.ui.ConflictForm name="conflictform"/>
    </page>
</x:config>
<c-rt:set var="viewall" value="<%=ConflictForm.FORWARD_VIEW_ALL%>"/>
<c-rt:set var="viewuser" value="<%=ConflictForm.FORWARD_VIEW_USER%>"/>
<c-rt:set var="viewresource" value="<%=ConflictForm.FORWARD_VIEW_RESOURCE%>"/>

<c:choose>
<c:when test="${forward.name == 'added'}" >
    <script>
        <%      WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
            ConflictForm cview = (ConflictForm)wm.getWidget("conflictpage.conflictform");
            String header = cview.getHeader();
            
          %>
        alert("<%=header%> <fmt:message key='calendar.message.addedSuccessfully'/> ");
        document.location = "<c:url value="/ekms/calendar/calendar.jsp" ></c:url>?cn=calendarPage.calendarView&et=select&eventId=<%out.print(cview.getEvent().getEventId());%>";

    </script>
</c:when>

<c:when test="${forward.name == 'updated'}" >
    <script>
        <%      WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
            ConflictForm cview = (ConflictForm)wm.getWidget("conflictpage.conflictform");
            String header = cview.getHeader();
            
          %>
        alert("<%=header%> <fmt:message key='calendar.message.updatedSuccessfully'/>");
        document.location = "<c:url value="/ekms/calendar/calendar.jsp" ></c:url>?cn=calendarPage.calendarView&et=select&eventId=<%out.print(cview.getEvent().getEventId());%>";
    </script>
</c:when>

<c:when test="${forward.name == 'cancel'}" >
<%
    WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
    ConflictForm cview = (ConflictForm)wm.getWidget("conflictpage.conflictform");
    String id =cview.getEvent().getEventId();
    pageContext.setAttribute("id",id.substring(0,id.indexOf("_")));
    pageContext.setAttribute("appointment", Appointment.class.getName());
    pageContext.setAttribute("event",CalendarEvent.class.getName());
    pageContext.setAttribute("emeeting",Meeting.class.getName());
%>
<c:choose>
    <c:when test="${id == appointment}" >
        <c:redirect url="/ekms/calendar/appointmentform.jsp" ></c:redirect>
    </c:when>
    <c:when test="${id == event}" >
        <c:redirect url="/ekms/calendar/eventform.jsp" ></c:redirect>
    </c:when>
    <c:when test="${id == emeeting}" >
        <c:redirect url="/ekms/calendar/emeetingform.jsp" ></c:redirect>
    </c:when>
</c:choose>



<%--
<script>
    history.back();
    history.back();
</script>
--%>
</c:when>

<c:when test="${forward.name == viewall}" >
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
</c:when>

<c:when test="${forward.name==viewuser}" >
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
</c:when>

<c:when test="${forward.name == viewresource}" >
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
</c:when>

<c:otherwise>

      <%
          WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
          if(wm!=null){
              ConflictForm cview = (ConflictForm)wm.getWidget("conflictpage.conflictform");
              if(cview!=null){
                  ConflictException exception = (ConflictException)session.getAttribute("conflict");
                  CalendarEvent d= ((CalendarEvent)session.getAttribute("event"));
                  if(d!=null)
                  {
                      cview.setEvent(d);

                  }
                  cview.setConflicts(exception.getConflictList());

                  cview.setResourceConflicts(exception.getResourcesList());
                  cview.setUpdate(((Boolean)session.getAttribute("edit")).booleanValue());
                  if(session.getAttribute("recurringEvents")!=null)
                      cview.setRecurringEvents((Collection)session.getAttribute("recurringEvents"));
                  else
                      cview.setRecurringEvents(null);
              }
          }
         %>
  </c:otherwise>
</c:choose>
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

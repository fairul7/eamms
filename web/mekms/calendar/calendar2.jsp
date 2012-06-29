<%@ page import="com.tms.portlet.ui.PortalServer,
                 com.tms.collab.calendar.ui.MCalendarView,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:config >
  <page name="calendarPage">
    <com.tms.collab.calendar.ui.MCalendarView name="calendarView" view="mDaily"/>
  </page>
</x:config>

<%
    String sEventId = request.getParameter("eventId")==null?"":request.getParameter("eventId");
    String sHeader = "";
    if (!sEventId.equals("")) {
        sHeader = sEventId.substring(sEventId.lastIndexOf(".")+1,sEventId.indexOf("_"));
    }
    else {
        String et = request.getParameter("et")==null?"":request.getParameter("et");
        if (et.equals("view")) {
            String view = request.getParameter("view")==null?"":request.getParameter("view");
            if (view.equals("daily")) {
                sEventId="";
            }
        }
    }
    pageContext.setAttribute("headline",sHeader);
%>
<% if (!sEventId.equals("")) { %>
<x:set name="calendarPage.calendarView" property="view" value="<%=MCalendarView.VIEW_M_EVENT%>"/>
<% } else { %>
<x:set name="calendarPage.calendarView" property="view" value="<%=MCalendarView.VIEW_M_DAILY%>"></x:set>
<% } %>

<x:set name="calendarPage.calendarView" property="eventUrl" value="calendar.jsp"/>

<c:if test="${!empty param.defaultDay}">
<x:set name="calendarPage.calendarView" property="defaultDay" value="${param.defaultDay}"/>
<x:set name="calendarPage.calendarView" property="view" value="<%=MCalendarView.VIEW_M_DAILY%>"></x:set>
</c:if>

     <jsp:include page="../includes/mheader.jsp"/>

<c:if test="${forward.name=='Appointment deleted'}">
 <script>
    alert("<fmt:message key='calendar.message.appointmentDeleted'/>");
 </script>
<%-- <x:set name="calendarPage.calendarView" property="view" value="<%=CalendarView.VIEW_DAILY%>" ></x:set>--%>
</c:if>
<c:if test="${forward.name=='CalendarEvent deleted'}">
 <script>
    alert("<fmt:message key='calendar.message.eventDeleted'/>");
 </script>
 <%-- <x:set name="calendarPage.calendarView" property="view" value="<%=CalendarView.VIEW_DAILY%>" ></x:set> --%>

</c:if>
<c:if test="${forward.name=='Task deleted'}">
                 <script>
    alert("<fmt:message key='calendar.message.toDoTaskDeleted'/>");
 </script>
<%-- <x:set name="calendarPage.calendarView" property="view" value="<%=CalendarView.VIEW_DAILY%>" ></x:set> --%>

</c:if>


     <jsp:include page="../includes/mheader.jsp" />


        <TABLE cellSpacing=0 cellPadding=0 width=100% border=0>
          <TBODY>
            <TR>
              <TD><FONT class=f06><strong>MOBILE DESKTOP &gt; <a href="<c:url value="/mekms" />/calendar/calendar2.jsp?defaultDay=today" >HOME</a>
                &gt;
                <c:choose>
                    <c:when test="${headline == ''}">DAILY CALENDAR VIEW</c:when>
					<c:when test="${headline == 'Task'}" ><fmt:message key='calendar.label.TODOTASK'/> </c:when>
					<c:when test="${headline=='CalendarEvent'}" ><fmt:message key='calendar.label.EVENT'/> </c:when>
					<c:when test="${headline == 'Meeting'}" ><fmt:message key='calendar.label.E-MEETING'/> </c:when>
					<c:otherwise><fmt:message key='calendar.label.APPOINTMENT'/> </c:otherwise>
				</c:choose>
                <!-- DAILY CALENDAR VIEW -->
                </strong></font></TD>
            </TR>
            <TR>
              <TD height="3"></TD>
            </TR>
            <TR>
              <TD><TABLE cellSpacing=0 cellPadding=0 width=100% border=0>
                  <TBODY>
                    <TR>
                      <TD height=5 bgColor=#6584CD> </TD>
                    </TR>
                  </TBODY>
                </TABLE></TD>
            </TR>
          </TBODY>
        </TABLE>
<TABLE width="100%" border=0 cellPadding=2 cellSpacing=0>
  <TBODY>
    <TR valign="top" class="f07">
      <TD width="18%">


    </TD>
      <TD width="82%">
    <table width="100%" border="0" cellpadding="5" cellspacing="0" valign="top">
            <tr valign="top"><td align="center" valign="top">
    <x:display name="calendarPage.calendarView" />
            </td></tr>
    </table>

    </TD>
  </TR>
</TBODY>
</TABLE>

<jsp:include page="../includes/mfooter.jsp" />


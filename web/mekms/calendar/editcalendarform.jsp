<%@ page import="com.tms.collab.calendar.ui.MCalendarForm,
                 com.tms.collab.calendar.ui.MCalendarUserTable"%>
<%@include file="/common/header.jsp"%>
<!-- Page/Widget Definition -->
<x:config reloadable="${param.reload}">
    <page name="editcalendarformpage">
        <com.tms.collab.calendar.ui.MCalendarForm name="EditAppointmentForm" prefix="com.tms.collab.calendar.model.Appointment" />
    </page>
</x:config>


<c:if test="${! empty param.eventId}" >
    <x:set name="editcalendarformpage.EditAppointmentForm" property="eventId" value="${param.eventId}"/>
</c:if>

<c:if test="${forward.name=='cancel'}" >
    <c:redirect url="/mekms/calendar/calendar2.jsp"/>
</c:if>
<c:if test="${forward.name=='selectAttendees'}">
    <script>
    alert("<fmt:message key="calendar.message.atLeastOneAttendee"/>");
    </script>
</c:if>


<c:if test="${forward.name =='appointment updated' }" >
    <script>
        alert("Appointment updated.");
        location = "MCalendarEventList.jsp?eventType=1";
    </script>
</c:if>

<c:if test="${forward.name=='conflict exception'}">

    <%    session.setAttribute("edit",Boolean.TRUE);%>
    <c:redirect url="/mekms/calendar/conflictsedit.jsp" ></c:redirect>
</c:if>

<%--
<c-rt:set var="selectuser"  value="<%=MCalendarForm.FORWARD_SELECT_USERS%>"/>

<c-rt:set var="selectoptionaluser"  value="<%=MCalendarForm.FORWARD_SELECT_OPTIONAL_USERS%>"/>

<c:if test="${forward.name == selectuser}">
    <c:redirect url="calendarselectusers.jsp?edit=1&atype=1&eventId=${widgets['editcalendarformpage.EditAppointmentForm'].eventId}" />
</c:if>

<c:if test="${forward.name == selectoptionaluser}">
    <c:redirect url="calendarselectusers.jsp?edit=1&atype=2&eventId=${widgets['editcalendarformpage.EditAppointmentForm'].eventId}" />
</c:if>
--%>

<%@include file="../includes/mheader.jsp"%>
<%--
  <jsp:include page="/ekms/init.jsp"/>
  <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
  <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
  <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
--%>

  <jsp:include page="../includes/mheader.jsp" />

          <TABLE cellSpacing=0 cellPadding=0 width=100% border=0>
            <TBODY>
              <TR>
                <TD><FONT class=f06><strong>MOBILE DESKTOP &gt; <a href="<c:url value="/mekms" />/calendar/calendar2.jsp?defaultDay=today">HOME</a>
                  &gt; DAILY CALENDAR VIEW</strong></font></TD>
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

<x:display name="editcalendarformpage.EditAppointmentForm" />

</TD>
  </TR>
</TBODY>
</TABLE>

<jsp:include page="../includes/mfooter.jsp" />
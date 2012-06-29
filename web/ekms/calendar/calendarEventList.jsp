<%@ page import="com.tms.portlet.ui.PortalServer,
                 com.tms.collab.calendar.ui.CalendarView"%>
<%@ include file="/common/header.jsp" %>

<x:config >
  <page name="calendarEventList">
    <com.tms.collab.calendar.ui.CalendarEventListTable name="table" width="100%"/>
  </page>
</x:config>

<c:choose>
    <c:when test="${param.et == 'sel' && !empty param.eventId}">
        <c:redirect url="/ekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=${param.eventId}" />
    </c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellpadding="5" cellspacing="0" valign="top">
    <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp; <fmt:message key='calendar.label.calendar'/> > <fmt:message key='calendar.label.listingCalendarView'/>
    </font></b></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
    <tr><td valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="calendarEventList.table" /></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>

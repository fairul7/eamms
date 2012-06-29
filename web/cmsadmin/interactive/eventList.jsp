<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.collab.calendar.ManageEvents" module="com.tms.collab.calendar.model.CalendarModule" url="noPermission.jsp" />

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerInteractive.jsp" flush="true" />


<x:config>
    <page name="eventList">
        <portlet name="calendarEventPortlet" text="<fmt:message key='calendar.label.eventList'/>" width="100%" permanent="true">
            <com.tms.collab.calendar.ui.CalendarEventTable name="calendarEventTable" width="100%">
                <forward name="add" url="eventAdd.jsp" />
                <listener_script>
                    if (event.getRequest().getParameter("eventId") != null) {
                        editForm = wm.getWidget("eventList.editCalendarEventPortlet.editCalendarEventForm");
                        editForm.setEventId(event.getRequest().getParameter("eventId"));
                        return new Forward(null, "eventEdit.jsp", true);
                    }
                </listener_script>
            </com.tms.collab.calendar.ui.CalendarEventTable>
        </portlet>
        <portlet name="editCalendarEventPortlet" text="<fmt:message key='calendar.label.editEvent'/>" width="100%" permanent="true">
            <com.tms.collab.calendar.ui.EditCalendarEventForm name="editCalendarEventForm" width="100%">
                <forward name="success" url="eventList.jsp" />
            </com.tms.collab.calendar.ui.EditCalendarEventForm>
        </portlet>
    </page>
</x:config>


<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">

        <jsp:include page="/cmsadmin/includes/sideInteractiveEvents.jsp" flush="true"/>

      </td>
      <td style="vertical-align: top;">
        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;"><br>

                  <x:display name="eventList.calendarEventPortlet" />

                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
              </td>
            </tr>
          </tbody>
        </table>
      </td>
    </tr>
  </tbody>
</table>













<jsp:include page="/cmsadmin/includes/footerInteractive.jsp" flush="true" />

<%@ include file="/cmsadmin/includes/footerAdmin.jsp" %>


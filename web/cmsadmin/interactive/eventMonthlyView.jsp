<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.collab.calendar.ManageEvents" module="com.tms.collab.calendar.model.CalendarModule" url="noPermission.jsp" />

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerInteractive.jsp" flush="true" />


<x:config>
    <page name="eventMonthlyView">
        <portlet name="monthlyCalendarPortlet" text="<fmt:message key='calendar.label.monthlyView'/>" width="100%" permanent="true">
            <com.tms.collab.calendar.ui.MonthlyCalendarView name="monthlyCalendarView" width="100%">
                <listener_script>
                    if (event.getRequest().getParameter("eventId") != null) {
                        editForm = wm.getWidget("eventList.editCalendarEventPortlet.editCalendarEventForm");
                        editForm.setEventId(event.getRequest().getParameter("eventId"));
                        return new Forward(null, "eventEdit.jsp", true);
                    }
                    else if ("date".equals(event.getType())) {
                        startDate = wm.getWidget("eventList.calendarEventPortlet.calendarEventTable.filterForm.startDate");
                        endDate = wm.getWidget("eventList.calendarEventPortlet.calendarEventTable.filterForm.endDate");
                        start = event.getWidget().getDate();
                        end = event.getWidget().getDate();
                        startDate.setDate(start);
                        endDate.setDate(end);
                        return new Forward(null, "eventList.jsp", true);
                    }
                </listener_script>
            </com.tms.collab.calendar.ui.MonthlyCalendarView>
        </portlet>
    </page>
</x:config>

<c-rt:set var="userIds" value="<%= new String[] { SecurityService.ANONYMOUS_USER_ID } %>"/>
<c:set target="${widgets['eventMonthlyView.monthlyCalendarPortlet.monthlyCalendarView']}" property="userIds" value="${userIds}"/>

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

                  <x:display name="eventMonthlyView.monthlyCalendarPortlet" body="custom">
                    <div align="center">
                        <x:display name="eventMonthlyView.monthlyCalendarPortlet.monthlyCalendarView" />
                    </div>
                  </x:display>

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


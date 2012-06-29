<%@include file="/common/header.jsp"%>


<x:config>
    <page name="avpage">
          <com.tms.collab.calendar.ui.AppointmentView name="appointmentview"/>

    </page>
</x:config>

<x:set name="avpage.appointmentview" property="instanceId" value="${param.instanceId}" ></x:set>
<x:set name="avpage.appointmentview" property="eventId" value="${param.id}" ></x:set>

<html>
    <body>
        <x:display name="avpage.appointmentview" ></x:display>
    </body>
</html>
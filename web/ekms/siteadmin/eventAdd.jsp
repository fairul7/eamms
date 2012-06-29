<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.collab.calendar.ManageEvents" module="com.tms.collab.calendar.model.CalendarModule" url="noPermission.jsp" />

<x:config>
    <page name="eventAdd">
        <com.tms.collab.calendar.ui.AddCalendarEventForm name="addCalendarEventForm" width="100%">
        	<forward name="success" url="eventList.jsp" />
        </com.tms.collab.calendar.ui.AddCalendarEventForm>
    </page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.events"/> > <fmt:message key="general.label.addNewEvent"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

      <x:display name="eventAdd.addCalendarEventForm" />

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
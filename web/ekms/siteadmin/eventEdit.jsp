<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.collab.calendar.ManageEvents" module="com.tms.collab.calendar.model.CalendarModule" url="noPermission.jsp" />

<x:config>
    <page name="eventEdit">
        <com.tms.collab.calendar.ui.EditCalendarEventForm name="editCalendarEventForm" width="100%">
        	<forward name="success" url="eventList.jsp" />
        </com.tms.collab.calendar.ui.EditCalendarEventForm>
    </page>
</x:config>

<c:if test="${!empty param.eventId}">
    <x:set name="eventEdit.editCalendarEventForm" property="eventId" value="${param.eventId}"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.events"/> > <fmt:message key='calendar.label.editEvent'/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

      <x:display name="eventEdit.editCalendarEventForm" />

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
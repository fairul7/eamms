<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.collab.calendar.ManageEvents" module="com.tms.collab.calendar.model.CalendarModule" url="noPermission.jsp" />

<x:config>
    <page name="eventList">
        <portlet name="calendarEventPortlet" text="<fmt:message key='calendar.label.eventList'/>" width="100%" permanent="true">
            <com.tms.collab.calendar.ui.CalendarEventTable name="calendarEventTable" width="100%"/>
        </portlet>
    </page>
</x:config>

<c:if test="${forward.name == 'add'}">
    <c:redirect url="eventAdd.jsp"/>
</c:if>
<c:if test="${!empty param.eventId}">
    <c:redirect url="eventEdit.jsp?eventId=${param.eventId}"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.events"/> > <fmt:message key="general.label.eventsListing"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

      <x:display name="eventList.calendarEventPortlet.calendarEventTable" />

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
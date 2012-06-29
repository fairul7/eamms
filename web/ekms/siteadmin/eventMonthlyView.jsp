<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.collab.calendar.ManageEvents" module="com.tms.collab.calendar.model.CalendarModule" url="noPermission.jsp" />

<x:config>
    <page name="eventMonthlyView">
        <portlet name="monthlyCalendarPortlet" text="<fmt:message key='calendar.label.monthlyView'/>" width="100%" permanent="true">
            <com.tms.collab.calendar.ui.MonthlyCalendarView name="monthlyCalendarView" width="100%"/>
        </portlet>
    </page>
</x:config>

<c:if test="${!empty param.eventId}">
    <c:redirect url="eventEdit.jsp?eventId=${param.eventId}"/>
</c:if>

<c-rt:set var="userIds" value="<%= new String[] { SecurityService.ANONYMOUS_USER_ID } %>"/>
<c:set target="${widgets['eventMonthlyView.monthlyCalendarPortlet.monthlyCalendarView']}" property="userIds" value="${userIds}"/>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.events"/> > <fmt:message key="calendar.label.monthlyView"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

      <x:display name="eventMonthlyView.monthlyCalendarPortlet" body="custom">
        <div align="center">
            <x:display name="eventMonthlyView.monthlyCalendarPortlet.monthlyCalendarView" />
        </div>
      </x:display>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
<%@ page import="kacang.ui.Event,
                 com.tms.collab.calendar.ui.CalendarView"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<x:config >
<page name="calendarportletpage">
    <com.tms.collab.calendar.ui.CalendarPortlet name="calendarportlet"/>
</page>
</x:config>


<c:if test="${forward.name == 'dateselected'}" >
    <c:url value="calendar/calendar.jsp" var="eUrl"   >
     <c:param name="cn" value="calendarPage.calendarView" />
     <c:param name="et" value="view"  ></c:param>
     <c:param name="view" value="daily" ></c:param>
     <c:param name="calendarPage.calendarView*day" value="${dayOfMonth}" ></c:param>

    </c:url>
    <x:set name="calendarPage.calendarView" property="date" value="${selectedDate}" ></x:set>
    <script>document.location="<c:out value="${eUrl}"/>";</script>


</c:if>



<html>
    <body>
       <x:display name="calendarportletpage.calendarportlet" />

    </body>
</html>
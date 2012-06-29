<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="calendar" value="${widget}"/>
<c:set var="eventList" value="${calendar.eventList}"/>

<p>
<table width="90%">
<tr>
    <td colspan="3">

        <c:choose>
            <c:when test="${empty param.start || empty param.end}">
                <b><fmt:message key='calendar.label.upcomingEvents'/></b>
            </c:when>
            <c:otherwise>
                <c:catch>
                    <fmt:parseDate var="startDate" value="${param.start}" pattern="yyyyMMdd" />
                    <fmt:parseDate var="endDate" value="${param.end}" pattern="yyyyMMdd" />
                </c:catch>
                <b><fmt:message key='calendar.label.eventsBetween'/>:  <fmt:formatDate value="${startDate}" pattern="${globalDateLong}" /> - <fmt:formatDate value="${endDate}" pattern="${globalDateLong}" /></b>
            </c:otherwise>
        </c:choose>
        <hr size="1">

    </td>
</tr>
<tr>
    <td width="50%"><b><fmt:message key='calendar.label.title'/></b></td>
    <td width="25%"><b><fmt:message key='calendar.label.startDate'/></b></td>
    <td width="25%"><b><fmt:message key='calendar.label.endDate'/></b></td>
</tr>
<c:forEach var="event" items="${eventList}">
<tr>
    <td>
        <a href="event.jsp?id=<c:out value="${event.eventId}"/>"><c:out value="${event.title}"/></a>
    </td>
    <td><fmt:formatDate value="${event.startDate}" pattern="${globalDateLong}" /></td>
    <td><fmt:formatDate value="${event.endDate}" pattern="${globalDateLong}" /></td>
</tr>
</c:forEach>
<tr>
    <td colspan="3">

        <hr size="1" width="100%">

        <!-- Date Selection Form -->
        <x:config reloadable="${empty param.start}">
            <page name="eventDateFormPage">
                <form name="eventDateForm">
                    <datefield name="startDate" />
                    <datefield name="endDate" />
                    <button name="submitButton" text="<fmt:message key='calendar.label.list'/>" />
                    <listener_script>
                        form = event.getWidget();
                        if (!form.isInvalid()) {
                            sdf = new java.text.SimpleDateFormat("yyyyMMdd");
                            startDate = sdf.format(form.getChild("startDate").getDate());
                            endDate = sdf.format(form.getChild("endDate").getDate());
                            return new Forward(null, "eventList.jsp?start=" + startDate + "&amp;end=" + endDate, true);
                        }
                    </listener_script>
                </form>
            </page>
        </x:config>

        <x:display name="eventDateFormPage.eventDateForm" body="custom">
            <table>
            <tr>
                <td><fmt:message key='calendar.label.from'/></td>
                <td><x:display name="eventDateFormPage.eventDateForm.startDate"/></td>
            </tr>
            <tr>
                <td><fmt:message key='calendar.label.to'/></td>
                <td><x:display name="eventDateFormPage.eventDateForm.endDate"/></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td><x:display name="eventDateFormPage.eventDateForm.submitButton"/></td>
            </tr>
            </table>
        </x:display>

    </td>
</tr>
</table>




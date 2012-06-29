<%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 26, 2005
  Time: 1:45:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>
<c:set var="task" value="${widget.task}"/>
<c:set var="ts" value="${widget.ts}"/>

<jsp:include page="../form_header.jsp" flush="true"/>

<table width="100%" cellpadding="4" cellspacing="1" class="forumBackground" align=center>
    
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.task"/></td>
        <td class="calendarRow">
        <a href="#" onclick="window.open('../calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=<c:out value="${task.id}"/>')">
        <c:out value="${task.title}" />
        </a>
        </td>
    </tr>

    <c:if test="${w.projectName!=null}">
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.project"/></td>
        <td class="calendarRow"><c:out value="${w.projectName}" /></td>
    </tr>
    </c:if>

    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.date"/></td>
        <td class="calendarRow"><fmt:formatDate value="${ts.createdDateTime}" pattern="${globalDateLong}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.duration"/></td>
        <td class="calendarRow"><c:out value="${ts.duration}" /> <fmt:message key="timesheet.label.hour"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.description"/></td>
        <td class="calendarRow"><c:out value="${ts.description}" /></td>
    </tr>
    <tr>
        <td colspan="2" class="calendarRow">&nbsp;</td>
    </tr>
    <tr>
        <td class="calendarHeader" colspan="2"><fmt:message key="timesheet.label.adjustment"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.duration"/></td>
        <td class="calendarRow"><x:display name="${w.sbDuration.absoluteName}"/> <fmt:message key="timesheet.label.hour"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.description"/></td>
        <td class="calendarRow"><x:display name="${w.tbDescription.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top">&nbsp;</td>
        <td class="calendarRow"><x:display name="${w.panel.absoluteName}"/></td>
    </tr>

</table>

<jsp:include page="../form_footer.jsp" flush="true"/>
<%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 22, 2005
  Time: 4:40:29 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>
<c:set var="ts" value="${widget.ts}"/>
<c:set var="task" value="${widget.task}"/>


<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%" cellpadding="4" cellspacing="1">
<tr>
    <td class="calendarHeader"><fmt:message key="timesheet.label.details"/> </td>
</tr>
<tr>
    <td>
<table width="80%" cellpadding="2" cellspacing="0" class="forumBackground" align=center>

    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.task"/></td>
        <td class="calendarRow"><c:out value="${task.title}" /></td>
    </tr>
    <c:if test="${! empty w.projectName}">
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.project"/></td>
        <td class="calendarRow"><c:out value="${w.projectName}" /></td>
    </tr>
    </c:if>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.date"/></td>
        <td class="calendarRow"><c:out value="${ts.tsDate}" /></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.duration"/></td>
        <td class="calendarRow">
        <c:if test="${ts.adjustment=='N'}">
            <c:out value="${ts.duration}" />
        </c:if>
        <c:if test="${ts.adjustment=='Y'}">
            <c:out value="${ts.adjustedDuration}"/>
        </c:if>
        </td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.description"/></td>
        <td class="calendarRow"><c:out value="${ts.description}" /></td>
    </tr>

    <c:if test="${ts.adjustment=='Y'}">
        <tr>
                <td class="calendarRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.adjustmentby"/></td>
                <td class="calendarRow"><c:out value="${ts.adjustmentBy}" /></td>
        </tr>
        <tr>
                <td class="calendarRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.adjustedduration"/></td>
                <td class="calendarRow"><c:out value="${ts.adjustedDuration}" /></td>
        </tr>
        <tr>
                <td class="calendarRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.adjustmentdatetime"/></td>
                <td class="calendarRow"><c:out value="${ts.adjustmentDateTime}" /></td>
        </tr>
    </c:if>

    <tr>
        <td class="calendarRowLabel" valign="top">&nbsp;</td>
        <td class="calendarRow"><x:display name="${w.btClose.absoluteName}" />&nbsp;<x:display name="${w.btEdit.absoluteName}"/>
        <c:if test="${w.assigner}">
        &nbsp;<x:display name="${w.btAdjust.absoluteName}"/>
        </c:if>
        </td>
    </tr>
    <tr>
        <td colspan="2"><input type=hidden name=id value=<c:out value="${w.id}"/>></td>
    </tr>

</table>
    </td>
</tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
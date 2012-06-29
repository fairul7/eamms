
<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>
<c:set var="task" value="${widget.task}"/>

<jsp:include page="../form_header.jsp" flush="true"/>

<table cellpadding="2" cellspacing="0" width="100%" class="contentBgColor">
<tr>
    <td class="calendarHeader"><fmt:message key="timesheet.label.editform"/>
    &gt; <c:out value="${task.category}"/>
    &gt; <c:out value="${task.title}"/>
    </td>
</tr>
<tr>
    <td>

<c:if test="${w.allow==true}">
<table width="100%" cellpadding="4" cellspacing="1" class="classBackground" align=center>

    <tr>
        <td class="classRowLabel" valign="top" width="100" align="right"><fmt:message key="timesheet.label.task"/></td>
        <td class="classRow"><c:out value="${task.title}" /></td>
    </tr>

    <tr>
        <td class="classRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.category"/></td>
        <td class="classRow"><c:out value="${task.category}" /></td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.date"/></td>
        <td class="classRow"><x:display name="${w.tfDate.absoluteName}" /></td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.duration"/></td>
        <td class="classRow"><x:display name="${w.sbDuration.absoluteName}" /> <fmt:message key="timesheet.label.hour"/></td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.description"/></td>
        <td class="classRow"><x:display name="${w.tbDescription.absoluteName}" /></td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top">&nbsp;</td>
        <td class="classRow">
        <x:display name="${w.btSubmit.absoluteName}" />
        &nbsp;
        <input type="button" value="Cancel" class="button" onClick="javascript: history.go(-1)">
        </td>
    </tr>

</table>
</c:if>
<c:if test="${w.allow==false}">
    <table width="100%" cellpadding="4" cellspacing="1" class="forumBackground" align=center>
        <tr>
            <td colspan="2" class="classRowLabel"><font color="#FF0000"><fmt:message key="timesheet.message.notEditable"/></font></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" width="150" align="right"><fmt:message key="timesheet.label.task"/></td>
            <td class="classRow"><c:out value="${task.title}" /></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.category"/></td>
            <td class="classRow"><c:out value="${task.category}" /></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.date"/></td>
            <td class="classRow"><fmt:formatDate value="${w.timesheet.tsDate}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.duration"/></td>
            <td class="classRow"><c:out value="${w.timesheet.duration}"/> <fmt:message key="timesheet.label.hour"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.description"/></td>
            <td class="classRow"><c:out value="${w.timesheet.description}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.adjustmentDate"/></td>
            <td class="classRow"><fmt:formatDate value="${w.timesheet.adjustmentDateTime}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.adjustmentDuration"/></td>
            <td class="classRow"><c:out value="${w.timesheet.adjustedDuration}"/> <fmt:message key="timesheet.label.hour"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.adjustmentBy"/></td>
            <td class="classRow"><c:out value="${w.timesheet.adjustmentBy}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top">&nbsp;</td>
            <td class="classRow">
                <input type="button" value="Cancel" class="button" onClick="javascript: history.go(-1)">
            <%-- <a href="javascript:history.go(-1)">Back</a> --%>
            </td>
        </tr>

    </table>
</c:if>
    </td>
</tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>
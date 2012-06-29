<%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 22, 2005
  Time: 2:58:34 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>
<c:set var="task" value="${widget.task}"/>
<c:set var="project" value="${widget.project}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="2" cellspacing="0" width="100%">
<tr>
    <td class="calendarHeader"><fmt:message key="timesheet.label.addform"/>
    &gt; <c:out value="${project.projectName}"/>
    &gt; <c:out value="${task.title}"/>
    </td>
</tr>
<tr>
    <td>
<table width="100%" cellpadding="4" cellspacing="1" class="classBackground" align=center>

    <tr>
        <td class="classRowLabel" valign="top" width="100" align="right"><fmt:message key="timesheet.label.task"/></td>
        <td class="classRow"><c:out value="${task.title}" /></td>
    </tr>

    <c:if test="${w.projectAttach}">
    <tr>
        <td class="classRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.category"/></td>
        <td class="classRow"><c:out value="${project.projectName}" /></td>
    </tr>
    </c:if>
    <c:if test="${!w.projectAttach}">
    <tr>
        <td class="classRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.project"/></td>
        <td class="classRow"><x:display name="${w.sbProject.absoluteName}"/></td>
    </tr>
    </c:if>

    <tr>
        <td class="classRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.date"/></td>
        <td class="classRow"><x:display name="${w.tfDate.absoluteName}" /></td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right"><fmt:message key="timesheet.label.duration"/>&nbsp;&nbsp;<FONT class="classRowLabel">*</FONT></td>
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
        &nbsp;<x:display name="${w.btnSubmitAdd.absoluteName}" />
        </td>
    </tr>

</table>
    </td>
</tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>
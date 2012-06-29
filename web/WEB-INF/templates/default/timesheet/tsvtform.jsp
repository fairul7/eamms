<%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 28, 2005
  Time: 2:10:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%" cellpadding="4" cellspacing="0" class="contentBgColor">
    <tr>
        <td class="calendarHeader"><fmt:message key="timesheet.label.view"/>
        &gt; <c:out value="${w.project.projectName}"/>
        </td>
    </tr>
    <tr>
        <td>
    <table width="100%" cellpadding="4" cellspacing="1" class="classBackground">
    <tr>
        <td class="classRowLabel" width="100" align="right"><fmt:message key="timesheet.label.selecttask"/> </td>
        <td class="classRow"><x:display name="${w.sbTask.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="classRowLabel">&nbsp;</td>
        <td class="classRow"><x:display name="${w.btSubmit.absoluteName}"/></td>
    </tr>
    </table>
        </td>
    </tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>
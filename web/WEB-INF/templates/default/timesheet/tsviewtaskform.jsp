<%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 26, 2005
  Time: 3:38:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>

<table width="100%" cellpadding="4" cellspacing="0">
    <tr>
        <td class="calendarHeader">
        <c:choose>
        <c:when test="${w.set==true}">
        <fmt:message key="timesheet.label.add"/>
        </c:when>
        <c:otherwise>
        <fmt:message key="timesheet.label.my"/>
        </c:otherwise>
        </c:choose>
        &gt; <c:out value="${w.project.projectName}"/>
        </td>
    </tr>
    <tr>
        <td>
    <table width="100%" cellpadding="4" cellspacing="1" class="forumBackground">
    <tr>
        <td class="calendarRowLabel" width="100" align="right"><fmt:message key="timesheet.label.selecttask"/></td>
        <td class="calendarRow"><x:display name="${w.sbTask.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel">&nbsp;</td>
        <td class="calendarRow"><x:display name="${w.btSubmit.absoluteName}"/></td>
    </tr>
    </table>
        </td>
    </tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>
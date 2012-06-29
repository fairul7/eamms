<%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: May 24, 2005
  Time: 12:03:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>

<table width="100%" cellpadding="4" cellspacing="0" class="contentBgColor">
    <tr>
        <td class="calendarHeader" colspan="2"><fmt:message key="timesheet.label.tsmanager"/></td>
    </tr>
    <tr>
        <td>
    <table width="100%" cellpadding="4" cellspacing="1" class="classBackground">
    <c:choose>
    <c:when test="${w.type=='project'}">
    <tr>
        <td class="classRowLabel" colspan="2"><fmt:message key="timesheet.label.viewbyproject"/></td>
    </tr>
    </c:when>
    <c:otherwise>
    <tr>
        <td class="classRowLabel" colspan="2"><fmt:message key="timesheet.label.viewbyuser"/></td>
    </tr>
    </c:otherwise>
    </c:choose>
    <tr>
        <td class="classRowLabel" width="100" align="right"><fmt:message key="timesheet.label.selectmonth"/> </td>
        <td class="classRow"><x:display name="${w.sbMonth.absoluteName}"/></td>
    </tr>
    <c:choose>
    <c:when test="${w.type=='project'}">
    <tr>
        <td class="classRowLabel" width="100" align="right"><fmt:message key="timesheet.label.selectmonthlyproject"/> </td>
        <td class="classRow"><x:display name="${w.sbProject.absoluteName}"/></td>
    </tr>
    </c:when>
    <c:otherwise>
    <tr>
        <td class="classRowLabel" width="100" align="right"><fmt:message key="timesheet.label.selectmonthlyuser"/> </td>
        <td class="classRow"><x:display name="${w.sbUser.absoluteName}"/></td>
    </tr>
    </c:otherwise>
    </c:choose>
    <tr>
        <td class="classRowLabel">&nbsp;</td>
        <td class="classRow">
        <input
        class="button"
        type="submit"
        name="<%= kacang.stdui.Button.PREFIX_BUTTON %><c:out value="${w.btViewReport.absoluteName}"/>"
        value="<c:out value="${w.btViewReport.text}"/>"
        >
        

        </td>
    </tr>
    </table>
        </td>
    </tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>
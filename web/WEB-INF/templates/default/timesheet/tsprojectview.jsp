<%@ page import="java.util.Calendar"%>
 <%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 27, 2005
  Time: 4:57:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<c:set var="w" value="${widget}"></c:set>

<table width="100%" cellpadding="4" cellspacing="0">
<tr>
    <td class="contentTitleFont" bgcolor="#003366" colspan="2">
    <c:choose>
    <c:when test="${w.print==true}">
    <%
        pageContext.setAttribute("today",Calendar.getInstance().getTime());
    %>
    <fmt:message key="timesheet.label.report"/> - <fmt:formatDate value="${today}" pattern="${globalDateLong}"/>
    </c:when>
    <c:otherwise>
    <fmt:message key="timesheet.label.project"/>
    </c:otherwise>
    </c:choose>
    </td>
</tr>
<tr>
    <td class="contentBgColor" bgcolor="#efefef" width="180" align="right">
    <b><fmt:message key="timesheet.label.project"/>:</b>
    </td>
    <td class="contentBgColor" bgcolor="#efefef" >
    <c:out value="${w.project.projectName}"/>
    </td>
</tr>
<tr>
    <td class="contentBgColor" bgcolor="#efefef" align="right">
    <b><fmt:message key="timesheet.label.estimatedhour"/>:</b>
    </td>
    <td class="contentBgColor" bgcolor="#efefef" >
    <c:out value="${w.estimatedHourSpent}"/>
    </td>
</tr>
<tr>
    <td class="contentBgColor" bgcolor="#efefef" align="right">
    <b><fmt:message key="timesheet.label.totalhourspent"/>:</b>
    </td>
    <td class="contentBgColor" bgcolor="#efefef" >
    <c:out value="${w.totalHourSpent}"/>
    </td>
</tr>
<c:if test="${w.print==false}">
<tr>
    <td class="contentBgColor" bgcolor="#efefef" align="right">
    &nbsp;
    </td>
    <td class="contentBgColor" bgcolor="#efefef" >
    <input type="button" name="button" class="button" value="<fmt:message key="timesheet.label.print"/>" onclick="window.open('../timesheet/TimeSheetPrint.jsp?projectid=<c:out value="${w.project.projectId}"/>');">
    </td>
</tr>
</c:if>
</table>
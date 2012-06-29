<%@ page import="java.util.Collection"%>
 <%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 27, 2005
  Time: 3:07:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<c:set var="w" value="${widget}"/>

<table width="100%" cellpadding="4" cellspacing="0">
<c:if test="${w.header==true}">
<tr>
    <td class="contentTitleFont" bgcolor="#003366">
    <b><font color="#ffcf63" class="contentTitleFont">
        <fmt:message key="timesheet.label.view"/>
        &gt; <c:out value="${w.project.projectName}"/>
        &gt; <c:out value="${w.task.title}"/>
    </font></b>
    </td>
</tr>
<tr>
    <td class="contentBgColor" bgcolor="#efefef">
    <table width="100%" cellpadding="4" cellspacing="0">
    <tr>
        <td width="150" align="right">
        <b><fmt:message key="timesheet.label.projectname"/>:</b>
        </td>
        <td align="left">
        <c:out value="${w.project.projectName}"></c:out>
        </td>
    </tr>
    <tr>
        <td width="150" align="right">
        <b><fmt:message key="timesheet.label.task"/>:</b>
        </td>
        <td>
        <a href="../calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=<c:out value="${w.task.id}"/>">
        <c:out value="${w.task.title}"/>
        </a>
        </td>
    </tr>
    <tr>
        <td align="right">
        <b><fmt:message key="timesheet.label.totalhourspent"/>:</b>
        </td>
        <td>
        <c:out value="${w.totalHourSpentForTask}"/>
        </td>
    </tr>
    <tr>
        <td align="right">
        <b><fmt:message key="timesheet.label.progress"/>:</b>
        </td>
        <td>
            <c:out value="${w.task.overallProgress}"/> %
        </td>
    </tr>
     <tr>
        <td align="right">
        <b><fmt:message key="timesheet.label.startdate"/>:</b>
        </td>
        <td>
            <fmt:formatDate value="${w.task.startDate}" pattern="${globalDateLong}"/>
        </td>
    </tr>
     <tr>
        <td align="right">
        <b><fmt:message key="timesheet.label.duedate"/>:</b>
        </td>
        <td>
            <fmt:formatDate value="${w.task.dueDate}" pattern="${globalDateLong}"/>
        </td>
    </tr>
    </table>
    </td>
</tr>

</c:if>
<tr>
    <td class="contentBgColor" bgcolor="#efefef">
    <%
        int i=0;
        String sCur=""+i;
    %>
    <c-rt:set var="i" value="<%=sCur%>"/>
    <c:if test="${empty w.useridList}">
        <fmt:message key="timesheet.error.notimesheet"/>
    </c:if>
    <c:forEach items="${w.useridList}" var="user">
        <table width="100%" cellpadding="1" cellspacing="0">
        <tr>
            <td class="folderlink">
                <b><fmt:message key="timesheet.label.user"/>:</b>
                <c:out value="${user}"/>
                &nbsp;&nbsp;&nbsp;
                <b><fmt:message key="timesheet.label.progress"/>:</b>
                <c:out value="${w.progress[i]}"/> %
            </td>
            <td align="right" class="folderlink">
                <c:choose>
                <c:when test="${!empty w.task.estimation}">
                <c:if test="${w.task.estimationType =='Manhours'}">
                <c:set var="totalManhoursEstimated" value="${w.task.estimation}" />
                </c:if>
                <c:if test="${w.task.estimationType =='Mandays'}">
                <c:set var="totalManhoursEstimated" value="${w.task.estimation*8}" />
                </c:if>
                    <c:if test="${totalManhoursEstimated < w.totalHourSpent[i]}">
                        <font color="#ff0000">
                    </c:if>
                    <b><fmt:message key="timesheet.label.estimatedhour"/>:</b>
                    <fmt:formatNumber value="${totalManhoursEstimated}" maxFractionDigits="2" pattern="#0.00" /> &nbsp;&nbsp;&nbsp;
                    <b><fmt:message key="timesheet.label.totalhourspent"/>:</b>
                    <fmt:formatNumber value="${w.totalHourSpent[i]}" maxFractionDigits="2" pattern="#0.00" />
                    <c:if test="${totalManhoursEstimated < w.totalHourSpent[i]}">
                        </font>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <b><fmt:message key="timesheet.label.totalhourspent"/>:</b>
                    <fmt:formatNumber value="${w.totalHourSpent[i]}" maxFractionDigits="2" pattern="#0.00" />
                </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
        <td colspan="2" class="tableBackground">
            <table width="100%" cellpadding="1" cellspacing="1">
            <tr>
                <td class="tableHeader" rowspan="2">&nbsp;</td>
                <td class="tableHeader" rowspan="2"><fmt:message key='timesheet.label.date'/></td>
                <td class="tableHeader" rowspan="2"><fmt:message key='timesheet.label.duration'/></td>
                <td class="tableHeader" rowspan="2"><fmt:message key='timesheet.label.description'/></td>
                <td class="tableHeader" colspan="4" align="center"><fmt:message key='timesheet.label.adjustment'/></td>
            </tr>
            <tr>
                <td class="tableHeader"><fmt:message key='timesheet.label.date'/></td>
                <td class="tableHeader"><fmt:message key='timesheet.label.duration'/></td>
                <td class="tableHeader"><fmt:message key='timesheet.label.by'/></td>
                <td class="tableHeader"><fmt:message key='timesheet.label.description'/></td>
            </tr>
            <%
                int j=0;
            %>
            <c:forEach items="${w.tsList[i]}" var="timesheet">
                <%
                    j++;
                %>
                <tr>
                    <td class="tableRow" valign="top"><%=j%></td>
                    <td class="tableRow" valign="top">
                    <c:choose>
                    <c:when test="${w.print==false}">
                    <a href="#" onclick="javascript:window.open('TimeSheetAdjForm.jsp?id=<c:out value="${timesheet.id}"/>','ts','width=400,height=400,toolbar=no,menubar=no,scrollbars=yes')">
                    <fmt:formatDate value="${timesheet.tsDate}" pattern="${globalDateLong}"/>
                    </a>
                    </c:when>
                    <c:otherwise>
                    <fmt:formatDate value="${timesheet.tsDate}" pattern="${globalDateLong}"/>
                    </c:otherwise>
                    </c:choose>
                    </td>
                    <td class="tableRow" valign="top"><c:out value="${timesheet.duration}"/></td>
                    <td class="tableRow" valign="top"><c:out value="${timesheet.description}"/></td>
                    <c:if test="${timesheet.adjustment=='Y'}">
                        <td class="tableRow" valign="top">
                        <fmt:formatDate value="${timesheet.adjustmentDateTime}" pattern="${globalDateLong}"/>
                        </td>
                        <td class="tableRow" valign="top"><c:out value="${timesheet.adjustedDuration}"/></td>
                        <td class="tableRow" valign="top"><c:out value="${timesheet.adjustmentBy}"/></td>
                        <td class="tableRow" valign="top"><c:out value="${timesheet.adjustmentDescription}"/></td>
                    </c:if>
                    <c:if test="${timesheet.adjustment=='N'}">
                        <td class="tableRow" valign="top">-</td>
                        <td class="tableRow" valign="top">-</td>
                        <td class="tableRow" valign="top">-</td>
                        <td class="tableRow" valign="top">-</td>
                    </c:if>
                </tr>
            </c:forEach>
            </table>
        </td>
    </tr>
    </table>
    <br>
    <%
        i++;
        sCur=""+i;
    %>
    <c-rt:set var="i" value="<%=sCur%>"/>
    </c:forEach>
    </td>
</tr>
</table>
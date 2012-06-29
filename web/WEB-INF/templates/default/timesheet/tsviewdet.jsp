<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.timesheet.TimeSheetUtil"%>
 <%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 27, 2005
  Time: 10:53:47 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />

<table width="100%" cellpadding="2" cellspacing="0">
<tr>
    <td class="contentTitleFont" bgcolor="#003366">
        <b><font class="contentTitleFont" color="#FFCF63"><fmt:message key="timesheet.label.my"/>
            &gt; <c:out value="${w.project.projectName}"/>
            &gt; <c:out value="${w.task.title}"/>
        </font></b>
    </td>
</tr>
<tr>
    <td class="contentBgColor" bgcolor="#EFEFEF">
<table width="100%" cellpadding="4" cellspacing="1">
<tr>
    <td colspan="3">
    <table width="100%" cellpadding="4" cellspacing="0">
    <tr>
        <td class="folderlink" align="right">   <b>
        <c:if test="${w.project != null}">
            <fmt:message key="timesheet.label.projectname"/>:</b>
            </td>
            <td>
            <c:out value="${w.project.projectName}"></c:out>
         </c:if>
        <c:if test="${w.project == null}">
            <fmt:message key="timesheet.label.projectname"/></b>
            </td>
            <td>
            --
        </c:if>
        </td>
    </tr>
    <tr>
        <td class="folderlink" width="100" align="right"><b>
        <fmt:message key="timesheet.label.task"/>:</b>
        </td>
        <td>
        <a href="../calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=<c:out value="${w.task.id}"/>"><c:out value="${w.task.title}"/></a>
        </td>
    </tr>
    <tr>
        <td class="folderlink" align="right">       <b>
        <fmt:message key="timesheet.label.startdate"/>:</b>
        </td>
        <td>
        <fmt:formatDate value="${w.task.startDate}" pattern="${globalDateLong}"/>
        </td>
    </tr>
    <tr>
        <td class="folderlink" align="right">       <b>
        <fmt:message key="timesheet.label.duedate"/>:</b>
        </td>
        <td>
        <fmt:formatDate value="${w.task.dueDate}" pattern="${globalDateLong}"/>
        </td>
    </tr>
    <tr>
        <td class="folderlink" align="right">       <b>
        <fmt:message key="timesheet.label.progress"/>:</b>
        </td>
        <td>
        <c:out value="${w.task.progress}"/> %
        </td>
    </tr>
    </table>
    </td>
</tr>
<tr>
    <td width="2%">&nbsp;</td>
    <td>
    <table width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td class="folderlink">      <b>
            <fmt:message key="timesheet.label.user"/>:</b>
            <%
                WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
                pageContext.setAttribute("username",wm.getUser().getName());
            %>
            <c:out value="${username}"/>
        </td>
        <td align="right" class="folderlink">   <b>
            <c:choose>
            <c:when test="${!empty w.totalHour}">
            <c:if test="${w.totalHourSpent>w.totalHour}">
                <font color="#ff0000">
            </c:if>
            <fmt:message key="timesheet.label.estimatedhour"/>:</b>
            <fmt:formatNumber value="${w.totalHour}" maxFractionDigits="2" pattern="#0.00" />  &nbsp;&nbsp;&nbsp;
            <b><fmt:message key="timesheet.label.totalhourspent"/>:</b>
            <fmt:formatNumber value="${w.totalHourSpent}" maxFractionDigits="2" pattern="#0.00" />
            <c:if test="${w.totalHourSpent>w.totalHour}">
                </font>
            </c:if>
            </c:when>
            <c:otherwise>
            <b><fmt:message key="timesheet.label.totalhourspent"/>:</b>
            <fmt:formatNumber value="${w.totalHourSpent}" maxFractionDigits="2" pattern="#0.00" />
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
                int i=0;
            %>
            <c:forEach items="${w.ts}" var="timesheet">
                <%
                    i++;
                %>
                <tr class="tableRow">
                    <td valign="top"><%=i%></td>
                    <td valign="top"><a href="timesheetEDForm.jsp?id=<c:out value="${timesheet.id}"/>"><fmt:formatDate value="${timesheet.tsDate}" pattern="${globalDateLong}"/></a></td>
                    <td valign="top"><c:out value="${timesheet.duration}"/></td>
                    <td valign="top"><c:out value="${timesheet.description}"/></td>
                    <c:if test="${timesheet.adjustment=='Y'}">
                        <td valign="top"><fmt:formatDate value="${timesheet.adjustmentDateTime}" pattern="${globalDateLong}"/></td>
                        <td valign="top"><c:out value="${timesheet.adjustedDuration}"/></td>
                        <td valign="top"><c:out value="${timesheet.adjustmentBy}"/></td>
                        <td valign="top"><c:out value="${timesheet.adjustmentDescription}"/></td>
                    </c:if>
                    <c:if test="${timesheet.adjustment=='N'}">
                        <td valign="top">-</td>
                        <td valign="top">-</td>
                        <td valign="top">-</td>
                        <td valign="top">-</td>
                    </c:if>
                </tr>
            </c:forEach>
            </table>
        </td>
    </tr>
    </table>
    </td>
    <td width="2%">&nbsp;</td>
</tr>
</table>
    </td>
</tr>
<tr>
    <td class="contentBgColor" bgcolor="#EFEFEF"> <br>&nbsp;&nbsp;
    <font color="#0000ff">
    *
    <fmt:message key="timesheet.message.estimatedhourdesc1"/>
    <%=TimeSheetUtil.WORKING_HOUR_PER_DAY%>
    <fmt:message key="timesheet.message.estimatedhourdesc2"/>
    </font>
    </td>
</tr>
<tr>
  <td class="contentBgColor" bgcolor="#EFEFEF">
  &nbsp;
  </td>
</tr>
</table>
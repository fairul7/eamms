<%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 27, 2005
  Time: 6:17:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<c:set var="w" value="${widget}"/>

<table width="100%" cellpadding="2" cellspacing="0">
<tr>
    <td colspan="3" class="calendarRowLabel">
    <fmt:message key="timesheet.label.user"/>:&nbsp;<c:out value="${w.userName}"/>
    </td>
</tr>
<tr>
    <td width="5%">&nbsp;</td>
    <td>
    <c:set var="i" value="0"/>
    <c:forEach items="${w.taskList}" var="task">
        <table width="100%" cellpadding="0" cellspacing="0">
        <tr>
            <td class="calendarRowLabel">
                <fmt:message key="timesheet.label.task"/>:
                &nbsp;<c:out value="${task.title}"/>
            </td>
            <td class="calendarRowLabel" align="right">
                <fmt:message key="timesheet.label.totalhourspent"></fmt:message>:
                &nbsp;<c:out value="${w.totalHourSpent[i]}"/>
            </td>
        </tr>
        <tr>
            <td class="calendarHeader" colspan="2">
                <table width="100%" cellpadding="2" cellspacing="1">
                <tr>
                    <td class="calendarRowLabel" rowspan="2">&nbsp;</td>
                    <td class="calendarRowLabel" rowspan="2">Date</td>
                    <td class="calendarRowLabel" rowspan="2">Duration</td>
                    <td class="calendarRowLabel" rowspan="2">Description</td>
                    <td class="calendarRowLabel" colspan="4" align="center">Adjustment</td>
                </tr>
                <tr>
                    <td class="calendarRowLabel">Date</td>
                    <td class="calendarRowLabel">Duration</td>
                    <td class="calendarRowLabel">By</td>
                    <td class="calendarRowLabel">Description</td>
                </tr>
                <%
                    int i=0;
                %>
                <c:forEach items="${w.tsList[i]}" var="timesheet">
                    <%
                        i++;
                    %>
                    <tr>
                        <td class="calendarRow"><%=i%></td>
                        <td class="calendarRow">
                        <a href="#" onclick="javascript:window.open('TimeSheetAdjForm.jsp?id=<c:out value="${timesheet.id}"/>','ts','width=400,height=400,toolbar=no,menubar=no')">
                        <c:out value="${timesheet.tsDate}"/></a>
                        </td>
                        <td class="calendarRow"><c:out value="${timesheet.duration}"/></td>
                        <td class="calendarRow"><c:out value="${timesheet.description}"/></td>
                        <c:if test="${timesheet.adjustment=='Y'}">
                            <td class="calendarRow"><c:out value="${timesheet.adjustmentDateTime}"/></td>
                            <td class="calendarRow"><c:out value="${timesheet.adjustedDuration}"/></td>
                            <td class="calendarRow"><c:out value="${timesheet.adjustmentBy}"/></td>
                            <td class="calendarRow"><c:out value="${timesheet.adjustmentDescription}"/></td>
                        </c:if>
                        <c:if test="${timesheet.adjustment=='N'}">
                            <td class="calendarRow">-</td>
                            <td class="calendarRow">-</td>
                            <td class="calendarRow">-</td>
                            <td class="calendarRow">-</td>
                        </c:if>
                    </tr>
                </c:forEach>
                </table>
            </td>
            <td width="5%">&nbsp;</td>
        </tr>
        </table>
        <br>
        <c:set var="i" value="i+1"/>
    </c:forEach>
    </td>
</tr>
</table>

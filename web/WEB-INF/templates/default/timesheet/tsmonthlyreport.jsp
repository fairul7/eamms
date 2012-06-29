<%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: May 24, 2005
  Time: 4:03:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<c:set var="w" value="${widget}"></c:set>
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="4" cellspacing="0" width="100%">
<tr>
    <td class="contentTitleFont" colspan="2">
    <fmt:message key="timesheet.label.timesheetmonthlyreport"/> &gt;
    <c:if test="${w.reportType=='project'}">
    <fmt:message key="timesheet.label.projectmonthlyreport"/>
    </c:if>
    <c:if test="${w.reportType=='user'}">
    <fmt:message key="timesheet.label.usermonthlyreport"/>    
    </c:if>
    </td>
</tr>
<tr>
    <td class="contentBgColor" colspan="2">&nbsp;</td>
</tr>
<tr>
    <td class="contentBgColor" bgcolor="#efefef" width="20%" align="right">
    <b><fmt:message key="timesheet.label.month"/>: </b>
    </td>
    <td class="contentBgColor" bgcolor="#efefef" align="left" width="80%">
    <c:out value="${w.month}"/>, <c:out value="${w.year}"/>
    </td>
</tr>
<tr>
    <c:choose>
    <c:when test="${w.reportType=='project'}">
        <td class="contentBgColor" bgcolor="#efefef" align="right">
        <b><fmt:message key="timesheet.label.project"/>: </b>
        </td>
        <td class="contentBgColor" bgcolor="#efefef" align="left" >
        <c:out value="${w.project.projectName}"/>
        </td>
    </c:when>
    <c:otherwise>
        <td class="contentBgColor" bgcolor="#efefef" align="right">
        <b><fmt:message key="timesheet.label.user"/>:</b>
        </td>
        <td class="contentBgColor" bgcolor="#efefef" align="left" >
        <c:out value="${w.user.name}"/>
        </td>
    </c:otherwise>
    </c:choose>
</tr>
<tr>
    <td class="contentBgColor" bgcolor="#efefef">&nbsp;</td>
    <td class="contentBgColor">
    <c:choose>
    <c:when test="${w.print}">
    &nbsp;
    </c:when>
    <c:otherwise>
        <c:if test="${!empty w.userId}">
        <input type="button" name="button" class="button" value="<fmt:message key="timesheet.label.print"/>" onclick="javascript:window.open('timeSheetMRPrint.jsp?userId=<c:out value="${w.userId}"/>&month=<c:out value="${w.monthParam}"/>');">
        </c:if>
        <c:if test="${!empty w.projectId}">
        <input type="button" name="button" class="button" value="<fmt:message key="timesheet.label.print"/>" onclick="javascript:window.open('timeSheetMRPrint.jsp?projectId=<c:out value="${w.projectId}"/>&month=<c:out value="${w.monthParam}"/>');">
        </c:if>
        <%-- <x:display name="${w.btPrint.absoluteName}"/> --%>
    </c:otherwise>
    </c:choose>
    </td>
</tr>
<tr>
    <td colspan="2" class="contentBgColor">
    <table cellpadding="1" cellspacing="0" width="100%">
    <tr>
        <td  class="tableBackground">
    <!-- start report table -->
    <table width="100%" cellpadding="4" cellspacing="1">
    <!-- start table header -->
    <tr>
        <td class="tableHeader" rowspan="2">
        <!-- check for which report to display -->
        <c:choose>
            <c:when test="${w.reportType=='project'}">
            <fmt:message key="timesheet.label.name"/>
            </c:when>
            <c:otherwise>
            <fmt:message key="timesheet.label.task"/>
            </c:otherwise>
        </c:choose>
        </td>
        <td class="tableHeader" colspan="<c:out value="${w.endDay}"/>" align="center">
            <fmt:message key="timesheet.label.day"></fmt:message>
        </td>
        <td class="tableHeader" rowspan="2">
            <fmt:message key="timesheet.label.total"></fmt:message>
        </td>
    </tr>
    <tr>
        <%
            int i=1;
            String sI = ""+i;
        %>
        <c:forEach items="${w.dayList}" var="day">
        <td class="tableHeader">
            <c:if test="${day==1}">
            <%=sI%>
            </c:if>
            <c:if test="${day==0}">
            <font color="#ff0000"><%=sI%></font>
            </c:if>
        </td>
        <%
            i++;
            sI=""+i;
        %>
        </c:forEach>
    </tr>
    <!-- end table header -->
    <!-- start table body -->
    <%
        int j=0;
        String strJ=""+j;
       
        //double[] daySpent=new double[endDay];
    %>
    <c:choose>
    <c:when test="${w.reportType=='project'}">
        <c:forEach items="${w.userDailySpent}" var="dailySpent">
        <% boolean bRow=true; %>
            <tr>
            <c-rt:set var="i" value="<%=strJ%>"/>
            <td class="tableRow"><c:out value="${w.userList[i].name}"/></td>
            <c:forEach items="${dailySpent}" var="spent">
                <%
                    if (bRow) {
                %>
                <td class="tableRowNew" align="right"><c:out value="${spent}"/></td>
                <%
                    bRow=false;}else {
                %>
                <td class="tableRow" align="right"><c:out value="${spent}"/></td>
                <%
                    bRow=true;}
                %>
            </c:forEach>
            <td class="tableRow" align="right"><c:out value="${w.userTotalList[i]}"/></td>
            <% j++; strJ=""+j; %>
            <%-- <c-rt:set var="i" value="<%=j%>"/> --%>
            </tr>
        </c:forEach>
        <c:if test="${empty w.userDailySpent}">
            <tr>
                <td class="tableRow">&nbsp;</td>
                <td class="tableRow" colspan="<c:out value="${w.endDay}"/>"><fmt:message key="timesheet.message.nomonthlyreportrecord"/></td>
                <td class="tableRow">&nbsp;</td>
            </tr>
        </c:if>
    </c:when>
    <c:otherwise>
        <c:forEach items="${w.taskDailySpent}" var="dailySpent">
        <% boolean bRow=true; %>
            <tr>
            <c-rt:set var="i" value="<%=strJ%>"/>
            <td class="tableRow">
            <c:if test="${w.print!=true}">
                <a href="../calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=<c:out value="${w.taskList[i].id}"/>">
            </c:if>
                [<c:out value="${w.taskList[i].category}"/>] <c:out value="${w.taskList[i].title}"/>
            <c:if test="${w.print!=true}">
                </a>
            </c:if>
            </td>
            <c:forEach items="${dailySpent}" var="spent">
                <%
                    if (bRow) {
                %>
            <td class="tableRowNew" align="right"><c:out value="${spent}"/></td>
                <%
                    bRow=false;} else {
                %>
                <td class="tableRow" align="right"><c:out value="${spent}"/></td>
                <%
                    bRow=true;}
                %>
            </c:forEach>
            <td class="tableRow" align="right"><c:out value="${w.taskTotalList[i]}"/></td>
            <% j++; strJ=""+j; %>
            </tr>
        </c:forEach>
        <c:if test="${empty w.taskDailySpent}">
            <tr>
                <td class="tableRow">&nbsp;</td>
                <td class="tableRow" colspan="<c:out value="${w.endDay}"/>"><fmt:message key="timesheet.message.nomonthlyreportrecord"/></td>
                <td class="tableRow">&nbsp;</td>
            </tr>
        </c:if>
    </c:otherwise>
    </c:choose>
    <!-- start table footer -->
    <c:if test="${!empty w.dailyTotal}">
    <tr>
        <td class="tableHeader"><fmt:message key="timesheet.label.total"/></td>
        <c:forEach items="${w.dailyTotal}" var="total">
        <td class="tableHeader" align="right"><c:out value="${total}"/></td>
        </c:forEach>
        <td class="tableHeader" align="right"><c:out value="${w.grandTotal}"/></td>
    </tr>
    </c:if>
    <!-- end table footer -->
    <!-- end table body -->
    </table>
    <!-- end report table -->
        </td>
    </tr>
    </table>
    </td>
</tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
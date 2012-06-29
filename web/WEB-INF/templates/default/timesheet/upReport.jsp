<%@ include file="/common/header.jsp" %>

<c:set var="w" value="${widget}"/>

<c:if test="${w.actionType==null || w.actionType==''}">
<jsp:include page="../form_header.jsp" flush="true"/>

<table width="100%" cellpadding="4" cellspacing="0" class="contentBgColor">
    <%--
    <tr>
        <td class="calendarHeader" colspan="2"><fmt:message key="timesheet.label.tsmanager"/></td>
    </tr>
    --%>
    <tr>
        <td class="contentTitleFont" colspan="2"><fmt:message key="timesheet.label.viewmonthlybyproject"/></td>
    </tr>
    <tr>
        <td>
    <table width="100%" cellpadding="4" cellspacing="1" class="classBackground">
    <tr>
        <td class="classRowLabel" width="100" align="right" ><fmt:message key="timesheet.label.selectmonth"/> </td>
        <td class="classRow"><b><x:display name="${w.monthSelect.absoluteName}"/></b></td>
    </tr>
    <tr>
        <td class="classRowLabel" width="100" align="right"valign="top"><fmt:message key="timesheet.label.selectuser"/> </td>
        <td class="classRow"><x:display name="${w.comboBox.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="classRowLabel">&nbsp;</td>
        <td class="classRow">
        <input
        class="button"
        type="submit"
        name="<%= kacang.stdui.Button.PREFIX_BUTTON %><c:out value="${w.btnSubmit.absoluteName}"/>"
        value="<c:out value="${w.btnSubmit.text}"/>"
        >


        </td>
    </tr>
    </table>
        </td>
    </tr>
</table>


<jsp:include page="../form_footer.jsp" flush="true"/>
</c:if>

<c:if test="${w.actionType!=''}">
<table cellpadding="4" cellspacing="0" width="100%">
<tr>
    <td class="contentTitleFont" colspan="2">
    <fmt:message key="timesheet.label.timesheetmonthlyreport"/>
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
    <b><c:out value="${w.month}"/>, <c:out value="${w.year}"/></b>
    </td>
</tr>
<% int iTotal = 0; %>
<c:forEach items="${w.reportDetails}" var="details">
<% String sTotal = ""+iTotal;%>
<c-rt:set var="iTotal" value="<%=sTotal%>"/>
<tr>
    <td class="contentBgColor"  bgcolor="#efefef" colspan="2">
        <b>User: <c:out value="${details.user.propertyMap.firstName}"/> <c:out value="${details.user.propertyMap.lastName}"/></b>
        <br>
        <table width="100%" cellpadding="0" cellspacing="2">
            <tr>
                <%-- <td rowspan="2" class="tableHeader">User</td> --%>
                <td rowspan="2" class="tableHeader"><b>Project</b></td>
                <td colspan="<c:out value="${w.endDay}"/>" class="tableHeader"><b>Days</b></td>
                <td rowspan="2" class="tableHeader"><b>Total</b></td>
            </tr>
            <tr>
                <%
                int i=1;
                String sI = ""+i;
                String sTotalDay="";
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
                    sTotalDay=""+i;
                    i++;
                    sI=""+i;
                %>
                </c:forEach>
            </tr>
            <% i=0; boolean hasProject=false;%>
            <c:forEach var="dailyTotal" items="${details.totalDetails}">
            <% sI=""+i;%>
            <c-rt:set var="i" value="<%=sI%>"/>
            <tr>
                <%
                    boolean bRow=true;
                    hasProject=true;
                %>
                <td class="tableRow"><c:out value="${details.projects[i].projectName}"/></td>
                <c:forEach var="spent" items="${dailyTotal}">
                    <% if (bRow) { %>
                        <td class="tableRowNew" align="right"><c:out value="${spent}"/></td>
                    <% bRow=false;} else { %>
                        <td class="tableRow" align="right"><c:out value="${spent}"/></td>
                    <% bRow=true;} %>
                </c:forEach>
                <td class="tableRow" align="right"><b><c:out value="${details.projectTotal[i]}"/></b></td>
            </tr>
            <% i++; %>
            </c:forEach>
            <%
                boolean bRow=true;
                if (hasProject)  {
            %>

            <tr>
                <td class="tableHeader">&nbsp;</td>
                <c:forEach var="dailyTotal" items="${details.dailyTotal}">
                    <% if (bRow) { %>
                    <td class="tableHeader" align="right"><b><c:out value="${dailyTotal}"/></b></td>
                    <% bRow=false;} else { %>
                    <td class="tableHeader" align="right"><b><c:out value="${dailyTotal}"/></b></td>
                    <% bRow=true;} %>
                </c:forEach>
                <td class="tableHeader" align="right"><b><c:out value="${w.grandTotal[iTotal]}"/></b></td>
            </tr>
            <%
                }else {
            %>
            <tr>
                <td class="tableRow">No Project Found</td>
                <td class="tableRow" colspan="<%=sTotalDay%>">&nbsp;</td>
                <td class="tableRow">&nbsp;</td>
            </tr>
            <%
                }
            %>
        </table>
    </td>
</tr>
<% iTotal++; %>
</c:forEach>
</table>
<input type="button" name="button" class="button" value="<fmt:message key="timesheet.label.print"/>" onclick="javascript:window.open('timeSheetUPPrint.jsp');">
</c:if>
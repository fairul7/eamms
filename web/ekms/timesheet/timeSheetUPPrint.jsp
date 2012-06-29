<%@ page import="kacang.ui.WidgetManager"%>
<%@ page import="com.tms.collab.timesheet.ui.TimeSheetUserProjectMR"%>
<%@ include file="/common/header.jsp" %>


<%
    WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
    TimeSheetUserProjectMR upReport = (TimeSheetUserProjectMR)wm.getWidget("UPMRForm.upmr");
    request.setAttribute("w",upReport);
%>
<html>
  <head><title>Print Time Sheet</title></head>
  <style>
  .tableBackground {background-color: #999999; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:bold}
  .contentTitleFont {background-color: #CCCCCC; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt; font-weight:bold}
  .contentBgColor {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  .tableHeader {background-color: #CCCCCC; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  .tableRow {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  .tableRowNew {background-color:  #DCDCDC; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  .folderlink {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  </style>
  <body>
    <table width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td>                                            

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
                <td colspan="2" class="tableRow">
                    <b>User: <c:out value="${details.user.propertyMap.firstName}"/> <c:out value="${details.user.propertyMap.lastName}"/></b>
                    <br>
                    <table width="100%" cellpadding="0" cellspacing="1"  class="tableBackground">
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
                            <td class="tableHeader" align="center">
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
            <%
                i=0;
                boolean bHasProject=false;
            %>
                        <c:forEach var="dailyTotal" items="${details.totalDetails}">
            <%
                sI=""+i;
                bHasProject=true;
            %>
                        <c-rt:set var="i" value="<%=sI%>"/>
                        <tr>
                <%
                                boolean bRow=true;
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
                if (bHasProject) {
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
                            }
                            else {
                        %>
                        <tr>
                            <td class="tableRow">No Project Found</td>
                            <td colspan="<%=sTotalDay%>" class="tableRow">&nbsp;</td>
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



        </td>
    </tr>
    </table>
  </body>
</html>
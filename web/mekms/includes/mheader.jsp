<%@ page import="java.util.Calendar,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 kacang.services.security.User"%>
<%@ include file="/common/header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<c:if test="${empty requestScope.headerCaption}">
    <c:set var="headerCaption" value="Summary" scope="request"/>
</c:if>
<html>
<head>
    <title>Mobile EKMS</title>
    <link href="<c:url value="/mekms/images/style.css"/>" rel="stylesheet" type="text/css">
</head>
<body leftmargin=0 rightmargin=0 topmargin=0 marginheight=0 marginwidth=0>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td><img src="<c:url value="/mekms/images/logo.gif"/>"></td>
        <td align="right"><a href="<c:url value="/mekms/index.jsp"/>" class="home">Home</a>&nbsp;</td>
    </tr>
    <tr bgcolor="#29558A">
        <td colspan="2">
            <%
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                User user = service.getCurrentUser(request);
                if(!(SecurityService.ANONYMOUS_USER_ID.equals(user.getId()) || "".equals(user.getId())))
                {
            %>
            <table border="0" align="center" cellpadding="0" cellspacing="2">
                <%--?cn=calendarPage.calendarView&et=view&view=mDaily&calendarPage.calendarView*day=3&calendarPage.calendarView*month=5&calendarPage.calendarView*year=2007--%>
                <form action="<c:url value="/mekms/calendar/calendar.jsp"/>" method="get">
                <input name="cn" type="hidden" value="calendarPage.calendarView">
                <input name="et" type="hidden" value="view">
                <input name="view" type="hidden" value="mDaily">
                <% Calendar cal = Calendar.getInstance(); %>
                <tr>
                    <%--<td width="50" class="search"> Calender</td>--%>
                    <td>
                        <select name="calendarPage.calendarView*day" size="0" class="field">
                            <option value="<%= cal.get(Calendar.DAY_OF_MONTH) %>" selected>dd</option>
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                            <option value="6">6</option>
                            <option value="7">7</option>
                            <option value="8">8</option>
                            <option value="9">9</option>
                            <option value="10">10</option>
                            <option value="11">11</option>
                            <option value="12">12</option>
                            <option value="13">13</option>
                            <option value="14">14</option>
                            <option value="15">15</option>
                            <option value="16">16</option>
                            <option value="17">17</option>
                            <option value="18">18</option>
                            <option value="19">19</option>
                            <option value="20">20</option>
                            <option value="21">21</option>
                            <option value="22">22</option>
                            <option value="23">23</option>
                            <option value="24">24</option>
                            <option value="25">25</option>
                            <option value="26">26</option>
                            <option value="27">27</option>
                            <option value="28">28</option>
                            <option value="29">29</option>
                            <option value="30">30</option>
                            <option value="31">31</option>
                        </select>
                        <select name="calendarPage.calendarView*month" size="0" class="field">
                            <option value="<%= cal.get(Calendar.MONTH) %>" selected>mm</option>
                            <option value="0">Jan</option>
                            <option value="1">Feb</option>
                            <option value="2">March</option>
                            <option value="3">April</option>
                            <option value="4">May</option>
                            <option value="5">June</option>
                            <option value="6">July</option>
                            <option value="7">Aug</option>
                            <option value="8">Sept</option>
                            <option value="9">Oct</option>
                            <option value="10">Nov</option>
                            <option value="11">Dec</option>
                        </select>
                        <input name="calendarPage.calendarView*year" type="hidden" value="<%= cal.get(Calendar.YEAR) %>">
                        <input name="Submit" type="submit" class="box" value="Go">
                    </td>
                </tr>
                </form>
            </table>
            <%
                }
                else
                {
            %>&nbsp;<%                    
                }
            %>
        </td>
    </tr>
    <tr><td height="20" align="center" background="<c:url value="/mekms/images/h1bg.gif"/>" colspan="2"><c:out value="${requestScope.headerCaption}"/></td></tr>
    <tr bgcolor="#E6EDF0">
        <td colspan="2" WIDTH="100%" align="center">

<%@ page import="kacang.stdui.CalendarBox,
                 java.util.ArrayList,
                 java.util.Iterator,
                 java.util.Enumeration"%>
<%@include file="/common/header.jsp"%>

<c:set var="calendar" value="${widget}"/>
<c:set var="archiveDates" value="${calendar.archiveDates}"/>
<table style="border: 1px solid silver" border="0" cellpadding="0"cellspacing="0">

<thead>
<Tr>
    <th colspan="7" align="center">
        <a style="text-decoration: none; color: black" href="<c:out value="${widget.blogUrl}" />?blogId=<c:out value="${widget.blogId}"/>&date=<c:out value="${widget.previousUrl}" />">&lt;&lt;</a>&nbsp;
        <fmt:formatDate value="${calendar.date}" pattern="MMMMMM yyyy" />
        <a style="text-decoration: none; color: black" href="<c:out value="${widget.blogUrl}" />?blogId=<c:out value="${widget.blogId}"/>&date=<c:out value="${widget.nextUrl}" />">&gt;&gt;</a>
    </th>
</tr>

<tr>
<c:forEach items="${calendar.daysInWeek}" var="day">
<div  style="border-bottom: 1px solid #B39169;">  <td style="border-bottom: 1px solid #B39169;">&nbsp;<c:out value="${day}"/>&nbsp; </td></div>

</c:forEach></tr>


</thead>

<tbody>
<c:forEach var="week" items="${calendar.calendarEntries}">
<tr >
  <c:forEach var="day" items="${week}" >

   <c:set var="iday" value="${day}" />
  <td align="center">

        <%
            ArrayList dates = (ArrayList)pageContext.getAttribute("archiveDates");

            Integer iDate = (Integer)pageContext.findAttribute("iday");
            if(dates.contains(iDate)){
            %>
		   <fmt:formatNumber pattern="00" var="paddedDay" value="${day}"/>   
           <a href="<c:out value="${widget.blogUrl}" />?blogId=<c:out value="${widget.blogId}"/>&date=<c:out value="${widget.yearStr}"/><c:out value="${widget.monthStr}"/><c:out value="${paddedDay}" />">
        <%
                }%>
        <c:choose>
        <c:when test="${calendar.baseMonth && calendar.baseDayOfMonth == day}">
            <div style="border: 1px solid #B39169"><c:out value="${day}"/></div>
          </c:when>
          <c:when test="${!empty day}" >
            <c:out value="${day}"/>
          </c:when>
        </c:choose>
       <%     if(dates.contains(iDate)){
            %>
        </a>
        <%}%>

  </td>
  </c:forEach>
</tr>
</c:forEach>
</tbody>

<tfoot>
    <Tr>
        <td align="center" colspan="7">
            <a href="<c:out value="${widget.blogUrl}" />?blogId=<c:out value="${widget.blogId}"/>&date=<c:out value="${widget.todayUrl}" />">Today</a>&nbsp;
        </td>
    </tr>
</tfoot>


</table>
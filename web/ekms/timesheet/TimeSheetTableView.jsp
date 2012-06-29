<%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: May 3, 2005
  Time: 3:01:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/header.jsp"%>

<x:config>
    <page  name="tsTablePage">
        <com.tms.collab.timesheet.ui.TimeSheetTableView name="tsTable" width="100%"/>
    </page>
</x:config>




 <c:choose>
   <c:when test="${forward.name == 'empty'}" >
    <script>
     alert("Please select at least one item(s)");
    </script>
   </c:when>
</c:choose>





<c:if test="${not empty(param.date)}">
	<x:set name="tsTablePage.tsTable" property="startDate" value="${param.date}"/>
	<x:set name="tsTablePage.tsTable" property="endDate" value="${param.date}"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<table width="100%" cellpadding="4" cellspacing="0">
<tr>
    <td class="calendarHeader"><fmt:message key='timesheet.label.timesheet'/> > <fmt:message key="timesheet.label.viewbydate"/></td>
</tr>
</table>
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
<td bgcolor="#efefef" class="contentBgColor"><x:display name="tsTablePage.tsTable"/></td>
</tr>
</table>


<%@include file="/ekms/includes/footer.jsp" %>
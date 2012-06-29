<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.crm.sales.misc.*, java.util.*, kacang.ui.WidgetManager"%>

<x:config>
    <page name="jsp_listingClosedSale_Group">
     	<com.tms.crm.sales.ui.ListingTable name="table1" type="ClosedSale_List" subType="Group" width="100%" />
    </page>
</x:config>


<c:choose>
	<c:when test="${not empty(param.fromDate)}">
		<c:set var="groupID"  value="${param.groupID}"/>
		<c:set var="fromDate" value="${param.fromDate}"/>
		<c:set var="toDate"   value="${param.toDate}"/>
	</c:when>
	<c:otherwise>
		<c:set var="groupID"  value="${widgets['jsp_listingClosedSale_Group.table1'].groupID}"/>
		<c:set var="fromDate" value="${widgets['jsp_listingClosedSale_Group.table1'].fromDate}"/>
		<c:set var="toDate"   value="${widgets['jsp_listingClosedSale_Group.table1'].toDate}"/>
	</c:otherwise>
</c:choose>
<x:set name="jsp_listingClosedSale_Group.table1" property="groupID"  value="${groupID}" />
<x:set name="jsp_listingClosedSale_Group.table1" property="fromDate" value="${fromDate}" />
<x:set name="jsp_listingClosedSale_Group.table1" property="toDate"   value="${toDate}" />
<c:set var="groupName"  value="${widgets['jsp_listingClosedSale_Group.table1'].groupName}"/>


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
	<%
		Date fromDate = DateUtil.getDateFromDateString((String) pageContext.getAttribute("fromDate"));
		Date toDate   = DateUtil.getDateFromDateString((String) pageContext.getAttribute("toDate"));
		String startMonth = DateUtil.formatDate("MMMM yyyy", fromDate);
		String endMonth   = DateUtil.formatDate("MMMM yyyy", toDate);
		String dateRange;
		if (startMonth.equals(endMonth)) {
			dateRange = startMonth;
		} else {
			dateRange = startMonth + " to " + endMonth;
		}
	%>
	<fmt:message key='sfa.message.groupClosedSale'/> - <%=dateRange%>	&nbsp;[<c:out value="${groupName}" />]
    </td>
    </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_listingClosedSale_Group.table1"></x:display>

</td>
    </tr>
    <tr>
    <td class="sfaFooter">
                         &nbsp;
    </td>
    </tr>


</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>

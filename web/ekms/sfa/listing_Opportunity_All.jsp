<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.crm.sales.misc.*, java.util.*"%>

<x:config>
    <page name="jsp_listingOpportunity_All">
     	<com.tms.crm.sales.ui.OpportunityListingTable name="table1" width="100%" />
    </page>
</x:config>

<c:if test="${! empty param.companyID}">
    <c:redirect url="/ekms/sfa/company_view_commain.jsp?companyID=${param.companyID}" />
</c:if>

<c:if test="${! empty param.opportunityID}" >
    <c:redirect url="/ekms/sfa/opportunity_details.jsp?opportunityID=${param.opportunityID}" />

</c:if>


<c:choose>
	<c:when test="${not empty(param.fromDate)}">
		<c:set var="sfromDate" value="${param.fromDate}"/>
		<c:set var="stoDate"   value="${param.toDate}"/>
        <%
            pageContext.setAttribute("fromDate",DateUtil.getDateFromDateString((String)pageContext.getAttribute("sfromDate")));
            pageContext.setAttribute("toDate",DateUtil.getDateFromDateString((String)pageContext.getAttribute("stoDate")));
        %>
        <x:set name="jsp_listingOpportunity_All.table1" property="fromDate" value="${fromDate}" />
        <x:set name="jsp_listingOpportunity_All.table1" property="toDate"   value="${toDate}" />
	</c:when>

	<c:otherwise>
		<c:set var="fromDate" value="${widgets['jsp_listingOpportunity_All.table1'].fromDate}"/>
		<c:set var="toDate"   value="${widgets['jsp_listingOpportunity_All.table1'].toDate}"/>
	</c:otherwise>

</c:choose>


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">

    <%
		Date fromDate = (Date)pageContext.getAttribute("fromDate");//DateUtil.getDateFromDateString((String) pageContext.getAttribute("fromDate"));
		Date toDate   = (Date)pageContext.getAttribute("toDate");//DateUtil.getDateFromDateString((String) pageContext.getAttribute("toDate"));
		String startMonth = DateUtil.formatDate("MMMM yyyy", fromDate);
		String endMonth   = DateUtil.formatDate("MMMM yyyy", toDate);
		String dateRange;
		if (startMonth.equals(endMonth)) {
			dateRange = startMonth;
		} else {
			dateRange = startMonth + " to " + endMonth;
		}
	%>


	<fmt:message key='sfa.message.opportunities'/> - <%=dateRange%>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_listingOpportunity_All.table1"></x:display>
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

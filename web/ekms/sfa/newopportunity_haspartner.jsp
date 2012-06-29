<%@ include file="/common/header.jsp" %>
<%@page import="com.tms.crm.sales.misc.*" %>

<% NaviUtil naviUtil = new NaviUtil(pageContext); %>


<c:set var="opportunityID" value="${param.opportunityID}"/>
<% naviUtil.hasPartner("opportunityID", "hasPartner"); %>

<c:choose>
	<c:when test="${hasPartner}">
		<c:redirect url="newopportunity_partner_list.jsp?opportunityID=${opportunityID}"/>
	</c:when>
	<c:otherwise>
		<%--
		<% naviUtil.getCompanyID4Opportunity("opportunityID", "companyID"); %>
		<c:redirect url="opportunity_list.jsp?companyID=${companyID}"/>
		--%>
		<c:redirect url="opportunity_details.jsp?opportunityID=${opportunityID}"/>
	</c:otherwise>
</c:choose>

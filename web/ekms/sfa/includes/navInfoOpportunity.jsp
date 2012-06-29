<%@include file="/common/header.jsp" %>
<%@page import="com.tms.crm.sales.misc.*, com.tms.crm.sales.model.*, java.util.*,
                kacang.Application" %>

<%
	int infoIndex = Integer.parseInt((String) request.getAttribute("infoIndex"));
	String companyID = null;
	String opportunityID = null;
	
	NaviUtil naviUtil = new NaviUtil(pageContext);
	if (infoIndex == 0) {
		if (request.getAttribute("companyID") != null) {
			companyID = (String) request.getAttribute("companyID");
		}
	} else {
		if (request.getAttribute("opportunityID") != null) {
			opportunityID = (String) request.getAttribute("opportunityID");
			companyID = naviUtil.getCompanyID4Opportunity(opportunityID);
		}
	}
	
	Company com = naviUtil.getCompany(companyID);
	Opportunity opp = null;
	if (infoIndex > 0) {
		opp = naviUtil.getOpportunity(opportunityID);
	}
	Company partner = null;
	if (infoIndex > 1) {
		String partnerID = naviUtil.getPartnerCompanyID4Opportunity(opportunityID);
		partner = naviUtil.getCompany(partnerID);
	}
%>

<table border="0" cellspacing="0" cellpadding="5">
<tr>
  <td><b><fmt:message key='sfa.message.company'/>: </b></td>
  <td><span class="mediumTitleStyle"><%=com.getCompanyName()%></span><font size="1" face="Verdana"><!-- [<%=com.getCompanyState()%>, <%=com.getCompanyCountry()%>]--></font></td>
</tr>
<% if (infoIndex > 0) { %>
<tr>
  <td><b><fmt:message key='sfa.message.opportunity'/>: </b></td>
  <td><span class="mediumTitleStyle"><%=opp.getOpportunityName()%></span></td>
</tr>
<% } %>
<c:if test="${productsSelected}" >
<tr >
  <td><b><fmt:message key='sfa.message.products'/>: </b></td>
  <td><span class="mediumTitleStyle">

    <%
        OpportunityProductModule module = (OpportunityProductModule) Application.getInstance().getModule(OpportunityProductModule.class);
        Collection col = module.listOpportunityProduct(request.getAttribute("opportunityID").toString(),"productName",false,0,-1);
        pageContext.setAttribute("products",col);
    %>
    <c:forEach items="${products}"var="product"varStatus="status" >
        <c:if test="${status.count>1}" >
        ,
        </c:if>
        <c:out value="${product.productName}" />
    </c:forEach>

  </span></td>
</tr>
</c:if>

<% if (infoIndex > 1) { %>
<tr >
  <td ><b><fmt:message key='sfa.message.partner'/>: </b></td>
  <td ><span class="mediumTitleStyle"><%=partner.getCompanyName()%></span></td>
</tr>
<% } %>
</table>
<br>

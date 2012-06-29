<%@page import="kacang.Application, kacang.services.security.*, kacang.ui.WidgetManager" %>
<%@page import="java.util.*, com.tms.crm.sales.misc.*, com.tms.crm.sales.model.*" %>
<%@ include file="/common/header.jsp" %>

<%
	OpportunityModule module = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
	
	String opportunityID = request.getParameter("opportunityID");
	
	User user = (User) session.getAttribute("currentUser");
	String userID = user.getId();
	boolean isSalesManager = AccessUtil.isSalesManager(userID);
	
	boolean status = false;
	if (opportunityID != null && isSalesManager) {
		NaviUtil naviUtil = new NaviUtil(pageContext); 
		Integer opportunityStatus = naviUtil.getStatus(opportunityID);
		
		if (!opportunityStatus.equals(Opportunity.STATUS_CLOSE)) {
			status = module.deleteOpportunity(opportunityID);
		}
	}
%>

<script>
<% if (status) { %>
	alert("Opportunity deleted");
<% } else { %>
	alert("Opportunity NOT deleted");
<% } %>
	location = 'main.jsp';
</script>
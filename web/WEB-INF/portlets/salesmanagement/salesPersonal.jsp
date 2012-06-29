<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, java.text.*, com.tms.crm.sales.model.*, com.tms.crm.sales.misc.*" %>
<%@page import="kacang.*, kacang.services.security.*, kacang.ui.WidgetManager" %>

<%
	WidgetManager wm     = WidgetManager.getWidgetManager(request);
	String        userID = wm.getUser().getId();
	
	SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
	boolean accessSalesManager = security.hasPermission(userID, "com.tms.crm.sales.SalesManager", "com.tms.crm.sales.model.AccountManagerModule", null);
	boolean accessSalesPerson  = security.hasPermission(userID, "com.tms.crm.sales.SalesPerson", "com.tms.crm.sales.model.AccountManagerModule", null);

	Date nowdate = DateUtil.getToday();
	int nowyear = DateUtil.getYear(nowdate);
	NumberFormat form = NumberFormat.getIntegerInstance();

	OPPModule opp = (OPPModule)Application.getInstance().getModule(OPPModule.class);
	
	int currMonthProj = opp.calculateCurrMonthProj(nowdate, userID);
	int currQuarterProj = opp.calculateCurrQuarterProj(nowdate, userID);
	int currAnnualProj = opp.calculateCurrAnnualProj(nowdate, userID);
	
	double currMonthOpp = opp.calculateCurrMonthOpp(nowdate, userID);
	double currMonthWeightedOpp = opp.calculateCurrMonthWeightedOpp(nowdate, userID);
	double currMonthSales = opp.calculateCurrMonthSales(nowdate, userID);
	
	double currQuarterOpp = opp.calculateCurrQuarterOpp(nowdate, userID);
	double currQuarterWeightedOpp = opp.calculateCurrQuarterWeightedOpp(nowdate, userID);
	double currQuarterSales = opp.calculateCurrQuarterSales(nowdate, userID);
	
	double currAnnualOpp = opp.calculateCurrAnnualOpp(nowdate, userID);
	double currAnnualWeightedOpp = opp.calculateCurrAnnualWeightedOpp(nowdate, userID);
	double currAnnualSales = opp.calculateCurrAnnualSales(nowdate, userID);
%>

<fmt:message key='sfa.label.summaryPersonalSales'/>
<% if (accessSalesPerson) { %>
	<a href="<%= request.getContextPath() %>/ekms/sfa/main.jsp" ><fmt:message key='sfa.label.more'/></a>
	<br><br>


<table border="1" cellspacing="0" cellpadding="2">
	<tr align="center">
		<td width="31%">&nbsp;</td>
		<td width="23%"><b><fmt:message key='sfa.label.month'/></b></td>
		<td width="23%"><b><fmt:message key='sfa.label.quarter'/></b></td>
		<td width="23%"><b><fmt:message key='sfa.label.year'/></b></td>
	</tr>
	<tr align="center">
		<td><fmt:message key='sfa.label.projections'/></td>
		<td><%= form.format(currMonthProj) %></td>
		<td><%= form.format(currQuarterProj) %></td>
		<td><%= form.format(currAnnualProj) %></td>
	</tr>
	<tr align="center">
		<td><fmt:message key='sfa.label.opportunities'/></td>
		<td><%= form.format(currMonthOpp) %></td>
		<td><%= form.format(currQuarterOpp) %></td>
		<td><%= form.format(currAnnualOpp) %></td>
	</tr>
	<tr align="center">
		<td><fmt:message key='sfa.label.wOpportunities'/></td>
		<td><%= form.format(currMonthWeightedOpp) %></td>
		<td><%= form.format(currQuarterWeightedOpp) %></td>
		<td><%= form.format(currAnnualWeightedOpp) %></td>
	</tr>
	<tr align="center">
		<td><fmt:message key='sfa.label.sales'/></td>
		<td><%= form.format(currMonthSales) %></td>
		<td><%= form.format(currQuarterSales) %></td>
		<td><%= form.format(currAnnualSales) %></td>
	</tr>
</table>
<% } else { %>
	<br><br><i><fmt:message key='sfa.label.sorry'/></i><br>
<% } %>
<br>

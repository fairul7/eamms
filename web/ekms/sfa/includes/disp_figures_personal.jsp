<%@ page import="kacang.*, java.util.*, java.text.*" %>
<%@ page import="com.tms.crm.sales.model.*, com.tms.crm.sales.misc.*" %>
<%@ include file="/common/header.jsp" %>
<%
		String       userID    = (String) request.getAttribute("userID");
		Date         nowdate   = (Date) request.getAttribute("nowdate");
		OPPModule    opp       = (OPPModule) request.getAttribute("opp");
		NumberFormat numFormat = (NumberFormat) request.getAttribute("numFormat");
		
		Date personalDate = DateUtil.beginningOfMonth(nowdate);
		
		try {
			String personal_year  = request.getParameter("personal_year");
			String personal_month = request.getParameter("personal_month");
			if (personal_year != null && personal_month != null) {
				personalDate = DateUtil.getDate(Integer.parseInt(personal_year), Integer.parseInt(personal_month), 1);
				session.setAttribute("personal_date", personalDate);
			} else if (session.getAttribute("personal_date") != null) {
				personalDate = (Date) session.getAttribute("personal_date");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		Date   startMonth = DateUtil.beginningOfMonth(personalDate);
		Date   endMonth   = DateUtil.endOfMonth(personalDate);
		String currMonthFrom = DateUtil.getDateString(startMonth);
		String currMonthTo   = DateUtil.getDateString(endMonth);
		
		Date   startQuarter = DateUtil.beginningOfQuarter(personalDate);
		Date   endQuarter   = DateUtil.endOfQuarter(personalDate);
		String currQuarterFrom = DateUtil.getDateString(startQuarter);
		String currQuarterTo   = DateUtil.getDateString(endQuarter);
		
		Date   startYear = DateUtil.beginningOfYear(personalDate);
		Date   endYear   = DateUtil.endOfYear(personalDate);
		String currYearFrom = DateUtil.getDateString(startYear);
		String currYearTo   = DateUtil.getDateString(endYear);
		
		
		double icurrMonthOpp         = opp.calculateCurrOpp(startMonth, endMonth, userID);
		double icurrMonthWeightedOpp = opp.calculateCurrWeightedOpp(startMonth, endMonth, userID);
		double icurrMonthSales       = opp.calculateCurrSales(startMonth, endMonth, userID);
		
		double icurrQuarterOpp         = opp.calculateCurrOpp(startQuarter, endQuarter, userID);
		double icurrQuarterWeightedOpp = opp.calculateCurrWeightedOpp(startQuarter, endQuarter, userID);
		double icurrQuarterSales       = opp.calculateCurrSales(startQuarter, endQuarter, userID);
		
		double icurrAnnualOpp         = opp.calculateCurrOpp(startYear, endYear, userID);
		double icurrAnnualWeightedOpp = opp.calculateCurrWeightedOpp(startYear, endYear, userID);
		double icurrAnnualSales       = opp.calculateCurrSales(startYear, endYear, userID);
		
		int icurrMonthProj   = opp.calculateCurrProj(startMonth, endMonth, userID);
		int icurrQuarterProj = opp.calculateCurrProj(startQuarter, endQuarter, userID);
		int icurrAnnualProj  = opp.calculateCurrProj(startYear, endYear, userID);
		
		
		Date minusMonth = DateUtil.dateAdd(personalDate, Calendar.MONTH, -1);
		Date plusMonth  = DateUtil.dateAdd(personalDate, Calendar.MONTH, 1);
		
		
		SalesGroupModule sgModule = (SalesGroupModule) Application.getInstance().getModule(SalesGroupModule.class);
		Collection sgCol = sgModule.getSalesGroups(userID);
		
		String personal_groupID = "";
		if (request.getParameter("personal_groupID") != null) {
			personal_groupID = request.getParameter("personal_groupID");
			session.setAttribute("personal_groupID", personal_groupID);
		} else if (session.getAttribute("personal_groupID") != null) {
			personal_groupID = (String) session.getAttribute("personal_groupID");
		} else if (sgCol!=null&&sgCol.size() != 0) {
			Iterator iterator = sgCol.iterator();
			personal_groupID = ((SalesGroup) iterator.next()).getId();
		}
			
		
		double grpMonthSales   = opp.calculateCurrSales_Group(startMonth, endMonth, personal_groupID);
		double grpQuarterSales = opp.calculateCurrSales_Group(startQuarter, endQuarter, personal_groupID);
		double grpAnnualSales  = opp.calculateCurrSales_Group(startYear, endYear, personal_groupID);
		
		int grpMonthProj   = opp.calculateCurrProj_Group(startMonth, endMonth, personal_groupID);
		int grpQuarterProj = opp.calculateCurrProj_Group(startQuarter, endQuarter, personal_groupID);
		int grpAnnualProj  = opp.calculateCurrProj_Group(startYear, endYear, personal_groupID);
%>

		<%
			// Getting the financial setting : currency symbols
			OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
			Opportunity csign = om.getFinancilSetting();
		%>
<table border="0">
<tr>
  <td>
    <span class="sfaRowLabel"><fmt:message key='sfa.message.personalSummary'/></span>
    <span class="smallTitleBoldStyle">&gt; <%=DateUtil.formatDate("MMM yyyy", personalDate)%> &gt;
        <input type="button" class="button" value="<fmt:message key='sfa.message.previous'/>" onClick="location = '?personal_year=<%=DateUtil.getYear(minusMonth)%>&personal_month=<%=DateUtil.getMonth(minusMonth)%>'" />
        <input type="button" class="button" value="<fmt:message key='sfa.message.now'/>" onClick="location = '?personal_year=<%=DateUtil.getYear(nowdate)%>&personal_month=<%=DateUtil.getMonth(nowdate)%>'" />
        <input type="button" class="button" value="<fmt:message key='sfa.message.next'/>" onClick="location = '?personal_year=<%=DateUtil.getYear(plusMonth)%>&personal_month=<%=DateUtil.getMonth(plusMonth)%>'" />
  </span></td>
</tr>
</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr valign="top"> 
  <td width="30%"><table width="460" border="1" cellpadding="5" cellspacing="0">
      <tr bgcolor="#CCCCCC"> 
        <td colspan="2"><span class="mediumTitleBoldStyle"><fmt:message key='sfa.message.individual'/></span></td>
        <td><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.monthly'/> (<%=csign.getCurrencySymbol()%>)</span></div></td>
        <td><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.quarterly'/> (<%=csign.getCurrencySymbol()%>)</span></div></td>
        <td><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.yearly'/> (<%=csign.getCurrencySymbol()%>)</span></div></td>
      </tr>
      <tr> 
        <td width="1%"><img src="images/priority.gif" width="29" height="29"></td>
        <td width="50%"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.projection'/></span><br><span class="smallTitleStyle"> <fmt:message key='sfa.message.allocatedindividualprojection'/></span></td>
        <td width="12%" bgcolor="#FFFFCC"><div align="center"><span class="smallTitleStyle"><%= numFormat.format(icurrMonthProj) %></span></div></td>
        <td width="12%"><div align="center"><span class="smallTitleStyle"><%= numFormat.format(icurrQuarterProj) %></span></div></td>
        <td width="12%"><div align="center"><span class="smallTitleStyle"><%= numFormat.format(icurrAnnualProj) %></span></div></td>
      </tr>
      <tr> 
        <td><img src="images/priority.gif" width="29" height="29"></td>
        <td><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.totalOpportunities'/></span><br><span class="smallTitleStyle"> <fmt:message key='sfa.message.sumofallyouropportunities'/></span></td>
        <td bgcolor="#FFFFCC"><div align="center"><span class="smallTitleStyle"><a href="listing_Opportunity_User.jsp?fromDate=<%=currMonthFrom%>&toDate=<%=currMonthTo%>"><%= numFormat.format(icurrMonthOpp) %></a></span></div></td>
        <td><div align="center"><span class="smallTitleStyle"><a href="listing_Opportunity_User.jsp?fromDate=<%=currQuarterFrom%>&toDate=<%=currQuarterTo%>"><%= numFormat.format(icurrQuarterOpp) %></a></span></div></td>
        <td><div align="center"><span class="smallTitleStyle"><a href="listing_Opportunity_User.jsp?fromDate=<%=currYearFrom%>&toDate=<%=currYearTo%>"><%= numFormat.format(icurrAnnualOpp) %></a></span></div></td>
      </tr>
      <tr> 
        <td><img src="images/priority.gif" width="29" height="29"></td>
        <td><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.totalAdjustedOpportunities'/></span><br><span class="smallTitleStyle"> <fmt:message key='sfa.message.totaladjustedbasedonstageofopportunity'/></span></td>
        <td bgcolor="#FFFFCC"><div align="center"><span class="smallTitleStyle"><%= numFormat.format(icurrMonthWeightedOpp) %></span></div></td>
        <td><div align="center"><span class="smallTitleStyle"><%= numFormat.format(icurrQuarterWeightedOpp) %></span></div></td>
        <td><div align="center"><span class="smallTitleStyle"><%= numFormat.format(icurrAnnualWeightedOpp) %></span></div></td>
      </tr>
      <tr> 
        <td><img src="images/priority.gif" width="29" height="29"></td>
        <td><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.totalSalesToDate'/></span><br><span class="smallTitleStyle"> <fmt:message key='sfa.message.totalsalesyouachievedtodate'/></span></td>
        <td bgcolor="#FFFFCC"><div align="center"><span class="smallTitleStyle"><a href="listing_ClosedSale_User.jsp?fromDate=<%=currMonthFrom%>&toDate=<%=currMonthTo%>"><%= numFormat.format(icurrMonthSales) %></a><br>
            [<%= opp.calculatePercentage(icurrMonthSales, icurrMonthProj) %>%]</span></div></td>
        <td><div align="center"><span class="smallTitleStyle"><a href="listing_ClosedSale_User.jsp?fromDate=<%=currQuarterFrom%>&toDate=<%=currQuarterTo%>"><%= numFormat.format(icurrQuarterSales) %></a><br>
            [<%= opp.calculatePercentage(icurrQuarterSales, icurrQuarterProj) %>%]</span></div></td>
        <td><div align="center"><span class="smallTitleStyle"><a href="listing_ClosedSale_User.jsp?fromDate=<%=currYearFrom%>&toDate=<%=currYearTo%>"><%= numFormat.format(icurrAnnualSales) %></a><br>
            [<%= opp.calculatePercentage(icurrAnnualSales, icurrAnnualProj) %>%]</span></div></td>
      </tr>
      <tr> 
        <td><img src="images/priority.gif" width="29" height="29"></td>
        <td><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.salesBalanceToDate'/></span><br><span class="smallTitleStyle"> <fmt:message key='sfa.message.totalsalesyettobeachievedtodate'/></span></td>
        <td bgcolor="#FFFFCC"><div align="center"><span class="smallTitleBoldStyle"><%= numFormat.format(Math.max(icurrMonthProj - icurrMonthSales, 0)) %></span></div></td>
        <td><div align="center"><span class="smallTitleBoldStyle"><%= numFormat.format(Math.max(icurrQuarterProj - icurrQuarterSales, 0)) %></span></div></td>
        <td><div align="center"><span class="smallTitleBoldStyle"><%= numFormat.format(Math.max(icurrAnnualProj - icurrAnnualSales, 0)) %></span></div></td>
      </tr>
    </table>
    
  </td>
  <td width="1%">&nbsp;&nbsp;</td>
  <td width="60%">
	<%

	%>
    <table border="0" cellpadding="1" cellspacing="0">
      <tr>
        <td><span class="mediumTitleBoldStyle"><fmt:message key='sfa.message.group'/>:&nbsp;</span></td>
        <td>
          <select class="mediumTitleStyle" onchange="location='main.jsp?personal_groupID=' + this.options[this.selectedIndex].value">
            <% try { %>
            <% Iterator iterator = sgCol.iterator(); %>
            <% while (iterator.hasNext()) { %>
            	<% SalesGroup sg = (SalesGroup) iterator.next(); %>
              <option value="<%=sg.getId()%>" <% if (personal_groupID.equals(sg.getId())) { %>selected<% } %>><%=sg.getGroupName()%></option>
            <% } %>
            <% } catch (Exception e) { %>
            <% 	e.printStackTrace(); %>
            <% } %>
          </select>
        </td>
      </tr>
    </table>
    
    <table width="100%" border="1" cellpadding="5" cellspacing="0">
      <tr bgcolor="#B7CCE3"> 
        <td bgcolor="#B7CCE3"><span class="mediumTitleBoldStyle"><fmt:message key='sfa.message.group'/></span></td>
        <td width="12%"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.monthly'/> (<%=csign.getCurrencySymbol()%>)</span></div></td>
        <td width="12%"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.quarterly'/> (<%=csign.getCurrencySymbol()%>)</span></div></td>
        <td width="13%"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.yearly'/> (<%=csign.getCurrencySymbol()%>)</span></div></td>
      </tr>
      <tr> 
        <td width="62%"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.groupProjection'/></span></td>
        <td><div align="center"><span class="smallTitleStyle"><%= numFormat.format(grpMonthProj) %></span></div></td>
        <td><div align="center"><span class="smallTitleStyle"><%= numFormat.format(grpQuarterProj) %></span></div></td>
        <td><div align="center"><span class="smallTitleStyle"><%= numFormat.format(grpAnnualProj) %></span></div></td>
      </tr>
      <tr> 
        <td><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.totalGroupSalesToDate'/></td>
        <td><div align="center"><span class="smallTitleStyle"><a href="listing_ClosedSale_Group.jsp?groupID=<%=personal_groupID%>&fromDate=<%=currMonthFrom%>&toDate=<%=currMonthTo%>"><%= numFormat.format(grpMonthSales) %></a><br> [<%= opp.calculatePercentage(grpMonthSales, grpMonthProj) %>%]</span></div></td>
        <td><div align="center"><span class="smallTitleStyle"><a href="listing_ClosedSale_Group.jsp?groupID=<%=personal_groupID%>&fromDate=<%=currQuarterFrom%>&toDate=<%=currQuarterTo%>"><%= numFormat.format(grpQuarterSales) %></a><br> [<%= opp.calculatePercentage(grpQuarterSales, grpQuarterProj) %>%]</span></div></td>
        <td><div align="center"><span class="smallTitleStyle"><a href="listing_ClosedSale_Group.jsp?groupID=<%=personal_groupID%>&fromDate=<%=currYearFrom%>&toDate=<%=currYearTo%>"><%= numFormat.format(grpAnnualSales) %></a><br> [<%= opp.calculatePercentage(grpAnnualSales, grpAnnualProj) %>%]</span></div></td>
      </tr>
      <tr> 
        <td><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.groupSalesBalanceToDate'/></td>
        <td><div align="center"><span class="smallTitleBoldStyle"><%= numFormat.format(Math.max(grpMonthProj - grpMonthSales, 0)) %></span></div></td>
        <td><div align="center"><span class="smallTitleBoldStyle"><%= numFormat.format(Math.max(grpQuarterProj - grpQuarterSales, 0)) %></span></div></td>
        <td><div align="center"><span class="smallTitleBoldStyle"><%= numFormat.format(Math.max(grpAnnualProj - grpAnnualSales, 0)) %></span></div></td>
      </tr>
    </table></td>
</tr>
</table>
<br>
<br>

<%@ page import="kacang.*, java.util.*, java.text.*" %>
<%@ page import="com.tms.crm.sales.model.*, com.tms.crm.sales.misc.*" %>
<%@taglib uri='/WEB-INF/cewolf.tld' prefix='cewolf' %>
<%@ include file="/common/header.jsp" %>

<%
		Date         nowdate   = (Date) request.getAttribute("nowdate");
		OPPModule    opp       = (OPPModule) request.getAttribute("opp");
		NumberFormat numFormat = (NumberFormat) request.getAttribute("numFormat");
		
		String currMonthFrom = DateUtil.getDateString(DateUtil.beginningOfMonth(nowdate));
		String currMonthTo   = DateUtil.getDateString(DateUtil.endOfMonth(nowdate));
		
		String currQuarterFrom = DateUtil.getDateString(DateUtil.beginningOfQuarter(nowdate));
		String currQuarterTo   = DateUtil.getDateString(DateUtil.endOfQuarter(nowdate));
		
		String currYearFrom = DateUtil.getDateString(DateUtil.beginningOfYear(nowdate));
		String currYearTo   = DateUtil.getDateString(DateUtil.endOfYear(nowdate));
		
		
		int currMonthProj = opp.calculateCurrMonthProj(nowdate, "");
		int currQuarterProj = opp.calculateCurrQuarterProj(nowdate, "");
		int currAnnualProj = opp.calculateCurrAnnualProj(nowdate, "");
		
		double currMonthOpp = opp.calculateCurrMonthOpp(nowdate, "");
		double currMonthWeightedOpp = opp.calculateCurrMonthWeightedOpp(nowdate, "");
		double currMonthSales = opp.calculateCurrMonthSales(nowdate, "");
		
		double currQuarterOpp = opp.calculateCurrQuarterOpp(nowdate, "");
		double currQuarterWeightedOpp = opp.calculateCurrQuarterWeightedOpp(nowdate, "");
		double currQuarterSales = opp.calculateCurrQuarterSales(nowdate, "");
		
		double currAnnualOpp = opp.calculateCurrAnnualOpp(nowdate, "");
		double currAnnualWeightedOpp = opp.calculateCurrAnnualWeightedOpp(nowdate, "");
		double currAnnualSales = opp.calculateCurrAnnualSales(nowdate, "");
%>

		<%
			// Getting the financial setting : currency symbols
			OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
			Opportunity csign = om.getFinancilSetting();

		%>

<jsp:useBean id="pageViews" class="com.tms.crm.sales.ui.SalesChart"/>
<cewolf:chart id="line" type="verticalbar" showlegend="true">
	<cewolf:colorpaint color="#FFFFFF"/> 
	<cewolf:data>
		<cewolf:producer id="pageViews">
			<cewolf:param name="value1" value="<%= new Float(currMonthProj) %>"/>
			<cewolf:param name="value2" value="<%= new Float(currMonthOpp) %>"/>
			<cewolf:param name="value3" value="<%= new Float(currMonthWeightedOpp) %>"/>
			<cewolf:param name="value4" value="<%= new Float(currMonthSales) %>"/>
		</cewolf:producer>
	</cewolf:data>
</cewolf:chart>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr> 
  <td>
    <%--<table border="0">
      <tr> 
        <td>
          <span class="headlineStyle"> <fmt:message key='sfa.message.intelligenceSummary'/></span>
          <span class="smallTitleBoldStyle">&gt; <a href="javascript: workInProgress();"><fmt:message key='sfa.message.gotoallreports'/></a></span>
        </td>
      </tr>
    </table>--%>
    <table width="100%" border="0" cellpadding="5" cellspacing="0">
      <tr valign="top"> 
        <td colspan="2"><span class="sfaRowLabel"><%=DateUtil.formatDate("MMMM", nowdate)%> <fmt:message key='sfa.message.salesOpportunities'/></span> <br>
          <table width="100%" border="1" cellspacing="0" cellpadding="5">
            <tr> 
              <td rowspan="4"><cewolf:img chartid="line" renderer="cewolf" width="200" height="156"/></td>
              <td valign="top"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.monthlyProjection'/> (<%=csign.getCurrencySymbol()%>)</span><br>
                  <span class="smallTitleStyle"><%= numFormat.format(currMonthProj) %></span></div></td>
            </tr>
            <tr> 
              <td valign="top"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.currentMonthOpportunities'/> (<%=csign.getCurrencySymbol()%>)</span><br>
                  <span class="smallTitleStyle"><a href="listing_Opportunity_All.jsp?fromDate=<%=currMonthFrom%>&toDate=<%=currMonthTo%>"><%= numFormat.format(currMonthOpp) %></a></span></div></td>
            </tr>
            <tr> 
              <td valign="top"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.monthlyWeighted'/> <br><fmt:message key='sfa.message.opportunities'/> (<%=csign.getCurrencySymbol()%>)</span><br>
                  <span class="smallTitleStyle"><%= numFormat.format(currMonthWeightedOpp) %></span></div></td>
            </tr>
            <tr> 
              <td valign="top"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.totalMonthlySales'/> (<%=csign.getCurrencySymbol()%>)</span><br>
                  <span class="smallTitleStyle"><a href="listing_ClosedSale_All.jsp?fromDate=<%=currMonthFrom%>&toDate=<%=currMonthTo%>"><%= numFormat.format(currMonthSales) %></a> [<%= opp.calculatePercentage(currMonthSales, currMonthProj) %>%]</span></div></td>
            </tr>
          </table>
          
        </td>
        <td width="50%"><span class="sfaRowLabel"><fmt:message key='sfa.message.yearToDateSummary'/></span> <br>
          <table width="100%" border="1" cellspacing="0" cellpadding="5">
            <tr bgcolor="#B7CCE3"> 
              <td width="33%" valign="top">&nbsp;</td>
              <td width="33%" valign="top"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.currentQuarter'/> (<%=csign.getCurrencySymbol()%>)</span></div></td>
              <td width="34%" valign="top"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.yearly'/> (<%=csign.getCurrencySymbol()%>)</span></div></td>
            </tr>
            <tr> 
              <td width="33%" valign="top"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.projections'/></span></div></td>
              <td width="33%" valign="top"><div align="center"><span class="smallTitleStyle"><%= numFormat.format(currQuarterProj) %></span></td>
              <td width="34%" valign="top"><div align="center"><span class="smallTitleStyle"><%= numFormat.format(currAnnualProj) %></span></td>
            </tr>
            <tr> 
              <td width="33%" valign="top"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.opportunities'/></span></div></td>
              <td width="33%" valign="top"><div align="center"><span class="smallTitleStyle"><a href="listing_Opportunity_All.jsp?fromDate=<%=currQuarterFrom%>&toDate=<%=currQuarterTo%>"><%= numFormat.format(currQuarterOpp) %></a></span></td>
              <td width="34%" valign="top"><div align="center"><span class="smallTitleStyle"><a href="listing_Opportunity_All.jsp?fromDate=<%=currYearFrom%>&toDate=<%=currYearTo%>"><%= numFormat.format(currAnnualOpp) %></a></span></td>
            </tr>
            <tr> 
              <td width="33%" valign="top"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.weighted'/><br><fmt:message key='sfa.message.opportunities'/></span></div></td>
              <td width="33%" valign="top"><div align="center"><span class="smallTitleStyle"><%= numFormat.format(currQuarterWeightedOpp) %></span></td>
              <td width="34%" valign="top"><div align="center"><span class="smallTitleStyle"><%= numFormat.format(currAnnualWeightedOpp) %></span></td>
            </tr>
            <tr> 
              <td width="33%" valign="top"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.salesToDate'/></span></div></td>
              <td width="33%" valign="top"><div align="center"><span class="smallTitleStyle"><a href="listing_ClosedSale_All.jsp?fromDate=<%=currQuarterFrom%>&toDate=<%=currQuarterTo%>"><%= numFormat.format(currQuarterSales) %></a> [<%= opp.calculatePercentage(currQuarterSales, currQuarterProj) %>%]</span></div></td>
              <td width="34%" valign="top"><div align="center"><span class="smallTitleStyle"><a href="listing_ClosedSale_All.jsp?fromDate=<%=currYearFrom%>&toDate=<%=currYearTo%>"><%= numFormat.format(currAnnualSales) %></a> [<%= opp.calculatePercentage(currAnnualSales, currAnnualProj) %>%]</span></div></td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
    <br>
  </td>
</tr>
</table>

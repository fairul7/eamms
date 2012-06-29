<%@ page import="kacang.*, java.util.*, java.text.*,
                 kacang.util.Log" %>
<%@ page import="com.tms.crm.sales.model.*, com.tms.crm.sales.misc.*" %>
<%@ include file="/common/header.jsp" %>
<%
		Date         nowdate   = (Date) request.getAttribute("nowdate");
		OPPModule    opp       = (OPPModule) request.getAttribute("opp");
		NumberFormat numFormat = (NumberFormat) request.getAttribute("numFormat");
		
		int nowyear = DateUtil.getYear(nowdate);
		int performanceYear = nowyear;
        Date[]  monthDateArr = new Date[12];
        double[] monthlySales = new double[12];
        int[]   monthlyProj  = new int[12];

		try {
			String performance_year = request.getParameter("performance_year");
			if (performance_year != null) {
				performanceYear = Integer.parseInt(performance_year);
				session.setAttribute("performance_year", performance_year);
			} else if (session.getAttribute("performance_year") != null) {
				performance_year = (String) session.getAttribute("performance_year");
				performanceYear = Integer.parseInt(performance_year);
			}
            for (int i=0; i<12; i++) {
                monthDateArr[i] = DateUtil.getDate(performanceYear, i, 1);
                monthlySales[i] = opp.calculateCurrMonthSales(monthDateArr[i], "");
                monthlyProj[i]  = opp.calculateCurrMonthProj(monthDateArr[i], "");
            }
		} catch (Exception e) {
			Log.getLog(OPPModule.class).error("Error retrieving sales performance", e);
		}
%>

		<%
			// Getting the financial setting : currency symbols
			OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
			Opportunity csign = om.getFinancilSetting();

		%>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
  <td>
    <table border="0">
      <tr>
        <td>
          <span class="sfaRowLabel"><fmt:message key='sfa.message.performance'/></span>
          <span class="smallTitleBoldStyle">&gt; <%= performanceYear %> &gt;
              <input type="button" class="button" value="<fmt:message key='sfa.message.previous'/>" onClick="location = '?performance_year=<%=(performanceYear - 1)%>' "  />
              <input type="button" class="button" value="<fmt:message key='sfa.message.now'/>" onClick="location = '?performance_year=<%=nowyear%>' "  />
              <input type="button" class="button" value="<fmt:message key='sfa.message.next'/>" onClick="location = '?performance_year=<%=(performanceYear + 1)%>' "  />
            </span>
        </td>
      </tr>
    </table>
    <table width="100%" border="1" cellspacing="0" cellpadding="5">
      <tr>
        <td><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.month'/></span></td>
        <td width="14%"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='general.label.jan'/></span></div></td>
        <td width="14%"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='general.label.feb'/></span></div></td>
        <td width="14%"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='general.label.mar'/></span></div></td>
        <td width="14%"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='general.label.apr'/></span></div></td>
        <td width="14%"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='general.label.may'/></span></div></td>
        <td width="14%"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='general.label.jun'/></span></div></td>
      </tr>
      <tr>
        <td><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.value'/> (<%=csign.getCurrencySymbol()%>)</span></td>

        <% for (int i=0; i<6; i++) { %>
        	<% Date fromDate = monthDateArr[i]; %>
        	<% Date toDate   = DateUtil.dateAdd(DateUtil.dateAdd(fromDate, Calendar.MONTH, 1), Calendar.DATE, -1); %>
        	<% String fromDateParam = DateUtil.getDateString(fromDate); %>
        	<% String toDateParam   = DateUtil.getDateString(toDate); %>
        <td width="14%"><div align="center"><span class="smallTitleStyle"><a href="listing_ClosedSale_All.jsp?fromDate=<%=fromDateParam%>&toDate=<%=toDateParam%>"><%= numFormat.format(monthlySales[i]) %></a><br>
            <%= opp.calculatePercentage(monthlySales[i], monthlyProj[i]) %>% </span></div></td>
        <% } %>

      </tr>
      <tr>
        <td><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.month'/></span></td>
        <td width="14%"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='general.label.jul'/></span></div></td>
        <td width="14%"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='general.label.aug'/></span></div></td>
        <td width="14%"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='general.label.sep'/></span></div></td>
        <td width="14%"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='general.label.oct'/></span></div></td>
        <td width="14%"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='general.label.nov'/></span></div></td>
        <td width="14%"><div align="center"><span class="smallTitleBoldStyle"><fmt:message key='general.label.dec'/></span></div></td>
      </tr>
      <tr>
        <td><span class="smallTitleBoldStyle"><fmt:message key='sfa.message.value'/> (<%=csign.getCurrencySymbol()%>)</span></td>

        <% for (int i=6; i<12; i++) { %>
        	<% Date fromDate = monthDateArr[i]; %>
        	<% Date toDate   = DateUtil.dateAdd(DateUtil.dateAdd(fromDate, Calendar.MONTH, 1), Calendar.DATE, -1); %>
        	<% String fromDateParam = DateUtil.getDateString(fromDate); %>
        	<% String toDateParam   = DateUtil.getDateString(toDate); %>
        <td width="14%"><div align="center"><span class="smallTitleStyle"><a href="listing_ClosedSale_All.jsp?fromDate=<%=fromDateParam%>&toDate=<%=toDateParam%>"><%= numFormat.format(monthlySales[i]) %></a><br>
            <%= opp.calculatePercentage(monthlySales[i], monthlyProj[i]) %>% </span></div></td>
        <% } %>

      </tr>
    </table>
  </td>
</tr>
</table>

<%@taglib uri='/WEB-INF/cewolf.tld' prefix='cewolf' %>
<%@ page import="kacang.Application,
				 java.text.NumberFormat,
				 com.tms.crm.sales.model.*,
				 java.text.DateFormatSymbols,
				 com.tms.crm.sales.misc.DateUtil,
				 java.util.*,
				 java.text.SimpleDateFormat,
				 java.text.DecimalFormat"%>
<%@ include file="/common/header.jsp" %>
<head>
	<style>
	.report{
		font-family: Arial, Helvetica, sans-serif; font-size:12 px;
	}
	</style>


	<style type="text/css">
	div.noPrint {display: none;}
	tr.breakhere {page-break-after: always}
	</style>
</head>


<body onLoad="window.print();">

	<c:set var="usersId" value="${widgets['salesReportIndv.reportIndv'].userIdList}" />
	<c:set var="financialYearStr" value="${widgets['salesReportIndv.reportIndv'].financialYearStr}"/>

	<table width="100%" border="0" cellspacing="0" bgcolor="#EAF6FF">
	<tr>
        <td class="report" height="25" bgcolor="#BDD5F3" colspan="2" >
        <b><fmt:message key="sfa.message.completedSaleReportIndividual"/></b>
        </td>
    </tr>
	<%
		OpportunityModule getEndingMonth = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
		Opportunity currentMonth  = getEndingMonth.getFinancilSetting();
		int endingMonth = Integer.parseInt(currentMonth.getYearEnds());
		String monthName = getEndingMonth.getMonthsText(endingMonth);
		pageContext.setAttribute("monthName",monthName);
	%>

	<tr>
        <td class="report" height="25" bgcolor="#BDD5F3" colspan="2" >
        <b><fmt:message key="sfa.message.financialYear"/>: <c:out value="${monthName}"/> <c:out value="${financialYearStr}"/></b>
        </td>
    </tr>

	<%
		int pageBreak = 0;
	%>

	<c:forEach var="userId" items="${usersId}">
	<%
		String userId = (String)pageContext.getAttribute("userId");
        AccountManagerModule am = (AccountManagerModule) Application.getInstance().getModule(AccountManagerModule.class);
        AccountManager accountManager = am.getAccountManager(userId);

		if (accountManager == null) {
			OpportunityModule getDeletedUser = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
			Opportunity dun  = getDeletedUser.getDeletedUsername(userId);
			String temp = dun.getUsername();
			dun.setFullName(temp);
			pageContext.setAttribute("user",dun);
		} else {
			pageContext.setAttribute("user",accountManager);
		}

        OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
    %>
	<%
		OpportunityModule grabSetting = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
		Opportunity setting  = grabSetting.getFinancilSetting(); // Grabbing the financial setting
		int monthEnd = Integer.parseInt(setting.getYearEnds());
		Object val = pageContext.getAttribute("financialYearStr");

		int myYear = Integer.parseInt(val.toString());
		int myMonth = monthEnd;
		Date myYearEnd = DateUtil.getDate(myYear, myMonth, 1);
		Date myYearStart = DateUtil.dateAdd(myYearEnd, Calendar.YEAR, -1);
		myYearStart = DateUtil.dateAdd(myYearStart, Calendar.MONTH, 1);
	%>
	<tr>
    	<td class="report" height="30" colspan="2">
        	<b><fmt:message key="sfa.label.accountManager"/>: </b> &nbsp;&nbsp;<c:out value="${user.fullName}"/>
        </td>
    </tr>

	<tr>
		<td width="50%">
			<table border="0" cellpadding="2" cellspacing="2" width="70%" align="center">
				<tr bgcolor="#BDD5F3">
					<td colspan="3" class="report" align="center"><b><fmt:message key="sfa.message.monthlySales"/></b></td>
				</tr>

				<tr bgcolor="#BDD5F3">
					<td class="report" align="center"><b><fmt:message key="sfa.message.month"/></b></td>
					<td class="report" align="center"><b><fmt:message key="sfa.message.projection"/> (<%=setting.getCurrencySymbol()%>)</b></td>
					<td class="report" align="center"><b><fmt:message key="sfa.label.sales"/> (<%=setting.getCurrencySymbol()%>)</b></td>
				</tr>

				<%--		this row will be in loop		--%>
				<%
					SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

					SimpleDateFormat sdf3 = new SimpleDateFormat("MMM");

					Date date = myYearStart;
					Opportunity opp = new Opportunity();
					Opportunity sales = new Opportunity();
					int pq1=0, pq2=0, pq3=0, pq4=0, pqTotal=0;
					double sq1=0, sq2=0, sq3=0, sq4=0, sqTotal=0;
					int projectionTotal=0;
					double salesTotal=0;

					for (int i=0 ; i < 12 ; i++) {
						pageContext.setAttribute("monthName", sdf.format(date));
						pageContext.setAttribute("monthParam", sdf2.format(date));
						pageContext.setAttribute("monthParam", sdf2.format(date));
						int month = DateUtil.getMonth(date);
						String temp = "month" + (month + 1);
						opp = om.getMonthlyProjection(temp,userId,String.valueOf(DateUtil.getYear(date)));
						sales = om.getMonthlySales(String.valueOf(month + 1),userId,String.valueOf(DateUtil.getYear(date)));

						if (opp !=null) {
							pageContext.setAttribute("projection", om.addDecimal(Double.parseDouble(String.valueOf(opp.getMonthlyProjection()))));
							projectionTotal = projectionTotal + Integer.parseInt(opp.getMonthlyProjection());

						} else if (opp == null) {
							pageContext.setAttribute("projection", "0.00");
						}

						if (sales != null)	{
							pageContext.setAttribute("sales", om.addDecimal(Double.parseDouble(String.valueOf(sales.getOpportunityValue()))));
							salesTotal = salesTotal + sales.getOpportunityValue();
						} else if (sales == null ) {
							pageContext.setAttribute("sales", "0.00");
						}

						// For quarterly Projection
						if (i >=0 && i<=2 ) {
							if (opp !=null) {
								int var = Integer.parseInt(opp.getMonthlyProjection());
								pq1 = pq1 + var;
							}
						} else if (i >=3 && i<=5 ) {
							if (opp !=null) {
								int var = Integer.parseInt(opp.getMonthlyProjection());
								pq2 = pq2 + var;
							}
						} else if (i>=6 && i<=8) {
							if (opp !=null) {
								int var = Integer.parseInt(opp.getMonthlyProjection());
								pq3 = pq3 + var;
							}
						} else if (i>=9 && i<=11) {
							if (opp !=null) {
								int var = Integer.parseInt(opp.getMonthlyProjection());
								pq4 = pq4 + var;
							}
						}

						// For quarterly sales
						if (i >=0 && i<=2 ) {
							if (sales !=null) {
								double var = sales.getOpportunityValue();
								sq1 = sq1 + var;
							}
						} else if (i >=3 && i<=5 ) {
							if (sales !=null) {
								double var = sales.getOpportunityValue();
								sq2 = sq2 + var;
							}
						} else if (i>=6 && i<=8) {
							if (sales !=null) {
								double var = sales.getOpportunityValue();
								sq3 = sq3 + var;
							}
						} else if (i>=9 && i<=11) {
							if (sales !=null) {
								double var = sales.getOpportunityValue();
								sq4 = sq4 + var;
							}
						}
						pageContext.setAttribute("pq1",om.addDecimal(pq1));
						pageContext.setAttribute("pq2",om.addDecimal(pq2));
						pageContext.setAttribute("pq3",om.addDecimal(pq3));
						pageContext.setAttribute("pq4",om.addDecimal(pq4));

						pageContext.setAttribute("sq1",om.addDecimal(Double.parseDouble(String.valueOf(sq1))));
						pageContext.setAttribute("sq2",om.addDecimal(Double.parseDouble(String.valueOf(sq2))));
						pageContext.setAttribute("sq3",om.addDecimal(Double.parseDouble(String.valueOf(sq3))));
						pageContext.setAttribute("sq4",om.addDecimal(Double.parseDouble(String.valueOf(sq4))));
						pageContext.setAttribute("projectionTotal",String.valueOf(om.addDecimal(Double.parseDouble(String.valueOf(projectionTotal)))));
						pageContext.setAttribute("salesTotal",om.addDecimal(salesTotal));
						date = DateUtil.dateAdd(date, Calendar.MONTH, 1);

				%>

				<tr>
					<td class="report" align="right" bgcolor="#E4EDFC"><c:out value="${monthName}"/></td>
					<td class="report" align="right" bgcolor="#E4EDFC"><c:out value="${projection}"/></td>
					<td class="report" align="right" bgcolor="#E4EDFC"><c:out value="${sales}"/></td>
				</tr>
				<%
					}
				%>
				<%
					pqTotal = pq1 + pq2 + pq3 + pq4 ;
					sqTotal = sq1 + sq2 + sq3 + sq4 ;
					pageContext.setAttribute("pqTotal",om.addDecimal(Double.parseDouble(String.valueOf(pqTotal))));
					pageContext.setAttribute("sqTotal",om.addDecimal(Double.parseDouble(String.valueOf(sqTotal))));
				%>

				<tr>
					<td class="report" align="right" bgcolor="#E4EDFC"><b><fmt:message key="sfa.message.total"/></b></td>
					<td class="report" align="right" bgcolor="#E4EDFC"><b><c:out value="${projectionTotal}"/></b></td>
					<td class="report" align="right" bgcolor="#E4EDFC"><b><c:out value="${salesTotal}"/></b></td>
				</tr>

			</table>
		</td>

		<%
			Date qMonth = myYearStart;
			Date q1From = qMonth;
			Date q2From = DateUtil.dateAdd(qMonth, Calendar.MONTH, 3);
			Date q3From = DateUtil.dateAdd(qMonth, Calendar.MONTH, 6);
			Date q4From = DateUtil.dateAdd(qMonth, Calendar.MONTH, 9);

			Date q1To = DateUtil.dateAdd(q1From, Calendar.MONTH, 2);
			Date q2To = DateUtil.dateAdd(q2From, Calendar.MONTH, 2);
			Date q3To = DateUtil.dateAdd(q3From, Calendar.MONTH, 2);
			Date q4To = DateUtil.dateAdd(q4From, Calendar.MONTH, 2);
		%>

		<td class="report" align="center" valign="top" >
			<table border="0" cellpadding="2" cellspacing="2" width="80%" align="center">

				<tr bgcolor="#BDD5F3">
					<td colspan="3" class="report" align="center"><b><fmt:message key="sfa.message.quarterlySales"/></b></td>
				</tr>

				<tr bgcolor="#BDD5F3">
					<td class="report" align="center"><b><fmt:message key="sfa.message.quarter"/></b></td>
					<td class="report" align="center"><b><fmt:message key="sfa.message.projection"/> (<%=setting.getCurrencySymbol()%>)</b></td>
					<td class="report" align="center"><b><fmt:message key="sfa.label.sales"/> (<%=setting.getCurrencySymbol()%>)</b></td>
				</tr>

				<tr bgcolor="#E4EDFC">
					<td class="report" align="center"><fmt:message key="sfa.message.q1"/> : <%=sdf3.format(q1From)%> - <%=sdf3.format(q1To)%></td>
					<td class="report" align="right"><c:out value="${pq1}"/></td>
					<td class="report" align="right"><c:out value="${sq1}"/></td>
				</tr>

				<tr bgcolor="#E4EDFC">
					<td class="report" align="center"><fmt:message key="sfa.message.q2"/> : <%=sdf3.format(q2From)%> - <%=sdf3.format(q2To)%></td>
					<td class="report" align="right"><c:out value="${pq2}"/></td>
					<td class="report" align="right"><c:out value="${sq2}"/></td>
				</tr>

				<tr bgcolor="#E4EDFC">
					<td class="report" align="center"><fmt:message key="sfa.message.q3"/> : <%=sdf3.format(q3From)%> - <%=sdf3.format(q3To)%></td>
					<td class="report" align="right"><c:out value="${pq3}"/></td>
					<td class="report" align="right"><c:out value="${sq3}"/></td>
				</tr>

				<tr bgcolor="#E4EDFC">
					<td class="report" align="center"><fmt:message key="sfa.message.q4"/> : <%=sdf3.format(q4From)%> - <%=sdf3.format(q4To)%></td>
					<td class="report" align="right"><c:out value="${pq4}"/></td>
					<td class="report" align="right"><c:out value="${sq4}"/></td>
				</tr>

				<tr bgcolor="#E4EDFC">
					<td class="report" align="center"><b><fmt:message key="sfa.message.total"/></b></td>
					<td class="report" align="right"><b><c:out value="${pqTotal}"/></b></td>
					<td class="report" align="right"><b><c:out value="${sqTotal}"/></b></td>
				</tr>

			</table>

			<jsp:useBean id="pageViews" class="com.tms.crm.sales.ui.ReportChart"/>
				<cewolf:chart id="line" type="horizontalbar" showlegend="true">
				<cewolf:colorpaint color="#FFFFFF"/>
				<cewolf:data>
				<cewolf:producer id="pageViews">
				<cewolf:param name="value1" value="<%= new Float(sqTotal) %>"/>
				<cewolf:param name="value2" value="<%= new Float(pqTotal) %>"/>
				</cewolf:producer>
				</cewolf:data>
			</cewolf:chart>


			<br>
			<table border="0" width="80%">
			<tr bgcolor="#E4EDFC">
				<td width="40%" ><cewolf:img chartid="line" renderer="cewolf" width="200" height="156"/></td>
				<td class="report" valign="top">
					<table width="100%" border="0">
					<%
						String ts;
						String tp;
						ts = (String) pageContext.getAttribute("salesTotal");
						tp = (String) pageContext.getAttribute("projectionTotal");
						double salesInPercentage = ((Double.parseDouble(ts) / Double.parseDouble(tp)) * 100) ;
						DecimalFormat df = new DecimalFormat("00");
						String csign = setting.getCurrencySymbol();
					%>

						<tr>
							<td height="60" class="report"><b><%=csign%> <%=ts%><br><font size="1"><%=df.format(salesInPercentage)%> % of Projection</font></b></td>
						</tr>

						<tr>
							<td height="60" class="report"><b><%=csign%> <%=tp%></b></td>
						</tr>
					</table>

				</td>
			</tr>
			</table>

		</td>
		</tr>
		<tr><td colspan="2"><hr></td></tr>

		<%
		pageBreak = pageBreak+1;
			if (pageBreak%2 == 0 ) {
		%>
		<tr class="breakhere"></tr>
		<%
			pageBreak = 0;
			}
		%>
		</c:forEach>
	</table>
	</body>


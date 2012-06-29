<%@ page import="com.tms.crm.sales.model.CompanyModule,
                 kacang.Application,
                 com.tms.crm.sales.model.Company,
                 com.tms.crm.sales.model.OpportunityModule,
                 java.util.Date,
                 java.util.Collection,
                 java.util.Map,
                 com.tms.crm.sales.model.Opportunity,
                 java.text.NumberFormat,
                 java.util.HashMap"%>
<%@ include file="/common/header.jsp" %>


<style>
.report{
	font-family: Arial, Helvetica, sans-serif; font-size:12 px;
}

</style>

	<c:set var="toDate" value="${widgets['jsp_reportbycompany.form1'].toDate}" />
	<c:set var="fromDate" value="${widgets['jsp_reportbycompany.form1'].fromDate}" />
	<c:set var="companiesId" value="${widgets['jsp_reportbycompany.form1'].companiesIdList}" />

	<c:set var="startFromDate" value="${widgets['jsp_reportbycompany.form1'].startFromDate}" />
	<c:set var="startToDate" value="${widgets['jsp_reportbycompany.form1'].startToDate}" />
	<c:set var="startBool" value="${widgets['jsp_reportbycompany.form1'].startBool}"/>
	<c:set var="closeBool" value="${widgets['jsp_reportbycompany.form1'].closeBool}"/>


<table width="100%" border="0" cellspacing="0" bgcolor="#EAF6FF" >

    <tr>
        <td class="report" height="25" bgcolor="#BDD5F3" >
        <b><fmt:message key='sfa.message.sALESOPPORTUNITYREPORT'/> - <fmt:message key='sfa.message.byCompany'/></b>
        </td>

    </tr>

    <%--<tr>
    <td class="report" height="25" >
       <b><fmt:message key='sfa.message.byCompany'/></b>
    </td>
    </tr>--%>

    <c:if test="${closeBool && !startBool}">
	<tr>
    <td class="report" height="25" >
        <b><fmt:message key="sfa.message.closeDate"/>: </b><fmt:formatDate value="${fromDate}" pattern="${globalDateLong}"/> <fmt:message key='sfa.message.to'/> <fmt:formatDate value="${toDate}" pattern="${globalDateLong}"/>
    </td>
    </tr>
	</c:if>

	<c:if test="${startBool && !closeBool}">
	<tr>
    <td class="report" height="25" >
        <b><fmt:message key="sfa.message.startDate"/>: </b><fmt:formatDate value="${startFromDate}" pattern="${globalDateLong}"/> <fmt:message key='sfa.message.to'/> <fmt:formatDate value="${startToDate}" pattern="${globalDateLong}"/>
    </td>
    </tr>
	</c:if>

	<c:if test="${startBool && closeBool}">
	<tr>
    <td class="report" height="25" >
        <b><fmt:message key="sfa.message.startDate"/>: </b><fmt:formatDate value="${startFromDate}" pattern="${globalDateLong}"/> <fmt:message key='sfa.message.to'/> <fmt:formatDate value="${startToDate}" pattern="${globalDateLong}"/>
    </td>
    </tr>
	<tr>
    <td class="report" height="25" >
        <b><fmt:message key="sfa.message.closeDate"/>: </b><fmt:formatDate value="${fromDate}" pattern="${globalDateLong}"/> <fmt:message key='sfa.message.to'/> <fmt:formatDate value="${toDate}" pattern="${globalDateLong}"/>
    </td>
    </tr>
	</c:if>

    <%

	    NumberFormat numFormat = NumberFormat.getIntegerInstance();
        Date fromDate = (Date) pageContext.getAttribute("fromDate");
        Date toDate = (Date) pageContext.getAttribute("toDate");
		Date startFromDate = (Date) pageContext.getAttribute("startFromDate");
		Date startToDate = (Date) pageContext.getAttribute("startToDate");
		Boolean startBool = (Boolean) pageContext.getAttribute("startBool");
		Boolean closeBool = (Boolean) pageContext.getAttribute("closeBool");
        long sumTotalValue = 0;
        long sumTotalAdValue = 0;
    %>

    <c:forEach var="companyId" items="${companiesId}" >
     <%
		 String companyId  = (String)pageContext.getAttribute("companyId");
         CompanyModule cm = (CompanyModule) Application.getInstance().getModule(CompanyModule.class);
         Company company = cm.getCompany(companyId);
         pageContext.setAttribute("company",company);
         OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
		 Collection col = null;

		 if (startBool.booleanValue() && !closeBool.booleanValue() )	{
			 col = om.listOpportunities(null,companyId,null,null,1,false,false,null,null,startFromDate,startToDate, "companyName",false,0,-1);
		 } else if (closeBool.booleanValue() && !startBool.booleanValue())	{
			col = om.listOpportunities(null,companyId,null,null,1,false,false,fromDate,toDate,null,null, "companyName",false,0,-1);
		 } else if (startBool.booleanValue() && closeBool.booleanValue()) {
			col = om.listOpportunities(null,companyId,null,null,1,false,false,fromDate,toDate,startFromDate,startToDate, "companyName",false,0,-1);
		 }
         pageContext.setAttribute("col",col);
         Map stageMap = Opportunity.getOpportunityStage_Less_Map();
         if(col.size()>0){
     %>

	 <%
		// Getting the financial setting : currency symbols
		Opportunity csign = om.getFinancilSetting();
	 %>

     <tr>
        <td class="report" height="30">
        <br>
            <b><fmt:message key='sfa.message.company'/>:</b> &nbsp;&nbsp;<c:out value="${company.companyName}" />
        </td>
     </tr>

     <tr>
        <td class="report" >
		<table width="100%"> <tr> <td> &nbsp;&nbsp;&nbsp;&nbsp; </td><td>
            <table width="100%" border="0" cellspacing="1" cellpadding="2">
             <tr bgcolor="#BDD5F3">

                <c:if test="${!closeBool && startBool}">
					<td class="report" align="center" height="25" ><b><fmt:message key='sfa.message.startdate'/></b></td>
				</c:if>

				<c:if test="${closeBool && !startBool}">
                	<td class="report" align="center" height="25" ><b><fmt:message key='sfa.message.closingDate'/></b></td>
				</c:if>

				<c:if test="${startBool && closeBool}">
					<td class="report" align="center" height="25" ><b><fmt:message key='sfa.message.startdate'/></b></td>
					<td class="report" align="center" height="25" ><b><fmt:message key='sfa.message.closingDate'/></b></td>
				</c:if>

                <td class="report" width="25%" align="center"><b><fmt:message key='sfa.message.opportunity'/></b></td>
                <td class="report" align="center"><b><fmt:message key='sfa.message.stage'/></b></td>
                <td class="report" align="center"><b><fmt:message key='sfa.message.totalValue'/> (<%=csign.getCurrencySymbol()%>)</b></td>
                <td class="report" align="center"><b><fmt:message key='sfa.message.wOpportunity'/> (<%=csign.getCurrencySymbol()%>)</b></td>
                <td class="report" align="center"><b><fmt:message key='sfa.message.lastUpdated'/></b></td>
             </tr>
             <%
                long totalValue = 0;
                long totalAdValue = 0;
             %>
             <c:forEach var="opportunity" items="${col}">

              <tr bgcolor="#E4EDFC">

				<c:if test="${!closeBool && startBool}">
			  		<td class="report"  valign="top" align="center"><fmt:formatDate value="${opportunity.opportunityStart}" pattern="${globalDateLong}"  /> </td>
				</c:if>

				<c:if test="${closeBool && !startBool}">
			  		<td class="report"  valign="top" align="center"><fmt:formatDate value="${opportunity.opportunityEnd}" pattern="${globalDateLong}"  /> </td>
				</c:if>

				<c:if test="${closeBool && startBool}">
					<td class="report"  valign="top" align="center"><fmt:formatDate value="${opportunity.opportunityStart}" pattern="${globalDateLong}"  /> </td>
					<td class="report"  valign="top" align="center"><fmt:formatDate value="${opportunity.opportunityEnd}" pattern="${globalDateLong}"  /> </td>
				</c:if>
<%--
                <td class="report" ><fmt:message key='sfa.message.type'/></td>
--%>
                <td class="report"  valign="top" align="center" ><c:out value="${opportunity.opportunityName}" /></td>
                <td class="report"  valign="top" align="center" >
                <%
                    Opportunity opp = (Opportunity) pageContext.getAttribute("opportunity");
                %>
                <%=stageMap.get(opp.getOpportunityStage())%>
                <%--<c:out value="${stageMap['opportunity.opportunityStage']}" />--%></td>

                <td class="report"  valign="top" align="right" ><%=numFormat.format(opp.getOpportunityValue())%></td>
                <td class="report"  valign="top" align="right" ><%=numFormat.format(opp.getAdjustedValue())%></td>
                <td class="report"  valign="top" align="center" ><fmt:formatDate value="${opportunity.modifiedDate}" pattern="${globalDateLong}" /></td>
                <%
                    totalValue += opp.getOpportunityValue();
                    totalAdValue += opp.getAdjustedValue();

                %>
              </tr>

             </c:forEach>
                   <tr>
                    <td class="report" colspan="2">&nbsp;</td>
                    <td class="report"  align="right">
                     <b><fmt:message key='sfa.message.total'/></b>&nbsp;&nbsp;&nbsp;
                    </td>
                    <td class="report" align="right" >
                        <%= numFormat.format(totalValue)%>
                    </td>
                    <td class="report" align="right" >
                        <%=numFormat.format(totalAdValue)%>
                    </td>
                    <td class="report">&nbsp;</td>
                   </tr>


            </table></td></tr></table>
        </td>
     </tr>
         <%
            sumTotalAdValue +=  totalAdValue;
            sumTotalValue +=  totalValue;
        }

         %>
    </c:forEach>
    <Tr>
       <td class="report" >&nbsp;</td>
    </tr>

<%--
    <Tr>
       <td class="report" >&nbsp;</td>
    </tr>
--%>
	<%
	 	OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
		Opportunity csign = om.getFinancilSetting();
	 %>

    <Tr>
       <td class="report" ><b><fmt:message key='sfa.message.summaryTotals'/></b></td>
    </tr>
    <tr>
        <td class="report" >
            <table>
                <tr>
                    <td class="report"  width="30%"> <b><fmt:message key='sfa.message.oppoturnities'/></b></td>
                    <td class="report"> <%=csign.getCurrencySymbol()%> <%=numFormat.format(sumTotalValue)%></td>
                </tr>
                <tr>
                    <td class="report"  width="30%"> <b><fmt:message key='sfa.message.weightedOppoturnities'/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
                    <td class="report"> <%=csign.getCurrencySymbol()%> <%=numFormat.format(sumTotalAdValue)%></td>
                </tr> </table>
        </td>
    </tr>

</table>




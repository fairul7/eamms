<%@ page import="com.tms.crm.sales.model.CompanyModule,
                 kacang.Application,
                 com.tms.crm.sales.model.Company,
                 com.tms.crm.sales.model.OpportunityModule,
                 java.util.Date,
                 java.util.Collection,
                 java.util.Map,
                 com.tms.crm.sales.model.Opportunity,
                 java.text.NumberFormat"%>
<%@ include file="/common/header.jsp" %>

<style>
.report{
	font-family: Arial, Helvetica, sans-serif; font-size:12 px;
}
</style>


<c:set var="toDate" value="${widgets['jsp_reportlostsales.form1'].toDate}" />
<c:set var="fromDate" value="${widgets['jsp_reportlostsales.form1'].fromDate}" />

<table width="100%" border="0" cellspacing="0" bgcolor="#EAF6FF">
    <tr>
        <td class="report" height="25" bgcolor="#BDD5F3" colspan="2">
        <b><fmt:message key='sfa.message.lOSTOPPORTUNITYREPORT'/></b>
        </td>
    </tr>

    <tr>
    <td class="report" height="25">
	<br>
        <b><fmt:message key='sfa.message.from'/> <fmt:formatDate value="${fromDate}" pattern="${globalDateLong}"/> <fmt:message key='sfa.message.to'/> <fmt:formatDate value="${toDate}" pattern="${globalDateLong}"/></b><br><br>
    </td>
    </tr>
    <%

	    NumberFormat numFormat = NumberFormat.getIntegerInstance();
        Date fromDate = (Date) pageContext.getAttribute("fromDate");
        Date toDate = (Date) pageContext.getAttribute("toDate");
        long sumTotalValue = 0;
        long sumTotalAdValue = 0;
    %>

     <%
        OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
        Collection col = om.listLostOpportunities(null,null,null,null,3,false,false,fromDate,toDate,"modifiedDate",false,0,-1);
        pageContext.setAttribute("col",col);
        Map stageMap = Opportunity.getOpportunityStage_Less_Map();
        if(col.size()>0){
     %>

	 <%
		// Getting the financial setting : currency symbols
	 	Opportunity csign = om.getFinancilSetting();
	 %>

     <tr>
        <td class="report" >
            <table width="100%" border="0" cellspacing="2" cellpadding="2">
             <tr bgcolor="#BDD5F3">

                <td class="report"  valign="top"><b><fmt:message key='sfa.message.dateLost'/></b></td>
                <td class="report"  valign="top"><b><fmt:message key='sfa.message.acctMgr'/></b> </td>
                <td class="report"  valign="top" width="20%"><b><fmt:message key='sfa.message.opportunity'/></b></td>
                <td class="report"  valign="top" width="25%"><b><fmt:message key='sfa.message.companyOrCustomer'/> </b></td>
                <td class="report"  valign="top"><b><fmt:message key='sfa.message.totalValue'/> (<%=csign.getCurrencySymbol()%>)</b></td>
                <td class="report"  valign="top"><b><fmt:message key='sfa.message.remarks'/></b></td>
             </tr>
             <%
                long totalValue = 0;
                long totalAdValue = 0;
             %>
             <c:forEach var="opportunity" items="${col}">

              <tr bgcolor="#E4EDFC">
                <td class="report"  valign="top"><fmt:formatDate value="${opportunity.modifiedDate}" pattern="${globalDateLong}"  /> </td>
                <td class="report"  valign="top">
                 <c:out value="${opportunity.accountManagers}" />
                </td>

                <td class="report"  valign="top"><c:out value="${opportunity.opportunityName}" /></td>
                <%
                    Opportunity opp = (Opportunity) pageContext.getAttribute("opportunity");
                %>
                <td class="report"  valign="top"><c:out value="${opportunity.propertyMap['companyName']}" /></td>
                <td class="report"  valign="top"><%=numFormat.format(opp.getOpportunityValue())%></td>
                <td class="report"  valign="top"><c:out value="${opportunity.opportunityLastRemarks}" />&nbsp;</td>
                <%
                    totalValue += opp.getOpportunityValue();
                    totalAdValue += opp.getAdjustedValue();
                %>
              </tr>

             </c:forEach>
             <Tr>
                <td class="report" colspan="6" >&nbsp;</tD>
            </tr>
                   <tr>
                     <td class="report" colspan="3">&nbsp;
                    </td><td class="report"  align="right">
                     <b><fmt:message key='sfa.message.totalLostOpp'/></b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td class="report"><b><%= numFormat.format(totalValue)%></b></td>
                    <td class="report" >&nbsp;</td>
                   </tr>
            </table></td></tr></table>
         <%
            sumTotalAdValue +=  totalAdValue;
            sumTotalValue +=  totalValue;
        }%>
    	<tr>
       		<td class="report" >&nbsp;</td>
    	</tr>


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

	<c:set var="toDate" value="${widgets['jsp_reportcompletedsales.form1'].toDate}" />
	<c:set var="fromDate" value="${widgets['jsp_reportcompletedsales.form1'].fromDate}" />

<table width="100%" border="0" cellspacing="0" bgcolor="#EAF6FF">
    <tr>
        <td class="report" height="25" bgcolor="#BDD5F3" colspan="2" >
        <b><fmt:message key='sfa.message.cOMPLETEDSALESREPORT'/></b>
        </td>
    </tr>

	<tr>
    <td class="report" height="25" >
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
        Collection col = om.listLostOpportunities(null,null,null,null,100,true,false,fromDate,toDate,"modifiedDate",false,0,-1);
        pageContext.setAttribute("col",col);
        Map stageMap = Opportunity.getOpportunityStage_Less_Map();
        if(col.size()>0){
     %>

	 <%
		// Getting the financial setting : currency symbols
	 	Opportunity csign = om.getFinancilSetting();
	 %>

     <tr>
        <td class="report">
            <table width="100%" border="0" cellspacing="2" cellpadding="2">
             <tr bgcolor="#BDD5F3">

                <td class="report"  valign="top"><b><fmt:message key='sfa.message.dateClosed'/></b></td>
	            <td class="report"  valign="top" width="25%"><b><fmt:message key='sfa.message.companyOrCustomer'/> </b></td>
                <td class="report"  valign="top" width="20%"><b><fmt:message key='sfa.message.opportunity'/></b></td>
                <td class="report"  valign="top"><b><fmt:message key='sfa.message.totalValue'/> (<%=csign.getCurrencySymbol()%>)</b></td>
                <td class="report"  valign="top"><b><fmt:message key='sfa.message.referenceNo'/></b></td>
                <td class="report"  valign="top"><b><fmt:message key='sfa.message.acctMgr'/></b> </td>
             </tr>
             <%
                long totalValue = 0;
                long totalAdValue = 0;
             %>
             <c:forEach var="opportunity" items="${col}">
             <%
                    Opportunity opp = (Opportunity) pageContext.getAttribute("opportunity");
                %>
              <tr bgcolor="#E4EDFC">
                <td class="report"  valign="top"><fmt:formatDate value="${opportunity.opportunityEnd}" pattern="${globalDateLong}"  /> </td>
                <td class="report"  valign="top"><c:out value="${opportunity.propertyMap['companyName']}" /></td>
                <td class="report"  valign="top"><c:out value="${opportunity.opportunityName}" /></td>
                <td class="report"  valign="top"><%=numFormat.format(opp.getOpportunityValue())%></td>
                <td class="report"  valign="top"><c:out value="${opportunity.closeReferenceNo}" />&nbsp;</td>
                <td class="report"  valign="top">
                 <c:out value="${opportunity.accountManagers}" />
                </td>
                <%
                    totalValue += opp.getOpportunityValue();
                    totalAdValue += opp.getAdjustedValue();
                %>
              </tr>

			</c:forEach>
                   <tr>
                    <td class="report" colspan=2 >&nbsp;</td>

                    <td class="report"  align="right">
                     <b><fmt:message key='sfa.message.totalSales'/></b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td class="report"><b><%= numFormat.format(totalValue)%></b></td>
                    <td class="report" colspan=2>&nbsp;</td>

                   </tr>

            </table> </td></tr></table>
         <%
            sumTotalAdValue +=  totalAdValue;
            sumTotalValue +=  totalValue;
        }%>
    <Tr>
       <td class="report" >&nbsp;</td>
    </tr>





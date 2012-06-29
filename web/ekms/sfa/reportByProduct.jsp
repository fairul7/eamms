<%@ page import="com.tms.crm.sales.model.ProductModule,
                 kacang.Application,
                 com.tms.crm.sales.model.Product,
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

	<c:set var="toDate" value="${widgets['jsp_reportbyproduct.form1'].toDate}" />
	<c:set var="fromDate" value="${widgets['jsp_reportbyproduct.form1'].fromDate}" />
	<c:set var="productList" value="${widgets['jsp_reportbyproduct.form1'].productIdList}" />

	<c:set var="startFromDate" value="${widgets['jsp_reportbyproduct.form1'].startFromDate}" />
	<c:set var="startToDate" value="${widgets['jsp_reportbyproduct.form1'].startToDate}" />
	<c:set var="startBool" value="${widgets['jsp_reportbyproduct.form1'].startBool}"/>
	<c:set var="closeBool" value="${widgets['jsp_reportbyproduct.form1'].closeBool}"/>


<table width="100%" border="0" cellspacing="0" bgcolor="#EAF6FF">
    <tr>
        <td class="report" height="25" bgcolor="#BDD5F3" colspan="2">
        <b><fmt:message key='sfa.message.sALESOPPORTUNITYREPORT'/> - <fmt:message key='sfa.message.byProduct'/></b>
        </td>
    </tr>

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
		// Setting page context
	    NumberFormat numFormat = NumberFormat.getIntegerInstance();
        Date fromDate = (Date) pageContext.getAttribute("fromDate");
        Date toDate = (Date) pageContext.getAttribute("toDate");
		Date startFromDate = (Date) pageContext.getAttribute("startFromDate");
		Date startToDate = (Date) pageContext.getAttribute("startToDate");
		Boolean startBool = (Boolean) pageContext.getAttribute("startBool");
		Boolean closeBool = (Boolean) pageContext.getAttribute("closeBool");

        long sumTotalValue = 0;
        long sumTotalAdValue = 0;

		OpportunityModule grabSetting = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
		Opportunity setting  = grabSetting.getFinancilSetting();

    %>

    <c:forEach var="productId" items="${productList}" >
     <%
        String productId  = (String)pageContext.getAttribute("productId");
     	ProductModule proModule = (ProductModule) Application.getInstance().getModule(ProductModule.class);
        Product product = proModule.getProduct(productId);
        pageContext.setAttribute("product",product);
		Collection col = null;
		OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
		

		if (startBool.booleanValue() && !closeBool.booleanValue() )	{
			col = om.listOpportunitiesByProducts(productId,null,null, startFromDate, startToDate);
		} else if (closeBool.booleanValue() && !startBool.booleanValue())	{
			col = om.listOpportunitiesByProducts(productId,fromDate,toDate, null, null);
		} else if (startBool.booleanValue() && closeBool.booleanValue()) {
			col = om.listOpportunitiesByProducts(productId,fromDate,toDate, startFromDate, startToDate);
		}

		pageContext.setAttribute("col",col);
        Map stageMap = Opportunity.getOpportunityStage_Less_Map();
        if(col.size()>0){
%>
     <tr>
        <td class="report">
        <br><br>
            <b><fmt:message key='sfa.message.product'/>:</b> &nbsp;&nbsp;<c:out value="${product.productName}" />
        </td>
     </tr>

     <tr>
        <td class="report" > <table width="100%"> <tr> <td> &nbsp;&nbsp;&nbsp;&nbsp; </td><td>
            <table width="100%" border="0" cellspacing="2" cellpadding="2">
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


<%--                <td class="report" align="center" ><b><fmt:message key='sfa.message.closingDate'/></b></td>--%>
                <td class="report"  align="center" width="25%"><b><fmt:message key='sfa.message.opportunity'/></b></td>
                <td class="report" align="center" ><b><fmt:message key='sfa.message.stage'/></b></td>
                <td class="report" align="center" ><b><fmt:message key='sfa.message.totalValue'/> (<%=setting.getCurrencySymbol()%>)</b></td>
                <td class="report" align="center" ><b><fmt:message key='sfa.message.wOpportunity'/> (<%=setting.getCurrencySymbol()%>)</b></td>
                <td class="report" align="center" ><b><fmt:message key='sfa.message.lastUpdated'/></b></td>
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


<%--                <td class="report"  valign="top" align="center" ><fmt:formatDate value="${opportunity.opportunityEnd}" pattern="${globalDateLong}"  /> </td>--%>
                <td class="report"  valign="top" align="center" ><c:out value="${opportunity.opportunityName}" /></td>
                <td class="report"  valign="top" align="center" >
                <%
                    Opportunity opp = (Opportunity) pageContext.getAttribute("opportunity");
                %>
                <%=stageMap.get(opp.getOpportunityStage())%>
                <td class="report"  valign="top" align="right" ><%=numFormat.format(opp.getOpValue())%></td>
                <td class="report"  valign="top" align="right" ><%=numFormat.format(opp.getProductAdjustedValue())%></td>
                <td class="report"  valign="top" align="center" ><fmt:formatDate value="${opportunity.modifiedDate}" pattern="${globalDateLong}" /></td>
                <%
            
                    totalValue += opp.getOpValue();
                    totalAdValue += opp.getAdjustedValue();
                    
                %>
              </tr>
              </c:forEach>
    		  <tr>
              	<td class="report" colspan="2">&nbsp;</td>
                <td class="report"  align="right">
                	<b><fmt:message key='sfa.message.total'/></b>&nbsp;&nbsp;&nbsp;
                </td>
                <td class="report" align="right"><%= numFormat.format(totalValue)%></td>
                <td class="report" align="right"><%=numFormat.format(totalAdValue)%></td>
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
    <tr>
       <td class="report" >&nbsp;</td>
    </tr>

    <tr>
       <td class="report" ><b><fmt:message key='sfa.message.summaryTotals'/></b></td>
    </tr>
    <tr>
        <td class="report" >
            <table>
                <tr>
                    <td class="report" width="30%"> <b><fmt:message key='sfa.message.oppoturnities'/></b></td>
                    <td class="report"><b><%=setting.getCurrencySymbol()%> <%=numFormat.format(sumTotalValue)%></b></td>
                </tr>
                <tr>
                    <td class="report" width="30%"> <b><fmt:message key='sfa.message.weightedOppoturnities'/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
                    <td class="report"><b> <%=setting.getCurrencySymbol()%> <%=numFormat.format(sumTotalAdValue)%></b></td>
                </tr> </table>
        </td>
    </tr>
</table>




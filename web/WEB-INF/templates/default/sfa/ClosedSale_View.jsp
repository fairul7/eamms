<%@ page import="com.tms.crm.sales.model.OpportunityModule,
				 kacang.Application,
				 com.tms.crm.sales.model.Opportunity"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>

<c:if test="${not empty(headerText)}">
	<c:out value="${headerText}" escapeXml="false"/>
</c:if>
<c:choose>
<c:when test="${form.type == 'View'}">
	<table border="1" cellpadding="2" cellspacing="0" class="formStyle" width="100%">
		<tr>
			<td valign="top" width="20%"><b><fmt:message key='sfa.message.sale'/>:</b></td>
			<td valign="top" width="30%"><x:display name="${form.absoluteName}.lbOpportunityName"/></td>
			<td valign="top" width="20%"><b><fmt:message key='sfa.message.type'/>:</b></td>
			<td valign="top" width="30%"><x:display name="${form.absoluteName}.lbHasPartner"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.status'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpportunityStatus"/></td>
            <td colspan="2" > &nbsp;</td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.startdate'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpportunityStart"/></td>
			<td valign="top"><b><fmt:message key='sfa.message.closingDate'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpportunityEnd"/></td>
		</tr>

		<%
			// Getting financial settings : currency symbols
			OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
			Opportunity csing = om.getFinancilSetting();
		%>

		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.source'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpportunitySource"/>&nbsp;</td>
			<td valign="top"><b><fmt:message key='sfa.message.value'/>:  (<%=csing.getCurrencySymbol()%>)</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpportunityValue"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.remarks'/>:</b></td>
			<td colspan="3" valign="top"><x:display name="${form.absoluteName}.lbOpportunityLastRemarks"/>&nbsp;</td>
		</tr>
	</table>
</c:when>
</c:choose>

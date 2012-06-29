<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<c:if test="${not empty(headerText)}">
	<c:out value="${headerText}" escapeXml="false"/>
</c:if>
	<table border="1" cellpadding="2" cellspacing="0" class="formStyle" width="<c:out value="${form.width}"/>">
		<tr>
			<td valign="top" width="20%"><b><fmt:message key='sfa.message.opportunity'/>:</b></td>
			<td valign="top" width="30%"><x:display name="${form.absoluteName}.lbOpportunityName"/></td>
			<td valign="top" width="20%"><b><fmt:message key='sfa.message.type'/>:</b></td>
			<td valign="top" width="30%"><x:display name="${form.absoluteName}.lbHasPartner"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.status'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpportunityStatus"/></td>
			<td valign="top"><b><fmt:message key='sfa.message.stage'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpportunityStage"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.startdate'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpportunityStart"/></td>
			<td valign="top"><b><fmt:message key='sfa.message.estimatedclosingdate'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpportunityEnd"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.modifiedBy'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbModifiedBy"/>&nbsp;</td>
			<td valign="top"><b><fmt:message key='sfa.message.value'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpportunityValue"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.remarks'/>:</b></td>
			<td colspan="3" valign="top"><x:display name="${form.absoluteName}.lbOpportunityLastRemarks"/>&nbsp;</td>
		</tr>
	</table>
<jsp:include page="../form_footer.jsp" flush="true"/>

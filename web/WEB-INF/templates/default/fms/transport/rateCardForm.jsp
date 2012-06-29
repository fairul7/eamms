<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>
<c:choose>
	<c:when test="${form.action == 'form.action.view'}">
		<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.transportChargesName'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbName"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.chargesBasedOn'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbChargesBasedOn"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.chargeAmount(RM)'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbChargeAmount"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				&nbsp;</td>
			<td class="classRow" valign="top" width="40%">
				&nbsp;</td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				&nbsp;</td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.pnButton"/></td>
			</tr>
		</table>
	</c:when>
	<c:when test="${form.action == 'form.action.add' or form.action == 'form.action.edit'}">
		<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.transportChargesName*'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.category"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.chargesBasedOn*'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.pnType"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.chargeAmount(RM)*'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfCharge"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.effectDate*'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.dpfEffectiveDate"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				&nbsp;</td>
			<td class="classRow" valign="top" width="40%">
				&nbsp;</td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				&nbsp;</td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.pnButton"/></td>
			</tr>
		</table>
	</c:when>
	<c:when test="${form.action == 'form.action.driver'}">
		<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.currentCharges'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbCurrentCharge"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.chargesPerHour*'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfCharge"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.effectDate*'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.dpfEffectiveDate"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				&nbsp;</td>
			<td class="classRow" valign="top" width="40%">
				&nbsp;</td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				&nbsp;</td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.pnButton"/></td>
			</tr>
		</table>		
	</c:when>
</c:choose>
<jsp:include page="../../form_footer.jsp" flush="true"/>
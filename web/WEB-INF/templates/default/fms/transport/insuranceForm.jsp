<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>

		<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key="fms.tran.form.vehicleNumber"/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbVehicleNum"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.roadTaxRenewalDate'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.dpfRTRenew"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.roadTaxAmount'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfRTAmount"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.roadTaxPeriodCovered'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.pnRTPeriod.lb5"/>
				<x:display name="${form.absoluteName}.pnRTPeriod.dpfRTPeriodFrom"/><br>
				<x:display name="${form.absoluteName}.pnRTPeriod.lb6"/>&nbsp;&nbsp;&nbsp;
				<x:display name="${form.absoluteName}.pnRTPeriod.dpfRTPeriodTo"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.insuranceRenewalDate'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.dpfISRenew"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.insuranceCompanyName'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfISName"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.insuranceAmount'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfISAmount"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.insurancePeriodCovered'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.pnISPeriod.lb10"/>
				<x:display name="${form.absoluteName}.pnISPeriod.dpfISPeriodFrom"/><br>
				<x:display name="${form.absoluteName}.pnISPeriod.lb11"/>&nbsp;&nbsp;&nbsp;
				<x:display name="${form.absoluteName}.pnISPeriod.dpfISPeriodTo"/></td>
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

<jsp:include page="../../form_footer.jsp" flush="true"/>
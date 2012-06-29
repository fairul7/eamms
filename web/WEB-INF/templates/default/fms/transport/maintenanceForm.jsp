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
				<fmt:message key='fms.tran.form.serviceDate'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.dpfServiceDate"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.sendDate'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.dpfSendDate"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.workshopName'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.sbWorkshop"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.workshopAddress'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tbWSAddress"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.jobOrderNumber'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfOrderNum"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.InvoiceNumber'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfInvoiceNum"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.cost'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfCost"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.reason'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tbReason"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.remark'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tbRemark"/></td>
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
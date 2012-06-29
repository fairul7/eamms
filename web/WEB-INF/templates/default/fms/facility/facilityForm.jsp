<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>
<c:choose>
	<c:when test="${form.action == 'form.action.view'}">
	<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
		<tr><td class="classRowLabel" valign="top" align="right" width="15%">
			<fmt:message key='fms.facility.form.category'/>*</td>
		<td class="classRow" valign="top" width="40%">
			<x:display name="${form.absoluteName}.lbCategory"/></td>
		</tr>
		<tr><td class="classRowLabel" valign="top" align="right" width="15%">
			<fmt:message key='fms.facility.form.itemName*'/></td>
		<td class="classRow" valign="top" width="40%">
			<x:display name="${form.absoluteName}.lbName"/></td>
		</tr>
		<tr><td class="classRowLabel" valign="top" align="right" width="15%">
			<fmt:message key='fms.facility.form.itemDescription'/></td>
		<td class="classRow" valign="top" width="40%">
			<x:display name="${form.absoluteName}.lbDescription"/></td>
		</tr>
		<tr><td class="classRowLabel" valign="top" align="right" width="15%">
			<fmt:message key='fms.facility.form.channel'/></td>
		<td class="classRow" valign="top" width="40%">
			<x:display name="${form.absoluteName}.lbChannel"/></td>
		</tr>
		<tr><td class="classRowLabel" valign="top" align="right" width="15%">
			<fmt:message key='fms.facility.form.makeType'/></td>
		<td class="classRow" valign="top" width="40%">
			<x:display name="${form.absoluteName}.lbMakeType"/></td>
		</tr>
		<tr><td class="classRowLabel" valign="top" align="right" width="15%">
			<fmt:message key='fms.facility.form.modelName'/></td>
		<td class="classRow" valign="top" width="40%">
			<x:display name="${form.absoluteName}.lbModelName"/></td>
		</tr>
		<tr><td class="classRowLabel" valign="top" align="right" width="15%">
			<fmt:message key='fms.facility.form.quantity'/></td>
		<td class="classRow" valign="top" width="40%">
			<x:display name="${form.absoluteName}.lbQuantity"/></td>
		</tr>
		<tr><td class="classRowLabel" valign="top" align="right" width="15%">
			<fmt:message key='fms.facility.form.preventiveMaintenance'/></td>
		<td class="classRow" valign="top" width="40%">
			<x:display name="${form.absoluteName}.lbPM"/></td>
		</tr>
		<tr><td class="classRowLabel" valign="top" align="right" width="15%">
			<fmt:message key='fms.facility.form.poolableItem'/></td>
		<td class="classRow" valign="top" width="40%">
			<x:display name="${form.absoluteName}.lbPool"/></td>
		</tr><tr><td class="classRowLabel" valign="top" align="right" width="15%">
			<fmt:message key='fms.facility.form.relatedChildItem'/></td>
		<td class="classRow" valign="top" width="40%">
			<x:display name="${form.absoluteName}.pnChildAll"/></td>
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
	<c:when test="${form.action == 'form.action.add' || form.action == 'form.action.edit'}">
		<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.category'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.sbCategory"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.itemName*'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfName"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.itemDescription'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tbDescription"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.channel'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.sbChannel"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.makeType'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfMakeType"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.modelName'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfModelName"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.preventiveMaintenance'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.pnPM"/></td>
			</tr>
			<tr id="pm" name="pm"><td class="classRowLabel" valign="top" align="right" width="15%">
				&nbsp;</td>
			<td class="classRow" valign="top" width="40%">
				<table border="0" cellpadding="2" cellspacing="0" width="100%" align="left">
					<tr><td valign="top" align="left" width="6%">
						<x:display name="${form.absoluteName}.pnPM1.rdPMMonth"/></td>
					<td id="monthField" valign="top" align="left" width="31%">
						<x:display name="${form.absoluteName}.pnPM1.pnPM2"/></td>
					</tr>
					<tr><td valign="top" align="left" width="6%">
						<x:display name="${form.absoluteName}.pnPM1.rdPMYear"/></td>
					<td id="yearField" valign="top" align="left" width="31%">
						<x:display name="${form.absoluteName}.pnPM1.pnPM3"/></td>
					</tr>
				</table></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.poolableItem'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.pnPool"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.relatedChildItem'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.pnChild"/></td>
			</tr>
			<tr id="child" name="child"><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.selectRelatedChildItem'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.ssbChild"/></td>
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
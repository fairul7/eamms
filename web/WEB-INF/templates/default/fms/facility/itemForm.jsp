<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>
<c:choose>
	<c:when test="${form.action == 'form.action.view'}">
		<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.itemName'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbItemName"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.itemCode'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbItemCode"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.eAssetNumber'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbEAsset"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.purchasedDate'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbPurchasedDate"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.purchasedCost'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbPurchasedCost"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.doNumber'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbDONum"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.table.storeLocation'/>*</td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbLocation"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.replacement'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbReplacement"/></td>
			</tr>
			<c:if test="${form.status == 'W' or form.status == 'M'}">
			<tr><td colspan="2" align="center">
			<table width="460px" style="border:1px solid #ff0000; ">
				<tr><td>
					<p style="color:#ff0000"><strong>
					<c:choose>
					<c:when test="${form.status == 'W'}">
						<fmt:message key='fms.facility.form.writeoff'/>
					</c:when>
					<c:when test="${form.status == 'M'}">
						<fmt:message key='fms.facility.form.missingStolen'/>
					</c:when>
					</c:choose>
					</strong></p>
				</td></tr>
				<tr><td>
					<strong><fmt:message key='fms.facility.form.date'/></strong> : 
					<x:display name="${form.absoluteName}.lbDate"/>
				</td></tr>
				<tr><td>
					<strong><fmt:message key='fms.facility.form.relatedDocument'/></strong> : 
					<x:display name="${form.absoluteName}.liLink"/>
				</td></tr>
				<tr><td>
					<strong><fmt:message key='fms.facility.form.Reason'/></strong> : <br>
					<x:display name="${form.absoluteName}.lbReason"/>
				</td></tr>
				<tr><td>
				<strong><fmt:message key='fms.tran.form.createdBy'/></strong> : 
				<x:display name="${form.absoluteName}.lbBy"/>
			</td></tr>
			</table>
			</td></tr>
			</c:if>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				&nbsp;</td>
			<td class="classRow" valign="top" width="40%">
				&nbsp;</td>
			</tr>
			<tr><td class="classRow" valign="top" width="40%" colspan="2" align="center">
				<x:display name="${form.absoluteName}.pnButton"/></td>
			</tr>
		</table>
	</c:when>
	<c:when test="${form.action == 'form.action.add' || form.action == 'form.action.edit'}">
		<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.itemName'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbItemName"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.itemCode'/></td>
			<td class="classRow" valign="top" width="40%">
				<c:choose>
				<c:when test="${form.action == 'form.action.edit'}">
					<x:display name="${form.absoluteName}.lbItemCode"/>
				</c:when>
				<c:when test="${form.action == 'form.action.add'}">
					<x:display name="${form.absoluteName}.tfItemCode"/>
				</c:when>
				</c:choose>
				</td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.eAssetNumber'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfEAsset"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.purchasedDate'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.dpfPurchasedDate"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.purchasedCost'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfPurchasedCost"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.doNumber'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfDONum"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.table.storeLocation'/>*</td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.sbLocation"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.replacement'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.cbReplacement"/></td>
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
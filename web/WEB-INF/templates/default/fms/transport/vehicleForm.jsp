<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>
<c:choose>
	<c:when test="${form.action == 'vehicle.action.view'}">
		<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.vehicleNumber*'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbVehicleNum"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.channel*'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbChannel"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.category'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbCategory"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.vehicleType'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbType"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.engineNumber'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbEngineNo"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.casisNo'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbChasisNo"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.makeType'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbMakeType"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.modelName'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbModelName"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.engineCapacity'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbEngineCapacity"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.color'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbColour"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.location'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbLocation"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.year'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbYear"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.ncb'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbNCB"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.registrationDate'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbRegistrationDate"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.numberOfPassengers'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbNumOfPassengers"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.vehicleCharges'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbCharge"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.preventiveMaintenance'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.lbMaintenanceType"/><br>
			</tr>
			<c:if test="${form.status == '2'}">
			<tr><td colspan="2" align="center">
			<table width="460px" style="border:1px solid #ff0000; ">
				<tr><td>
					<p style="color:#ff0000"><strong><fmt:message key='fms.tran.form.writeoff'/></strong></p>
				</td></tr>
				<tr><td>
					<strong><fmt:message key='fms.tran.form.attachment'/></strong> : 
						<x:display name="${form.absoluteName}.liWriteoffLink"/>
				</td></tr>
				<tr><td>
					<strong><fmt:message key='fms.tran.form.Reason'/></strong> : <br>
					<x:display name="${form.absoluteName}.lbWriteoffReason"/>
				</td></tr>
				<tr><td>
				<strong><fmt:message key='fms.tran.form.writeoffBy'/></strong> : 
				<x:display name="${form.absoluteName}.lbWriteoffBy"/>
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
	<c:when test="${form.action == 'vehicle.action.add' || form.action == 'vehicle.action.edit'}">
		<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.vehicleNumber*'/></td>
			<td class="classRow" valign="top" width="40%">
				<c:choose>
				<c:when test="${form.action == 'vehicle.action.edit'}">
					<x:display name="${form.absoluteName}.lbVehicleNum"/>
				</c:when>
				<c:when test="${form.action == 'vehicle.action.add'}">
					<x:display name="${form.absoluteName}.tfVehicleNum"/>
				</c:when>
				</c:choose>
				</td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.channel*'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.sbChannel"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.category'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.sbCategory"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.vehicleType'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.pnType"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.engineNumber'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfEngineNo"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.casisNo'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfChasisNo"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.makeType'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.sbMakeType"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.modelName'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfModelName"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.engineCapacity'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfEngineCapacity"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.color'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfColour"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.location'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfLocation"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.year'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfYear"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.ncb'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfNCB"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.registrationDate'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.dpfRegistrationDate"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.numberOfPassengers'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfNumOfPassengers"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.vehicleCharges*'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.sbCharge"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.form.preventiveMaintenance'/></td>
			<td class="classRow" valign="top" width="40%">
				<table border="0" cellpadding="2" cellspacing="0" width="100%" align="left">
					<tr><td valign="top" align="left" width="8%">
						<fmt:message key='fms.tran.form.maintenanceType'/></td>
					<td valign="top" align="left" width="6%">
						<x:display name="${form.absoluteName}.rdByKM"/></td>
					<td id="kmField" valign="top" align="left" width="31%">
						<fmt:message key='fms.tran.form.every'/>
						<x:display name="${form.absoluteName}.pnMaintenance1.pnMaintenance2.tfByKM"/>
						<fmt:message key='fms.tran.form.km'/></td>
					</tr>
					<tr><td valign="top" align="right" width="8%">&nbsp;</td>
					<td valign="top" align="left" width="6%">
						<x:display name="${form.absoluteName}.rdByMonth"/></td>
					<td id="monthField" valign="top" align="left" width="31%">
						<fmt:message key='fms.tran.form.every'/>
						<x:display name="${form.absoluteName}.pnMaintenance1.pnMaintenance3.tfByMonth"/>
						<fmt:message key='fms.tran.form.month'/></td>
					</tr>
				</table>
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
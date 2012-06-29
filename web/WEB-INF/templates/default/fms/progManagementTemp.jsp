<%@include file="/common/header.jsp" %>
<c:set var="form" value="${widget}" />

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
	<tr>
		<td>
            <jsp:include page="../form_header.jsp" flush="true"></jsp:include>
			<table width="100%" cellpadding="3" cellspacing="1">
				<tr>
					<td class="classRowLabel" valign="top" align="right" width="15%"><fmt:message key='fms.setup.form.programName'/>&nbsp;*</td>
					<td class="classRow" valign="top" width="40%"><x:display name="${form.childMap.name.absoluteName}"/></td>
				</tr>
				<tr>
					<td class="classRowLabel" valign="top" align="right" width="15%"><fmt:message key='fms.setup.form.description'/></td>
					<td class="classRow" valign="top" width="40%"><x:display name="${form.childMap.description.absoluteName}"/></td>
				</tr>
				<tr>
					<td class="classRowLabel" valign="top" align="right" width="15%"><fmt:message key='fms.setup.form.producer'/></td>
					<td class="classRow" valign="top" width="40%"><x:display name="${form.childMap.producer.absoluteName}"/></td>
				</tr>
				<tr>
					<td class="classRowLabel" valign="top" align="right" width="15%"><fmt:message key='fms.setup.form.pfeCode'/>&nbsp;*</td>
					<td class="classRow" valign="top" width="40%">
						<x:display name="${form.childMap.pfeCode.absoluteName}"/>
						<x:display name="${form.childMap.pfeCodeHidden.absoluteName}"/>
					</td>
				</tr>
				<tr>
					<td class="classRowLabel" valign="top" align="right" width="15%"><fmt:message key='fms.setup.form.startProductionDate'/>&nbsp;*</td>
					<td class="classRow" valign="top" width="40%"><x:display name="${form.absoluteName}.startProductionDate"/></td>
				</tr>
				<tr>
					<td class="classRowLabel" valign="top" align="right" width="15%"><fmt:message key='fms.setup.form.endProductionDate'/>&nbsp;*</td>
					<td class="classRow" valign="top" width="40%"><x:display name="${form.childMap.endProductionDate.absoluteName}"/></td>
				</tr>
				<tr>
					<td class="classRowLabel" valign="top" align="right" width="15%"><fmt:message key='fms.setup.form.department'/>&nbsp;*</td>
					<td class="classRow" valign="top" width="40%"><x:display name="${form.childMap.department.absoluteName}"/></td>
				</tr>
				<tr>
					<td class="classRowLabel" valign="top" align="right" width="15%"><fmt:message key='fms.setup.form.engManpowerBudget'/></td>
					<td class="classRow" valign="top" width="40%"><x:display name="${form.childMap.engManpowerBudget.absoluteName}"/></td>
				</tr>
				<tr>
					<td class="classRowLabel" valign="top" align="right" width="15%"><fmt:message key='fms.setup.form.facilitiesBudget'/></td>
					<td class="classRow" valign="top" width="40%"><x:display name="${form.childMap.facilitiesBudget.absoluteName}"/></td>
				</tr>
				<tr>
					<td class="classRowLabel" valign="top" align="right" width="15%"><fmt:message key='fms.setup.form.vtrBudget'/></td>
					<td class="classRow" valign="top" width="40%"><x:display name="${form.childMap.vtrBudget.absoluteName}"/></td>
				</tr>
				<tr>
					<td class="classRowLabel" valign="top" align="right" width="15%"><fmt:message key='fms.setup.form.transportBudget'/></td>
					<td class="classRow" valign="top" width="40%"><x:display name="${form.childMap.transportBudget.absoluteName}"/></td>
				</tr>
				<tr>
					<td class="classRowLabel" valign="top" align="right" width="15%"><fmt:message key='fms.setup.form.status'/></td>
					<td class="classRow" valign="top" width="40%"><x:display name="${form.childMap.status.absoluteName}"/></td>
				</tr>
				<tr>
					<td class="classRowLabel" valign="top" align="right" width="15%">&nbsp;</td>
					<td class="classRow" valign="top" width="40%"><x:display name="${form.childMap.pnbtn.absoluteName}"/></td>
				</tr>
				<%--<tr>
					<td>&nbsp;</td>
					<td><x:display name="${form.childMap.btnsave.absoluteName}"/><x:display name="${form.childMap.btncancel.absoluteName}"/></td>
				</tr>--%>
								
				<jsp:include flush="true" page="../form_footer.jsp"></jsp:include>
			</table>
         </td>
	</tr>	
</table>



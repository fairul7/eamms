

<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
<c:set var="form" value="${w}" />

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
	<tr>
		<td>
                        <jsp:include page="../form_header.jsp" flush="true"></jsp:include>
			<table width="100%" cellpadding="3" cellspacing="1">
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<fmt:message key="leave.label.Global" />
					</td>
					<td class="classRow">
						<x:display name="${form.halfDay.absoluteName}" /><br/>
						<x:display name="${form.workingDaysFive.absoluteName}" />
						<x:display name="${form.workingDaysFiveHalf.absoluteName}" />
						<x:display name="${form.workingDaysSix.absoluteName}" /><br/>
						<x:display name="${form.alternateWeekend.absoluteName}" /><br/>
						<x:display name="${form.fridayWeekend.absoluteName}" /><br/>
						<x:display name="${form.proRata.absoluteName}" /><br/>
						<x:display name="${form.realTime.absoluteName}" /><br/>
						<x:display name="${form.allowShift.absoluteName}" /><br/>
						<x:display name="${form.notifyMemo.absoluteName}" /><br/>
						<x:display name="${form.notifyEmail.absoluteName}" /><br/>
						<x:display name="${form.ccAdmin.absoluteName}" /><br/>
						<x:display name="${form.carryForwardMaxDays.absoluteName}" />
						<fmt:message key="leave.label.carryForwardMaxDays" /><br/>
						<br/>
						<x:display name="${form.submitButton.absoluteName}" />
						<br/>
					</td>
				</tr>
				
				<tr>
					<td class="contentTitleFont" colspan="2" valign="top" align="left">
						<fmt:message key="general.label.carryForward" />
					</td>
				</tr>
				
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<fmt:message key="leave.label.LeaveCarryForward" />
					</td>
					<td class="classRow">
						<x:display name="${form.yearSelect.absoluteName}" /><br/>
						<br/>
						<x:display name="${form.carryForwardButton.absoluteName}" /><br/>
					</td>
				</tr>
								
				<jsp:include flush="true" page="../form_footer.jsp"></jsp:include>
			</table>
			
		</td>
	</tr>
</table>

</td>
</tr>	
</table>





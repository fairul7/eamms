<%@ include file="/common/header.jsp" %>

<x:config >
    <page name="financialYear">
        <com.tms.crm.sales.ui.FinancialYearSetup name="setup"/>
    </page>
</x:config>

	<c:if test="${forward.name == 'added'}">
		<script>
			alert('Financial settings have been updated');
		</script>
	</c:if>

	<c:if test="${forward.name == 'updated'}">
		<script>
			alert('Financial settings have been updated');
	</script>
	</c:if>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />

<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
	<tr valign="top">
		<td align="left" valign="top" class="sfaHeader">
			<fmt:message key="sfa.message.financialYearSetup"/>
		</td>
	</tr>

	<tr>
		<td class="sfaRow">
					<x:display name="financialYear.setup" ></x:display>
		</td>
	</tr>
	<tr>
		<td class="sfaFooter">
			&nbsp;
		</td>
	</tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
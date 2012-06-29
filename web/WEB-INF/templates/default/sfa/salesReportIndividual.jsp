<%@include file="/common/header.jsp"%>
<c:set var="form" value="${widget}"  ></c:set>
<jsp:include page="../form_header.jsp" flush="true"/>


<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="100%">
	<tr>
		<td colspan ="2" height="40" >
			<b>Select account manager to see the sales report</b>
		</td>
	</tr>


	<tr>
		<td width="20%" >
			<b><fmt:message key="sfa.label.accountManager"/><b/>
		</td>
		<td>
			<x:display name="${form.accMrgs.absoluteName}" />
		</td>
	</tr>
	<tr>
		<td>
			<b><fmt:message key="sfa.message.financialYear"/></b>
		</td>
		<td>
			<x:display name="${form.financialYear.absoluteName}" />
		</td>
	</tr>


	<tr>
		<td>&nbsp;</td>
		<td>
			<x:display name="${form.view.absoluteName}" />
		</td>
	</tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>

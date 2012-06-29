<%@include file="/common/header.jsp"%>
<c:set var="form" value="${widget}"  ></c:set>
<jsp:include page="../form_header.jsp" flush="true"/>


<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="100%">

	<br>
	<tr>
		<td width="30%" >
			<b><fmt:message key="sfa.message.yearEnds"/><b/>
		</td>
		<td>
			<x:display name="${form.months.absoluteName}"/>
		</td>
	</tr>

	<tr>
		<td width="30%" >
			<b><fmt:message key="sfa.message.currencySymbol"/><b/>
		</td>
		<td>
			<x:display name="${form.currency.absoluteName}"/>
		</td>
	</tr>

	<tr>
		<td>&nbsp;</td>
		<td>
			<x:display name="${form.submit.absoluteName}"/>
		</td>
	</tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>

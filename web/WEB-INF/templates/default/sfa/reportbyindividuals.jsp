<%@include file="/common/header.jsp"%>

<c:set var="form" value="${widget}"  ></c:set>

    <jsp:include page="../form_header.jsp" flush="true"/>

<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>">
 <tr>
    <td width="20%">
        <b><fmt:message key='sfa.message.accountManagers'/></b>
    </td>
    <td>
        <x:display name="${form.viewAll.absoluteName}" />
    </td>
 </tr>


 <tr>
    <td>&nbsp;</td>

    <td>
        <x:display name="${form.viewSelected.absoluteName}"/>
        <br>
        <x:display name="${form.users.absoluteName}"/>
    </td>
 </tr>
 </table>

<div align="right">

<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="80%">
	<tr>
		<td colspan="2"><x:display name="${form.start.absoluteName}"/><b><fmt:message key="sfa.message.startDate"/></b></td>
		<td colspan="2"><x:display name="${form.close.absoluteName}"/><b><fmt:message key="sfa.message.closeDate"/></b></td>
	</tr>
	<tr>
		<td><b><fmt:message key='sfa.message.from'/></b></td>
		<td><x:display name="${form.startFrom.absoluteName}"/></td>
		<td><b><fmt:message key='sfa.message.from'/></b></td>
		<td><x:display name="${form.from.absoluteName}"/></td>
	</tr>

	<tr>
		<td><b><fmt:message key='sfa.message.to'/></b></td>
		<td><x:display name="${form.startTo.absoluteName}"/></td>
		<td><b><fmt:message key='sfa.message.to'/></b></td>
		<td><x:display name="${form.to.absoluteName}" /></td>
	</tr>


	<tr>
    <td colspan="4">
        <x:display name="${form.view.absoluteName}" />
    </td>
    </tr>
</table>
</div>

<jsp:include page="../form_footer.jsp" flush="true"/>

<%----%>

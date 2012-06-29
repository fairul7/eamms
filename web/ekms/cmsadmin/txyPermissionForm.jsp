<%@ include file="/common/header.jsp" %>

<x:config>
	<page name="txyPermissionForm">
		<com.tms.cms.taxonomy.ui.TaxonomyPermissionForm name="permissionForm"/>
	</page>
</x:config>

<c:if test="${forward.name=='cancel' }">
<c:redirect url="/ekms/cmsadmin/"/>
</c:if>
<c:if test="${forward.name=='success' }">
<script>
alert('<fmt:message key="message.success.permissionSettings"/>');
</script>
</c:if>
<c:if test="${forward.name=='noRole' }">
<script>
alert('<fmt:message key="message.error.noRole"/>');
</script>
</c:if>


<%@include file="/ekms/includes/header.jsp" %>

<table cellpadding="0" cellspacing="0" width="100%" border="0">

<tr>
    <td ><x:display name="txyPermissionForm.permissionForm" /> </td>
</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>

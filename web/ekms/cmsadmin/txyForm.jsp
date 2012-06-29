<%@ include file="/common/header.jsp" %>

<x:config>
	<page name="txyForm">
		<com.tms.cms.taxonomy.ui.TaxonomyParentForm name="parentForm"/>
	</page>
</x:config>

<c:if test="${forward.name=='move' }">
<c:redirect url="/ekms/cmsadmin/txyMoveForm.jsp?taxonomyId=${widgets['txyForm.parentForm'].parentId}"/>
</c:if>
<c:if test="${forward.name=='parent' }">
<c:redirect url="/ekms/cmsadmin/txyAddForm.jsp?parentId=0"/>
</c:if>
<c:if test="${forward.name=='child' }">
<c:redirect url="/ekms/cmsadmin/txyAddForm.jsp?parentId=${widgets['txyForm.parentForm'].parentId}"/>
</c:if>
<c:if test="${forward.name=='edit' }">
<c:redirect url="/ekms/cmsadmin/txyEditForm.jsp?taxonomyId=${widgets['txyForm.parentForm'].parentId}"/>
</c:if>
<c:if test="${forward.name=='errorParent' }">
<script>
alert('<fmt:message key="message.error.selectParent"/>');
</script>
</c:if>
<c:if test="${forward.name=='errorId' }">
<script>
alert('<fmt:message key="message.error.selectNode"/>');
</script>
</c:if>


<%@include file="/ekms/includes/header.jsp" %>

<table cellpadding="4" cellspacing="1" width="100%" border="0">
<tr>
    <td><x:display name="txyForm.parentForm" /> </td>
</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>

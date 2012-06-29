<%@ include file="/common/header.jsp" %>

<x:config>
	<page name="txyMoveForm">
		<com.tms.cms.taxonomy.ui.TaxonomyMoveForm name="moveForm"/>
	</page>
</x:config>


<c:if test="${!empty param.taxonomyId }">
<x:set name="txyMoveForm.moveForm" property="id" value="${param.taxonomyId }"/>
</c:if>


<c:if test="${forward.name=='errorId' }">
<script>
alert('<fmt:message key="message.error.selectParent"/>');
</script>
</c:if>
<c:if test="${forward.name=='success' }">
<c:redirect url="/ekms/cmsadmin/txyForm.jsp"/>
</c:if>
<c:if test="${forward.name=='cancel' }">
<c:redirect url="/ekms/cmsadmin/txyForm.jsp"/>
</c:if>



<%@include file="/ekms/includes/header.jsp" %>

<table cellpadding="4" cellspacing="1" width="100%" border="0">
<tr>
    <td><x:display name="txyMoveForm.moveForm" /> </td>
</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>

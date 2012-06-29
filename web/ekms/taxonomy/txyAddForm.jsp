<%@ include file="/common/header.jsp" %>

<x:config>
	<page name="txyAddForm">
		<com.tms.cms.taxonomy.ui.TaxonomyAddForm name="addForm"/>
	</page>
</x:config>

<c:if test="${!empty param.parentId }">
<x:set name="txyAddForm.addForm" property="parentId" value="${param.parentId }"/>
</c:if>
<c:if test="${!empty param.taxonomyId}">
<x:set name="txyAddForm.addForm" property="taxonomyId" value="${param.taxonomyId }"/>
</c:if>

<c:if test="${forward.name=='success' }">
	<script>
		alert("Node added successfully");
		document.location="txyForm.jsp";
	</script>
</c:if>
<c:if test="${forward.name=='cancel' }">
	<script>
		document.location="txyForm.jsp";
	</script>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<table cellpadding="4" cellspacing="1" width="100%" border="0">
<tr>
    <td><x:display name="txyAddForm.addForm" /> </td>
</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>
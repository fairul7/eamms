<%@ include file="/common/header.jsp" %>

<x:config>
	<page name="txyForm">
		<com.tms.cms.taxonomy.ui.TaxonomyParentForm name="parentForm"/>
	</page>
</x:config>

<c:if test="${forward.name=='parent' }">
<c:redirect url="/ekms/taxonomy/txyAddForm.jsp"/>
</c:if>
<c:if test="${forward.name=='child' }">
<c:redirect url="/ekms/taxonomy/txyAddForm.jsp?parentId=${widgets['txyForm.parentForm'].parentId}"/>
</c:if>
<c:if test="${forward.name=='edit' }">
<c:redirect url="/ekms/taxonomy/txyAddForm.jsp?taxonomyId=${widgets['txyForm.parentForm'].parentId}"/>
</c:if>
<c:if test="${forward.name=='errorParent' }">
<script>
alert("Please select a parent");
</script>
</c:if>
<c:if test="${forward.name=='errorId' }">
<script>
alert("Please select a taxonomy node");
</script>
</c:if>


<%@include file="/ekms/includes/header.jsp" %>

<table cellpadding="4" cellspacing="1" width="100%" border="0">
<tr>
    <td><x:display name="txyForm.parentForm" /> </td>
</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>

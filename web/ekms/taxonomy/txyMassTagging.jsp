<%@ include file="/common/header.jsp" %>

<x:config>
	<page name="txyMassTagging">
		<com.tms.cms.taxonomy.ui.TaxonomyMassTagging name="massTagging"/>
	</page>
</x:config>


<%@include file="/ekms/includes/header.jsp" %>
<table cellpadding="4" cellspacing="1" width="100%" border="0">
<tr>
    <td><x:display name="txyMassTagging.massTagging" /> </td>
</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>
<%@page import="kacang.ui.WidgetManager,
				com.tms.cms.taxonomy.ui.TaxonomyEditForm,
				kacang.Application" %>
<%@ include file="/common/header.jsp" %>

<x:config>
	<page name="txyEditForm">
		<com.tms.cms.taxonomy.ui.TaxonomyEditForm name="editForm"/>
	</page>
</x:config>

<c:if test="${!empty param.taxonomyId}">
<x:set name="txyEditForm.editForm" property="taxonomyId" value="${param.taxonomyId }"/>
</c:if>

<c:if test="${forward.name=='success' }">
	<script>
		alert('<fmt:message key="message.taxonomyNode.edited"/>');
		document.location="txyForm.jsp";
	</script>
</c:if>
<c:if test="${forward.name=='cancel' }">
	<script>
		document.location="txyForm.jsp";
	</script>
</c:if>
<c:if test="${forward.name=='delete'}">
	<%
	WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
	TaxonomyEditForm addForm = (TaxonomyEditForm)wm.getWidget("txyEditForm.editForm");
	String taxonomyId = addForm.getTaxonomyId();
	
	%>
	<script language="JavaScript">
		document.location="txyDelete.jsp?taxonomyId=<%=taxonomyId%>&mv=1";		
	</script>
</c:if>
<c:if test="${forward.name=='deleteChild' }">
	<%
	WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
	TaxonomyEditForm addForm = (TaxonomyEditForm)wm.getWidget("txyEditForm.editForm");
	String taxonomyId = addForm.getTaxonomyId();
	
	%>
	<script language="javascript">
		document.location="txyDelete.jsp?taxonomyId=<%=taxonomyId%>";	
	</script>
</c:if>


<%@include file="/ekms/includes/header.jsp" %>
<table cellpadding="4" cellspacing="1" width="100%" border="0">
<tr>
    <td><x:display name="txyEditForm.editForm" /> </td>
</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>
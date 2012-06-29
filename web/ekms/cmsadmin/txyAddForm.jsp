<%@page import="kacang.ui.WidgetManager,
				com.tms.cms.taxonomy.ui.TaxonomyAddForm,
				kacang.Application" %>
<%@ include file="/common/header.jsp" %>

<x:config>
	<page name="txyAddForm">
		<com.tms.cms.taxonomy.ui.TaxonomyAddForm name="addForm"/>
	</page>
</x:config>

<c:if test="${!empty param.parentId }">
<x:set name="txyAddForm.addForm" property="parentId" value="${param.parentId }"/>
</c:if>
<c:if test="${empty param.parentId }">
<x:set name="txyAddForm.addForm" property="parentId" value="0"/>
</c:if>
<c:if test="${!empty param.taxonomyId}">
<x:set name="txyAddForm.addForm" property="taxonomyId" value="${param.taxonomyId }"/>
</c:if>
<c:if test="${empty param.taxonomyId }">
<x:set name="txyAddForm.addForm" property="taxonomyId" value=""/>

</c:if>

<c:if test="${forward.name=='success' }">
	<script>
		alert('<fmt:message key="message.taxonomyNode.added"/>');
		document.location="txyForm.jsp";
	</script>
</c:if>
<c:if test="${forward.name=='cancel' }">
	<script>
		document.location="txyForm.jsp";
	</script>
</c:if>
<%--
<c:if test="${forward.name=='delete'}">
	<script language="VBScript">
		msgAnswer = MsgBox("Click Yes to delete all child node(s).\nClick No to move child node(s) to the parent Node.", vbYesNo)
	</script>
	
	<%
	WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
	TaxonomyAddForm addForm = (TaxonomyAddForm)wm.getWidget("txyAddForm.addForm");
	String taxonomyId = addForm.getTaxonomyId();
	
	%>

	<script language="JavaScript">
		if(msgAnswer==6)
		{
		     document.location="txyDelete.jsp?taxonomyId=<%=taxonomyId%>";		
		}
		else if (msgAnswer==7)
		{
			 document.location="txyDelete.jsp?taxonomyId=<%=taxonomyId%>&mv=1";	
		}
	</script>


</c:if>
--%>
<%@include file="/ekms/includes/header.jsp" %>
<table cellpadding="4" cellspacing="1" width="100%" border="0">
<tr>
    <td><x:display name="txyAddForm.addForm" /> </td>
</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>
<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.AddForm,
                 com.tms.collab.formwizard.ui.AddFormField,
                 com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.collab.formwizard.ui.EditField"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="editFieldPage">
          <com.tms.collab.formwizard.ui.EditField name="editFormField"/>
    </page>
</x:config>



<x:set name="editFieldPage.editFormField" property="fieldFormAbsoluteName" value="addG2FieldFormPage.addG2FieldForm" />

<c:if test="${!empty param.formId}">
	<x:set name="editFieldPage.editFormField" property="formId" value="${param.formId}"/>
</c:if>

<c:if test="${!empty param.formUid}">
	<x:set name="editFieldPage.editFormField" property="formUid" value="${param.formUid}"/>
</c:if>

<c:if test="${!empty param.templateNodeName}">
	<x:set name="editFieldPage.editFormField" property="templateNodeName" value="${param.templateNodeName}"/>
</c:if>


<c:if test="${forward.name == 'movedUp' || forward.name == 'movedDown' || forward.name == 'fieldEdited'}">
<script>
<!--
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	EditField form = (EditField)wm.getWidget("editFieldPage.editFormField");
%>
<c-rt:set var="formId" value="<%=form.getFormId()%>"/>
window.open('<c:url value="frwEditPreviewForm.jsp"/>?formId=<c:out value="${formId}"/>',
            'preview','scrollbars=yes,resizable=yes,status=yes,width=700,height=500,location=yes')
<%
}
%>
//-->
</script>
</c:if>

<c:if test="${forward.name == 'removed'}">
<script>
<!--
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	EditField form = (EditField)wm.getWidget("editFieldPage.editFormField");
%>
<c-rt:set var="formId" value="<%=form.getFormId()%>"/>
window.close();
window.open('<c:url value="frwEditPreviewForm.jsp"/>?formId=<c:out value="${formId}"/>',
            'preview','scrollbars=yes,resizable=yes,status=yes,width=700,height=500,location=yes')
<%
}
%>
//-->
</script>
</c:if>


<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
     <link rel="stylesheet" href="<%= request.getContextPath() %>/cmsadmin/styles/style.css">
</head>
<table width="100%" style="border: 1px ridge silver" cellspacing="0" cellpadding="4">
  <tr valign="MIDDLE">
    <td height="22" style="background: #003366; color: white; font-weight: bold">&nbsp;Edit Field</td>
    <td align="right" style="background: #003366; color: white; font-weight: bold">&nbsp;</td>
  </tr>
  
  <tr>
  	<td colspan="2" valign="TOP"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>

  <tr>
  	<td colspan="2" valign="TOP">
    	<x:display name="editFieldPage.editFormField" ></x:display>
    </td>
  </tr>
</table>
</body>
</html>



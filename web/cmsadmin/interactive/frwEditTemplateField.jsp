<%@ page import="kacang.ui.WidgetManager,             
                 com.tms.collab.formwizard.ui.EditTemplateField,
                 com.tms.collab.formwizard.ui.EditField"%>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.collab.formwizard.Add" module="com.tms.collab.formwizard.model.FormModule" url="noPermission.jsp" />

<x:config>
    <page name="editTemplateFieldPage">
          <com.tms.collab.formwizard.ui.EditTemplateField name="editTemplateField"/>
    </page>
</x:config>

<x:set name="editTemplateFieldPage.editTemplateField" property="fieldFormAbsoluteName" value="addG2FieldFormPage.addG2FieldForm" />


<c:if test="${!empty param.formTemplateId}">
	<x:set name = "editTemplateFieldPage.editTemplateField" property = "formTemplateId" value = "${param.formTemplateId}" />
</c:if>

<c:if test="${!empty param.formUid}">
	<x:set name = "editTemplateFieldPage.editTemplateField" property = "formUid" value = "${param.formUid}" />
</c:if>


<c:if test="${forward.name == 'movedUp' || forward.name == 'movedDown' || forward.name == 'fieldEdited'}">
<script>
<!--
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	EditTemplateField form = (EditTemplateField)wm.getWidget("editTemplateFieldPage.editTemplateField");
%>
<c-rt:set var="formTemplateId" value="<%=form.getFormTemplateId()%>"/>
window.open('<c:url value="frwTemplatePreviewForm.jsp"/>?formTemplateId=<c:out value="${formTemplateId}"/>',
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
	EditTemplateField form = (EditTemplateField)wm.getWidget("editTemplateFieldPage.editTemplateField");
%>
<c-rt:set var="formTemplateId" value="<%=form.getFormTemplateId()%>"/>
window.close();
window.open('<c:url value="frwTemplatePreviewForm.jsp"/>?formTemplateId=<c:out value="${formTemplateId}"/>',
            'preview','scrollbars=yes,resizable=yes,status=yes,width=700,height=500,location=yes')
<%
}
%>
//-->
</script>
</c:if>


<%--<c:if test="${forward.name == 'fieldEdited' || forward.name == 'cancel'}">
<script>
<!--
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	EditTemplateField form = (EditTemplateField)wm.getWidget("editTemplateFieldPage.editTemplateField");
%>
<c-rt:set var="formTemplateId" value="<%=form.getFormTemplateId()%>"/>
<c:redirect url="frwTemplatePreviewForm.jsp?formTemplateId=${formTemplateId}" />
<%
}
%>
</c:if>--%>

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
    	<x:display name="editTemplateFieldPage.editTemplateField" ></x:display>
    </td>
  </tr>

</table>

</body>
</html>



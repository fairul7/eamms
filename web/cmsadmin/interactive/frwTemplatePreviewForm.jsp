<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.collab.formwizard.Add" module="com.tms.collab.formwizard.model.FormModule" url="noPermission.jsp" />
<x:config>
    <page name="templatePreviewPanelPage">
          <com.tms.collab.formwizard.ui.TemplatePreviewPanel name="templatePreviewPanel"/>
    </page>
</x:config>

<c:if test="${!empty param.formTemplateId}">
	<x:set name = "templatePreviewPanelPage.templatePreviewPanel" property = "formTemplateId" value = "${param.formTemplateId}" />
</c:if>


<script>
<!--
    function addfields(formTemplateId){
        window.opener.document.location="<c:url value="frwAddFormTemplateField.jsp" />?formTemplateId="+formTemplateId;
        window.close();
    }

    function openEditField(formTemplateId,formUid,templateName) {
          window.open("<c:url value="frwEditTemplateField.jsp" />?formTemplateId=" + formTemplateId + "&formUid=" + formUid ,
                      "editField");
          return false;
    }
//-->
</script>

<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
     <link rel="stylesheet" href="<%= request.getContextPath() %>/cmsadmin/styles/style.css">
</head>

<body onload="this.focus()">
<table width="100%" style="border: 1px ridge silver" cellspacing="0" cellpadding="4">
	<tr valign="MIDDLE">
    	<td height="22" style="background: #003366; color: white; font-weight: bold">&nbsp;<fmt:message key='formWizard.label.formPreview'/></td>
	    <td align="right" style="background: #003366; color: white; font-weight: bold">&nbsp;</td>
	</tr>

	<tr>
    	<td colspan="2" valign="TOP"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
    </tr>
	<tr>
    	<td colspan="2" valign="TOP"><i><fmt:message key='formWizard.label.italicHiddenField'/></i></td>
	</tr>

	<tr>
	    <td colspan="2" valign="TOP"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
	</tr>

	<tr>
     	<td colspan="2" valign="TOP">
            <x:display name="templatePreviewPanelPage.templatePreviewPanel" ></x:display>
	    </td>
    </tr>
</table>
</body>
</html>



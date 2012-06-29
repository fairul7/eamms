<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.EditForm"%>
<%@ include file="/common/header.jsp" %>


<!-- Declare Widgets -->
<x:config>
    <page name="editPreviewPanelPage">
          <com.tms.collab.formwizard.ui.PreviewPanel name="editPreviewPanel"/>
    </page>
</x:config>

<c:if test="${!empty param.formId}">
	<x:set name = "editPreviewPanelPage.editPreviewPanel" property = "formId" value = "${param.formId}" />
</c:if>


<script>
<!--
    function addfields(formID){
        window.opener.document.location="<c:url value="frwEditFormField.jsp" />?formId="+formID;
        window.close();
    }

    function openEditField(formId,formUid, templateName) {
          window.open("<c:url value="frwEditField.jsp" />?formId=" + formId + "&formUid=" + formUid + "&templateNodeName=" + templateName,
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
    <td style="background: #003366; color: white; font-weight: bold">&nbsp;<fmt:message key='formWizard.label.formPreview'/></td>
  </tr>


  <tr>
    <td valign="TOP"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>

  <tr>
	<td valign="TOP"><i><fmt:message key='formWizard.label.italicHiddenField'/></i></td>
  </tr>

  <tr>
    <td valign="TOP"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>

  <tr>
     <td valign="TOP">
        <x:display name="editPreviewPanelPage.editPreviewPanel" ></x:display>
     </td>
  </tr>

     </td>
  </tr>
</table>
</body>
</html>



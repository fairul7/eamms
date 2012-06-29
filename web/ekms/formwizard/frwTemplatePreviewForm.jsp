<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.collab.formwizard.Add" module="com.tms.collab.formwizard.model.FormModule" url="noPermission.jsp" />
<x:config>
    <page name="ekmsTemplatePreviewPanelPage">
          <com.tms.collab.formwizard.ui.TemplatePreviewPanel name="ekmsTemplatePreviewPanel"/>
    </page>
</x:config>

<c:if test="${!empty param.formTemplateId}">
	<x:set name = "ekmsTemplatePreviewPanelPage.ekmsTemplatePreviewPanel" property = "formTemplateId" value = "${param.formTemplateId}" />
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
    <title>Form Template Preview</title>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
     <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body onload="this.focus()">

<table width="100%" cellspacing="0" cellpadding="4">
  <tr valign="MIDDLE" class="tableHeader">
    <td height="22" colspan="2">&nbsp;<fmt:message key='formWizard.label.formPreview'/></td>
  </tr>


  <tr class="tableBackground">
    <td colspan="2" valign="TOP"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>

  <tr class="tableBackground">
	<td colspan="2" valign="TOP"><i><fmt:message key='formWizard.label.italicHiddenField'/> </i></td>
  </tr>

  <tr class="tableBackground">
    <td colspan="2" valign="TOP"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>

  <tr class="tableBackground">
     <td colspan="2" valign="TOP">
     <x:display name="ekmsTemplatePreviewPanelPage.ekmsTemplatePreviewPanel" ></x:display>
     </td>
     </tr>
</table>
</body>
</html>



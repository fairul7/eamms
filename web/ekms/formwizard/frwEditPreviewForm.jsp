<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.EditForm,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>
<!-- Declare Widgets -->
<x:config>
    <page name="ekmsEditPreviewPanelPage">
          <com.tms.collab.formwizard.ui.PreviewPanel name="ekmsEditPreviewPanel"/>
    </page>
</x:config>

<c:if test="${!empty param.formId}">
	<x:set name = "ekmsEditPreviewPanelPage.ekmsEditPreviewPanel" property = "formId" value = "${param.formId}" />
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
    <title>Form Preview</title>
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
	<td colspan="2" valign="TOP"><i><fmt:message key='formWizard.label.italicHiddenField'/></i></td>
  </tr>

  <tr class="tableBackground">
    <td colspan="2" valign="TOP"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  
  <tr class="tableBackground">
     <td colspan="2" valign="TOP">
     <x:display name="ekmsEditPreviewPanelPage.ekmsEditPreviewPanel" ></x:display>
     </td>
     </tr>
</table>
</body>
</html>



<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="addG2FieldFormPage">
        	<com.tms.collab.formwizard.ui.AddG2FieldForm name="addG2FieldForm"/>
    </page>
</x:config>

<c:if test="${!empty param.widgetName}">
    <x:set name="addG2FieldFormPage.addG2FieldForm" property="initWidgetId" value="${param.widgetName}"/>
</c:if>

<c:if test="${!empty param.formId}">
    <x:set name="addG2FieldFormPage.addG2FieldForm" property="formId" value="${param.formId}"/>
</c:if>

<c:if test="${!empty param.formUid}">
    <x:set name="addG2FieldFormPage.addG2FieldForm" property="formUid" value="${param.formUid}"/>
</c:if>

<c:if test="${!empty param.formTemplateId}">
    <x:set name="addG2FieldFormPage.addG2FieldForm" property="formTemplateId" value="${param.formTemplateId}"/>
</c:if>

<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/cmsadmin/styles/style.css">
</head>
<body onload="this.focus()">

<table width="100%" style="border: 1px ridge silver" cellspacing="0" cellpadding="4">
  <tr valign="MIDDLE">
    <td style="background: #003366; color: white; font-weight: bold">&nbsp;<fmt:message key='formWizard.label.frwAddG2FieldForm.tableGridColumns'/></td>
  </tr>


  <tr>
    <td valign="TOP"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>


  <tr>
     <td valign="TOP">
        <x:display name="addG2FieldFormPage.addG2FieldForm" />
     </td>
  </tr>

     </td>
  </tr>
</table>
</body>
</html>
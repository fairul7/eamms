<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="ekmsAddG2FieldFormPage">
        	<com.tms.collab.formwizard.ui.AddG2FieldForm name="ekmsAddG2FieldForm"/>
    </page>
</x:config>

<c:if test="${!empty param.widgetName}">
    <x:set name="ekmsAddG2FieldFormPage.ekmsAddG2FieldForm" property="initWidgetId" value="${param.widgetName}"/>
</c:if>

<c:if test="${!empty param.formId}">
    <x:set name="ekmsAddG2FieldFormPage.ekmsAddG2FieldForm" property="formId" value="${param.formId}"/>
</c:if>

<c:if test="${!empty param.formUid}">
    <x:set name="ekmsAddG2FieldFormPage.ekmsAddG2FieldForm" property="formUid" value="${param.formUid}"/>
</c:if>

<c:if test="${!empty param.formTemplateId}">
    <x:set name="addG2FieldFormPage.addG2FieldForm" property="formTemplateId" value="${param.formTemplateId}"/>
</c:if>

<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
     <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>

<table width="100%" cellspacing="0" cellpadding="4">
  <tr valign="MIDDLE" class="tableHeader">
    <td height="22" colspan = "2">&nbsp;<fmt:message key='formWizard.label.frwAddG2FieldForm.tableGridColumns'/></td>
  </tr>

  <tr class="tableBackground">
  	<td colspan="2" valign="TOP"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>

  <tr class="tableBackground">
  	<td colspan="2" valign="TOP">
    	<x:display name="ekmsAddG2FieldFormPage.ekmsAddG2FieldForm" />
    </td>
  </tr>

</table>


</html>
<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.AddForm,
                 com.tms.collab.formwizard.ui.AddFormField,
                 com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.collab.formwizard.ui.EditField"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="ekmsEditFieldPage">
          <com.tms.collab.formwizard.ui.EditField name="ekmsEditFormField"/>
    </page>
</x:config>



<x:set name="ekmsEditFieldPage.ekmsEditFormField" property="fieldFormAbsoluteName" value="ekmsAddG2FieldFormPage.ekmsAddG2FieldForm" />

<c:if test="${!empty param.formId}">
	<x:set name="ekmsEditFieldPage.ekmsEditFormField" property="formId" value="${param.formId}"/>
</c:if>

<c:if test="${!empty param.formUid}">
	<x:set name="ekmsEditFieldPage.ekmsEditFormField" property="formUid" value="${param.formUid}"/>
</c:if>

<c:choose>
<c:when test="${!empty param.templateNodeName}">
	<x:set name="ekmsEditFieldPage.ekmsEditFormField" property="templateNodeName" value="${param.templateNodeName}"/>
</c:when>
<c:otherwise>
	<x:set name="ekmsEditFieldPage.ekmsEditFormField" property="templateNodeName" value="${null}"/>
</c:otherwise>
</c:choose>


<c:if test="${forward.name == 'movedUp' || forward.name == 'movedDown'}">
<script>
<!--
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	EditField form = (EditField)wm.getWidget("ekmsEditFieldPage.ekmsEditFormField");
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

<c:if test="${forward.name == 'removed' || forward.name == 'fieldEdited'}">
<script>
<!--
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	EditField form = (EditField)wm.getWidget("ekmsEditFieldPage.ekmsEditFormField");
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
    <title><fmt:message key="formWizard.label.editField"/></title>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
     <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<table width="100%" cellspacing="0" cellpadding="4">
  <tr valign="MIDDLE" class="tableHeader">
    <td height="22" colspan = "2">&nbsp;<fmt:message key="formWizard.label.editField"/></td>
  </tr>
  
  <tr class="tableBackground">
  	<td colspan="2" valign="TOP"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  
  <tr class="tableBackground">
  	<td colspan="2" valign="TOP">
    	<x:display name="ekmsEditFieldPage.ekmsEditFormField" ></x:display>
    </td>
  </tr>
  
</table>

</body>
</html>



<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.EditTemplateField,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.collab.formwizard.Add" module="com.tms.collab.formwizard.model.FormModule" url="noPermission.jsp" />

<x:config>
    <page name="ekmsEditTemplateFieldPage">
          <com.tms.collab.formwizard.ui.EditTemplateField name="ekmsEditTemplateField"/>
    </page>
</x:config>

<x:set name="ekmsEditTemplateFieldPage.ekmsEditTemplateField" property="fieldFormAbsoluteName" value="ekmsAddG2FieldFormPage.ekmsAddG2FieldForm" />

<c:if test="${!empty param.formTemplateId}">
	<x:set name = "ekmsEditTemplateFieldPage.ekmsEditTemplateField" property = "formTemplateId" value = "${param.formTemplateId}" />
</c:if>

<c:if test="${!empty param.formUid}">
	<x:set name = "ekmsEditTemplateFieldPage.ekmsEditTemplateField" property = "formUid" value = "${param.formUid}" />
</c:if>


<c:if test="${forward.name == 'movedUp' || forward.name == 'movedDown' || forward.name == 'fieldEdited'}">
<script>
<!--
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	EditTemplateField form = (EditTemplateField)wm.getWidget("ekmsEditTemplateFieldPage.ekmsEditTemplateField");
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
	EditTemplateField form = (EditTemplateField)wm.getWidget("ekmsEditTemplateFieldPage.ekmsEditTemplateField");
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
    <td height="22" colspan="2">&nbsp;Edit Field</td>
  </tr>


  <tr class="tableBackground">
    <td colspan="2" valign="TOP"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>


  <tr class="tableBackground">
     <td colspan="2" valign="TOP">
     <x:display name="ekmsEditTemplateFieldPage.ekmsEditTemplateField" ></x:display>
     </td>
     </tr>
</table>
</body>
</html>




<%--
<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;
      Edit Form Template Field
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
			     <x:display name="ekmsEditTemplateFieldPage.ekmsEditTemplateField" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>--%>

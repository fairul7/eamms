<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.AddFormTemplateField"%>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.collab.formwizard.Add" module="com.tms.collab.formwizard.model.FormModule" url="noPermission.jsp" />

<x:config>
    <page name="ekmsAddFormTemplateFieldPage">
        	<com.tms.collab.formwizard.ui.AddFormTemplateField name="ekmsAddFormTemplateField"/>
    </page>
</x:config>

<x:set name="ekmsAddFormTemplateFieldPage.ekmsAddFormTemplateField" property="fieldFormAbsoluteName" value="ekmsAddG2FieldFormPage.ekmsAddG2FieldForm" />

<c:if test="${!empty param.formTemplateId}">
    <x:set name="ekmsAddFormTemplateFieldPage.ekmsAddFormTemplateField" property="formTemplateId" value="${param.formTemplateId}"/>
</c:if>

<c:if test="${forward.name == 'formTemplateFieldAdded'}">
<script>
<!--
<%
    WidgetManager wm = (WidgetManager) session.getAttribute("WidgetManager");

    if (wm != null) {
        AddFormTemplateField form = (AddFormTemplateField) wm.getWidget("ekmsAddFormTemplateFieldPage.ekmsAddFormTemplateField");
%>

        document.location = "<c:url value="frwAddFormTemplateField.jsp?formTemplateId=" /><%= form.getFormTemplateId() %>";
        myWin = window.open('frwTemplatePreviewForm.jsp?formTemplateId=<%= form.getFormTemplateId() %>','preview','scrollbars=yes,resizable=yes,width=700,height=500');
<%
    }
%>
//-->
</script>
</c:if>





<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;
      <fmt:message key='formWizard.label.formTemplateField'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
			     <x:display name="ekmsAddFormTemplateFieldPage.ekmsAddFormTemplateField" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>
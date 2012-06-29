<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.AddFormTemplate"%>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.collab.formwizard.Add" module="com.tms.collab.formwizard.model.FormModule" url="noPermission.jsp" />

<x:config>
    <page name="ekmsAddFormTemplatePage">
        	<com.tms.collab.formwizard.ui.AddFormTemplate name="ekmsAddFormTemplate"/>
    </page>
</x:config>




<c:if test="${forward.name == 'formTemplateAdded'}">
<%
    WidgetManager wm = (WidgetManager) session.getAttribute("WidgetManager");

    if (wm != null) {
        AddFormTemplate form = (AddFormTemplate) wm.getWidget("ekmsAddFormTemplatePage.ekmsAddFormTemplate");
%>
        <c-rt:set var="templateId" value="<%= form.getTemplateId() %>" />
        <c:redirect url = "frwAddFormTemplateField.jsp?formTemplateId=${templateId}" />
<%
    }
%>
</c:if>



<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='formWizard.label.formTemplate'/> >
       <fmt:message key='formWizard.label.newFormTemplate'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
			     <x:display name="ekmsAddFormTemplatePage.ekmsAddFormTemplate" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>
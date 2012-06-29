<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.ViewForm,
                 com.tms.collab.formwizard.model.FormModule,
                 com.tms.collab.formwizard.ui.DynamicViewFormField"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="ekmsViewFormPage">
	        <com.tms.collab.formwizard.ui.ViewForm name="ekmsViewForm"/>
            <com.tms.collab.formwizard.ui.DynamicViewFormField name="ekmsDynamicViewFormField"/>
    </page>


</x:config>

<c:if test="${!empty param.formId}">
	<x:set name="ekmsViewFormPage.ekmsViewForm" property="formId" value="${param.formId}"/>
    <x:set name="ekmsViewFormPage.ekmsDynamicViewFormField" property="formId" value="${param.formId}"/>
</c:if>



<c:if test="${forward.name == 'submissionMessage'}">
<%
    WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
    if (wm != null){
	    ViewForm form = (ViewForm)wm.getWidget("ekmsViewFormPage.ekmsViewForm");
%>
    <c-rt:set var="formId" value="<%=form.getFormId()%>"/>
    <c:redirect url="frwFormMessagePanel.jsp?reload=true&formId=${formId}" />
<%
}
%>
</c:if>

<c:if test="${forward.name == 'formLink'}">
<%
    WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
    if (wm != null){
	    DynamicViewFormField form = (DynamicViewFormField)wm.getWidget("ekmsViewFormPage.ekmsDynamicViewFormField");
%>
    <c-rt:set var="formId" value="<%=form.getFormId()%>"/>
    <c:redirect url="frwViewForm.jsp?formId=${formId}" />
<%
    }
%>
</c:if>

<c:if test="${forward.name == 'dataDrafted'}">
    <c:redirect url="frwFormsView.jsp" />
</c:if>



<%
    SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
    FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
    ViewForm form = null;
    String formId = null;

    WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
    if (wm != null)
        form = (ViewForm)wm.getWidget("ekmsViewFormPage.ekmsViewForm");

    formId = request.getParameter("formId");
    if ( formId == null || "".equals(formId))
        formId = form.getFormId();
    
    if (form == null || !module.isValidForm(formId, service.getCurrentUser(request).getId())) {
%>
<c:redirect url = "frwFormsView.jsp"/>
<%
    }
%>




<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;
          <fmt:message key='formWizard.label.formWizard'/> > 
      <fmt:message key='formWizard.label.frwViewForm.submitForm'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="ekmsViewFormPage.ekmsViewForm" ></x:display>
        <x:display name="ekmsViewFormPage.ekmsDynamicViewFormField" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>



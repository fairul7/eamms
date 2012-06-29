<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.ViewForm,
                 com.tms.collab.formwizard.model.FormModule,
                 com.tms.collab.formwizard.ui.DynamicViewFormField" %>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="viewFormPage">
		<portlet name="viewFormPortlet" text="Submit Form" width="100%" permanent="true">
	        <com.tms.collab.formwizard.ui.ViewForm name="viewForm"/>
            <com.tms.collab.formwizard.ui.DynamicViewFormField name="dynamicViewFormField"/>
		</portlet>
    </page>
</x:config>

<c:if test="${!empty param.formId}">
	<x:set name="viewFormPage.viewFormPortlet.viewForm" property="formId" value="${param.formId}"/>
  	<x:set name="viewFormPage.viewFormPortlet.dynamicViewFormField" property="formId" value="${param.formId}"/>
</c:if>



<c:if test="${forward.name == 'submissionMessage'}">
<%
    WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
    if (wm != null){
        ViewForm form = (ViewForm)wm.getWidget("viewFormPage.viewFormPortlet.viewForm");
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
        DynamicViewFormField form = (DynamicViewFormField)wm.getWidget("viewFormPage.viewFormPortlet.dynamicViewFormField");
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
        form = (ViewForm)wm.getWidget("viewFormPage.viewFormPortlet.viewForm");

    formId = request.getParameter("formId");
    if ( formId == null || "".equals(formId))
        formId = form.getFormId();


    if (form == null || !module.isValidForm(formId, service.getCurrentUser(request).getId())) {
%>
<c:redirect url = "frwFormsView.jsp"/>
<%
    }
%>

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>
<jsp:include page="/cmsadmin/includes/headerInteractive.jsp" flush="true" />
<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">
        <jsp:include page="/cmsadmin/includes/sideInteractiveFW.jsp" flush="true"/>

      </td>
      <td style="vertical-align: top;">
        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;"><br>

     <x:display name="viewFormPage.viewFormPortlet" ></x:display>




                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
              </td>
            </tr>
          </tbody>
        </table>
      </td>
    </tr>
  </tbody>
</table>






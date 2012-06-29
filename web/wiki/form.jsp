<%@ include file="/common/header.jsp" %>



<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.ViewForm,
                 kacang.services.security.SecurityService,
                 kacang.Application,
                 com.tms.collab.formwizard.model.FormModule,
                 kacang.services.security.User,
                 com.tms.collab.formwizard.ui.DynamicCmsViewFormField"%>
<x:config>
    <page name="cmsEkpDefaultViewFormPage">
	        <com.tms.collab.formwizard.ui.ViewForm name="cmsEkpDefaultFormViewForm"/>
            <com.tms.collab.formwizard.ui.DynamicCmsViewFormField name="cmsEkpDefaultDynamicCmsViewFormField"/>
    </page>
</x:config>

<c:if test="${!empty param.formId}">
	<x:set name="cmsEkpDefaultViewFormPage.cmsEkpDefaultFormViewForm" property="formId" value="${param.formId}"/>
    <x:set name="cmsEkpDefaultViewFormPage.cmsEkpDefaultDynamicCmsViewFormField" property="formId" value="${param.formId}"/>
</c:if>

<%
    ViewForm form = null;
    String formId = null;
    WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
%>
<c:if test="${forward.name == 'submissionMessage'}">
<%
    if (wm != null){
	    form = (ViewForm)wm.getWidget("cmsEkpDefaultViewFormPage.cmsEkpDefaultFormViewForm");
%>
<c-rt:set var="formId" value="<%=form.getFormId()%>"/>
<c:redirect url="formMessage.jsp?reload=true&formId=${formId}" />
<%
}
%>
</c:if>

<c:if test="${forward.name == 'formLink'}">
<%
DynamicCmsViewFormField dynamicForm = null;
if (wm != null){
	dynamicForm = (DynamicCmsViewFormField)wm.getWidget("cmsEkpViewFormPage.cmsEkpDefaultDynamicCmsViewFormField");
%>
<c-rt:set var="formId" value="<%=dynamicForm.getFormId()%>"/>
<c:redirect url="form.jsp?reload=true&formId=${formId}" />
<%
}
%>
</c:if>

<%
    SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
    FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);


    if (wm != null)
        form = (ViewForm)wm.getWidget("cmsEkpDefaultViewFormPage.cmsEkpDefaultFormViewForm");

    formId = request.getParameter("formId");
    if ( formId == null || "".equals(formId))
        formId = form.getFormId();


    if (form == null || !module.isValidForm(formId, service.getCurrentUser(request).getId())) {
%>
<c:redirect url = "formInvalid.jsp"/>
<%
    }
%>

<jsp:include page="includes/header.jsp" flush="true"  />

<div class="siteBodyHeader">
    Submit Form
</div>

<x:display name="cmsEkpDefaultViewFormPage.cmsEkpDefaultFormViewForm" ></x:display>
<x:display name="cmsEkpDefaultViewFormPage.cmsEkpDefaultDynamicCmsViewFormField" ></x:display>


<jsp:include page="includes/footer.jsp" flush="true"  />






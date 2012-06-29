<%@ include file="/common/header.jsp" %>

<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.ViewForm,
                 kacang.services.security.SecurityService,
                 kacang.Application,
                 com.tms.collab.formwizard.model.FormModule,
                 kacang.services.security.User,                 
                 com.tms.collab.formwizard.ui.DynamicCmsViewFormField"%>
<x:config>
    <page name="cmsEkpViewFormPage">
	        <com.tms.collab.formwizard.ui.ViewForm name="cmsEkpFormViewForm"/>
            <com.tms.collab.formwizard.ui.DynamicCmsViewFormField name="cmsEkpDynamicCmsViewFormField"/>
    </page>
</x:config>

<c:if test="${!empty param.formId}">
	<x:set name="cmsEkpViewFormPage.cmsEkpFormViewForm" property="formId" value="${param.formId}"/>
    <x:set name="cmsEkpViewFormPage.cmsEkpDynamicCmsViewFormField" property="formId" value="${param.formId}"/>
</c:if>

<%
    ViewForm form = null;
    String formId = null;
    WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
%>
<c:if test="${forward.name == 'submissionMessage'}">
<%
    if (wm != null){
	    form = (ViewForm)wm.getWidget("cmsEkpViewFormPage.cmsEkpFormViewForm");
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
	dynamicForm = (DynamicCmsViewFormField)wm.getWidget("cmsEkpViewFormPage.cmsEkpDynamicCmsViewFormField");
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
        form = (ViewForm)wm.getWidget("cmsEkpViewFormPage.cmsEkpFormViewForm");

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

<x:display name="cmsEkpViewFormPage.cmsEkpFormViewForm" ></x:display>
<x:display name="cmsEkpViewFormPage.cmsEkpDynamicCmsViewFormField" ></x:display>


<jsp:include page="includes/footer.jsp" flush="true"  />






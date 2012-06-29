<%@ include file="/common/header.jsp" %>

<%@ page import="kacang.services.security.SecurityService,
				 kacang.Application,				 				 
				 com.tms.collab.formwizard.model.FormModule,
				 kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.FormDataDetail"%>



<x:config>
    <page name="formDataDetailPage">
	    <portlet name="formDataDetailPortlet" text="Submitted Form Approval" width="100%" permanent="true">
        	<com.tms.collab.formwizard.ui.FormDataDetail name="formDataDetail"/>
        </portlet>
    </page>
</x:config>

<c:if test="${!empty param.formUid}">
	<x:set name="formDataDetailPage.formDataDetailPortlet.formDataDetail" property="id" value="${param.formUid}" />
</c:if>
                 
<c:if test="${forward.name == 'dataApproved'}">
	<c:redirect url = "frwApproveFormData.jsp"/>
</c:if>

<c:if test="${forward.name == 'cancel'}">
	<c:redirect url = "frwApproveFormData.jsp"/>
</c:if>


<c:if test="${forward.name == 'rejectData'}">
<script>
<!--
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null) {
	FormDataDetail form = (FormDataDetail)wm.getWidget("formDataDetailPage.formDataDetailPortlet.formDataDetail");
%>
window.open('frwRejectForm.jsp?reload=true&formUID=<%= form.getId() %>','rejectForm','scrollbars=yes,resizable=yes,width=400,height=250');
<%
}
%>
//-->
</script>
</c:if>
                 
                 
<%  
SecurityService moduleSecurity = (SecurityService)Application.getInstance().getService(SecurityService.class);
User moduleUser = moduleSecurity.getCurrentUser(request);
FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
FormDataDetail form = null;
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null)
	form = (FormDataDetail)wm.getWidget("formDataDetailPage.formDataDetailPortlet.formDataDetail");
	

if (form == null || !module.hasPermission(moduleUser.getId(),form.getId()) ) {
%>
<c:redirect url = "frwApproveFormData.jsp"/>
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

     <x:display name="formDataDetailPage.formDataDetailPortlet" ></x:display>

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


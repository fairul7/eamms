<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.FormDataDetail,
                 com.tms.collab.formwizard.model.FormModule"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="ekmsFormDataDetailPage">
        	<com.tms.collab.formwizard.ui.FormDataDetail name="ekmsFormDataDetail"/>
    </page>
</x:config>

<c:if test="${!empty param.formUid}">
	<x:set name="ekmsFormDataDetailPage.ekmsFormDataDetail" property="id" value="${param.formUid}" />
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
	FormDataDetail form = (FormDataDetail)wm.getWidget("ekmsFormDataDetailPage.ekmsFormDataDetail");
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
	form = (FormDataDetail)wm.getWidget("ekmsFormDataDetailPage.ekmsFormDataDetail");


if (form == null || !module.hasPermission(moduleUser.getId(),form.getId()) ) {
%>
<c:redirect url = "frwApproveFormData.jsp"/>
<%
}
%>





<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;
      Submitted Form Approval
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
                 <x:display name="ekmsFormDataDetailPage.ekmsFormDataDetail" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>





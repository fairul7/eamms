<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.AddForm,
                 com.tms.collab.formwizard.model.FormConstants"%>
<%@ include file="/common/header.jsp" %>

<c-rt:set var="addPermissionConst" value="<%= FormConstants.FORM_ADD_PERMISSION_ID %>" />

<x:permission permission="com.tms.collab.formwizard.Add" module="com.tms.collab.formwizard.model.FormModule" url="noPermission.jsp" />

<!-- Declare Widgets -->
<x:config>
    <page name="addFormPage">
	    <portlet name="addFormPortlet" text="<fmt:message key='formWizard.label.newForm'/>" width="100%" permanent="true">
        	<com.tms.collab.formwizard.ui.AddForm name="addForm"/>
	    </portlet>
    </page>
</x:config>


<c:if test="${forward.name == 'formAdded'}">
<script>
<!--
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	AddForm form = (AddForm)wm.getWidget("addFormPage.addFormPortlet.addForm");
%>
    document.location = "<c:url value="frwAddFormField.jsp" />?formId=<%=form.getFormId()%>";
    myWin = window.open('frwEditPreviewForm.jsp?formId=<%=form.getFormId()%>','preview','scrollbars=yes,resizable=yes,width=450,height=380')
<%
}
%>
//-->
</script>
</c:if>




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


			     <x:display name="addFormPage.addFormPortlet" ></x:display>


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



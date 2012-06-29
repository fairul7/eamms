<%@ page import="com.tms.collab.formwizard.ui.AddFormTemplate"%>
<%@ include file="/common/header.jsp" %>


<x:permission permission="com.tms.collab.formwizard.Add" module="com.tms.collab.formwizard.model.FormModule" url="noPermission.jsp" />

<x:config>
    <page name="addFormTemplatePage">
	    <portlet name="addFormTemplatePortlet" text="New Form Template" width="100%" permanent="true">
        	<com.tms.collab.formwizard.ui.AddFormTemplate name="addFormTemplate"/>
	    </portlet>
    </page>
</x:config>




<c:if test="${forward.name == 'formTemplateAdded'}">
<%
    WidgetManager wm = (WidgetManager) session.getAttribute("WidgetManager");

    if (wm != null) {
        AddFormTemplate form = (AddFormTemplate) wm.getWidget("addFormTemplatePage.addFormTemplatePortlet.addFormTemplate");
%>
        <c-rt:set var="templateId" value="<%= form.getTemplateId() %>" />
        <c:redirect url = "frwAddFormTemplateField.jsp?formTemplateId=${templateId}" />
<%
    }
%>
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


			         <x:display name="addFormTemplatePage.addFormTemplatePortlet" ></x:display>


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




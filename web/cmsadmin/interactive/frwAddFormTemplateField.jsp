<%@ page import="com.tms.collab.formwizard.ui.AddFormTemplateField"%>
<%@ include file="/common/header.jsp" %>


<x:permission permission="com.tms.collab.formwizard.Add" module="com.tms.collab.formwizard.model.FormModule" url="noPermission.jsp" />

<x:config>
    <page name="addFormTemplateFieldPage">
	    <portlet name="addFormTemplateFieldPortlet" text="New Form Template Field" width="100%" permanent="true">
        	<com.tms.collab.formwizard.ui.AddFormTemplateField name="addFormTemplateField"/>
	    </portlet>
    </page>
</x:config>

<x:set name="addFormTemplateFieldPage.addFormTemplateFieldPortlet.addFormTemplateField" property="fieldFormAbsoluteName" value="addG2FieldFormPage.addG2FieldForm" />

<c:if test="${!empty param.formTemplateId}">
    <x:set name="addFormTemplateFieldPage.addFormTemplateFieldPortlet.addFormTemplateField" property="formTemplateId" value="${param.formTemplateId}"/>
</c:if>

<c:if test="${forward.name == 'formTemplateFieldAdded'}">
<script>
<!--
<%
    WidgetManager wm = (WidgetManager) session.getAttribute("WidgetManager");

    if (wm != null) {
        AddFormTemplateField form = (AddFormTemplateField) wm.getWidget("addFormTemplateFieldPage.addFormTemplateFieldPortlet.addFormTemplateField");
%>

        document.location = "<c:url value="frwAddFormTemplateField.jsp?formTemplateId=" /><%= form.getFormTemplateId() %>";
        myWin = window.open('frwTemplatePreviewForm.jsp?formTemplateId=<%= form.getFormTemplateId() %>','preview','scrollbars=yes,resizable=yes,width=700,height=500');
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



                   <x:display name="addFormTemplateFieldPage.addFormTemplateFieldPortlet" ></x:display>


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




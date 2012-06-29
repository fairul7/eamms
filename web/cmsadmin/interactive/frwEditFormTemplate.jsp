
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.collab.formwizard.Add" module="com.tms.collab.formwizard.model.FormModule" url="noPermission.jsp" />

<x:config>
    <page name="editFormTemplatePage">
	     <portlet name="editFormTemplatePortlet" text="Edit Template" width="100%" permanent="true">
	          <com.tms.collab.formwizard.ui.EditFormTemplate name="editFormTemplate"/>
	    </portlet>
    </page>
</x:config>

<c:if test="${!empty param.formTemplateId}">
    <x:set name="editFormTemplatePage.editFormTemplatePortlet.editFormTemplate" property="templateId" value="${param.formTemplateId}" />
</c:if>

<c:if test="${forward.name == 'formTemplateEdited'}">
    <c:redirect url="frwTemplatesEdit.jsp"/>
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



           <x:display name="editFormTemplatePage.editFormTemplatePortlet" ></x:display>





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





<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.collab.formwizard.Edit" module="com.tms.collab.formwizard.model.FormModule" url="noPermission.jsp" />

<!-- Declare Widgets -->
<x:config>
    <page name="formsEditPage">
	     <portlet name="formsEditPortlet" text="Edit | Delete Form" width="100%" permanent="true">
	          <com.tms.collab.formwizard.ui.FormsEdit name="formsEdit"/>
	    </portlet>
    </page>
</x:config>

<c:if test="${!empty param.formId}">
    <c:redirect url="frwEditForm.jsp?formId=${param.formId}" />
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





     <x:display name="formsEditPage.formsEditPortlet" ></x:display>



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





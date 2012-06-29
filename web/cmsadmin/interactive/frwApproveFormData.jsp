<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.ApproveFormData"%>
<%@ include file="/common/header.jsp" %>

<!-- Declare Widgets -->
<x:config>
    <page name="approveFormDataPage">
	    <portlet name="approveFormDataPortlet" text="Submitted Form Approval" width="100%" permanent="true">
        	<com.tms.collab.formwizard.ui.ApproveFormData name="approveFormData"/>
        </portlet>  
    </page>
</x:config>

<c:if test="${!empty param.formUid}">
    <c:redirect url="frwFormDataDetail.jsp?formUid=${param.formUid}" />
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


     <x:display name="approveFormDataPage.approveFormDataPortlet" ></x:display>


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

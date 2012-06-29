<%@ page import="com.tms.collab.formwizard.model.FormModule"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="resubmitFormDataPage">
	     <portlet name="resubmitFormDataPortlet" text="" width="100%" permanent="true">
	          <com.tms.collab.formwizard.ui.ResubmitFormData name="resubmitFormData"/>
              <com.tms.collab.formwizard.ui.DynamicResubmitFormDataField name="dynamicResubmitFormDataField"/>
	    </portlet>
    </page>
</x:config>

<c:if test="${!empty param.id}">
	<x:set name = "resubmitFormDataPage.resubmitFormDataPortlet.resubmitFormData" property = "id" value="${param.id}"/>
    <x:set name = "resubmitFormDataPage.resubmitFormDataPortlet.dynamicResubmitFormDataField" property = "id" value="${param.id}"/>
</c:if>

<c:if test="${forward.name == 'cancel' || forward.name == 'dataResumitted'}">
	<c:redirect url = "frwStatusReport.jsp" />
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

     <x:display name="resubmitFormDataPage.resubmitFormDataPortlet" ></x:display>



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



<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="formSubmitDraftPage">
	    <portlet name="formSubmitDraftPortlet" text="Draft" width="100%" permanent="true">
        	<com.tms.collab.formwizard.ui.SubmitDraft name="submitDraft"/>
            <com.tms.collab.formwizard.ui.DynamicSubmitDraftField name="dynamicSubmitDraftField"/>
	    </portlet>
    </page>
</x:config>

<c:if test="${!empty param.formUid}">
    <x:set name = "formSubmitDraftPage.formSubmitDraftPortlet.dynamicSubmitDraftField" property = "id" value="${param.formUid}"/>
    <x:set name = "formSubmitDraftPage.formSubmitDraftPortlet.submitDraft" property = "id" value="${param.formUid}"/>
</c:if>

<c:if test="${forward.name == 'dataDrafted'}">
    <c:redirect url="frwFormsDraft.jsp" />
</c:if>

<c:if test="${forward.name == 'submissionMessage'}">
    <c:set var="formId" value="${widgets['formSubmitDraftPage.formSubmitDraftPortlet.submitDraft'].formId}"/>
    <c:redirect url="frwFormMessagePanel.jsp?reload=true&formId=${formId}" />
</c:if>

<c:if test="${forward.name == 'formLink'}">
    <c:set var="formId" value="${widgets['formSubmitDraftPage.formSubmitDraftPortlet.dynamicSubmitDraftField'].formId}"/>
    <c:redirect url="frwViewForm.jsp?formId=${formId}" />
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


                  <x:display name="formSubmitDraftPage.formSubmitDraftPortlet" ></x:display>


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

<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="ekmsFormSubmitDraftPage">
        	<com.tms.collab.formwizard.ui.SubmitDraft name="ekmsSubmitDraft"/>
            <com.tms.collab.formwizard.ui.DynamicSubmitDraftField name="ekmsDynamicSubmitDraftField"/>
    </page>
</x:config>

<c:if test="${!empty param.formUid}">
    <x:set name = "ekmsFormSubmitDraftPage.ekmsSubmitDraft" property = "id" value="${param.formUid}"/>
    <x:set name = "ekmsFormSubmitDraftPage.ekmsDynamicSubmitDraftField" property = "id" value="${param.formUid}"/>
</c:if>

<c:if test="${forward.name == 'dataDrafted'}">
    <c:redirect url="frwFormsDraft.jsp" />
</c:if>

<c:if test="${forward.name == 'submissionMessage'}">
    <c:set var="formId" value="${widgets['ekmsFormSubmitDraftPage.ekmsSubmitDraft'].formId}"/>
    <c:redirect url="frwFormMessagePanel.jsp?reload=true&formId=${formId}" />
</c:if>

<c:if test="${forward.name == 'formLink'}">
    <c:set var="formId" value="${widgets['ekmsFormSubmitDraftPage.ekmsDynamicSubmitDraftField'].formId}"/>
    <c:redirect url="frwViewForm.jsp?formId=${formId}" />
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;
      <fmt:message key="formWizard.label.draftSubmission"/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
			     <x:display name="ekmsFormSubmitDraftPage.ekmsSubmitDraft" ></x:display>
                 <x:display name="ekmsFormSubmitDraftPage.ekmsDynamicSubmitDraftField" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>
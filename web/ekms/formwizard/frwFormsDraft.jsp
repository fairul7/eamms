<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="ekmsFormsDraftPage">
        	<com.tms.collab.formwizard.ui.FormsDraft name="ekmsFormsDraft"/>
    </page>
</x:config>

<c:if test="${!empty param.formUid}">
    <c:redirect url = "frwSubmitDraft.jsp?formUid=${param.formUid}" />
</c:if>

<c:if test="${forward.name == 'dataDrafted'}">
    <c:redirect url="frwFormsView.jsp" />
</c:if>




<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='formWizard.label.personal'/> > 
      <fmt:message key='formWizard.label.draftSubmission'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
			     <x:display name="ekmsFormsDraftPage.ekmsFormsDraft" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>
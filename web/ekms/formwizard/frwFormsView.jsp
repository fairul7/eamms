<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="ekmsFormsViewPage">
        	<com.tms.collab.formwizard.ui.FormsView name="ekmsFormsView"/>
    </page>
</x:config>

<c:if test="${!empty param.formId}">
    <c:redirect url="frwViewForm.jsp?formId=${param.formId}" />
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;
        <fmt:message key='formWizard.label.formWizard'/> > 
      <fmt:message key='formWizard.label.formListing'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="ekmsFormsViewPage.ekmsFormsView" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>

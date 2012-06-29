<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.ApproveFormData"%>
<%@ include file="/common/header.jsp" %>

<!-- Declare Widgets -->
<x:config>
    <page name="ekmsApproveFormDataPage">
        	<com.tms.collab.formwizard.ui.ApproveFormData name="ekmsApproveFormData"/>
    </page>
</x:config>

<c:if test="${!empty param.formUid}">
    <c:redirect url="frwFormDataDetail.jsp?formUid=${param.formUid}" />
</c:if>


<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='formWizard.label.workflow'/> > 
      <fmt:message key='formWizard.label.submittedFormApproval'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
                 <x:display name="ekmsApproveFormDataPage.ekmsApproveFormData" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>

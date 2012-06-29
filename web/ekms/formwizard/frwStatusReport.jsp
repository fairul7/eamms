<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="ekmsStatusReportTablePage">
    		<com.tms.collab.formwizard.ui.StatusReportTable name="ekmsStatusReportTable"/>
    </page>
</x:config>


<c:if test="${!empty param.formUid}">
	<c:redirect url = "frwResubmitData.jsp?id=${param.formUid}" />
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='formWizard.label.workflow'/> > 
      <fmt:message key='formWizard.label.submittedFormStatus'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
             <x:display name="ekmsStatusReportTablePage.ekmsStatusReportTable" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>

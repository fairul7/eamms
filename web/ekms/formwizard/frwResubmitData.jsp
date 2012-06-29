<%@ page import="com.tms.collab.formwizard.model.FormModule"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="ekmsResubmitFormDataPage">
	          <com.tms.collab.formwizard.ui.ResubmitFormData name="ekmsResubmitFormData"/>
              <com.tms.collab.formwizard.ui.DynamicResubmitFormDataField name="ekmsDynamicResubmitFormDataField"/>
    </page>
</x:config>

<c:if test="${!empty param.id}">
	<x:set name = "ekmsResubmitFormDataPage.ekmsResubmitFormData" property = "id" value="${param.id}"/>
    <x:set name = "ekmsResubmitFormDataPage.ekmsDynamicResubmitFormDataField" property = "id" value="${param.id}"/>
</c:if>

<c:if test="${forward.name == 'cancel' || forward.name == 'dataResumitted'}">
	<c:redirect url = "frwStatusReport.jsp" />
</c:if>


<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;      
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
             <x:display name="ekmsResubmitFormDataPage.ekmsResubmitFormData" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>


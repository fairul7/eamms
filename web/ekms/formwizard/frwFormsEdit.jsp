<%--
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

--%>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.collab.formwizard.Edit" module="com.tms.collab.formwizard.model.FormModule" url="noPermission.jsp" />

<!-- Declare Widgets -->
<x:config>
    <page name="ekmsFormsEditPage">
	          <com.tms.collab.formwizard.ui.FormsEdit name="ekmsFormsEdit"/>
    </page>
</x:config>

<c:if test="${!empty param.formId}">
    <c:redirect url="frwEditForm.jsp?formId=${param.formId}" />
</c:if>


<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;  <fmt:message key='formWizard.label.formWizard'/> > 
      <fmt:message key='formWizard.label.editdelete'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="ekmsFormsEditPage.ekmsFormsEdit" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>

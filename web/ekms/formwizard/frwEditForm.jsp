<%--<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>--%>
<%@ include file="/common/header.jsp" %>

<!-- Declare Widgets -->
<x:config>
    <page name="ekmsEditFormPage">
        	<com.tms.collab.formwizard.ui.EditForm name="ekmsEditForm"/>
    </page>
</x:config>

<c:if test="${forward.name == 'formEdited'}">
	<c:redirect url = "frwFormsEdit.jsp" />
</c:if>

<c:if test="${!empty param.formId}">
	<x:set name="ekmsEditFormPage.ekmsEditForm" property="formId" value="${param.formId}"/>
</c:if>


<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;  <fmt:message key='formWizard.label.formWizard'/> > 
      <fmt:message key='formWizard.label.editForm'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
     <x:display name="ekmsEditFormPage.ekmsEditForm" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>

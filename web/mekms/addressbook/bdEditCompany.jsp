<%@ page import="com.tms.collab.directory.ui.CompanyNewForm"%>
<%@include file="/common/header.jsp" %>

<x:config>
    <page name="bdEditCompany">
        <com.tms.collab.directory.ui.CompanyEditForm name="form" />
    </page>
</x:config>

<%@include file="includes/folderTreeAction.jsp" %>

<c:if test="${!empty param.id}">
    <x:set name="bdEditCompany.form" property="id" value="${param.id}"/>
</c:if>

<c:if test="${!empty param['button*bdEditCompany.form.buttonPanel.cancel_form_action']}">
    <c:redirect url="bdCompanyList.jsp" />
</c:if>

<c-rt:set var="forwardSuccess" value="<%= CompanyNewForm.FORWARD_SUCCESS %>"/>
<c:if test="${forward.name eq forwardSuccess}">
    <c:redirect url="bdCompanyList.jsp" />
</c:if>

<c-rt:set var="forwardPending" value="<%= CompanyNewForm.FORWARD_PENDING %>"/>
<c:if test="${forward.name eq forwardPending}">
    <script>
    <!--
        alert("Company submitted for approval");
        location.href="bdCompanyList.jsp";
    //-->
    </script>
</c:if>

<c-rt:set var="forwardError" value="<%= CompanyNewForm.FORWARD_ERROR %>" />
<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>

<%@include file="includes/header.jsp" %>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='addressbook.label.businessDirectoryEditCompany'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP">
        <x:display name="bdEditCompany.form" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>


<%@include file="includes/footer.jsp" %>

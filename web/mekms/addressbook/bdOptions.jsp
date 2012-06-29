<%@ page import="com.tms.collab.directory.ui.ContactCsvImportForm"%>
<%@include file="/common/header.jsp" %>

<x:config>
    <page name="bdOptions">
        <com.tms.collab.directory.ui.ContactCsvImportForm name="form" type="DirectoryModule"/>
    </page>
</x:config>

<%@include file="includes/folderTreeAction.jsp" %>

<c:if test="${!empty param['button*bdOptions.form.tmpPanel.cancel_form_action']}">
    <c:redirect url="bdContactList.jsp" />
</c:if>

<c-rt:set var="forwardError" value="<%= ContactCsvImportForm.FORWARD_ERROR %>" />
<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>

<%@include file="includes/header.jsp" %>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='addressbook.label.businessDirectoryOptions'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        &nbsp;<strong><fmt:message key='addressbook.label.importCSVFile'/></strong>
        <c:if test="${!empty requestScope['ContactCsvImportForm.importCount']}">
            <blockquote>
            <c:out value="${requestScope['ContactCsvImportForm.importCount']}"/> <fmt:message key='addressbook.label.contactsimported'/>!
            </blockquote>
        </c:if>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP">
        <x:display name="bdOptions.form" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>


<%@include file="includes/footer.jsp" %>

<%@ page import="com.tms.collab.directory.ui.FolderNewForm,
                 com.tms.collab.directory.ui.ContactNewForm"%>
<%@include file="/common/header.jsp" %>

<x:config>
    <page name="abEditContact">
        <com.tms.collab.directory.ui.ContactEditForm name="form"/>
    </page>
</x:config>

<%@include file="includes/folderTreeAction.jsp" %>

<c:if test="${!empty param.id}">
    <x:set name="abEditContact.form" property="id" value="${param.id}"/>
</c:if>

<c:if test="${!empty param['button*abEditContact.form.buttonPanel.cancel_form_action']}">
    <c:redirect url="abContactList.jsp" />
</c:if>

<c-rt:set var="forwardSuccess" value="<%= ContactNewForm.FORWARD_SUCCESS %>"/>
<c:if test="${forward.name eq forwardSuccess}">
    <c:redirect url="abContactList.jsp" />
</c:if>

<c-rt:set var="forwardError" value="<%= ContactNewForm.FORWARD_ERROR %>" />
<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>

<%@include file="includes/header.jsp" %>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='addressbook.label.editPersonalContact'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP">
        <x:display name="abEditContact.form" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>


<%@include file="includes/footer.jsp" %>

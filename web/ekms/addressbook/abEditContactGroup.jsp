<%@ page import="com.tms.collab.directory.ui.ContactNewForm"%>
<%@include file="/common/header.jsp" %>

<x:config>
    <page name="abEditContactGroup">
        <com.tms.collab.directory.ui.ContactGroupEditForm name="form" />
    </page>
</x:config>

<%@include file="includes/folderTreeAction.jsp" %>

<c:if test="${!empty param.id}">
    <x:set name="abEditContactGroup.form" property="id" value="${param.id}"/>
</c:if>

<c:if test="${!empty param['button*abEditContactGroup.form.buttonPanel.cancel_form_action']}">
    <c:redirect url="abViewContact.jsp?id=${widgets['abEditContactGroup.form'].id}" />
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
      &nbsp;<fmt:message key='addressbook.label.personalContacts'/> > <fmt:message key='addressbook.label.editContactGroup'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP">
        <x:display name="abEditContactGroup.form" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>


<%@include file="includes/footer.jsp" %>

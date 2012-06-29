<%@ page import="com.tms.collab.directory.ui.DirectoryContactNewForm"%>
<%@include file="/common/header.jsp" %>

<x:config>
    <page name="bdNewContactGroup">
        <com.tms.collab.directory.ui.DirectoryContactGroupNewForm name="form" />
    </page>
</x:config>

<%@include file="includes/folderTreeAction.jsp" %>

<c:if test="${!empty param['button*bdNewContactGroup.form.buttonPanel.cancel_form_action']}">
    <c:redirect url="bdContactList.jsp" />
</c:if>

<c-rt:set var="forwardSuccess" value="<%= DirectoryContactNewForm.FORWARD_SUCCESS %>"/>
<c:if test="${forward.name eq forwardSuccess}">
    <c:redirect url="bdContactList.jsp" />
</c:if>

<c-rt:set var="forwardPending" value="<%= DirectoryContactNewForm.FORWARD_PENDING %>"/>
<c:if test="${forward.name eq forwardPending}">
    <script>
    <!--
        alert("<fmt:message key="addressbook.label.contactSubmitted"/>");
        location.href="bdContactList.jsp";
    //-->
    </script>
</c:if>

<c-rt:set var="forwardError" value="<%= DirectoryContactNewForm.FORWARD_ERROR %>" />
<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>

<%@include file="includes/header.jsp" %>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='addressbook.label.menu.businessDirectory'/> > <fmt:message key='addressbook.label.businessDirectoryNewContactGroup'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP">
        <x:display name="bdNewContactGroup.form" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>


<%@include file="includes/footer.jsp" %>

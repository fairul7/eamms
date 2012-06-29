<%@ page import="com.tms.collab.directory.ui.FolderNewForm,
                 com.tms.collab.directory.ui.ContactTable,
                 com.tms.collab.directory.ui.DirectoryContactTable"%>
<%@include file="/common/header.jsp" %>

<x:config>
    <page name="bdContactList">
        <com.tms.collab.directory.ui.DirectoryContactTable name="table" sort="firstName" />
    </page>
</x:config>

<%@include file="includes/folderTreeAction.jsp" %>

<c-rt:set var="forwardNewContact" value="<%= DirectoryContactTable.FORWARD_NEW_CONTACT %>"/>
<c:if test="${forward.name eq forwardNewContact}">
    <c:redirect url="bdNewContact.jsp" />
</c:if>

<c-rt:set var="forwardNewContactGroup" value="<%= DirectoryContactTable.FORWARD_NEW_CONTACT_GROUP %>"/>
<c:if test="${forward.name eq forwardNewContactGroup}">
    <c:redirect url="bdNewContactGroup.jsp" />
</c:if>

<c-rt:set var="forwardSendMessage" value="<%= DirectoryContactTable.FORWARD_SEND_MESSAGE %>"/>
<c:if test="${forward.name eq forwardSendMessage}">
    <c:redirect url="/ekms/messaging/composeMessage.jsp" />
</c:if>

<c-rt:set var="forwardError" value="<%= DirectoryContactTable.FORWARD_ERROR %>" />
<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>

<c:if test="${!empty param.id}">
    <c:redirect url="bdViewContact.jsp?id=${param.id}"/>
</c:if>

<c:if test="${param.query != null}">
    <x:set name="bdContactList.table" property="query" value="${param.query}" />
</c:if>

<c:if test="${param.folderId != null}">
    <x:set name="bdContactList.table" property="selectedFolder" value="${param.folderId}" />
</c:if>

<c:if test="${param.companyId != null}">
    <x:set name="bdContactList.table" property="selectedCompany" value="${param.companyId}" />
</c:if>

<%@include file="includes/header.jsp" %>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='addressbook.label.menu.businessDirectory'/> > <fmt:message key='addressbook.label.businessDirectoryContacts'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="bdContactList.table" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>


<%@include file="includes/footer.jsp" %>

<%@ page import="com.tms.collab.directory.ui.FolderNewForm,
                 com.tms.collab.directory.ui.ContactTable"%>
<%@include file="/common/header.jsp" %>

<x:config>
    <page name="udContactList">
        <com.tms.collab.directory.ui.UserTable name="table" sort="username" />
    </page>
</x:config>

<%@include file="includes/folderTreeAction.jsp" %>

<c-rt:set var="forwardSendMessage" value="<%= ContactTable.FORWARD_SEND_MESSAGE %>"/>
<c:if test="${forward.name eq forwardSendMessage}">
    <c:redirect url="/ekms/messaging/composeMessage.jsp" />
</c:if>

<c-rt:set var="forwardError" value="<%= ContactTable.FORWARD_ERROR %>" />
<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>

<c:if test="${!empty param.id}">
    <c:redirect url="udViewContact.jsp?id=${param.id}"/>
</c:if>

<c:if test="${!empty param.query}">
    <x:set name="udContactList.table" property="query" value="${param.query}" />
</c:if>

<c:if test="${!empty param.groupId}">
    <x:set name="udContactList.table" property="selectedGroup" value="${param.groupId}" />
</c:if>

<%@include file="includes/header.jsp" %>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='addressbook.label.intranetUsers'/> > <fmt:message key='addressbook.label.menu.users'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="udContactList.table" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>


<%@include file="includes/footer.jsp" %>

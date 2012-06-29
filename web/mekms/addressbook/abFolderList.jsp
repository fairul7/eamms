<%@ page import="com.tms.collab.directory.ui.FolderNewForm,
                 com.tms.collab.directory.ui.FolderTable"%>
<%@include file="/common/header.jsp" %>

<x:config>
    <page name="abFolderList">
        <com.tms.collab.directory.ui.FolderTable name="table" sort="name" />
    </page>
</x:config>

<%@include file="includes/folderTreeAction.jsp" %>

<c-rt:set var="forwardNewFolder" value="<%= FolderTable.FORWARD_NEW_FOLDER %>"/>
<c:if test="${forward.name eq forwardNewFolder}">
    <c:redirect url="abNewFolder.jsp" />
</c:if>

<c-rt:set var="forwardSendMessage" value="<%= FolderTable.FORWARD_SEND_MESSAGE %>"/>
<c:if test="${forward.name eq forwardSendMessage}">
    <c:redirect url="/ekms/messaging/composeMessage.jsp" />
</c:if>

<c-rt:set var="forwardError" value="<%= FolderTable.FORWARD_ERROR %>" />
<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>

<c:if test="${!empty param.id}">
    <c:redirect url="abEditFolder.jsp?id=${param.id}"/>
</c:if>

<%@include file="includes/header.jsp" %>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='addressbook.label.personalFolders'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="abFolderList.table" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>


<%@include file="includes/footer.jsp" %>

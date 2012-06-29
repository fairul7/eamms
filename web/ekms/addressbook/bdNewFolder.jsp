<%@ page import="com.tms.collab.directory.ui.FolderNewForm,
                 com.tms.collab.directory.model.DirectoryModule"%>
<%@include file="/common/header.jsp" %>

<x:permission module="<%= DirectoryModule.class.getName() %>" permission="<%= DirectoryModule.PERMISSION_MANAGE_FOLDERS %>" url="noPermission.jsp" />

<x:config>
    <page name="bdNewFolder">
        <com.tms.collab.directory.ui.FolderNewForm name="form" type="DirectoryModule"/>
    </page>
</x:config>

<%@include file="includes/folderTreeAction.jsp" %>

<c:if test="${!empty param['button*bdNewFolder.form.buttonPanel.cancel_form_action']}">
    <c:redirect url="bdFolderList.jsp" />
</c:if>

<c-rt:set var="forwardSuccess" value="<%= FolderNewForm.FORWARD_SUCCESS %>"/>
<c:if test="${forward.name eq forwardSuccess}">
    <c:redirect url="bdFolderList.jsp" />
</c:if>

<c-rt:set var="forwardError" value="<%= FolderNewForm.FORWARD_ERROR %>" />
<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>

<%@include file="includes/header.jsp" %>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='addressbook.label.personalFolders'/> > <fmt:message key='addressbook.label.businessDirectoryNewFolder'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP">
        <x:display name="bdNewFolder.form" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>


<%@include file="includes/footer.jsp" %>

<%@ page import="com.tms.collab.directory.ui.FolderNewForm"%>
<%@include file="/common/header.jsp" %>

<x:config>
    <page name="abEditFolder">
        <com.tms.collab.directory.ui.FolderEditForm name="form"/>
    </page>
</x:config>

<%@include file="includes/folderTreeAction.jsp" %>

<c:if test="${!empty param.id}">
    <x:set name="abEditFolder.form" property="id" value="${param.id}"/>
</c:if>

<c:if test="${!empty param['button*abEditFolder.form.buttonPanel.cancel_form_action']}">
    <c:redirect url="abFolderList.jsp" />
</c:if>

<c-rt:set var="forwardSuccess" value="<%= FolderNewForm.FORWARD_SUCCESS %>"/>
<c:if test="${forward.name eq forwardSuccess}">
    <c:redirect url="abFolderList.jsp" />
</c:if>

<c-rt:set var="forwardError" value="<%= FolderNewForm.FORWARD_ERROR %>" />
<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>

<%@include file="includes/header.jsp" %>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;
      Folders > Edit Personal Folder
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP">
        <x:display name="abEditFolder.form" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>


<%@include file="includes/footer.jsp" %>
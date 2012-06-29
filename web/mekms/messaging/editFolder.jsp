<%@include file="includes/taglib.jsp" %>
 <%@ page import="com.tms.collab.messaging.ui.EditFolderForm"%>

<x:config>
    <page name="editFolderPage">
        <com.tms.collab.messaging.ui.EditFolderForm name="form"/>
    </page>
</x:config>

<c-rt:set var="forwardSuccess" value="<%= EditFolderForm.FORWARD_SUCCESS %>"/>
<c:if test="${forward.name eq forwardSuccess}">
    <c:redirect url="folderList.jsp" />
</c:if>

<c-rt:set var="forwardError" value="<%= EditFolderForm.FORWARD_ERROR %>" />
<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>

<%@include file="includes/header.jsp" %>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='messaging.label.editFolder'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP">
        <x:display name="editFolderPage.form" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>

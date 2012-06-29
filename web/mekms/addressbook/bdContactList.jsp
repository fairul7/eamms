<%@ page import="com.tms.collab.directory.ui.FolderNewForm,
                 com.tms.collab.directory.ui.ContactTable,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp" %>

<x:config>
    <page name="bdContactList">
        <com.tms.collab.directory.ui.MDirectoryContactTable name="table" sort="firstName" />
    </page>
</x:config>

<%--
<%@include file="includes/folderTreeAction.jsp" %>
--%>

<c-rt:set var="forwardNewContact" value="<%= ContactTable.FORWARD_NEW_CONTACT %>"/>
<c:if test="${forward.name eq forwardNewContact}">
    <c:redirect url="bdNewContact.jsp" />
</c:if>

<c-rt:set var="forwardSendMessage" value="<%= ContactTable.FORWARD_SEND_MESSAGE %>"/>
<c:if test="${forward.name eq forwardSendMessage}">
    <c:redirect url="/mekms/messaging/composeMessage.jsp" />
</c:if>

<c-rt:set var="forwardError" value="<%= ContactTable.FORWARD_ERROR %>" />
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

<%@include file="../includes/mheader.jsp"%>
  <jsp:include page="/ekms/init.jsp"/>
  <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
  <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
  <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">




<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='addressbook.label.businessDirectoryContacts'/>
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


<%@include file="../includes/mfooter.jsp"%>

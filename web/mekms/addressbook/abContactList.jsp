<%@ page import="com.tms.collab.directory.ui.FolderNewForm,
                 com.tms.collab.directory.ui.ContactTable,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp" %>

<x:config>
    <page name="abContactList">
        <com.tms.collab.directory.ui.MContactTable name="table" sort="firstName" />
    </page>
</x:config>

<%--
<%@include file="includes/folderTreeAction.jsp" %>
--%>

<c-rt:set var="forwardNewContact" value="<%= ContactTable.FORWARD_NEW_CONTACT %>"/>
<c:if test="${forward.name eq forwardNewContact}">
    <c:redirect url="abNewContact.jsp" />
</c:if>

<c-rt:set var="forwardSendMessage" value="<%= ContactTable.FORWARD_SEND_MESSAGE %>"/>
<c:if test="${forward.name eq forwardSendMessage}">
    <c:redirect url="/ekms/messaging/composeMessage.jsp" />
</c:if>

<c-rt:set var="forwardError" value="<%= ContactTable.FORWARD_ERROR %>" />
<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>

<c:if test="${!empty param.id}">
    <c:redirect url="abViewContact.jsp?id=${param.id}"/>
</c:if>

<c:if test="${!empty param.query}">
    <x:set name="abContactList.table" property="query" value="${param.query}" />
</c:if>

<c:if test="${!empty param.folderId}">
    <x:set name="abContactList.table" property="selectedFolder" value="${param.folderId}" />
</c:if>

<%@include file="../includes/mheader.jsp"%>
  <jsp:include page="/ekms/init.jsp"/>
  <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
  <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
  <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">





<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='addressbook.label.personalContacts'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="abContactList.table" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>


<%@include file="../includes/mfooter.jsp"%>

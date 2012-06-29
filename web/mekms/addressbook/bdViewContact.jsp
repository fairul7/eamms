<%@ page import="com.tms.collab.directory.ui.FolderNewForm,
                 com.tms.collab.directory.ui.ContactNewForm,
                 com.tms.collab.directory.model.DirectoryModule,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp" %>

<x:permission var="editable" module="<%= DirectoryModule.class.getName() %>" permission="<%= DirectoryModule.PERMISSION_MANAGE_CONTACTS %>" />

<x:config>
    <page name="bdViewContact">
        <com.tms.collab.directory.ui.ContactView name="form" type="DirectoryModule" />
    </page>
</x:config>

<%--
<%@include file="includes/folderTreeAction.jsp" %>
--%>

<c:if test="${forward.name == 'viewIntranet'}">
    <c:redirect url="/mekms/addressbook/udViewContact.jsp?id=${param.id}" />
</c:if>
<c:if test="${!empty param.id}">
    <x:set name="bdViewContact.form" property="id" value="${param.id}"/>
</c:if>


<%@include file="../includes/mheader.jsp"%>
  <jsp:include page="/ekms/init.jsp"/>
  <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
  <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
  <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">



<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='addressbook.label.businessDirectoryViewContact'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <%--display form--%>
  <tr>
    <td colspan="2" valign="TOP">
        <x:display name="bdViewContact.form" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <%--display buttons--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%">&nbsp;</td>
    <td class="contentBgColor">
<%--
        <c:if test="${editable || (widgets['bdViewContact.form'].contact.ownerId == sessionScope.currentUser.id)}">
            <input type="button" class="buttonClass" value="<fmt:message key='addressbook.label.edit'/>" onclick="location='bdEditContact.jsp?id=<c:out value="${widgets['bdViewContact.form'].id}" />'">
        </c:if>
--%>
        <input type="button" class="buttonClass" value="<fmt:message key='addressbook.label.cancel'/>" onclick="location='bdContactList.jsp'">
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>


<%@include file="../includes/mfooter.jsp"%>

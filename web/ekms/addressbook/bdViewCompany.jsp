<%@ page import="com.tms.collab.directory.ui.FolderNewForm,
                 com.tms.collab.directory.ui.ContactNewForm,
                 com.tms.collab.directory.ui.ContactTable"%>
<%@include file="/common/header.jsp" %>

<x:permission var="editable" scope="page" module="<%= DirectoryModule.class.getName() %>" permission="<%= DirectoryModule.PERMISSION_MANAGE_COMPANIES %>" />

<x:config>
    <page name="bdViewCompany">
        <com.tms.collab.directory.ui.CompanyView name="form" />
    </page>
</x:config>

<%@include file="includes/folderTreeAction.jsp" %>

<c:if test="${!empty param.id}">
    <x:set name="bdViewCompany.form" property="id" value="${param.id}"/>
</c:if>

<%@include file="includes/header.jsp" %>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='addressbook.label.businessDirectoryViewCompany'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <%--display form--%>
  <tr>
    <td colspan="2" valign="TOP">
        <x:display name="bdViewCompany.form" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <%--display buttons--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%">&nbsp;</td>
    <td class="contentBgColor">
        <c:if test="${editable || (widgets['bdViewCompany.form'].company.ownerId == sessionScope.currentUser.id)}">
            <input type="button" class="buttonClass" value="<fmt:message key='addressbook.label.edit'/>" onclick="location='bdEditCompany.jsp?id=<c:out value="${widgets['bdViewCompany.form'].id}" />'">
        </c:if>
        <input type="button" class="buttonClass" value="<fmt:message key='addressbook.label.contacts'/>" onclick="location='bdContactList.jsp?folderId=&companyId=<c:out value="${widgets['bdViewCompany.form'].id}" />'">
        <input type="button" class="buttonClass" value="<fmt:message key='addressbook.label.cancel'/>" onclick="location='bdCompanyList.jsp'">
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


<%@include file="includes/footer.jsp" %>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
<page name="contentOfficeEdit">
    <portlet name="editContentPortlet" text="<fmt:message key='general.label.editContent'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.ViewOfficeContentObjectPanel name="viewContentPanel" width="100%" />
        <com.tms.cms.core.ui.EditOfficeContentObjectPanel name="editContentPanel" width="100%" />
    </portlet>
</page>
</x:config>

<c:if test="${!empty param.id}">
    <x:set name="contentOfficeEdit.editContentPortlet.viewContentPanel" property="id" value="${param.id}"/>
    <x:set name="contentOfficeEdit.editContentPortlet.editContentPanel" property="id" value="${param.id}"/>
</c:if>


<html>
<head>
<link rel="stylesheet" href="<c:url value="/cmsadmin/styles/style.css"/>">
</head>
<body>

<fmt:message key="security.label.username"/>: <c:out value="${currentUser.username}"/>
<p>

<c:if test="${forward.name == 'save'}" >
<script>
    alert('<fmt:message key="cms.label.contentSaved"/>');
</script>
</c:if>

<c:if test="${forward.name == 'submit'}" >
    <c:redirect url="documentSubmitted.jsp"/>
</c:if>

<c:if test="${forward.name == 'approve'}" >
    <c:redirect url="documentApproved.jsp"/>
</c:if>

<c:if test="${forward.name == 'cancel_form_action'}" >
<script>
    window.close();
</script>
</c:if>

<TABLE width="100%" cellpadding=0 cellspacing=0 border=0 bgcolor="#333333" background="images/bannerbg.gif">
<TR>
	<TD width="407"><img src="../images/banner.gif"></TD>
	<TD valign="top" align="right">
	</TD>
	<TD bgcolor="#CCCCCC" width="1"><img src="../images/clear.gif" width="1"></TD>
	<TD align="right" valign="bottom">
	</TD>
</TR>
</TABLE>

<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top;">
        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;">
                <br>
                <p>
                  <%--Edit Content--%>
                  <x:display name="contentOfficeEdit.editContentPortlet"/>
                  <p><br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
              </td>
            </tr>
          </tbody>
        </table></td>
    </tr>
  </tbody>
</table>

</body>
</html>



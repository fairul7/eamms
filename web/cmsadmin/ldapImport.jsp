<%@ include file="/common/header.jsp" %>

<x:permission permission="kacang.services.security.User.add" module="kacang.services.security.SecurityService" url="security/noPermission.jsp" />

<x:config>
    <page name="ldapImport">
        <com.tms.util.security.LdapImportTable name="table" width="100%"/>
    </page>
</x:config>

<c:if test="${param.action == 'Connect'}">
    <x:set name="ldapImport.table" property="url" value="${param.url}"/>
    <x:set name="ldapImport.table" property="principal" value="${param.principal}"/>
    <x:set name="ldapImport.table" property="credentials" value="${param.credentials}"/>
    <x:set name="ldapImport.table" property="context" value="${param.context}"/>
</c:if>

<c:set var="table" value="${widgets['ldapImport.table']}"/>
<c:set var="url" value="${table.url}"/>
<c:set var="principal" value="${table.principal}"/>
<c:set var="credentials" value="${table.credentials}"/>
<c:set var="context" value="${table.context}"/>

<!-- Header -->
<HTML>
<HEAD>
<TITLE></TITLE>
    <link rel="stylesheet" href="styles/style.css">
</HEAD>
<BODY leftmargin="0" topmargin="0" marginheight="0" marginwidth="0" bgcolor="#E2E2E2">



<TABLE width="100%" cellpadding=0 cellspacing=0 border=0 bgcolor="#333333" background="images/bannerbg.gif">
<TR>
	<TD width="5">&nbsp;</TD>
	<TD valign="top" align="left">
        <font color="#00FF00" size="5"><b>LDAP/Active Directory Import</b></font>
	</TD>
	<TD bgcolor="#CCCCCC" width="1"><img src="images/clear.gif" width="1"></TD>
	<TD align="right" valign="bottom">
	</TD>
</TR>
</TABLE>
<img src="images/clear.gif" height="1"><br>

<!-- End Header -->

<table width="100%">
  <tr>
    <td>
        <b>Connection Settings</b>
        <table>
        <form method="post">
        <tr>
            <td width="100" valign="top">URL</td>
            <td><input name="url" size="50" value="<c:out value='${url}'/>"> ldap://host:389
            </td>
        </tr>
        <tr>
            <td valign="top">Username</td>
            <td><input name="principal" size="50" value="<c:out value='${principal}'/>">
                CN=username,CN=Users,DC=domain,DC=com or
                username@domain
            </td>
        </tr>
        <tr>
            <td valign="top">Password</td>
            <td><input type="password" size="50" name="credentials" value=""></td>
        </tr>
        <tr>
            <td valign="top">Context</td>
            <td><input name="context" size="50" value="<c:out value='${context}'/>"> CN=Users,DC=domain,DC=com
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td><input type="submit" name="action" value="Connect"></td>
        </tr>
        </form>
        </table>
    </td>
  </tr>
  <tr>
    <td>
        <x:display name="ldapImport.table"/>
    </td>
  </tr>
  <tr>
    <td>
        <c:out value="${widgets['ldapImport.table'].message}"/>
    </td>
  </tr>
</table>




</BODY>
</HTML>


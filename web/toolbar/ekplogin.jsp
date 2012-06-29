<%@ include file="/common/header.jsp" %>

<x:template type="com.tms.ekms.security.ui.LoginFormProcess" properties="forward=ekptoolbar.jsp" name="procLogin" />

<html>
<title>EKP</title>
<head>
<style>
<!--
body, td, div, a, input {font-family:Arial; font-size:6pt; font-weight:bold; text-decoration:none;}
//-->
</style>
<script>
<!--
    function doFocus() {
        document.forms["loginForm"].elements["loginUsername"].focus();
    }
//-->
</script>
</head>
<body leftmargin="0" topmargin="0" marginheight="0" marginwidth="0" bgcolor="lightblue" onload="doFocus()">
<table cellspacing="0" cellpadding="0" align="center">
<form name="loginForm" method="POST">
<tr>
<td valign="top">
<input type="text" tabindex="1" name="loginUsername" class="" size="5">
</td>
<td valign="top">
<input type="password" tabindex="2" name="loginPassword" class="" size="5">
</td>
<td valign="top">
<input type="submit" tabindex="3" value="&nbsp;&gt;&gt;&nbsp;">
</td>
</tr>
<input type="hidden" name="rememberPassword" value="1">
<input type="hidden" name="action" value="Login">
<input type="hidden" name="forward" value="<c:out value="${widget.forward}"/>">
</form>
</table>
</body>
</html>

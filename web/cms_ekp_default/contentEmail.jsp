<%@ include file="/common/header.jsp" %>

<x:template type="TemplateDisplaySetup" name="setup"/>

<html>
<head>
<title><c:out value='${setup.propertyMap.siteName}'/></title>
<link rel="stylesheet" href="<c:out value='${setup.propertyMap.siteStyleSheet}'/>">
</head>

<body>

<x:template type="TemplateProcessEmail" />

<form method="post" action="contentEmail.jsp?id=<c:out value='${param.id}'/>">
  <table border="0">
    <tr valign="top">
      <td><b><font face="arial, helvetica, sans-serif" size="2" class="tstextbold">Email To:</font></b></td>
      <td><input type="text" name="emailto" size="30" maxlength="255" value="<c:out value='${param.emailto}'/>"></td>
    </tr>
    <tr valign="top">
      <td><b><font face="arial, helvetica, sans-serif" size="2" class="tstextbold">Your Email:</font></b></td>
      <td><input type="text" name="emailfrom" size="30" maxlength="255" value="<c:out value='${param.emailfrom}'/>"></td>
    </tr>
    <tr valign="top">
      <td><b><font face="arial, helvetica, sans-serif" size="2" class="tstextbold">Comments:</font></b></td>
      <td><textarea name="comments" cols="30" rows="10" wrap="virtual"><c:out value='${param.comments}'/></textarea></td>
    </tr>
    <tr valign="top">
      <td>&nbsp;</td>
      <td>
        <input type="submit" class="button" name="action" value="Send">
        <input type="reset" value="Reset">
      </td>
    </tr>
  </table>
</form>

</body>
</html>


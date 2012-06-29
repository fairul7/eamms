<%@ page import="com.tms.util.license.TmsLicense,
                 com.tms.util.license.License,
                 java.util.Date"%>
<%@ include file="/common/header.jsp" %>

<x:template type="TemplateProcessLoginForm" properties="forward=index.jsp" />

<jsp:include page="/cmsadmin/blank.jsp" flush="true" />

<!-- Header -->
<HTML>
<HEAD>
<TITLE>Forbidden</TITLE>
    <link rel="stylesheet" href="<c:url value="/cmsadmin/styles/style.css"/>">
</HEAD>
<BODY leftmargin="0" topmargin="0" marginheight="0" marginwidth="0" bgcolor="#E2E2E2">



<TABLE width="100%" cellpadding=0 cellspacing=0 border=0 bgcolor="#333333" background="<c:url value="/cmsadmin/images/bannerbg.gif"/>">
<TR>
	<TD width="407"><img src="<c:url value="/cmsadmin/images/logo_ekp.gif"/>"></TD>
	<TD valign="top" align="right">
	</TD>
	<TD bgcolor="#CCCCCC" width="1"><img src="<c:url value="/cmsadmin/images/clear.gif"/>" width="1"></TD>
	<TD align="right" valign="bottom">
	</TD>
</TR>
</TABLE>
<img src="<c:url value="/cmsadmin/images/clear.gif"/>" height="1"><br>

<!-- End Header -->

<blockquote>
<table width="300">
  <tr>
    <td>
        <h2>Forbidden</h2>
        <hr size="1">
        The requested operation is not allowed.
    </td>
  </tr>
</table>
</blockquote>

</BODY>
</HTML>


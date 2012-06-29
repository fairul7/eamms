<%@ page import="com.tms.util.license.TmsLicense,
                 com.tms.util.license.License,
                 java.util.Date"%>
<%@ include file="/common/header.jsp" %>

<x:template type="TemplateProcessLoginForm" properties="forward=index.jsp" />

<jsp:include page="/cmsadmin/blank.jsp" flush="true" />

<!-- Header -->
<HTML>
<HEAD>
<TITLE><fmt:message key='cms.label.mainTitle'/></TITLE>
    <link rel="stylesheet" href="styles/style.css">
</HEAD>
<BODY leftmargin="0" topmargin="0" marginheight="0" marginwidth="0" bgcolor="#E2E2E2">



<TABLE width="100%" cellpadding=0 cellspacing=0 border=0 bgcolor="#333333" background="images/bannerbg.gif">
<TR>
	<TD width="407"><img src="images/banner.gif"></TD>
	<TD valign="top" align="right">
	</TD>
	<TD bgcolor="#CCCCCC" width="1"><img src="images/clear.gif" width="1"></TD>
	<TD align="right" valign="bottom">
	</TD>
</TR>
</TABLE>
<img src="images/clear.gif" height="1"><br>

<!-- End Header -->

<blockquote>
<table width="300">
  <tr>
    <td>
        <c-rt:set var="expiryDays" value="<%= TmsLicense.getLicenseValidityDays(new Date()) %>"/>
        <c:if test="${!empty expiryDays }">
            <div style="color:red"><fmt:message key="general.label.expiresIn"><fmt:param value="${expiryDays}"/></fmt:message></div>
        </c:if>
        <hr size="1">
        <fmt:message key='general.label.welcome'/>.<br>
        <fmt:message key='cms.label.loginUsername'/>
        <x:template type="TemplateDisplayLoginForm"/>
    </td>
  </tr>
</table>
</blockquote>

<script>
<!--
    function loginFocus() {
        try {
            document.forms['loginForm'].elements['loginUsername'].focus();
        }
        catch(e) {
        }
    }
    window.onload=loginFocus;
//-->
</script>

</BODY>
</HTML>


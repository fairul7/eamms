<%@ page import="com.tms.util.license.TmsLicense,
				 kacang.Application,
				 java.util.Date,
				 java.net.InetAddress,
				 com.tms.portlet.taglibs.PortalServerUtil,
				 java.net.URLEncoder"%>
<%@ include file="/common/header.jsp" %>
<script type="text/javascript" src="/ekms/messenger/js/cookiesCreation.js"></script>
<script>
	createCookie('creatingOpenPanel',false ,1);
</script>
<c:set var="strForward" value="${param.continue}"/>
<c:if test="${empty strForward}">
	<c:set var="strForward" value="home.jsp"/>
</c:if>
<%
	String strContinue = request.getParameter("continue");
	if(strContinue == null || "".equals(strContinue))
		strContinue = "home.jsp";
	else
		strContinue = URLEncoder.encode(strContinue);
	request.setAttribute("strContinue", strContinue);
%>
<x:template type="com.tms.ekms.security.ui.LoginFormProcess" properties="forward=${strContinue}" name="procLogin" />
<c:set var="showServerDetail"><%= Application.getInstance().getProperty("cluster.showServerDetail") %></c:set>
<html>
<head>
<title><fmt:message key="general.label.ekp"/></title>

<link rel="stylesheet" href="/ekms/images/fms2008/default.css">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<c:choose>
	<c:when test="${not empty (procLogin.message.loginUsername) or not empty (procLogin.message.loginPassword)}">
		<c:set var="errMessage"><fmt:message key="security.message.loginInvalid"/></c:set>
	</c:when>
	<c:when test="${not empty (procLogin.message.error)}">
		<c:set var="errMessage" value="${procLogin.message.error}"/>
	</c:when>
</c:choose>
<script>
<!--
    if (window != top) {
        top.location.href='login.jsp';
    }

    function loadFocus() {
        <c:if test="${not empty (errMessage)}">
            alert("<c:out value="${errMessage}"/>");
		</c:if>

        var obj = document.getElementById("loginUsername");
        if (obj != null && obj.value == '')
            obj.focus();
    }
//-->
</script>
</head>

<body onload="loadFocus()" style: bgcolor="#e9eccd" leftmargin="0" rightmargin="0" topmargin="0" marginheight="0" marginwidth="0">
<br><br><br><br><br><br><br><br><br><br>
<table border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td width="456" height="298" valign="top" class="login-bg"><br>
      <br>
      <table width="80%" border="0" align="center" cellpadding="0" cellspacing="5">
      <tr>
        <td width="95"><img src="images/fms2008/login_logo.png" width="90" height="42"></td>
        <td class="login-title">Facility Management System</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>  
      </table>
      <x:template type="com.tms.ekms.security.ui.LoginFormDisplay" properties="forward=home.jsp" body="custom">
      <form method="post" name="loginForm" id="loginForm">
      <table border="0" align="center" cellpadding="0" cellspacing="15">
        <tr>
          <td class="login-text"><fmt:message key="security.label.username"/></td>
          <td><label>
            <input name="loginUsername" type="text" class="login-field" id="loginUsername" size="24">
          </label></td>
        </tr>
        <tr>
          <td class="login-text"><fmt:message key="security.label.password"/></td>
          <td><input name="loginPassword" type="password" class="login-field" id="loginPassword" size="24">
          	<input type="hidden" name="action" value="Login">
			<input type="hidden" name="forward" value="<c:out value="${widget.forward}"/>"></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td class="login-smalltext"><a href="/register.jsp"><fmt:message key="security.label.register"/></a></td>
        </tr>
        <tr>
          <td>&nbsp;</td>                   
          <td><input type="image" src="images/fms2008/btn_login.png" name="image" width="83" height="30"><a href="javascript:document.loginForm.reset();"><img src="images/fms2008/btn_cancel.png" width="82" height="30" border="0"></a></td>          
        </tr>        
		<tr>                    
        </tr>
      </table>
      </form>
      </x:template></td>
  </tr>
  <tr>
    <td><img src="images/fms2008/login_shadow.gif" width="456" height="83"></td>
  </tr>
</table>
</body>
</html>

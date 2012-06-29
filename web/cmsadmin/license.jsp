<%@ page import="com.tms.util.license.TmsLicense,
                 com.tms.util.license.Activation,
                 com.tms.util.license.License,
				 kacang.Application,
				 java.util.Collection,
				 com.tms.ekms.security.ui.LoginForm,
				 kacang.services.security.*,
				 kacang.util.Log"%>
<%@ include file="/common/header.jsp" %>
<x:template type="com.tms.ekms.setup.ui.LicenseWidget" body="custom" />
<!-- Header -->
<html>
<head>
	<title></title>
	<link rel="stylesheet" href="styles/style.css">
</head>
<body leftmargin="0" topmargin="0" marginheight="0" marginwidth="0" bgcolor="#E2E2E2">
<table width="100%" cellpadding=0 cellspacing=0 border=0 bgcolor="#333333" background="images/bannerbg.gif">
	<tr>
		<td valign="top" align="left"><img src="images/banner.gif"></td>
		<td bgcolor="#CCCCCC" width="1"><img src="images/clear.gif" width="1"></td>
		<td align="right" valign="bottom"></td>
	</tr>
</table>
<img src="images/clear.gif" height="1"><br>
<!-- End Header -->
<blockquote>
<table>
	<tr><td><jsp:include page="version.html" flush="true"/></td></tr>
	<tr>
		<td>
        	<c-rt:set var="license" value="<%= TmsLicense.getLicense() %>"/>
			<c:if test="${license.validSerialCode}">
				[<c:out value='${license.registrationCode}'/>]
			</c:if>
			<br>
			<c:choose>
				<c:when test="${!license.validSerialCode}">
					<fmt:message key='general.label.licenseIsNotValid'/>
				</c:when>
				<c:when test="${empty license.expiryDate}"></c:when>
				<c:otherwise>
					<fmt:message key='general.label.licenseExpiresOn'/> <fmt:formatDate value="${license.expiryDate}" pattern="${globalDatetimeLong}" />
				</c:otherwise>
			</c:choose>
			<%
				License license = TmsLicense.getLicense();
				if(TmsLicense.REQUIRE_ACTIVATION) {
					String activationKey = TmsLicense.getActivationKey();
					Activation a = Activation.getInstance();
					boolean activated = a.isActivationKeyValid(a.getSystemKey(), activationKey, license.getRegistrationCode());
					request.setAttribute("activated", Boolean.valueOf(activated));
				} else {
					// no need to activate!
					request.setAttribute("activated", Boolean.TRUE);
				}
				//Checking for users
				if(TmsLicense.PREFIX_EKP.equals(TmsLicense.getLicense().getPrefix())) {
					boolean validUsers = false;
                    try
					{
						SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
						Collection users = service.getUsersByPermission(LoginForm.PROPERTY_EKMS_PERMISSION, Boolean.FALSE, null, false, 0, -1);
						int registeredFor = TmsLicense.getLicense().getMaxLicense();
						if(registeredFor == 0 || registeredFor == -1)
							validUsers = true;
						else if(users.size() <= registeredFor)
							validUsers = true;
						else
							validUsers = false;
					}
					catch (kacang.services.security.SecurityException e)
					{              
						Log.getLog(this.getClass()).error("Error while checking for number of users", e);
					}
					request.setAttribute("validUsers", Boolean.valueOf(validUsers));
				}
			%>
			<c:if test="${!activated}"><br><font color="DD0000"> >>> NOTE: Product has not been activated! <<< </font></c:if>
			<c:if test="${!empty validUsers && !validUsers}"><br><font color="DD0000"> >>> NOTE: You have exceeded the number of licensed registered users! <<< </font></c:if>
			<br>
			<hr>
			<form method="POST">
				<fmt:message key='general.label.newLicense'/>
				<input type="text" name="license" value="<c:out value='${param.license}'/>">
				<%
					if(TmsLicense.REQUIRE_ACTIVATION) {
				%>
				<br><br>
				<hr>
				<b>System Activation</b>
				<br>To activate this product, send an email to support@tmsasia.com
				with your serial number and system key. You should receive an email
				response within two working days. Follow the instructions from the
				email response.
				<br><br>
				System Key: <b><%= Activation.getInstance().getSystemKey() %></b>
				<br>
				Activation Key: <input type="text" name="activation" value="<c:out value='${param.activation}'/>">
				<%
					}
				%>
				<br><br>
				<hr>
				<input type="submit" value="<fmt:message key='vote.label.update'/>">
			</form>
		</td>
	</tr>
</table>
</blockquote>



</BODY>
</HTML>


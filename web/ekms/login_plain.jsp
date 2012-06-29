<%@ page import="java.util.Date,
                 com.tms.util.license.TmsLicense" errorPage="login.jsp" %>
<%@ include file="/common/header.jsp" %>
<x:template type="com.tms.ekms.security.ui.LoginFormProcess" properties="forward=home.jsp" name="procLogin" />
<html>
<head>
<title><fmt:message key="general.label.ekp"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style>
<!--
.f01 {font-size: 9px; color: #cccccc; line-height: 16px; font-family: Verdana,Arial,Helvetica,sans-serif}
.f02 {font-size: 11px; color: #666666; line-height: 16px; font-family: Verdana,Arial,Helvetica,sans-serif}
.f03 {font-size: 12px; color: #99cc33; line-height: 16px; font-family: Verdana,Arial,Helvetica,sans-serif; font-weight: Bold}
.f04 {font-size: 10px; color: #666666; line-height: 16px; font-family: Verdana,Arial,Helvetica,sans-serif}
.f05 {font-size: 11px; color: #99cc33; line-height: 16px; font-family: Verdana,Arial,Helvetica,sans-serif; font-weight: Bold}
.f06 {font-size: 10px; color: #666666; line-height: 16px; font-family: Arial,Verdana,Helvetica,sans-serif}
.f07 {font-size: 9px; color: #666666; line-height: 16px; font-family: Verdana,Arial,Helvetica,sans-serif}
.login_box {border : 1px solid #999999; background : #f2f2f2; font : 10px/10px Verdana; width : 85px; color : #666666;}
.login_button {cursor:hand; cursor:pointer; border :1px solid #25528B; background : #A7CFF1; font : 9px/10px Verdana; width : 50px; height: 16px; color : #666666;}
//-->
</style>

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
<body bgcolor="#ffffff" text="333333" link="FFcc00" alink="FF6600" vlink="cccccc" leftmargin="0" rightmargin="10" topmargin="0" marginheight="0" marginwidth="0" onload="loadFocus()">
    <table width="760" height="99" border="0" cellspacing="0" cellpadding="0" align="center">
        <tr>
            <td width="500" align="left" valign="top">
                <table width="500" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <c:set var="loginHeader"><x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_LoginHeader"/></c:set>
                        <c:choose>
                        <c:when test="${!empty loginHeader}">
                            <td>
                            <c:out value="${loginHeader}" escapeXml="false"/>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td align="left" valign="bottom" width="250" height="63"><img src="<c:url value="/ekms/images/logo.jpg"/>" width="211" height="50"></td>
                            <td width="250" align="right" valign="bottom"><img src="<c:url value="/ekms/images/bulletin.gif"/>" width="212" height="63"></td>
                        </c:otherwise>
                        </c:choose>
                    </tr>
                </table>
                <table width="500" border="0" cellspacing="0" cellpadding="0">
                    <tr><td width="500" height="11" align="left" valign="top"><img src="<c:url value="/ekms/images/line_dot.gif"/>" width="500" height="11"></td></tr>
                </table>
                <table width="500" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="500" height="25" bgcolor="#25528B" align="left" valign="middle">
                            <table width="485" border="0" cellspacing="0" cellpadding="0" align="center">
                                <tr>
                                    <td align="left" valign="middle"><font class="f01"><fmt:message key='general.label.welcomeToEKP'/></font></td>
                                    <td align="right" valign="middle">
                                        <font class="f01">
                                            <%  pageContext.setAttribute("date", new Date()); %>
                                            <fmt:formatDate value="${pageScope.date}" pattern="${globalDateLong}" /> |
                                            <fmt:formatDate value="${pageScope.date}" pattern="EEEE" />
                                        </font>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="20">&nbsp;</td>
            <td width="240" align="left" valign="top">
                <table width="240" border="0" cellspacing="0" cellpadding="0">
                    <tr><td width="240" height="45" align="left" valign="top">
                        <c-rt:set var="expiryDays" value="<%= TmsLicense.getLicenseValidityDays(new Date()) %>"/>
                        <c:if test="${!empty expiryDays }">
                            <div class="f01" style="color:red"><fmt:message key="general.label.expiresIn"><fmt:param value="${expiryDays}"/></fmt:message></div>
                        </c:if>
                        <img src="<c:url value="/ekms/images/login.gif"/>" width="75" height="45">
                    </td></tr>
                </table>
                <table width="240" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td bgcolor="#E2F5FD" width="240" height="54" align="left" valign="top" background="<c:url value="/ekms/images/login_bg.gif"/>">
                            <table width="240" border="0" cellspacing="5" cellpadding="0">
                                <tr>
                                    <x:template type="com.tms.ekms.security.ui.LoginFormDisplay" properties="forward=home.jsp" body="/ekms/loginBox.jsp"/>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <img src="<c:url value="/ekms/images/clear.gif"/>" width="1" height="8"><br>
    <table width="760"  border="0" cellspacing="0" cellpadding="5" align="center">
        <tr>
            <td width="500" height="100%" bgcolor="#F2F2F2" align="right" valign="top">
                <table width="490" border="0" cellspacing="0" cellpadding="0" height="100%">
                    <tr>
                        <td bgcolor="#FFFFFF" align="left" valign="top">
                            <table width="490" border="0" cellspacing="0" cellpadding="0">
                                <tr><td bgcolor="#F2F2F2" width="490" height="6" align="left" valign="top"><img src="<c:url value="/ekms/images/clear.gif"/>" width="71" height="6"></td></tr>
                            </table>
                            <img src="<c:url value="/ekms/images/clear.gif"/>" width="1" height="10"><br>
                            <table width="490" border="0" cellspacing="6" cellpadding="0">
                                <tr>
                                    <td align="left" valign="top">
                                        <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_Funnies"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td width="20">&nbsp;</td>
            <td width="240" height="100%" bgcolor="#F2F2F2" align="left" valign="top">
                <table width="230" border="0" cellspacing="0" cellpadding="0" height="100%">
                    <tr>
                        <td bgcolor="#FFFFFF" align="left" valign="top">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr><td height="6" bgcolor="#f2f2f2" align="left" valign="top"><img src="<c:url value="/ekms/images/clear.gif"/>" width="164" height="6"></td></tr>
                            </table>
                            <table width="100%" border="0" cellspacing="6" cellpadding="0">
                                <tr><td align="left" valign="top"><x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_MissionStatement"/></td></tr>
                            </table>
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr><td height="6" bgcolor="#f2f2f2" align="left" valign="top"><img src="<c:url value="/ekms/images/clear.gif"/>"></td></tr>
                            </table>
                            <table width="100%" border="0" cellspacing="6" cellpadding="0">
                                <tr><td align="left" valign="top"><x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_SalesQuote"/></td></tr>
                            </table>
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr><td height="6" bgcolor="#f2f2f2" align="left" valign="top"><img src="<c:url value="/ekms/images/clear.gif"/>" width="114" height="6"></td></tr>
                            </table>
                            <table width="100%" border="0" cellspacing="6" cellpadding="0">
                                <tr><td align="left" valign="top"><x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_NewsBoard"/></td></tr>
                            </table>
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr><td height="6" bgcolor="#f2f2f2" align="left" valign="top"><img src="<c:url value="/ekms/images/clear.gif"/>" width="178" height="6"></td></tr>
                            </table>
                            <table width="100%" border="0" cellspacing="6" cellpadding="0">
                                <tr><td align="left" valign="top"><x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_QuoteOfTheWeek"/></td></tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <img src="<c:url value="/ekms/images/clear.gif"/>" width="1" height="6"><br>
    <table width="760" border="0" cellspacing="0" cellpadding="0" align="center">
        <tr><td><x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_ComicStrip"/></td><tr>
    </table>
    <table width="760" border="0" cellspacing="0" cellpadding="0" align="center">
        <tr><td bgcolor="#99cc66" width="760" height="5"><img src="<c:url value="/ekms/images/clear.gif"/>" width="1" height="5"></td></tr>
    </table>
    <table width="760" border="0" cellspacing="0" cellpadding="5" align="center">
        <tr><td align="center"><font class="f06">
        <c:set var="loginFooter"><x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_LoginFooter"/></c:set>
        <c:choose>
        <c:when test="${!empty loginFooter}">
            <c:out value="${loginFooter}" escapeXml="false"/>
        </c:when>
        <c:otherwise>
            &copy;2004 Copyright The Media Shoppe Berhad. All rights reserved. Developed and designed by The Media Shoppe Berhad.
        </c:otherwise>
        </c:choose>
        </font></td></tr>
    </table>
</body>
</html>

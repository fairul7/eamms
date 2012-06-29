<%@ page import="java.util.Date,
                 com.tms.portlet.taglibs.PortalServerUtil" errorPage="login.jsp" %>
<%@ include file="/common/header.jsp" %>
<x:template type="com.tms.ekms.security.ui.LoginFormProcess" properties="forward=index.jsp" name="procLogin" />
<c:set var="headerCaption" scope="request"><fmt:message key="kacang.services.log.security.Login"/></c:set>
<jsp:include page="includes/mheader.jsp" flush="true" />
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
<x:template type="com.tms.ekms.security.ui.LoginFormDisplay" properties="forward=index.jsp" body="custom">
<form method="post" action="login.jsp">
<table cellspacing="5" cellpadding="0" align="center">
    <td>
        <table border="0" cellspacing="0" cellpadding="1">
        <tr><td class="title">Username</td></tr>
        <tr><td class="data"><input type="text" id="loginUsername" name="loginUsername" size="11" class="login_box"></td></tr>
        <tr><td class="title">Password</td></tr>
        <tr><td class="data"><input type="password" name="loginPassword" size="11" class="login_box"></td></tr>
        <tr><td colspan="2"><input type="submit" name="Submit" value="<fmt:message key="security.label.login"/>" class="button"></td></tr>
        </table>
        <input type="hidden" name="action" value="Login">
        <input type="hidden" name="forward" value="<c:out value="${widget.forward}"/>">
    </td>
</table>
</form>
</x:template>
<jsp:include page="includes/mfooter.jsp" flush="true" />
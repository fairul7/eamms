<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<form method="post" name="loginForm" action="<c:out value="${widget.url}"/>">
    <table width="100%" class="loginTable" cellpadding="2" cellspacing="0">
        <tr>
            <td class="loginLabel" width="20%"><fmt:message key="security.label.username"/>: </td>
            <td class="loginError"><input type="text" name="loginUsername" size="15"> <c:out value="${widget.message.loginUsername}"/></td>
        </tr>
        <tr>
            <td class="loginLabel" width="20%"><fmt:message key="security.label.password"/>: </td>
            <td class="loginError"><input type="password" name="loginPassword" size="15"> <c:out value="${widget.message.loginPassword}"/></td>
        </tr>
        <tr>
            <td class="loginLabel" width="20%">&nbsp;</td>
            <td class="loginLabel"><input type="checkbox" name="rememberPassword" value="1"><fmt:message key="security.label.rememberPassword"/></td>
        </tr>
        <c:if test="${!(empty widget.message.error)}">
            <tr>
                <td>&nbsp;</td>
                <td class="loginError"><c:out value="${widget.message.error}"/></td>
            </tr>
        </c:if>
        <tr>
            <td>&nbsp;</td>
            <td>
            <input type="hidden" name="action" value="Login">
            <input type="submit" value="<fmt:message key="security.label.login"/>">
            </td>
        </tr>
    </table>
    <input type="hidden" name="forward" value="<c:out value="${widget.forward}"/>">
</form>

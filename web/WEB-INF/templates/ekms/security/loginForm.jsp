<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<form method="post" name="loginForm" action="<c:out value="${widget.url}"/>">
    <table class="loginTable" cellpadding="3" cellspacing="0" >
<%--
        <tr><td class="loginHeader" colspan="2">Login</td></tr>
--%>
        <tr>
            <td class="loginLabel" width="30%" align="right"><fmt:message key="security.label.username"/>: </td>
            <td class="loginError"><input type="text" name="loginUsername" size="15"> <br><c:out value="${widget.message.loginUsername}"/></td>
        </tr>
        <tr>
            <td class="loginLabel" width="30%" align="right"><fmt:message key="security.label.password"/>: </td>
            <td class="loginError"><input type="password" name="loginPassword" size="15"> <br><c:out value="${widget.message.loginPassword}"/></td>
        </tr>
        <tr>
            <td class="loginLabel" width="30%">&nbsp;</td>
            <td class="loginRow"><input type="checkbox" name="rememberPassword" value="1"><fmt:message key="security.label.rememberPassword"/></td>
        </tr>
        <c:if test="${!(empty widget.message.error)}">
            <tr><td class="loginError" colspan="2"><c:out value="${widget.message.error}"/></td></tr>
        </c:if>
        <tr>
            <td class="loginRow">&nbsp;</td>
            <td class="loginRow">
            <input type="hidden" name="action" value="Login" class="button">
            <input type="submit" value="<fmt:message key="security.label.login"/>" class="button">
            </td>
        </tr>
    </table>
    <input type="hidden" name="forward" value="<c:out value="${widget.forward}"/>">
</form>

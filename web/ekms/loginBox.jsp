<%@ include file="/common/header.jsp" %>
<form method="post" action="login.jsp">
    <td>
        <input type="text" id="loginUsername" name="loginUsername" size="11" class="login_box">
        <input type="password" name="loginPassword" size="11" class="login_box">
        <input type="submit" name="Submit" value="<fmt:message key="security.label.login"/>" class="login_button">
        <input type="checkbox" name="rememberPassword" value="1">
        <font class="f07"><fmt:message key="security.label.rememberPassword"/></font>
        <input type="hidden" name="action" value="Login">
        <input type="hidden" name="forward" value="<c:out value="${widget.forward}"/>">
    </td>
</form>
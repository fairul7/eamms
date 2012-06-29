<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${requestScope.forgetPasswordForm}"/>
<form method="post" action="<c:out value="${widget.url}"/>">
    <table class="profileTable" cellpadding="2" cellspacing="0">
        <tr><td class="loginLabel" colspan="2"><fmt:message key="security.message.forgetPassword"/></td></tr>
        <tr>
            <td class="loginLabel" width="20%"><fmt:message key="security.label.email"/>: </td>
            <td class="loginError"><input type="test" name="forgetPasswordEmail" value="" size="20">  <c:out value="${widget.message.forgetPasswordEmail}"/></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>
            <input type="hidden" name="action" value="Retrieve Password">
            <input type="submit" class="button" value="<fmt:message key="security.label.forgetPasswordButton"/>">
            </td>
        </tr>
    </table>
</form>

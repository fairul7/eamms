<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

    <b>
    <div class="contentHeader">
        <fmt:message key='cms.label.unauthorized'/>
    </div>
    </b>

    <p>
    <span class="contentBody">
    <fmt:message key='cms.label.noPermission'/>
    </span>

    <p>
    <c:if test="${empty sessionScope.currentUser || sessionScope.currentUser.id == 'anonymous'}">
        <div class="siteBodyHeader">
            <fmt:message key='security.label.login'/>
        </div>

        <c:set var="forwardUrl" value="${pageContext.request.requestURL}?${pageContext.request.queryString}"/>

        <!-- Login Form -->
        <form method="post" action="login.jsp">
            <table width="100%" class="loginTable" cellpadding="2" cellspacing="0">
                <tr>
                    <td class="loginLabel" width="20%"><fmt:message key="security.label.username"/>: </td>
                    <td class="loginError"><input type="text" name="loginUsername" size="15"></td>
                </tr>
                <tr>
                    <td class="loginLabel" width="20%"><fmt:message key="security.label.password"/>: </td>
                    <td class="loginError"><input type="password" name="loginPassword" size="15"></td>
                </tr>
                <tr>
                    <td class="loginLabel" width="20%">&nbsp;</td>
                    <td class="loginLabel"><input type="checkbox" name="rememberPassword" value="1"><fmt:message key="security.label.rememberPassword"/></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>
                    <input type="hidden" name="action" value="Login">
                    <input type="submit" value="<fmt:message key="security.label.login"/>">
                    </td>
                </tr>
            </table>
            <input type="hidden" name="forward" value="<c:out value="${forwardUrl}"/>">
        </form>

        <hr size="1">

        <div class="siteBodyHeader">
            <fmt:message key='security.label.forgotPassword'/>
        </div>
        <x:template type="kacang.services.security.ui.ForgetPasswordFormDisplay" />
        <hr size="1">

        <div class="siteBodyHeader">
            <fmt:message key='security.label.register'/>
        </div>
        <p>
        <fmt:message key='security.label.pleaseSignup'/>
        </p>
    </c:if>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${requestScope.profileForm}"/>

<form method="post" action="<c:out value="${widget.url}"/>">
    <table class="profileTable" cellpadding="2" cellspacing="1">
        <tr><td colspan="2" class="profileHeader"><b><fmt:message key="security.label.personalInformation"/></b></td></tr>
        <tr>
            <td class="profileRow"><fmt:message key="security.label.firstName"/>*</td>
            <td class="profileRow"><input type="text" name="firstName" size="30" value="<c:out value="${widget.firstName}"/>"> <font class="profileError"><c:out value="${widget.message.firstName}"/></font></td>
        </tr>
        <tr>
            <td class="profileRow"><fmt:message key="security.label.lastName"/>*</td>
            <td class="profileRow"><input type="text" name="lastName" size="30" value="<c:out value="${widget.lastName}"/>"> <font class="profileError"><c:out value="${widget.message.lastName}"/></font></td>
        </tr>
        <tr>
            <td class="profileRow"><fmt:message key="security.label.email"/>*</td>
            <td class="profileRow"><input type="text" name="email" size="40" value="<c:out value="${widget.email}"/>"/> <font class="profileError"><c:out value="${widget.message.email}"/></font></td>
        </tr>
        <tr><td colspan="2" class="profileHeader"><b><fmt:message key="security.label.contactInformation"/></b></td></tr>
        <tr>
            <td class="profileRow"><fmt:message key="security.label.address"/></td>
            <td class="profileRow"><textarea rows="7" cols="35" name="address"><c:out value="${widget.address}"/></textarea> <font class="profileError"><c:out value="${widget.message.address}"/></font></td>
        </tr>
        <tr>
            <td class="profileRow"><fmt:message key="security.label.postcode"/></td>
            <td class="profileRow"><input type="text" name="postcode" size="5" value="<c:out value="${widget.postcode}"/>"> <font class="profileError"><c:out value="${widget.message.postcode}"/></font></td>
        </tr>
        <tr>
            <td class="profileRow"><fmt:message key="security.label.city"/></td>
            <td class="profileRow"><input type="text" name="city" size="10" value="<c:out value="${widget.city}"/>"> <font class="profileError"><c:out value="${widget.message.city}"/></font></td>
        </tr>
        <tr>
            <td class="profileRow"><fmt:message key="security.label.state"/></td>
            <td class="profileRow"><input type="text" name="state" size="20" value="<c:out value="${widget.state}"/>"> <font class="profileError"><c:out value="${widget.message.state}"/></font></td>
        </tr>
        <tr>
            <td class="profileRow"><fmt:message key="security.label.country"/></td>
            <td class="profileRow">
                <select name="country">
                    <option value="" <c:if test="${empty widget.country}">SELECTED</c:if>><fmt:message key="security.label.pleaseSelect"/></option>
                    <option value="??" <c:if test="${widget.country=='??'}">SELECTED</c:if>><fmt:message key="security.label.notApplicable"/></option>
                    <c-rt:set var="countryArray" value="<%= kacang.stdui.CountrySelectBox.COUNTRIES %>" />
                    <c:forEach items="${countryArray}" var="country">
                        <option value="<c:out value='${country[0]}'/>" <c:if test="${widget.country==country[0]}">SELECTED</c:if>><c:out value='${country[1]}'/></option>
                    </c:forEach>
                </select>
                <font class="profileError"><c:out value="${widget.message.country}"/></font>
            </td>
        </tr>
        <tr>
            <td class="profileRow"><fmt:message key="security.label.officePhone"/></td>
            <td class="profileRow"><input type="text" name="telOffice" size="15" value="<c:out value="${widget.telOffice}"/>"> <font class="profileError"><c:out value="${widget.message.officePhone}"/></font></td>
        </tr>
        <tr>
            <td class="profileRow"><fmt:message key="security.label.homePhone"/></td>
            <td class="profileRow"><input type="text" name="telHome" size="15" value="<c:out value="${widget.telHome}"/>"> <font class="profileError"><c:out value="${widget.message.telHome}"/></font></td>
        </tr>
        <tr>
            <td class="profileRow"><fmt:message key="security.label.mobilePhone"/></td>
            <td class="profileRow"><input type="text" name="telMobile" size="15" value="<c:out value="${widget.telMobile}"/>"> <font class="profileError"><c:out value="${widget.message.telMobile}"/></font></td>
        </tr>
        <tr>
            <td class="profileRow"><fmt:message key="security.label.fax"/></td>
            <td class="profileRow"><input type="text" name="fax" size="15" value="<c:out value="${widget.fax}"/>"> <font class="profileError"><c:out value="${widget.message.fax}"/></font></td>
        </tr>
        <tr><td colspan="2" class="profileHeader"><b><fmt:message key="security.label.authenticationInformation"/></b></td></tr>
        <tr>
            <td class="profileRow"><fmt:message key="security.label.username"/>*</td>
            <td class="profileRow">
                <c:if test="${!empty widget.username && empty widget.message.username}">
                    <c:out value="${widget.username}"/>
                    <input type="hidden" name="username" size="15" value="<c:out value="${widget.username}"/>"> <font class="profileError"><c:out value="${widget.message.username}"/></font>
                </c:if>
                <c:if test="${empty widget.username || !empty widget.message.username}">
                    <input type="text" name="username" size="15" value="<c:out value="${widget.username}"/>"> <font class="profileError"><c:out value="${widget.message.username}"/></font>
                </c:if>
            </td>
        </tr>
        <tr>
            <td class="profileRow"><fmt:message key="security.label.password"/>*</td>
            <td class="profileRow"><input type="password" name="password" size="15" value=""> <font class="profileError"><c:out value="${widget.message.password}"/></font></td>
        </tr>
        <tr>
            <td class="profileRow"><fmt:message key="security.label.confirmPassword"/>*</td>
            <td class="profileRow"><input type="password" name="confirmPassword" size="15" value=""> <font class="profileError"><c:out value="${widget.message.confirmPassword}"/></font></td>
        </tr>
        <tr>
            <td colspan="2" align="right" class="profileRow">
                <input type="hidden" name="action" value="Submit">
                <input type="submit" value="<fmt:message key="security.label.submit"/>">
            </td></tr>
    </table>
</form>

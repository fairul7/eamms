<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${requestScope.userActivateForm}"/>
<c:if test="${!(empty widget.user)}">
    <form method="post" action="<c:out value="${widget.url}"/>">
        <table class="profileTable" cellpadding="2" cellspacing="1">
            <tr><td class="profileRow" colspan="2"><span class="profileHeader"><fmt:message key="security.label.personalInformation"/></span></td></tr>
            <tr>
                <td class="profileRow" valign="top"><fmt:message key="security.label.firstName"/></td>
                <td class="profileRow"><c:out value="${widget.user.propertyMap.firstName}"/></td>
            </tr>
            <tr>
                <td class="profileRow" valign="top"><fmt:message key="security.label.lastName"/></td>
                <td class="profileRow"><c:out value="${widget.user.propertyMap.lastName}"/></td>
            </tr>
            <tr>
                <td class="profileRow" valign="top"><fmt:message key="security.label.email"/></td>
                <td class="profileRow"><c:out value="${widget.user.propertyMap.email1}"/></td>
            </tr>
            <tr><td class="profileRow" colspan="2"><span style="font-family:Arial; font-size:16px; font-weight:bold"><fmt:message key="security.label.contactInformation"/></span></td></tr>
            <tr>
                <td class="profileRow" valign="top"><fmt:message key="security.label.address"/></td>
                <td class="profileRow"><c:out value="${widget.user.propertyMap.address}"/></td>
            </tr>
            <tr>
                <td class="profileRow" valign="top"><fmt:message key="security.label.postcode"/></td>
                <td class="profileRow"><c:out value="${widget.user.propertyMap.postcode}"/></td>
            </tr>
            <tr>
                <td class="profileRow" valign="top"><fmt:message key="security.label.city"/></td>
                <td class="profileRow"><c:out value="${widget.user.propertyMap.city}"/></td>
            </tr>
            <tr>
                <td class="profileRow" valign="top"><fmt:message key="security.label.state"/></td>
                <td class="profileRow"><c:out value="${widget.user.propertyMap.state}"/></td>
            </tr>
            <tr>
                <td class="profileRow" valign="top"><fmt:message key="security.label.country"/></td>
                <td class="profileRow"><c:out value="${widget.user.propertyMap.country}"/></td>
            </tr>
            <tr>
                <td class="profileRow" valign="top"><fmt:message key="security.label.officePhone"/></td>
                <td class="profileRow"><c:out value="${widget.user.propertyMap.telOffice}"/></td>
            </tr>
            <tr>
                <td class="profileRow" valign="top"><fmt:message key="security.label.homePhone"/></td>
                <td class="profileRow"><c:out value="${widget.user.propertyMap.telHome}"/></td>
            </tr>
            <tr>
                <td class="profileRow" valign="top"><fmt:message key="security.label.mobilePhone"/></td>
                <td class="profileRow"><c:out value="${widget.user.propertyMap.telMobile}"/></td>
            </tr>
            <tr>
                <td class="profileRow" valign="top"><fmt:message key="security.label.fax"/></td>
                <td class="profileRow"><c:out value="${widget.user.propertyMap.fax}"/></td>
            </tr>
            <tr><td class="profileRow" colspan="2"><span style="font-family:Arial; font-size:16px; font-weight:bold"><fmt:message key="security.label.authenticationInformation"/><span></td></tr>
            <tr>
                <td class="profileRow" valign="top"><fmt:message key="security.label.username"/></td>
                <td class="profileRow"><c:out value="${widget.user.username}"/></td>
            </tr>
            <tr>
                <td class="profileRow">&nbsp;</td>
                <td class="profileRow">
                    <input type="hidden" name="action" value="Activate"/>
                    <input type="submit" value="<fmt:message key="security.label.activate"/>"/>
                </td>
            </tr>
        </table>
        <input type="hidden" name="uid" value="<c:out value="${widget.userId}"/>">
        <input type="hidden" name="qualifier" value="<c:out value="${widget.password}"/>">
    </form>
</c:if>

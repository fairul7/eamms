<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${requestScope.subscriptionList}"/>
<form method="post" action="<c:out value="${widget.url}"/>">
    <table class="subscriptionListTable">
        <c:forEach items="${widget.subscriptions}" var="subscription">
            <tr><td class="subscriptionListLabel" colspan="2"><c:out value="${subscription.name}"/></td><td class="subscriptionListRow"></td>
            </tr>
            <tr>
                <td class="subscriptionListRow"><fmt:message key='general.label.price'/>: </td>
                <td class="subscriptionListRow"><c:out value="${subscription.price}"/></td>
            </tr>
            <tr>
                <td class="subscriptionListRow"><fmt:message key='general.label.period'/>: </td>
                <td class="subscriptionListRow"><c:out value="${subscription.months}"/> <fmt:message key='general.label.months'/></td>
            </tr>
            <tr><td class="subscriptionListRow" colspan="2"><c:out value="${subscription.description}"/></td></tr>
            <c:if test="${widget.user.id != 'anonymous'}">
                <tr><td colspan="2" class="subscriptionListRow" align="right"><input type="checkbox" name="subscriptionListId" value="<c:out value="${subscription.id}"/>"> <fmt:message key='security.label.subscribeToThisPackage'/></td></tr>
            </c:if>
            <tr><td colspan="2">&nbsp;</td></tr>
        </c:forEach>
        <tr><td colspan="2">&nbsp;</td></tr>
        <c:if test="${widget.user.id != 'anonymous'}">
            <tr>
            <td colspan="2" align="right">
                <input type="hidden" name="action" value="Subscribe">
                <input type="submit" class="button" value="<fmt:message key='security.label.subscribe'/>">
            </td>
            </tr>
        </c:if>
    </table>
</form>
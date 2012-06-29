<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${requestScope.subscriptionCart}"/>
<c:if test="${!(empty widget)}">
    <form method="post" action="<c:out value="${widget.url}"/>">
        <table width="100%" cellpadding="1" cellspacing="0" class="subscriptionCartTable">
            <tr>
                <td>
                    <table width="100%" cellpadding="2" cellspacing="2">
                        <tr>
                            <td class="subscriptionCartHeader">&nbsp;</td>
                            <td class="subscriptionCartHeader"><fmt:message key='general.label.name'/></td>
                            <td class="subscriptionCartHeader" align="center"><fmt:message key='security.label.subscriptionPeriod'/></td>
                            <td class="subscriptionCartHeader" align="center"><fmt:message key='security.label.expiryDate'/></td>
                            <td class="subscriptionCartHeader" align="center"><fmt:message key='general.label.status'/></td>
                        </tr>
                        <c:forEach items="${widget.subscriptions}" var="subscription">
                            <tr>
                                <td><input type="checkbox" name="subscriptionCartId" value="<c:out value="${subscription.subscriptionId}"/>"></td>
                                <td><c:out value="${subscription.subscription.name}"/></td>
                                <td align="center"><c:out value="${subscription.subscription.months}"/> <fmt:message key='general.label.months'/></td>
                                <td align="center"><fmt:formatDate value="${subscription.dateExpire}" pattern="${globalDateLong}" /></td>
                                <td align="center">
                                    <c:choose>
                                        <c:when test="${subscription.state == '0'}"><fmt:message key='vote.label.pending'/></c:when>
                                        <c:when test="${subscription.state == '1'}"><fmt:message key='general.label.active'/></c:when>
                                        <c:when test="${subscription.state == '2'}"><fmt:message key='general.label.expired'/></c:when>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr><td colspan="5">&nbsp;</td></tr>
                        <tr>
                        <td colspan="5" align="right">
                            <input type="hidden" name="action" value="Unsubscribe">
                            <input type="submit" class="button" value="<fmt:message key='security.label.unsubscribe'/>">
                        </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </form>
</c:if>

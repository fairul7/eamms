<%@ page import="com.tms.cms.subscription.Subscriber"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<jsp:include page="../../form_header.jsp"/>
<c:if test="${!((empty widget.subscriptionId)||(empty widget.userId))}">
    <c:if test="${!(empty widget.message)}">
        <script>alert("<c:out value="${widget.message}"/>");</script>
    </c:if>
    <table cellpadding="2" cellspacing="1" width="95%">
        <tr><td colspan="2"><span style="font-family:Arial; font-size:16px; font-weight:bold"><fmt:message key='security.label.subscriberInformation'/></span></td></tr>
        <tr>
            <td valign="top"><fmt:message key='general.label.name'/></td>
            <td><c:out value="${widget.subscriber.user.propertyMap.firstName}"/> <c:out value="${widget.subscriber.user.propertyMap.lastName}"/> [<c:out value="${widget.subscriber.user.username}"/>]</td>
        </tr>
        <tr>
            <td valign="top"><fmt:message key='security.label.dateSubscribed'/></td>
            <td><c:out value="${widget.subscriber.dateSubscribed}"/></td>
        </tr>
        <tr>
            <td valign="top"><fmt:message key='security.label.dateLastRenewed'/></td>
            <td><c:out value="${widget.subscriber.dateLastRenewed}"/></td>
        </tr>
        <tr>
            <td valign="top"><fmt:message key='security.label.expiryDate'/></td>
            <td><c:out value="${widget.subscriber.dateExpire}"/></td>
        </tr>
        <tr>
            <td valign="top"><fmt:message key='general.label.state'/></td>
            <td>
                <c:choose>
                    <c:when test="${widget.subscriber.state == 1}">
                        <fmt:message key='general.label.active'/>
                    </c:when>
                    <c:when test="${widget.subscriber.state == 0}">
                        <fmt:message key='general.label.pending'/>
                    </c:when>
                    <c:when test="${widget.subscriber.state == 2}">
                        <fmt:message key='general.label.expired'/>
                    </c:when>
                </c:choose>
            </td>
        </tr>
        <tr><td colspan="2"><span style="font-family:Arial; font-size:16px; font-weight:bold"><fmt:message key='security.label.paymentHistory'/></span></td></tr>
        <tr>
            <td colspan="2">
                <c:choose>
                    <c:when test="${empty widget.history}"><tr><td colspan="2"><fmt:message key='security.label.noPaymentHistoryFound'/></td></tr></c:when>
                    <c:otherwise>
                        <table cellpadding="2" cellspacing="2" width="100%">
                            <tr>
                                <td><fmt:message key='general.label.date'/></td>
                                <td><fmt:message key='vote.label.action'/></td>
                                <td><fmt:message key='security.label.paymentMethod'/></td>
                                <td><fmt:message key='general.label.number'/></td>
                                <td><fmt:message key='security.label.amount'/></td>
                            </tr>
                            <c:forEach items="${widget.history}" var="history">
                                <tr>
                                    <td><c:out value="${history.actionDate}"/></td>
                                    <td><c:out value="${history.action}"/></td>
                                    <td><c:out value="${history.method}"/></td>
                                    <td><c:out value="${history.methodNumber}"/></td>
                                    <td><c:out value="${history.amount}"/></td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <c:if test="${!((widget.childMap.renewButton.hidden) && (widget.childMap.approveButton.hidden))}">
            <tr><td colspan="2"><span style="font-family:Arial; font-size:16px; font-weight:bold"><fmt:message key='security.label.paymentDetails'/></span></td></tr>
            <tr>
                <td valign="top"><fmt:message key='security.label.paymentMethod'/>: </td>
                <td><x:display name="${widget.paymentMethod.absoluteName}"/></td>
            </tr>
            <tr>
                <td valign="top"><fmt:message key='security.label.paymentNumber'/>: </td>
                <td><x:display name="${widget.methodNumber.absoluteName}"/></td>
            </tr>
            <tr>
                <td valign="top"><fmt:message key='security.label.amount'/>: </td>
                <td><x:display name="${widget.amount.absoluteName}"/></td>
            </tr>
        </c:if>
        <tr>
            <td>&nbsp;</td>
            <td>
                <c:if test="${!(widget.childMap.renewButton.hidden)}">
                    <x:display name="${widget.renewButton.absoluteName}"/>
                </c:if>
                <c:if test="${!(widget.childMap.approveButton.hidden)}">
                    <x:display name="${widget.approveButton.absoluteName}"/>
                </c:if>
                <x:display name="${widget.cancelButton.absoluteName}"/>
            </td>
        </tr>
    </table>
</c:if>
<jsp:include page="../../form_footer.jsp"/>
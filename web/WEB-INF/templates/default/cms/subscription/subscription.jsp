<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<jsp:include page="../../form_header.jsp"/>
<c:if test="${!(empty widget.id)}">
    <c:if test="${!(empty widget.message)}">
        <script>alert("<c:out value="${widget.message}"/>");</script>
    </c:if>
    <table cellpadding="2" cellspacing="1">
        <tr><td colspan="2"><span style="font-family:Arial; font-size:16px; font-weight:bold"><fmt:message key='security.label.subscription'/></span></td></tr>
        <tr>
            <td valign="top"><fmt:message key='general.label.name'/></td>
            <td><x:display name="${widget.subscriptionName.absoluteName}"/></td>
        </tr>
        <tr>
            <td valign="top"><fmt:message key='general.label.description'/></td>
            <td><x:display name="${widget.description.absoluteName}"/></td>
        </tr>
        <tr>
            <td valign="top"><fmt:message key='general.label.price'/></td>
            <td><x:display name="${widget.price.absoluteName}"/></td>
        </tr>
        <tr>
            <td valign="top"><fmt:message key='general.label.months'/></td>
            <td><x:display name="${widget.months.absoluteName}"/></td>
        </tr>
        <tr>
            <td valign="top"><fmt:message key='security.label.group'/></td>
            <td><x:display name="${widget.group.absoluteName}"/></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td><x:display name="${widget.subscriptionState.absoluteName}"/> <fmt:message key='general.label.active'/></td>
        </tr>
        <tr><td colspan="2">&nbsp;</td></tr>
        <tr>
            <td>&nbsp;</td>
            <td><x:display name="${widget.subscriptionButton.absoluteName}"/> <x:display name="${widget.cancelButton.absoluteName}"/></td>
        </tr>
        <tr><td colspan="2">&nbsp;</td></tr>
    </table>
</c:if><jsp:include page="../../form_footer.jsp"/>
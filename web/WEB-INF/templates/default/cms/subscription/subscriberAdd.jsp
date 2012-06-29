<%@ page import="com.tms.cms.subscription.Subscriber"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<jsp:include page="../../form_header.jsp"/>
<c:if test="${!(empty widget.subscriptionId)}">
    <c:if test="${!(empty widget.message)}">
        <script>alert("<c:out value="${widget.message}"/>");</script>
    </c:if>
    <table cellpadding="2" cellspacing="1" width="95%">
        <tr><td colspan="2"><span style="font-family:Arial; font-size:16px; font-weight:bold"><fmt:message key='security.label.subscriberInformation'/></span></td></tr>
        <tr>
            <td><fmt:message key='security.label.user'/></td>
            <td><x:display name="${widget.userSelect.absoluteName}"/></td>
        </tr>
        <tr><td colspan="2">&nbsp;</td></tr>
        <tr>
            <td>&nbsp;</td>
            <td><x:display name="${widget.buttonSave.absoluteName}"/> <x:display name="${widget.buttonCancel.absoluteName}"/></td>
        </tr>
        <tr><td colspan="2">&nbsp;</td></tr>
    </table>
</c:if>
<jsp:include page="../../form_footer.jsp"/>
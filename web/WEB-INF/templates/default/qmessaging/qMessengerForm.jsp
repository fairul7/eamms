<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
<c:if test="${!empty form.user}">
    <table cellspacing="1" cellpadding="3" width="100%">
        <jsp:include page="../form_header.jsp" flush="true"/>
        <tr><td class="qMessagingRowHeader" colspan="2"><fmt:message key='qmessaging.message.quickMessage'/></td></tr>
        <tr>
            <td class="qMessagingRowLabel" width="20%" nowrap align="right" valign="right"><fmt:message key='qmessaging.message.recipient'/> </td>
            <td class="qMessagingRow" width="80%">[<c:out value="${form.user.username}"/>] <c:out value="${form.user.propertyMap['firstName']} ${form.user.propertyMap['lastName']}"/></td>
        </tr>
        <tr>
            <td class="qMessagingRowLabel" width="20%" nowrap align="right" valign="right"><fmt:message key='qmessaging.message.message'/> </td>
            <td class="qMessagingRow" width="80%"><x:display name="${form.body.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="qMessagingRow" width="20%" nowrap>&nbsp;</td>
            <td class="qMessagingRow" width="80%"><x:display name="${form.submit.absoluteName}"/> <x:display name="${form.cancel.absoluteName}"/></td>
        </tr>
        <jsp:include page="../form_footer.jsp" flush="true"/>
    </table>
</c:if>

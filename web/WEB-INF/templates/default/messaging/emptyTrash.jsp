<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />

<c:if test="${w.emptyTrashSuccessful}">
    <fmt:message key='messaging.message.emptyTrashMessage'/>.
</c:if>
<c:if test="${!w.emptyTrashSuccessful}">
    <fmt:message key='messaging.message.trashErrorMessage'/>.
</c:if>

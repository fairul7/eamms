<%@ include file="/common/header.jsp" %>

<c:set var="fileselector" value="${widget}"/>

<c:set var="fileList" value="${fileselector.fileList}"/>
<c:if test="${!empty fileList[0]}">
    <a href="<c:url value="/storage${fileList[0].absolutePath}"/>" target="_blank"><c:out value="${fileList[0].name}"/></a>
    <br>
    <c:if test="${!fileselector.readOnly}">
        <input type="checkbox" name="<c:out value="${fileselector.deleteParameter}"/>" value="true"> <fmt:message key="helpdesk.label.remove"/>
        <br>
    </c:if>
</c:if>

<c:if test="${!fileselector.readOnly}">
<c:choose>
    <c:when test="${!fileselector.invalid}">
    <input
       type="file" name="<c:out value="${fileselector.absoluteName}"/>"
       onBlur="<c:out value="${fileselector.onBlur}"/>"
       onChange="<c:out value="${fileselector.onChange}"/>"
       onFocus="<c:out value="${fileselector.onFocus}"/>"
    >
    </c:when>
    <c:otherwise>
    !<input
       style="border:1 solid #de123e"
       type="file" name="<c:out value="${fileselector.absoluteName}"/>"
       onBlur="<c:out value="${fileselector.onBlur}"/>"
       onChange="<c:out value="${fileselector.onChange}"/>"
       onFocus="<c:out value="${fileselector.onFocus}"/>"
    >
    </c:otherwise>
</c:choose>
</c:if>

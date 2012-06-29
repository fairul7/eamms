<%@ include file="/common/header.jsp" %>

<c:set var="fieldList" value="${widget.fieldList}"/>

<c:if test="${!empty fieldList}">
    <table width="100%">
        <c:forEach var="field" items="${fieldList}">
        <tr>
            <c:set var="label" value="${field.label}"/>
            <c:if test="${empty label}"><c:set var="label" value="${field.name}"/></c:if>
            <td valign="top" width="15%" class="contentChildSummary"><b><c:out value="${label}"/></b></td>
            <td valign="top" class="contentChildName" width="5"><b>:</b></td>
            <td valign="top" class="contentChildSummary"><c:out value="${field.value}" escapeXml="false" /></td>
        </tr>
        </c:forEach>
    </table>
</c:if>
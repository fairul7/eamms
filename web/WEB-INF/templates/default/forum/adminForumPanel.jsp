<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="panel" value="${widget}"/>
<jsp:include page="../panel_header.jsp" flush="true"/>
<table width="100%">
    <c:if test="${!(empty requestScope.message)}">
        <script>alert("<c:out value="${requestScope.message}"/>");</script>
    </c:if>
    <tr>
        <td>
            <x:display name="${panel.childMap.adminForumTable.absoluteName}"/>
            <x:display name="${panel.childMap.createForumForm.absoluteName}"/>
            <x:display name="${panel.childMap.editForumForm.absoluteName}"/>
            <x:display name="${panel.childMap.adminThreadTable.absoluteName}"/>
            <x:display name="${panel.childMap.editThreadForm.absoluteName}"/>
            <x:display name="${panel.childMap.adminMessageTable.absoluteName}"/>
            <x:display name="${panel.childMap.editMessageForm.absoluteName}"/>
        </td>
    </tr>
</table>

<jsp:include page="../panel_footer.jsp" flush="true"/>

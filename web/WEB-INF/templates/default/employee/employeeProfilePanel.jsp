<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="panel" value="${widget}"/>
<c:if test="${!(empty requestScope.message)}">
    <script>alert("<c:out value="${requestScope.message}"/>");</script>
</c:if>
<jsp:include page="../panel_header.jsp" flush="true"/>
<table width="100%">
    <tr>
        <td>
            <x:display name="${panel.employeeProfileTable.absoluteName}"/>
            <x:display name="${panel.employeeProfileFormEdit.absoluteName}"/>
            <x:display name="${panel.employeeProfileForm.absoluteName}"/>
        </td>
    </tr>
</table>
<jsp:include page="../panel_footer.jsp" flush="true"/>
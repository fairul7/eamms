<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="panel" value="${widget}"/>
<c:if test="${!(empty requestScope.message)}">
    <script>alert("<c:out value="${requestScope.message}"/>");</script>
</c:if>
<table width="100%">
    <tr>
        <td>
            <x:display name="${panel.globalEntitlementForm.absoluteName}"/>
            <x:display name="${panel.globalEntitlementTable.absoluteName}"/>
        </td>
    </tr>
</table>

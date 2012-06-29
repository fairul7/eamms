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
        <c:if test="${!form.submissionMsg.hidden}">
            <x:display name="${panel.submissionMsg.absoluteName}"/>
         </c:if>
        </td>
    </tr>
</table>

<jsp:include page="../panel_footer.jsp" flush="true"/>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="portal" value="${widget}"/>
<table cellpadding="0" cellspacing="0" width="100%">
    <tr><td><x:display name="${portal.manager.absoluteName}"/></td></tr>
</table>
<br>
<%--
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td>
            <c:if test="${!(empty portal.themes)}">
                <x:display name="${portal.themes.absoluteName}"/>
            </c:if>
            <c:if test="${!(empty portal.portlets)}">
                <x:display name="${portal.portlets.absoluteName}"/>
            </c:if>
        </td>
    </tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>--%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="panel" value="${widget}"/>

<c:set var="column" value="0"/>
<c:set var="row" value="0"/>
<table width="<c:out value="${panel.width}"/>" cellpadding="2">
    <tr>
        <c:forEach var="child" items="${panel.children}">
            <td valign="top"><x:display name="${child.absoluteName}"/></td>
            <c:set var="column" value="${column + 1}"/>
            <c:if test="${column ge panel.columns}">
                </tr>
                <tr>
                <c:set var="column" value="0"/>
                <c:set var="row" value="${row + 1}"/>
            </c:if>
        </c:forEach>
    </tr>
</table>

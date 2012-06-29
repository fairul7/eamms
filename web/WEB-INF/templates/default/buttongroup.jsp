<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="panel" value="${widget}"/>

<c:if test="${panel.invalid}">
    <span style="border:1 solid #de123e">
</c:if>

<c:choose>
    <c:when test="${panel.columns == 0}">
        <c:forEach var="child" items="${panel.children}">
            <x:display name="${child.absoluteName}"/><br>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <c:set var="column" value="0"/>
        <c:set var="row" value="0"/>
        <table width="<c:out value="${panel.width}"/>">
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
    </c:otherwise>
</c:choose>

<c:if test="${panel.invalid}">
    </span>
</c:if>

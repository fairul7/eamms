<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="portlet" value="${widget}"/>
<c:choose>
    <c:when test="${portlet.minimized}">
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${portlet.columns == 0}">
                <c:forEach var="child" items="${portlet.children}">
                    <x:display name="${child.absoluteName}"/>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <c:set var="column" value="0"/>
                <c:set var="row" value="0"/>
                <table width="<c:out value="${portlet.width}"/>">
                    <tr>
                        <c:forEach var="child" items="${portlet.children}">
                            <td valign="top">
                            <x:display name="${child.absoluteName}"/></td>
                            <c:set var="column" value="${column + 1}"/>
                            <c:if test="${column ge portlet.columns}">
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
    </c:otherwise>
</c:choose>

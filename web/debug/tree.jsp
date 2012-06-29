<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<table border=0 cellspacing=2 cellpadding=1>
<c:forEach var="co" items="${widget.children}" varStatus="coStatus">
    <c:set var="cookieName"><c:out value="${co.absoluteName}"/></c:set>
    <c:set var="cookieValue"><c:choose><c:when test="${cookie[cookieName].value == 'block'}">block</c:when><c:otherwise>none</c:otherwise></c:choose></c:set>
<tr>
    <td valign=top>
        <c:choose>
        <c:when test="${empty co.children || empty co.children[0]}">
            <span style="border:solid 1px gray; margin:0px; padding: 2px; text-align:center; width:14px; font-size:5px; line-height: 6px">-</span>
        </c:when>
        <c:when test="${!empty co.children && !empty co.children[0] && cookieValue=='none'}">
            <a  style="border:solid 1px gray; margin:0px; padding: 2px; text-align:center; width:14px; font-size:16px; line-height: 6px" href="#" onclick="toggle('<c:out value="${co.absoluteName}"/>'); return false"><span id="icon_<c:out value="${co.absoluteName}"/>">+</span></a>
        </c:when>
        <c:otherwise>
            <a style="border:solid 1px gray; margin:0px; padding: 2px; text-align:center; width:14px; font-size:5px; line-height: 6px" href="#" onclick="toggle('<c:out value="${co.absoluteName}"/>'); return false"><span id="icon_<c:out value="${co.absoluteName}"/>">-</span></a>
        </c:otherwise>
        </c:choose>
    </td>
    <td>
        <a title="<c:out value="${co.class.name}"/>"
           href="#"
           onclick="window.open('debug_details.jsp?name=<c:out value="${co.absoluteName}"/>','debug_details','width=640,height=480,status=yes,scrollbars=yes,resizable=yes');return false">
        <c:out value="${co.name}"/></a>
        <c:if test="${!empty co.children && !empty co.children[0]}">
            <span id="<c:out value="${co.absoluteName}"/>" style="display: <c:out value="${cookieValue}"/>">
                </script>
                    <c:set var="orig" scope="page" value="${widget}"/>
                    <c:set var="widget" scope="request" value="${co}"/>
                    <c:catch var="ie">
                        <jsp:include page="tree.jsp" flush="true"/>
                    </c:catch>
                    <c:set var="widget" scope="request" value="${orig}"/>
            </span>
        </c:if>
    </td>
</tr>
</c:forEach>
</table>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>
<table>
    <c:forEach var="module" items="${widget.permissionsForDisplay}">
        <tr>
            <td class="classRow" valign="top" colspan="4"><br><b><c:out value="${module.key}"/></b><hr size="1"></td>
        </tr>
            <c:set var="offset" value="1"/>
            <c:forEach var="permission" items="${module.value}">
                <c:if test="${offset == 1}">
                    <tr>
                </c:if>
                <td class="classRow" valign="top"><x:display name="${widget.absoluteName}.${permission}"/></td>
                <c:set var="offset" value="${offset+1}"/>
                <c:if test="${offset == 5}">
                    </tr>
                    <c:set var="offset" value="1"/>
                </c:if>
            </c:forEach>
            <c:forEach begin="1" end="${5-offset}">
                <td class="classRow">&nbsp;</td>
            </c:forEach>
        </tr>
    </c:forEach>
</table>


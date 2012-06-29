<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="portlet" value="${widget}"/>

<c:if test="${portlet.minimized}">
    <!--Minimized--><fmt:message key="portlet.minimized"/>
</c:if>
</td>
</tr>
</table>

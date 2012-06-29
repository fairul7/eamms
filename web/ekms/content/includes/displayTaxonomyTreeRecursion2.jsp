<%@ page import="kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<table border=0 cellspacing=2 cellpadding=1 width="100%">
<c:forEach var="co" items="${root.childNodes}" varStatus="coStatus">
    <c:set var="cookieName"><c:out value="${widget.name}"/><c:out value="${co.taxonomyId}"/></c:set>
    <c:set var="cookieValue"><c:choose><c:when test="${cookie[cookieName].value == 'block'}">block</c:when><c:otherwise>none</c:otherwise></c:choose></c:set>

<tr>
    <td align="center" valign="top" width="10" >
	<span class="contentTreeNode">-</span>
    <!-- <img src="<c:url value="/ekms/"/>images/khazanah2005/arrow.gif" width=10 height=11>-->
    </td>
    <td valign="top">
        <a title="<c:out value="${co.taxonomyName}"/>" class=contentTreeLink href="/ekms/content/taxonomyTree.jsp?id=<c:out value="${co.taxonomyId}"/>"><font class="menuFont"><b><c:out value="${co.taxonomyName}"/></b></font></a>
    </td>
</tr>

</c:forEach>
</table>

<%@ page import="kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>


<table border=0 cellspacing=2 cellpadding=1>
<c:forEach var="co" items="${root.children}" varStatus="coStatus">
    <c:set var="cookieName"><c:out value="${tree.name}"/><c:out value="${co.id}"/></c:set>
    <c:set var="cookieValue"><c:choose><c:when test="${cookie[cookieName].value == 'block'}">block</c:when><c:otherwise>none</c:otherwise></c:choose></c:set>
<tr>
    <td valign=top>
        <c:choose>
        <c:when test="${empty co.children || empty co.children[0]}">
            <span class="contentTreeNode">-</span>
        </c:when>
        <c:when test="${!empty co.children && !empty co.children[0] && cookieValue=='none'}">
            <a class="contentTreeNode" href="#" onclick="treeToggle('<c:out value="${tree.name}"/><c:out value="${co.id}"/>'); return false"><span id="icon_<c:out value="${tree.name}"/><c:out value="${co.id}"/>">+</span></a>
        </c:when>
        <c:otherwise>
            <a class="contentTreeNode" href="#" onclick="treeToggle('<c:out value="${tree.name}"/><c:out value="${co.id}"/>'); return false"><span id="icon_<c:out value="${tree.name}"/><c:out value="${co.id}"/>">-</span></a>
        </c:otherwise>
        </c:choose>
    </td>
    <td>
        <c:if test="${co.propertyMap.orphan}">*</c:if>
        <a title="<c:out value="${co.name}"/>"
           class="contentTreeLink"
           href="content.jsp?id=<c:out value="${co.id}"/>">
            <c:out value="${co.name}"/></a>
        <c:if test="${!empty co.children && !empty co.children[0]}">
            <span id="<c:out value="${tree.name}"/><c:out value="${co.id}"/>" style="display: <c:out value="${cookieValue}"/>">
                    <c:set var="orig" scope="page" value="${root}"/>
                    <c:set var="root" scope="request" value="${co}"/>
                    <c:catch var="ie">
                        <jsp:include page="displayContentTreeRecursion.jsp" flush="true">
                            <jsp:param name="children" value="true" />
                        </jsp:include>
                    </c:catch>
                    <c:set var="root" scope="request" value="${orig}"/>
            </span>
        </c:if>
    </td>
</tr>
<c:if test="${!param.children}">
<tr height="0">
    <td colspan="2"><span class="contentTreeSeparator"><img src="<c:url value='/cmsadmin/images/clear.gif'/>" height="0" width="0"></span></td>
</tr>
</c:if>
</c:forEach>
</table>



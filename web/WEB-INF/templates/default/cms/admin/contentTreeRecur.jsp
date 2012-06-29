<%@ page import="kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="root" value="${orig}"/>

<table border=0 cellspacing=2 cellpadding=1>
<c:forEach items="${root}" var="child">

<c:set var="cookieName"><c:out value="${child.id}"/></c:set>
<c:set var="cookieValue"><c:choose><c:when test="${cookie[cookieName].value == 'block'}">block</c:when><c:otherwise>none</c:otherwise></c:choose></c:set>

<tr>
    <td valign=top>
        <c:choose>
        <c:when test="${empty child.children}">
            <span class="contentCollapsed">-</span>
        </c:when>
        <c:when test="${!empty child.children && cookieValue=='none'}">
            <a class="contentExpanded" href="#" onclick="treeToggle('<c:out value="${child.id}"/>'); return false"><span id="icon_<c:out value="${child.id}"/>">+</span></a>
        </c:when>
        <c:otherwise>
            <a class="contentExpanded" href="#" onclick="treeToggle('<c:out value="${child.id}"/>'); return false"><span id="icon_<c:out value="${child.id}"/>">-</span></a>
        </c:otherwise>
        </c:choose>
    </td>
    <td>
    	<x:display name="${child.chkBx.absoluteName}"/><c:out value="${child.name}"/>
    	
    	<span id="<c:out value="${child.id}"/>" style="display: <c:out value="${cookieValue}"/>">
    	<c:set var="orig" value="${child.children}" scope="request"/>
    	<jsp:include page="contentTreeRecur.jsp" flush="true"/>
    	</span>
    	
    </td>
</tr>

</c:forEach>
</table>

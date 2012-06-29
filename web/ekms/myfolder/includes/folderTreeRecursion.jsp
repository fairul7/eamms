<%@ page import="kacang.ui.Event,
                 com.tms.collab.messaging.model.Util,
                 com.tms.collab.messaging.model.Folder"%>
<%@ include file="/common/header.jsp" %>

<table border=0 cellspacing=2 cellpadding=1>
<c:forEach var="co" items="${root[tree.childrenProperty]}" varStatus="coStatus">
    <c:set var="cookieName"><c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/></c:set>
    <c:set var="cookieValue"><c:choose><c:when test="${cookie[cookieName].value == 'block'}">block</c:when><c:otherwise>none</c:otherwise></c:choose></c:set>

<tr>

    <c:set var="tdStyle" value=""/>
    <td style="<c:out value="${tdStyle}"/>" valign=top style="padding-top:5px; padding-bottom:5px">
        <c:choose>
        <c:when test="${empty co[tree.childrenProperty] || empty co[tree.childrenProperty][0]}">
            <span class="contentTreeNode">-</span>
        </c:when>
        <c:when test="${!empty co[tree.childrenProperty] && !empty co[tree.childrenProperty][0] && cookieValue=='none'}">
            <a class="contentTreeNode" href="#" onclick="treeToggle('<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>'); return false"><span id="icon_<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>">+</span></a>
        </c:when>
        <c:otherwise>
            <a class="contentTreeNode" href="#" onclick="treeToggle('<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>'); return false"><span id="icon_<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>">-</span></a>
        </c:otherwise>
        </c:choose>
    </td>
    
</tr>

</c:forEach>
</table>



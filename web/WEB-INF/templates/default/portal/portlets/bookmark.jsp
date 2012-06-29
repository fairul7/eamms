<%@ include file="/common/header.jsp" %>
<c:set var="bookmark" value="${widget}"/>
<table cellpadding="1" cellspacing="1" width="100%" class="bookmarkBackground" align="center">
    <c:choose>
        <c:when test="${!empty bookmark.links}">
            <c:forEach var="link" items="${bookmark.links}">
                <tr><td class="bookmarkRow"><li><a href="" onClick="window.open('http://<c:out value="${link.key}"/>'); return false;"><c:out value="${link.value}"/></a></li></td></tr>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <tr><td class="bookmarkRow"><fmt:message key='portlet.message.noItemsFound'/></td></tr>
        </c:otherwise>
    </c:choose>
    <tr><td class="bookmarkRow">&nbsp;</td></tr>
    <tr><td class="portletFooter">&nbsp;</td></tr>
</table>

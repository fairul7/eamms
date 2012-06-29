<%@ page import="kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<table border=0 cellspacing=2 cellpadding=1>
    <c:forEach var="co" items="${root.children}" varStatus="coStatus">
        <c:set var="cookieName"><c:out value="${tree.name}"/><c:out value="${co.messageId}"/></c:set>
        <c:set var="cookieValue"><c:choose><c:when test="${cookie[cookieName].value == 'none'}">none</c:when><c:otherwise>block</c:otherwise></c:choose></c:set>
        <tr>
            <td valign=top class="forumRow">
                <c:choose>
                <c:when test="${empty co.children || empty co.children[0]}">
                    <span class="forumTreeNode">-</span>
                </c:when>
                <c:when test="${!empty co.children && !empty co.children[0] && cookieValue=='none'}">
                    <a class="forumTreeNode" href="#" onclick="treeToggle('<c:out value="${tree.name}"/><c:out value="${co.messageId}"/>'); return false"><span id="icon_<c:out value="${tree.name}"/><c:out value="${co.messageId}"/>">+</span></a>
                </c:when>
                <c:otherwise>
                    <a class="forumTreeNode" href="#" onclick="treeToggle('<c:out value="${tree.name}"/><c:out value="${co.messageId}"/>'); return false"><span id="icon_<c:out value="${tree.name}"/><c:out value="${co.messageId}"/>">-</span></a>
                </c:otherwise>
                </c:choose>
            </td>
            <td class="forumRow">
                <c:set var="cssClass" value="forumTreeLink"/>
                <c:if test="${param.messageId == co.messageId}">
                    <c:set var="cssClass" value="forumTreeLinkSelected"/><b>
                </c:if>
                <a title="<c:out value="${co.subject}"/>" class="<c:out value="${cssClass}"/>" href="forumMessageList.jsp?threadId=<c:out value="${co.threadId}"/>&messageId=<c:out value="${co.messageId}"/>">
                    <c:out value="${co.subject}" escapeXml="false" />
                </a>
                <c:if test="${param.messageId == co.messageId}">
                    </b>
                </c:if>

                (<fmt:formatDate value="${co.modificationDate}" pattern="${globalDatetimeLong}"  />, <c:out value="${co.ownerId}"/>)

                <c:if test="${!empty co.children && !empty co.children[0]}">
                    <span id="<c:out value="${tree.name}"/><c:out value="${co.messageId}"/>" style="display: <c:out value="${cookieValue}"/>">
                        <c:set var="orig" scope="page" value="${root}"/>
                        <c:set var="root" scope="request" value="${co}"/>
                        <c:catch var="ie">
                            <jsp:include page="messageTreeRecursion.jsp" flush="true">
                                <jsp:param name="children" value="true" />
                            </jsp:include>
                        </c:catch>
                        <c:set var="root" scope="request" value="${orig}"/>
                    </span>
                </c:if>
            </td>
        </tr>
        <c:if test="${!param.children}">
            <tr height="0"><td colspan="2" class="forumRow"><span class="forumTreeSeparator"><img src="<c:url value='/cmsadmin/images/clear.gif'/>" height="0" width="0"></span></td></tr>
        </c:if>
    </c:forEach>
</table>



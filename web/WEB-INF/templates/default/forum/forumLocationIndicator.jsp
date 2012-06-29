<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="panel" value="${widget}"/>
<jsp:include page="../panel_header.jsp" flush="true"/>
<table width="100%">
    <tr>
        <td>
<c:if test="${(panel.childMap.currentForum.hidden) && (panel.childMap.currentForumLabel.hidden) }">
            <b>
</c:if>
            <x:display name="${panel.childMap.forumListing.absoluteName}"/>
<c:if test="${(panel.childMap.currentForum.hidden) && (panel.childMap.currentForumLabel.hidden) }">
            </b>
</c:if>
<c:if test="${!(panel.childMap.currentForum.hidden)}">
            &gt;
</c:if>
<c:if test="${!(panel.childMap.currentForumLabel.hidden)}">
            &gt;
</c:if>
<c:if test="${(panel.childMap.currentThreadLabel.hidden) && (panel.childMap.currentThread.hidden) && (panel.childMap.currentMessageLabel.hidden)}">
            <b>
</c:if>
            <x:display name="${panel.childMap.currentForum.absoluteName}"/>
            <x:display name="${panel.childMap.currentForumLabel.absoluteName}"/>
<c:if test="${(panel.childMap.currentThreadLabel.hidden) && (panel.childMap.currentThread.hidden) && (panel.childMap.currentMessageLabel.hidden)}">
            </b>
</c:if>
<c:if test="${!(panel.childMap.currentThreadLabel.hidden) || !(panel.childMap.currentThread.hidden) || !(panel.childMap.currentMessageLabel.hidden)}">
            &gt;
</c:if>
<c:if test="${(panel.childMap.currentMessageLabel.hidden)}">
            <b>
</c:if>
            <x:display name="${panel.childMap.currentThread.absoluteName}"/>
            <x:display name="${panel.childMap.currentThreadLabel.absoluteName}"/>
<c:if test="${(panel.childMap.currentMessageLabel.hidden)}">
            </b>
</c:if>
<c:if test="${!(panel.childMap.currentMessageLabel.hidden)}">
            &gt;
</c:if>

            <b><x:display name="${panel.childMap.currentMessageLabel.absoluteName}"/></b></td>
            </td>

    </tr>
</table>

<jsp:include page="../panel_footer.jsp" flush="true"/>
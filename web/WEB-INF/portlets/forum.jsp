<%@ page import="com.tms.collab.forum.ui.ForumPortlet"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<x:config>
    <page name="forumPortlet">
        <com.tms.collab.forum.ui.ForumPortlet name="portlet"/>
    </page>
</x:config>
<crt:set var="forward_forum" value="<%= ForumPortlet.FORWARD_FORUM %>"/>
<crt:set var="forward_thread" value="<%= ForumPortlet.FORWARD_THREAD %>"/>
<c:if test="${forward.name == forward_forum}">
    <script>document.location="<c:url value="/ekms/"/>forums/forumTopicList.jsp?forumId=<c:out value="${param.forumId}"/>";</script>
</c:if>
<c:if test="${forward.name == forward_thread}">
    <c:set var="strParam" value="threadId=${param.threadId}"/>
    <c:if test="${param.type == 'M'}">
        <c:set var="strParam" value="${strParam}&messageId=${param.messageId}"/>
    </c:if>
    <script>
        document.location="<c:url value="/ekms/"/>forums/forumMessageList.jsp?<c:out value="${strParam}"/>#highlight";
    </script>
</c:if>
<x:display name="forumPortlet.portlet"/>
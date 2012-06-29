<%@ page import="com.tms.collab.forum.ui.ForumPortlet"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>
<link ref="stylesheet" href="images/style.css">
<c:if test="${!(empty widget.entity)}">
    <table cellpadding="0" cellspacing="0" width="100%">
        <tr>
            <td class="forumRow" align="center">
                <table cellpadding="0" cellspacing="1" width="95%">
                    <c:choose>
                        <c:when test="${!(empty widget.forums)}">
                            <crt:set var="forum_event" value="<%= ForumPortlet.EVENT_FORUM %>"/>
                            <crt:set var="thread_event" value="<%= ForumPortlet.EVENT_THREAD %>"/>
                            <crt:set var="forum_key" value="<%= ForumPortlet.FORUM_KEY %>"/>
                            <crt:set var="thread_key" value="<%= ForumPortlet.THREAD_KEY %>"/>
                            <crt:set var="message_key" value="<%= ForumPortlet.MESSAGE_KEY %>"/>
                            <c:forEach items="${widget.forums}" var="forum">
                                <tr>
                                    <td class="forumRow">
                                        Forum:
                                        <x:event name="${widget.absoluteName}" type="${forum_event}" param="${forum_key}=${forum.forumId}">
                                            <b><c:out value="${forum.name}"/></b>
                                        </x:event>
                                    </td>
                                </tr>
                                <c:forEach items="${widget.threads[forum.forumId]}" var="thread">
                                    <tr>
                                        <td class="forumRow">
                                            <li>
                                                <x:event name="${widget.absoluteName}" type="${thread_event}" param="${thread_key}=${thread.threadId}&type=${thread.type}&${message_key}=${thread.id}">
                                                    <c:out value="${thread.subject}"/>
                                                </x:event>
                                                (<fmt:formatDate value="${thread.modificationDate}" pattern="${globalDatetimeLong}"/>) - <c:out value="${thread.username}"/>
                                            </li>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <tr><td><hr size="1"></td></tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr><td class="forumRow">No Forums Found</td></tr>
                        </c:otherwise>
                    </c:choose>
                </table>
            </td>
        </tr>
        <tr><td class="forumRow">&nbsp;</td></tr>
        <tr><td class="forumFooter"><img src="images/blank.gif" width="1" height="15"></td></tr>
    </table>
</c:if>
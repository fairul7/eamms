<%@ page import="org.apache.commons.lang.StringUtils,
                 com.tms.collab.forum.model.Message"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="list" value="${widget}"/>
<c:if test="${!(empty list.message)}">
    <script>alert("<c:out value="${list.message}"/>");</script>
</c:if>
<script language="javascript">
function selectPage(page)
{
    document.location="forumMessageList.jsp?threadId=<c:out value="${list.thread.threadId}"/>&iPage=" + page + "&currentPage=" + page;
}
</script>
<table border="0" align="center" cellspacing="1" cellpadding="4" class="forumBackground" width="100%">
    <form method="post">
        <tr>
            <td class="forumHeader" colspan="2">
                <a href="forums.jsp"><font class="forumHeader"><fmt:message key="general.label.forums"/></font></a>
                &gt; <a href="forumTopicList.jsp?forumId=<c:out value="${list.thread.forumId}" />"><font class="forumHeader"><c:out value="${list.forumName}"/></font></a>
                &gt; <c:out value="${list.thread.subject}" />
            </td>
        </tr>
        <tr><td class="forumFooter" colspan="2">&nbsp;</td></tr>
        <tr>
            <td class="forumRowLabel" align="right"><fmt:message key="forum.label.topic"/></td>
            <td class="forumRow"><c:out value="${list.thread.subject}" /></td>
        </tr>
        <tr>
            <td class="forumRowLabel" align="right">Author</td>
            <td class="forumRow"><c:out value="${list.thread.ownerId}" /></td>
        </tr>
        <tr>
            <td class="forumRowLabel" align="right">Date</td>
            <td class="forumRow"><fmt:formatDate value="${list.thread.creationDate}" pattern="${globalDatetimeLong}" /></td>
        </tr>
        <%--<tr><td class="forumRow" align="right" colspan="2">&nbsp;</td></tr>--%>
        <tr><td class="forumFooterLabel" colspan="2">Contents</td></tr>
        <tr>
            <td class="forumRow" colspan="2">
                <c:set var="content" value="${list.thread.content}"/>
                <%
                    String content = StringUtils.replace(((String)pageContext.getAttribute("content")), "\n", "<br>");
                    pageContext.setAttribute("translated", content);
                %>
                <c:out escapeXml="false" value="${translated}" />
            </td>
        </tr>
        <tr>
            <td class="forumRow" colspan="2" align="right">
                <input type="button" class="button" value="<fmt:message key="forum.label.postNewMessage"/>" onClick="document.location='forumMessageForm.jsp?threadId=<c:out value="${list.thread.threadId}"/>';">
            </td>
        </tr>
        <tr><td class="forumFooterLabel" colspan="2">Topic Threads</td></tr>
        <c:if test="${list.groupStartPage > 0}">
            <tr>
                <td colspan="2" class="forumRow" align="right">
                    <b><fmt:message key="forum.label.page"/>:</b>
                    <select onChange="selectPage(this.options[this.selectedIndex].value);" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:8.5pt; font-weight:normal">
                        <c:forEach begin="${list.groupStartPage}" end="${list.groupEndPage}" varStatus="status" >
                            <option value="<c:out value="${status.index}"/>" <c:if test="${list.currentPage==status.index}">SELECTED</c:if>><c:out value="${status.index}"/></option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
        </c:if>
        <c:choose>
            <c:when test="${empty list.postedMessages}">
                <tr><td class="forumRow" colspan="2">No Message Found</td></tr>
            </c:when>
            <c:otherwise>
                <c:set var="class" value="forumList1"/>
                <c:forEach items="${list.postedMessages}" var="postedMessage" varStatus="cnt">
                    <tr>
                        <td class="<c:out value="${class}"/>" colspan="2">
                            <table cellpadding="3" cellspacing="0" width="100%">
                                <tr>
                                    <td colspan="2" class="forumListSmall">
                                        <b>Posted by:</b> <c:out value="${postedMessage.ownerId}"/><br>
                                        <b>Subject:</b>
                                        <c:if test="${param.messageId == postedMessage.messageId}">
                                            <a name="highlight">
                                            <span class="highlight">
                                            <strong>
                                        </c:if>
                                            <c:out value="${postedMessage.subject}" />
                                        <c:if test="${param.messageId == postedMessage.messageId}">
                                            </strong>
                                            </span>
                                            </a>
                                        </c:if>
                                    </td>
                                    <td align="right" colspan="2" valign="top" class="forumListSmall"><fmt:formatDate value="${postedMessage.creationDate}" pattern="${globalDatetimeLong}" /></td>
                                </tr>
                            </table>
                            <hr size="1">
                            <table cellpadding="3" cellspacing="0" width="100%">
                                <%
                                    String translated = StringUtils.replace(((Message)pageContext.getAttribute("postedMessage")).getContent(), "\n", "<br>");
                                    pageContext.setAttribute("translated", translated);
                                %>
                                <tr><td><font class="<c:out value="${class}"/>"><c:out value="${translated}" escapeXml="false"/></font></td></tr>
                                <tr><td><input type="button" class="button" value="Reply To This Posting" onClick="document.location='forumMessageForm.jsp?threadId=<c:out value="${list.thread.threadId}"/>&parentMessageId=<c:out value="${postedMessage.messageId}"/>';"></td></tr>
                            </table>
                            <br>
                        </td>
                    </tr>
                    <c:choose>
                        <c:when test="${class == 'forumList1'}"><c:set var="class" value="forumList2"/></c:when>
                        <c:otherwise><c:set var="class" value="forumList1"/></c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        <tr>
            <td class="forumRow" colspan="2" align="right">
                <input type="button" class="button" value="<fmt:message key="forum.label.postNewMessage"/>" onClick="document.location='forumMessageForm.jsp?threadId=<c:out value="${list.thread.threadId}"/>';">
            </td>
        </tr>
        <c:if test="${list.groupStartPage > 0}">
            <tr>
                <td colspan="2" class="forumRow" align="right">
                    <b><fmt:message key="forum.label.page"/>:</b>
                    <select onChange="selectPage(this.options[this.selectedIndex].value);" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:8.5pt; font-weight:normal">
                        <c:forEach begin="${list.groupStartPage}" end="${list.groupEndPage}" varStatus="status" >
                            <option value="<c:out value="${status.index}"/>" <c:if test="${list.currentPage==status.index}">SELECTED</c:if>><c:out value="${status.index}"/></option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
        </c:if>
        <tr><td class="forumFooter" colspan="2">&nbsp;</td><tr>
    </form>
</table>

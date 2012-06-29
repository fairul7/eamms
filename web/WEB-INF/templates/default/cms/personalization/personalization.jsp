<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${requestScope.personalization}"/>
<c:if test="${!(empty(widget.setting))}">
    <table width="95%" class="personalizationTable" cellpadding="1" cellspacing="0" align="center">
        <tr><td class="personalizationHeader"><b><fmt:message key='general.label.sections'/></b><td></tr>
        <c:forEach items="${widget.sectionContent}" var="section">
        <tr>
            <td class="personalizationRow">
                    <a href="content.jsp?id=<c:out value="${section.key.id}"/>" class="personalizationLink"><c:out value="${section.key.name}"/></a>
                    <ul>
                    <c:forEach items="${section.value}" var="article">
                        <li><a href="content.jsp?id=<c:out value="${article.id}"/>" class="personalizationLink"><c:out value="${article.name}"/></a></li>
                    </c:forEach>
                    </ul>
            </td>
        </tr>
        </c:forEach>
    </table>
    <br>
    <table width="95%" class="personalizationTable" cellpadding="1" cellspacing="0" align="center">
        <tr><td class="personalizationHeader"><b><fmt:message key='general.label.forums'/></b><td></tr>
        <c:forEach items="${widget.forumContent}" var="forum">
        <tr>
            <td class="personalizationRow">
                <a href="forumTopicList.jsp?forumId=<c:out value="${forum.key.forumId}"/>" class="personalizationLink"><c:out value="${forum.key.name}"/></a>
                <ul>
                <c:forEach items="${forum.value}" var="thread">
                    <li><a href="forumMessageList.jsp?threadId=<c:out value="${thread.threadId}"/>" class="personalizationLink"><c:out value="${thread.subject}"/></a></li>
                </c:forEach>
                </ul>
            </td>
        </tr>
        </c:forEach>
    </table>
    <br>
</c:if>
    <table width="95%" cellpadding="2" cellspacing="1" class="personalizationRow" align="center">
        <tr><td align="right"><a href="personalizationSettings.jsp" class="personalizationLink"><fmt:message key='personalization.label.personalizeThisPage'/></a></td></tr>
    </table>

<%@ page import="com.tms.cms.portlet.ArticlePortlet,
                 java.util.Calendar,
                 com.tms.cms.core.model.ContentObject,
                 com.tms.cms.core.model.ContentManager"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
<table cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td>
            <table cellpadding="0" cellspacing="1" width="95%" align="center">
                <c:choose>
                    <c:when test="${!(empty widget.articles)}">
                        <crt:set var="id_key" value="<%= ArticlePortlet.KEY_ID %>"/>
                        <crt:set var="content_event" value="<%= ArticlePortlet.EVENT_CLICK %>"/>
                        <%
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.DATE, -7);
                        %>
                        <c:forEach items="${widget.articles}" var="section">
                            <c:if test="${! empty section.value}">
                                <tr>
                                    <td class="articleBody">
                                        <x:event name="${widget.absoluteName}" type="${content_event}" param="${id_key}=${section.key.id}">
                                            <b><c:out value="${section.key.name}"/></b>
                                        </x:event>
                                    </td>
                                </tr>
                                <tr><td><hr size="1"></td></tr>
                                <c:forEach items="${section.value}" var="article">
                                    <tr>
                                        <td class="articleBody">
                                            <ul>
                                                <li>
                                                    <x:event name="${widget.absoluteName}" type="${content_event}" param="${id_key}=${article.id}">
                                                        <c:out value="${article.name}"/>
                                                    </x:event>
                                                    <%
                                                        if(((ContentObject) pageContext.getAttribute("article")).getDate().after(calendar.getTime()))
                                                        {
                                                    %>
                                                    <font size="-2">[<font color="FF0000"><fmt:message key="portlet.articles.new"/></font>]</font>
                                                    <%
                                                        }
                                                    %>
                                                    <br>
                                                    <c:out value="${article.summary}" escapeXml="false" /><br>
                                                    <font class="articleSmall">
                                                        <c:if test="${! empty article.author}"><c:out value="${article.author}"/> - </c:if>
                                                        <fmt:formatDate value="${article.date}"/> | <fmt:message key="portlet.articles.views"/>:
                                                        <font class="articleRead"><c:out value="${widget.articleCount[article.id]}"/></font>
                                                    </font>
                                                </li>
                                            </ul>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </c:forEach>
                        <tr><td class="articleBody">&nbsp;</td></tr>
                    </c:when>
                    <c:otherwise>
                        <tr><td class="articleBody"><fmt:message key="portlet.articles.noArticles"/></td></tr>
                        <tr><td class="articleBody">&nbsp;</td></tr>
                    </c:otherwise>
                </c:choose>
            </table>
        </td>
    </tr>
    <tr>
        <td class="portletFooter">
            <img src="images/blank.gif" width="1" height="15">
            <x:permission permission="<%= ContentManager.PERMISSION_MANAGE_CONTENT %>" module="<%= ContentManager.class.getName() %>">
                <script>
                <!--
                    function newArticle(){
                        window.open('<c:url value="/ekms/content/popup/newArticle.jsp"/>','newContentPopup','scrollbars=yes,resizable=yes,width=700,height=500')
                    }
                //-->
                </script>
                <input class="button" value="<fmt:message key="portlet.articles.newArticle"/>" type="button" onClick="newArticle()"/>
            </x:permission>
        </td>
    </tr>
</table>
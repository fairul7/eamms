<%@ page import="com.tms.cms.article.Article,
                 com.tms.cms.core.model.ContentUtil"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="co" scope="request" value="${widget.contentObject}"/>

<%-- Script for article stock quote popup --%>
<script>
<!--
    function articleEmail() {
        window.open('contentEmail.jsp?id=<c:out value='${co.id}'/>', 'popupEmail', 'height=330,width=430,left=50,top=50,screenx=50,screeny=50,scrollbars=no');
        return false;
    }

    function articleStockQuoteFilter(exchange, counter) {
        window.open("<%= request.getContextPath() %>/stockquote/stockquote.jsp?exchange=" + exchange + "&counter=" + counter, "StockQuoteWindow", "toolbar=no,menubar=no,location=no,scrollbars=yes,resize=no,width=420,height=300");
    }
//-->
</script>

<%-- Display Edit Mode Options --%>
<x:template type="TemplateDisplayFrontEndEdit" properties="id=${co.id}" />
<x:template type="com.tms.cms.tdk.DisplayContentSubscriptionOption" properties="id=${co.id}" />

<%-- Display article header --%>
<p>
<b><span class="contentName"><c:out value="${co.name}"/></span></b>
<br>
<span class="contentDate"><fmt:formatDate pattern="${globalDatetimeLong}" value="${co.date}"/></span>
<br>
<c:if test="${!empty co.author}">
<span class="contentAuthor"><fmt:message key="article.label.author"/> <c:out value="${co.author}"/></span>
</c:if>
</p>

<%--Display profile--%>
<p>
<x:template type="TemplateDisplayContentProfile" properties="id=${co.id}&version=${co.version}"  />
</p>

<c:set var="pageMap" value="${co.pageMap}"/>
<c:choose>
    <c:when test="${empty pageMap['1'] || param.paging == '0'}">
    <%--    Display whole story --%>
        <p>
        <span class="contentBody">
            <%
                // display filtered content
                Article article = (Article)request.getAttribute("co");
                out.print(article.getFilteredContent(request, article.getContents()));
            %>
        </span>
        </p>

        <%--    Display comments and footer --%>
        <c:if test="${param.paging != '0'}">
            <p>
            <jsp:include page="displayContentCommentary.jsp" flush="true" />
            </p>

            <p>
            <br>
            <br>
            <span class="contentFooter">
                   <a class="contentFooter" href="contentPrint.jsp?id=<c:out value='${co.id}'/>&paging=0"><fmt:message key='article.label.printableVersion'/></a>
                   |
                   <a class="contentFooter" href="#" onClick="return articleEmail()"><fmt:message key='article.label.emailToFriend'/></a>
            </span>
            <br>
            <br>
            </p>
        </c:if>
    </c:when>
    <c:otherwise>
    <%--    Display paged story --%>

        <c:set var="page" value="${param.page}"/>
        <c:set var="story" scope="request" value="${pageMap[param.page]}"/>
        <c:if test="${empty story}">
            <c:set var="story" scope="request" value="${pageMap['1']}"/>
            <c:set var="page" value="1"/>
        </c:if>

        <p>
        <span class="contentBody">
            <%
                // display filtered content
                Article article = (Article)request.getAttribute("co");
                String story = (String)request.getAttribute("story");
                out.print(article.getFilteredContent(request, story));
            %>
        </span>
        </p>

        <p class="contentPaging" align="right">
        [ <fmt:message key='article.label.page'/>
        <c:forEach items="${pageMap}" varStatus="status">
            <c:choose>
                <c:when test="${page == status.index+1}"><b><c:out value="${status.index + 1}"/></b></c:when>
                <c:otherwise><a class="contentPageLink" href="content.jsp?id=<c:out value='${co.id}'/>&page=<c:out value='${status.index+1}'/>"><c:out value="${status.index + 1}"/></a></c:otherwise>
            </c:choose>
            <c:set var="total" scope="request" value="${status.count}"/>
        </c:forEach>
        ]
        </p>

        <%-- Display comments on last page --%>
        <c:if test="${page == total && param.paging != '0'}">
            <p>
            <jsp:include page="displayContentCommentary.jsp" flush="true" />
            </p>
       </c:if>

        <%-- Display footer --%>
        <c:if test="${param.paging != '0'}">
            <p>
            <br>
            <br>
            <span class="contentFooter">
                   <a class="contentFooter" href="contentPrint.jsp?id=<c:out value='${co.id}'/>&paging=0"><fmt:message key='article.label.printableVersion'/></a>
                   |
                   <a class="contentFooter" href="#" onClick="return articleEmail()"><fmt:message key='article.label.emailToFriend'/></a>
            </span>
            <br>
            <br>
            </p>
        </c:if>
    </c:otherwise>

</c:choose>


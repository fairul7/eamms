<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="searchResults" value="${widget}"/>

<c:choose>
<c:when test="${!empty searchResults.query && empty searchResults.results[0]}">

    <ul>
        <span class="contentBody"><b><fmt:message key='cms.label.noResults'/></b></span>
    </ul>

</c:when>
<c:otherwise>

    <ul>
    <c:forEach var="co" items="${searchResults.results}">
        <a href="content.jsp?id=<c:out value="${co.id}"/>" class="contentChildName"><c:out value="${co.name}"/></a>
        <br>
        <span class="contentChildDate"><fmt:formatDate pattern="${globalDateLong}" value="${co.date}"/></span>
        <br>
        <span class="contentChildAuthor"><c:out value="${co.author}"/></span>
        <p>
    </c:forEach>

    <%-- Paging --%>
    <c:if test="${!empty searchResults.results[0]}">
        <c:set var="queryParam" value="query"/>
        <c:if test="${!empty param.advQuery}">
            <c:set var="queryParam" value="advQuery"/>
        </c:if>
        <p class="contentPaging" align="right">
        [ <fmt:message key='general.label.page'/>
        <c:set var="pageNum" value="10"/>
        <c:set var="pageBuf" value="${pageNum/2}"/>
        <c:set var="pageStart" value="${searchResults.page - pageBuf}"/>
        <c:set var="pageEnd" value="${searchResults.page + pageBuf}"/>
        <c:forEach var="pg" begin="1" end="${searchResults.pageCount}" varStatus="stat">
            <c:choose>
                <c:when test="${!hidePage && (pg > 1) && (pg < pageStart)}">..
                    <c:set var="hidePage" value="${true}"/>
                </c:when>
                <c:when test="${!hidePage && (pg < searchResults.pageCount) && (pg > pageEnd)}">..
                    <c:set var="hidePage" value="${true}"/>
                </c:when>
                <c:when test="${(pg == 1) || (pg == searchResults.pageCount) || ((pg >= pageStart) && (pg <= pageEnd))}">
                    <c:set var="hidePage" value="${false}"/>
                    <c:if test="${(stat.index > 1) || (pg == pageStart)}"> | </c:if>
                    <c:choose>
                    <c:when test="${pg == searchResults.page}">
                        <span class="contentPageLink"><b><c:out value='${pg}'/></b></span>
                    </c:when>
                    <c:otherwise>
                        <c:url var="pageUrl" value="search.jsp">
                            <c:param name="${queryParam}" value="${searchResults.query}"/>
                            <c:param name="sort" value="${searchResults.sort}"/>
                            <c:param name="page" value="${pg}"/>
                            <c:if test="${!empty param.pageSize}">
                                <c:param name="pageSize" value="${searchResults.pageSize}"/>
                            </c:if>
                        </c:url>
                        <a class="contentPageLink" href="<c:out value='${pageUrl}'/>"><c:out value='${pg}'/></a>
                    </c:otherwise>
                    </c:choose>
                </c:when>
            </c:choose>
        </c:forEach>
        ]
        </p>
    </c:if>
    </ul>

</c:otherwise>
</c:choose>


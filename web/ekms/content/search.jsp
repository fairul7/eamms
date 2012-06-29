<%@ include file="/common/header.jsp" %>

<x:template type="TemplateProcessVote" />
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true" />

<!-- Search Form -->
<div align="center">
    <form method="POST">
    <input type="text" name="query" value="<c:out value='${param.query}'/>">
    <select name="sort">
        <option value=""<c:if test='${empty param.sort}'> selected</c:if>"><fmt:message key="general.label.closestMatch"/></option>
        <option value="date"<c:if test='${param.sort == "date"}'> selected</c:if>><fmt:message key="general.label.mostRecent"/></option>
    </select>
    <input type="submit" class="button" value="<fmt:message key="cms.label.search"/>">
    <br>
    [<a href="searchForm.jsp" style="color:blue; font-family:Arial; font-size:8pt"><fmt:message key="cms.label.advancedSearch"/></a>]
    </form>
</div>

<table cellpadding="10" cellspacing="0" width="100%" border="0">
    <tr>
        <td valign="top" class="contentBody">

    <!-- TDK: Search Results -->
    <c:set var="query" value="${param.query}"/>
    <c:if test="${!empty param.advQuery}">
    <c:set var="query" value="${widgets['searchFormPage.searchForm'].query}"/>
    </c:if>
    <x:template name="widget" type="TemplateDisplaySearchResults" properties="query=${query}&sort=${param.sort}" body="custom">

        <c:set var="searchResults" value="${widget}"/>

        <c:choose>
        <c:when test="${!empty searchResults.query && empty searchResults.results[0]}">

            <ul>
                <span class="contentBody"><b><fmt:message key='general.label.noResults'/></b></span>
            </ul>

        </c:when>
        <c:otherwise>

			<style>
				.hilite { background: yellow }
			</style>
            <c:forEach var="co" items="${searchResults.results}" varStatus="cnt">
                <hr size="1" style="border:1px dotted silver">
                <c:set var="wName" value="displayPath_${cnt.index}"/>
                <x:template name="${wName}" type="TemplateDisplayPath" properties="id=${co.id}&rootId=com.tms.cms.section.Section_Sections" body="custom">
                    <c:set var="pathToContentObject" value="${pageScope[wName].pathToContentObject}"/>

                    <c:if test="${!empty pathToContentObject[0]}">
                        <a class="contentPath" href="index.jsp"><fmt:message key='general.label.home'/></a> <span class="separator">&gt;</span>
                    </c:if>

                    <c:forEach items="${pathToContentObject}" var="pathObject" varStatus="ps">
                        <c:if test="${!ps.last}">
                            <a title="<c:out value="${pathObject.name}"/>"
                               class="contentPath"
                               href="<c:url value="content.jsp?id=${pathObject.id}"/>">
                                    <c:out value="${pathObject.name}"/></a>
                            <span class="separator">&gt;</span>
                        </c:if>
                    </c:forEach>
                </x:template>
                <br>
                <a href="<c:url value="content.jsp?id=${co.id}"/>&curpage=tt" class="contentSubheader"><c:out value="${co.name}"/></a>
                <br>
                <span class="contentAuthor"><fmt:formatDate pattern="MMMM d, yyyy" value="${co.date}"/></span>
                <br>
                <span class="contentAuthor"><c:out value="${co.author}"/></span>
                <br>
                <div>
                <c:out value="${co.propertyMap.hilite}" escapeXml="false"/>
                </div>
                <p>
            </c:forEach>

            <%-- Paging --%>
            <c:if test="${!empty searchResults.results[0]}">
                <c:set var="queryParam" value="query"/>
                <c:if test="${!empty param.advQuery}">
                    <c:set var="queryParam" value="advQuery"/>
                </c:if>
                <p class="eventNormal" align="right">
				<form name="searchPageForm" method="POST">
                	<input type="hidden" name="query" value="<c:out value="${query}"/>">
                	<input type="hidden" name="sort" value="<c:out value="${searchResults.sort}"/>">
                    <c:if test="${!empty param.pageSize}">
	                	<input type="hidden" name="pageSize" value="<c:out value="${searchResults.pageSize}"/>">
                	</c:if>
                	<input type="hidden" name="page" value="1">
                </form>
                <script>
                <!--
                	function goToPage(page) {
                		var searchPageForm = document.forms['searchPageForm'];
                		var pageField = searchPageForm.elements['page'];
                		pageField.value = page;
                		searchPageForm.submit();
                		return false;
                	}
                //-->
                </script>
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
                                <a class="rw_boldLink" href="<c:out value='${pageUrl}'/>" onclick="return goToPage('<c:out value='${pg}'/>')"><c:out value='${pg}'/></a>
                            </c:otherwise>
                            </c:choose>
                        </c:when>
                    </c:choose>
                </c:forEach>
                ]
                </p>
            </c:if>

        </c:otherwise>
        </c:choose>


    </x:template>
    <!-- TDK End: Search Results -->

            </table>
        </td>
    </tr>
</table>

<jsp:include page="includes/footer.jsp" flush="true" />
<%@ include file="/ekms/includes/footer.jsp" %>

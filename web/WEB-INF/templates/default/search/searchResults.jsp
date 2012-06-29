<%@ page import="kacang.services.indexing.SearchableModule,
                 com.tms.ekms.search.ui.SearchWidget"%>
<%@ include file="/common/header.jsp" %>

<c:set var="searchWidget" value="${widget}"/>

<c-rt:set var="searchEvent" value="<%= SearchWidget.EVENT_SEARCH_RESULT %>" />
<c-rt:set var="searchKey" value="<%= SearchableModule.SEARCH_PROPERTY_KEY %>" />
<c-rt:set var="searchModule" value="<%= SearchableModule.SEARCH_PROPERTY_MODULE_CLASS %>" />
<c-rt:set var="searchObject" value="<%= SearchableModule.SEARCH_PROPERTY_OBJECT_CLASS %>" />
<c-rt:set var="searchTitle" value="<%= SearchableModule.SEARCH_PROPERTY_TITLE %>" />
<c-rt:set var="searchDescription" value="<%= SearchableModule.SEARCH_PROPERTY_DESCRIPTION %>" />
<c-rt:set var="searchType" value="<%= SearchWidget.LABEL_SEARCH_TYPE %>" />

<table cellpadding="3" cellspacing="0" width="100%">
    <tr><td class="contentTitleFont"><span class="contentTitleFont">Search</span></td></tr>
    <tr><td class="contentBody">&nbsp;</td></tr>
    <tr>
        <td align="center">
            <table cellpadding="0" cellspacing="0" width="95%">
                <tr>
                    <td>
						<!-- Search Form -->
						<jsp:include page="searchForm.jsp" flush="true" />
						<!-- Search Results -->
						<table cellpadding="3" cellspacing="0" width="100%">
							<tr><td class="contentTitleFont"><span class="contentTitleFont"><fmt:message key="${searchWidget.selectedModule}"/></span></td></tr>
							<tr><td class="contentBody">&nbsp;</td></tr>
							<c:set var="base" value="${((searchWidget.page*searchWidget.pageSize)-searchWidget.pageSize)+1}"/>
							<tr><td class="contentBody" align="right"><b>Results <c:out value="${base}"/> - <c:out value="${(base+searchWidget.pageSize)-1}"/></b></td></tr>
							<tr>
								<td class="contentBody" align="center">
									<table cellpadding="0" cellspacing="0" width="95%">
										<tr>
											<td>

												<ul>
												<c:forEach var="item" items="${searchWidget.searchResults.searchResultItems}">
													<li>
														<div class="contentBody">
															<a href="<c:url value="?et=${searchEvent}&${searchKey}=${item.valueMap[searchKey]}&${searchModule}=${item.valueMap[searchModule]}&${searchObject}=${item.valueMap[searchObject]}&query=${param.query}" />" class="searchLink"><c:out value="${item.valueMap[searchTitle]}"/></a>
															<c:if test="${!empty item.valueMap[searchDate]}">
																<br><font class="searchDate"><fmt:formatDate value="${item.valueMap[searchDate]}" pattern="${globalDatetimeLong}" /></font>
															</c:if>
														</div>
														<div class="contentChildSummary"><c:out value="${item.valueMap[searchDescription]}" escapeXml="true" /></div>
														<br>
													</li>
												</c:forEach>

												<%-- Paging --%>
												<c:if test="${!empty searchWidget.searchResults.searchResultItems[0]}">
													<c:set var="queryParam" value="query"/>
													<c:if test="${!empty param.advQuery}">
														<c:set var="queryParam" value="advQuery"/>
													</c:if>
													<p class="contentPaging" align="right">
													[ <fmt:message key='general.label.page'/>
													<c:set var="pageNum" value="10"/>
													<c:set var="pageBuf" value="${pageNum/2}"/>
													<c:set var="pageStart" value="${searchWidget.page - pageBuf}"/>
													<c:set var="pageEnd" value="${searchWidget.page + pageBuf}"/>
													<c:forEach var="pg" begin="1" end="${searchWidget.pageCount}" varStatus="stat">
														<c:choose>
															<c:when test="${!hidePage && (pg > 1) && (pg < pageStart)}">..
																<c:set var="hidePage" value="${true}"/>
															</c:when>
															<c:when test="${!hidePage && (pg < searchWidget.pageCount) && (pg > pageEnd)}">..
																<c:set var="hidePage" value="${true}"/>
															</c:when>
															<c:when test="${(pg == 1) || (pg == searchWidget.pageCount) || ((pg >= pageStart) && (pg <= pageEnd))}">
																<c:set var="hidePage" value="${false}"/>
																<c:if test="${(stat.index > 1) || (pg == pageStart)}"> | </c:if>
																<c:choose>
																<c:when test="${pg == searchWidget.page}">
																	<span class="contentPageLink"><b><c:out value='${pg}'/></b></span>
																</c:when>
																<c:otherwise>
																	<c:url var="pageUrl" value="search.jsp">
																		<c:param name="${queryParam}" value="${searchWidget.query}"/>
																		<c:param name="${searchModule}" value="${searchWidget.selectedModule}"/>
																		<c:param name="${searchType}" value="${searchWidget.searchType}"/>
																		<c:param name="page" value="${pg}"/>
																		<c:if test="${!empty param.pageSize}">
																			<c:param name="pageSize" value="${searchWidget.pageSize}"/>
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
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

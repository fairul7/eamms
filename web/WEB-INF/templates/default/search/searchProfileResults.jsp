<%@ page import="kacang.services.indexing.SearchableModule,
                 com.tms.ekms.search.ui.SearchProfileDisplay,
                 com.tms.ekms.search.ui.SearchWidget"%>
<%@ include file="/common/header.jsp" %>

<c-rt:set var="searchEvent" value="<%= SearchWidget.EVENT_SEARCH_RESULT %>" />
<c-rt:set var="searchKey" value="<%= SearchableModule.SEARCH_PROPERTY_KEY %>" />
<c-rt:set var="searchModule" value="<%= SearchableModule.SEARCH_PROPERTY_MODULE_CLASS %>" />
<c-rt:set var="searchObject" value="<%= SearchableModule.SEARCH_PROPERTY_OBJECT_CLASS %>" />
<c-rt:set var="searchTitle" value="<%= SearchableModule.SEARCH_PROPERTY_TITLE %>" />
<c-rt:set var="searchDescription" value="<%= SearchableModule.SEARCH_PROPERTY_DESCRIPTION %>" />
<c-rt:set var="searchType" value="<%= SearchProfileDisplay.LABEL_SEARCH_TYPE%>" />
<c-rt:set var="searchDate" value="<%= SearchableModule.SEARCH_PROPERTY_DATE %>" />
<!-- Search Results -->
                   <c:forEach var="result" items="${widget.searchResultsSummary}">
                       <c:choose>
                           <c:when test="${result.value.searchResultItems[0] != null}">
                              <table cellpadding="3" cellspacing="0" width="100%">
                                   <tr><td class="contentTitleFont"><span class="contentTitleFont"><fmt:message key="${result.key}"/></span></td></tr>
                                   <tr><td class="contentBody">&nbsp;</td></tr>
                                   <tr>
                                      <td class="contentBody" align="center">
                                           <table cellpadding="0" cellspacing="0" width="95%">
                                               <tr>
                                                   <td>
                                                      <ul>
                                                       <c:forEach var="item" items="${result.value.searchResultItems}">
                                                           <li>
                                                              <div class="contentBody">
                                                                  <a href="<c:url value="?et=${searchEvent}&${searchKey}=${item.valueMap[searchKey]}&${searchModule}=${item.valueMap[searchModule]}&${searchObject}=${item.valueMap[searchObject]}&query=${param.query}"/>" class="searchLink"><c:out value="${item.valueMap[searchTitle]}"/></a>
                                                                   <c:if test="${!empty item.valueMap[searchDate]}">
                                                                       <br><font class="searchDate"><fmt:formatDate value="${item.valueMap[searchDate]}" pattern="${globalDatetimeLong}" /></font>
                                                                   </c:if>
                                                               </div>
                                                              <div class="contentChildSummary"><c:out value="${item.valueMap[searchDescription]}" escapeXml="true" /></div>
                                                              <br>
                                                           </li>
                                                       </c:forEach>
                                                       <c:if test="${result.value.totalSize > widget.summaryPageSize}">
                                                       <p class="contentPaging">
                                                       [ <a href="/ekms/search/search.jsp?query=<c:out value='${widget.query}'/>&<c:out value='${searchModule}'/>=<c:out value='${result.key}'/>&<c:out value="${searchType}"/>=<c:out value="${widget.selectedSearchType[result.key]}"/>">More - <c:out value="${result.value.totalSize}"/> Results</a> ]
                                                       </p>
                                                       </c:if>
                                                   </td>
                                                </tr>
                                           </table>
                                      </td>
                                   </tr>
                               </table>
                           </c:when>
                          <c:otherwise>
                               <table cellpadding="3" cellspacing="0" width="100%">
                                 <tr><td class="contentBody">&nbsp;</td></tr>
                                 <tr><td class="contentTitleFont"><span class="contentTitleFont"><fmt:message key="${result.key}"/></span></td></tr>
                                 <tr><td class="contentBody" align="left"><fmt:message key="general.label.noResults"/></td></tr>
                              </table>
                          </c:otherwise>
                        </c:choose>
                        <br>
                    </c:forEach>
<%@ page import="com.tms.ekms.search.ui.SearchProfileDisplay,
                 java.util.Collection,
                 java.util.ArrayList,
                 com.tms.ekms.search.ui.SearchWidget,
                 kacang.services.indexing.SearchableModule"%>

<%@ include file="/common/header.jsp" %>
<c:set var="searchProfileDisplay" value="${widget}"/>
<c:set var="searchProfileDisplay" value="${widget}" scope="page"/>
<c-rt:set var="typeSearch" value="<%= SearchProfileDisplay.LABEL_SEARCH_ONLY %>"/>
<c-rt:set var="typeFullText" value="<%= SearchProfileDisplay.LABEL_FULLTEXT_ONLY %>"/>
<c-rt:set var="typeAll" value="<%= SearchProfileDisplay.LABEL_ALL_SEARCHES %>"/>
<c-rt:set var="searchType" value="<%= SearchProfileDisplay.LABEL_SEARCH_TYPE %>"/>

<c-rt:set var="forward_cancel" value="<%= SearchProfileDisplay.FORWARD_CANCEL %>" />
<c-rt:set var="forward_run" value="<%= SearchProfileDisplay.FORWARD_RUN %>" />

<c:if test="${forward_action == forward_cancel}">
    <script>
        document.location="searchProfile.jsp";
    </script>
</c:if>
        <!-- Search Form -->
        <%
    SearchProfileDisplay widget = (SearchProfileDisplay) pageContext.getAttribute("searchProfileDisplay");

    Object[] modules = widget.getSearchableModuleList().keySet().toArray();
    String[] selectedModules = widget.getModules();
    Collection selected = new ArrayList();
    if(selectedModules != null)
    {
        for(int i = 0; i < selectedModules.length; i++)
            selected.add(selectedModules[i]);
    }
%>

<table cellpadding="3" cellspacing="0" width="100%">
      <tr><td class="contentTitleFont"><span class="contentTitleFont"><fmt:message key="searchprofile.label.details"/> - <c:out value="${widget.profileName}" /></span></td></tr>
      <tr><td class="contentBody">&nbsp;</td></tr>
      <tr>
        <td align="center">
            <table cellpadding="0" cellspacing="0" width="95%">
                <tr>
                    <td>
    <!-- display form -->
     <form method="post" action="?">
        <input disabled type="text" name="query" value="<c:out value="${widget.query}" />" />
        <br>

        <c:set var="display" value="${param.display}"/>
        <c:if test="${empty display}">
            <c:set var="display" value="none"/>
        </c:if>
            <table width="95%" cellspacing="1" cellpadding="3">
            <c:forEach var="module" items="${searchProfileDisplay.searchableModuleList}" varStatus="stat">
                <tr>
                    <td valign="top" width="50%" class="searchRow">
                        <c:set var="currentModule" value="${module.key.class.name}" scope="page"/>
                        <input disabled type="checkbox" name="modules" value="<c:out value='${module.key.class.name}'/>"
                        <%
                            String current = (String) pageContext.getAttribute("currentModule");
                            if(selected.contains(current) || selectedModules == null)
                            {
                        %>
                            CHECKED
                        <%
                            }
                        %>
                        >
                        <fmt:message key="${module.key.class.name}"/>

                        <c:if test="${typeAll == module.value}">
                            <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

                            <input disabled type="radio" name="<c:out value="${module.key.class.name}${searchType}"/>" value="<c:out value="${typeSearch}"/>"
                            <% if(SearchProfileDisplay.LABEL_SEARCH_ONLY.equals(widget.getSelectedSearchType().get(current)) || (!SearchProfileDisplay.LABEL_FULLTEXT_ONLY.equals(widget.getSelectedSearchType().get(current)))) { %>
                                CHECKED
                            <% } %>
                            ><fmt:message key="general.label.normalSearch"/>
                            <input disabled type="radio" name="<c:out value="${module.key.class.name}${searchType}"/>" value="<c:out value="${typeFullText}"/>"
                            <% if(SearchProfileDisplay.LABEL_FULLTEXT_ONLY.equals(widget.getSelectedSearchType().get(current))) { %>
                                CHECKED
                            <% } %>
                            ><fmt:message key="general.label.fullTextSearch"/>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
             <tr>
                <td class="searchRow" valign="top" width="50%">
                    <input type="hidden" name="profileId" value="<c:out value="${param.profileId}" />" />
                    <input type="checkbox" name="allMatches" value="allMatches"

                   <c:if test="${widget.allMatches}">

                        CHECKED

                    </c:if>
                  /><fmt:message key="searchprofile.label.allmatch" /> <br>
                    <input type="submit" value="<%= SearchProfileDisplay.FORWARD_RUN %>" class="button" name="action"/>
                    <input type="submit" value="<%= SearchProfileDisplay.FORWARD_CANCEL %>" class="button" name="action"/>
                </td>
            </tr>
            
        </table>
</form>
                </td>
             </tr>
        </table>

<c:if test="${forward_action == forward_run}">
    <jsp:include page="searchProfileResults.jsp" />
</c:if>
        </td>
    </tr>
</table>



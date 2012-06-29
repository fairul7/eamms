<%@ page import="com.tms.ekms.search.ui.SearchWidget,
                 java.util.Collection,
                 java.util.ArrayList,
                 kacang.model.Module,
                 java.util.Iterator"%>
<%@ include file="/common/header.jsp" %>
<c:set var="searchWidget" value="${widget}"/>
<c:set var="searchWidget" value="${widget}" scope="page"/>
<c-rt:set var="typeSearch" value="<%= SearchWidget.LABEL_SEARCH_ONLY %>"/>
<c-rt:set var="typeFullText" value="<%= SearchWidget.LABEL_FULLTEXT_ONLY %>"/>
<c-rt:set var="typeAll" value="<%= SearchWidget.LABEL_ALL_SEARCHES %>"/>
<c-rt:set var="searchType" value="<%= SearchWidget.LABEL_SEARCH_TYPE %>"/>
<!-- Search Form -->
<%
    SearchWidget widget = (SearchWidget) pageContext.getAttribute("searchWidget");
    Object[] modules = widget.getSearchableModuleList().keySet().toArray();
    String[] selectedModules = request.getParameterValues("modules");
    Collection selected = new ArrayList();
    if(selectedModules != null)
    {
        for(int i = 0; i < selectedModules.length; i++)
            selected.add(selectedModules[i]);
    }
%>
<script src="<c:url value="/common/tree/tree.js"/>"></script>
<form method="post" action="?">
    <input type="text" name="query" value="<c:out value='${param.query}'/>">
    <input type="submit" value="<fmt:message key="general.label.search"/>" class="button">
    <input type="submit" value="<fmt:message key="general.label.options"/>" class="button" onClick="treeToggle('searchWidget_modules'); return false;">
    <br>
    <c:set var="display" value="${param.display}"/>
    <c:if test="${empty display}">
        <c:set var="display" value="none"/>
    </c:if>
    <div id="searchWidget_modules" style="display: <c:out value="${display}"/>; align: center">
        <table width="95%" cellspacing="1" cellpadding="3">
            <%--<tr>
                <td valign="top" width="50%" class="searchRow">
                    <%
                        for(int i = 0; i <= (widget.getSearchableModuleList().size()/2); i++)
                        {
                            Module module = (Module) modules[i];
                            String currentModule = module.getClass().getName();
                            String searchType = (String) widget.getSearchableModuleList().get(module);
                    %>
                        <input type="checkbox" name="modules" value="<%= currentModule %>"
                    <%
                            if(selected.contains(currentModule) || selectedModules == null)
                            {
                    %>
                        CHECKED
                    <%
                            }
                    %>
                        ><%= module.getClass().getName() %>
                    <%
                            if(SearchWidget.LABEL_ALL_SEARCHES.equals(searchType))
                            {
                    %>
                                <br>
                                <input type="radio" name="<%= currentModule %><%= SearchWidget.LABEL_SEARCH_TYPE %>" value="<%= SearchWidget.LABEL_SEARCH_ONLY %>"
                    <%
                                if(SearchWidget.LABEL_SEARCH_ONLY.equals(widget.getSelectedSearchType().get(currentModule)) || (!SearchWidget.LABEL_FULLTEXT_ONLY.equals(widget.getSelectedSearchType().get(currentModule))))
                                {
                    %>
                                    CHECKED
                    <%          }

                    %>
                                >Normal Search
                                <input type="radio" name="<%= currentModule %><%= SearchWidget.LABEL_SEARCH_TYPE %>" value="<%= SearchWidget.LABEL_FULLTEXT_ONLY %>"
                    <%
                                if(SearchWidget.LABEL_FULLTEXT_ONLY.equals(widget.getSelectedSearchType().get(currentModule)))
                                {
                                    %>CHECKED<%
                                } %>>Full Text Search<%
                            }
                        }
                    %>
                </td>
                <td valign="top" width="50%" class="searchRow">
                    <%
                        for(int i = (widget.getSearchableModuleList().size()/2) + 1; i < modules.length; i++)
                        {
                            Module module = (Module) modules[i];
                            String currentModule = module.getClass().getName();
                            String searchType = (String) widget.getSearchableModuleList().get(module);
                    %>
                        <input type="checkbox" name="modules" value="<%= currentModule %>"
                    <%
                            if(selected.contains(currentModule) || selectedModules == null)
                            {
                    %>
                        CHECKED
                    <%
                            }
                    %>
                        ><%= module.getClass().getName() %>
                    <%
                            if(SearchWidget.LABEL_ALL_SEARCHES.equals(searchType))
                            {
                    %>
                                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input type="radio" name="<%= currentModule %><%= SearchWidget.LABEL_SEARCH_TYPE %>" value="<%= SearchWidget.LABEL_SEARCH_ONLY %>"
                    <%
                                if(SearchWidget.LABEL_SEARCH_ONLY.equals(widget.getSelectedSearchType().get(currentModule)) || (!SearchWidget.LABEL_FULLTEXT_ONLY.equals(widget.getSelectedSearchType().get(currentModule))))
                                {
                    %>
                                    CHECKED
                    <%          }

                    %>
                                >Normal Search
                                <input type="radio" name="<%= currentModule %><%= SearchWidget.LABEL_SEARCH_TYPE %>" value="<%= SearchWidget.LABEL_FULLTEXT_ONLY %>"
                    <%
                                if(SearchWidget.LABEL_FULLTEXT_ONLY.equals(widget.getSelectedSearchType().get(currentModule)))
                                {
                                    %>CHECKED<%
                                } %>>Full Text Search<%
                            }
                        }
                    %>
                </td>
            </tr>--%>
            <c:forEach var="module" items="${searchWidget.searchableModuleList}" varStatus="stat">
                <tr>
                    <td valign="top" width="50%" class="searchRow">
                        <c:set var="currentModule" value="${module.key.class.name}" scope="page"/>
                        <input type="checkbox" name="modules" value="<c:out value='${module.key.class.name}'/>"
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
                        <%--<c:out value="${module.key.class.name}"/>--%>
                        <c:if test="${typeAll == module.value}">
                            <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="radio" name="<c:out value="${module.key.class.name}${searchType}"/>" value="<c:out value="${typeSearch}"/>"
                            <% if(SearchWidget.LABEL_SEARCH_ONLY.equals(widget.getSelectedSearchType().get(current)) || (!SearchWidget.LABEL_FULLTEXT_ONLY.equals(widget.getSelectedSearchType().get(current)))) { %>
                                CHECKED
                            <% } %>
                            ><fmt:message key="general.label.normalSearch"/>
                            <input type="radio" name="<c:out value="${module.key.class.name}${searchType}"/>" value="<c:out value="${typeFullText}"/>"
                            <% if(SearchWidget.LABEL_FULLTEXT_ONLY.equals(widget.getSelectedSearchType().get(current))) { %>
                                CHECKED
                            <% } %>
                            ><fmt:message key="general.label.fullTextSearch"/>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td>
                    <input type="text" name="profilename"/>
                    <input type="submit" value="<fmt:message key="searchprofile.label.save" />" class="button" name="action"/>
                </td>
            </tr>
        </table>
    </div>
</form>
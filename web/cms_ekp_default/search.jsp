<%@ include file="/common/header.jsp" %>

<jsp:include page="includes/header.jsp" flush="true"  />

    <div class="siteBodyHeader">
        Search
    </div>

    <!-- Search Form -->
    <div align="center">
        <form>
        Search
        <input type="text" name="query" value="<c:out value='${param.query}'/>">
        <select name="sort">
            <option value=""<c:if test='${empty param.sort}'> selected</c:if>">Closest Match</option>
            <option value="date"<c:if test='${param.sort == "date"}'> selected</c:if>>Most Recent</option>
        </select>
        <input type="submit" class="button" value="Go">
        <br>
        [<a href="searchForm.jsp" style="color:blue; font-family:Arial; font-size:8pt">Advanced Search</a>]
        </form>
    </div>

    <p>

    <!-- TDK: Search Results -->
    <c:set var="query" value="${param.query}"/>
    <c:if test="${!empty param.advQuery}">
    <c:set var="query" value="${widgets['searchFormPage.searchForm'].query}"/>
    </c:if>
    <x:template type="TemplateDisplaySearchResults" properties="query=${query}&sort=${param.sort}" />
    <!-- TDK End: Search Results -->

<jsp:include page="includes/footer.jsp" flush="true"  />


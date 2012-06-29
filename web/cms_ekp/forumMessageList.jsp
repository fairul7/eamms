<%@ include file="/common/header.jsp" %>

<x:template type="TemplateDisplaySetup" name="setup" scope="request"/>

<jsp:include page="includes/header.jsp" flush="true"  />

    <div class="siteBodyHeader">
        Forum Messages
    </div>

    <p>
    <c:choose>
    <c:when test="${setup.propertyMap.siteThreadedForum}">
        <x:template type="TemplateDisplayForumMessageTree" />
    </c:when>
    <c:otherwise>
        <x:template type="TemplateDisplayForumMessageList" />
    </c:otherwise>
    </c:choose>

<jsp:include page="includes/footer.jsp" flush="true"  />


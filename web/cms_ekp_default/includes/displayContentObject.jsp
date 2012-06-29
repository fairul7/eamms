<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="co" value="${widget.contentObject}" scope="request"/>

<c:choose>
<c:when test="${!widget.restricted && (co.class.name == 'com.tms.cms.section.Section')}">

<x:template type="TemplateDisplayFrontEndEdit" properties="id=${co.id}" />
<x:template type="TemplateDisplayContentSubscriptionOption" properties="id=${co.id}" />

    <p>
    <span class="contentName"><c:out value="${co.name}"/></span>
    </p>

    <p class="contentBody"><c:out value="${co.summary}" escapeXml="false" /></p>
    <x:template
        type="TemplateDisplaySubsections"
        properties="id=${co.id}&noHeader=true&orphans=true"
        body="/cms_ekp_default/includes/displaySubsections.jsp" />

<hr size=1 color="#DDDDDD">

<p>
<x:template
    name="DisplayContentChildren"
    type="TemplateDisplayChildren"
    properties="id=${co.id}&types=com.tms.cms.article.Article,com.tms.cms.document.Document,com.tms.cms.page.Page"
    body="/cms_ekp_default/includes/displayContentChildren.jsp"
/>
</p>

<%--Display commentary--%>
<p>
<jsp:include page="displayContentCommentary.jsp" flush="true" />
</p>

</c:when>
<c:otherwise>

    <x:template type="TemplateDisplayContent" properties="hideSummary=true"  />

</c:otherwise>
</c:choose>

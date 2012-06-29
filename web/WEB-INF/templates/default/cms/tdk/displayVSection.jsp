<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="co" value="${widget.contentObject}"/>

<x:template type="TemplateDisplayFrontEndEdit" properties="id=${co.id}" />

<p>
<b><span class="contentName"><c:out value="${co.name}" escapeXml="false" /></span></b>
</p>

<span class="contentBody">
    <c:out value="${co.summary}" escapeXml="false" />
</span>

<p>
<c:set var="children" value="${co.contentObjectList}"/>
<c:forEach var="child" items="${children}">
        <a href="content.jsp?id=<c:out value="${child.id}"/>" class="contentChildName"><c:out value="${child.name}"/></a>
        <br>
        <span class="contentChildDate"><fmt:formatDate pattern="${globalDateLong}" value="${child.date}"/></span>
        <br>
        <span class="contentChildAuthor"><c:out value="${child.author}"/></span>
        <p>
        <span class="contentChildSummary">
        <c:out value="${child.summary}" escapeXml="false" />
        </span>
        </p>
</c:forEach>
<p>

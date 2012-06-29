<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="co" value="${widget.contentObject}"/>

<p>
<b><span class="contentName"><c:out value="${co.name}"/></span></b>
<br>
<span class="contentDate"><fmt:formatDate pattern="${globalDatetimeLong}" value="${co.date}"/></span>
<br>
<span class="contentAuthor"><c:out value="${co.author}"/></span>
</p>

<hr size="1">

<p>
<span class="contentBody"><c:out value="${co.summary}"/></span>
</p>

<p>
<x:template
    name="DisplayContentChildren"
    type="com.tms.cms.tdk.DisplayContentChildren"
    properties="id=${co.id}&page=${param.page}" />

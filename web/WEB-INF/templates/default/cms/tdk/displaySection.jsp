<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="co" value="${widget.contentObject}"/>

<x:template type="TemplateDisplayFrontEndEdit" properties="id=${co.id}" />
<x:template type="com.tms.cms.tdk.DisplayContentSubscriptionOption" properties="id=${co.id}" />

<p>
<b><span class="contentName"><c:out value="${co.name}" escapeXml="false" /></span></b>
</p>

<span class="contentBody">
    <c:out value="${co.summary}" escapeXml="false" />
</span>

<p>
<x:template
    name="DisplayContentChildren"
    type="com.tms.cms.tdk.DisplayContentChildren"
    properties="id=${co.id}" />
</p>

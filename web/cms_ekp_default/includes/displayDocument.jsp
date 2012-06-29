<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="co" scope="request" value="${widget.contentObject}"/>

<x:template type="TemplateDisplayFrontEndEdit" properties="id=${co.id}" />
<x:template type="com.tms.cms.tdk.DisplayContentSubscriptionOption" properties="id=${co.id}" />

<p>
<b><span class="contentName"><c:out value="${co.name}"/></span></b>
<br>
<span class="contentDate"><fmt:formatDate pattern="${globalDatetimeLong}" value="${co.date}"/></span>
<br>
<c:if test="${!empty co.author}">
<span class="contentAuthor"><fmt:message key="document.label.author"/> <c:out value="${co.author}"/></span>
</c:if>
</p>

<p>
<span class="contentBody">
<c:out value="${co.summary}" escapeXml="false" />
</span>
</p>

<c:if test="${!empty co.fileName}">
<hr size="1">
<p>
    <a class="contentOptionLink" href="<%= request.getContextPath() %>/cms/documentstorage/<c:out value="${co.id}/${co.fileName}"/>" target="_blank"><c:out value="${co.fileName}"/></a>
    <span class="contentOption">
    <br>
    <c:out value="${co.fileSize}"/> <fmt:message key='general.label.bytes'/>
    <br>
    <c:out value="${co.contentType}"/>
    </span>
</p>
</c:if>

<%--Display profile--%>
<p>
<x:template type="TemplateDisplayContentProfile" properties="id=${co.id}&version=${co.version}"  />
</p>

<%--Display commentary--%>
<p>
<jsp:include page="displayContentCommentary.jsp" flush="true" />
</p>

<script>
<!--
    location.href="<c:url value="/documentstorage/${co.id}/${co.fileName}"/>";
//-->
</script>


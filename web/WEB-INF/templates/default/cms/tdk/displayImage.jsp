<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="co" scope="request" value="${widget.contentObject}"/>

<x:template type="TemplateDisplayFrontEndEdit" properties="id=${co.id}" />

<p>
<b><span class="contentName"><c:out value="${co.name}"/></span></b>
<br>
<span class="contentDate"><fmt:formatDate pattern="${globalDatetimeLong}" value="${co.date}"/></span>
<br>
<span class="contentAuthor"><c:out value="${co.author}"/></span>
</p>

<p>
<span class="contentBody">
<c:out value="${co.summary}" escapeXml="false" />
</span>
</p>

<p>
<img src="<%= request.getContextPath() %>/storage<c:out value="${co.filePath}"/>">
</p>

<c:if test="${!empty co.fileName}">
<hr>
<p>
    <a class="contentOptionLink" href="<%= request.getContextPath() %>/storage<c:out value="${co.filePath}"/>" target="_blank"><c:out value="${co.fileName}"/></a>
    <span class="contentOption">
    <br>
    <c:out value="${co.fileSize}"/> bytes
    <br>
    <c:out value="${co.contentType}"/>
    </span>
</p>
</c:if>

<%--Display profile--%>
<p>
<x:template type="com.tms.cms.profile.ui.DisplayContentProfile" properties="id=${co.id}&version=${co.version}"  />
</p>

<%--Display commentary--%>
<p>
<jsp:include page="displayContentCommentary.jsp" flush="true" />
</p>



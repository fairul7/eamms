<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="co" value="${widget.contentObject}"/>

<br>
<%-- Display Edit Mode Options --%>
<x:template type="TemplateDisplayFrontEndEdit" properties="id=${co.id}" />


<p>
<a href="<c:out value="${co.url}"/>" class="contentName"><c:out value="${co.name}"/></a>
</p>

<p>
<span class="contentBody">
<c:out value="${co.summary}" escapeXml="false" />
</span>
</p>

<%--Display profile--%>
<p>
<x:template type="com.tms.cms.profile.ui.DisplayContentProfile" properties="id=${co.id}&version=${co.version}"  />
</p>

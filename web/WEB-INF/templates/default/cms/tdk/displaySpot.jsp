<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="co" value="${widget.contentObject}"/>

<x:template type="TemplateDisplayFrontEndEdit" properties="id=${co.id}" />

<c:out value="${co.summary}" escapeXml="false" />

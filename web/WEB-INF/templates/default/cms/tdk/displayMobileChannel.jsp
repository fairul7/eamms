<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="channel" scope="request" value="${widget.channel}"/>

<c:if test="${!empty channel}">
    <x:template type="TemplateDisplayContent" properties="id=${channel.contentId}" />
</c:if>
<c:if test="${empty channel}">
    <fmt:message key='cms.label.channelUnavailable'/>
</c:if>

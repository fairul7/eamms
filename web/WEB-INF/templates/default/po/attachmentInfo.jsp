<%@ page import="java.net.URLEncoder"%>
<%@include file="/common/header.jsp" %>

<script>
<!--
    function openAttachmentWindow() {
        window.open('<c:url value="/ekms/purchaseordering/attachment.jsp" />','messaging', 'scrollbars=yes,resizable=yes,width=600,height=380');
        return false;
    }
//-->
</script>

<c:set var="w" value="${widget}" />
<c:set var="map" value="${w.attachmentMap}" />
<div id="attachmentItems">
<c:forEach var="a" items="${map}">
    <c:set var="key" value="${a.key}" />
    <%
        String key = (String) pageContext.getAttribute("key");
        key =  URLEncoder.encode(key, "UTF-8");
        pageContext.setAttribute("encodedKey", key);
    %>
    <li><c:out value="${a.key}" /></li>
</c:forEach>
</div>
<c:if test="${! empty map}"><br></c:if>
<a href="" onclick="return openAttachmentWindow()"><fmt:message key='messaging.message.add/RemoveAttachments'/></a>

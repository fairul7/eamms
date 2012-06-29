<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
				 com.tms.sam.po.ui.AttachmentForm"%>
<%@include file="/common/header.jsp"%>

<x:config>
	<page name="attachmentPage">
		<com.tms.sam.po.ui.AttachmentForm name="attachment"/>
	</page>
</x:config>

<c-rt:set var="forwardError" value="<%= AttachmentForm.FORWARD_ERROR %>" />

<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>

<c-rt:set var="forwardDone" value="<%= AttachmentForm.FORWARD_DONE %>" />

<c:if test="${forward.name eq forwardDone}">
    <c:set var="map" value="${sessionScope.attachmentMap}" />
    <c:set var="innerHtml"><c:forEach var="a" items="${map}"><c:set var="key" value="${a.key}" /><%
                String key = (String) pageContext.getAttribute("key");
                //key = Util.escapeJavaScript(key);
                pageContext.setAttribute("key", key);
            %><li><c:out value="${pageScope.key}" /></li></c:forEach></c:set>
    <script>
        o = window.opener;
        e = o.document.getElementById('attachmentItems');
        e.innerHTML = "<c:out value="${innerHtml}" escapeXml="false" />";
        o.focus();
        window.close();
    </script>
</c:if>

<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
    <title>Attachment</title>
</head>

<body onload="focus()" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" bgcolor="#FFFFFF">

<x:display name="attachmentPage.attachment" />

</body>
</html>
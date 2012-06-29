<%@include file="/common/header.jsp"%>

<x:config>
	<page name="viewMedia">
		<com.tms.cms.medialib.ui.MediaView name="mediaView" />
	</page>
</x:config>

<c:if test="${!empty param.id}">
	<x:set name="viewMedia.mediaView" property="mediaId" value="${param.id}" />
	<x:set name="viewMedia.mediaView" property="ip" value="${pageContext.request.remoteAddr}" />
</c:if>

<jsp:include page="includes/header.jsp"/>
<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
    <title>Enterprise Knowledge Portal</title>
</head>

<body>
<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
			<x:display name="viewMedia.mediaView" />
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>
</body>
</html>
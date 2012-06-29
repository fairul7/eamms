<%@include file="/common/header.jsp"%>

<x:config>
	<page name="editMedia">
		<com.tms.cms.medialib.ui.MediaFormPopupEdit name="mediaFormEdit" />
	</page>
</x:config>

<c:if test="${forward.name eq 'success'}">
	<c:set var="redirectedForward" value="success" scope="session"/>
	<c:redirect url="popupEditMedia.jsp"/>
</c:if>
<c:if test="${forward.name eq 'failure'}">
	<c:set var="redirectedForward" value="failure" scope="session"/>
	<c:redirect url="popupEditMedia.jsp"/>
</c:if>

<c:if test="${!empty param.id}">
	<x:set name="editMedia.mediaFormEdit" property="mediaId" value="${param.id}" />
	<x:set name="editMedia.mediaFormEdit" property="ip" value="${pageContext.request.remoteAddr}" />
</c:if>

<jsp:include page="includes/header.jsp"/>
<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
    <title>Enterprise Knowledge Portal</title>
    
    <script language="JavaScript">
    	function reloadOpenerWindow() {
    		window.opener.location.reload();
    		//window.opener.location = '<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/editAlbum.jsp';
    	}
    </script>
</head>

<body onunload="reloadOpenerWindow()">
<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
	<c:if test="${!empty redirectedForward}">
   		<c:if test="${redirectedForward eq 'success'}">
   			<c:set var="redirectedForward" value="" scope="session"/>
			<script language="JavaScript">
				alert("<fmt:message key='medialib.message.mediaUpdateSuccess'/>");
			</script>
   		</c:if>
   		<c:if test="${redirectedForward eq 'failure'}">
   			<c:set var="redirectedForward" value="" scope="session"/>
   			<script language="JavaScript">
				alert("<fmt:message key='medialib.message.updateFailure'/>");
			</script>
   		</c:if>
   	</c:if>
   	
	<tr>
		<td>
			<x:display name="editMedia.mediaFormEdit" />
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>
</body>
</html>
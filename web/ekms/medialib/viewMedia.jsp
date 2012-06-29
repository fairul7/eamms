<%@page import="kacang.Application,
				com.tms.cms.medialib.model.MediaModule"
%>
<%@include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	MediaModule module = (MediaModule) app.getModule(MediaModule.class);
	
	// Determine if user is accessible to this media
	boolean isAccessible = module.isAccessible(request.getParameter("id"));
	if(isAccessible) {
		request.setAttribute("isAccessible", "true");
	}
	else {
		request.setAttribute("isAccessible", "false");
	}
%>

<c:if test="${isAccessible eq 'false'}">
	<c:redirect url="mediaListing.jsp"/>
</c:if>

<x:config>
	<page name="viewMedia">
		<com.tms.cms.medialib.ui.MediaView name="mediaView" />
	</page>
</x:config>

<c:if test="${!empty param.id}">
	<x:set name="viewMedia.mediaView" property="mediaId" value="${param.id}" />
	<x:set name="viewMedia.mediaView" property="ip" value="${pageContext.request.remoteAddr}" />
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
			<x:display name="viewMedia.mediaView" />
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>
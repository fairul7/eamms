<%@page import="kacang.Application,
				com.tms.cms.medialib.model.AlbumModule"
%>
<%@include file="/common/header.jsp"%>

<%
	if(request.getParameter("id") != null &&
		! "".equals(request.getParameter("id"))) {
		Application app = Application.getInstance();
		AlbumModule module = (AlbumModule) app.getModule(AlbumModule.class);
		
		// Determine if user is accessible to this library
		boolean isAccessible = module.isAccessible(request.getParameter("id"));
		if(isAccessible) {
			request.setAttribute("isAccessible", "true");
		}
		else {
			request.setAttribute("isAccessible", "false");
		}
		
		request.setAttribute("isEditable", "false");
	}
%>

<c:if test="${!empty param.cn && !empty param.id}">
	<c:if test="${param.cn eq 'viewAlbum.mediaByAlbumListing'}">
		<c:redirect url="viewMedia.jsp?id=${param.id}&page=1"/>
	</c:if>
</c:if>

<c:if test="${isAccessible eq 'false'}">
	<c:redirect url="albumListing.jsp"/>
</c:if>

<x:config>
	<page name="viewAlbum">
		<com.tms.cms.medialib.ui.AlbumView name="albumView" />
		<com.tms.cms.medialib.ui.ThumbnailView name="thumbnailView" />
		<com.tms.cms.medialib.ui.MediaListingByAlbumTable name="mediaByAlbumListing" enforceViewOnly="true"/>
	</page>
</x:config>

<c:if test="${!empty param.id}">
	<x:set name="viewAlbum.albumView" property="albumId" value="${param.id}" />
	<x:set name="viewAlbum.thumbnailView" property="albumId" value="${param.id}" />
	<x:set name="viewAlbum.mediaByAlbumListing" property="albumId" value="${param.id}" />
</c:if>

<c:if test="${!empty param.page}">
	<x:set name="viewAlbum.thumbnailView" property="page" value="${param.page}"></x:set>
</c:if>

<c:if test="${!empty param.row}">
	<x:set name="viewAlbum.thumbnailView" property="row" value="${param.row}"></x:set>
</c:if>

<x:set name="viewAlbum.mediaByAlbumListing" property="enforceViewOnly" value="true" />

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>

<c:set var="enforceViewOnly" value="true" scope="request"/>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="5">
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
			<fmt:message key="medialib.label.viewAlbum"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<x:display name="viewAlbum.albumView" />
		</td>
	</tr>
</table>
<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="5">
	<tr>
		<td>
			<x:display name="viewAlbum.thumbnailView" />
		</td>
	</tr>
</table>
<table valign="top" width="99%" border="0" cellspacing="0" cellpadding="5" align="center">
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
			<fmt:message key="medialib.label.mediaListing"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<x:display name="viewAlbum.mediaByAlbumListing" />
		</td>
	</tr>
</table>
<%@include file="/ekms/includes/footer.jsp" %>
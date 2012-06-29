<%@page import="kacang.Application,
				kacang.services.security.SecurityService,
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
		
		if(isAccessible) {
			// Check if user is the library manager of requested album
			if(module.isManager(request.getParameter("id"))) {
				request.setAttribute("isEditable", "true");
			}
			else {
				request.setAttribute("isEditable", "false");
			}
		}
	}
%>

<c:if test="${!empty param.cn && !empty param.id}">
	<c:if test="${param.cn eq 'editAlbum.mediaByAlbumListing'}">
		<c:redirect url="editMedia.jsp?id=${param.id}&page=1"/>
	</c:if>
</c:if>

<c:if test="${isAccessible eq 'false'}">
	<c:redirect url="albumListing.jsp"/>
</c:if>

<c:if test="${isEditable eq 'false'}">
	<c:redirect url="viewAlbum.jsp?id=${param.id}&page=1"/>
</c:if>

<x:config>
	<page name="editAlbum">
		<com.tms.cms.medialib.ui.AlbumFormEdit name="albumFormEdit" />
		<com.tms.cms.medialib.ui.ThumbnailView name="thumbnailView" />
		<com.tms.cms.medialib.ui.MediaListingByAlbumTable name="mediaByAlbumListing" />
	</page>
</x:config>

<c:if test="${forward.name eq 'success'}">
	<c:set var="redirectedForward" value="success" scope="session"/>
	<c:redirect url="editAlbum.jsp"/>
</c:if>
<c:if test="${forward.name eq 'failure'}">
	<c:set var="redirectedForward" value="failure" scope="session"/>
	<c:redirect url="editAlbum.jsp"/>
</c:if>

<c:if test="${!empty param.id}">
	<x:set name="editAlbum.albumFormEdit" property="albumId" value="${param.id}" />
	<x:set name="editAlbum.thumbnailView" property="albumId" value="${param.id}" />
	<x:set name="editAlbum.mediaByAlbumListing" property="albumId" value="${param.id}" />
</c:if>

<c:if test="${!empty param.page}">
	<x:set name="editAlbum.thumbnailView" property="page" value="${param.page}"></x:set>
</c:if>

<c:if test="${!empty param.row}">
	<x:set name="editAlbum.thumbnailView" property="row" value="${param.row}"></x:set>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>

<script language="JavaScript">
	function back() {
		document.location.href = "<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/albumListing.jsp";
		return false;
	}
</script>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="5">
	<c:if test="${!empty redirectedForward}">
   		<c:if test="${redirectedForward eq 'success'}">
   			<c:set var="redirectedForward" value="" scope="session"/>
   			<script language="JavaScript">
				alert("<fmt:message key='medialib.message.albumUpdateSuccess'/>");
			</script>
   		</c:if>
   		<c:if test="${redirectedForward eq 'failure'}">
   			<c:set var="redirectedForward" value="" scope="session"/>
   			<script language="JavaScript">
				alert("<fmt:message key='medialib.message.updateFailure'/>");
			</script>
   		</c:if>
   	</c:if>
   	
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
			<fmt:message key="medialib.label.administration"/> > <fmt:message key="medialib.label.editAlbum"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<x:display name="editAlbum.albumFormEdit" />
		</td>
	</tr>
</table>
<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<x:display name="editAlbum.thumbnailView" />
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
			<x:display name="editAlbum.mediaByAlbumListing" />
		</td>
	</tr>
</table>
<%@include file="/ekms/includes/footer.jsp" %>
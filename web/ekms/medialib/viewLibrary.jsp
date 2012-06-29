<%@page import="kacang.Application,
				com.tms.cms.medialib.model.LibraryModule"
%>
<%@include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	LibraryModule module = (LibraryModule) app.getModule(LibraryModule.class);
	
	// Determine if user is accessible to this library
	if(request.getParameter("id") != null &&
	! "".equals(request.getParameter("id"))) {
		boolean isAccessible = module.isAccessible(request.getParameter("id"));
		if(isAccessible) {
			request.setAttribute("isAccessible", "true");
		}
		else {
			request.setAttribute("isAccessible", "false");
		}
	}
%>

<c:if test="${!empty param.cn}">
	<c:if test="${param.cn eq 'viewLibrary.albumByLibraryListing'}">
		<c:redirect url="viewAlbum.jsp?id=${param.id}&page=1"/>
	</c:if>
</c:if>

<c:if test="${isAccessible eq 'false'}">
	<c:redirect url="libraryListing.jsp"/>
</c:if>

<x:config>
	<page name="viewLibrary">
		<com.tms.cms.medialib.ui.LibraryView name="libraryView" />
		<com.tms.cms.medialib.ui.AlbumListingByLbraryTable name="albumByLibraryListing" />
	</page>
</x:config>

<c:if test="${!empty param.id}">
	<x:set name="viewLibrary.libraryView" property="libraryId" value="${param.id}" />
	<x:set name="viewLibrary.albumByLibraryListing" property="libraryId" value="${param.id}" />
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="5">
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
			<fmt:message key="medialib.label.libraryBrowsing"/> > <fmt:message key="medialib.label.viewLibrary"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<x:display name="viewLibrary.libraryView" />
		</td>
	</tr>
	<tr>
		<td>
			<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/images/blank.gif"/>
		</td>
	</tr>
</table>
<table valign="top" width="99%" border="0" cellspacing="0" cellpadding="5" align="center">
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
			<fmt:message key="medialib.label.albumListing"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<x:display name="viewLibrary.albumByLibraryListing" />
		</td>
	</tr>
</table>
<%@include file="/ekms/includes/footer.jsp" %>
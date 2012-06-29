<%@include file="/common/header.jsp"%>

<c:if test="${!empty param.id}">
	<c:redirect url="editMedia.jsp?id=${param.id}"/>
</c:if>

<c:if test="${!empty param.albumId}">
	<c:redirect url="editAlbum.jsp?id=${param.albumId}&page=1"/>
</c:if>

<c:if test="${!empty param.libraryId}">
	<c:redirect url="editLibrary.jsp?id=${param.libraryId}"/>
</c:if>

<x:config>
	<page name="mediaListing">
		<com.tms.cms.medialib.ui.MediaListingTable name="mediaListingTable" />
	</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="5">
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
			<fmt:message key="medialib.label.administration"/> > <fmt:message key="medialib.label.mediaListing"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<x:display name="mediaListing.mediaListingTable" />
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>
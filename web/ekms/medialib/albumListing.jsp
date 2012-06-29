<%@include file="/common/header.jsp"%>

<c:if test="${!empty param.id}">
	<c:redirect url="editAlbum.jsp?id=${param.id}&page=1"/>
</c:if>

<c:if test="${!empty param.libraryId}">
	<c:redirect url="editLibrary.jsp?id=${param.libraryId}"/>
</c:if>

<x:config>
	<page name="albumListing">
		<com.tms.cms.medialib.ui.AlbumListingTable name="albumListingTable" />
	</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="5">
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
			<fmt:message key="medialib.label.administration"/> > <fmt:message key="medialib.label.albumListing"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<x:display name="albumListing.albumListingTable" />
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>
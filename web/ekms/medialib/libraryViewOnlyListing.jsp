<%@include file="/common/header.jsp"%>

<c:if test="${!empty param.id}">
	<c:redirect url="viewLibrary.jsp?id=${param.id}"/>
</c:if>

<x:config>
	<page name="libraryViewOnlyListing">
		<com.tms.cms.medialib.ui.LibraryViewOnlyListingTable name="libraryViewOnlyListingTable" />
	</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="5">
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
			<fmt:message key="medialib.label.mediaLibrary"/> > <fmt:message key="medialib.label.libraryBrowsing"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<x:display name="libraryViewOnlyListing.libraryViewOnlyListingTable" />
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>
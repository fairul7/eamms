<%@include file="/common/header.jsp"%>

<x:permission module="com.tms.cms.medialib.model.LibraryModule" permission="com.tms.cms.medialib.CreateLibrary" url="home.jsp" />

<c:if test="${!empty param.id}">
	<c:redirect url="editLibrary.jsp?id=${param.id}"/>
</c:if>

<x:config>
	<page name="libraryListing">
		<com.tms.cms.medialib.ui.LibraryListingTable name="libraryListingTable" />
	</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="5">
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
			<fmt:message key="medialib.label.administration"/> > <fmt:message key="medialib.label.libraryListing"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<x:display name="libraryListing.libraryListingTable" />
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>
<%@include file="/common/header.jsp"%>

<x:config>
	<page name="home">
		<com.tms.cms.medialib.ui.FeaturedAlbumView name="featuredAlbumView" />
	</page>
</x:config>

<c:choose>
	<c:when test="${!empty param.id}">
		<x:set name="home.featuredAlbumView" property="albumId" value="${param.id}"/>
		<x:set name="home.featuredAlbumView" property="randomSelect" value="${false}"/>
	</c:when>
	<c:otherwise>
		<x:set name="home.featuredAlbumView" property="randomSelect" value="${true}"/>
	</c:otherwise>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>

<c:set var="enforceViewOnly" value="true" scope="request"/>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
			<x:display name="home.featuredAlbumView" />
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>
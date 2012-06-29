<%@include file="/common/header.jsp"%>

<x:config>
	<page name="featuredAlbumView">
		<com.tms.cms.medialib.ui.ThumbnailView name="thumbnailView" />
	</page>
</x:config>

<c:set var="w" value="${widget}"/>

<script language="JavaScript">
function goSelectedAlbum(value) {
	document.location.href = "<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/home.jsp?id=" + value; 
}
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="contentTitleFont">
						<fmt:message key="medialib.label.mediaLibrary"/> > <fmt:message key="medialib.label.featuredAlbum"/>
					</td>
					<td align="right">
						<c:if test="${w.libraryAlbumListSize > 0}">
							<fmt:message key="medialib.label.selectAlbum"/>
							<select name="album" onChange="return goSelectedAlbum(options[selectedIndex].value)">
								<c:forEach items="${w.libraryAlbumList}" var="libraryAlbumObject">
									<c:choose>
										<c:when test="${w.album.id eq libraryAlbumObject.albumId}">
											<option value="<c:out value='${libraryAlbumObject.albumId}'/>" selected="selected"><c:out value="${libraryAlbumObject.libraryName}"/> > <c:out value="${libraryAlbumObject.albumName}"/></option>
										</c:when>
										<c:otherwise>
											<option value="<c:out value='${libraryAlbumObject.albumId}'/>"><c:out value="${libraryAlbumObject.libraryName}"/> > <c:out value="${libraryAlbumObject.albumName}"/></option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</c:if>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/images/blank.gif"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<c:choose>
			<c:when test="${!empty w.album}">
				<c:set var="mediaType" value="${w.media.mediaType}"/>
				<%
					String mediaGeneralType = (String) pageContext.findAttribute("mediaType");
					pageContext.setAttribute("mediaType", mediaGeneralType.substring(0, mediaGeneralType.indexOf("/")));
				%>
				<table width="100%" border="0" cellspacing="0" cellpadding="5">
					<tr>
						<c:if test="${mediaType eq 'image'}">
							<td width="<c:out value='${w.media.thumbnailWidth}'/>" valign="middle" class="thumbnailBorder">
								<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/popupViewMedia.jsp?id=<c:out value="${w.media.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,statusbar=yes,width=650,height=450')">
								<img src="/storage/medialib/<c:out value="${w.media.albumId}"/>/thumbnails/<c:out value="${w.media.fileName}"/>" border="0" alt="<c:out value="${w.media.name}"/>" title="<c:out value="${w.media.name}"/>"/>
								</a>
							</td>
						</c:if>
						<c:if test="${mediaType eq 'audio'}">
							<td width="80" valign="middle" class="thumbnailBorder">
								<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/popupViewMedia.jsp?id=<c:out value="${w.media.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,width=650,height=450')"/>
								<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_audio.gif" border="0" alt="<c:out value="${w.media.name}"/>" title="<c:out value="${w.media.name}"/>"/>
								</a>
							</td>
						</c:if>
						<c:if test="${mediaType eq 'video'}">
							<td width="80" valign="middle" class="thumbnailBorder">
								<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/popupViewMedia.jsp?id=<c:out value="${w.media.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,width=650,height=450')"/>
								<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_video.gif" border="0" alt="<c:out value="${w.media.name}"/>" title="<c:out value="${w.media.name}"/>"/>
								</a>
							</td>
						</c:if>
						<c:if test="${w.media.mediaType eq 'application/x-shockwave-flash'}">
							<td width="80" valign="middle" class="thumbnailBorder">
								<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/popupViewMedia.jsp?id=<c:out value="${w.media.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,width=650,height=450')"/>
								<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_flash.gif" border="0" alt="<c:out value="${w.media.name}"/>" title="<c:out value="${w.media.name}"/>"/>
								</a>
							</td>
						</c:if>
						<td width="10">&nbsp;</td>
						<td valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="5">
								<tr>
									<td class="albumName">
										<a href="viewLibrary.jsp?id=<c:out value="${w.album.libraryId}"/>"><c:out value="${w.album.libraryName}"/></a> > 
										<a href="viewAlbum.jsp?id=<c:out value="${w.album.id}&page=1"/>"><c:out value="${w.album.name}"/></a>
									</td>
								</tr>
								<tr>
									<td>
										<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/images/blank.gif"/>
									</td>
								</tr>
								<tr>
									<td>
										<c:out value="${w.album.description}"/>
									</td>
								</tr>
								<tr>
									<td>
										<fmt:message key="medialib.label.eventDate"/>: <fmt:formatDate value="${w.album.eventDate}" pattern="dd-MMM-yyyy kk:mm"/>
									</td>
								</tr>
								<tr>
									<td>
										<a href="viewAlbum.jsp?id=<c:out value="${w.album.id}&page=1"/>"><fmt:message key="medialib.label.viewAlbum"/></a>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<fmt:message key="medialib.message.noFeaturedAlbumFound"/>
			</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td>
			<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/images/blank.gif"/>
		</td>
	</tr>
</table>
<c:if test="${!empty w.album}">
	<x:set name="featuredAlbumView.thumbnailView" property="albumId" value="${w.album.id}" />
	<c:if test="${!empty param.page}">
		<x:set name="featuredAlbumView.thumbnailView" property="page" value="${param.page}"/>
	</c:if>
	<c:if test="${!empty param.row}">
		<x:set name="featuredAlbumView.thumbnailView" property="row" value="${param.row}"/>
	</c:if>
	<x:display name="featuredAlbumView.thumbnailView" />
</c:if>
<%@page import="java.util.ArrayList,
				kacang.Application,
				com.tms.cms.medialib.model.MediaModule,
				com.tms.cms.medialib.model.LibraryAlbumObject"%>
<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<%
	MediaModule mediaModule = (MediaModule) Application.getInstance().getModule(MediaModule.class);
	
	// Retrieve related pair of library-album entries
	ArrayList libraryAlbumList = mediaModule.getLibraryAlbumList(true);
%>

<script language="JavaScript">
	var libraryAlbumSize = <%=libraryAlbumList.size() + 1%>;
	var libraryId = new Array(libraryAlbumSize);
	var libraryName = new Array(libraryAlbumSize);
	var albumId = new Array(libraryAlbumSize);
	var albumName = new Array(libraryAlbumSize);
	
	<%
	if(libraryAlbumList != null && libraryAlbumList.size() != 0) {
		LibraryAlbumObject obj = new LibraryAlbumObject();
		for(int i=0; i<libraryAlbumList.size(); i++) {
			obj = (LibraryAlbumObject) libraryAlbumList.get(i);
	%>
			libraryId[<%=i+1%>] = "<%=obj.getLibraryId()%>";
			libraryName[<%=i+1%>] = "<%=obj.getLibraryName()%>";
			albumId[<%=i+1%>] = "<%=obj.getAlbumId()%>";
			albumName[<%=i+1%>] = "<%=obj.getAlbumName()%>";
	<%
		}
	}
	%>
	
	// Once the Library select box is changed, JavaScript will look for album(s) matching the selected library, 
	// and re-populate the Album select box
	function setLibraryAlbumChange(){
		var librarySelectBox = document.forms['<c:out value="${w.absoluteName}"/>'].elements['<c:out value="${w.libraryName.absoluteName}"/>'];
		var albumSelectBox = document.forms['<c:out value="${w.absoluteName}"/>'].elements['<c:out value="${w.albumName.absoluteName}"/>'];
		
		// Clear off all the items in Album select box
		for(i=albumSelectBox.options.length - 1; i>=0; i--){
			albumSelectBox.options[i] = null;
		}
		
		albumSelectBox.selectedIndex = -1;
		
		// Re-populate the Album select box based on the selected library
		var selectedLibrary = librarySelectBox.options[librarySelectBox.selectedIndex].value;
		
		albumSelectBox.options[albumSelectBox.options.length] = new Option("<fmt:message key="medialib.label.pleaseSelect"/>", "");
		if(selectedLibrary != ""){
			for(i=0; i<libraryAlbumSize; i++){
				if(selectedLibrary == libraryId[i]){
					if(albumId[i] == '<c:out value="${w.selectedAlbumId}"/>') {
						albumSelectBox.options[albumSelectBox.options.length] = new Option(albumName[i], albumId[i], true);
					}
					else {
						albumSelectBox.options[albumSelectBox.options.length] = new Option(albumName[i], albumId[i]);
					}
				}
			}
		}
	}
	
	function openBrWindow(theURL,winName,features) {
		window.open(theURL,winName,features);
	}
	
	function back() {
		document.location.href = "<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/mediaListing.jsp";
		return false;
	}
	
	<!-- Expandable Content Header Script Start -->
	var ie4 = false;
	if(document.all) {
	  ie4 = true;
	}
	
	function getObject(id) {
	  if (ie4) {
	    return document.all[id];
	  }
	  else {
	    return document.getElementById(id);
	  }
	}
	
	function toggle(link, divId) {
	  var lText = link.innerHTML;
	  var d = getObject(divId);
	
	  if (lText == '[+] <fmt:message key="medialib.label.fullScaledImage"/>') {
	    link.innerHTML = '[-] <fmt:message key="medialib.label.fullScaledImage"/>';
	    d.style.display = 'block';
	  }
	  else {
	    link.innerHTML = '[+] <fmt:message key="medialib.label.fullScaledImage"/>';
	    d.style.display = 'none';
	  }
	}
	<!-- Expandable Content Header Script End -->
</script>

<jsp:include page="../form_header.jsp" flush="true"/>

<table width="100%" border="0" cellspacing="0" cellpadding="5" >
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
			<fmt:message key="medialib.label.administration"/> > <fmt:message key="medialib.label.editMedia"/>
		</td>
	</tr>
	<c:set var="mediaType" value="${w.mediaObject.mediaType}"/>
	<%
		String mediaGeneralType = (String) pageContext.findAttribute("mediaType");
		pageContext.setAttribute("mediaType", mediaGeneralType.substring(0, mediaGeneralType.indexOf("/")));
	%>
	<tr>
		<td class="contentBgColor">
			<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/images/blank.gif"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<table width="100%" border="0" cellspacing="0" cellpadding="5">
				<tr>
					<c:choose>
						<c:when test="${w.isManager eq 'true'}">
							<c:set var="rowspan" value="6"/>
						</c:when>
						<c:otherwise>
							<c:set var="rowspan" value="5"/>
						</c:otherwise>
					</c:choose>
					<td class="thumbnailBorder" rowspan="<c:out value='${rowspan}'/>" width="130">
						<c:choose>
						<c:when test="${mediaType eq 'image'}">
							<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/popupViewMedia.jsp?id=<c:out value="${w.mediaId}"/>','popupViewMedia','resizable=1,scrollbars=yes,width=650,height=450')">
							<%-- 
								<img src="/storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/thumbnails/<c:out value="${w.mediaObject.fileName}"/>" border="0" alt="<c:out value="${w.mediaObject.name}"/>" title="<c:out value="${w.mediaObject.name}"/>"/>
							--%>
								<img src="/storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/<c:out value="${w.mediaObject.fileName}"/>" border="0" alt="<c:out value="${w.mediaObject.name}"/>" title="<c:out value="${w.mediaObject.name}"/>"  width="<c:out value='${w.mediaObject.thumbnailWidth}' />" height="<c:out value='${w.mediaObject.thumbnailHeight}' />"   />	
								<br /><fmt:message key="medialib.label.viewMedia" />
							</a>
						</c:when>
						<c:when test="${mediaType eq 'audio'}">
							<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/popupViewMedia.jsp?id=<c:out value="${w.mediaId}"/>','popupViewMedia','resizable=1,scrollbars=yes,width=650,height=450')">
								<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_audio.gif" border="0" alt="<c:out value="${w.mediaObject.name}"/>" title="<c:out value="${w.mediaObject.name}"/>"/>
								<br /><fmt:message key="medialib.label.viewMedia" />
							</a>
						</c:when>
						<c:when test="${mediaType eq 'video'}">
							<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/popupViewMedia.jsp?id=<c:out value="${w.mediaId}"/>','popupViewMedia','resizable=1,scrollbars=yes,width=650,height=450')">
								<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_video.gif" border="0" alt="<c:out value="${w.mediaObject.name}"/>" title="<c:out value="${w.mediaObject.name}"/>"/>
								<br /><fmt:message key="medialib.label.viewMedia" />
							</a>
						</c:when>
						<c:when test="${w.mediaObject.mediaType eq 'application/x-shockwave-flash'}">
							<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/popupViewMedia.jsp?id=<c:out value="${w.mediaId}"/>','popupViewMedia','resizable=1,scrollbars=yes,width=650,height=450')">
								<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_flash.gif" border="0" alt="<c:out value="${w.mediaObject.name}"/>" title="<c:out value="${w.mediaObject.name}"/>"/>
								<br /><fmt:message key="medialib.label.viewMedia" />
							</a>
						</c:when>
						<c:otherwise>
							<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/popupViewMedia.jsp?id=<c:out value="${w.mediaId}"/>','popupEditMedia','resizable=1,scrollbars=yes,statusbar=1,width=650,height=450')">
								<fmt:message key="medialib.label.unknown" />
							</a>
						</c:otherwise>
						</c:choose>
					</td>
					<td width="18%" valign="top">
						<fmt:message key="medialib.label.library*"/>
					</td>
					<td>
						<x:display name="${w.libraryName.absoluteName}"/>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<fmt:message key="medialib.label.album*"/>
					</td>
					<td>
						<x:display name="${w.albumName.absoluteName}"/>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<fmt:message key="medialib.label.name*"/>
					</td>
					<td>
						<x:display name="${w.mediaName.absoluteName}"/>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<fmt:message key="medialib.label.mediaType"/>
					</td>
					<td>
						<c:out value="${mediaType}"/>
						<c:if test="${mediaType eq 'image'}">
							- <c:out value="${w.mediaObject.imageWidth}"/> x <c:out value="${w.mediaObject.imageHeight}"/> px
						</c:if>
						 - <c:out value="${w.mediaObject.fileSizeStr}"/>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<fmt:message key="medialib.label.description"/>
					</td>
					<td>
						<x:display name="${w.description.absoluteName}"/>
					</td>
				</tr>
				<c:if test="${w.isManager eq 'true'}">
					<tr>
						<td valign="top">
							<fmt:message key="medialib.label.publishingStatus"/>
						</td>
						<td>
							<x:display name="${w.publishingStatusPanel.absoluteName}"/>
						</td>
					</tr>
				</c:if>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>
						<x:display name="${w.submit.absoluteName}"/>
						<x:display name="${w.cancel.absoluteName}"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<c:if test="${mediaType eq 'image'}">
		<tr>
			<td class="contentBgColor">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><strong><a title="show/hide" id="expFullImage_link" href="javascript: void(0);" onclick="toggle(this, 'expFullImage');"  style="text-decoration: none; color: black; ">[-] <fmt:message key="medialib.label.fullScaledImage"/></a></strong></td>
					</tr>
				</table>
				<div id="expFullImage" class="thumbnailBorder">
					<img src="/storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/<c:out value="${w.mediaObject.fileName}"/>" border="0" alt="<c:out value="${w.mediaObject.name}"/>" title="<c:out value="${w.mediaObject.name}"/>"/>
				</div>
			</td>
		</tr>
	</c:if>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>
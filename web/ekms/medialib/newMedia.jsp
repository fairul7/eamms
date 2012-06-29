<%@page import="java.util.ArrayList,
				kacang.Application,
				com.tms.cms.medialib.model.MediaModule,
				com.tms.cms.medialib.model.LibraryAlbumObject"%>
<%@include file="/common/header.jsp"%>


<c:if test="${!empty forward.name}">
	<c:redirect url="editAlbum.jsp?id=${forward.name}&page=1"/>
</c:if>

<x:config>
	<page name="newMedia">
		<com.tms.cms.medialib.ui.MediaFormAdd name="mediaFormAdd" />
	</page>
</x:config>

<%
	MediaModule mediaModule = (MediaModule) Application.getInstance().getModule(MediaModule.class);
	
	// ArrayList libraryList = mediaModule.getLibrarySelectList();
	// ArrayList albumList = mediaModule.getAlbumSelectList();
	
	// Retrieve related pair of library-album entries
	ArrayList libraryAlbumList = mediaModule.getLibraryAlbumList(true);
%>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>

<script language="JavaScript">
	function back() {
		document.location.href = "<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/mediaListing.jsp";
		return false;
	}
</script>

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
		var librarySelectBox = document.forms['newMedia.mediaFormAdd'].elements['newMedia.mediaFormAdd.libraryName'];
		var albumSelectBox = document.forms['newMedia.mediaFormAdd'].elements['newMedia.mediaFormAdd.albumName'];
		
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
					albumSelectBox.options[albumSelectBox.options.length] = new Option(albumName[i], albumId[i]);
				}
			}
		}
	}
</script>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="5">
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
			<fmt:message key="medialib.label.administration"/> > <fmt:message key="medialib.label.newMedia"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/images/blank.gif"/>
		</td>
	</tr>
	<tr>
		<td class="classRowLabel">
			<fmt:message key="medialib.label.supportFileTypes" />:
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td class="classRow" width="180" valign="middle"><img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_image_20x20.gif"/> <fmt:message key="medialib.message.permittedImage"/></td>
					<td class="classRow" width="180" valign="middle"><img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_audio_20x20.gif"/> <fmt:message key="medialib.message.permittedAudio"/></td>
					<td class="classRow" width="180" valign="middle"><img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_video_20x20.gif"/> <fmt:message key="medialib.message.permittedVideo"/></td>
					<td class="classRow" valign="middle"><img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_flash_20x20.gif"/> <fmt:message key="medialib.message.permittedFlash"/></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<x:display name="newMedia.mediaFormAdd" />
		</td>
	</tr>
</table>

<script language="JavaScript">
	document.forms['newMedia.mediaFormAdd'].elements['newMedia.mediaFormAdd.libraryName'].onChange = setLibraryAlbumChange();
</script>

<%@include file="/ekms/includes/footer.jsp" %>
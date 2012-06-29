<%@page import="java.util.ArrayList,
				kacang.Application,
				com.tms.cms.medialib.model.MediaModule,
				com.tms.cms.medialib.model.LibraryAlbumObject"%>
<%@include file="/common/header.jsp"%>

<%String browser="";%>
<c:set var="w" value="${widget}"/>

<script language="JavaScript">
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
	
	  if (lText == '+') {
	    link.innerHTML = '&#8722;';
	    d.style.display = 'block';
	  }
	  else {
	    link.innerHTML = '+';
	    d.style.display = 'none';
	  }
	}
	<!-- Expandable Content Header Script End -->
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="5" >
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
			<fmt:message key="medialib.label.viewMedia"/>
		</td>
	</tr>
	<c:set var="mediaType" value="${w.mediaObject.mediaType}"/>
	<%
		String mediaGeneralType = (String) pageContext.findAttribute("mediaType");
		pageContext.setAttribute("mediaType", mediaGeneralType.substring(0, mediaGeneralType.indexOf("/")));
	%>
	<c:if test="${mediaType eq 'image'}">
		<tr>
			<td class="contentBgColor">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><strong>[<a title="show/hide" id="expFullImage_link" href="javascript: void(0);" onclick="toggle(this, 'expFullImage');"  style="text-decoration: none; color: #527595; ">-</a>] <fmt:message key="medialib.label.fullScaledImage"/></strong></td>
					</tr>
				</table>
				<div id="expFullImage" class="thumbnailBorder">
					<img src="/storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/<c:out value="${w.mediaObject.fileName}"/>" border="0" alt="<c:out value="${w.mediaObject.name}"/>" title="<c:out value="${w.mediaObject.name}"/>"/>
				</div>
				<script language="javascript">toggle(getObject('expFullImage_link'), 'expFullImage');</script>
			</td>
		</tr>
	</c:if>
	<c:if test="${(mediaType eq 'video' || mediaType eq 'audio') && w.mediaObject.mediaType != 'video/mov'}">
		<tr>
			<td class="contentBgColor" align="center">
				<object classid="clsid:6BF52A52-394A-11D3-B153-00C04F79FAA6" id="WindowsMediaPlayer1" width="250" height="250">
					<param name="URL" value="/storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/<c:out value="${w.mediaObject.fileName}"/>">
					<param name="rate" value="1">
					<param name="balance" value="0">
					<param name="currentPosition" value="0">
					<param name="defaultFrame" value>
					<param name="playCount" value="1">
					<param name="autoStart" value="-1">
					<param name="currentMarker" value="0">
					<param name="invokeURLs" value="-1">
					<param name="baseURL" value>
					<param name="volume" value="50">
					<param name="mute" value="0">
					<param name="uiMode" value="full">
					<param name="stretchToFit" value="0">
					<param name="windowlessVideo" value="0">
					<param name="enabled" value="-1">
					<param name="enableContextMenu" value="-1">
					<param name="fullScreen" value="0">
					<param name="SAMIStyle" value>
					<param name="SAMILang" value>
					<param name="SAMIFilename" value>
					<param name="captioningID" value>
					<param name="enableErrorDialogs" value="0">
				</object>
			</td>
		</tr>
		<tr>
			<td class="contentBgColor">
				<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/images/blank.gif"/>
			</td>
		</tr>
	</c:if>
	<tr>
		<td class="contentBgColor">
			<table width="100%" border="0" cellspacing="0" cellpadding="5">
				<tr>
					<td class="thumbnailBorder" rowspan="6" width="130">
						<c:if test="${mediaType eq 'image'}">
							<a href="/storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/<c:out value="${w.mediaObject.fileName}"/>">
								<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_image.gif" border="0" alt="<c:out value="${w.mediaObject.name}"/>" title="<c:out value="${w.mediaObject.name}"/>"/>
							</a>
						</c:if>
						<c:if test="${mediaType eq 'audio'}">
							<a href="/storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/<c:out value="${w.mediaObject.fileName}"/>">
								<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_audio.gif" border="0" alt="<c:out value="${w.mediaObject.name}"/>" title="<c:out value="${w.mediaObject.name}"/>"/>
							</a>
						</c:if>
						<c:if test="${mediaType eq 'video'}">
							<a href="/storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/<c:out value="${w.mediaObject.fileName}"/>">
								<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_video.gif" border="0" alt="<c:out value="${w.mediaObject.name}"/>" title="<c:out value="${w.mediaObject.name}"/>"/>
							</a>
						</c:if>
					</td>
					<td width="18%" valign="top">
						<fmt:message key="medialib.label.library"/>
					</td>
					<td>
						<c:out value="${w.mediaObject.libraryName}"/>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<fmt:message key="medialib.label.album"/>
					</td>
					<td>
						<c:out value="${w.mediaObject.albumName}"/>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<fmt:message key="medialib.label.name"/>
					</td>
					<td>
						<c:out value="${w.mediaObject.name}"/>
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
					</td>
				</tr>
				<tr>
					<td valign="top">
						<fmt:message key="medialib.label.description"/>
					</td>
					<td>
						<c:out value="${w.mediaObject.description}"/>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<fmt:message key="medialib.label.download"/>
					</td>
					<td>
						<a href="/storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/<c:out value="${w.mediaObject.fileName}"/>">
						<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_download_20x20.gif" border="0" alt="<fmt:message key='medialib.message.rightClickToDownload'/>" title="<fmt:message key='medialib.message.rightClickToDownload'/>"/>
						</a>
						<br />
						<fmt:message key='medialib.message.rightClickToDownload'/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
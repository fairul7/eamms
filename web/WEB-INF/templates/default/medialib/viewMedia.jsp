<%@page import="java.util.ArrayList,
				kacang.Application,
				com.tms.cms.medialib.model.MediaModule,
				com.tms.cms.medialib.model.LibraryAlbumObject"%>
<%@include file="/common/header.jsp"%>

<%String browser="";%>
<c:set var="w" value="${widget}"/>

<script type="text/javascript" src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/includes/qtobject.js"></script>
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
	
	  if (lText == '[+] <fmt:message key="medialib.label.fullScaledImage"/>') {
	    link.innerHTML = '[-] <fmt:message key="medialib.label.fullScaledImage"/>';
	    d.style.display = 'block';
	  }
	  else {
	    link.innerHTML = '[+] <fmt:message key="medialib.label.fullScaledImage"/>';
	    d.style.display = 'none';
	  }
	}
	
	function toggleMediaURL(link, divId) {
	  var lText = link.innerHTML;
	  var d = getObject(divId);
	
	  if (lText == '[+] <fmt:message key="medialib.label.mediaURL"/>') {
	    link.innerHTML = '[-] <fmt:message key="medialib.label.mediaURL"/>';
	    d.style.display = 'block';
	  }
	  else {
	    link.innerHTML = '[+] <fmt:message key="medialib.label.mediaURL"/>';
	    d.style.display = 'none';
	  }
	}
	<!-- Expandable Content Header Script End -->
	
	function goToMedia(mediaId) {
		document.location = "<c:out value='${pageContext.request.contextPath}' />/ekms/medialib/popupViewMedia.jsp?id=" + mediaId;
	}
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="5" >
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
			<fmt:message key="medialib.label.viewMedia"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<c:if test="${!empty w.mediaNav.prevId}">
							<input type="button" value="&lt;" style="font-size:10pt;width:50px" class="button" onclick="goToMedia('<c:out value='${w.mediaNav.prevId}' />')" />
						</c:if>
					</td>
					<td>&nbsp;</td>
					<td align="right">
						<c:if test="${!empty w.mediaNav.nextId}">
							<input type="button" value="&gt;" style="font-size:10pt;width:50px" class="button" onclick="goToMedia('<c:out value='${w.mediaNav.nextId}' />')" />
						</c:if>
					</td>
				</tr>
			</table>
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
						<td><strong><a title="show/hide" id="expFullImage_link" href="javascript: void(0);" onclick="toggle(this, 'expFullImage');"  style="text-decoration: none; color: black; ">[-] <fmt:message key="medialib.label.fullScaledImage"/></a></strong></td>
					</tr>
				</table>
				<div id="expFullImage" class="thumbnailBorder">
					<img src="/storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/<c:out value="${w.mediaObject.fileName}"/>" border="0" alt="<c:out value="${w.mediaObject.name}"/>" title="<c:out value="${w.mediaObject.name}"/>"/>
				</div>
			</td>
		</tr>
	</c:if>
	<c:if test="${mediaType eq 'video' || mediaType eq 'audio' || w.mediaObject.mediaType eq 'application/x-shockwave-flash'}">
		<c:if test="${mediaType eq 'video'}">
			<c:set var="playerHeight" value="270"/>
		</c:if>
		<c:if test="${mediaType eq 'audio'}">
			<c:set var="playerHeight" value="70"/>
		</c:if>
		<tr>
			<td class="contentBgColor" align="center">
				<div class="thumbnailBorder">
				<c:choose>
					<c:when test="${w.mediaObject.mediaType eq 'video/mov'}">
						<!-- -QuickTime Player ->
						<script type="text/javascript">
							// <![CDATA[
							var myQTObject = new QTObject("/storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/<c:out value="${w.mediaObject.fileName}"/>", "quickTimePlayer", "320", "<c:out value='${playerHeight + 15}'/>");
							myQTObject.addParam("autostart", "false");
							myQTObject.write();
							// ]]>
						</script>
					</c:when>
					<c:when test="${w.mediaObject.mediaType eq 'application/x-shockwave-flash'}">
						<!-- Flash Player-->
						<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" 
							codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab" width="100%">
          					<param name="movie" value="/storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/<c:out value="${w.mediaObject.fileName}"/>">
							<param name="quality" value="high">
							<embed src="/storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/<c:out value="${w.mediaObject.fileName}"/>" 
								quality="high" 
								pluginspage="http://www.macromedia.com/go/getflashplayer" 
								type="application/x-shockwave-flash" 
								width="100%">
							</embed>
						</object>
					</c:when>
					<c:otherwise>
						<!-- Windows Media Player -->
						<!-- Embed WMP using ActiveX control for IE and a plug-in for non-IE  -->
						<!-- src: http://msdn.microsoft.com/library/default.asp?url=/library/en-us/wmp6sdk/htm/embeddingwindowsmediaplayer.asp -->
						<object id="MediaPlayer" width="320" height="<c:out value='${playerHeight}'/>" 
						    classid="CLSID:22D6F312-B0F6-11D0-94AB-0080C74C7E95"
						    standby="Loading Windows Media Player components..." 
						    type="application/x-oleobject">
						
							<param name="FileName" value="/storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/<c:out value="${w.mediaObject.fileName}"/>">
						  	<param name="AutoStart" value="True">
						  	<param name="ShowStatusBar" value="True">
						  	<embed 
						  		pluginspage='http://microsoft.com/windows/mediaplayer/en/download/'
						  		src="/storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/<c:out value="${w.mediaObject.fileName}"/>"
						    	name="MediaPlayer"
						    	showstatusbar='1'
						    	width="320"
						    	autostart="0" 
						    	height="<c:out value='${playerHeight}'/>">
						  	</embed>
						</object>
						<%-- 
						<object id="MediaPlayer" width="320" height="<c:out value='${playerHeight}'/>" 
						    classid="CLSID:22D6F312-B0F6-11D0-94AB-0080C74C7E95"
						    standby="Loading Windows Media Player components..." 
						    type="application/x-oleobject">
						
							<param name="FileName" value="/storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/<c:out value="${w.mediaObject.fileName}"/>">
						  	<param name="AutoStart" vakue="False">
						  	<param name="ShowStatusBar" value="True">
						  	<embed type="application/x-mplayer2" 
						  		pluginspage='http://microsoft.com/windows/mediaplayer/en/download/'
						  		src="/storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/<c:out value="${w.mediaObject.fileName}"/>"
						    	name="MediaPlayer"
						    	showstatusbar='1'
						    	width="320"
						    	autostart="0" 
						    	height="<c:out value='${playerHeight}'/>">
						  	</embed>
						</object>
						--%>
					</c:otherwise>
				</c:choose>
				</div>
			</td>
		</tr>
	</c:if>
	<tr>
		<td class="contentBgColor">
			<table width="100%" border="0" cellspacing="0" cellpadding="5">
				<tr>
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
						 - <c:out value="${w.mediaObject.fileSizeStr}"/>
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
						<a href="/medialib/downloadMedia?id=<c:out value="${w.mediaObject.id}"/>">
							<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_download_20x20.gif" border="0" alt="<fmt:message key='medialib.message.rightClickToDownload'/>" title="<fmt:message key='medialib.message.rightClickToDownload'/>"/>
						</a>
					</td>
				</tr>
				<tr>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td valign="top" colspan="2">
								<strong><a title="show/hide" id="expMediaURL_link" href="javascript: void(0);" onclick="toggleMediaURL(this, 'expMediaURL');"  style="text-decoration: none; color: black; ">[-] <fmt:message key="medialib.label.mediaURL"/></a></strong>
							</td>
						</tr>
					</table>
					<div id="expMediaURL">
						<%
						String requestURL = request.getRequestURL().toString();
						String domainRoot = requestURL.substring(0, requestURL.indexOf("/", 8) + 1);
						//String domainRoot = "http://" + request.getLocalName();
						/*if(! "".equals(String.valueOf(request.getLocalPort()).trim())) {
							domainRoot += ":" + request.getLocalPort() + "/";
						}*/
						if(! "".equals(request.getContextPath())) {
							domainRoot += request.getContextPath() + "/";
						}
						request.setAttribute("domain", domainRoot);
						%>
						<textarea rows="3" cols="90" style="font: normal 10pt Arial;" readonly="readonly"><c:out value="${domain}"/>storage/medialib/<c:out value="${w.mediaObject.albumId}"/>/<c:out value="${w.mediaObject.fileName}"/></textarea>
					</div>
					<script language="javascript">toggleMediaURL(getObject('expMediaURL_link'), 'expMediaURL');</script>
				</tr>
			</table>
		</td>
	</tr>
	
</table>
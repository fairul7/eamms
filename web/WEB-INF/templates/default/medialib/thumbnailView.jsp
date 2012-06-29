<%@page import="java.util.ArrayList"%>
<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>
<c:set var="mediaList" value="${w.mediaList}"/>
<c:set var="maxRowItem" value="${w.maxRowItem}"/>
<c:set var="mediaCount" value="${w.totalRecord}"/>

<c:choose>
	<c:when test="${!empty enforceViewOnly}">
		<c:if test="${enforceViewOnly eq 'true'}">
			<c:set var="openMediaJSP" value="popupViewMedia.jsp"/>
		</c:if>
	</c:when>
	<c:otherwise>
		<c:if test="${w.isEditable eq 'true'}">
			<c:set var="openMediaJSP" value="popupEditMedia.jsp"/>
		</c:if>
		<c:if test="${w.isEditable eq 'false'}">
			<c:set var="openMediaJSP" value="popupViewMedia.jsp"/>
		</c:if>
	</c:otherwise>
</c:choose>

<%
	String domainRoot = "/";
	if(! "".equals(request.getContextPath())) {
		domainRoot += request.getContextPath() + "/";
	}
	domainRoot += request.getRequestURI().substring(1);
	request.setAttribute("domain", domainRoot);
%>

<script language="JavaScript">
function goSelectedPage(paramType, value) {
		document.location.href = "<c:out value="${domain}"/>?id=<c:out value='${w.albumId}'/>&" + paramType + "=" + value;
}

function openBrWindow(theURL,winName,features) {
	mywindow = window.open(theURL,winName,features);
	mywindow.moveTo(0,0);
}
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="5" >
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
			<fmt:message key="medialib.label.mediaFiles"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor" align="right">
			<fmt:message key="medialib.label.maxRow"/>
			<select name="row" onChange="return goSelectedPage('row', options[selectedIndex].value)">
				<c:forEach begin="1" end="10" var="index">
					<c:choose>
						<c:when test="${index eq w.maxPageRow}">
							<option value="<c:out value='${index}'/>" selected="selected"><c:out value='${index}'/></option>
						</c:when>
						<c:otherwise>
							<option value="<c:out value='${index}'/>"><c:out value='${index}'/></option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<table width="100%" border="0" cellspacing="0" cellpadding="5">
				<c:choose>
				<c:when test="${mediaCount > 0}">
					<c:set var="count" value="0"/>
					<c:forEach items="${mediaList}" var="mediaObject">
						<c:set var="mediaType" value="${mediaObject.mediaType}"/>
						<%
						String mediaGeneralType = (String) pageContext.findAttribute("mediaType");
						pageContext.setAttribute("mediaType", mediaGeneralType.substring(0, mediaGeneralType.indexOf("/")));
						%>
						<c:choose>
						<c:when test="${count eq 0}">
							<tr align="center">
								<td width="22%" class="thumbnailBorder">
									<c:choose>
									<c:when test="${mediaType eq 'image'}">
										<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/<c:out value="${openMediaJSP}"/>?id=<c:out value="${mediaObject.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,statusbar=1,width=650,height=450')">
											<%-- 
											<img src="/storage/medialib/<c:out value="${mediaObject.albumId}"/>/thumbnails/<c:out value="${mediaObject.fileName}"/>" border="0" alt="<c:out value="${mediaObject.name}"/>" title="<c:out value="${mediaObject.name}"/>"/>
											--%>
											<img src="/storage/medialib/<c:out value="${mediaObject.albumId}"/>/<c:out value="${mediaObject.fileName}"/>" border="0" alt="<c:out value="${mediaObject.name}"/>" title="<c:out value="${mediaObject.name}"/>" width="<c:out value='${mediaObject.thumbnailWidth}' />" height="<c:out value='${mediaObject.thumbnailHeight}' />" />
										</a>
									</c:when>
									<c:when test="${mediaType eq 'audio'}">
										<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/<c:out value="${openMediaJSP}"/>?id=<c:out value="${mediaObject.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,statusbar=1,width=650,height=450')">
											<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_audio.gif" border="0" alt="<c:out value="${mediaObject.name}"/>" title="<c:out value="${mediaObject.name}"/>"/>
										</a>
									</c:when>
									<c:when test="${mediaType eq 'video'}">
										<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/<c:out value="${openMediaJSP}"/>?id=<c:out value="${mediaObject.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,statusbar=1,width=650,height=450')">
											<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_video.gif" border="0" alt="<c:out value="${mediaObject.name}"/>" title="<c:out value="${mediaObject.name}"/>"/>
										</a>
									</c:when>
									<c:when test="${mediaObject.mediaType eq 'application/x-shockwave-flash'}">
										<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/<c:out value="${openMediaJSP}"/>?id=<c:out value="${mediaObject.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,statusbar=1,width=650,height=450')">
											<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_flash.gif" border="0" alt="<c:out value="${mediaObject.name}"/>" title="<c:out value="${mediaObject.name}"/>"/>
										</a>
									</c:when>
									<c:otherwise>
										<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/<c:out value="${openMediaJSP}"/>?id=<c:out value="${mediaObject.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,statusbar=1,width=650,height=450')">
											<fmt:message key="medialib.label.unknown" />
										</a>
									</c:otherwise>
									</c:choose>
									<br/>
									<c:out value="${mediaObject.name}"/>
								</td>
						</c:when>
						<c:when test="${(count % maxRowItem) eq 0}">
							</tr>
							<tr>
								<td class="contentBgColor" colspan="7">
									<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/images/blank.gif"/>
								</td>
							</tr>
							<tr align="center">
								<td width="22%" class="thumbnailBorder">
									<c:choose>
									<c:when test="${mediaType eq 'image'}">
										<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/<c:out value="${openMediaJSP}"/>?id=<c:out value="${mediaObject.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,statusbar=1,width=650,height=450')">
											<img src="/storage/medialib/<c:out value="${mediaObject.albumId}"/>/thumbnails/<c:out value="${mediaObject.fileName}"/>" border="0" alt="<c:out value="${mediaObject.name}"/>" title="<c:out value="${mediaObject.name}"/>"/>
										</a>
									</c:when>
									<c:when test="${mediaType eq 'audio'}">
										<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/<c:out value="${openMediaJSP}"/>?id=<c:out value="${mediaObject.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,statusbar=1,width=650,height=450')">
											<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_audio.gif" border="0" alt="<c:out value="${mediaObject.name}"/>" title="<c:out value="${mediaObject.name}"/>"/>
										</a>
									</c:when>
									<c:when test="${mediaType eq 'video'}">
										<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/<c:out value="${openMediaJSP}"/>?id=<c:out value="${mediaObject.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,statusbar=1,width=650,height=450')">
											<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_video.gif" border="0" alt="<c:out value="${mediaObject.name}"/>" title="<c:out value="${mediaObject.name}"/>"/>
										</a>
									</c:when>
									<c:when test="${mediaObject.mediaType eq 'application/x-shockwave-flash'}">
										<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/<c:out value="${openMediaJSP}"/>?id=<c:out value="${mediaObject.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,statusbar=1,width=650,height=450')">
											<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_flash.gif" border="0" alt="<c:out value="${mediaObject.name}"/>" title="<c:out value="${mediaObject.name}"/>"/>
										</a>
									</c:when>
									<c:otherwise>
										<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/<c:out value="${openMediaJSP}"/>?id=<c:out value="${mediaObject.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,statusbar=1,width=650,height=450')">
											<fmt:message key="medialib.label.unknown" />
										</a>						</c:otherwise>
									</c:choose>
									<br/>
									<c:out value="${mediaObject.name}"/>
								</td>
						</c:when>
						<c:otherwise>
							<td width="4%">&nbsp;</td>
							<td width="22%" class="thumbnailBorder">
								<c:choose>
								<c:when test="${mediaType eq 'image'}">
									<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/<c:out value="${openMediaJSP}"/>?id=<c:out value="${mediaObject.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,statusbar=1,width=650,height=450')">
										<img src="/storage/medialib/<c:out value="${mediaObject.albumId}"/>/thumbnails/<c:out value="${mediaObject.fileName}"/>" border="0" alt="<c:out value="${mediaObject.name}"/>" title="<c:out value="${mediaObject.name}"/>"/>
									</a>
								</c:when>
								<c:when test="${mediaType eq 'audio'}">
									<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/<c:out value="${openMediaJSP}"/>?id=<c:out value="${mediaObject.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,statusbar=1,width=650,height=450')">
										<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_audio.gif" border="0" alt="<c:out value="${mediaObject.name}"/>" title="<c:out value="${mediaObject.name}"/>"/>
									</a>
								</c:when>
								<c:when test="${mediaType eq 'video'}">
									<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/<c:out value="${openMediaJSP}"/>?id=<c:out value="${mediaObject.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,statusbar=1,width=650,height=450')">
										<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_video.gif" border="0" alt="<c:out value="${mediaObject.name}"/>" title="<c:out value="${mediaObject.name}"/>"/>
									</a>
								</c:when>
								<c:when test="${mediaObject.mediaType eq 'application/x-shockwave-flash'}">
									<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/<c:out value="${openMediaJSP}"/>?id=<c:out value="${mediaObject.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,statusbar=1,width=650,height=450')">
										<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/images/icon_flash.gif" border="0" alt="<c:out value="${mediaObject.name}"/>" title="<c:out value="${mediaObject.name}"/>"/>
									</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:openBrWindow('<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/<c:out value="${openMediaJSP}"/>?id=<c:out value="${mediaObject.id}"/>','popupEditMedia','resizable=1,scrollbars=yes,statusbar=1,width=650,height=450')">
										<fmt:message key="medialib.label.unknown" />
									</a>
								</c:otherwise>
								</c:choose>
								<br/>
								<c:out value="${mediaObject.name}"/>
							</td>
						</c:otherwise>
						</c:choose>
						<c:set var="count" value="${count+1}"/>
					</c:forEach>
					<tr>
						<td>
							<c:out value="${w.beginIndex}"/> - 
							<c:out value="${w.endIndex}"/> 
							<fmt:message key="medialib.message.of"/> 
							<c:out value="${w.totalRecord}"/> 
							<fmt:message key="medialib.message.medias"/>
						</td>
						<td colspan="6" align="right">
							<fmt:message key="medialib.label.page"/>
							<select name="page" onChange="return goSelectedPage('page', options[selectedIndex].value)">
								<c:forEach begin="1" end="${w.totalPage}" var="index">
									<c:choose>
										<c:when test="${index eq w.page}">
											<option value="<c:out value='${index}'/>" selected="selected"><c:out value='${index}'/></option>
										</c:when>
										<c:otherwise>
											<option value="<c:out value='${index}'/>"><c:out value='${index}'/></option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td align="center">
							<span style="font-weight:bold"><fmt:message key="medialib.message.noRecordFound"/></span> 
						</td>
					</tr>
				</c:otherwise>
				</c:choose>
			</table>
		</td>
	</tr>
	<tr>
		<td class="contentBody">
			<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/images/blank.gif"/>
		</td>
	</tr>
</table>
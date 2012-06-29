<%@ page import="com.tms.cms.document.Document,
				 com.tms.cms.core.model.ContentObject,
				 com.tms.cms.core.model.ContentPublisher,
				 kacang.Application,
				 kacang.services.security.User,
				 kacang.services.security.SecurityService"%>
<%@ include file="/common/header.jsp" %>

<%!
	String getContentIcon(String contentType){
		String image = new String("");
		//html, htm
		if("text/html".equalsIgnoreCase(contentType))
			image = "icf_html.gif";
		//txt
		else if("text/plain".equalsIgnoreCase(contentType))
			image = "icf_text.gif";
		//video
		else if("video/x-msvideo".equalsIgnoreCase(contentType) || "video/x-sgi-movie".equalsIgnoreCase(contentType) || "video/mpeg".equalsIgnoreCase(contentType)
					|| "video/mpeg2".equalsIgnoreCase(contentType) || "video/quicktime".equalsIgnoreCase(contentType) || "application/x-pn-realaudio-plugin".equalsIgnoreCase(contentType))
			image = "icf_mpeg.gif";
		//word
		else if("application/msword".equalsIgnoreCase(contentType))
			image = "icf_word.gif";
		//powerpoint
		else if("application/mspowerpoint".equalsIgnoreCase(contentType))
			image = "icf_pps.gif";
		//excel
		else if("application/msexcel".equalsIgnoreCase(contentType) || "application/ms-excel".equalsIgnoreCase(contentType) || "application/x-excel".equalsIgnoreCase(contentType)
		 			|| "application/vnd.ms-excel".equalsIgnoreCase(contentType))
			image = "icf_excel.gif";
		//pdf
		else if("application/pdf".equalsIgnoreCase(contentType) || "application/x-pdf".equalsIgnoreCase(contentType))
			image = "icf_acrobat.gif";
		//images
		else if("image/gif".equalsIgnoreCase(contentType) || "image/x-png".equalsIgnoreCase(contentType) || "image/jpeg".equalsIgnoreCase(contentType)
					|| "image/tiff".equalsIgnoreCase(contentType) || "image/pjpeg".equalsIgnoreCase(contentType) || "image/x-ms-bmp".equalsIgnoreCase(contentType))
			image = "icf_image.gif";
		//audio
		else if("x-music/x-midi".equalsIgnoreCase(contentType) || "audio/x-wav".equalsIgnoreCase(contentType) || "audio/mpeg".equalsIgnoreCase(contentType)
					|| "audio/x-ms-wma".equalsIgnoreCase(contentType) )
			image = "icf_audio.gif";
		//flash (swf)
		else if("application/x-shockwave-flash".equalsIgnoreCase(contentType) || "audio/x-wav".equalsIgnoreCase(contentType))
			image = "icf_flash.gif";
		else
			image = "contenticon.gif";
		return image;
	}
%>
<br>
<table width="95%" align="center" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td align="left" valign="top">
			<table   border="0" cellspacing="2">
				<tr align="left" valign="top">
					<td colspan="3" align="left">
						<span class="middlehd">
							Content In &ldquo;
							<c:choose>
								<c:when test="${empty widget.selectedNode}">HOME</c:when>
								<c:otherwise><c:out value="${widget.selectedNode.taxonomyName}"/></c:otherwise>
							</c:choose>
							&rdquo;
							<br/>
						</span>
					</td>
				</tr>
				<c:choose>
					<c:when test="${empty  widget.mapResult}">
						<tr align="left" valign="top"><td colspan="3" align="left" class="contentdate2">No contents found</td></tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="result" items="${widget.mapResult}">
							<c:set var="co" value="${result.contentObject}"/>
							<tr align="left" valign="top">
								<td align="left" valign="middle" >&nbsp;
								<%--	<fmt:formatDate value="${result.contentObject.sourceDate}" pattern="${globalDateLong}"/> --%>
								</td>
								<td width="25" align="left">
									<c:choose>
							    		<c:when test="${co.className == 'com.tms.cms.document.Document'}">
							    			<%
							    				ContentObject object = (ContentObject) pageContext.getAttribute("co");
							    				
							    				ContentPublisher cp = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);
							    				User user = ((SecurityService)Application.getInstance().getService(SecurityService.class)).getCurrentUser(request);
							    				Document doc = (Document) cp.view(object.getId(), user);
							    			%>
							     			<img src="<%=request.getContextPath()%>/ekms/images/<%=getContentIcon(doc.getContentType())%>" width="25" height="22"/> 
							    			<%--<img src="<%=request.getContextPath()%>/ekms/images/contenticon.gif" width="25" height="22"/>--%>
							    		</c:when>
							            <c:when test="${co.className == 'com.tms.cms.image.Image'}">
						    			    <img src="<%=request.getContextPath()%>/ekms/images/contenticon3.gif"/> 
							    		</c:when>
							    		<c:otherwise>
							    			<img src="<%=request.getContextPath()%>/ekms/images/contenticon2.gif" width="25" height="22"/>
							    		</c:otherwise>
							    	</c:choose>
								</td>
								<td align="left" valign="middle">
								<a href="/ekms/content/content.jsp?id=<c:out value="${result.contentObject.id}"/>" class="contentlink"><c:out value="${result.contentObject.name}"/></a><br/>
								<c:if test="${! empty result.contentObject.author}"><span class="contentChildAuthor"><c:out value="${result.contentObject.author}"/></span> <br/></c:if>
    							<span class="textsmall3">
    							<c:if test="${! empty result.contentObject.summary}"><c:out value="${result.contentObject.summary}" escapeXml="false"/></c:if>
    							</span>
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</table>
		</td>
	</tr>
    <tr>
        <td align="right" valign="top" class="eventNormal">
            <% int i=0; %>
            <c:forEach var="page" items="${widget.pageMap}">
                <% if (i==0) { %>[ Page <% } else { %> | <% } i++;%>
                <c:if test="${widget.page == page}">
                    <span class="contentPageLink"><b><c:out value="${page}"/></b></span>
                </c:if>
                <c:if test="${widget.page != page}">
                <span>
                <a href="taxonomyTree.jsp?id=<c:out value="${widget.selectedNode.taxonomyId}"/>&page=<c:out value="${page}"/>" class="rw_boldLink"><c:out value="${page}"/></a>  </span>
                </c:if>
            </c:forEach>
            <% if (i>0) { %>]<% } %>
        </td>
    </tr>
</table>
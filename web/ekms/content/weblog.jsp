<%@ page import="kacang.services.security.SecurityService,
                 kacang.Application,
                 kacang.services.security.User,
                 com.tms.collab.weblog.model.WeblogModule,
                 java.util.Collection"%>
<%@ include file="/common/header.jsp" %>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true" />
<table cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td valign="top">
            <table cellpadding="3" cellspacing="0" width="100%">
                <tr><td class="contentPath"><a href="<c:url value="/ekms/content/"/> " class="contentPathLink"><fmt:message key='weblog.label.weblog'/></a></td></tr>
            </table>
        </td>
    </tr>
	<tr>
		<td valign="top" class="contentBody">
			<table cellpadding="2" cellspacing="1" width="95%"  align="center">
				<tr><td>&nbsp;</td></tr>
				<tr>
					<td>
						<table cellpadding="5" cellspacing="0" width="100%" align="center">
							<tr><td class="contentHeader"><span class="contentName"><fmt:message key='weblog.label.weblog'/></span></td></tr>
						</table>
						<br>
						<table width="100%" align="center">
							<tr>
								<td valign="top" class="contentBody" valign="top">
									<x:template name="latestposts" type="com.tms.collab.weblog.ui.LatestPosts" properties="postViewUrl=postView.jsp"/><br><br>
									<x:template name="popular" type="com.tms.collab.weblog.ui.PopularBlogs" properties="blogViewUrl=blogview.jsp"/><br><br>
									<x:template name="latestblogs" type="com.tms.collab.weblog.ui.LatestBlogs" properties="blogViewUrl=blogview.jsp"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr><td>&nbsp;</td></tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="contentStrapColor">
			&nbsp;
			<input value="<fmt:message key='weblog.label.allUsersBlog'/>" onClick="document.location='bloguserlist.jsp';" type="button" class="button">
			<%
				SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
				User user = ss.getCurrentUser(request);
				WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
				Collection col = wm.getBlogByUserId(user.getId());
				if(ss.hasPermission(user.getId(),WeblogModule.PERMISSION_CREATE_BLOG,null,null))
				{
					if(col== null||col.size()<3)
					{
						%>
						<input value="<fmt:message key='weblog.label.createBlog'/>" onClick="document.location='addBlogForm.jsp';" type="button" class="button">
						<%
					}
				}
				pageContext.setAttribute("blogs",col);
			%>
			<c:forEach var="blog" items="${blogs}" >
				<input value="<c:out value="${blog.title}" />" onClick="document.location='blogview.jsp?blogId=<c:out value="${blog.id}"/>';" type="button" class="button">
			</c:forEach>
		</td>
	</tr>
</table>
<jsp:include page="includes/footer.jsp" flush="true" />
<%@ include file="/ekms/includes/footer.jsp" %>


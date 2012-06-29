<%@ page import="org.apache.commons.lang.StringUtils,
                 kacang.services.security.SecurityService,
                 kacang.Application,
                 com.tms.collab.weblog.model.Blog,
                 com.tms.collab.weblog.model.BlogUtil,
                 com.tms.collab.weblog.model.WeblogModule"%>
<%@include file="/common/header.jsp"%>
<style>

.midColumn
{
  padding: 10px;
  width: 500px;
  /*margin: auto;
  overflow: hidden;*/
  float: left;
  voice-family: "\"}\"";
  voice-family: inherit;
  width: 509px;
}

.storyBox
{
  border-bottom: 1px solid #B39169;
}


.storyDateBox
{
  font-family: Times New Roman, serif;
  float: left;
  padding:0px 5px 5px 0px;
  margin: 0px 10px 4px 0px;
  border-right: 1px solid #B39169;
  width: 70px;
  position: relative;
  /*background: #f7f7f7;*/
}
.storyDateBox h2
{
  font-weight: 400;
  margin: 0px;
  padding: 0px;
}

.storyDateBox span
{
  font-size: 11px;
}
.blockDisplay
{
  display: block;
}
.noDisplay
{
  display: none;
}

.story
{
  font-family: Verdana, sans-serif;
  line-height: 2em;
  width: 300px
  /*color: #222;*/
  /*margin-bottom: 25px;*/
}

div.sidebarBox, form.sidebarBox
{
  margin-bottom: 25px;
}
</style>
<c:set var="blog" value="${widget.blog}"  />
<c:if test="${blog == null}" >
    <script>
        document.location = "weblog.jsp";
    </script>
</c:if>
<span class="contentName"><c:out value="${blog.title}" /></span>
<c:set var="posts" value="${blog.posts}"/>
<table width="100%">
	<tr>
		<td valign="top"><c:out value="${blog.description}" escapeXml="false"/>
			<table>
				<c:forEach var="post" items="${posts}">
					<tr>
						<td>
						<br>
						<div class="storyBox">
							<div class="storyDateBox">
								<h2>
									<fmt:formatDate value="${post.publishTime}" pattern="d"  />
									<span><fmt:formatDate value="${post.publishTime}" pattern="MMM"/></span>
									<span class="blockDisplay">
									<span class="noDisplay">&middot;</span>
									<fmt:formatDate value="${post.publishTime}" pattern="EEE yyyy"/>
									</span>
								</h2>
							</div>
							<div>
								<a href="postView.jsp?blogId=<c:out value="${widget.blogId}" />&postId=<c:out value="${post.id}" />"style="text-decoration: none;"><strong><c:out value="${post.title}" /></strong></a><br>
								<c:set var="content" value="${post.contents}"/>
								<%
									String translated = StringUtils.replace((String)pageContext.getAttribute("content"), "\n", "<br>");
									pageContext.setAttribute("content", translated);
								%>
								<c:out value="${content}" escapeXml="false"/>
								<br>
							</div>
							<em >@ <fmt:formatDate value="${post.publishTime}" pattern="h:mm a"/></em>&nbsp;&nbsp;&nbsp;
							<a href="<c:out value="${widget.postViewUrl}" />?blogId=<c:out value="${widget.blogId}" />&postId=<c:out value="${post.id}" />#comment">
							<fmt:message key='weblog.label.comments'/>[<font color="#FF0000"><c:out value="${post.totalComments}" /></font>]</a>
							<c:set var="userId" value="${widget.currentUserId}"/>
							<c:set var="postId" value="${post.id}"/>
							<%
								String userId =(String)pageContext.getAttribute("userId");
								String postId = (String) pageContext.getAttribute("postId");
								if(BlogUtil.hasEditPostPermission(userId,postId)){
							%>
							&nbsp;&nbsp;&nbsp;<INPUT TYPE="BUTTON" class="button" value="Edit" onclick="document.location='editPostForm.jsp?postId=<c:out value="${post.id}" />&blogId=<c:out value="${blog.id}" />'"/>
							<%}%>
							<br><br>
						</div>
						</td>
					</tr>
				</c:forEach>
			</table>
		</td>
		<td valign="top" width="30%">
			<div class="sidebarbox">
				<x:template name="calendar" type="com.tms.collab.weblog.ui.BlogCalendar" properties="blogUrl=blogview.jsp"></x:template>
			</div>
			<c:if test="${!empty blog.links}" >
				<div style="border-bottom: 1px solid #B39169;text-alignment:justify"><strong><fmt:message key='weblog.label.links'/></strong></div>
				<table>
					<c:forEach var="link" items="${blog.links}" >
						<tr><td><a href="//<c:out value="${link.url}" />" target="_blank"><c:out value="${link.name}" /></a><br></td></tr>
					</c:forEach>
				</table>
			</c:if>
		</td>
	</tr>
</table>
<%
    Blog blog = (Blog)pageContext.getAttribute("blog");
    SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
    String userId = ss.getCurrentUser(request).getId();
    if(BlogUtil.hasEditBlogPermission(userId,blog.getId()))
	{
%>
<input value="<fmt:message key='weblog.label.editBlog'/>" onClick="document.location='editBlogForm.jsp?blogId=<c:out value="${blog.id}"/>';" type="button" class="button">
<input value="<fmt:message key='weblog.label.addPost'/>" onClick="document.location='addPostForm.jsp?blogId=<c:out value="${blog.id}"/>';" type="button" class="button">
<input value="<fmt:message key='weblog.label.managePost'/>" onClick="document.location='posttable.jsp?blogId=<c:out value="${blog.id}"/>';" type="button" class="button">
<input value="<fmt:message key='weblog.label.manageLinks'/>" onClick="document.location='addLinkForm.jsp?blogId=<c:out value="${blog.id}"/>';" type="button" class="button">
<%
	}
    if(ss.hasPermission(userId,WeblogModule.PERMISSION_DELETE_BLOG,null,null))
	{
        %>
			<input value="<fmt:message key='weblog.label.deleteBlog'/>" onClick="if(confirm('Are you sure you want delete this blog?'))document.location='blogview.jsp?blogId=<c:out value="${blog.id}"/>&et=delete'; return false" type="button" class="button">
        <%
    }
%>






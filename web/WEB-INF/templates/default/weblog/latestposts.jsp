<%@ page import="org.apache.commons.lang.StringUtils,
                 com.tms.collab.weblog.model.Blog,
                 com.tms.collab.weblog.model.WeblogModule,
                 kacang.Application"%>
<%@include file="/common/header.jsp"%>
<c:set var="posts" value="${widget.posts}" />
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr><td colspan="4" class="blogHeader"><fmt:message key='weblog.label.latestWeblogPosts'/></td></tr>
	<tr><td colspan="4" class="blogHeader" height="5"></td></tr>
    <tr>
        <td class="blogLabel"><fmt:message key='weblog.label.weblog'/></td>
        <td class="blogLabel"><fmt:message key='weblog.label.title'/></td>
        <td class="blogLabel"><fmt:message key='weblog.label.author'/></td>
		<td class="blogLabel"><fmt:message key='weblog.label.time'/></td>
    </tr>
	<tr><td colspan="4" height="1"><hr size="1"></td></tr>
    <c:forEach var="post" items="${posts}">
		<tr>
			<td class="blogRow">
				<c:set var="blogId" value="${post.blogId}" ></c:set>
				<%
					WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
					Blog blog = wm.getBlog((String)pageContext.getAttribute("blogId"));
					pageContext.setAttribute("blog",blog);
				%>
				<a href="blogview.jsp?blogId=<c:out value="${blogId}" />"><c:out value="${blog.title}" /></a>
			</td>
			<td class="blogRow"><a href="<c:out value="${widget.postViewUrl}" />?blogId=<c:out value="${post.blogId}" />&postId=<c:out value="${post.id}" />"><c:out value="${post.title}" /></a></td>
			<td class="blogRow"><c:out value="${post.userName}" /></td>
			<td class="blogRow"><fmt:formatDate pattern="${globalDatetimeLong}" value="${post.publishTime}" /></td>
		</tr>
    </c:forEach>
</table>

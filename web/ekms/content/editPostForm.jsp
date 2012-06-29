<%@ page import="com.tms.collab.weblog.ui.PostForm,
                 kacang.ui.WidgetManager,
                 com.tms.collab.weblog.ui.editPostForm"%>
<%@include file="/common/header.jsp" %>
<x:config >
    <page name="editPostPage" >
        <com.tms.collab.weblog.ui.editPostForm name="editPostForm"/>
    </page>
</x:config>
<c:if test="${!empty param.blogId}" >
    <x:set name="editPostPage.editPostForm" property="blogId" value="${param.blogId}" />
</c:if>
<c:if test="${!empty param.postId}" >
    <x:set name="editPostPage.editPostForm" property="postId" value="${param.postId}" />
</c:if>
<c:set var="blogId" value="${param.blogId}" />
<c:if test="${empty param.blogId}" >
    <%
        WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
        if(wm!=null)
		{
            editPostForm form= (editPostForm)wm.getWidget("editPostPage.editPostForm");
            pageContext.setAttribute("blogId",form.getPost().getBlogId());
        }
    %>
</c:if>
<c-rt:set var="posted" value="<%=PostForm.FORWARD_POST_UPDATED%>"  />
<c:if test="${forward.name==posted}" >
    <c:redirect url="posttable.jsp?blogId=${blogId} " ></c:redirect>
</c:if>
<c-rt:set var="cancel" value="<%=PostForm.FORWARD_POST_CANCEL%>"/>
<c:if test="${forward.name==cancel}" >
    <c:redirect url="posttable.jsp?blogId=${blogId}" />
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true" />
<table cellpadding="0" cellspacing="0" width="100%">
	<tr>
        <td valign="top">
            <table cellpadding="3" cellspacing="0" width="100%">
                <tr>
					<td class="contentPath">
						<a href="weblog.jsp" class="contentPathLink"><fmt:message key='weblog.label.weblog'/></a> >
						<a href="" class="contentPathLink" onClick="return false;"><fmt:message key='weblog.label.editPost'/></a>
					</td>
				</tr>
            </table>
        </td>
    </tr>
	<tr>
		<td valign="top" class="contentBody">
			<table cellpadding="2" cellspacing="1" width="95%"  align="center">
				<tr><td>&nbsp;</td></tr>
				<tr>
					<td class="contentBody">
						<input value="<fmt:message key='weblog.label.addPost'/>" onClick="document.location='addPostForm.jsp?blogId=<c:out value="${blogId}"/>';" type="button" class="button">
						<input value="<fmt:message key='weblog.label.editBlog'/>" onClick="document.location='editBlogForm.jsp?blogId=<c:out value="${blogId}"/>';" type="button" class="button">
						<input value="<fmt:message key='weblog.label.managePost'/>" onClick="document.location='posttable.jsp?blogId=<c:out value="${blogId}"/>';" type="button" class="button">
						<input value="<fmt:message key='weblog.label.manageLinks'/>" onClick="document.location='addLinkForm.jsp?blogId=<c:out value="${blogId}"/>';" type="button" class="button">
						<input value="<fmt:message key='weblog.label.viewBlog'/>" onClick="document.location='blogview.jsp?blogId=<c:out value="${blogId}"/>';" type="button" class="button">
					</td>
				</tr>
				<tr><td>&nbsp;</td></tr>
				<tr><td><x:display name="editPostPage"/></td></tr>
				<tr><td>&nbsp;</td></tr>
			</table>
		</td>
	</tr>

</table>
<jsp:include page="includes/footer.jsp" flush="true"  />

<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.weblog.ui.PostTable"%>
<%@ include file="/common/header.jsp" %>
<x:config >
    <page name="PostTablePage" >
        <com.tms.collab.weblog.ui.PostTable name="PostTable"/>
    </page>
</x:config>
<c:if test="${!empty param.blogId}" >
    <x:set name="PostTablePage.PostTable" property="blogId" value="${param.blogId}" />
</c:if>
<c:set var="blogId" value="${param.blogId}" />
<c:if test="${empty param.blogId}" >
    <%
        WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
        if(wm!=null){
            PostTable table = (PostTable)wm.getWidget("PostTablePage.PostTable");
            pageContext.setAttribute("blogId",table.getBlogId());
        }
    %>
</c:if>
<c:if test="${!empty param.id}">
    <c:redirect url="editPostForm.jsp?postId=${param.id}&blogId=${blogId}" />
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
						<a href="" class="contentPathLink" onClick="return false;"><fmt:message key='weblog.label.managePost'/></a>
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
						<input value="<fmt:message key='weblog.label.addPost'/>" onClick="document.location='addPostForm.jsp?blogId=<c:out value="${blogId}"/>'" type="button" class="button">
						<input value="<fmt:message key='weblog.label.editBlog'/>" onClick="document.location='editBlogForm.jsp?blogId=<c:out value="${blogId}"/>'" type="button" class="button">
						<%--<input value="<fmt:message key='weblog.label.managePost'/>" onClick="document.location='posttable.jsp?blogId=<c:out value="${blogId}"/>';" type="button" class="button">--%>
						<input value="<fmt:message key='weblog.label.manageLinks'/>" onClick="document.location='addLinkForm.jsp?blogId=<c:out value="${blogId}"/>'" type="button" class="button">
						<input value="<fmt:message key='weblog.label.viewBlog'/>" onClick="document.location='blogview.jsp?blogId=<c:out value="${blogId}"/>'" type="button" class="button">
					</td>
				</tr>
				<tr><td>&nbsp;</td></tr>
				<tr><td><x:display name="PostTablePage"/></td></tr>
				<tr><td>&nbsp;</td></tr>
			</table>
		</td>
	</tr>
</table>
<jsp:include page="includes/footer.jsp" flush="true"  />
<%@ include file="/ekms/includes/footer.jsp" %>

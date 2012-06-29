<%@ page import="com.tms.collab.weblog.ui.BlogForm,
                 kacang.ui.WidgetManager,
                 com.tms.collab.weblog.ui.addBlogLinkForm"%>
<%@include file="/common/header.jsp" %>
<x:config >
	<page name="addBlogLinkPage">
		<com.tms.collab.weblog.ui.addBlogLinkForm name="linkform"/>
	</page>
</x:config>
<c:set var="blogId" value="${param.blogId}"  ></c:set>
<c:if test="${empty param.blogId}" >
    <%
        WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
        if(wm!=null)
		{
            addBlogLinkForm form = (addBlogLinkForm)wm.getWidget("addBlogLinkPage.linkform");
            pageContext.setAttribute("blogId",form.getBlogId());
        }
    %>
</c:if>
<c-rt:set var="cancel" value="<%=BlogForm.FORWARD_CANCEL%>"/>
<c:if test="${forward.name==cancel }" >
    <script>
        document.location="addLinkForm.jsp?blogId=<c:out value="${blogId}"/>";
    </script>
</c:if>
<c:if test="${!empty param.blogId}" >
    <x:set name="addBlogLinkPage.linkform" property="blogId" value="${param.blogId}" />
</c:if>
<c:if test="${!empty param.id}" >
    <c:redirect url="editLinkForm.jsp?blogId=${blogId}&linkId=${param.id}" ></c:redirect>
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
						<a href="" class="contentPathLink" onClick="return false;"><fmt:message key='weblog.label.addLink'/></a>
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
				<tr><td><x:display name="addBlogLinkPage"/></td></tr>
				<tr><td>&nbsp;</td></tr>
			</table>
		</td>
	</tr>
</table>
<br>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@ include file="/ekms/includes/footer.jsp" %>


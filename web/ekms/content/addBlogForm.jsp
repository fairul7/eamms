<%@ page import="com.tms.collab.weblog.ui.BlogForm,
                 kacang.ui.WidgetManager,
                 com.tms.collab.weblog.ui.addBlogLinkForm,
                 com.tms.collab.weblog.ui.addBlogForm"%>
<%@include file="/common/header.jsp" %>
<x:config >
	<page name="addBlogPage">
		<com.tms.collab.weblog.ui.addBlogForm name="blogform"/>
	</page>
</x:config>
<c-rt:set var="cancel" value="<%=BlogForm.FORWARD_CANCEL%>"/>
<c:if test="${forward.name==cancel }" >
    <script>
        history.back();
        history.back();
    </script>
</c:if>
<%
    WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
    if(wm!=null)
	{
        addBlogForm form = (addBlogForm)wm.getWidget("addBlogPage.blogform");
        pageContext.setAttribute("blogId",form.getBlogId());
    }
%>
<c-rt:set var="cancel" value="<%=BlogForm.FORWARD_BLOG_ADDED%>"/>
<c:if test="${forward.name==cancel }" >
    <script>
        alert("<fmt:message key='weblog.label.weblogCreated'/>!");
        document.location ="addPostForm.jsp?blogId=<c:out value="${blogId}" />";
    </script>
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true" />
<table cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td valign="top">
            <table cellpadding="3" cellspacing="0" width="100%">
                <tr>
					<td class="contentPath">
						<a href="weblog.jsp"  class="contentPathLink"><fmt:message key='weblog.label.weblog'/></a> >
						<a href="" class="contentPathLink" onClick="return false;"><fmt:message key='weblog.label.usersBlog'/></a>
					</td>
				</tr>
            </table>
        </td>
    </tr>
	<tr>
		<td valign="top" class="contentBody">
			<table cellpadding="2" cellspacing="1" width="95%"  align="center">
				<tr><td>&nbsp;</td></tr>
				<tr><td><x:display name="addBlogPage"/></td></tr>
				<tr><td>&nbsp;</td></tr>
			</table>
		</td>
	</tr>
</table>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@ include file="/ekms/includes/footer.jsp" %>


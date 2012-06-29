<%@ page import="com.tms.collab.weblog.ui.BlogForm,
                 kacang.ui.WidgetManager,
                 com.tms.collab.weblog.ui.editBlogForm"%>
<%@include file="/common/header.jsp" %>

<x:config >
<page name="editBlogPage">
            <com.tms.collab.weblog.ui.editBlogForm name="blogform"/>
</page>
</x:config>

<c:if test="${!empty param.blogId}" >
<x:set name="editBlogPage.blogform" property="blogId" value="${param.blogId}" />

</c:if>

<c-rt:set var="updated" value="<%=BlogForm.FORWARD_BLOG_UPDATED%>"  ></c-rt:set>
<c:if test="${forward.name==updated}" >
    <script>
        alert("Blog updated!");
    </script>
</c:if>

<c-rt:set var="cancel" value="<%=BlogForm.FORWARD_CANCEL%>"  ></c-rt:set>
<c:if test="${forward.name==cancel}" >
    <c:redirect url="/cms/weblog.jsp" />
</c:if>

<c:set var="blogId" value="${param.blogId}"  ></c:set>

<c:if test="${empty param.blogId}" >
    <%
        WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
        if(wm!=null){
            editBlogForm form = (editBlogForm)wm.getWidget("editBlogPage.blogform");
            pageContext.setAttribute("blogId",form.getBlogId());
        }
    %>
</c:if>


<jsp:include page="includes/header.jsp" flush="true"  />
   <div class="siteBodyHeader">
       <a href="weblog.jsp"  class="blogHeaderLink">Weblog</a> > Edit Blog
    </div>
     <p>
    <%--<a href="editBlogForm.jsp?blogId=<c:out value="${blogId}"/>">--%>Edit Blog<%--</a>--%> &nbsp;&nbsp;<a href="addPostForm.jsp?blogId=<c:out value="${blogId}" />">Add Post</a> &nbsp;&nbsp;<a href="posttable.jsp?blogId=<c:out value="${blogId}" />">Manage Post</a>
   &nbsp;&nbsp; <a href="addLinkForm.jsp?blogId=<c:out value="${blogId}" />">Manage Links</a> &nbsp;&nbsp;   <a href="blogview.jsp?blogId=<c:out value="${blogId}" />">View Blog</a>
    </p>

   <p>


    <x:display name="editBlogPage" ></x:display>


<jsp:include page="includes/footer.jsp" flush="true"  />


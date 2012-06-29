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
        if(wm!=null){
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




<jsp:include page="includes/header.jsp" flush="true"  />
   <div class="siteBodyHeader">
       <a href="weblog.jsp"  class="blogHeaderLink">Weblog</a> > Edit Post
    </div>
    <p>
    <a href="editBlogForm.jsp?blogId=<c:out value="${blogId}"/>">Edit Blog</a> &nbsp;&nbsp;<a href="addPostForm.jsp?blogId=<c:out value="${blogId}" />">Add Post</a> &nbsp;&nbsp;   <a href="posttable.jsp?blogId=<c:out value="${blogId}" />">Manage Post</a>
   &nbsp;&nbsp; <a href="addLinkForm.jsp?blogId=<c:out value="${blogId}" />">Manage Links</a> &nbsp;&nbsp;   <a href="blogview.jsp?blogId=<c:out value="${blogId}" />">View Blog</a>
   </p>

    <p>

    <x:display name="editPostPage" ></x:display>
    </p>
<jsp:include page="includes/footer.jsp" flush="true"  />

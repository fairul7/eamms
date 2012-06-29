<%@ page import="com.tms.collab.weblog.ui.PostForm,
                 kacang.ui.WidgetManager,
                 com.tms.collab.weblog.ui.PostTable,
                 com.tms.collab.weblog.ui.addPostForm"%>
<%@include file="/common/header.jsp" %>

<x:config >
    <page name="addPostPage" >
        <com.tms.collab.weblog.ui.addPostForm name="addPostForm"/>
    </page>
</x:config>
<c-rt:set var="cancel" value="<%=PostForm.FORWARD_POST_CANCEL%>"  />

<c:if test="${forward.name==cancel}" >
 <script>
    history.back();
    history.back();
 </script>
 </c:if>
<c-rt:set var="added" value="<%=PostForm.FORWARD_POST_ADDED%>"  />


<c:set var="blogId" value="${param.blogId}"  ></c:set>

<c:if test="${forward.name==added}" >
    <%
        WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
        if(wm!=null){
            addPostForm form = (addPostForm)wm.getWidget("addPostPage.addPostForm");
            pageContext.setAttribute("blogId",form.getBlogId());
        }
    %>
    <c:redirect url="posttable.jsp?blogId=${blogId}" ></c:redirect>
</c:if>

<c:if test="${!empty param.blogId}" >
    <x:set name="addPostPage.addPostForm" property="blogId" value="${param.blogId}" />
</c:if>





<jsp:include page="includes/header.jsp" flush="true"  />
   <div class="siteBodyHeader">
       <a href="weblog.jsp"  class="blogHeaderLink">Weblog</a> > Add Post
    </div>
    <p>
    <a href="editBlogForm.jsp?blogId=<c:out value="${blogId}"/>">Edit Blog</a> &nbsp;&nbsp;Add Post &nbsp;&nbsp;<a href="posttable.jsp?blogId=<c:out value="${blogId}" />">Manage Post</a>
    &nbsp;&nbsp; <a href="addLinkForm.jsp?blogId=<c:out value="${blogId}" />">Manage Links</a>   &nbsp;&nbsp;  <a href="blogview.jsp?blogId=<c:out value="${blogId}" />">View Blog</a>
    </p>

    <p>


    <x:display name="addPostPage" ></x:display>
<jsp:include page="includes/footer.jsp" flush="true"  />

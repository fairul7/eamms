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
    if(wm!=null){
        addBlogForm form = (addBlogForm)wm.getWidget("addBlogPage.blogform");
        pageContext.setAttribute("blogId",form.getBlogId());
    }

%>


<c-rt:set var="cancel" value="<%=BlogForm.FORWARD_BLOG_ADDED%>"/>
<c:if test="${forward.name==cancel }" >
    <script>
        alert("New weblog created sucessfully!");
        document.location ="addPostForm.jsp?blogId=<c:out value="${blogId}" />";
    </script>
</c:if>



<jsp:include page="includes/header.jsp" flush="true"  />
   <div class="siteBodyHeader">
       <a href="weblog.jsp"  class="blogHeaderLink">Weblog</a> > Create Blog
    </div>
    <p>


    <x:display name="addBlogPage" ></x:display>


<jsp:include page="includes/footer.jsp" flush="true"  />


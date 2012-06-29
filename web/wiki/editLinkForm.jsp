<%@ page import="com.tms.collab.weblog.ui.BlogForm,
                 kacang.ui.WidgetManager,
                 com.tms.collab.weblog.ui.addBlogLinkForm"%>
<%@include file="/common/header.jsp" %>

<x:config >
<page name="editBlogLinkPage">
            <com.tms.collab.weblog.ui.editBlogLinkForm name="linkform"/>
</page>
</x:config>
<c:set var="blogId" value="${param.blogId}"  ></c:set>

<c:if test="${empty param.blogId}" >
    <%
        WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
        if(wm!=null){
            addBlogLinkForm form = (addBlogLinkForm)wm.getWidget("addBlogLinkPage.linkform");
            pageContext.setAttribute("blogId",form.getBlogId());
        }
    %>
</c:if>

<c-rt:set var="cancel" value="<%=BlogForm.FORWARD_CANCEL%>"/>
<c:if test="${forward.name==cancel }" >
    <script>
        history.back();
        history.back();
    </script>
</c:if>

<c:if test="${!empty param.blogId}" >
    <x:set name="editBlogLinkPage.linkform" property="blogId" value="${param.blogId}" />
</c:if>
<c:if test="${!empty param.id}" >
    <c:redirect url="editLinkForm.jsp?blogId=${blogId}&linkId=${param.id}" ></c:redirect>
</c:if>

<c:if test="${!empty param.linkId}" >
    <x:set name="editBlogLinkPage.linkform" property="linkId" value="${param.linkId}" />
</c:if>






<jsp:include page="includes/header.jsp" flush="true"  />
   <div class="siteBodyHeader">
       <a href="weblog.jsp"  class="blogHeaderLink">Weblog</a> > Edit Link
    </div>
   <p>
    <a href="editBlogForm.jsp?blogId=<c:out value="${blogId}"/>">Edit Blog</a> &nbsp;&nbsp;<a href="addPostForm.jsp?blogId=<c:out value="${blogId}" />">Add Post</a> &nbsp;&nbsp;<a href="posttable.jsp?blogId=<c:out value="${blogId}" />">Manage Post</a>
   &nbsp;&nbsp; <a href="addLinkForm.jsp?blogId=<c:out value="${blogId}" />">Manage Link</a> &nbsp;&nbsp;   <a href="blogview.jsp?blogId=<c:out value="${blogId}" />">View Blog</a>
    </p>
    <p>


    <x:display name="editBlogLinkPage" ></x:display>
     </p>

<jsp:include page="includes/footer.jsp" flush="true"  />


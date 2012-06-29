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

<jsp:include page="includes/header.jsp" flush="true"  />
   <div class="siteBodyHeader">
       <a href="weblog.jsp"  class="blogHeaderLink">Weblog</a> > Manage Post
    </div>
    <p>
  &nbsp;&nbsp;<a href="editBlogForm.jsp?blogId=<c:out value="${blogId}"/>">Edit Blog</a> &nbsp;&nbsp;<a href="addPostForm.jsp?blogId=<c:out value="${blogId}" />">Add Post</a> &nbsp;&nbsp;<%--   <a href="posttable.jsp?blogId=<c:out value="${blogId}" />">--%>Manage Post<%--</a>--%>
  &nbsp;&nbsp; <a href="addLinkForm.jsp?blogId=<c:out value="${blogId}" />">Manage Links</a> &nbsp;&nbsp;   <a href="blogview.jsp?blogId=<c:out value="${blogId}" />">View Blog</a>
  </p>
    <p>
       <x:display name="PostTablePage" ></x:display>

    </p>


<jsp:include page="includes/footer.jsp" flush="true"  />

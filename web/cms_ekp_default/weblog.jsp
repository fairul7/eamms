<%@ page import="kacang.services.security.SecurityService,
                 kacang.Application,
                 kacang.services.security.User,
                 com.tms.collab.weblog.model.WeblogModule,
                 java.util.Collection"%>
<%@ include file="/common/header.jsp" %>

<jsp:include page="includes/header.jsp" flush="true"  />
 <div class="siteBodyHeader">
       Weblog
    </div>

    <p><br>
    <table width="100%">
    <tr>
    <td valign="top">
      <x:template name="latestposts" type="com.tms.collab.weblog.ui.LatestPosts" properties="postViewUrl=postView.jsp" ></x:template>
    </td>
    <td valign="top">
      <x:template name="popular" type="com.tms.collab.weblog.ui.PopularBlogs" properties="blogViewUrl=blogview.jsp" ></x:template><br>
      <x:template name="latestblogs" type="com.tms.collab.weblog.ui.LatestBlogs" properties="blogViewUrl=blogview.jsp" ></x:template>
    </td>
    </tr>
    </table>
      <br>
     <a href="bloguserlist.jsp" >All Users' Blog</a>&nbsp;&nbsp;
 <%
    SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
     User user = ss.getCurrentUser(request);
     WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
     Collection col = wm.getBlogByUserId(user.getId());
     if(ss.hasPermission(user.getId(),WeblogModule.PERMISSION_CREATE_BLOG,null,null)){
         if(col== null||col.size()<3){
         %><a href="addBlogForm.jsp" > Create Blog</a>
            <%
         }

     }
                pageContext.setAttribute("blogs",col);
 %>
 <c:forEach var="blog" items="${blogs}" >
 &nbsp;&nbsp;<a href="blogview.jsp?blogId=<c:out value="${blog.id}"/>"><c:out value="${blog.title}" /></a>
 </c:forEach>

<jsp:include page="includes/footer.jsp" flush="true"  />



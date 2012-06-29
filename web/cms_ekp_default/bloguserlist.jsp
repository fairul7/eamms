<%@ page import="com.tms.collab.weblog.ui.BlogForm"%>
<%@include file="/common/header.jsp" %>

<x:config >
<page name="userPage">
            <com.tms.collab.weblog.ui.UsersList name="userList"/>
</page>
</x:config>

<c:if test="${!empty param.id}" >
<c:redirect url="blogview.jsp?blogId=${param.id}" />
</c:if>


<jsp:include page="includes/header.jsp" flush="true"  />
   <div class="siteBodyHeader">
       <a href="weblog.jsp"  class="blogHeaderLink">Weblog</a> > Users' Blog
    </div>
    <p>


    <x:display name="userPage" ></x:display>


<jsp:include page="includes/footer.jsp" flush="true"  />


<%@ include file="/common/header.jsp" %>

<jsp:include page="includes/header.jsp" flush="true"  />
   <div class="siteBodyHeader">
       <a href="weblog.jsp"  class="blogHeaderLink">Weblog</a> > Post
    </div>

    <x:template name="postview" type="com.tms.collab.weblog.ui.PostView" properties="postId=${param.postId}"></x:template>



<jsp:include page="includes/footer.jsp" flush="true"  />

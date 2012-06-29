<%@ include file="/common/header.jsp" %>

<jsp:include page="includes/header.jsp" flush="true"  />
   <div class="siteBodyHeader">
       <a href="weblog.jsp"  class="blogHeaderLink">Weblog</a>
    </div>

    <p>

    <x:template name="users" type="com.tms.collab.weblog.ui.BlogView" properties="blogId=${param.blogId}&postViewUrl=postView.jsp"></x:template>

</body>


<jsp:include page="includes/footer.jsp" flush="true"  />

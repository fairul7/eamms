<%@ include file="/common/header.jsp" %>
<%@ include file="includes/security.jsp" %>

<x:template type="TemplateProcessForumMessageForm" properties="url=forumMessageList.jsp" />

<jsp:include page="includes/header.jsp" flush="true"  />

    <div class="siteBodyHeader">
        New Forum Message
    </div>

    <x:template type="TemplateDisplayForumMessageForm" />

<jsp:include page="includes/footer.jsp" flush="true"  />


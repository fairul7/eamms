<%@ include file="/common/header.jsp" %>
<%@ include file="includes/security.jsp" %>

<x:template type="TemplateProcessForumTopicForm" properties="url=forumTopicList.jsp" />

<jsp:include page="includes/header.jsp" flush="true"  />

    <div class="siteBodyHeader">
        New Forum Topic
    </div>

    <x:template type="TemplateDisplayForumTopicForm" />

<jsp:include page="includes/footer.jsp" flush="true"  />


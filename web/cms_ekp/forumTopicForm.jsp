<%@ include file="/common/header.jsp" %>

<c:if test="${empty sessionScope.currentUser || sessionScope.currentUser.id == 'anonymous'}">
    <jsp:forward page="login.jsp"/>
</c:if>

<x:template type="TemplateProcessForumTopicForm" properties="url=forumTopicList.jsp" />

<jsp:include page="includes/header.jsp" flush="true"  />

    <div class="siteBodyHeader">
        New Forum Topic
    </div>

    <p>
    <x:template type="TemplateDisplayForumTopicForm" />

<jsp:include page="includes/footer.jsp" flush="true"  />


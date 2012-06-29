<%@ include file="/common/header.jsp" %>

<c:if test="${empty sessionScope.currentUser || sessionScope.currentUser.id == 'anonymous'}">
    <jsp:forward page="login.jsp" />
</c:if>

<x:template type="TemplateProcessForumMessageForm" properties="url=forumMessageList.jsp" />

<jsp:include page="includes/header.jsp" flush="true"  />

    <div class="siteBodyHeader">
        New Forum Message
    </div>

    <p>
    <x:template type="TemplateDisplayForumMessageForm" />

<jsp:include page="includes/footer.jsp" flush="true"  />


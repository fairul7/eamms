<%@ include file="/common/header.jsp" %>

<c:if test="${empty currentUser || currentUser.id == 'anonymous'}">
    <c:redirect url="index.jsp"/>
</c:if>

<jsp:include page="includes/header.jsp" flush="true"  />

    <div class="siteBodyHeader">
        Events
    </div>

    <x:template type="TemplateDisplayEventList" properties="start=${param.start}&end=${param.end}" />

<jsp:include page="includes/footer.jsp" flush="true"  />


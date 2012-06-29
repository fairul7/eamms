<%@ include file="/common/header.jsp" %>

<c:if test="${empty currentUser || currentUser.id == 'anonymous'}">
    <c:redirect url="index.jsp"/>
</c:if>

<jsp:include page="includes/header.jsp" flush="true"  />

    <div class="siteBodyHeader">
        View Event
    </div>

    <x:template type="TemplateDisplayEvent" properties="id=${param.id}" />
    <br>
    <br>
    <input type="button" class="button" onclick="javascript:history.back()" value="Back">

<jsp:include page="includes/footer.jsp" flush="true"  />


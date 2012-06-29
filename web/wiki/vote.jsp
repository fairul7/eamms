<%@ include file="/common/header.jsp" %>

<jsp:include page="includes/header.jsp" flush="true"  />

    <div class="siteBodyHeader">
        Vote
    </div>

    <x:template type="TemplateDisplayVote" properties="id=${param.VoteID}&showImage=true" />

<jsp:include page="includes/footer.jsp" flush="true"  />


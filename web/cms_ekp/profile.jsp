<%@ include file="/common/header.jsp" %>

<x:template type="TemplateProcessProfileForm" properties="forward=../cms/index.jsp" />

<jsp:include page="includes/header.jsp" flush="true"/>

    <div class="siteBodyHeader">
        User Profile
    </div>

    <x:template type="TemplateDisplayProfileForm" />

<jsp:include page="includes/footer.jsp" flush="true"/>
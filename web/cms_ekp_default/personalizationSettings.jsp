<%@ include file="/common/header.jsp" %>
<%@ include file="includes/security.jsp" %>

<x:template type="TemplateProcessPersonalizationSettings" properties="forward=./personalization.jsp" />

<jsp:include page="includes/header.jsp" flush="true"/>

    <b>
    <div class="siteBodyHeader">
        Personalization Settings
    </div>
    </b>

    <x:template type="TemplateDisplayPersonalizationSettings" />

<jsp:include page="includes/footer.jsp" flush="true"/>
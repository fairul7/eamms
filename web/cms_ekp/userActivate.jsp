<%@ include file="/common/header.jsp" %>

<%-- Process User Activation --%>
<x:template type="TemplateProcessUserActivateForm" properties="forward=../cms/index.jsp" />

<jsp:include page="includes/header.jsp" flush="true"/>

    <div class="siteBodyHeader">
        User Activation
    </div>

    <x:template type="TemplateDisplayUserActivateForm" />

<jsp:include page="includes/footer.jsp" flush="true"/>
<%@ include file="/common/header.jsp" %>

<x:template type="TemplateProcessSignupForm" properties="forward=../cms/signupMessage.jsp" />

<jsp:include page="includes/header.jsp" flush="true"/>

    <div class="siteBodyHeader">
        Register New User
    </div>

    <x:template type="TemplateDisplaySignupForm" />

<jsp:include page="includes/footer.jsp" flush="true"/>
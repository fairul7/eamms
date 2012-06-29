<%@ include file="/common/header.jsp" %>
<%-- Process Actions --%>
<x:template type="TemplateProcessLoginForm" properties="forward=../cms/index.jsp" />
<x:template type="TemplateProcessForgetPasswordForm" properties="forward=../cms/index.jsp" />

<jsp:include page="includes/header.jsp" flush="true"/>

    <div class="siteBodyHeader">
        Login
    </div>
    <x:template type="TemplateDisplayLoginForm" />
    <hr size="1">

    <div class="siteBodyHeader">
        Forgot Your Password?
    </div>
    <x:template type="TemplateDisplayForgetPasswordForm" />
    <hr size="1">

    <div class="siteBodyHeader">
        Register
    </div>
    <p>
    If you are not a registered user, please <a href="signup.jsp">signup</a> first
    </p>

<jsp:include page="includes/footer.jsp" flush="true"/>
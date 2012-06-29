<%@ page import="com.tms.portlet.ui.ThemeForm"%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="themesEdit">
        <com.tms.portlet.ui.ThemeEdit name="form"/>
    </page>
</x:config>
<%-- Event Handling --%>
<c:if test="${!(empty param.themeId)}">
    <x:set name="themesEdit.form" property="themeId" value="${param.themeId}"/>
</c:if>
<c-rt:set var="forward_cancel" value="<%= ThemeForm.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_success" value="<%= ThemeForm.FORWARD_SUCCESS %>"/>
<c:if test="${forward.name == forward_cancel}">
    <c:redirect url="/ekms/portalserver/themes.jsp"/>
</c:if>
<c:if test="${forward.name == forward_success}">
    <script>
        alert("<fmt:message key='portlet.message.themeUpdated'/>");
        document.location = "<c:url value="/ekms/portalserver/"/>themes.jsp";
    </script>
</c:if>
<%-- Render --%>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>
<x:display name="themesEdit.form"/>
<jsp:include page="includes/footer.jsp"/>
<%@ include file="/ekms/includes/footer.jsp" %>


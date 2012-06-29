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
    <c:redirect url="themes.jsp"/>
</c:if>
<c:if test="${forward.name == forward_success}">
    <script>
        alert("<fmt:message key='portlet.message.themeUpdated'/>");
        document.location = "<c:url value="themes.jsp"/>";
    </script>
</c:if>
<%-- Render --%>
<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<table width="100%">
<tr>
    <td class="portletTableHeader">
        <fmt:message key='general.label.systemAdministration'/> > <fmt:message key='portlet.message.editthemeRegistration'/>
    </td>
</tr>
<tr>
    <td>
        <x:display name="themesEdit.form"/>
    </td>
</tr>
</table>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>


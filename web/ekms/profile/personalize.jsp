<%@ page import="com.tms.portlet.ui.PortalServerPersonalize,
                 kacang.services.security.ui.UserLocaleForm"%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="personalize">
        <com.tms.portlet.ui.PortalServerPersonalize name="personalization"/>
    </page>
</x:config>
<%-- Event Handling --%>
<c-rt:set var="forward_add" value="<%= PortalServerPersonalize.FORWARD_ADD %>"/>
<c-rt:set var="forward_update" value="<%= PortalServerPersonalize.FORWARD_UPDATE %>"/>
<c-rt:set var="forward_cancel" value="<%= PortalServerPersonalize.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_success" value="<%= UserLocaleForm.FORWARD_SUCCESS %>"/>
<c-rt:set var="forward_pcancel" value="<%= UserLocaleForm.CANCEL_FORM_ACTION %>"/>
<c:if test="${(forward.name == forward_add)}">
    <script>alert("<fmt:message key='portlet.message.portletAdded'/>");</script>
</c:if>
<c:if test="${(forward.name == forward_success)}">
    <script>
    <!--
        alert("<fmt:message key='portlet.message.settingUpdated'/>");
        location.href="personalize.jsp";
    //-->
    </script>
</c:if>
<c:if test="${(forward.name == forward_update)}">
    <script>alert("<fmt:message key='portlet.message.settingUpdated'/>");</script>
</c:if>
<c:if test="${(forward.name == forward_cancel || forward.name == forward_pcancel)}">
    <%--<c:redirect url="index.jsp"/>--%>
    <script>document.location="<c:url value="/ekms/"/>index.jsp";</script>
</c:if>
<%-- END: Event Handling --%>
<%@ include file="/ekms/includes/header.jsp" %>
<table width="100%" border="0" cellpadding="5" cellspacing="0">
    <tr><td align="center"><x:display name="personalize.personalization"/></td></tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>

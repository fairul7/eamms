<%@ page import="com.tms.portlet.ui.PortletForm"%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="portletsAdd">
        <com.tms.portlet.ui.PortletAdd name="form"/>
    </page>
</x:config>
<%-- Event Handling --%>
<c-rt:set var="forward_cancel" value="<%= PortletForm.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_success" value="<%= PortletForm.FORWARD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= PortletForm.FORWARD_FAILED %>"/>
<c:if test="${forward.name == forward_cancel}">
    <c:redirect url="/ekms/portalserver/portlets.jsp"/>
</c:if>
<c:if test="${forward.name == forward_success}">
    <script>
        alert("<fmt:message key='portlet.message.portletAdded'/>");
        document.location = "<c:url value="/ekms/portalserver/"/>portlets.jsp";
    </script>
</c:if>
<%-- Render --%>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<x:display name="portletsAdd.form"/>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>




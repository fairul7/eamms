<%@ page import="com.tms.portlet.ui.PortletForm,
                 com.tms.portlet.ui.PortletEdit,
                 kacang.ui.WidgetManager,
                 kacang.ui.Widget"%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="portletsOpen">
        <com.tms.portlet.ui.PortletEdit name="form"/>
    </page>
</x:config>
<%-- Event Handling --%>
<c:if test="${!(empty param.portletId)}">
    <x:set name="portletsOpen.form" property="portletId" value="${param.portletId}"/>
</c:if>
<c-rt:set var="forward_add_preference" value="<%= PortletEdit.FORWARD_ADD_PREFERENCE %>"/>
<c-rt:set var="forward_cancel" value="<%= PortletForm.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_success" value="<%= PortletForm.FORWARD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= PortletForm.FORWARD_FAILED %>"/>
<c:if test="${forward.name == forward_cancel}">
    <c:redirect url="/ekms/portalserver/portlets.jsp"/>
</c:if>
<c:if test="${forward.name == forward_success}">
    <script>
        alert("<fmt:message key='portlet.message.portletUpdated'/>");
        document.location = "<c:url value="/ekms/portalserver/"/>portlets.jsp";
    </script>
</c:if>
<c:if test="${forward.name == forward_add_preference}">
    <%
        WidgetManager manager = WidgetManager.getWidgetManager(request);
        PortletEdit form = (PortletEdit) manager.getWidget("portletsOpen.form");
        request.setAttribute("pid", form.getPortletId());
    %>
    <script>
        window.open("<c:url value="/ekms/portalserver/"/>portletPreferences.jsp?portletId=<%= request.getAttribute("pid") %>", "preferenceWindow", "height=200,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    </script>
</c:if>
<c:if test="${(!(empty param.portletId)) && (!(empty param.name))}">
    <script>
        window.open("<c:url value="/ekms/portalserver/"/>portletPreferences.jsp?portletId=<c:out value="${param.portletId}"/>&name=<c:out value="${param.name}"/>", "preferenceWindow", "height=200,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    </script>
</c:if>
<%-- Render --%>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>
<x:display name="portletsOpen.form"/>
<jsp:include page="includes/footer.jsp"/>
<%@ include file="/ekms/includes/footer.jsp" %>




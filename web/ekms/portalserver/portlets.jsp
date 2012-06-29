<%@ page import="com.tms.portlet.ui.PortalServer,
                 com.tms.portlet.ui.ThemeTable"%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="portlets">
        <com.tms.portlet.ui.PortletTable name="table"/>
    </page>
</x:config>
<%-- Event Handling --%>
<c-rt:set var="forward_add" value="<%= ThemeTable.FORWARD_ADD %>"/>
<c-rt:set var="forward_delete" value="<%= ThemeTable.FORWARD_DELETE %>"/>
<c:if test="${!(empty param.portletId)}">
    <c:redirect url="/ekms/portalserver/portletsOpen.jsp?portletId=${param.portletId}"/>
</c:if>
<c:if test="${forward.name == forward_add}">
    <c:redirect url="/ekms/portalserver/portletsAdd.jsp"/>
</c:if>
<c:if test="${forward.name == forward_delete}">
    <script>alert("<fmt:message key='portlet.message.portletsDeleted'/>");</script>
</c:if>
<%-- Render --%>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellpadding="5" cellspacing="1">
    <tr><td class="portletHeader"><fmt:message key='portlet.message.portletRegistry'/></td></tr>
    <tr><td class="portletLabel" align="center"><x:display name="portlets.table"/><br><br></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>




<%@ page import="com.tms.portlet.ui.PortalServer,
                 com.tms.portlet.ui.ThemeTable"%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="themes">
        <com.tms.portlet.ui.ThemeTable name="table"/>
    </page>
</x:config>
<%-- Event Handling --%>
<c-rt:set var="forward_add" value="<%= ThemeTable.FORWARD_ADD %>"/>
<c-rt:set var="forward_delete" value="<%= ThemeTable.FORWARD_DELETE %>"/>
<c:if test="${!(empty param.themeId)}">
    <c:redirect url="themesOpen.jsp?themeId=${param.themeId}"/>
</c:if>
<c:if test="${forward.name == forward_add}">
    <c:redirect url="/ekms/portalserver/themesAdd.jsp"/>
</c:if>
<c:if test="${forward.name == forward_delete}">
    <script>alert("<fmt:message key='portlet.message.themesDeleted'/>");</script>
</c:if>
<%-- Render --%>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>
<table width="100%" border="0" cellpadding="5" cellspacing="0">
    <tr><td class="portletHeader"><fmt:message key='portlet.message.themeRegistry'/></td></tr>
    <tr><td class="portletLabel" align="center"><x:display name="themes.table"/><br><br></td></tr>
</table>
<jsp:include page="includes/footer.jsp"/>
<%@ include file="/ekms/includes/footer.jsp" %>




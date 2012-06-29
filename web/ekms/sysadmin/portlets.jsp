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
    <c:redirect url="portletsOpen.jsp?portletId=${param.portletId}"/>
</c:if>
<c:if test="${forward.name == forward_add}">
    <c:redirect url="portletsAdd.jsp"/>
</c:if>
<c:if test="${forward.name == forward_delete}">
    <script>alert("<fmt:message key='portlet.message.portletsDeleted'/>");</script>
</c:if>
<%-- Render --%>
<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key='general.label.systemAdministration'/> > <fmt:message key="portlet.message.portletRegistry"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

    <x:display name="portlets.table"/>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>

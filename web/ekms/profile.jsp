<%@ page import="kacang.services.security.ui.Profile,
                 com.tms.hr.competency.ui.UserCompetencyForm"%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="profile">
        <kacang.services.security.ui.Profile name="profileForm"/>
    </page>
</x:config>
<c-rt:set var="forward_success" value="<%= Profile.FORWARD_SUCCESS %>"/>
<c-rt:set var="forward_cancel" value="<%= Profile.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_competency_add" value="<%= UserCompetencyForm.FORWARD_ADD %>"/>
<c:if test="${forward_success == forward.name}">
    <script>
        alert("<fmt:message key='general.label.profileUpdated'/>");
    </script>
</c:if>
<c:if test="${forward.name == forward_competency_add}">
    <script>
        window.open("<c:url value="/ekms/worms/addUserCompetency.jsp"/>", "profileWindow", "height=300,width=400,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    </script>
</c:if>
<c:if test="${forward_cancel == forward.name}">
    <c:redirect url="index.jsp"/>
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<table width="100%" border="0" cellpadding="5" cellspacing="0">
    <tr><td align="center"><x:display name="profile.profileForm"/></td></tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>

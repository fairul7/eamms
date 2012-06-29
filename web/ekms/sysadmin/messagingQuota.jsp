<%@ page import="com.tms.collab.messaging.model.Util"%>
<%@ include file="/common/header.jsp" %>
<%
	String clear = request.getParameter("clearOrphan");
	if(!("".equals(clear) || clear == null))
	{
		Util.cleanup();
%>
	<script>alert("<fmt:message key="messaging.label.clearOrphanAlert"/>");</script>
<% } %>
<x:config>
    <page name="messagingQuota">
        <com.tms.collab.messaging.ui.QuotaForm name="form" />
        <com.tms.collab.messaging.ui.QuotaTable name="table" width="100%"/>
    </page>
</x:config>
<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key='general.label.systemAdministration'/> > <fmt:message key="messaging.label.messagingQuota"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
<p>&nbsp;</p>
<div align="center"><x:display name="messagingQuota.form"/></div>
<x:display name="messagingQuota.table"/>
<form method="post" action="<c:url value="/ekms/sysadmin/messagingQuota.jsp"/>">
	<table cellpadding="2" cellspacing="1" width="100%" border="0">
		<tr><td><input name="clearOrphan" type="submit" class="buttonClass" value="<fmt:message key="messaging.label.clearOrphan"/>" onClick="if(!(confirm('<fmt:message key="messaging.label.clearOrphanConfirmation"/>'))) return false;"></td></tr>
	</table>
</form>
<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>

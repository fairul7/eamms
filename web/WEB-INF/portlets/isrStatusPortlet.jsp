<%@ page import="com.tms.collab.isr.permission.model.PermissionModel,
                 kacang.Application,
                 kacang.services.security.*"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="isr">
        <com.tms.collab.isr.ui.ISRStatusPortlet name="statusPortlet"/>
    </page>
</x:config>

<%
	Application app = Application.getInstance();
	PermissionModel permissionModel = (PermissionModel) app.getModule(PermissionModel.class);
	SecurityService security = (SecurityService) app.getService(SecurityService.class);
	String userId = security.getCurrentUser(request).getId();
	boolean isAccessible = permissionModel.isUserInActiveGroup(userId);
%>

<style type="text/css">
.fieldTitle{
	text-align:left;
	font-weight:bold;
}
</style>

<c:choose>
<c-rt:when test="<%=isAccessible%>">
    <x:display name="isr.statusPortlet"/>
</c-rt:when>
<c:otherwise>
    Internal Service Request (ISR) module requires proper setup of your account at Hierachy Management of Organization Chart. 
    <br/>Please consult your System Administrator or relevant authority to perform relevant setup accordingly, before you can be granted access to ISR.
</c:otherwise>
</c:choose>
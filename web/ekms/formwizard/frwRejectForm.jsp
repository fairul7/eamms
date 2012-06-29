<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp"%>


<x:config  reloadable="${param.reload}">
        <page name="ekmsRejectFormpage">
            <com.tms.collab.formwizard.ui.RejectForm name="ekmsRejectForm" />
        </page>
</x:config>

<c:if test="${!empty param.formUID}">
	<x:set name="ekmsRejectFormpage.ekmsRejectForm" property="formUID" value="${param.formUID}"/>
</c:if>


<c:if test="${forward.name=='rejected'}" >
<script>
<!--
window.opener.location = 'frwApproveFormData.jsp';
window.close();
//-->    
</script>
</c:if>

<c:if test="${forward.name=='cancel'}" >
<script>
<!--
window.close();
//-->    
</script>
</c:if>


<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
    <title>Reject Submitted Form Data</title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body>
	        <x:display name="ekmsRejectFormpage.ekmsRejectForm"/>      
</body>
</html>
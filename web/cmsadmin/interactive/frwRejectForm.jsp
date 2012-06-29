<%@include file="/common/header.jsp"%>


<x:config  reloadable="${param.reload}">
        <page name="rejectFormpage">
            <com.tms.collab.formwizard.ui.RejectForm name="rejectForm" />
        </page>
</x:config>

<c:if test="${!empty param.formUID}">
	<x:set name="rejectFormpage.rejectForm" property="formUID" value="${param.formUID}"/>
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
     <link rel="stylesheet" href="<%= request.getContextPath() %>/cmsadmin/styles/style.css">
</head>
<body>
	<x:display name="rejectFormpage.rejectForm"/>
</body>
</html>
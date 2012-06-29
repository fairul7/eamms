<%@ include file="/common/header.jsp" %>
<html>
	<head><title>EKP Help Index</title>
	<link rel="stylesheet" href="/help/images/default.css">
</head>
<body>
<table cellpadding="0" cellspacing="0">
<script>
	function maximizeHelp()
	{
	window.open("<%= request.getRequestURI() %>", "helpPopup", "height=350,width=500,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
	}
</script>
<a href="" onClick="maximizeHelp(); return false;"><img src="<c:url value="/ekms/images/menu/ic_enlarge.gif"/>" border="0" title="<fmt:message key="theme.ekp2005.enlarge"/>"></a>
</table>

<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.maillist.ManageMailList" module="com.tms.cms.maillist.model.MailListModule" url="noPermission.jsp" />

<html>
<head>
<title><fmt:message key='maillist.label.previewMailingList'/></title>
</head>
<body onload="focus()">
<x:template type="com.tms.cms.maillist.ui.MailListPreview" properties="id=${param.id}"/>
</body>
</html>
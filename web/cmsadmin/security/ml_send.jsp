<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.maillist.ManageMailList" module="com.tms.cms.maillist.model.MailListModule" url="noPermission.jsp" />

<html>
<head>
<title><fmt:message key='maillist.label.sendMailingList'/></title>
</head>
<body onload="focus()">
<script language="JavaScript">
<!--
    document.write("<p>");
//-->
</script>
<%
    out.print("<p><table id='tempMsg'><tr><td>Sending mailing list. Please wait... (do not refresh your browser)</td></tr></table><p>");
    out.flush();
%>
<x:template type="com.tms.cms.maillist.ui.MailListSend" properties="id=${param.id}"/>
<script language="JavaScript">
<!--
    e = document.getElementById("tempMsg");
    e.style.display="none";
//-->
</script>
</body>
</html>
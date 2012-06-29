<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />

<table width="100%" cellpadding="5" cellspacing="1" border="0"><tr><td class="contentBgColor" valign="top">

<b><fmt:message key="messaging.label.accountLog" /></b><br><br>

<textarea cols="80" rows="20"><c:out value="${w.pop3Account.accountLog}" /></textarea>
<br>[ <a href="<c:url value="/ekms/messaging/editPop3Account.jsp?accountId=${w.accountId}&clearLog=1" />"><fmt:message key="messaging.label.clearLog" /></a> ]

</td></tr></table>
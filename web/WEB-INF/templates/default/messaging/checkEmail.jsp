<%@ page import="com.tms.collab.messaging.ui.CheckEmail,
                 java.io.StringWriter,
                 java.io.PrintWriter"%>
<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />

<fmt:message key='messaging.message.emailsdownloadedfrom'/>
<font color="#FF0000"><b><c:out value="${w.accountCount}" /></b></font>
<fmt:message key='messaging.message.pOP3accounts'/>

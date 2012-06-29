<%@ page import="java.util.List,
                 com.tms.collab.messaging.model.MessagingQueue,
                 com.tms.collab.messaging.model.Pop3Account,
                 javax.mail.internet.MimeMessage,
                 com.tms.collab.messaging.model.SmtpAccount,
                 kacang.Application,
                 java.util.ResourceBundle,
                 com.tms.collab.messaging.model.queue.QueueItem,
                 kacang.services.security.SecurityService,
                 kacang.services.security.User,
                 com.tms.collab.messaging.model.queue.SendQueueItem"%>
<%@include file="/common/header.jsp" %>
<%
    SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
    MessagingQueue mq = MessagingQueue.getInstance();
    List checkPop3Queue = mq.getCheckPop3Queue();
%>
<c:set var="qs" value="${widget}" />

<blockquote>
<table border="1" cellpadding="5" cellspacing="0" width="100%"><tr><td>
<b><fmt:message key="messaging.label.queueStatus.jobs" /></b><br>
<fmt:message key="messaging.label.queueStatus.jobsDescription" />
<ul>
<c:if test="${empty qs.jobNames}">
    <li>
    <fmt:message key="messaging.label.queueStatus.jobsNotRunning" />
    </li>
</c:if>
<c:if test="${!empty qs.jobNames}">
    <li>
    <fmt:message key="messaging.label.queueStatus.jobsRunning" />
    </li>
</c:if>
<li>
<fmt:message key="messaging.label.queueStatus.currentCheck">
    <fmt:param><%= mq.getCheckPop3Current() %></fmt:param>
</fmt:message>
</li>
<li>
<fmt:message key="messaging.label.queueStatus.currentDownload">
    <fmt:param><%= mq.getDownloadPop3Current() %></fmt:param>
</fmt:message>
</li>
<li>
<fmt:message key="messaging.label.queueStatus.currentSend">
    <fmt:param><%= mq.getSendSmtpQueueCurrent() %></fmt:param>
</fmt:message>
</li>
</ul>
<form>
    <input type="button" class="button" value="<fmt:message key="messaging.label.queueStatus.startJobs" />" onclick="location='<c:url value="/ekms/messaging/queueStatus.jsp?action=start" />'">
    <input type="button" class="button" value="<fmt:message key="messaging.label.queueStatus.stopJobs" />" onclick="location='<c:url value="/ekms/messaging/queueStatus.jsp?action=stop" />'">
</form>
</td></tr></table>

<br><br>

    <table border="1" cellpadding="5" cellspacing="0" width="100%">
    <tr><td>
    <b><fmt:message key="messaging.label.queueStatus.queue" /></b><br>

    <blockquote>
    <fmt:message key="messaging.label.queueStatus.checkSize" /> <%= checkPop3Queue.size() %>
    <br>
    <%
        Application app = Application.getInstance();
        QueueItem o;
        User user;
        if(checkPop3Queue.size()>0) {
            out.print(app.getMessage("messaging.label.queueStatus.next10InQueue"));
            for (int i = 0; i < checkPop3Queue.size() && i <10; i++) {
                o = (QueueItem) checkPop3Queue.get(i);
                user = ss.getUser(o.getUserId());
                out.print("<li>" +  user.getUsername() + "</li>");
            }
        }
    %>
    </blockquote>

    <%
        List downloadPop3Queue = mq.getDownloadPop3Queue();
    %>
    <blockquote>
    Download POP3 queue size: <%= downloadPop3Queue.size() %>.
    <br>
    <%
        if(downloadPop3Queue.size()>0) {
            out.print(app.getMessage("messaging.label.queueStatus.next10InQueue"));
            for (int i = 0; i < downloadPop3Queue.size() && i <10; i++) {
                o = (QueueItem) downloadPop3Queue.get(i);
                user = ss.getUser(o.getUserId());
                out.print("<li>" +  user.getUsername() + "</li>");
            }
        }
    %>
    </blockquote>

    <%
        List sendSmtpQueue = mq.getSendSmtpQueue();
    %>
    <blockquote>
    Send SMTP messages queue size: <%= sendSmtpQueue.size() %>.
    <br>
    <%
        Object[] sqi;
        MimeMessage mimeMessage;
        SmtpAccount smtpAccount;
        if(sendSmtpQueue.size()>0) {
            out.print(app.getMessage("messaging.label.queueStatus.next10InQueue"));
            for (int i = 0; i < sendSmtpQueue.size() && i <10; i++) {
                sqi = (Object[]) sendSmtpQueue.get(i);
                smtpAccount = (SmtpAccount)sqi[0];
                mimeMessage = (MimeMessage)sqi[1];
                out.print("<li>\"" +  mimeMessage.getSubject() + "\" " + app.getMessage("messaging.label.queueStatus.via", "via") + " " + smtpAccount.getServerName() + "</li>");
            }
        }
    %>
    </blockquote>
    </td></tr>

    </table>



<br><br>

</blockquote>
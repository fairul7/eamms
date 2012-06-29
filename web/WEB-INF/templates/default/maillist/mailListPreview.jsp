<%@ page import="com.tms.cms.maillist.model.MailList,
                 kacang.ui.Widget"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="w" value="${widget}"/>
<b><fmt:message key='maillist.label.previewMailingList'/></b>
<br><br>

<c:if test="${w.mailList.readyToSend}">

<b><fmt:message key='maillist.label.from'/>:</b> <c:out value="${w.mailList.senderEmail}" /><br>
<b><fmt:message key='maillist.label.recipients'/>:</b> <c:out value="${w.mailList.recipientsEmailForDisplay}" /><br>
<b><fmt:message key='maillist.label.subject'/>:</b> <c:out value="${w.mailList.subject}" /><br>
<pre>
--- [ <fmt:message key='maillist.label.BEGIN'/>: <fmt:message key='maillist.label.mailingListPreview'/> ] ---</pre><c:if test="${!w.mailList.html}"><pre><c:out value="${w.mailList.content}" /></pre></c:if><c:if test="${w.mailList.html}"><c:out value="${w.mailList.content}" escapeXml="yes" /></c:if><pre>--- [ <fmt:message key='maillist.label.END'/>: <fmt:message key='maillist.label.mailingListPreview'/> ] ---</pre>

</c:if>

<c:if test="${!w.mailList.readyToSend}">
    <fmt:message key='maillist.error.mailingListNotReady'/> :-
    <ul>
        <li><fmt:message key='maillist.label.noSubject'/>
        <li><fmt:message key='maillist.error.noSenderEmail'/>
        <li><fmt:message key='maillist.error.noRecipient'/>
        <li><fmt:message key='maillist.error.mailingListMustBeSaved'/>
    </ul>
</c:if>

<br>
<a href="JavaScript:window.close()"><fmt:message key='ad.label.close'/></a>
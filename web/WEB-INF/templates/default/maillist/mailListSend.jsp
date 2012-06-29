<%@ page import="com.tms.cms.maillist.model.MailList"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="w" value="${widget}"/>

<b><fmt:message key='maillist.label.sendMailingList'/></b>
<br><br>

<c:if test="${w.busy}">
    <fmt:message key='maillist.error.mailingListSystemBusy'/>
</c:if>
<c:if test="${!w.busy}">
    <c:out value="${w.message}" />
</c:if>

<br>
<a href="JavaScript:window.close()"><fmt:message key='ad.label.close'/></a>
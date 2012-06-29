<%@ page import="com.tms.collab.messaging.ui.CheckEmail,
                 java.io.StringWriter,
                 java.io.PrintWriter,
                 com.tms.collab.messaging.ui.InboxPortlet,
                 com.tms.collab.messaging.model.Account"%>
<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />

<c:if test="${notActivated}">
    <fmt:message key='messaging.message.notActivatedMessage'/>. <a href="<c:url value="/ekms/" />messaging/"><fmt:message key='messaging.message.clickhere'/></a> <fmt:message key='messaging.message.toActivate'/>.
</c:if>

<c:if test="${!notActivated}">
    <c:set var="ww" value="${widgets['statusHeader.status']}"/>
    <fmt:message key='messaging.message.youhave'/> <font color=red><c:out value="${ww.emailCount}" /></font>
    <fmt:message key='messaging.message.newmailsfrom'/>
    <font color=red><c:out value="${ww.accountCount}" /></font>
    <fmt:message key='messaging.message.inboxPop3Accounts'/>

    <fmt:message key='messaging.message.lastcheckedat'/> <fmt:formatDate value="${ww.lastCheckDate}" pattern="${globalTimeLong}" />.
    <hr size="1">

    <table cellpadding=0 cellspacing=2 border=0 width="100%">

    <c:forEach var="message" items="${w.messages}" varStatus="status">
        <c:set var="indicator" value="${message.indicator}" scope="page" />
        <%
            Object indicator = pageContext.getAttribute("indicator");
            String color = (String) Account.indicatorColorMap.get(indicator.toString());
            if (color == null) {
                color = "black";  // default color
            }
            pageContext.setAttribute("color", color);
        %>
        <c:set var="subject" value="${message.subject}" scope="page" />
        <%
            String subject = (String) pageContext.getAttribute("subject");
            subject = subject==null ? "(no subject)" : subject.trim();
            subject = subject.length() == 0 ? null : subject;
            pageContext.setAttribute("subject", subject);

        %>
			<tr>
			<td valign=top align=left width=16><c:choose><c:when test="${message.read}"><IMG SRC="images/ic_mail.gif" height=14 width=16 border=0></c:when><c:otherwise><IMG SRC="images/ic_unread.gif" height=10 width=13 border=0></c:otherwise></c:choose></td>
			<td valign=top align=right width=16><font color="<c:out value="${color}" />">&bull;</font></td>
			<td valign=top align=left colspan=2><a href="<c:url value="/ekms/messaging/readMessage.jsp?messageId=${message.id}&index=${status.index+1}" />" class="tsBody"><c:if test="${!message.read}"><b></c:if><c:out value="${subject}" default="(no subject)" /><c:if test="${!message.read}"></b></c:if></a></td>
			</tr>
			<tr>
			<td valign=top align=left colspan=2>&nbsp;</td>
			<td valign=top align=left><span class="generallink"><c:out value="${message.from}" escapeXml="false" /></span></td>
			<td valign=top align=right nowrap><span class="tsBody2"><fmt:formatDate value="${message.date}" pattern="${globalDatetimeLong}"/></span></td>
			</tr>
			<tr>
			<td colspan="3"><img src="<c:url value='/ekms/images/clear.gif'/>" height="5"></td>
			</tr>
    </c:forEach>

    </table>

</c:if>

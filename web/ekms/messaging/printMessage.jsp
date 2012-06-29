<%@ page pageEncoding="UTF-8" %>
<%@ page import="com.tms.collab.messaging.model.MessagingModule,
                 com.tms.collab.messaging.model.Message,
                 com.tms.collab.messaging.model.MessagingException,
                 kacang.Application,
                 kacang.services.storage.StorageFile,
                 com.tms.collab.messaging.model.Util"%>
<%@include file="includes/taglib.jsp" %>

<c:if test="${!empty param.messageId}" >
    <c:set var="messageId" value="${param.messageId}" />
    <%
        String messageId = (String)pageContext.getAttribute("messageId");
        MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
        Message message = new Message();
	    try {
			message = mm.getMessageByMessageId(messageId);
			String messageBody = message.getBody();
			if (message.getMessageFormat()==1) {
				// plain text message body, replace \n with <br> & empty space with &nbsp;
				messageBody = messageBody.replaceAll("\n","<br>");
				messageBody = messageBody.replaceAll(" ","&nbsp;");
				message.setBody(messageBody);
			}
	    } catch (MessagingException e) {
		    %>
			<script>
				alert('<%=e.getMessage()%>');
			</script>
		    <%
	    }
	    pageContext.setAttribute("message",message);
		//request.setAttribute("message",message);
    %>
</c:if>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Print Email</title>
</head>
  <style>
  .tableBackground {background-color: #999999; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:bold}
  .contentTitleFont {background-color: #FFFFFF; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 10pt; font-weight:bold}
  .contentBgColor {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  .tableHeader {background-color: #CCCCCC; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  .tableRow {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 9pt; font-weight:normal}
  .button { cursor:hand;cursor:pointer;BORDER-RIGHT: #aaaaaa 1pt solid; BORDER-TOP: #DEF2FF 1pt solid; FONT-WEIGHT: normal; FONT-SIZE: 7pt; BORDER-LEFT: #DEF2FF 1pt solid; BORDER-BOTTOM: #aaaaaa 1pt solid; FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif; BACKGROUND-COLOR: #ffffff; TEXT-DECORATION: none}
  </style>
<script>
function Print() {
document.body.offsetHeight;
window.print();
}
</script>
<body onload="Print()">
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td colspan="2" valign="TOP">
            <table width="100%" border="0" cellpadding="2" cellspacing="0">
                <%--subject--%>
                <!-- <tr><td colspan="2" class="tableBackground"><spacer type="block" height="1"></spacer></td></tr> -->
                <tr>
                    <td class="contentBgColor" width="10%" valign="top" nowrap><strong><fmt:message key='messaging.message.date'/>: </strong></td>
                    <td class="contentBgColor" width="90%">
                        <fmt:formatDate pattern="${globalDatetimeLong}" value="${message.date}" />
                    </td>
                </tr>
                <tr>
                    <td class="contentBgColor" valign="top" nowrap><strong><fmt:message key='messaging.message.from'/>: </strong></td>
                    <td class="contentBgColor"><c:out value="${message.from}" /></td>
                </tr>
                <%-- to --%>
                <tr>
                    <td class="contentBgColor" valign="top" nowrap><strong><fmt:message key='messaging.message.to'/>: </strong></td>
                    <td class="contentBgColor">
                        <c:if test="${!empty message.toIntranetList}">
                            <c:out value="${message.toIntranetList}" /><br>
                        </c:if>
                        <c:if test="${!empty message.toList}">
                            <c:out value="${message.toList}" />
                        </c:if>
                    </td>
                </tr>
                <%--cc--%>
                <c:if test="${(!empty message.ccIntranetList) || (!empty message.ccList)}">
                    <tr>
                        <td class="contentBgColor"  valign="top" nowrap><strong><fmt:message key='messaging.message.cc'/>: </strong></td>
                        <td class="contentBgColor">
                            <c:if test="${!empty message.ccIntranetList}">
                                <c:out value="${message.ccIntranetList}" /><br>
                            </c:if>
                            <c:if test="${!empty message.ccList}">
                                <c:out value="${message.ccList}" />
                            </c:if>
                        </td>
                    </tr>
                </c:if>
                <%--display attachments, if any--%>
                <c:if test="${!empty message.storageFileList}">
                    <tr>
                    <td class="contentBgColor"  valign="top" nowrap><strong><fmt:message key='messaging.message.attachments'/>: </strong></td>
                    <td class="contentBgColor">
                        <c:forEach var="storageFile" items="${message.storageFileList}" varStatus="status" >
                            <c:set var="attachmentName" value="${storageFile.name}" scope="page" />
                            <%
                                String attachmentName = (String) pageContext.getAttribute("attachmentName");
                                attachmentName = Util.encodeHex(attachmentName);
                                pageContext.setAttribute("attachmentName", attachmentName);
                            %>
                            <a href="<c:url value="/messaging/downloadAttachment" />?messageId=<c:out value="${message.messageId}" />&index=<c:out value="${status.index}"/>&name=<c:out value="${attachmentName}" />"><c:out value="${storageFile.name}" /></a>
                            <c:if test="${!status.last}" >, </c:if>
                        </c:forEach>
                    </td>
                    </tr>
                </c:if>
                <tr>
                    <td class="contentBgColor" valign="top" nowrap><strong><fmt:message key='messaging.message.subject'/>: </strong></td>
                    <td class="contentBgColor">
                        <c:set var="subject" value="${message.subject}" scope="page" />
                        <%
                            String subject = (String) pageContext.getAttribute("subject");
                            subject = subject==null ? "" : subject.trim();
                            subject = subject.length() == 0 ? null : subject;
                            pageContext.setAttribute("subject", subject);
                        %>
                        <c:out value="${pageScope.subject}" default="(no subject)" />
                        <%-- 
                        <c:if test="${!message.read}"><font color=red>(<fmt:message key='messaging.message.unread'/>)</font></c:if>
                        --%>
                    </td>
                </tr>
                <%--message body--%>
                <tr>
                    <td colspan="2"  class="contentBgColor">
                        <c:choose>
                            <c:when test="${message.messageFormat eq 1}">
                                <%--text/plain--%>
                                <table cellpadding=0 cellspacing=0 width="100%">
                                    <tr>
                                        <td class="tableRow">
                                            <%--<textarea
                                                style="background:#FFFFFF; color:black; border: none"
                                                cols="70"
                                                rows="20"
                                                wrap="virtual"
                                                readonly="true">--%>
                                                <c:out value="${message.body}" escapeXml="false"/>
                                            <%-- </textarea> --%>
                                        </td>
                                    </tr>
                                </table>
                            </c:when>
                            <c:when test="${message.messageFormat == 2}">
                                <%--text/html--%>
                                <table cellpadding=5 cellspacing=0 width="100%" align="center">
                                    <tr>
                                        <td class="tableRow" bgcolor="white">
                                            <c:out value="${message.body}" escapeXml="false" />
                                        </td>
                                    </tr>
                                </table>
                            </c:when>
                            <c:otherwise>
                            <%--unknown formats!--%>
                            <table cellpadding=5 cellspacing=0 width="100%" align="center">
                                <tr>
                                    <td class="contentFont" bgcolor="white">
                                        <fmt:message key='messaging.message.unknownmessageformat'/>
                                    </td>
                                </tr>
                            </table>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <%--display inline attachments for supported extension types--%>
                <c:if test="${!empty message.storageFileList}">
                    <%--first pass, to see if anything to display--%>
                    <c:forEach var="storageFile" items="${message.storageFileList}" varStatus="status" >
                        <c:set var="sf" value="${storageFile}" scope="page" />
                        <%
                            String ext;
                            StorageFile sf = (StorageFile) pageContext.getAttribute("sf");
                            int idx = sf.getName().lastIndexOf(".");
                            if(idx!=-1) {
                                ext = sf.getName().substring(idx).toLowerCase();
                            } else {
                                ext = ".";
                            }
                            pageContext.setAttribute("ext", ext);
                        %>
                        <c:if test="${ext eq '.jpg' || ext eq '.gif'}">
                            <c:set var="showViewer" value="true" />
                        </c:if>
                    </c:forEach>
                    <c:if test="${showViewer}">
                        <tr>
                            <td class="contentBgColor" align="right" valign="top" nowrap>&nbsp;</td>
                            <td class="contentBgColor">
                            <script>
                                function showAttach()
                                {
                                    if(document.getElementById('viewer').style.display=="block")
                                        document.getElementById('viewer').style.display="none";
                                    else
                                        document.getElementById('viewer').style.display="block";
                                }
                            </script>
                            <input type="button" onclick="showAttach()" value="Show/Hide Attachment(s)" class="button"> 
                            <div id="viewer" class="contentFont" style="display:block">
                    </c:if>
                    <%--second pass, show the attachments inline--%>
                    <c:forEach var="storageFile" items="${message.storageFileList}" varStatus="status" >
                        <c:set var="sf" value="${storageFile}" scope="page" />
                        <%
                            String ext;
                            StorageFile sf = (StorageFile) pageContext.getAttribute("sf");
                            int idx = sf.getName().lastIndexOf(".");
                            if(idx!=-1) {
                                ext = sf.getName().substring(idx).toLowerCase();
                            } else {
                                ext = ".";
                            }
                            pageContext.setAttribute("ext", ext);
                        %>
                        <c:if test="${ext eq '.jpg' || ext eq '.gif'}">
                            <c:set var="attachmentName" value="${storageFile.name}" scope="page" />
                            <%
                                String attachmentName = (String) pageContext.getAttribute("attachmentName");
                                attachmentName = Util.encodeHex(attachmentName);
                                pageContext.setAttribute("attachmentName", attachmentName);
                            %>
                            <hr size="1">
                            <fmt:message key='messaging.message.filename'/>: <a href="<c:url value="/messaging/downloadAttachment" />?messageId=<c:out value="${message.messageId}" />&name=<c:out value="${attachmentName}" />"><b><c:out value="${storageFile.name}" /></b></a><br>
                            <img src="<c:url value="/messaging/downloadAttachment" />?messageId=<c:out value="${message.messageId}" />&name=<c:out value="${attachmentName}" />">
                        </c:if>
                    </c:forEach>
                    <c:if test="${showViewer}">
                                </div>
                            </td>
                        </tr>
                    </c:if>
                </c:if>
            </table>
        </td>
    </tr>
    <!-- BUG 2558: old template appears -->
    <!-- td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"-->
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentBgColor">
            <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15">
        </td>
    </tr>
</table>



</body>
</html>
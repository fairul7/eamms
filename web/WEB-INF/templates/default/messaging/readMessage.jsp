<%@ page import="kacang.services.storage.StorageFile,
                 com.tms.collab.messaging.model.Util,
                 com.tms.collab.messaging.model.Folder,
                 java.util.List"%>
<%@ page import="java.net.URLEncoder"%>
<%@include file="/common/header.jsp" %>
<c:set var="w" value="${widget}" />
<c:set var="message" value="${w.message}" />
<c:set var="folder" value="${w.folder}" />
<c:set var="success" value="${w.success}" />
<%
	int stringMaxLength = 150;
%>
<c:if test="${success eq 'Calendar Updated'}">
	<script>
	    alert("<fmt:message key='messaging.label.calendarUpdatedSuccessfully'/>");
		//document.history.back(-1);
	</script>
</c:if>
<c:if test="${success eq 'Error updating calendar'}">
		<script>alert("<fmt:message key='messaging.label.unableToUpdateCalendar'/>") </script>
</c:if>
<c:if test="${success eq 'Attendee status updated'}">
		<script>alert("<fmt:message key='messaging.label.attendeeStatusUpdated'/>") </script>
</c:if>
<c:if test="${success eq 'Unable to update the Attendee status'}">
		<script>alert("<fmt:message key='messaging.label.unableToUpdateTheAttendeeStatus'/>") </script>
</c:if>
<c:if test="${success eq 'Invalid calendar file'}">
		<script>alert("<fmt:message key='messaging.label.invalidCalendarFile'/>") </script>
</c:if>


<script>
    function printPreview(){
        window.open('printMessage.jsp?messageId=<c:out value="${message.id}"/>','printMessage','scrollbars=yes,resizable=yes,width=650,height=480');
    }
    function toggleAddress(id){
    	var ele = document.getElementById(id);
    	var eleDotDot = document.getElementById(id+"DotDot");
    	var eleLink = document.getElementById(id+"Link");
    	if(ele != null && eleDotDot != null && eleLink != null ){
    		if(ele.style.display == 'none'){
    			ele.style.display = '';
    			eleDotDot.style.display = 'none';
    			eleLink.innerHTML = '<fmt:message key='general.label.hide'/>';
    		}
    		else if(ele.style.display == 'block'){
    			ele.style.display = 'none';
    			eleDotDot.style.display = '';
    			eleLink.innerHTML = '<fmt:message key='general.label.show'/>';
    		}else{
    			ele.style.display = 'none';
    			eleDotDot.style.display = '';
    			eleLink.innerHTML = '<fmt:message key='general.label.show'/>';
    		}
    	}
    	return false;
    }
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="MIDDLE">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
            &nbsp;
            <img id="moveIcon" style="position:relative" alt="<fmt:message key='messaging.label.dragToMove'/>" src="<c:url value="/ekms/images/ic_drag.gif"/>" width="10" height="9">
            <c:out value="${folder.name}" /> &gt; <c:out value="${message.truncatedSubject}" />
            </font></b>
        </td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr>
        <td colspan="2" valign="TOP">
            <table width="100%" border="0" cellpadding="5" cellspacing="1">
                <%--message details--%>
                <%-- Encoding issues fix --%>
                <script language="javascript">
                    function popupEncoding()
                    {
                        window.open("<c:url value="/ekms/messaging/readEncoded.jsp"/>?messageId=<c:out value="${message.messageId}"/>&encoding=UTF-8", "messageWindow", "height=300,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
                    }
                </script>
                <tr>
                    <td class="contentBgColor" width="10%" align="right" valign="top" nowrap><strong><fmt:message key='messaging.message.date'/></strong></td>
                    <td class="contentBgColor" width="90%">
                        <table cellpadding="0" cellspacing="0" align="right">
                            <tr>
                                <td>[<a href="<c:url value="/messaging/downloadAttachment" />?action=source&messageId=<c:out value="${message.messageId}" />"><fmt:message key='messaging.message.downloadEmail'/></a>]</td>
                                <td>&nbsp;</td>
                                <td>[<a href="" onClick="popupEncoding(); return false;"><fmt:message key='messaging.message.applyDifferentEncoding'/></a>]</td>
                            </tr>
                        </table>
                        <fmt:formatDate pattern="${globalDatetimeLong}" value="${message.date}" />
                    </td>
                </tr>
                <tr>
                    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='messaging.message.from'/></strong></td>
                    <td class="contentBgColor"><c:out value="${message.from}" /></td>
                </tr>
                <%--to--%>
                <tr>
                    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='messaging.message.to'/></strong></td>
                    <td class="contentBgColor">
                        <c:if test="${!empty message.toIntranetList}">
                        	<c:set var="toIntranetList" value="${message.toIntranetList}"/>
                        	<%
                        		List toIntranetList = (List)pageContext.getAttribute("toIntranetList");
                        		StringBuffer intranetStr = new StringBuffer(toIntranetList.toString());
                        		StringBuffer intranetStr2 = new StringBuffer();
                        		if(intranetStr.length() > stringMaxLength){
                        			intranetStr2 = new StringBuffer(intranetStr.substring(stringMaxLength, intranetStr.length()));
                        			intranetStr = new StringBuffer(intranetStr.substring(0, stringMaxLength));
                        		}
                        		pageContext.setAttribute("toIntranetList", intranetStr.toString());
                        		pageContext.setAttribute("toIntranetList2", intranetStr2.toString());
                        	%>
                            <c:out value="${toIntranetList}" /><c:if test="${! empty toIntranetList2}"><span id='toIntranetList' style='display:none;'><c:out value="${toIntranetList2}"/></span>
                            <span id='toIntranetListDotDot'>...]</span>
                            <a id='toIntranetListLink' href='#' onclick='toggleAddress("toIntranetList")'><fmt:message key="general.label.show"/></a>
                            </c:if>
                            <br>
                            <%-- <c:out value="${message.toIntranetList}" /><br/> --%>
                        </c:if>
                        <c:if test="${!empty message.toList}">
                        	<c:set var="toList" value="${message.toList}"/>
                        	<%
                        		List toList = (List)pageContext.getAttribute("toList");
                        		StringBuffer toStr = new StringBuffer(toList.toString());
                        		StringBuffer toStr2 = new StringBuffer();
                        		if(toStr.length() > stringMaxLength){
                        			toStr2 = new StringBuffer(toStr.substring(stringMaxLength, toStr.length()));
                        			toStr = new StringBuffer(toStr.substring(0, stringMaxLength));
                        		}
                        		pageContext.setAttribute("toList", toStr.toString());
                        		pageContext.setAttribute("toList2", toStr2.toString());
                        	%>
                            	<c:out value="${toList}"/><c:if test="${! empty toList2}"><span id='toList' style='display:none;'><c:out value="${toList2}"/></span>
                            	<span id='toListDotDot'>...]</span>
                            	<a id='toListLink' href='#' onclick='toggleAddress("toList")'><fmt:message key="general.label.show"/></a>
                            	</c:if>
                            	<%--<br/><c:out value="${message.toList}" />--%>
                        </c:if>
                    </td>
                </tr>
                <%--cc--%>
                <c:if test="${(!empty message.ccIntranetList) || (!empty message.ccList)}">
                    <tr>
                        <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='messaging.message.cc'/></strong></td>
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
                <%--subject--%>
                <tr>
                    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='messaging.message.subject'/></strong></td>
                    <td class="contentBgColor">
                        <c:set var="subject" value="${message.subject}" scope="page" />
                        <%
                            String subject = (String) pageContext.getAttribute("subject");
                            subject = subject==null ? "" : subject.trim();
                            subject = subject.length() == 0 ? null : subject;
                            pageContext.setAttribute("subject", subject);
                        %>
                        <c:out value="${pageScope.subject}" default="(no subject)" />
                        <c:if test="${!message.read}"><font color=red>(<fmt:message key='messaging.message.unread'/>)</font></c:if>
                    </td>
                </tr>
                <%--display attachments, if any--%>
                <c:if test="${!empty message.storageFileList}">
                    <tr>
                    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='messaging.message.attachments'/></strong></td>
                    <td class="contentBgColor">
                        <c:forEach var="storageFile" items="${message.storageFileList}" varStatus="status" >
                            <c:set var="attachmentName" value="${storageFile.name}" scope="page" />
                            <%
                                String attachmentName = (String) pageContext.getAttribute("attachmentName");
                                //attachmentName = URLEncoder.encode(attachmentName , "UTF-8");
                                //attachmentName = attachmentName.replaceAll("\\+", "%20");  
                            	attachmentName = Util.encodeHex(attachmentName);
                                pageContext.setAttribute("attachmentName", attachmentName);
                            %>
                            <a href="<c:url value="/messaging/downloadAttachment" />?messageId=<c:out value="${message.messageId}" />&index=<c:out value="${status.index}"/>&name=<c:out value="${attachmentName}" />"><c:out value="${storageFile.name}" /></a>
                            <c:if test="${!status.last}" >, </c:if>
                        </c:forEach>
                    </td>
                    </tr>
                </c:if>
                <%--display buttons--%>
                <tr>
                    <td class="contentBgColor" align="right" valign="top" nowrap>&nbsp;</td>
                    <td class="contentBgColor">
                        <% request.setAttribute("draftName", Folder.FOLDER_DRAFT); %>
                        <c:if test="${folder.specialFolder && folder.name eq draftName}">
                            <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.edit'/>" onclick="location='editDraftMessage.jsp?messageId=<c:out value="${message.id}" />'">
                        </c:if>
                        <c:if test="${!(folder.specialFolder && folder.name eq draftName)}">
                            <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.reply'/>" onclick="location='replyMessage.jsp?messageId=<c:out value="${message.id}" />&replyAll=0'">
                            <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.replyAll'/>" onclick="location='replyMessage.jsp?messageId=<c:out value="${message.id}" />&replyAll=1'">
                            <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.forward'/>" onclick="location='forwardMessage.jsp?messageId=<c:out value="${message.id}" />'">
                        </c:if>
                        <!-- start changes BUG 3095, 3120 -->
                        <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.delete'/>" onclick="location='deleteMessage.jsp?messageId=<c:out value="${message.id}" /><c:if test="${!empty w.nextMessage}">&nextMessageId=<c:out value="${w.nextMessage.messageId}"/>&index=<c:out value="${w.nextMessageIndex}"/></c:if>'">
                        <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.markReadUnread'/>" onclick="location='readMessage.jsp?toggleRead=1&messageId=<c:out value="${message.id}" />&index=<c:out value="${param.index}"/>'">
                        <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.saveContacts'/>" onclick="location='saveContacts.jsp?messageId=<c:out value="${message.id}" /><c:if test="${!empty w.nextMessage}">&index=<c:out value="${param.index}"/></c:if>'">
                        <!-- end changes BUG 3095, 3120 -->
                        <c:set var="currentMsgIndex" value="${param.index}" />
                        <c:if test="${!(empty w.previousMessage)}">
                            <c:set var="previousMsgIndex" value="${w.previousMessageIndex}" />
                            <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.previous'/>" onclick="location='readMessage.jsp?messageId=<c:out value="${w.previousMessage.messageId}" />&index=<c:out value="${previousMsgIndex}"/>'">
                        </c:if>
                        <c:if test="${!(empty w.nextMessage)}">
                            <c:set var="nextMsgIndex" value="${w.nextMessageIndex}" />
                            <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.next'/>" onclick="location='readMessage.jsp?messageId=<c:out value="${w.nextMessage.messageId}" />&index=<c:out value="${nextMsgIndex}"/>'">
                        </c:if>
                        <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.printButton'/>" onclick="javascript:printPreview()">
                        <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.cancel'/>" onclick="location='messageList.jsp'">
                        <%  if(request.getAttribute("ICSAttachment").equals("true")){ %>
                        <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.updateCalendar'/>" onclick="location='readMessage.jsp?icsupdate=true&messageId=<c:out value="${message.id}" /><c:if test="${!empty w.nextMessage}">&index=<c:out value="${param.index}"/></c:if>'">
                        <%} %>
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
                                        <td align="center">
                                            <textarea
                                                style="background:#FFFFFF; color:black; border: none"
                                                cols="70"
                                                rows="20"
                                                wrap="virtual"
                                                readonly="true"><c:out value="${message.body}" escapeXml="true"/>
                                            </textarea>
                                        </td>
                                    </tr>
                                </table>
                            </c:when>
                            <c:when test="${message.messageFormat == 2}">
                                <%--text/html--%>
                                <table cellpadding=5 cellspacing=0 width="100%" align="center">
                                    <tr>
                                        <td class="contentFont" bgcolor="white">
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
                            <div id="viewer" class="contentFont" style="display:none">
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
                <%--display buttons--%>
                <tr>
                    <td class="contentBgColor" align="right" valign="top" nowrap>&nbsp;</td>
                    <td class="contentBgColor">
                        <% request.setAttribute("draftName", Folder.FOLDER_DRAFT); %>
                        <c:if test="${folder.specialFolder && folder.name eq draftName}">
                            <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.edit'/>" onclick="location='editDraftMessage.jsp?messageId=<c:out value="${message.id}" />'">
                        </c:if>
                        <c:if test="${!(folder.specialFolder && folder.name eq draftName)}">
                            <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.reply'/>" onclick="location='replyMessage.jsp?messageId=<c:out value="${message.id}" />&replyAll=0'">
                            <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.replyAll'/>" onclick="location='replyMessage.jsp?messageId=<c:out value="${message.id}" />&replyAll=1'">
                            <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.forward'/>" onclick="location='forwardMessage.jsp?messageId=<c:out value="${message.id}" />'">
                        </c:if>
                        <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.delete'/>" onclick="location='deleteMessage.jsp?messageId=<c:out value="${message.id}" />'">
                        <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.markReadUnread'/>" onclick="location='readMessage.jsp?toggleRead=1&messageId=<c:out value="${message.id}" />&index=<c:out value="${param.index}"/>'">
                        <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.saveContacts'/>" onclick="location='saveContacts.jsp?messageId=<c:out value="${message.id}" />'">
                        <c:set var="currentMsgIndex" value="${param.index}" />
                        <c:if test="${!(empty w.previousMessage)}">
                            <c:set var="previousMsgIndex" value="${w.nextMessageIndex}" />
                            <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.previous'/>" onclick="location='readMessage.jsp?messageId=<c:out value="${w.previousMessage.messageId}" />&index=<c:out value="${previousMsgIndex}"/>'">
                        </c:if>
                        <c:if test="${!(empty w.nextMessage)}">
                            <c:set var="nextMsgIndex" value="${w.previousMessageIndex}" />
                            <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.next'/>" onclick="location='readMessage.jsp?messageId=<c:out value="${w.nextMessage.messageId}" />&index=<c:out value="${nextMsgIndex}"/>'">
                        </c:if>
                        <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.printButton'/>" onclick="javascript:printPreview()">
                        <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.cancel'/>" onclick="location='messageList.jsp'">
                        <%  if(request.getAttribute("ICSAttachment").equals("true")){ %>
                        <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.updateCalendar'/>" onclick="location='readMessage.jsp?icsupdate=true&messageId=<c:out value="${message.id}" /><c:if test="${!empty w.nextMessage}">&index=<c:out value="${param.index}"/></c:if>'">
                        <%} %>
                    </td>
                </tr>
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

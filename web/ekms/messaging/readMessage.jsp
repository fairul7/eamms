<%@ page import="com.tms.collab.messaging.model.MessagingModule,
                 com.tms.collab.messaging.model.Message,
                 com.tms.collab.messaging.model.MessagingException,
                 com.tms.collab.messaging.ui.MessageTable"%>
<%@include file="includes/taglib.jsp" %>


<%
	/* Redirecting after drag and drop */
	if(MessageTable.EVENT_DRAG_DROP.equals(request.getParameter("et")))
	{
%>
	<script>
		document.location="readMessage.jsp?cn=mFolderTree.tree&folder=<%= request.getParameter("originalFolder") %>";
	</script>
<%
	}
%>

<c:if test="${!empty param.messageId}" >
    <c:set var="messageId" value="${param.messageId}" />
    <%
		String userId = kacang.ui.WidgetManager.getWidgetManager(request).getUser().getId();
        String messageId = (String)pageContext.getAttribute("messageId");
        MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
	    try {
			Message message = mm.getMessageByMessageId(messageId);
		%>
	    	<c:if test="${param.icsupdate eq 'true'}">
	    		<% mm.parseICSFile(messageId,message,userId); %> 
	    	</c:if>
    	<%
    	mm.checkICSFile(message);
    	if(message.hasICSAttachment()){
				request.setAttribute("ICSAttachment","true");
			}else{
				request.setAttribute("ICSAttachment","false");			
			}
            if(request.getParameter("toggleRead")==null){
                if(message!=null && !message.isRead()){
                    message.setRead(true);
                    mm.updateMessage(message);
                }
            }
	    } catch (MessagingException e) {
		    %>
			<script>
				alert('<%=e.getMessage()%>');
				document.location = "messageList.jsp";
			</script>
		    <%
	    }
    %>
</c:if>

<%-- // get the MessageTable widget, this is for the next/prev button --%>
<c:set var="listingWidget" scope="request" value="${widgets['messageListPage.table']}"/>

<%@include file="includes/header.jsp" %>


 <x:template type="com.tms.collab.messaging.ui.ReadMessage" name="readWidget"/>

 <c:if test="${notfound}" >
    <script>
        document.location = "messageList.jsp";
    </script>
 </c:if>
<%@include file="includes/footer.jsp" %>

<script type="text/javascript" src="<c:url value="/common/dragndrop/wz_dragdrop.js"/>"></script>

<c:set var="formCn" value="messageListPage.table"/>
<c:set var="checkboxCn" value="messageListPage.table.sel"/>
<form name="readMsg.dragDropForm" method="POST">
<input type="hidden" name="cn" value="<c:out value="${formCn}"/>">
<input type="hidden" name="et" value="<%= MessageTable.EVENT_DRAG_DROP %>">
<input type="hidden" name="targetFolder" value="">
<input type="hidden" name="id" value="">
<input type="hidden" name="originalFolder" value="<c:out value="${readWidget.folder.folderId}"/>">
</form>
<script type="text/javascript">
<!--
    SET_DHTML(CURSOR_HAND<c:out value="${dropFolderList}" escapeXml="false" />,"moveIcon"+TRANSPARENT);

    function my_DropFunc() {
        try {
            // find target
            for (i=0; i<dd.elements.length; i++) {
                var el = dd.elements[i];
                var folder = el.name;
                if (folder.indexOf("folder_") == 0) {
                    if (my_DropFunc_InTarget(dd.obj, el)) {
                        if (confirm("<fmt:message key='messaging.label.moveselectedmessagestoselectedfolder'/>")) {
                            var dragDropForm = document.forms['readMsg.dragDropForm'];
                            dragDropForm.elements['targetFolder'].value = folder;
                            dragDropForm.elements['id'].value = '<c:out value="${messageId}"/>';
                            dragDropForm.submit();
                        }
                        break;
                    }
                }
            }
        }
        catch (e) {
        }
        // move source back to original position
        dd.obj.moveTo(dd.obj.defx, dd.obj.defy);
    }

    function my_DropFunc_InTarget(src, target) {
    //    alert (src.x + ", " + src.y + ", " + target.x + ", " + target.y + ", " + (target.x + target.w) + ", " + (target.y + target.h));
        return (src.x >= target.x && src.x <= (target.x + target.w) && src.y >= target.y && src.y <= (target.y + target.h));
    }

//-->
</script>

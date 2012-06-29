<%@ page import="com.tms.collab.messaging.model.MessagingModule,
                 com.tms.collab.messaging.model.Message,
                 com.tms.collab.messaging.model.MessagingException"%>
<%@include file="includes/taglib.jsp" %>
<c:if test="${!empty param.messageId}" >
    <c:set var="messageId" value="${param.messageId}" />
    <%
        String messageId = (String)pageContext.getAttribute("messageId");
        MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
	    try {
			Message message = mm.getMessageByMessageId(messageId);
			if(message!=null){
				message.setRead(true);
				mm.updateMessage(message);
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



 <x:template type="com.tms.collab.messaging.ui.ReadMessage" />
 <c:if test="${notfound}" >
    <script>
        document.location = "messageList.jsp";
    </script>
 </c:if>
<%@include file="includes/footer.jsp" %>
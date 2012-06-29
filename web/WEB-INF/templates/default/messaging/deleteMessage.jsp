<%@include file="/common/header.jsp" %>
<c:set var="w" value="${widget}" />
<c:if test="${w.deleteMessageSuccessful}">
    <c:if test="${w.movedToTrash}">
        <script>
            alert("Message has been moved to Trash folder.");
			<c:choose>
				<%-- BUG 3095 --%>
				<c:when test="${(!empty w.nextMessage) && (!empty w.index)}">
                	document.location="<c:url value="/ekms/messaging/readMessage.jsp?messageId=${w.nextMessage}&index=${w.index}"/>";	
				</c:when>
				
				<c:otherwise>
					document.location="<c:url value="/ekms/messaging/messageList.jsp"/>";
				</c:otherwise>
			</c:choose>
        </script>
    </c:if>
    <c:if test="${!w.movedToTrash}">
        <script>
            alert("Message has been deleted from Trash folder.");
            document.location="<c:url value="/ekms/messaging/messageList.jsp"/>";
        </script>
    </c:if>
</c:if>
<c:if test="${!w.deleteMessageSuccessful}">
    <script>
        alert("Error occurred while deleting message.");
        document.location="<c:url value="/ekms/messaging/messageList.jsp"/>";
    </script>
</c:if>



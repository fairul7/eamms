<c:if test="${empty sessionScope.currentUser || sessionScope.currentUser.id == 'anonymous'}">
    <jsp:forward page="login.jsp" />
</c:if>

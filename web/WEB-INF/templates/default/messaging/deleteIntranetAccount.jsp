<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />

<c:choose>
    <c:when test="${w.action eq 'showForm'}">
        <form method="post">
            <fmt:message key='messaging.message.deleteAccountMessage'/>
            <i><c:out value="${sessionScope.currentUser.username}" /></i>.
           <fmt:message key='messaging.message.thisactionisnotreversible'/> .
            <br><br>
            <fmt:message key='messaging.message.proceedMessage'/>:<br><br>
            <blockquote>
                <input type="text" name="confirm" size="40">
                <br>
                <input type="submit" class="buttonClass" value="Delete My Messaging Data">
            </blockquote>
        </form>
    </c:when>
    <c:when test="${w.action eq 'deleted'}">
        <fmt:message key='messaging.message.accountDeletedMessage'/>.
        <br><br>
        [<a href="activate.jsp"><fmt:message key='messaging.message.continue'/></a>]
    </c:when>
    <c:otherwise>
      <fmt:message key='messaging.message.invalidaction'/> !
    </c:otherwise> 
</c:choose>

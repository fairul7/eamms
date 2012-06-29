<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="kacang.tld" prefix="x" %>



<c:set var="poll"  value="${widget}"/>

<table border=0 width="100%">
<tr width="100%"><td width="100%">
<c:if test="${poll.action eq 'view'}" >
<x:display name="${poll.questionTable.absoluteName}"/>
</c:if>
<c:if test="${poll.action eq 'edit'}" >
<x:display name="${poll.editQuestionView.absoluteName}"/>
</c:if>
<%--<c:otherwise>
<x:display name="${poll.editQuestionView.absoluteName}"/>

</c:otherwise>--%>
        </td></tr>

<%--<c:forEach var="question" items="${poll.questions}">
<tr>
     <td>
        <c:out value="${question.question}"/>
     </td>
</tr>

</c:forEach>--%>


</table>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%--
<%@ taglib uri="kacang.tld" prefix="x" %>
--%>

<c:set var="vote" value="${widget}"/>

<c:if test="${vote.question!=null && vote.question.active}">

<%--
<c:if test="${!vote.showImage}">
<table width="100%" border="0" cellpadding="3" cellspacing="1" align="center">
  <tr>
    <td height="19" class="sideheading">Vote</td>
  </tr>
</table>
</c:if>
--%>

<form method="GET" action="">
    <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="90%">
      <tr>
         <td>
            <c:if test="${vote.showImage == false && vote.images == true}">
                <c:out value="${vote.question.title}" escapeXml="false" />
            </c:if>
            <c:if test="${vote.showImage == true || vote.images == false}">
                <c:out value="${vote.question.question}" escapeXml="false" />
            </c:if>
         </td>
       </tr>
       <tr> <td><hr></td> </tr>
        <c:if test="${vote.showImage == true || vote.images == false}">
        <c:forEach items="${vote.answers}" var="answer">
           <tr>
            <td>
                <input TYPE="radio" value=<c:out value="${answer.id}"/> name="votechoice">
                <span class="voteAnswer"><c:out value="${answer.answer}"/></span>
            </td>
            </tr>
        </c:forEach>
       <tr> <td><hr></td> </tr>
        </c:if>
       <tr>
          <input type="hidden" name="VoteID" value="<c:out value="${vote.question.id}"/>"/>
          <c:forEach items="${pageContext.request.parameterNames}" var="paramName">
              <input type="hidden" name="<c:out value='${paramName}'/>" value="<c:out value='${param[paramName]}'/>"/>
          </c:forEach>
         <td colspan="2" align="center">
         <input name="VOTE" type="submit" class="voteButton" value="<fmt:message key='vote.label.voteButton'/>">
            <c:if test="${vote.showImage == true || vote.images == false}">
              <input type="submit" class="voteButton" name="Result" value="<fmt:message key='vote.label.resultButton'/>"></td>
            </c:if>
         </td>

       </tr>
    </table>
</form>
</c:if>
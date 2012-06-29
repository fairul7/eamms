<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="voteresult" value="${widget}"/>

<table border=0 width="100%">
    <c:forEach items="${voteresult.question.answers}" var="answer">
        <tr >
             <td height="45">
                <span class="voteAnswer"><c:out value="${answer.answer}"/></span>
                &nbsp;
                <span class="voteCount">(<c:out value="${answer.total}"/> vote<c:if test="${answer.total != 1}">s</c:if>)</span>
                <br>
             <table  border="0" cellpadding="0" cellspacing="0" width="100%">
              <tr>
              <td width="<fmt:formatNumber value="${answer.percentage}"minFractionDigits="0" maxFractionDigits="0" />%">

              <table bgcolor="red" border="0" cellpadding="0" cellspacing="0" width="100%">
                  <TR><TD align="right" height="15"></TD>
                  </TR>
                </table>
              </td>
              <td>&nbsp</td>
              <td>
                <table  border="0" cellpadding="0" cellspacing="0"  >
                  <tr>
                  <td allign="left">
                    <b><span class="voteResult">
                        <fmt:formatNumber value="${answer.percentage}"minFractionDigits="1" maxFractionDigits="1" />%</span></b>
                    </span>
                  </td>
                  </tr>
                 </table>
              </td>
              </tr>
             </table>
           </td>
        </tr>
    </c:forEach>
     <tr>
        <td><span class="voteTotal"><fmt:message key='vote.label.totalVotes'/>: <c:out value="${voteresult.question.total}"/></span></td>
     </tr>

</table>
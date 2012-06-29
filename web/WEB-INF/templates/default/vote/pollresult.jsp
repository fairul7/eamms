<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="vote" value="${widget}"/>

<%--
<c:if test="${!vote.showImage}">
<table width="100%" border="0" cellpadding="3" cellspacing="1" align="center">
  <tr>
    <td height="19" class="sideheading">Vote</td>
  </tr>
</table>
</c:if>
--%>

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
    <tr>
         <td>
           <hr>
         </td>
    </tr>
    <tr>
    <td>
    <x:template type="com.tms.collab.vote.ui.VoteResult"
                name="${vote.question.title}"
                scope="request"
                properties="questionID=${vote.question.id}"/>
    </td>
    </tr>
</table>
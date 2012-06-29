<%@ page import="com.tms.collab.vote.ui.PollAnswerView,
                 com.tms.collab.vote.model.Answer"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<%--<c-rt:set var="addConstant" value="${vote.ui.PollAnswerView.POLL_ANSWER_ADD}"/>
<c-rt:set var="editConstant" value="<%=vote.ui.PollAnswerView.POLL_ANSWER_EDIT%>"/>--%>
<c:set var="pollanswer" value="${widget}"/>
<jsp:include page="/WEB-INF/templates/default/form_header.jsp"/>
<table border="0" cellpadding="2" cellspacing="1" width="100%">
  <thead> <tr width="100%" class="tableHeader" >
        <td width="10%">
          <span ><fmt:message key='general.label.number'/></span>
        </td>
        <td width="50%">
          <span ><fmt:message key='vote.label.answer'/> </span>
        </td>
        <td width="20%">
          <span ><fmt:message key='vote.label.action'/>  </span>
        </td>
        <td>
          <span ><fmt:message key='vote.label.order'/>  </span>
       </td>
   </tr>
  </thead>

   <% int i=1;%>
   <c:forEach items="${pollanswer.answers}" var="answer">

   <% if(i%2 == 0)
     { %>
         <c:set var="style" value="background: #dddddd"/>
     <%} else
   {%>
     <c:set var="style" value="background: white"/>
     <%}%>



   <%-- tr style="<c:out value="${style}"/>"> --%>
   <tr  class="tableRow">
       <td><%--
            <c:out value="${i}"/>--%>     <%=i%>
       </td>
       <td>
            <c:out value="${answer.answer}"/>
       </td>
       <td>
       <x:event name="${pollanswer.absoluteName}"
                     type="edit"
                     param="id=${answer.id}" >
            <fmt:message key='forum.label.edit'/>
       </x:event>
       |
       <x:event name="${pollanswer.absoluteName}"
                type="delete"
                param="id=${answer.id}">
            <fmt:message key='general.label.delete'/>
       </x:event>
       </td>
       <td>

       <c:if test="${answer.piority>1}">
        <x:event name="${pollanswer.absoluteName}"
                 type="up"
                 param="pior=${answer.piority}">

            <fmt:message key='vote.label.up'/>

        </x:event>
        </c:if>
        <c:if test="${answer.piority<=1}">
        <fmt:message key='vote.label.up'/>   </c:if>
        |
       <%if(((Answer)pageContext.getAttribute("answer")).getPiority()<((PollAnswerView)pageContext.getAttribute("pollanswer")).getAnswers().length )
       {%>

        <x:event name="${pollanswer.absoluteName}"
                 type="down"
                 param="pior=${answer.piority}">
           <fmt:message key='vote.label.down'/>
        </x:event>
       <%}else
       {%> <fmt:message key='vote.label.down'/>
       <%}%>
       </td>
   </tr>
    <% i++;%>
   </c:forEach>

</table>


 <table border=0>
   <tr>
   <c:if test="${pollanswer.answers == null}" >
         <td> <fmt:message key='vote.message.pleaseAddAnswers'/></td>
        </td></tr>
    <tr>
   </c:if>
   <td width="100%">
<%-- <c:out value="${addConstant}"/>--%>
   <br>
 <c:if test="${pollanswer.action eq 'Add'}">
   <x:display name="${pollanswer.answerTextBox.absoluteName}"/>
    </td><tr><td>
    <font size=1><fmt:message key='vote.message.separateEachAnswer'/></font></td><Tr><td>
 </c:if>
 <c:if test="${pollanswer.action eq 'update'}">
  <x:display name="${pollanswer.answerTextField.absoluteName}"/>


  </c:if>
   <x:display name="${pollanswer.actionButton.absoluteName}"/>

   </td>
   </tr>
 </table>

 <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
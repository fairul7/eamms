<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="poll" value="${widget}"/>
<%-- 
<jsp:include page="/WEB-INF/templates/default/form_header.jsp"/>
<table border=0 width="100%">
 <tr>
    <td>

    </td>
 </tr>
 <tr>
    <td width="100%">
      <table border=0 width="100%">
--%>      
      
      
      <table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>

<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
	<tr>
		<td>
                        <jsp:include page="../form_header.jsp" flush="true"></jsp:include>
			<table width="100%" cellpadding="3" cellspacing="1">
      
      
      
      
        <tr>
          <td class="classRowLabel" width="20%" align="right">
              <fmt:message key='general.label.title'/> *
          </td>
          <td class="classRow">
               <x:display name="${poll.titleTextField.absoluteName}"/>
          </td>
        </tr>
        <tr valign="top">
            <td class="classRowLabel" align="right" >
                <fmt:message key='vote.label.question'/> *
            </td>
            <td class="classRow">
            <x:display name="${poll.questionTextField.absoluteName}"/>
            </td>
        </tr>
        <tr valign="top">
            <td class="classRowLabel" align="right">
                <fmt:message key='general.label.status'/>
            </td>
            <td class="classRow">
                <x:display name="${poll.pendingCB.absoluteName}"/>    <br>
                <x:display name="${poll.activeCB.absoluteName}"/>     <br>
                <x:display name="${poll.publicCB.absoluteName}"/>
            </td>
        </tr>
        <tr>
            <td class="classRowLabel" align="right">
                <fmt:message key='vote.label.assignedTo'/>
            </td>
            <td class="classRow">
                <x:display name="${poll.assignedSelectBox.absoluteName}" ></x:display>
            </td>
        </tr>
      <tr>
      <td class="classRow"></td>
    <td class="classRow" align="left">
     <br>
       <x:display name="${poll.updateButton.absoluteName}"/>

    </td>
 </tr>
 </table>
 </td>
 </tr>

<%-- 
 <tr>
   <td>
     <hr>
   </td>
 </tr>
 --%>

 <tr>
  <td>
  <table width ="100%" cellspacing="1" cellpadding="0">
   <tr>
    <td class="classRowLabel" width="20%" align="right" valign="top">
        <fmt:message key='vote.label.answer'/>
     </Td >
    <td class="classRow">
     <x:display name="${poll.answerView.absoluteName}" />

    </td>
  </tr>
  </table>

  </td>
  </tr>

<%-- 
  <tr>
     <td>
       <hr>
     </td>
   </tr>
--%>

  <tr>
  <td>
    <table width="100%" cellpadding="0" cellspacing="1">
    <tr>
     <td class="classRowLabel" width="20%" valign="top" align="right">
        <fmt:message key='vote.label.result'/>
     </td>
     <td align = "left" class="classRow">
                    <x:template type="com.tms.collab.vote.ui.VoteResult"
                                name="voteresult"
                                properties="questionID=${poll.selectedQuestion.id}"/>
        <br> <x:display name="${poll.clearResultButton.absoluteName}"/>
     </td>
    </tr>
   </table>
   </td>
   </tr>
<%-- 
   <tr>
     <td>
       <hr>
     </td>
   </tr>
--%>
<%-- 
   <tr>
   <td>
    <table width="100%">
    <tr>
     <td width="20%">  </td>
    <td >
       <x:display name="${poll.cancelButton.absoluteName}"/>

    </td>
 </tr>
 --%>
 
 
 
 <jsp:include flush="true" page="../form_footer.jsp"></jsp:include>
			</table>
			
		</td>
	</tr>
</table>

</td>
</tr>	
</table>
 
 
 
 
 
 <%-- 
</table>
</td>
</tr></table>
<jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
--%>



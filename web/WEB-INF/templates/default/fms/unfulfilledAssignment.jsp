<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>



<c:set var="form" value="${widget}"/>
<c:set var="request" value="${widget.reQ}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >


	<tr>
        <td  class="classRowLabel" width="33%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestTitle'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">     
        	<c:out value="${request.requestTitle}"/>                 
        </td>
    </tr>
    
    <tr>
        <td  class="classRowLabel" width="33%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.requestProgram'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">            
            <c:out value="${request.program}"/>     
        </td>
    </tr>
    
     <tr>
        <td  class="classRowLabel" width="33%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.transport.assDateTime'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">
           <c:set var="sDate"><fmt:formatDate pattern="d MMM yyyy" value="${request.startDate}" /></c:set>	
           <c:set var="eDate"><fmt:formatDate pattern="d MMM yyyy" value="${request.endDate}" /></c:set>
           <c:set var="sTime"><fmt:formatDate pattern="hh:mm:a" value="${request.startDate}" /></c:set>
           <c:set var="eTime"><fmt:formatDate pattern="hh:mm:a" value="${request.endDate}" /></c:set>
           							
           	<c:out value="${sDate}"/> to <c:out value="${eDate}"/> <br>      
           
        	<c:out value="${sTime}"/> - <c:out value="${eTime}"/>     
            
        </td>
    </tr>
    
     <tr>
        <td  class="classRowLabel" width="33%" height="20" valign="top" align="right"  valign="top"><fmt:message key='fms.label.fulfilled'/>&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">            
            <x:display name="${form.reasonTB.absoluteName}"/>     
        </td>
    </tr>
    

	<tr>
        <td  class="classRowLabel" width="33%" height="20" valign="top" align="right"  valign="top">&nbsp; <FONT class="classRowLabel"></FONT></td>
        <td class="classRow">            
            <x:display name="${form.rejectButton.absoluteName}" />
     		<x:display name="${form.cancelButton.absoluteName}" /> 
        </td>
    </tr>




</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
<jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>
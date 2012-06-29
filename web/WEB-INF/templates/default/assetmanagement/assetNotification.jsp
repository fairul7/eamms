<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>

<c:if test="${!empty param.notification}">
   <c:set var="notification" value="${param.notification}"/>
   <x:set name="assetNotification.form" property="strNotification" value="${param.notification}"></x:set>
</c:if>  

<c:if test="${empty param.notification}">
	<c:set var="notification" value="false"/>
</c:if> 
 
 <c:if test="${forward.name=='addNotification'}" >
</c:if>

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
  <tr>
    <td>
    
  	  <table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
  	    <tr>
  	      <td>	 	      
  	          <jsp:include page="../form_header.jsp" flush="true"/>
  	          <%
  	        	String notification = (String)pageContext.getAttribute("notification");
  				if ("true".equals(notification)){
   				%>	<table width="100%" cellpadding="3" cellspacing="1" bgColor="#F0F8FF">	
	   				<tr>
	   				<td colspan="2" bgcolor="#003366" class="contentTitleFont" align="left" valign="top"><fmt:message key='asset.label.btnNotification'/></td>
	   				</tr>	   							  			
   				  	   <tr height="50">
	  	          		<td class="classRowLabel"  align="right"><fmt:message key='asset.label.numberOfnotification'/>:&nbsp;&nbsp;</td>
	  	          		<td  class="classRowLabel"><x:display name="${form.txtfdNotification.absoluteName}"/> &nbsp; &nbsp;
	  	          		<input class=button type=button name="notification" value="<fmt:message key='asset.label.btnNotification'/>" onClick="setNumbOfNotification()">&nbsp; &nbsp;
	  	          		<input class=button type=button name="cancel" value="<fmt:message key='asset.label.btnCancel'/>" onClick="window.close()">
	  	          		</td> 
	  	          	</tr> 
	  	          	
	  	          	<tr>
					<td class="classRowLabel" colspan="2" align="left"><fmt:message key='asset.message.noteNotification'/></td>
					</tr>		
  	    
				</table>
				<BR>
				<BR>
  	          		 <%}%>
  	          		 	          		 
  	          		 <table width="100%" cellpadding="3" cellspacing="1" bgColor="#F0F8FF">
  	          		 <tr height="5">
	   				<td colspan="2" bgcolor="#003366" class="contentTitleFont" align="left" valign="top"><fmt:message key='asset.label.setupNotification'/></td>
	   				</tr>
	   						
					<tr  height="5">
  	          		  <td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.title'/>:&nbsp;&nbsp;</td>
  	          		  <td class="classRow"><x:display name="${form.txtfdTitle.absoluteName}"/></td>           
  	          		</tr>  	          		
  	          		
  	          		<tr>
  	          		  <td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.recipient'/>:&nbsp;&nbsp;</td>
  	          		  <td class="classRow"><x:display name="${form.userSelect.absoluteName}"/></td>           
  	          		</tr>  
  	          			
  	          		<tr>
  	          		  <td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.notifyMethod'/>:&nbsp;&nbsp;</td>
  	          		  <td class="classRow"><x:display name="${form.cbxMemo.absoluteName}"/><fmt:message key='asset.label.memo'/>&nbsp;&nbsp;
  	          		  <x:display name="${form.cbxEmail.absoluteName}"/><fmt:message key='asset.label.email'/></td>           
  	          		</tr>  
  	          		
				<c:forEach items="${widget.timeNotification}" var="timeField" varStatus = "status">
					<c:set var="dateField" value="${widget.dateNotification[status.index]}"/>
					<c:set var="msg" value="${widget.txtbxMsg[status.index]}"/>
					
					<tr class="classRowLabel">
						<td colspan="2"><HR></td>
					</tr>
					<tr>
					<td class="classRowLabel" ></td>
					<td class="classRowLabel" valign="top" align="left"><fmt:message key='asset.label.notification'/> <c:out value="${count + 1}"/></td>
					<tr><td class="classRowLabel" valign="top" align="right" >
							<fmt:message key='asset.label.date'/>:</td><td class="classRow"><x:display name="${dateField.absoluteName}" />&nbsp;&nbsp;</td></tr>
					<tr><td class="classRowLabel" valign="top" align="right"> <fmt:message key='asset.label.time'/>:</td><td class="classRow"><x:display name="${timeField.absoluteName}" />&nbsp;&nbsp;</td></tr>
					<tr><td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.message'/>:</td><td class="classRow"><x:display name="${msg.absoluteName}" />&nbsp;&nbsp;</td>											
					
					</td>					
					</tr>
						
				<c:set var="count" value="${count + 1}"/>
				</c:forEach>
				  	            
  	          		<tr class="classRowLabel">
  	          		  <td></td>
  	          		  <td><x:display name="${form.btnSubmit.absoluteName}"/></td>           
  	          		</tr>    	            	          	    		
  	          	   <jsp:include page="../form_footer.jsp" flush="true"/>
  	          	   	<tr class="classRowLabel">
  	          		  <td></td>
  	          		  <td></td>           
  	          		</tr>    			      
  	        </table>  	        
  	      </td>
  	    </tr>
  	  </table>  	  
    </td>
  </tr>
</table>

  	  
<script>

	function setNumbOfNotification(){

    numberNotication = document.forms['assetNotification.form'].elements['assetNotification.form.txtfdNotification'].value;
  	//Active Notifications  
	if (numberNotication > 0){
		 document.forms['assetNotification.form'].elements['assetNotification.form.txtfdNotification'].style.borderWidth = "1px";
		 document.forms['assetNotification.form'].elements['assetNotification.form.txtfdNotification'].style.borderColor = "#CCCCCC";
		 document.forms['assetNotification.form'].elements['assetNotification.form.txtfdNotification'].style.borderStyle = "solid";
  		window.location = "assetNotification.jsp?strNumb="+ numberNotication + "&notification=true";
 	}else { 	
		 document.forms['assetNotification.form'].elements['assetNotification.form.txtfdNotification'].style.borderWidth = "1px";
		 document.forms['assetNotification.form'].elements['assetNotification.form.txtfdNotification'].style.borderColor = "#de123e";
		 document.forms['assetNotification.form'].elements['assetNotification.form.txtfdNotification'].style.borderStyle = "solid";	
        alert('Invalid Number!');	
 		}
 	} 	
 	  	
 </script> 	  	
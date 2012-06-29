<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>
<c:set var="zeroDepreciationId" value="${widget.zeroDepreciationCategory}"/>
<% String [] zeroDepIdList = (String [])pageContext.getAttribute("zeroDepreciationId"); %>

<script language="javascript">

	function setNumbOfNotification(){
    numberNotication = document.forms['assetEdit.form1'].elements['assetEdit.form1.txtfdNotification'].value;
  	//Active Notifications  
	if (numberNotication > 0){
		document.forms['assetEdit.form1'].elements['assetEdit.form1.txtfdNotification'].style.borderWidth = "1px";
		document.forms['assetEdit.form1'].elements['assetEdit.form1.txtfdNotification'].style.borderColor = "#CCCCCC";
		document.forms['assetEdit.form1'].elements['assetEdit.form1.txtfdNotification'].style.borderStyle = "solid";
  		window.open('assetNotification.jsp?strNumbOfNotification='+numberNotication,'','resizable=yes,width=500,height=400,menubar=no,toolbar=no,scrollbars=yes');
 	}else { 	
		document.forms['assetEdit.form1'].elements['assetEdit.form1.txtfdNotification'].style.borderWidth = "1px";
		document.forms['assetEdit.form1'].elements['assetEdit.form1.txtfdNotification'].style.borderColor = "#de123e";
		document.forms['assetEdit.form1'].elements['assetEdit.form1.txtfdNotification'].style.borderStyle = "solid";	
        alert('Invalid Number!');	
 		}
 	} 
 
 	function getCalculateTotalCost(){
 	  	//quantity = document.getElementById('quantity');
 		//unitPrice = document.getElementById('unitPrice');
 
 	   	quantity = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'];
 	  	unitPrice = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdUnitPrice.absoluteName}"/>']; 		  
		category = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.selbxCategory.absoluteName}"/>']; 		  
 		if (quantity.value != '' && unitPrice.value != '') {
	 		qVal = parseInt(quantity.value);
 			pVal = parseFloat(unitPrice.value);
 			if (isNaN(qVal) || isNaN(pVal)) {
 				if(isNaN(qVal)){
		 		document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderWidth = "1px";
				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderColor = "#de123e";
		  		document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderStyle = "solid";
		  		
		        }else if(isNaN(pVal)){
		        document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdUnitPrice.absoluteName}"/>'].style.borderWidth = "1px";
		       	document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdUnitPrice.absoluteName}"/>'].style.borderColor = "#de123e";
		        document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdUnitPrice.absoluteName}"/>'].style.borderStyle = "solid";
		 		}
		 		alert('<fmt:message key='asset.message.isNotNumber'/>');
		 		
 			}else if(checkZeroDepreciation()){
 				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderWidth = "1px";
				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderColor = "#de123e";
		  		document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderStyle = "solid";
		  		alert('<fmt:message key='asset.massage.errorUnit'/>');
 			
 			}else if (moreThanTwoDecimal(pVal)) {
	 			document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderWidth = "1px";
				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderColor = "#CCCCCC";
	        	document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderStyle = "solid";  
	        		
	        	document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdUnitPrice.absoluteName}"/>'].style.borderWidth = "1px";
				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdUnitPrice.absoluteName}"/>'].style.borderColor = "#de123e";
				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdUnitPrice.absoluteName}"/>'].style.borderStyle = "solid";
				
			    quantity.value = qVal;
				unitPrice.value = pVal;
				alert('<fmt:message key='asset.message.twoDecimalPlace'/>');
 			} else { 		
 				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderWidth = "1px";
				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderColor = "#CCCCCC";
        		document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderStyle = "solid";  
       			
       			document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdUnitPrice.absoluteName}"/>'].style.borderWidth = "1px";
				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdUnitPrice.absoluteName}"/>'].style.borderColor = "#CCCCCC";
				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdUnitPrice.absoluteName}"/>'].style.borderStyle = "solid";  
				
				quantity.value = qVal;
				pVal = pVal.toString();
				if (pVal.indexOf(".") == -1) {
 					pVal = pVal + ".00";
 				}else if ((pVal.length - pVal.indexOf(".") - 1) < 2) {
					pVal = pVal + "0";
				}
				unitPrice.value = pVal;					
 				total =(roundVal(pVal*qVal)).toString();
 				if (total.indexOf(".") == -1) {
 					total = total + ".00";
 				}else if ((total.length - total.indexOf(".") - 1) < 2) {
					total = total + "0";
				}
 				document.getElementById('labelTotal').innerHTML=total;			
 			}
 		}
 	}
 	
 	function checkZeroDepreciation(){
 		if(<%= zeroDepIdList.length > 0 %>){
			<% 	for (int i=0; i<zeroDepIdList.length; i++){%>
				if(category.value == "<%=zeroDepIdList[i]%>"){					
					 return quantity.value.toString() != "1";						
				}	
		<%}%>		
		}  		
 		return false;
 	}
 	
	function roundVal(val){	
		var dec = 2;
		var result = Math.round(val*Math.pow(10,dec))/Math.pow(10,dec);
		return result;
	}
	
	function moreThanTwoDecimal(num) {
		var str = num.toString();
		if (str.indexOf(".") != -1) {
			if ((str.length - str.indexOf(".") - 1) > 2) {
				return true;
			}
		}
		return false;
	}	 
	
</script>
<!-- ------------------Forward Message ----------------------------->

<style>
.content{
	font-size: 8pt; font-family: Arial, Helvetica, sans-serif; 
}
.title{
	font-size: 9pt; font-family: Arial, Helvetica, sans-serif; 
}
</style>
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
  <tr>
    <td>
    
  	  <table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
  	    <tr>
  	      <td>
  	      
  	        <table width="100%" cellpadding="3" cellspacing="1">
  	          <jsp:include page="../form_header.jsp" flush="true"/>
  	    
  	          		<tr>
  	          		  <td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.assetName'/>&nbsp;*&nbsp;</td>
  	          		  <td class="classRow"><x:display name="${form.txtfdName.absoluteName}"/></td>           
  	          		</tr>  
  	          			
  	          		<tr>
  	          		  <td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.categoryName'/>&nbsp;*&nbsp;</td>
  	          		  <td class="classRow"><x:display name="${form.selbxCategory.absoluteName}"/></td>           
  	          		</tr> 
  	          		
  	          		<tr>
  	          		  <td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.dateOfPurchased'/>&nbsp;*&nbsp;</td>
  	          		  <td class="classRow"><x:display name="${form.datePurchased.absoluteName}"/></td>           
  	          		</tr> 
  	          		
  	          		<tr>
  	          		  <td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.quantity'/>&nbsp;*&nbsp;</td>
  	          		  <td class="classRow"><x:display name="${form.txtfdQty.absoluteName}"/></td>  
  	          		  <!-- <input type="text"  size="20" name="quantity" id="quantity" maxlength="10" onchange="getCalculateTotalCost()"> </td>            -->
  	          		</tr> 
  	          		
  	          		<tr>
  	          		  <td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.unitPrice'/><b><fmt:message key='asset.report.rm'/></b>&nbsp;*&nbsp;</td>
  	          		  <td class="classRow"><x:display name="${form.txtfdUnitPrice.absoluteName}"/></td>  
  	          		  <!--  <input type="text" name="unitPrice" id="unitPrice" size="20" maxlength="20" onchange="getCalculateTotalCost()"></td>-->
										
  	          		</tr>           		
  	         
  	          		<tr>
  	          		  <td class="classRowLabel" valign="top" align="right"><span ><fmt:message key='asset.label.totalCost'/></span>&nbsp;&nbsp;
  	          		   <br><span class="classRow"><fmt:message key="asset.message.noteTotalCost"/></span></td>
  	                  		<td class="classRow" ><div class="title" id="labelTotal">0</div></td>    	          		       
  	          		</tr>  	          		 
  	          			<!---- Hide Notitification Field ---->
  	          		<c:set var="hideField" value="${widget.strHideNotificationField}"/>         	
  	          		
  	          		<% if ("false".equals((String)pageContext.getAttribute("hideField"))){%>     	          		
  	          	
  	       	         <tr>
	  	          		<td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.numberOfnotification'/>&nbsp;&nbsp;</td>
	  	          		<td class="classRow"><x:display name="${form.txtfdNotification.absoluteName}"/> &nbsp; &nbsp;<input class=button type=button name="notification" value="Generate Notification(s)" onClick="setNumbOfNotification()"></td> 
	  	          	</tr> 
  	          		 
  	          		<%}
  	          		else {
  	          		 %>  	
  	          		           		 
  	       	         <tr>  	       	         
	  	          		<td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.setupNotification'/>&nbsp;&nbsp;</td>
	  	          		<td class="classRow"><x:display name="${form.radioNotifyYes.absoluteName}"/> &nbsp; <fmt:message key='asset.label.yes'/>&nbsp; &nbsp; <x:display name="${form.radioNotifyNo.absoluteName}"/>  &nbsp;<fmt:message key='asset.label.no'/></td>
	  	          	</tr> 
  	          		 <%
  	          		}  	          		
  	          		%>
  	          		
  	          		<tr>
  	          		  <td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.description'/>&nbsp;&nbsp;</td>
  	          		  <td class="classRow"><x:display name="${form.txtbxDescription.absoluteName}"/></td>           
  	          		</tr> 
  	          		
  	          		<tr>
  	          		  <td class="classRowLabel"></td>
  	          		  <td class="classRow"><x:display name="${form.panelBtn.absoluteName}"/></td>           
  	          		</tr> 
  	 
  	          				          
				 <jsp:include page="../form_footer.jsp" flush="true"/>          
  	        </table>
  	        
  	      </td>
  	    </tr>
  	  </table>	
  	  	
    </td>
  </tr>
</table>
<script language=javascript>
	window.onload = getCalculateTotalCost;
</script> 
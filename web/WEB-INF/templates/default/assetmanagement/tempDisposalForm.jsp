<%@page import="com.tms.assetmanagement.ui.popUpSingleSelectBx,
				com.tms.assetmanagement.model.AssetModule,
				kacang.Application,
				java.util.Map" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>
<c:set var="strUnit" value="${widget.strAvailableQuantity}"/>
<c:set var="strPrice" value="${widget.strPrice}"/>
<c:set var="unitLeft" value="${strUnit}"/>
<script>
	
	function calculateDisposalCostAdd(){
	
		unitpriceStr = (<c:out value="${strPrice}"/>);	
		if(unitpriceStr == '0'){
			return;
		}						 			
		unitprice = parseFloat(unitpriceStr);
		
		availableunit =parseInt( <c:out value="${strUnit}"/>);
		disposalQty = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'];

		if (disposalQty.value != '') {
	 		qVal = parseInt(disposalQty.value);
	 		if (isNaN(qVal)  || !(disposalQty.value.indexOf(".") == -1)) {
				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderWidth = "1px";
				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderColor = "#de123e";
				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderStyle = "solid";	     
						
 				alert("<fmt:message key="asset.message.notInteger"/>");
 			}else if(parseInt(disposalQty.value) > availableunit) {
 				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderWidth = "1px";
				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderColor = "#de123e";
				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderStyle = "solid";	     
 				
 				alert("<fmt:message key="asset.message.invalidQuantity1"/> " + availableunit + " <fmt:message key="asset.message.invalidQuantity2"/>"); 
 				document.getElementById('labelDisposalTotal').innerHTML = 0;							 			
	 		}else {	 			
				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderWidth = "1px";
				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderColor = "#CCCCCC";
				document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdQty.absoluteName}"/>'].style.borderStyle = "solid";
				  		
	 			total = (parseInt(disposalQty.value) * unitprice).toString();	 			
				if (total.indexOf(".") == -1) {
 					total = total + ".00";
 				}else if ((total.length - total.indexOf(".") - 1) < 2) {
					total = total + "0";
				}				
				document.getElementById('labelDisposalTotal').innerHTML = total;			
	 		}
	 		unitpriceStr = unitpriceStr.toString();
	 		//set the unitPrice in format ".00"
	 		if (unitpriceStr.indexOf(".") == -1) {
	 			unitpriceStr = unitpriceStr + ".00";
	 		}else if ((unitpriceStr.length - unitpriceStr.indexOf(".") - 1) < 2) {
				unitpriceStr = unitpriceStr + "0";
			}
			document.getElementById('unitPrice').innerHTML = " x " + unitpriceStr ;			
	 	
	 	}
	 		
	}
</script>
<!-- -------- Forward Messages ------------->
<c:if test="${forward.name=='InvalidQty'}" > 
</c:if>

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
       		  <td class="classRow"><x:display name="${form.singleSelectBx.absoluteName}"/></td>           
       		</tr>        
       		<tr>
       		  <td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.dateOfDisposal'/>&nbsp;*&nbsp;</td>
       		  <td class="classRow"><x:display name="${form.dateDisposal.absoluteName}"/></td>           
       		</tr>        		
       		<tr>
       		  <td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.quantity'/>&nbsp;*&nbsp;</td>
       		  <td class="classRow"  change="calculateDisposalCostAdd()"><x:display name="${form.txtfdQty.absoluteName}"/> &nbsp;<span id="unitPrice"></span> &nbsp; </td>           
       		</tr>  		
			<tr>
       		  <td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.disposalcost'/>&nbsp;&nbsp;
       		  <br><span class="classRow"><fmt:message key="asset.message.noteDisposalCost"/></span></td>
       		  <td class="classRow"><div id="labelDisposalTotal">0</div>&nbsp; &nbsp;
       		</td>           
       		</tr>            		
       		<tr>
       		  <td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.reasonDisposal'/>&nbsp;&nbsp;</td>
       		  <td class="classRow"><x:display name="${form.txtbxReasonD.absoluteName}"/></td>           
       		</tr>        		
       		<tr>
       		  <td class="classRowLabel" valign="top" align="right"></td>
       		  <td class="classRow"><x:display name="${form.panelBtn.absoluteName}"/></td>           
       		</tr>     		        	
         
      <x:set name="assetdisposal.form" property="strAvailableQuantity" value="0"></x:set>
      <x:set name="assetdisposal.form" property="strPrice" value="0"></x:set>

 <jsp:include page="../form_footer.jsp" flush="true"/>          
       </table>  	        
  	  </td>
  	  </tr>
  	  </table>  	  	
</td>
</tr>
</table>
<script language=javascript>
	window.onload = calculateDisposalCostAdd;
</script> 

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>
  
<c:set var="availableunit" value="${widget.strAvailableUnit}"/>
<c:set var="unitPrice" value="${widget.strUnitPrice}"/>
    
<script language=javascript>

	function calculateDisposalCost(){
		//set the unitPrice in format ".00"
		strUnitPrice = (<c:out value="${unitPrice}"/>).toString();
		if (strUnitPrice.indexOf(".") == -1) {
 			strUnitPrice = strUnitPrice + ".00";
 		}else if ((strUnitPrice.length - strUnitPrice.indexOf(".") - 1) < 2) {
			strUnitPrice = strUnitPrice + "0";
		}
		
		document.getElementById('labelunitPrice').innerHTML = strUnitPrice.toString();	
		iAvailableUnit = parseInt(<c:out value="${availableunit}"/>);
		fUnitPrice = parseFloat(strUnitPrice);
		disposalQuantity = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdDisposalQty.absoluteName}"/>'].value;
		if (isNaN(disposalQuantity)  || !(disposalQuantity.indexOf(".") == -1)) {
			document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdDisposalQty.absoluteName}"/>'].style.borderWidth = "1px";
			document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdDisposalQty.absoluteName}"/>'].style.borderColor = "#de123e";
			document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdDisposalQty.absoluteName}"/>'].style.borderStyle = "solid";	     
 			alert("Not a Integer!!!");
		}else if (disposalQuantity <= iAvailableUnit){
			document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdDisposalQty.absoluteName}"/>'].style.borderWidth = "1px";
			document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdDisposalQty.absoluteName}"/>'].style.borderColor = "#CCCCCC";
			document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdDisposalQty.absoluteName}"/>'].style.borderStyle = "solid";
  		
			dVAl = (disposalQuantity * fUnitPrice).toString();
				if (dVAl.indexOf(".") == -1) {
 					dVAl = dVAl + ".00";
 				}else if ((dVAl.length - dVAl.indexOf(".") - 1) < 2) {
					dVAl = dVAl + "0";
				}
			document.getElementById('labelDisposalTotal').innerHTML=dVAl.toString();			
		}else{
			document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdDisposalQty.absoluteName}"/>'].style.borderWidth = "1px";
			document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdDisposalQty.absoluteName}"/>'].style.borderColor = "#de123e";
			document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.txtfdDisposalQty.absoluteName}"/>'].style.borderStyle = "solid";	     

			alert("<fmt:message key="asset.message.invalidQuantity1"/> " + iAvailableUnit + " <fmt:message key="asset.message.invalidQuantity2"/>"); 
			document.getElementById('labelDisposalTotal').innerHTML=0;	
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
  
   
		<c:set var="maprow" value="${widget.mapDisposal}"/>
		<%					
			java.util.Map tempMapRow = (java.util.Map)pageContext.getAttribute("maprow"); 
			String strCategoryName	 = "";
			String strItemName = "";	
			
			if (tempMapRow != null){
			strCategoryName = tempMapRow.get("categoryName").toString();
			strItemName = tempMapRow.get("itemName").toString();
			}										
		%>
		<tr>
		<td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.Category'/>&nbsp;&nbsp;</td>
		<td class="classRow"><%=strCategoryName %></td>           
		</tr>  			
		<tr>
		<td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.assetName'/>&nbsp;&nbsp;</td>
		<td class="classRow"><%=strItemName %></td>           
		</tr>		
		<tr>
		<td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.dateOfDisposal'/>&nbsp;*&nbsp;</td>
		<td class="classRow"><x:display name="${form.dateDisposal.absoluteName}"/></td>           
		</tr> 		
		<tr>
		<td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.quantity'/>&nbsp;*&nbsp;</td>
		<td class="classRow"><x:display name="${form.txtfdDisposalQty.absoluteName}"/>&nbsp; x &nbsp;<span id="labelunitPrice">0</span> </td>           
		</tr> 		
		<tr>
		<td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.disposalcost'/>&nbsp;&nbsp;
		<br><span class="classRow"><fmt:message key="asset.message.noteDisposalCost"/></span></td>
		<td class="classRow"><div id="labelDisposalTotal">0</div></td>

		</tr>          		
		<tr>
		<td class="classRowLabel" valign="top" align="right"><fmt:message key='asset.label.reasonDisposal'/> &nbsp;*&nbsp;</td>
		<td class="classRow"><x:display name="${form.txtbxReasonD.absoluteName}"/></td>           
		</tr> 		
		<tr>
		<td class="classRowLabel" valign="top" align="right"></td>
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
	window.onload = calculateDisposalCost;
</script> 
<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title>Print Quotation</title>

</head>
  <style>
  .tableBackground {background-color: #999999; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:bold}
  .contentTitleFont {background-color: #FFFFFF; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 10pt; font-weight:bold}
  .contentBgColor {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  .tableHeader {background-color: #BBD5F2; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
  .tableRow {background-color: #FFFFFF; font-family: Arial, Verdana, Helvetica, sans-serif; font-size: 9pt; font-weight:normal}
  .button { cursor:hand;cursor:pointer;BORDER-RIGHT: #aaaaaa 1pt solid; BORDER-TOP: #DEF2FF 1pt solid; FONT-WEIGHT: normal; FONT-SIZE: 7pt; BORDER-LEFT: #DEF2FF 1pt solid; BORDER-BOTTOM: #aaaaaa 1pt solid; FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif; BACKGROUND-COLOR: #ffffff; TEXT-DECORATION: none}
  </style>
  
<script>

	function Print() {
	document.body.offsetHeight;
	window.print();
	}

</script>

<body >
<br>
<img src="/ekms/images/tmslogo2.gif">
<hr></hr>
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
	<td colspan="2" valign="TOP">
    	<table width="100%" border="0" cellpadding="2" cellspacing="0">
        	<tr>
            	<td class="contentBgColor" width="90%">
                    <x:display name="${w.company.absoluteName}" />
                </td>
	        </tr>
            <tr>
                <td class="contentBgColor" width="90%">
                    <x:display name="${w.companyAdd.absoluteName}" />
                </td>
            </tr>
            <tr>
                <td class="contentBgColor" width="90%">
                    <fmt:message key='supplier.label.telephone'/> : 
                    <x:display name="${w.companyTelephone.absoluteName}" />
                </td>
            </tr>
            <tr>
                <td class="contentBgColor" width="90%">
                    <fmt:message key='supplier.label.fax'/> : 
                    <x:display name="${w.companyFax.absoluteName}" />
                </td>
             </tr>
             <tr>
                 <td class="contentBgColor" width="90%"> 
                    <fmt:message key='department.label.email'/> : 
                    <x:display name="${w.companyEmail.absoluteName}" />
                 </td>
             </tr>
             
		</table>

<p></p>
         
		<table width="100%" border="0" cellpadding="2" cellspacing="0">
			<tr>
		        <td class="contentBgColor" width="20%" valign="top" nowrap>
		        	<strong>
		        		<fmt:message key='supplier.label.supp'/>: 
		        	</strong>
		        </td>
		        <td class="contentBgColor" width="90%">
		            <x:display name="${w.txtSupplier.absoluteName}" />
		        </td>
		    </tr>
		    <tr>
		        <td class="contentBgColor" width="20%" valign="top" nowrap>
		        	<strong>
		        		<fmt:message key='supplier.label.company'/>: 
		        	</strong>
		        </td>
		        <td class="contentBgColor" width="90%">
		            <x:display name="${w.txtSupplierCompany.absoluteName}" />
		        </td>
		    </tr>
		    <tr>
		        <td class="contentBgColor" width="25%" valign="top" align="left"><b>
		                    <x:display name="${w.purchaseItem.absoluteName}" /></b>
		        </td>
		        <td class="contentBgColor" width="25%" valign="top" align="left">
					<c:forEach items="${w.txtItem}" var="txtItem" varStatus = "status">
						- <x:display name="${txtItem.absoluteName}" /><br>
					</c:forEach>
		        </td>
		    </tr> 
		    <tr>
		         <td class="contentBgColor" width="20%" valign="top" nowrap>
		         	<strong>
		         		<fmt:message key='supplier.label.quotation'/>: 
		         	</strong>
		         </td>
		         <td class="contentBgColor" width="90%">
		            
		             <c:out value= "${w.txtQuotation }"/>
		         </td>
		    </tr>
		    <tr>
		         <td class="contentBgColor" width="20%" valign="top" nowrap>
		         	<strong>
		         		<fmt:message key='supplier.label.minBudget'/>: 
		         	</strong>
		         </td>
		         <td class="contentBgColor" width="90%">
		            RM <c:out value= "${w.txtMinBudget }"/>
		         </td>
		   	</tr>
		    <tr>        
		        <td class="contentBgColor" width="20%">
		         	<input type="button" class="button" value="Print" onclick="javascript:Print()">
		        </td>
		    </tr>
		</table>
	</td>
</table>
</body>
</html>
 <%@ page import="java.util.Collection, 
				com.tms.assetmanagement.model.AssetModule,
				com.tms.assetmanagement.model.DataSaveFixedAssetReport,
				com.tms.ekms.setup.model.SetupModule,				
				java.text.DecimalFormat,  
				java.text.SimpleDateFormat,
				java.util.Map,
				java.util.Date,   
				java.util.Iterator,        
                kacang.Application "%>
<%@include file="/common/header.jsp"%>

<x:permission permission="com.tms.assetmanagement.manageAssetPermission" module="com.tms.assetmanagement.model.AssetModule" url="noPermission.jsp">

<c:if test="${!empty param.id}">
	<c:set var="id" value="${param.id}"/>
</c:if>

<%
	String strId = (String)pageContext.getAttribute("id");

		Application app = Application.getInstance(); 
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);	
		SetupModule setup = (SetupModule) app.getModule(SetupModule.class);
		
		String moneyPattern="", financialMonth="", strAssetYear="", currencySymbol="" ;
		Collection colSavedReport = mod.retrieveSaveFixedAsset((String)strId);
		if(colSavedReport != null && colSavedReport.size() > 0){
			for (Iterator iterator = colSavedReport.iterator(); iterator.hasNext();) {
				DataSaveFixedAssetReport objSavedReport = (DataSaveFixedAssetReport)iterator.next();
				moneyPattern = objSavedReport.getCurrencyPattern();
				financialMonth = objSavedReport.getEndingMonth();
				currencySymbol = objSavedReport.getCurrencySymbol();
				strAssetYear = new Integer(objSavedReport.getAssetYear()).toString();
				break;
			}
		}		
	%>

<html>
<head>
<style>
.content{
	font-size: 8pt; font-family: Arial, Helvetica, sans-serif; 
}
.title{
	font-size: 9pt; font-family: Arial, Helvetica, sans-serif; 
}
.tableBackground{
	font-size: 10pt; font-family: Arial, Helvetica, sans-serif; background-color=#666666;
}
.tableHeader{
	font-size: 8pt; font-family: Arial, Helvetica, sans-serif; background-color=#CCCCCC;
	
}
</style>
</head>
<body>
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
  <tr>
    <td>
    
  	 <table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
  	   <tr>
  	      <td>
  	      
  	        <table width="100%" cellpadding="3" cellspacing="1">
  	          <jsp:include page="../form_header.jsp" flush="true"/>
  	          	
				<!--- set header field--->
				 
				<tr>
  	          	<td colspan="12"  align="right"><img src="/ekms/<%=setup.get("siteLogo") %>"/></td>
  	          	</tr>
  	          	
				<%
			// Report Header 
			String [] getMonth = new String [13];
				getMonth[0] = Application.getInstance().getMessage("asset.report.jan","Jan");
				getMonth[1] = Application.getInstance().getMessage("asset.report.feb","Feb");
				getMonth[2] = Application.getInstance().getMessage("asset.report.mar","Mar");
				getMonth[3] = Application.getInstance().getMessage("asset.report.apr","Apr");
				getMonth[4] = Application.getInstance().getMessage("asset.report.may","May");
				getMonth[5] = Application.getInstance().getMessage("asset.report.jun","Jun");
				getMonth[6] = Application.getInstance().getMessage("asset.report.jul","Jul");
				getMonth[7] = Application.getInstance().getMessage("asset.report.aug","Aug");
				getMonth[8] = Application.getInstance().getMessage("asset.report.sept","Sept");
				getMonth[9] = Application.getInstance().getMessage("asset.report.oct","Oct");
				getMonth[10] = Application.getInstance().getMessage("asset.report.nov","Nov");
				getMonth[11] = Application.getInstance().getMessage("asset.report.dec","Dec");	
	
			String strReportDate = "";
			String strNextYear = "" ;
			int iNextYear = 0;
			System.out.println("sad"+ financialMonth);
			int iEndingMonth = Integer.parseInt(financialMonth) + 1 ;
			int iStartingMonth = iEndingMonth + 1;
			if (iStartingMonth > 12)
				strReportDate = getMonth[0] + " " + new Integer(strAssetYear).toString() +" - " + getMonth[11] + " " + new Integer(strAssetYear).toString();
			else{
				iNextYear = Integer.parseInt(strAssetYear) + 1 ;
				strReportDate = getMonth[iStartingMonth - 1]+" "+ new Integer(strAssetYear).toString()+" - "+ getMonth[iEndingMonth-1]+" " +  new Integer(iNextYear).toString();
			}
			%>
				<tr>
					<td colspan="17" align="center" class="tableHeader" ><b><fmt:message key='asset.report.fixedAssetReport'/> &nbsp; <%= strReportDate%> </b></td>
				</tr>
				
				
				<tr class="tableHeader" ><th><th><th><th rowspan="1" class="tableHeader" colspan="4" align="center"><b><fmt:message key='asset.report.costField'/></b>
					<th rowspan="1" class="tableHeader"  colspan="4" align="center"><b><fmt:message key='asset.report.accumDepn'/></b></th><th></th></tr>
				<tr>
					<th class="tableHeader" align="center"><fmt:message key='asset.report.description'/></th>
				   	<th class="tableHeader" align="center"><fmt:message key='asset.report.unit'/></th>
				    <th class="tableHeader" align="center"><fmt:message key='asset.report.date'/></th>
				    <th class="tableHeader" align="center"><fmt:message key='asset.report.balbf'/></th>
				    <th class="tableHeader" align="center"><fmt:message key='asset.report.addition'/></th>
				    <th class="tableHeader" align="center"><fmt:message key='asset.report.disposal'/></th>
				    <th class="tableHeader" align="center"><fmt:message key='asset.report.balcf'/></th>
				    <th class="tableHeader" align="center"><fmt:message key='asset.report.balbf'/></th>
			        <th class="tableHeader" align="center"><fmt:message key='asset.report.dCharge'/></th>
			        <th class="tableHeader" align="center"><fmt:message key='asset.report.disposal'/></th>
      		        <th class="tableHeader" align="center"><fmt:message key='asset.report.balcf'/></th>
 		            <th class="tableHeader" align="center"><fmt:message key='asset.report.nbv'/></th>		      
				</tr>
					<tr>
					<th class="tableHeader" colspan="3"></th>
				    <th class="tableHeader" align="center"><%=currencySymbol%></th>
				    <th class="tableHeader" align="center"><%=currencySymbol%></th>
				    <th class="tableHeader" align="center"><%=currencySymbol%></th>
				    <th class="tableHeader" align="center"><%=currencySymbol%></th>
				    <th class="tableHeader" align="center"><%=currencySymbol%></th>
			        <th class="tableHeader" align="center"><%=currencySymbol%></th>
			        <th class="tableHeader" align="center"><%=currencySymbol%></th>
      		        <th class="tableHeader" align="center"><%=currencySymbol%></th>
 		            <th class="tableHeader" align="center"><%=currencySymbol%></th>		      
				</tr>	  	          		   
<!----<%
		
		String [] strFieldName = {"costBalBf", "costAdditon", "costDisposal", "costBalCf", "accumDepnBalBf"
  			, "accumDepnDCharge", "accumDepnDisposal", "accumDepnBalCf", "nbv"};  			
  			double [] dGrandTotal = new double[strFieldName.length];	
  			
  			String strCurrentCategory;
 			String strPreviousCategory = "";
 			
 			int iItems = 0;
	 		int iTotalItems = 0;
	 		
			Collection colSavedMonthlyReport = mod.retrieveSaveFixedAsset((String)strId);
			if(colSavedMonthlyReport != null && colSavedMonthlyReport.size() > 0){
				for (Iterator iterator = colSavedMonthlyReport.iterator(); iterator.hasNext();) {
					DataSaveFixedAssetReport objSavedReport = (DataSaveFixedAssetReport)iterator.next();
					strCurrentCategory = objSavedReport.getCategoryName();
				if (!strCurrentCategory.equals(strPreviousCategory)){
					//get number of items for each category														
	 					iTotalItems = mod.countSaveFixedAsset(Integer.parseInt(strAssetYear), strCurrentCategory, objSavedReport.getSaveAssetId());	 						
	 					iItems = 0;
	 				
	 				String strDepreciation = new DecimalFormat("0").format(objSavedReport.getDepreciation());
					%>
						<!--- Category and Depreciation (%) --->	
						<tr colspan="c" class="title">
						<td align="left"><b><%= strCurrentCategory%>&nbsp;(<%= strDepreciation%>%)</b></td> 		
						</tr>	
					<%
				}
				strPreviousCategory = strCurrentCategory;
			
				//generate record
					iItems ++ ; 
				
 		 		%>  		 		
		 	
	 		<tr>
		 		<td align="left" class="content"><%= objSavedReport.getItemName()%></td>
		 		<td align="center" class="content"><%= new DecimalFormat("0").format((float)objSavedReport.getAssetUnit())%></td>
		 		<td align="center" class="content"><%= new SimpleDateFormat("dd/MM/yyyy").format(objSavedReport.getAssetDate())%></td>		 		
		 		<td align="right" class="content"><%= (objSavedReport.getCostBalBf()== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(objSavedReport.getCostBalBf())))%></td>
		 		<td align="right" class="content"><%= (objSavedReport.getCostAdditon()== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(objSavedReport.getCostAdditon())))%></td>
		 		<td align="right" class="content"><%= (objSavedReport.getCostDisposal()== 0 ? "-" : "("+new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(objSavedReport.getCostDisposal()))+")")%></td>
		 		<td align="right" class="content"><%= new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(objSavedReport.getCostBalCf()))%></td>
		 		<td align="right" class="content"><%= (objSavedReport.getAccumDepnBalBf()== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(objSavedReport.getAccumDepnBalBf())))%></td>
		 		<td align="right" class="content"><%= (objSavedReport.getAccumDepnDCharge()== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(objSavedReport.getAccumDepnDCharge())))%></td>
		 		<td align="right" class="content"><%= (objSavedReport.getAccumDepnDisposal()== 0 ? "-" :"("+ new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(objSavedReport.getAccumDepnDisposal()))+")")%></td>
		 		<td align="right" class="content"><%= new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(objSavedReport.getAccumDepnBalCf()))%></td>
		 		<td align="right" class="content"><%= new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(objSavedReport.getNbv()))%></td>
			 </tr>	 			
	 		
 		 <%		 
 		  		 		 				
	 	//calculate SubTotal	 
	 	if (iItems == iTotalItems){	
	 		double [] dFixedAsset = new double[strFieldName.length];
  		  		for (int i=0; i<dFixedAsset.length; i++)
  				dFixedAsset[i] = mod.getFixedSumOfColumn(Integer.parseInt((String)strAssetYear), strPreviousCategory, strFieldName[i], objSavedReport.getSaveAssetId());
  				
  				%>		
  		<tr><td></td><td></td><td></td>
  			<td colspan="9"><img src="images/clear.gif" height="1" width="100%" style="background-color:black"></td>
  		</tr>
  			
  		<tr>
  			<td align="left" class="content"><b>SubTotal</b></td>
			<td></td>
	 		<td></td>	
	 		<td align="right" class="content"><b><%= (dFixedAsset[0]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[0])))%></b></td>	 		
	 		<td align="right" class="content"><b><%= (dFixedAsset[1]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[1])))%></b></td>
	 		<td align="right" class="content"><b><%= (dFixedAsset[2]== 0 ? "-" : "("+new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[2]))+")")%></b></td>
	 		<td align="right" class="content"><b><%= new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[3]))%></td>
	 		<td align="right" class="content"><b><%= (dFixedAsset[4]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[4])))%></b></td>
	 		<td align="right" class="content"><b><%= (dFixedAsset[5]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[5])))%></b></td>
    		<td align="right" class="content"><b><%= (dFixedAsset[6]== 0 ? "-" :"("+ new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[6]))+")")%></b></td>
	 		<td align="right" class="content"><b><%= new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[7]))%></b></td>
	 		<td align="right" class="content"><b><%= new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[8]))%></b></td>
	 	</tr>
  		<tr><td colspan="12"></td></tr>
  		
   		
   		 	<%
 		 	
 		 	//Calculate Grand Total			
  			
  			for (int i=0; i<dGrandTotal.length; i++)		
  				dGrandTotal[i] += mod.getFixedSumOfColumn(Integer.parseInt((String)strAssetYear), strPreviousCategory, strFieldName[i], objSavedReport.getSaveAssetId());
  				  			
 		 		
 		 		} 			 		 	
			}

 		 		%>
	 		<tr height="20" valign="bottom"><td></td><td></td><td></td>
  			<td colspan="9" valign="bottom"><img src="images/clear.gif" height="1" width="100%" style="background-color:black"></td>
  		</tr>
  			
  		<tr valign="top">
  			<td align="left" class="content"><b>Grand Total</b></td>
			<td></td>
	 		<td></td>	
	 		<td align="right" class="content"><b><%= (dGrandTotal[0]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[0])))%></b></td>	 		
	 		<td align="right" class="content"><b><%= (dGrandTotal[1]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[1])))%></b></td>
	 		<td align="right" class="content"><b><%= (dGrandTotal[2]== 0 ? "-" : "("+new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[2]))+")")%></b></td>
	 		<td align="right" class="content"><b><%= new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[3]))%></td>
	 		<td align="right" class="content"><b><%= (dGrandTotal[4]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[4])))%></b></td>
	 		<td align="right" class="content"><b><%= (dGrandTotal[5]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[5])))%></b></td>
    		<td align="right" class="content"><b><%= (dGrandTotal[6]== 0 ? "-" :"("+ new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[6]))+")")%></b></td>
	 		<td align="right" class="content"><b><%= new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[7]))%></b></td>
	 		<td align="right" class="content"><b><%= new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[8]))%></b></td>
	 	</tr>
  		<tr valign="top"><td></td><td></td><td></td>
  			<td colspan="9" valign="top"><img src="images/doubleLine.gif" height="3" width="100%" style="background-color:black"></td>
		</tr>
 		 <%
			
		}
		 %>		
			<jsp:include page="../form_footer.jsp" flush="true"/>          
  	        </table>
  	        
  	      </td>
  	    </tr>
  	  </table>	
  	  	
    </td>
  </tr>
</table>
</body>
</html>

</x:permission>
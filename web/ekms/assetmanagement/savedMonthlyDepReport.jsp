 <%@ page import="java.util.Collection, 
				com.tms.assetmanagement.model.AssetModule,
				com.tms.assetmanagement.model.DataSaveMonthDepReport,
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
		
		String moneyPattern="", financialMonth="", strAssetYear="" ;
		Collection colSavedReport = mod.retrieveSaveMonthlyDep((String)strId);
		if(colSavedReport != null && colSavedReport.size() > 0){
			for (Iterator iterator = colSavedReport.iterator(); iterator.hasNext();) {
				DataSaveMonthDepReport objSavedReport = (DataSaveMonthDepReport)iterator.next();
				moneyPattern = objSavedReport.getCurrencyPattern();
				financialMonth = objSavedReport.getEndingMonth();
				strAssetYear = new Integer(objSavedReport.getAssetYear()).toString();
				break;
			}
		}
		String [] setNewMonth  = new String [13];
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
		
		String strYear =  strAssetYear.substring(2);
		int j=Integer.parseInt(financialMonth.toString()) + 1 ;
		if ( j >11) // if j=12 (december) -> set to Jan
			j=0;
		for (int i = 0; i <12; i++){
			setNewMonth[i]= getMonth[j]+ " '" + strYear;
			j++;
			if (j >11){
				j=0;
				int iYear = Integer.parseInt(strYear) + 1;
				if (iYear == 100)
					iYear = 0;
				strYear = new DecimalFormat("00").format(iYear);
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
  	          	<tr>
  	          	<td colspan="17"  align="right"><img src="/ekms/<%=setup.get("siteLogo") %>"/></td>
  	          	</tr>
  	          	
				<!--- set header field--->
				<tr>
					<td colspan="17" align="center" class="tableHeader" ><b><fmt:message key='asset.report.monthlyDepreciationReport'/> &nbsp; <%= strAssetYear%> </b></td>
				</tr>
				
				<tr>
				  <th class="tableHeader" align="center"><fmt:message key='asset.report.description'/></th>
				  <th class="tableHeader"><fmt:message key='asset.report.unit'/></th>
				  <th class="tableHeader"><fmt:message key='asset.report.date'/></th>
				  <th class="tableHeader"><fmt:message key='asset.report.cost'/></th>
				  <th class="tableHeader"><%=setNewMonth[0]%></th>
				  <th class="tableHeader"><%=setNewMonth[1]%></th>
				  <th class="tableHeader"><%=setNewMonth[2]%></th>
				  <th class="tableHeader"><%=setNewMonth[3]%></th>
				  <th class="tableHeader"><%=setNewMonth[4]%></th>
				  <th class="tableHeader"><%=setNewMonth[5]%></th>
				  <th class="tableHeader"><%=setNewMonth[6]%></th>
				  <th class="tableHeader"><%=setNewMonth[7]%></th>
				  <th class="tableHeader"><%=setNewMonth[8]%></th>
				  <th class="tableHeader"><%=setNewMonth[9]%></th>
				  <th class="tableHeader"><%=setNewMonth[10]%></th>
				  <th class="tableHeader"><%=setNewMonth[11]%></th>
				  <th class="tableHeader"><fmt:message key='asset.report.total'/></th>
				</tr>
				
		<%
		
			String [] strFieldName = {"totalCost", "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug","sept", "oct", "nov", "dece", "totalDepreciation"};
  			double [] dmonthlyDep = new double[14];
  			double [] dmonthlyGrandTotal = new double[14];
  			double [] dNewGrandTotal = new double[13];
  			
  			String strCurrentCategory;
 			String strPreviousCategory = "";
 			
 			int iItems = 0;
	 		int iTotalItems = 0;
	 		
			Collection colSavedMonthlyReport = mod.retrieveSaveMonthlyDep((String)strId);
			if(colSavedMonthlyReport != null && colSavedMonthlyReport.size() > 0){
				for (Iterator iterator = colSavedMonthlyReport.iterator(); iterator.hasNext();) {
					DataSaveMonthDepReport objSavedReport = (DataSaveMonthDepReport)iterator.next();
					strCurrentCategory = objSavedReport.getCategoryName();
				if (!strCurrentCategory.equals(strPreviousCategory)){
					//get number of items for each category
	 					iTotalItems = mod.countSaveMonthlyDep(Integer.parseInt(strAssetYear), strCurrentCategory, objSavedReport.getSaveMonthlyId());	 						
	 					iItems = 0;
	 					
					String strDepreciation = new DecimalFormat("0").format(objSavedReport.getDepreciation());
					%>
						<!--- Category and Depreciation (%) --->	
						<tr colspan="12" class="title">
						<td align="left"><b><%= strCurrentCategory%>&nbsp;(<%= strDepreciation%>%)</b></td> 		
						</tr>	
					<%
				}
				strPreviousCategory = strCurrentCategory;
			
				//generate record
					iItems ++ ; 
				
 		 		double[] dValue= {objSavedReport.getJan(), objSavedReport.getFeb(), objSavedReport.getMar(),objSavedReport.getApr(), objSavedReport.getMay(), objSavedReport.getJun(),
 		 		objSavedReport.getJul(), objSavedReport.getAug(), objSavedReport.getSept(), objSavedReport.getOct(), objSavedReport.getNov(), objSavedReport.getDece()};  		 		
 		 			
 		 		double [] dNewValue = new double[13];
 		 		int a = Integer.parseInt(financialMonth)+ 1;
 		 		if (a > 11)
 		 			a=0;
 		 		for(int n=0; n<12; n++){
 		 			dNewValue[n] = dValue[a];
 		 			a++;
 		 			if (a >11)
 		 			a=0;
 		 		}
 		 		%> 
 		 		
		 	<tr> 
		 		<td align="left" class="content"><%= objSavedReport.getItemName()%></td>
		 		<td align="center" class="content"><%= new DecimalFormat("0").format((float)objSavedReport.getAssetUnit())%></td>
		 		<td align="center" class="content"><%= new SimpleDateFormat("dd/MM/yyyy").format(objSavedReport.getAssetDate())%></td>
		 		<td align="right" class="content"><%= new DecimalFormat(moneyPattern).format(objSavedReport.getTotalCost())%></td>
		 		<td align="right" class="content"><%= (dNewValue[0]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[0])))%></td>
		 		<td align="right" class="content"><%= (dNewValue[1]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[1])))%></td>
		 		<td align="right" class="content"><%= (dNewValue[2]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[2])))%></td>
		 		<td align="right" class="content"><%= (dNewValue[3]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[3])))%></td>
		 		<td align="right" class="content"><%= (dNewValue[4]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[4])))%></td>
		 		<td align="right" class="content"><%= (dNewValue[5]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[5])))%></td>
		 		<td align="right" class="content"><%= (dNewValue[6]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[6])))%></td>
		 		<td align="right" class="content"><%= (dNewValue[7]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[7])))%></td>
		 		<td align="right" class="content"><%= (dNewValue[8]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[8])))%></td>
		 		<td align="right" class="content"><%= (dNewValue[9]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[9])))%></td>
		 		<td align="right" class="content"><%= (dNewValue[10]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[10])))%></td>
		 		<td align="right" class="content"><%= (dNewValue[11]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[11])))%></td>
		 		<td align="right" class="content"><%= new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(objSavedReport.getTotalDepreciation()))%></td>
			
		 </tr>
 		 		
 		 <%			 
 		  		 		 				
	 	//calculate SubTotal	 
	 	if (iItems == iTotalItems){	
  			for (int i=0; i<14; i++)
  				dmonthlyDep[i] = mod.getMonthlySumOfColumn(Integer.parseInt((String)strAssetYear), strPreviousCategory, strFieldName[i], objSavedReport.getSaveMonthlyId());
  				
  				double [] dNewSubTotal = new double[13];
 		 		int b = Integer.parseInt(financialMonth)+ 1 ;
		 		 if(b > 11)
		 		 	 b=0;
 		 		for(int y=0; y<12; y++){
 		 			dNewSubTotal[y] = dmonthlyDep[b+1];
 		 			b++;
 		 			if (b >11)
 		 				b=0;
 		 		}
 		 	%>
 		 				<tr><td></td><td></td><td></td>
  			<td colspan="14"><img src="images/clear.gif" height="1" width="100%" style="background-color:black"></td>
  			</tr>
	
			<tr>
		 	<td align="left" class="content"><b>SubTotal</b></td>
		 	<td></td>
		 	<td></td>
		 	<td align="right" class="content"><b><%= (dmonthlyDep[0]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dmonthlyDep[0])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewSubTotal[0]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[0])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewSubTotal[1]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[1])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewSubTotal[2]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[2])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewSubTotal[3]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[3])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewSubTotal[4]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[4])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewSubTotal[5]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[5])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewSubTotal[6]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[6])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewSubTotal[7]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[7])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewSubTotal[8]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[8])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewSubTotal[9]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[9])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewSubTotal[10]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[10])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewSubTotal[11]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[11])))%></b></td>
		 	<td align="right" class="content"><b><%= new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dmonthlyDep[13]))%></b></td>
		 	</tr> 
 		 	<%
 		 	
 		 	//Calculate Grand Total			
  			
  			for (int i=0; i<14; i++)		
  				dmonthlyGrandTotal[i] += mod.getMonthlySumOfColumn(Integer.parseInt((String)strAssetYear), strPreviousCategory, strFieldName[i],objSavedReport.getSaveMonthlyId()); 
  				  			
 		 		int c = Integer.parseInt(financialMonth) + 1 ;
 		 		if (c > 11)
 		 			c=0;
 		 		for(int t=0; t<12; t++){
 		 		dNewGrandTotal[t] = dmonthlyGrandTotal[c+1];
 					c++;
 					if (c >11)
 		 				c=0;
 		 		} 	
 		 		} 			 		 	
			}
 		 		%>
 		 		<tr height="20" valign="bottom"><td></td><td></td><td></td>
  			<td colspan="14"><img src="images/clear.gif" height="1" width="100%" style="background-color:black"></td>
  			</tr>
		
  			<tr>
		 	<td align="left" class="content"><b>Grand Total</b></td>
		 	<td></td>
		 	<td></td>
		 	<td align="right" class="content"><b><%= (dmonthlyGrandTotal[0]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dmonthlyGrandTotal[0])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewGrandTotal[0]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[0])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewGrandTotal[1]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[1])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewGrandTotal[2]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[2])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewGrandTotal[3]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[3])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewGrandTotal[4]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[4])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewGrandTotal[5]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[5])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewGrandTotal[6]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[6])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewGrandTotal[7]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[7])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewGrandTotal[8]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[8])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewGrandTotal[9]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[9])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewGrandTotal[10]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[10])))%></b></td>
		 	<td align="right" class="content"><b><%= (dNewGrandTotal[11]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[11])))%></b></td>
		 	<td align="right" class="content"><b><%= new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dmonthlyGrandTotal[13]))%></b></td>
		 </tr>
		 
		 <tr height="20" valign="top"><td></td><td></td><td></td>
  			<td colspan="14"><img src="images/doubleLine.gif" height="3" width="100%" style="background-color:black"></td>
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
				
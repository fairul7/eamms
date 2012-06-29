<%@ page import="java.util.Collection, 
				com.tms.assetmanagement.model.AssetModule,
				com.tms.assetmanagement.model.DataFixedAssetReport,
				com.tms.assetmanagement.model.DataFinancialSetup,
				java.util.Iterator,
				java.util.Date,
				java.text.DecimalFormat,  
				java.text.SimpleDateFormat,
				java.util.Map, 
				java.util.StringTokenizer,          
                kacang.Application "%>
                
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>

<c:if test="${!empty param.print}">
	<c:set var="print" value="${param.print}"/>
</c:if>  

<c:if test="${empty param.print}">
	<c:set var="print" value="false"/>
</c:if>  

<%
//default moneyPattern
  String moneyPattern = "#,###,###,##0.00";
  String currencySymbol = "RM";
  String financialMonth = "0";
  
	Application app = Application.getInstance(); 
	AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);	
	Collection colFinancialDetail = mod.retrieveFinancialSetup();
	if (colFinancialDetail != null && colFinancialDetail.size() > 0){
		for (Iterator iterator = colFinancialDetail.iterator(); iterator.hasNext();) {
			DataFinancialSetup obj = (DataFinancialSetup)iterator.next();
			 moneyPattern = obj.getCurrencyPattern();
			 currencySymbol = obj.getCurrencySymbol();
			 financialMonth = obj.getFinancialMonth();
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
  				<tr >
    				<td  colspan="12" align="right" valign="top"><img src="<c:out value="${widget.companyLogo}"/>"></td>  
				</tr>
				<%--<tr>
   		 			<td colspan="12" align="left"><b><c:out value="${widget.companyName}"/></b></td>
   		 		</tr>
   		 		--%>
   		 		
  		<c:set var="toyear" value="${widget.year}"/>		
		<c:set var="category" value="${widget.category}"/>
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
			String stryear = (String)pageContext.getAttribute("toyear");
			String strNextYear = "" ;
			int iNextYear = 0;
			int iEndingMonth = Integer.parseInt(financialMonth) + 1 ;
			int iStartingMonth = iEndingMonth + 1;
			if (iStartingMonth > 12)
				strReportDate = getMonth[0] + " " + new Integer(stryear).toString() +" - " + getMonth[11] + " " + new Integer(stryear).toString();
			else{
				iNextYear = Integer.parseInt(stryear) + 1 ;
				strReportDate = getMonth[iStartingMonth - 1]+" "+ new Integer(stryear).toString()+" - "+ getMonth[iEndingMonth-1]+" " +  new Integer(iNextYear).toString();
			}
			pageContext.setAttribute("strReportDate", strReportDate);
		%>						
		<!-- Set Record Year -->
		<x:set name="fixedAssetReport.form" property="recordYear" value="${strReportDate}"/>	
			
		<tr>
					<td colspan="12" align="center"  class="tableHeader" ><b><fmt:message key='asset.report.fixedAssetReport'/>&nbsp; &nbsp;<%=strReportDate%></b></td>
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
		          	  
	
		<%	 
			String strTo = (String)pageContext.getAttribute("toyear");
			int iToYear = Integer.parseInt(strTo);
			String strCategory = (String)pageContext.getAttribute("category");
			StringTokenizer tokenizer = new StringTokenizer(strCategory, ",");
			String category ="";
			
			String [] strFieldName1 = {"costBalBf", "costAdditon", "costDisposal", "costBalCf", "accumDepnBalBf"
  			, "accumDepnDCharge", "accumDepnDisposal", "accumDepnBalCf", "nbv"};  			
  			double [] dGrandTotal = new double[strFieldName1.length];	
  			 			
			 Map mapCategory =  null;
			 Map mapItem = null;
			 Collection coldata = null;	
			 String strCurrentCategoryid;
			 String strPreviousCategoryid = "";
			 int iItems = 0;
			 int iTotalItems = 0;
	 	
	 		if(iToYear != 0  && strCategory != null && !strCategory.equals("")){
	 			while(tokenizer.hasMoreTokens())
             	{
             		category = tokenizer.nextToken();            	
		
	 			coldata = mod.retrieveFixedAssetRecord(category, iToYear);
	 			if (coldata != null && coldata.size() > 0 ){
	 				%>
		  			<x:set name="fixedAssetReport.form" property="showPrintViewDisposal" value="true"/>	
		  			<%
	
	 				for (java.util.Iterator iterator = coldata.iterator(); iterator.hasNext();) {
	 					DataFixedAssetReport obj = (DataFixedAssetReport)iterator.next();
	 					strCurrentCategoryid = obj.getCategoryId().toString();
	 				
	 					if (!strCurrentCategoryid.equals(strPreviousCategoryid)){
	 						//get number of items for each category
	 						iTotalItems = mod.countFixedAsset(strCurrentCategoryid, iToYear);
	 						iItems = 0;
	 									
	 						 mapCategory = mod.retrieveCategory(strCurrentCategoryid);
	 						 strPreviousCategoryid = strCurrentCategoryid;
	 						
	 						 if (mapCategory != null){ 
	 						 	String	strCategoryName = mapCategory.get("categoryName").toString();
								float fDepreciation = Float.parseFloat(mapCategory.get("depreciation").toString());
								String depreciation = new DecimalFormat("0").format((float)fDepreciation);
								%>
								
								<!--- Category and Depreciation (%) --->	
								<tr colspan="12">
									<td align="left" class="title"><b><%= strCategoryName%>&nbsp;(<%= depreciation%>%)</b></td> 		
								</tr>	
															
								<%
								}//end of map is nt null	 						 	
	 					}//end if different id
	 			//Listing details
	 			mapItem = (Map) mod.retrieveItems (obj.getItemId().toString(), obj.getCategoryId().toString());
	 			iItems ++ ;
	 	 			%>
	 		<tr>
		 		<td align="left" class="content"><%= mapItem.get("itemName")%></td>
		 		<td align="center" class="content"><%= new DecimalFormat("0").format((float)obj.getAssetUnit())%></td>
		 		<td align="center" class="content"><%= new SimpleDateFormat("dd/MM/yyyy").format(obj.getAssetDate())%></td>		 		
		 		<td align="right" class="content"><%= (obj.getCostBalBf()== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getCostBalBf())))%></td>
		 		<td align="right" class="content"><%= (obj.getCostAdditon()== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getCostAdditon())))%></td>
		 		<td align="right" class="content"><%= (obj.getCostDisposal()== 0 ? "-" : "("+new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getCostDisposal()))+")")%></td>
		 		<td align="right" class="content"><%= new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getCostBalCf()))%></td>
		 		<td align="right" class="content"><%= (obj.getAccumDepnBalBf()== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getAccumDepnBalBf())))%></td>
		 		<td align="right" class="content"><%= (obj.getAccumDepnDCharge()== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getAccumDepnDCharge())))%></td>
		 		<td align="right" class="content"><%= (obj.getAccumDepnDisposal()== 0 ? "-" :"("+ new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getAccumDepnDisposal()))+")")%></td>
		 		<td align="right" class="content"><%= new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getAccumDepnBalCf()))%></td>
		 		<td align="right" class="content"><%= new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getNbv()))%></td>
			 </tr>
	 			
	 			<%		
	 	//calculate SubTotal	
	 	if (iItems == iTotalItems){		
	 		 	
  			String [] strFieldName = {"costBalBf", "costAdditon", "costDisposal", "costBalCf", "accumDepnBalBf"
  			, "accumDepnDCharge", "accumDepnDisposal", "accumDepnBalCf", "nbv"};
  			double [] dFixedAsset = new double[strFieldName.length];
  			for (int i=0; i<dFixedAsset.length; i++)
  				dFixedAsset[i] = mod.getAssetSumOfColumn(iToYear, obj.getCategoryId().toString(), strFieldName[i]);
  	  	
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
   			
  		}// end of items(subTotal)
	 	
	 					}//end for loop
	 	
	
	 	//Calculate Grand Total

  			for (int i=0; i<dGrandTotal.length; i++)
  				dGrandTotal[i] += mod.getAssetSumOfColumn( iToYear, category, strFieldName1[i]); 
  			}//while StringTonizer is nt null
  						 		
	 			
	 		}//end if coldata is nt null! 
	 		
	 		if (iItems != 0 ){
	 			
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
  		
  		<%
  			String strPrint = (String)pageContext.getAttribute("print");
  			if ("false".equals(strPrint)){
  		%>
  			<tr><td  colspan="20" align="center">
  			<x:display name="${form.btnSave.absoluteName}"/>
  			<input type="button" class="button" value="<fmt:message key='asset.report.exportTocsv'/>" onclick="location='exportFixedAssetReportCSV.jsp'"></td></td></tr>
  		<%
  		}
  		}
			}//end of IF attributes not null!
				
		%>
				 <jsp:include page="../form_footer.jsp" flush="true"/>          
  	        </table>  	        
 	    </td>
  	  </tr>
  	  </table>	
  	 <script>
        function popUpViewDisposalDate(){		
       		myWin = open("<c:url value="viewDisposalDate.jsp?yearly=true"/>","View","width=800,height=500,toolbar=no,menubar=yes,resizable=yes,scrollbars=yes");
        }
    	function doPrint() {
   		myWin = open("<c:url value="printFixedAssetReport.jsp?print=true"/>","Report","width=800,height=500,toolbar=no,menubar=no,resizable=yes,scrollbars=yes");
      }
    </script>
    	<c:set var="show" value="${widget.showPrintViewDisposal}"/>
  		<% String show = (String)pageContext.getAttribute("show"); 
	
    		if("true".equals(show))  {
    	%>
	   		<tr>
	        <td colspan ="2" class="contentBgColor"><a href="javascript:popUpViewDisposalDate();">
	        	<fmt:message key='asset.label.viewDisposalDate'/>
	        	</a>
	        </td>
	        </tr>
			<tr><td colspan ="2" class="contentBgColor"><input class=button type=button name="print" value="Print" onClick="doPrint()"></td></tr>
		<%}%>
  </td>
  </tr>
</table>
</body>
</html>
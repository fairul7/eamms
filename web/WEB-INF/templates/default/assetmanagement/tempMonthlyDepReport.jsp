 <%@ page import="java.util.Collection, 
				com.tms.assetmanagement.model.DataCategory,
				com.tms.assetmanagement.model.AssetModule,
				com.tms.assetmanagement.model.DataMonthlyDepReport,
				com.tms.assetmanagement.model.DataItemsCategory,
				com.tms.assetmanagement.model.DataFinancialSetup,
				java.util.Iterator,
				java.text.DecimalFormat,  
				java.text.SimpleDateFormat,
				java.math.BigDecimal,
				java.util.Map,  
				java.util.StringTokenizer,          
                kacang.Application "%>
                  
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>
<c:set var="year" value="${widget.year}"/>
<c:set var="category" value="${widget.category}"/>

<c:if test="${!empty param.print}">
	<c:set var="print" value="${param.print}"/>
</c:if>  

<c:if test="${empty param.print}">
	<c:set var="print" value="false"/>
</c:if>  

<%
  String stryear =(String)pageContext.getAttribute("year");
  
//default moneyPattern
  String moneyPattern = "#,###,###,##0.00";
  String financialMonth = "0";
  
	Application app = Application.getInstance(); 
	AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);	
	Collection colFinancialDetail = mod.retrieveFinancialSetup();
	if (colFinancialDetail != null &&colFinancialDetail.size() > 0){
		for (Iterator iterator = colFinancialDetail.iterator(); iterator.hasNext();) {
			DataFinancialSetup obj = (DataFinancialSetup)iterator.next();
			 moneyPattern = obj.getCurrencyPattern();
			 financialMonth = obj.getFinancialMonth();
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
	
	String strReportDate = "";
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
	
	String strYear =  stryear.substring(2);
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
	pageContext.setAttribute("strReportDate", strReportDate);
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
				<tr>
    				<td colspan="17" align="right" valign="top"><img src="<c:out value="${widget.companyLogo}"/>"></td>  
				</tr>
				<%-- <tr>
   		 			<td colspan="17" align="left"><b><c:out value="${widget.companyName}"/></b></td>     
				</tr>	--%>	
				
				<tr>
					<td colspan="17" align="center" class="tableHeader" ><b><fmt:message key='asset.report.monthlyDepreciationReport'/> &nbsp; <%= stryear%> </b></td>
				</tr>
				
				<!-- Set Record Year -->
			  	<x:set name="monthlyDepReport.form" property="recordYear" value="${strReportDate}"/>	
			  			
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
		   	String year =(String)pageContext.getAttribute("year");
			String strCategory =(String)pageContext.getAttribute("category");
			StringTokenizer tokenizer = new StringTokenizer(strCategory, ",");
			String category ="";
			
			String [] strFieldName = {"totalCost", "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug","sept", "oct", "nov", "dece", "totalDepreciation"};
  			double [] dmonthlyDep = new double[14];
  			double [] dmonthlyGrandTotal = new double[14];
  			double [] dNewGrandTotal = new double[13];			
		   
		  	Collection coldata = null;		
	 		Map mapItem = null;	
	 		Map mapCategory = null;
	 		String strCurrentCategoryid;
	 		String strPreviousCategoryid = "";
		
		   	int iItems = 0;
	 		int iTotalItems = 0;	 
			 			    
		   if (year != null && !year.equals("")&& strCategory != null && !strCategory.equals("")){
		   		while(tokenizer.hasMoreTokens())
             	{
             		category = tokenizer.nextToken();    		   		   			
					coldata = (Collection)mod.retrieveMonthDepReport(Integer.parseInt(year), category);				
					if ( coldata != null && coldata.size() > 0){
						%>
			  			<x:set name="monthlyDepReport.form" property="showPrintViewDisposal" value="true"/>	
			  			<%
						for (java.util.Iterator iterator = coldata.iterator(); iterator.hasNext();) {
	 					DataMonthlyDepReport object = (DataMonthlyDepReport)iterator.next();
	 					strCurrentCategoryid = object.getCategoryId().toString();
	 					
	 					if (!strCurrentCategoryid.equals(strPreviousCategoryid)){
	 						//get number of items for each category
	 						iTotalItems = mod.countMonthDepReport(Integer.parseInt(year), strCurrentCategoryid);	 						
	 						iItems = 0;	 													
	 						mapCategory = mod.retrieveCategory(strCurrentCategoryid);
	 						strPreviousCategoryid = strCurrentCategoryid;
	 						 
	 						 if (mapCategory != null){ 
	 							String	strCategoryName = mapCategory.get("categoryName").toString();
								float fDepreciation = Float.parseFloat(mapCategory.get("depreciation").toString());
								String depreciation = new DecimalFormat("0").format((float)fDepreciation);
								%>
								
								<!--- Category and Depreciation (%) --->	
								<tr colspan="12" class="title">
									<td align="left"><b><%= strCategoryName%>&nbsp;(<%= depreciation%>%)</b></td> 		
								</tr>	
															
								<%
								}//end of map is nt null
	 						 }//end of same  category id
	 			//Listing details
	 			mapItem = (Map) mod.retrieveItems (object.getItemId().toString(), object.getCategoryId().toString());
	 			iItems ++ ;         
 		 		double[] dValue= {object.getJan(), object.getFeb(), object.getMar(),object.getApr(), object.getMay(), object.getJun(),
 		 				object.getJul(), object.getAug(), object.getSept(), object.getOct(),object.getNov(),object.getDece()}; 
 		 		
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
		 		<td align="left" class="content"><%= mapItem.get("itemName")%></td>
		 		<td align="center" class="content"><%= new DecimalFormat("0").format((float)object.getAssetUnit())%></td>
		 		<td align="center" class="content"><%= new SimpleDateFormat("dd/MM/yyyy").format(object.getAssetDate())%></td>
		 		<td align="right" class="content"><%= new DecimalFormat(moneyPattern).format(object.getTotalCost())%></td>
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
		 		<td align="right" class="content"><%= new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(object.getTotalDepreciation()))%></td>
		 </tr>	
		 	<%
	 	 				
	 	//calculate SubTotal	
	 	if (iItems == iTotalItems){	
	 	
  			for (int i=0; i<14; i++)
  				dmonthlyDep[i] = mod.getSumOfColumn(Integer.parseInt((String)year),  object.getCategoryId().toString(), strFieldName[i]);
  				
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
	 	
	 	}//end of number of item					
			
			} //end of for loop
					
			//Calculate Grand Total			
  			
  			for (int i=0; i<14; i++)		
  				dmonthlyGrandTotal[i] += mod.getSumOfColumn(Integer.parseInt((String)year), category, strFieldName[i]); 
  				  			
 		 		int c = Integer.parseInt(financialMonth) + 1 ;
 		 		if (c > 11)
 		 			c=0;
 		 		for(int t=0; t<12; t++){
 		 		dNewGrandTotal[t] = dmonthlyGrandTotal[c+1];
 					c++;
 					if (c >11)
 		 				c=0;
 		 		} 		
  				
  			}//while StringTonizer is nt null
  			
 			}// end of if coldata is not null
 			
 			if (dmonthlyGrandTotal[12] > 0){
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
  			</tr> 			
   		<%
  			String strPrint = (String)pageContext.getAttribute("print");
  			if ("false".equals(strPrint)){
  		%> 
  			<tr><td colspan="20" align = "center">
  			<x:display name="${form.btnSave.absoluteName}"/>
  			<input type="button" class="button" value="<fmt:message key='asset.report.exportTocsv'/>" onclick="location='exportMonthlyReportCSV.jsp'"></td>
  			</tr>
	 		
 			<%
 			}
 			}//if total has its value
		}   //end of if attribute not null	    
   	
		%>        	
		
		 <jsp:include page="../form_footer.jsp" flush="true"/>          
  	        </table>
  	        
  	      </td>
  	    </tr>
  	  </table>	
	 <script>
    
     function popUpViewDisposalDate()
     {		
       myWin = open("<c:url value="viewDisposalDate.jsp?monthly=true"/>","View","width=800,height=500,toolbar=no,menubar=yes,resizable=yes,scrollbars=yes");
     } 
    
    function doPrint() {
   		myWin = open("<c:url value="printMonthlyDepReport.jsp?category=${param.category}&year=${param.year}&print=true"/>","Report","width=800,height=500,toolbar=no,menubar=no,resizable=yes,scrollbars=yes");
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
 <%@ page import="java.util.Collection, 
				com.tms.assetmanagement.model.DataDisposal,
				com.tms.assetmanagement.model.AssetModule,
				com.tms.assetmanagement.model.DataFinancialSetup,				
				java.text.DecimalFormat,  
				java.text.SimpleDateFormat,
				java.util.Map,
				java.util.Date,   
				java.util.Iterator,
				java.text.SimpleDateFormat, 
				java.util.StringTokenizer,        
                kacang.Application "%>
<%@include file="/common/header.jsp"%>
<x:permission permission="com.tms.assetmanagement.manageAssetPermission" module="com.tms.assetmanagement.model.AssetModule" url="noPermission.jsp">

<c:if test="${!empty param.monthly}">
	<c:set var="year" value="${widgets['monthlyDepReport.form'].year}" />
	<c:set var="category" value="${widgets['monthlyDepReport.form'].category}" />
</c:if>     		    	  
			
<c:if test="${!empty param.yearly}">
	<c:set var="year" value="${widgets['fixedAssetReport.form'].year}" />
	<c:set var="category" value="${widgets['fixedAssetReport.form'].category}" />
</c:if>

<html>
<head>
<title><fmt:message key='asset.label.fixedAssetReport'/></title>
<style>
.tableBackground{
	font-size: 10pt; font-family: Arial, Helvetica, sans-serif; background-color=#666666;
}
.tableHeader{
	font-size: 8pt; font-family: Arial, Helvetica, sans-serif; background-color=#99CCFF;
}
.tableRow{
	font-size: 7pt; font-family: Arial, Helvetica, sans-serif; background-color=#FFFFFF;
}
.contentTitleFont {
	font-size: 12pt; font-family: Arial, Helvetica, sans-serif;
}
.contentBgColor {
	font-size: 10pt; font-family: Arial, Helvetica, sans-serif; background-color=#F0F8FF;
}
.content{
	font-size: 9pt; font-family: Arial, Helvetica, sans-serif; background-color=#F0F8FF;
}

</style>
</head>
<body>
<head>
  <title>Asset Notification</title>
  <link rel="stylesheet" href="/ekms/images/ekp2005/default.css">
</head>
    <jsp:include page="includes/header.jsp"/>
        <table width="100%" border="0" cellspacing="0" cellpadding="5" class="classBackground">    	
   
        <% 
      	String strYear = (String)pageContext.getAttribute("year");
		String category = (String)pageContext.getAttribute("category");
		
		DataFinancialSetup obj = null;
		Map mapDisposal = null;			 
	  
		Application app = Application.getInstance(); 
	 	AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);	 	
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		  
		 	//retrieve financial month from financial setup
			Collection colFinancialDetail = mod.retrieveFinancialSetup();
			if (colFinancialDetail != null && colFinancialDetail.size() > 0){
				for (Iterator iterator = colFinancialDetail.iterator(); iterator.hasNext();) {
					obj = (DataFinancialSetup)iterator.next();
				}
			}	
			
			//get Month
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
				
			//Report Header 
			int iNextYear = 0;
			String strReportDate;
			int iEndingMonth = Integer.parseInt(obj.getFinancialMonth()) + 1;
			int iStartingMonth = iEndingMonth + 1;
			if (iStartingMonth > 12)
				strReportDate = getMonth[0] + " " + new Integer(strYear).toString() +" - " + getMonth[11] + " " + new Integer(strYear).toString();
			else{
				iNextYear = Integer.parseInt(strYear) + 1 ;
				strReportDate = getMonth[iStartingMonth - 1]+" "+ new Integer(strYear).toString()+" - "+ getMonth[iEndingMonth-1]+" " +  new Integer(iNextYear).toString();
			}
			 %>
				<tr><td colspan="7"><font size="3"><b><fmt:message key='asset.label.viewDisposalDate'/>: &nbsp;<%= strReportDate %> </b></font></td></tr>
			   	<tr>
					<th class="tableHeader" align="center"></th>
					<th class="tableHeader" align="center"><fmt:message key='asset.label.assetName'/></th>
					<th class="tableHeader" align="center"><fmt:message key='asset.label.categoryName'/></th>
					<th class="tableHeader" align="center"><fmt:message key='asset.label.datePurchased'/></th>
					<th class="tableHeader" align="center"><fmt:message key='asset.label.totalUnitPurchased'/></th>
					<th class="tableHeader" align="center"><fmt:message key='asset.label.disposaldate'/></th>
					<th class="tableHeader" align="center"><fmt:message key='asset.label.disposalQty'/></th>
					<th class="tableHeader" align="center"><fmt:message key='asset.label.reasonDisposal'/></th>
				</tr>

			 <%		
				
				//get Opening Year and Closing year	
				
				int iOpeningYear =  Integer.parseInt(strYear);
			
				int iClosingYear = iOpeningYear;
				int iDayOfMonth = 0;
				Date dateOpeningYear = null;
				Date dateClosingYear = null;	
				String strCategory = "";

				if (iStartingMonth > 12){//Financial Year ends on the month of December -> eg.Jan 2000 - Dec 2000
					iStartingMonth = 1;				
					dateOpeningYear =  sdf.parse(new Integer(iOpeningYear).toString()+"/"+ new Integer(iStartingMonth).toString()+"/1" );
					iDayOfMonth = mod.getDaysInMonth (sdf.parse(new Integer(iClosingYear).toString()+"/"+ new Integer(iEndingMonth).toString()+"/1"));
					dateClosingYear = sdf.parse(new Integer(iClosingYear).toString()+"/"+ new Integer(iEndingMonth).toString()+"/" + new Integer(iDayOfMonth).toString());
					}
				else {
					iClosingYear = iOpeningYear + 1;
					dateOpeningYear =  sdf.parse(new Integer(iOpeningYear).toString()+"/"+ new Integer(iStartingMonth).toString()+ "/1");
					iDayOfMonth = mod.getDaysInMonth(sdf.parse(new Integer(iClosingYear).toString()+"/"+ new Integer(iEndingMonth).toString()+ "/1"));
					dateClosingYear = sdf.parse(new Integer(iClosingYear).toString()+"/"+ new Integer(iEndingMonth).toString()+ "/" + new Integer(iDayOfMonth).toString());	
				}
		
	 		if(category != null && !category.equals("")&& !strYear.equals("")){
	 			
	 		int count = 0; 		
			StringTokenizer tokenizer = new StringTokenizer(category, ",");
	 		while(tokenizer.hasMoreTokens())
             {
             	strCategory = tokenizer.nextToken();    
		   		count++;   
	 			Collection colDisposal = mod.getDisposalDateByYear(strCategory, dateOpeningYear, dateClosingYear );
	 			if (colDisposal != null && colDisposal.size() > 0){
		 			for(Iterator iterator = colDisposal.iterator(); iterator.hasNext();){							
						Map tempMap = (Map) iterator.next();
						if(tempMap.get("disposalId")!= null){
				%>
				<tr  class="contentBgColor">
					<td align="left" class="content"><%= count %></td>
					<td align="left" class="content"><%= tempMap.get("itemName")%></td>
					<td align="left" class="content"><%= tempMap.get("categoryName")%></td>
					<td align="center" class="content"><%= new SimpleDateFormat("dd/MM/yyyy").format((Date)tempMap.get("datePurchased"))%></td>
					<td align="center" class="content"><%= new DecimalFormat("0").format(tempMap.get("itemQty"))%></td>
					<td align="center" class="content"><%= new SimpleDateFormat("dd/MM/yyyy").format((Date)tempMap.get("dateDisposal"))%></td>				
					<td align="center" class="content"><%= new DecimalFormat("0").format(tempMap.get("disposalQty"))%></td>
					<td align="left" class="content"><%= tempMap.get("disposalReason")%></td>
				</tr>
		
				<%
						}
					}
				}	 		
	 		}
	 	}
	 		
        %> 
    </table>
    
    
    <jsp:include page="includes/footer.jsp"/>
</body>
</html>
</x:permission>
    
<%@ page import="java.util.Collection, 
				com.tms.assetmanagement.model.AssetModule,
				com.tms.assetmanagement.model.DataFixedAssetReport,
				com.tms.assetmanagement.model.DataFinancialSetup,
				java.util.Iterator,
				java.text.DateFormat,
				java.util.Calendar,
				com.tms.util.csv.CSVPrinterUtil,
				java.text.DecimalFormat,  
				java.text.SimpleDateFormat,
				java.util.Map, 
				java.util.StringTokenizer,          
                kacang.Application"%>
                
<%@include file="/common/header.jsp"%>
<x:permission permission="com.tms.assetmanagement.manageAssetPermission" module="com.tms.assetmanagement.model.AssetModule" url="noPermission.jsp">

<c:set var="year" value="${widgets['fixedAssetReport.form'].year}" />
<c:set var="category" value="${widgets['fixedAssetReport.form'].category}" />
<%

	//default moneyPattern
	  String moneyPattern = "#,###,###,##0.00";
	  String currencySymbol = "RM";
	  String financialMonth = "0";
	  CSVPrinterUtil csvp = new CSVPrinterUtil();
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
				String stryear = (String)pageContext.getAttribute("year");
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
				
				csvp.write(new String[]{Application.getInstance().getMessage("asset.report.fixedAssetReport","Fixed Asset Listing Report")+ " " + strReportDate});
				csvp.writeln();
				csvp.write(new String[]{""});
				csvp.write(new String[]{""});
				csvp.write(new String[]{""});
				csvp.write(new String[]{""});
				csvp.write(new String[]{""});
				csvp.write(new String[]{Application.getInstance().getMessage("asset.report.costField","COST")});
				csvp.write(new String[]{""});
				csvp.write(new String[]{""});
				csvp.write(new String[]{Application.getInstance().getMessage("asset.report.accumDepn","ACCUMULATED DEPRECIATION")});
				csvp.write(new String[]{""});
				csvp.writeln();
				csvp.write(new String[]{Application.getInstance().getMessage("asset.report.description","Description")});
				csvp.write(new String[]{Application.getInstance().getMessage("asset.report.unit","Unit")});
				csvp.write(new String[]{Application.getInstance().getMessage("asset.report.date","Date")});
				csvp.write(new String[]{Application.getInstance().getMessage("asset.report.balbf","Bal b/f")});
				csvp.write(new String[]{Application.getInstance().getMessage("asset.report.addition","Addition")});
				csvp.write(new String[]{Application.getInstance().getMessage("asset.report.disposal","Disposal")});
				csvp.write(new String[]{Application.getInstance().getMessage("asset.report.balcf","Bal c/f")});
				csvp.write(new String[]{Application.getInstance().getMessage("asset.report.balbf","Bal b/f")});
				csvp.write(new String[]{Application.getInstance().getMessage("asset.report.dCharge","D.charge")});
				csvp.write(new String[]{Application.getInstance().getMessage("asset.report.disposal","Disposal")});
				csvp.write(new String[]{Application.getInstance().getMessage("asset.report.balcf","Bal c/f")});
				csvp.write(new String[]{Application.getInstance().getMessage("asset.report.nbv","NBV")});
				csvp.writeln();
				csvp.write(new String[]{""});
				csvp.write(new String[]{""});
				csvp.write(new String[]{""});
				csvp.write(new String[]{currencySymbol});
				csvp.write(new String[]{currencySymbol});	
				csvp.write(new String[]{currencySymbol});
				csvp.write(new String[]{currencySymbol});
				csvp.write(new String[]{currencySymbol});	
				csvp.write(new String[]{currencySymbol});
				csvp.write(new String[]{currencySymbol});
				csvp.write(new String[]{currencySymbol});	
				csvp.write(new String[]{currencySymbol});
				csvp.writeln();			
				
			 
				String strTo = (String)pageContext.getAttribute("year");
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
									
									csvp.write(new String[]{strCategoryName + " ("+ depreciation + "%)"});
									csvp.writeln();
								
									}//end of map is nt null	 						 	
		 					}//end if different id
		 			//Listing details
		 			mapItem = (Map) mod.retrieveItems (obj.getItemId().toString(), obj.getCategoryId().toString());
		 			iItems ++ ;
		 			
					csvp.write(new String[]{mapItem.get("itemName").toString()});
					csvp.write(new String[]{ new DecimalFormat("0").format((float)obj.getAssetUnit())});
					csvp.write(new String[]{new SimpleDateFormat("dd/MM/yyyy").format(obj.getAssetDate())});
					csvp.write(new String[]{(obj.getCostBalBf()== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getCostBalBf())))});
					csvp.write(new String[]{ (obj.getCostAdditon()== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getCostAdditon())))});
					csvp.write(new String[]{(obj.getCostDisposal()== 0 ? "-" : "("+new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getCostDisposal()))+")")});
					csvp.write(new String[]{new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getCostBalCf()))});
					csvp.write(new String[]{(obj.getAccumDepnBalBf()== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getAccumDepnBalBf())))});
					csvp.write(new String[]{(obj.getAccumDepnDCharge()== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getAccumDepnDCharge())))});					
					csvp.write(new String[]{(obj.getAccumDepnDisposal()== 0 ? "-" :"("+ new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getAccumDepnDisposal()))+")")});							
					csvp.write(new String[]{ new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getAccumDepnBalCf()))});
					csvp.write(new String[]{new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(obj.getNbv()))});
					csvp.writeln();	
					csvp.writeln();	
		 			
		 	//calculate SubTotal	
		 	if (iItems == iTotalItems){		
		 		 	
	  			String [] strFieldName = {"costBalBf", "costAdditon", "costDisposal", "costBalCf", "accumDepnBalBf"
	  			, "accumDepnDCharge", "accumDepnDisposal", "accumDepnBalCf", "nbv"};
	  			double [] dFixedAsset = new double[strFieldName.length];
	  			for (int i=0; i<dFixedAsset.length; i++)
	  				dFixedAsset[i] = mod.getAssetSumOfColumn(iToYear, obj.getCategoryId().toString(), strFieldName[i]);
	  			
	  			csvp.write(new String[]{Application.getInstance().getMessage("asset.report.subtotal","SubTotal")});
	  			csvp.write(new String[]{""});
	  			csvp.write(new String[]{""});
	  			csvp.write(new String[]{(dFixedAsset[0]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[0])))});
	  			csvp.write(new String[]{ (dFixedAsset[1]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[1])))});
	  			csvp.write(new String[]{(dFixedAsset[2]== 0 ? "-" : "("+new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[2]))+")")});
	  			csvp.write(new String[]{new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[3]))});
	  			csvp.write(new String[]{(dFixedAsset[4]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[4])))});
	  			csvp.write(new String[]{(dFixedAsset[5]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[5])))});
	  			csvp.write(new String[]{(dFixedAsset[6]== 0 ? "-" :"("+ new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[6]))+")")});
	  			csvp.write(new String[]{new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[7]))});
	  			csvp.write(new String[]{new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dFixedAsset[8]))});
	  			csvp.writeln();
	  			csvp.writeln();
	 	
	  		}// end of items(subTotal)
		 	
		 		}//end for loop
		 	
		
		 	//Calculate Grand Total
	
	  			for (int i=0; i<dGrandTotal.length; i++)
	  				dGrandTotal[i] += mod.getAssetSumOfColumn( iToYear, category, strFieldName1[i]); 
	  			}//while StringTonizer is nt null
	  						 		
		 			
		 		}//end if coldata is nt null! 
		 		
		 		if (iItems != 0 ){
					csvp.write(new String[]{Application.getInstance().getMessage("asset.report.grandtotal","Grand Total")});
					csvp.write(new String[]{""});
					csvp.write(new String[]{""});				
					csvp.write(new String[]{(dGrandTotal[0]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[0])))});
					csvp.write(new String[]{(dGrandTotal[1]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[1])))});
					csvp.write(new String[]{(dGrandTotal[2]== 0 ? "-" : "("+new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[2]))+")")});
					csvp.write(new String[]{ new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[3]))});
					csvp.write(new String[]{(dGrandTotal[4]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[4])))});
					csvp.write(new String[]{(dGrandTotal[5]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[5])))});
					csvp.write(new String[]{(dGrandTotal[6]== 0 ? "-" :"("+ new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[6]))+")")});
					csvp.write(new String[]{new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[7]))});
					csvp.write(new String[]{new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dGrandTotal[8]))});
				
	  		}
		 		Calendar calendar = Calendar.getInstance();
			    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-kkmmss");
	
			    String fileName = "FixedAssetListingReport_" + dateFormat.format(calendar.getTime()) + ".csv";
			    
			  //  response.setContentType("text/excel");
			    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
				}//end of IF attributes not null!
					
			%>
			
			<%= csvp.getOutputString() %>   
			
</x:permission>
				
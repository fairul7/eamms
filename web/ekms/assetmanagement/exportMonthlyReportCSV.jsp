<%@ page import="java.util.Collection, 
				java.text.SimpleDateFormat,
				java.text.DateFormat,
				java.util.Calendar,
				com.tms.assetmanagement.model.AssetModule,
				com.tms.assetmanagement.model.DataMonthlyDepReport,	
				com.tms.assetmanagement.model.DataFinancialSetup,
				com.tms.util.csv.CSVPrinterUtil,
				java.util.Map,
				java.text.DecimalFormat,
				java.util.Iterator, 
				java.util.StringTokenizer,         
                kacang.Application"%>
                
<%@include file="/common/header.jsp"%>
<x:permission permission="com.tms.assetmanagement.manageAssetPermission" module="com.tms.assetmanagement.model.AssetModule" url="noPermission.jsp">

<c:set var="year" value="${widgets['monthlyDepReport.form'].year}" />
<c:set var="category" value="${widgets['monthlyDepReport.form'].category}" />

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
	
		CSVPrinterUtil csvp = new CSVPrinterUtil();
		
		csvp.write(new String[]{Application.getInstance().getMessage("asset.report.monthlyDepreciationReport","Monthly Depreciation Report")+ " " + stryear});
		csvp.writeln();
		csvp.write(new String[]{Application.getInstance().getMessage("asset.report.description", "Description")});
		csvp.write(new String[]{Application.getInstance().getMessage("asset.report.unit", "Unit")});
		csvp.write(new String[]{Application.getInstance().getMessage("asset.report.date", "Date")});
		csvp.write(new String[]{Application.getInstance().getMessage("asset.report.cost", "Cost")});
		csvp.write(new String[]{setNewMonth[0]});
		csvp.write(new String[]{setNewMonth[1]});
		csvp.write(new String[]{setNewMonth[2]});
		csvp.write(new String[]{setNewMonth[3]});
		csvp.write(new String[]{setNewMonth[4]});
		csvp.write(new String[]{setNewMonth[5]});
		csvp.write(new String[]{setNewMonth[6]});
		csvp.write(new String[]{setNewMonth[7]});	
		csvp.write(new String[]{setNewMonth[8]});
		csvp.write(new String[]{setNewMonth[9]});
		csvp.write(new String[]{setNewMonth[10]});
		csvp.write(new String[]{setNewMonth[11]});
		csvp.write(new String[]{Application.getInstance().getMessage("asset.report.total", "Total")});
		csvp.writeln();
	
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
						 	//Category and Depreciation (%) 
							csvp.write(new String[]{strCategoryName+ " (" + depreciation + "%)"});
							csvp.writeln();															
						
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
		 		
		 		csvp.write(new String[]{mapItem.get("itemName").toString()});
		 		csvp.write(new String[]{new DecimalFormat("0").format((float)object.getAssetUnit())});
		 		csvp.write(new String[]{new SimpleDateFormat("dd/MM/yyyy").format(object.getAssetDate())});
		 		csvp.write(new String[]{new DecimalFormat(moneyPattern).format(object.getTotalCost())});
		 		csvp.write(new String[]{(dNewValue[0]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[0])))});
		 		csvp.write(new String[]{(dNewValue[1]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[1])))});
		 		csvp.write(new String[]{(dNewValue[2]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[2])))});
		 		csvp.write(new String[]{(dNewValue[3]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[3])))});
		 		csvp.write(new String[]{(dNewValue[4]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[4])))});
		 		csvp.write(new String[]{(dNewValue[5]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[5])))});
		 		csvp.write(new String[]{(dNewValue[6]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[6])))});
		 		csvp.write(new String[]{(dNewValue[7]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[7])))});
		 		csvp.write(new String[]{(dNewValue[8]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[8])))});
		 		csvp.write(new String[]{(dNewValue[9]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[9])))});
		 		csvp.write(new String[]{(dNewValue[10]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[10])))});
		 		csvp.write(new String[]{(dNewValue[11]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewValue[11])))});		 		
		 		csvp.write(new String[]{new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(object.getTotalDepreciation()))});
				csvp.writeln();
				csvp.writeln();
		 				
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
		 		csvp.write(new String[]{Application.getInstance().getMessage("asset.report.subtotal", "SubTotal")});
		 		csvp.write(new String[]{" "});
		 		csvp.write(new String[]{" "});
		 		csvp.write(new String[]{(dmonthlyDep[0]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dmonthlyDep[0])))}); 		 		
		 		csvp.write(new String[]{(dNewSubTotal[0]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[0])))});	
		 		csvp.write(new String[]{(dNewSubTotal[1]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[1])))});
		 		csvp.write(new String[]{(dNewSubTotal[2]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[2])))});
		 		csvp.write(new String[]{(dNewSubTotal[3]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[3])))});
		 		csvp.write(new String[]{(dNewSubTotal[4]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[4])))});
		 		csvp.write(new String[]{(dNewSubTotal[5]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[5])))});
	 			csvp.write(new String[]{(dNewSubTotal[6]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[6])))});
		 		csvp.write(new String[]{(dNewSubTotal[7]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[7])))});
		 		csvp.write(new String[]{(dNewSubTotal[8]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[8])))});	
		 		csvp.write(new String[]{(dNewSubTotal[9]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[9])))});	
		 		csvp.write(new String[]{(dNewSubTotal[10]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[10])))});	
		 		csvp.write(new String[]{(dNewSubTotal[11]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewSubTotal[11])))});	
		 		csvp.write(new String[]{new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dmonthlyDep[13]))});
		 		csvp.writeln();
				csvp.writeln();
		
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
				csvp.write(new String[]{Application.getInstance().getMessage("asset.report.grandtotal", "Grand Total")});
				csvp.write(new String[]{""});
				csvp.write(new String[]{""});
				csvp.write(new String[]{(dmonthlyGrandTotal[0]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dmonthlyGrandTotal[0])))}); 				
				csvp.write(new String[]{(dNewGrandTotal[0]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[0])))});
				csvp.write(new String[]{(dNewGrandTotal[1]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[1])))});
				csvp.write(new String[]{(dNewGrandTotal[2]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[2])))});
				csvp.write(new String[]{(dNewGrandTotal[3]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[3])))});
				csvp.write(new String[]{(dNewGrandTotal[4]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[4])))});
				csvp.write(new String[]{(dNewGrandTotal[5]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[5])))});
				csvp.write(new String[]{(dNewGrandTotal[6]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[6])))});
				csvp.write(new String[]{(dNewGrandTotal[7]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[7])))});
				csvp.write(new String[]{(dNewGrandTotal[8]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[8])))});
				csvp.write(new String[]{(dNewGrandTotal[9]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[9])))});
				csvp.write(new String[]{(dNewGrandTotal[10]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[10])))});
				csvp.write(new String[]{(dNewGrandTotal[11]== 0 ? "-" : new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dNewGrandTotal[11])))});
				csvp.write(new String[]{new DecimalFormat(moneyPattern).format(mod.roundUpTo2DecimalPoints(dmonthlyGrandTotal[13]))});
				csvp.writeln();
		
			}//if total has its value
			Calendar calendar = Calendar.getInstance();
		    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-kkmmss");
	
		    String fileName = "monthlyDepreciationReport_" + dateFormat.format(calendar.getTime()) + ".csv";
		    
		  //  response.setContentType("text/excel");
		    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
	}   //end of if attribute not null	    
	 		
	%>  
	<%= csvp.getOutputString() %>     
	
</x:permission>

	         

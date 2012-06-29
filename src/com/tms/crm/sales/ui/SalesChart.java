package com.tms.crm.sales.ui;

import java.util.Date;
import java.util.Map;
import java.io.Serializable;
import org.jfree.chart.tooltips.CategoryToolTipGenerator;
import org.jfree.data.CategoryDataset;
import org.jfree.data.DefaultCategoryDataset;
import de.laures.cewolf.ChartPostProcessor;
import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.CategoryItemLinkGenerator;
import de.laures.cewolf.taglib.tags.Parameterized;
import kacang.util.UuidGenerator;
import kacang.Application;

public class SalesChart implements ChartPostProcessor, Parameterized, DatasetProducer, Serializable, CategoryItemLinkGenerator, CategoryToolTipGenerator {    
	private final String[] categories = {""};    
	private final String[] seriesNames = {Application.getInstance().getMessage("sfa.label.projection","Projection"), Application.getInstance().getMessage("sfa.label.currentOpp","Current Opp."), Application.getInstance().getMessage("sfa.label.weightedOpp","Weighted Opp."), Application.getInstance().getMessage("sfa.label.totalSales","Total Sales")};    
	private final float[] seriesValues = new float[seriesNames.length];
	private final Integer[] [] values = new Integer[seriesNames.length] [categories.length];
	
	public void processChart(Object chart, Map params) {}

	public void addParameter(String name, Object value) {}
	
	//bar chart
	public Object produceDataset(Map params) throws DatasetProduceException {		
		//reset seriesValues
		for (int i=0; i<seriesValues.length; i++) {seriesValues[i] = 0;}
		
		//set seriesValues
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();        
		if (params.get("value1") != null) {seriesValues[0] = Float.parseFloat(params.get("value1").toString());} else {System.out.println("Parameter value1 is empty!");}
		if (params.get("value2") != null) {seriesValues[1] = Float.parseFloat(params.get("value2").toString());} else {System.out.println("Parameter value2 is empty!");}
		if (params.get("value3") != null) {seriesValues[2] = Float.parseFloat(params.get("value3").toString());} else {System.out.println("Parameter value3 is empty!");}
		if (params.get("value4") != null) {seriesValues[3] = Float.parseFloat(params.get("value4").toString());} else {System.out.println("Parameter value4 is empty!");}
		for (int series=0; series<seriesNames.length; series++) {            
			for (int i=0; i<categories.length; i++) {                
				dataset.addValue(seriesValues[series], seriesNames[series], categories[i]);            
			}        
		}
		return dataset;    
	}
	
	public boolean hasExpired(Map params, Date since) {		        
		return (System.currentTimeMillis() - since.getTime())  > 5000;	
	}	
	
	public String getProducerId() {		
		return UuidGenerator.getInstance().getUuid();
	}
	
	public String generateLink(Object data, int series, Object category) {    
		return seriesNames[series];  
	}  
	
	public String generateToolTip(CategoryDataset arg0, int series, int arg2) {    
		return seriesNames[series];  
	}
}

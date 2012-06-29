package com.tms.collab.project.ui;

import java.util.Date;
import java.util.Map;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

public class ProjectReportChart implements ChartPostProcessor, Parameterized, DatasetProducer, CategoryToolTipGenerator, CategoryItemLinkGenerator, Serializable {    

//	 These values would normally not be hard coded but produced by
    // some kind of data source like a database or a file
    private String[] categories;
    private String[] seriesNames;
    private float[] seriesValue;
    public void processChart(Object chart, Map params) {}

	public void addParameter(String name, Object value) {}
	/**
	 *  Produces some random data.
	 */
    public Object produceDataset(Map params) throws DatasetProduceException {   	
    	DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
    	if (params.get("value") != null) {
    		String value=params.get("value").toString();
    		String []valuearray=value.trim().split(",,");
    		seriesValue = new float[valuearray.length];
    		for (int i = 0; i < valuearray.length; i ++) {
    			seriesValue[i]=Float.parseFloat(valuearray[i]);
    		}
    		
    		} else {seriesValue=new float[0];seriesValue[0]=0;}
    	if (params.get("name") != null) {
    		String value=params.get("name").toString();
    		seriesNames=value.trim().split(",,");   		
    		} else {seriesNames=new String[0];seriesNames[0]="";}
    	if (params.get("cat") != null) {
    		String value=params.get("cat").toString();
    		categories=value.trim().split(",,");   		
    		} else {categories=new String[1];categories[0]="";}
        for (int series = 0; series < seriesNames.length; series ++) {
            for (int i = 0; i < categories.length; i++) {              
                dataset.addValue(seriesValue[series], seriesNames[series], categories[i]);
            }
        }
        return dataset;
    }

    /**
     * This producer's data is invalidated after 5 seconds. By this method the
     * producer can influence Cewolf's caching behaviour the way it wants to.
     */
	public boolean hasExpired(Map params, Date since) {		
		return (System.currentTimeMillis() - since.getTime())  > 5000;
	}

	/**
	 * Returns a unique ID for this DatasetProducer
	 */
	public String getProducerId() {
		return "PageViewCountData DatasetProducer";
	}

    /**
     * Returns a link target for a special data item.
     */
    public String generateLink(Object data, int series, Object category) {
        return seriesNames[series];
    }

	/**
	 * @see org.jfree.chart.tooltips.CategoryToolTipGenerator#generateToolTip(CategoryDataset, int, int)
	 */
	public String generateToolTip(CategoryDataset arg0, int series, int arg2) {
		return seriesNames[series];
	}

}

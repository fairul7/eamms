package com.tms.collab.project.ui;

import java.util.*;
import javax.naming.*;
import java.io.Serializable;
import org.jfree.chart.tooltips.PieToolTipGenerator;
import org.jfree.data.CategoryDataset;
import org.jfree.data.PieDataset;
import org.jfree.data.DefaultPieDataset;
import de.laures.cewolf.DatasetProducer;

public class ProjectReportPie implements DatasetProducer, Serializable {

	private String[] seriesNames;
    private float[] seriesValue;
        /**
         * Produces a Dataset that provides data for a graphic chart.
         *
         * @param  params  Parameters passed in request
         * @return         Dataset used to render chart
         */
        public Object produceDataset(Map params) {
                DefaultPieDataset ds = null;

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

                       
                        
                        ds = new DefaultPieDataset();
                        for (int series = 0; series < seriesNames.length; series ++) {
                        	ds.setValue(seriesNames[series],seriesValue[series]);
                        }
            
                return ds;
        }


        /**
         *  Gets the producerId attribute of the SeriesData object
         *
         * @return    The producerId value
         */
        public String getProducerId() {
                return "PieDataProducer";
        }


        /**
         *  Checks whether curent chart image has expired.
         *
         * @param  params  Parameters passed in Request
         * @param  since   Date used for determining expiration
         * @return         Boolean indicating if Dataset has expired
         */
        public boolean hasExpired(Map params, java.util.Date since) {
                return false;
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



package com.tms.cms.taxonomy;

import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hpsf.PropertySet;
import org.apache.poi.hpsf.PropertySetFactory;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Jul 26, 2005
 * Time: 4:17:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class POIFSSummaryInformation implements POIFSReaderListener {

    protected String subject;
    protected String keywords;


    public void processPOIFSReaderEvent(POIFSReaderEvent event)
    {
        //System.out.println("process POIFS Reader event");
        SummaryInformation si = null;
        try
        {
            si = (SummaryInformation)
                 PropertySetFactory.create(event.getStream());
        }
        catch (Exception ex)
        {
            throw new RuntimeException
                ("Property set stream \"" +
                 event.getPath() + event.getName() + "\": " + ex);
        }
        subject = si.getSubject();
        keywords = si.getKeywords();
    }

    public String getSubject() {
        return subject;
    }

    public String getKeywords() {
        return keywords;
    }
}


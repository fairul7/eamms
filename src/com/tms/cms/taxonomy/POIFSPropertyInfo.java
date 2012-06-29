package com.tms.cms.taxonomy;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hpsf.Property;
import org.apache.poi.hpsf.PropertySet;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.Section;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.util.HexDump;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Jul 26, 2005
 * Time: 4:36:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class POIFSPropertyInfo {
    protected String keywords;
    protected String category;
    protected String subject;
    protected String fileName;
    protected String title;
    
    public POIFSPropertyInfo(String fileName) {
        this.fileName = fileName;

        try {
            POIFSReader reader = new POIFSReader();
            reader.registerListener(new POIFSReaderSummaryInfo(),"\005SummaryInformation");
            reader.registerListener(new POIFSReaderPropertySet(), "\005DocumentSummaryInformation");
            reader.read(new FileInputStream(this.fileName));
        }
        catch(Exception e) {

        }
    }

    public String getKeywords() {
        return keywords;
    }

    public String getSubject() {
        return subject;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    class POIFSReaderSummaryInfo implements POIFSReaderListener {


        public void processPOIFSReaderEvent(POIFSReaderEvent event) {
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
            title = si.getTitle();

        }
    }

    class POIFSReaderPropertySet implements POIFSReaderListener {
        public void processPOIFSReaderEvent(POIFSReaderEvent event) {
            PropertySet ps = null;
            try {
                ps = (PropertySet)PropertySetFactory.create(event.getStream());
            }
            catch(Exception e) {
                System.out.println("error : "+e.toString());
            }
            /* Print the number of sections: */
            //long sectionCount = ps.getSectionCount();

            /* Print the list of sections: */
            List sections = ps.getSections();
            //int nr = 0;
            
            //loop once, cos it will replace the value in the second loop
            Iterator i=sections.iterator();
            if(i.hasNext())
            {
                /* Print a single section: */
                Section sec = (Section) i.next();

                String s = HexDump.toHex(sec.getFormatID().getBytes());
                s = s.substring(0, s.length() - 1);

                /* Print the properties: */
                Property[] properties = sec.getProperties();
                for (int i2 = 0; i2 < properties.length; i2++)
                {
                    /* Print a single property: */
                    Property p = properties[i2];
                    int id = p.getID();
                    if (id==2){
                        category = p.getValue().toString();
                        break;
                    }
                }
            }
        }
    }
}

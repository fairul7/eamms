package com.tms.cms.taxonomy;

import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.hpsf.PropertySet;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.Section;
import org.apache.poi.hpsf.Property;
import org.apache.poi.util.HexDump;

import java.util.List;
import java.util.Iterator;


/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Jul 26, 2005
 * Time: 4:11:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class POIFSPropertySet implements POIFSReaderListener {

    public String category = "";

    public String getCategory() {
        return category;
    }
        public void processPOIFSReaderEvent (POIFSReaderEvent event)
        {
            PropertySet ps = null;
            try {
                ps = (PropertySet)PropertySetFactory.create(event.getStream());
            }
            catch(Exception e) {
                System.out.println("error : "+e.toString());
            }
            /* Print the number of sections: */
            long sectionCount = ps.getSectionCount();

            /* Print the list of sections: */
            List sections = ps.getSections();
            int nr = 0;

            for (Iterator i = sections.iterator(); i.hasNext();)
            {
                /* Print a single section: */
                Section sec = (Section) i.next();

                String s = HexDump.toHex(sec.getFormatID().getBytes());
                s = s.substring(0, s.length() - 1);

                /* Print the number of properties in this section. */
                //int propertyCount = sec.getPropertyCount();
                //System.out.println("      No. of properties: " + propertyCount);

                /* Print the properties: */
                Property[] properties = sec.getProperties();
                for (int i2 = 0; i2 < properties.length; i2++)
                {
                    /* Print a single property: */
                    Property p = properties[i2];
                    int id = p.getID();
                    if (id==2)
                        category = p.getValue().toString();
                    //long type = p.getType();
                    //Object value = p.getValue();
                    //System.out.println("      Property ID: " + id + ", type: " + type +
                    //    ", value: " + value);
                }
            }

        }
}

package com.tms.cms.document.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import kacang.util.Log;

import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.util.LittleEndian;

/**
 * Filters an MS Powerpoint document.
 */
public class PowerpointFilter extends DocumentFilter {

    ByteArrayOutputStream writer = new ByteArrayOutputStream();
    boolean isUnicode = false;

    public String filter(InputStream in) throws IOException {
        String content = "";
    	POIFSReader r = new POIFSReader();
        r.registerListener(new PowerpointReaderListener());
        r.read(in);
        if (getIsUnicode() == true) {
        	content = new String(writer.toByteArray(),"UTF-16LE");
            return content;
        } else {
        	content = new String(writer.toByteArray(),"ISO-8859-1");
        	return content;	
        }
    }

    class PowerpointReaderListener implements POIFSReaderListener {
        int filename = 1;

        public void processPOIFSReaderEvent(POIFSReaderEvent event) {
            try {
                if (!event.getName().equalsIgnoreCase("PowerPoint Document")){
                    return;
                }
                DocumentInputStream input = event.getStream();
                byte[] buffer = new byte[input.available()];
                input.read(buffer, 0, input.available());
                
                for (int i = 0; i < buffer.length - 20; i++) {
                    long type = LittleEndian.getUShort(buffer, i + 2);
                    long size = LittleEndian.getUInt(buffer, i + 4);

                    if (type == 4008){
                        writer.write(buffer, i + 4 + 1, (int) size + 3);
                        i = i + 4 + 1 + (int) size - 1;
                        setIsUnicode(false);
                    } else if (type == 4000){
                	    String strTempContent = new String(buffer, i + 6,(int) (size) + 2);
						byte bytes[] = strTempContent.getBytes();
						writer.write(bytes);
                        setIsUnicode(true);
                    }
                }                
        	} catch (Exception ex) {
                Log.getLog(getClass()).debug("Error parsing powerpoint document", ex);
                return;
            }
        }
    }

	public boolean getIsUnicode() {
		return isUnicode;
	}

	public void setIsUnicode(boolean isUnicode) {
		this.isUnicode = isUnicode;
	}
}
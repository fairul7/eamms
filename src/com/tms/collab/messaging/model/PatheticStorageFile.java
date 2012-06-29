package com.tms.collab.messaging.model;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.util.Log;

/**
 * A pathetic storage file to store whatever content that is passed into the
 * setInputStream(...) method. Please do not use this class, it is a 
 * phathetically created one, just to solve some messaging attachment cannot
 * be read cause its file is already somehow being deleted. (bug 2612)
 *
 * This storage file acts like a cache, when the constructor is called
 * it reads data and store them in bytes. This class is used in the
 * FilterActionEnum, with in a method (as a local memeber) it should be
 * eligibal for garbage collection after the end of method, the inputstream
 * should be closed by the caller (in this case the FilterActionEnum's 
 * calling method. 
 * 
 * This class, could be passed down the method call to be used as 
 * normal StorageFile because with bug2612, when the actual code that needs
 * to get input stream (the actual underlying file to get the input
 * stream from is gone), using this class as 'some sort of' wrapper is
 * to eliminate this problem.
 *
 *
 * DO NOT USE THIS CLASS (IT IS JUST TO FIX BUG2612)
 */
public class PatheticStorageFile extends StorageFile {

    private static final Log _log = Log.getLog(PatheticStorageFile.class);
    
    byte[] _contents = new byte[0];
    
    public PatheticStorageFile(String name, InputStream is) {
        setName(name);
        setInputStream(is);
    }
    
    /* (non-Javadoc)
     * @see kacang.services.storage.StorageFile#getData()
     */
    public byte[] getData() throws StorageException {
        return _contents;
    }
    
    /* (non-Javadoc)
     * @see kacang.services.storage.StorageFile#getInputStream()
     */
    public InputStream getInputStream() throws FileNotFoundException, StorageException {
        return new ByteArrayInputStream(_contents);
    }
    
    /* (non-Javadoc)
     * @see kacang.services.storage.StorageFile#setInputStream(java.io.InputStream)
     */
    public void setInputStream(InputStream inputStream) {
        if (inputStream == null) { return; }
        List bytes = new ArrayList();
        try {
        int a=inputStream.read();
        while(a >= 0) {
            bytes.add(new Byte((byte)a));
            a=inputStream.read();
        }
        if (bytes.size() > 0) {
            _contents = new byte[bytes.size()];
            int z = 0;
            for(Iterator i = bytes.iterator(); i.hasNext(); ) {
                Byte b = (Byte) i.next();
                _contents[z] = b.byteValue();
                z = z+1;
            }
        }
        }
        catch(Exception e) {
            _log.error(e.toString(), e);
        }
    }
}

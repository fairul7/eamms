package com.tms.collab.messaging.model;

import kacang.Application;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.util.Log;

import javax.activation.DataSource;
import javax.activation.MimetypesFileTypeMap;
import java.io.*;

/**
 * An implementation of Java Activation Framework's (JAF) DataSource. This is
 * to be used by the JavaMail API.
 */
public class StorageFileDataSource implements DataSource {
    private StorageFile storageFile;
    private ByteArrayOutputStream baos = null;

    public StorageFileDataSource(StorageFile storageFile) {
        this.storageFile = storageFile;

        baos = new ByteArrayOutputStream();
        try {
        	InputStream in = storageFile.getInputStream();
        	if (in == null) {
        		Application app = Application.getInstance();
        		StorageService storage = (StorageService)app.getService(StorageService.class);
        		storageFile = storage.get(storageFile);
        		in = storageFile.getInputStream();
        	}
            copy(in, baos);

		} catch (Exception e) {
			Log.getLog(getClass()).error("Error getting file attachment inputstream", e);
		}
    }

    public String getContentType() {
        MimetypesFileTypeMap mmp;
        String contentType;

        contentType = storageFile.getContentType();

        // if content type not specified in storageFile, detect it
        if (contentType == null) {
            mmp = new MimetypesFileTypeMap();
            contentType = mmp.getContentType(storageFile.getAbsolutePath());

            // just in case MimetypesFileTypeMap cannot determine contentType
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
        }
        return contentType;
    }

    public InputStream getInputStream() {
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    public String getName() {
        String name = storageFile.getName();
        return name;
    }

    public OutputStream getOutputStream() throws IOException {
        throw new IOException("getOutputStream() not supported in StorageFileDataSource");
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        synchronized (in) {
            synchronized (out) {

                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int bytesRead = in.read(buffer);
                        if (bytesRead == -1) {
                            break;
                        }
                        out.write(buffer, 0, bytesRead);
                    }
                }
                finally {
                    try {
                        in.close();
                    }
                    catch (IOException e) {
                        // ignore
                    }
                }
            }
        }
    }
}

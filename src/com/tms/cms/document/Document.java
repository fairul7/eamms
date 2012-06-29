package com.tms.cms.document;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.document.filter.DocumentFilter;
import com.tms.cms.document.filter.DocumentFilterFactory;
import com.tms.cms.profile.model.ContentProfileModule;
import com.tms.cms.profile.model.ProfiledContent;
import kacang.Application;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.util.Encryption;
import kacang.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Represents a Document (File).
 */
public class Document extends ContentObject implements ProfiledContent {

    private Properties properties;

    private transient StorageFile storageFile;

    public Document() {
        properties = new Properties();
    }

    public Class getContentModuleClass() {
        return DocumentModule.class;
    }

    public String getContents() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            properties.store(os, "");
            return os.toString();
        }
        catch (IOException e) {
            return properties.toString();
        }
    }

    public void setContents(String contents) {
        properties = new Properties();
        if (contents != null) {
            ByteArrayInputStream is = new ByteArrayInputStream(contents.getBytes());
            try {
                properties.load(is);
            }
            catch (IOException e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }
        }
    }

    public String getFileName() {
        return properties.getProperty("fileName");
    }

    public void setFileName(String fileName) {
        if (fileName != null)
            properties.setProperty("fileName", fileName);
        else
            properties.remove("fileName");
    }

    public String getFilePath() {
        return properties.getProperty("filePath");
    }

    public void setFilePath(String filePath) {
        if (filePath != null)
            properties.setProperty("filePath", filePath);
        else
            properties.remove("filePath");
    }

    public long getFileSize() {
        try {
            return Long.parseLong(properties.getProperty("fileSize"));
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    public void setFileSize(long fileSize) {
        properties.setProperty("fileSize", new Long(fileSize).toString());
    }

    public String getContentType() {
        return properties.getProperty("contentType");
    }

    public void setContentType(String contentType) {
        if (contentType != null)
            properties.setProperty("contentType", contentType);
        else
            properties.remove("contentType");
    }

    public StorageFile getStorageFile() {
        return storageFile;
    }

    public void setStorageFile(StorageFile storageFile) {
        this.storageFile = storageFile;
    }

    public String getIndexableContent() {

        String content = super.getIndexableContent() + "\n" +
                nullToEmpty(getFileName()) + "\n";

        // handle profiled content
        Application app = Application.getInstance();
        if (ContentProfileModule.isProfileEnabled()) {
            try {
                ContentProfileModule profileMod = (ContentProfileModule)app.getModule(ContentProfileModule.class);
                content += profileMod.getIndexableProfileData(getId(), getVersion()) + "\n";
            }
            catch (ContentException e) {
                Log.getLog(getClass()).error("Unable to retrieve profile data for document " + getId());
            }
        }

        // handle document contents
        DocumentFilter filter = DocumentFilterFactory.getDocumentFilter(getFileName(), getContentType());
        if (filter != null) {
            try {
                StorageFile file = new StorageFile(getFilePath());
                Application application = Application.getInstance();
                StorageService storage = (StorageService)application.getService(StorageService.class);
                InputStream in = storage.getInputStream(file);
                content += filter.filter(in);
            }
            catch(Exception e) {
                Log.getLog(getClass()).debug("Error filtering document " + getId(), e);
                //e.printStackTrace();
            }
        }

        return content;
    }

    protected String nullToEmpty(String str) {
        return (str != null) ? str : "";
    }

    public String getIdDirectory() {
    	String id = getId();
    	if (id != null) {
    		return getDocumentDirectory(id);
    	}
    	else {
    		return null;
    	}
    }

    /** 
     * Used to determine a subdirectory to store the document, to overcome OS limit of number of subdirectories per directory
     * @param id
     * @return
     */
    public static String getDocumentDirectory(String id) {
    	if (id != null) {
    		// implementation using MD5 hash
    		String hash = Encryption.encrypt(id);
    		String dir = hash.substring(0, 3) + "/" + id;
    		return dir;
    	}
    	else {
    		return null;
    	}
    }

}

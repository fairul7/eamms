package com.tms.cms.document;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.DefaultContentModuleDao;
import com.tms.cms.core.model.InvalidKeyException;
import com.tms.cms.core.model.ContentManager;
import kacang.Application;
import kacang.model.DaoException;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;

import java.util.Properties;

/**
 * Document Module DAO.
 */
public class DocumentModuleDao extends DefaultContentModuleDao {
	public static final String TABLE_NAME = "cms_content_document";

    protected String getTableName() {
        return TABLE_NAME;
    }

    protected String getStorageRoot() {
        return "documents";
    }

    protected Class getContentObjectClass() {
        return Document.class;
    }

    protected Properties properties() {
        Properties props = new Properties();
        props.putAll(DEFAULT_PROPERTIES);
        props.put("fileName", "fileName");
        props.put("filePath", "filePath");
        props.put("fileSize", "fileSize");
        props.put("contentType", "contentType");
        return props;
    }

    public int insert(ContentObject contentObject) throws InvalidKeyException, DaoException {
        // store uploaded file
        Document doc = (Document)contentObject;
        if (doc.getStorageFile() != null) {
            try {
                Application application = Application.getInstance();
                StorageService storage = (StorageService)application.getService(StorageService.class);
                StorageFile file = new StorageFile("/" + getStorageRoot() + "/" + doc.getIdDirectory() + "/" + doc.getVersion(), doc.getStorageFile());
                doc.setStorageFile(file);
                doc.setFilePath(file.getAbsolutePath());
                storage.store(file);
            }
            catch(Exception e) {
                throw new DaoException("Unable to save uploaded file for " + doc.getId() + ": " + e.toString());
            }
        }

        int result = super.insert(contentObject);

        return result;
    }

    public int update(ContentObject contentObject) throws DaoException {
        Application application = Application.getInstance();
        StorageService storage = (StorageService)application.getService(StorageService.class);
        ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
        try {
            Document doc = (Document)contentObject;

            if (doc.getFilePath() == null) {
                // delete existing file
                StorageFile dir = new StorageFile("/" + getStorageRoot() + "/" + doc.getIdDirectory() + "/" + doc.getVersion());
                storage.delete(dir);
            }
            else if (doc.getStorageFile() != null) {
                // delete existing file
                StorageFile dir = new StorageFile("/" + getStorageRoot() + "/" + doc.getIdDirectory() + "/" + doc.getVersion());
                storage.delete(dir);

                // store new file
                StorageFile file = new StorageFile("/" + getStorageRoot() + "/" + doc.getIdDirectory() + "/" + doc.getVersion(), doc.getStorageFile());
                doc.setStorageFile(file);
                doc.setFilePath(file.getAbsolutePath());
                storage.store(file);
            }

            if (!cm.isVersioningEnabled() && doc.getFilePath() != null && doc.getStorageFile() == null) {
            // versioning disabled, copy file from previous version

                // copy
                StorageFile oldFile = new StorageFile(doc.getFilePath());
                StorageFile newFile = new StorageFile("/" + getStorageRoot() + "/" + doc.getIdDirectory() + "/" + doc.getVersion(), doc.getFileName());
                if (!oldFile.getAbsolutePath().equals(newFile.getAbsolutePath())) {
                    storage.copy(oldFile, newFile);
                    doc.setFilePath(newFile.getAbsolutePath());
                }
            }
        }
        catch(Exception e) {
            throw new DaoException("Unable to save uploaded file for " + contentObject.getId() + ": " + e.toString());
        }

        int result = super.update(contentObject);

        return result;
    }

    public void delete(String key) throws DaoException {
        super.delete(key);
        // delete uploaded files
        try {
            Application application = Application.getInstance();
            StorageService storage = (StorageService)application.getService(StorageService.class);
            StorageFile file = new StorageFile("/" + getStorageRoot() + "/" + Document.getDocumentDirectory(key));
            storage.delete(file);
            file = new StorageFile("/" + getStorageRoot() + "/" + key);
            storage.delete(file);
        }
        catch(Exception e) {
            throw new DaoException("Unable to delete uploaded files for " + key + ": " + e.toString());
        }

    }

    public void delete(String key, String version) throws DaoException {
        super.delete(key, version);
        // delete uploaded file
        try {
            Application application = Application.getInstance();
            StorageService storage = (StorageService)application.getService(StorageService.class);
            StorageFile file = new StorageFile("/" + getStorageRoot() + "/" +  Document.getDocumentDirectory(key) + "/" + version);
            storage.delete(file);
            file = new StorageFile("/" + getStorageRoot() + "/" +  key + "/" + version);
            storage.delete(file);
        }
        catch(Exception e) {
            throw new DaoException("Unable to delete uploaded files for " + key + ": " + e.toString());
        }

    }

}

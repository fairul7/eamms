package com.tms.cms.document;

import com.tms.cms.core.model.ContentModule;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.ContentManager;
import com.tms.util.security.NtlmHttpFilter;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.services.storage.StorageServlet;
import kacang.util.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileNotFoundException;

public class DocumentStorageServlet extends StorageServlet {

    protected StorageFile processFile(HttpServletRequest request, HttpServletResponse response, String path) throws Exception {
        try {
            doNtlmLogon(request, response);

            Application application = Application.getInstance();
            StorageService storage = (StorageService) application.getService(StorageService.class);
            SecurityService security = (SecurityService)application.getService(SecurityService.class);
            User user = security.getCurrentUser(request);

            // locate content Id
            String id = null;
            String fileName = null;
            StringTokenizer st = new StringTokenizer(path, "/");
            if (st.hasMoreTokens()) {
                id = st.nextToken();
            }
            if (st.hasMoreTokens()) {
                fileName = st.nextToken();
            }


            // determine latest published version for document
            ContentPublisher publisher = (ContentPublisher)application.getModule(ContentPublisher.class);
            Document co = (Document)publisher.view(id, user);

            // check permission
            ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
            if (!contentManager.hasPermission(co, user.getId(), ContentManager.USE_CASE_VIEW)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }

            // determine URL for file
            ContentModule cm = (ContentModule)application.getModule(co.getContentModuleClass());
            DocumentModuleDao dao = (DocumentModuleDao)cm.getDao();
            String storageRoot = storage.getRootPath();
            String docRoot = dao.getStorageRoot();
            String latestVersion = co.getVersion();
            String filePath = co.getFilePath();

            if (filePath != null) {
                StringTokenizer st2 = new StringTokenizer(filePath, "/");
                st2.nextToken(); // document root
                String tmp = st2.nextToken(); // id
            	if (!tmp.startsWith("com.")) {
            		st2.nextToken();
            	}
                latestVersion = st2.nextToken();
            }
            String newPath = "/" + docRoot + "/" + co.getIdDirectory() + "/" + latestVersion + "/" + fileName;
            File tmpFile = new File(storageRoot, newPath);
            if (!tmpFile.exists()) {
            	newPath = "/" + docRoot + "/" + co.getId() + "/" + latestVersion + "/" + fileName;
            }

            // retrieve and return file
            StorageFile file = storage.get(new StorageFile(newPath));
            return file;
        }
        catch (DataObjectNotFoundException e) {
            Log.getLog(getClass()).error("Document not found: " + path, e);
            throw new FileNotFoundException();
        }
        catch (IllegalStateException e) {
            return null;
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving document " + path, e);
            return null;
        }
    }



    public static String getServerUrl(HttpServletRequest request) {
        String url = "http://" + request.getServerName();
        if (request.getServerPort() != 80) {
            url += ":" + request.getServerPort();
        }
        return url;
    }

    protected void doNtlmLogon(HttpServletRequest req, HttpServletResponse resp) {

        HttpSession ssn = req.getSession(true);
        if (ssn != null && ssn.getAttribute(NtlmHttpFilter.NTLM_AUTH_USERNAME) != null
                && !Boolean.valueOf((String) ssn.getAttribute(NtlmHttpFilter.NTLM_AUTH_ONCE)).booleanValue()) {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService) app.getService(SecurityService.class);

            String ntlmUsername = (String)ssn.getAttribute(NtlmHttpFilter.NTLM_AUTH_USERNAME);
            try {
                // login to kacang
                security.loginWithoutPassword(ssn, ntlmUsername);
            }
            catch (Exception e) {
                Log.getLog(getClass()).debug("Error logging in user " + req.getRemoteUser() + ": " + e.toString(), e);
            }
            ssn.setAttribute(NtlmHttpFilter.NTLM_AUTH_ONCE, "true");
        }

    }


}

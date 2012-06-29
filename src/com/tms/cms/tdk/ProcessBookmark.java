package com.tms.cms.tdk;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.model.DataObjectNotFoundException;
import kacang.Application;
import kacang.util.Log;
import com.tms.cms.bookmark.Bookmark;
import com.tms.cms.core.model.*;
import com.tms.cms.template.TemplateFrontServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProcessBookmark extends LightWeightWidget {

    public void onRequest(Event event) {
        try {
            Application application = Application.getInstance();
            HttpServletRequest request = event.getRequest();
            HttpServletResponse response = event.getResponse();
            String id = request.getParameter(TemplateFrontServlet.PARAMETER_KEY_CONTENT_ID);

            if (Bookmark.class.getName().equals(ContentUtil.getClassNameFromKey(id))) {
                // get object and redirect to URL
                User user = ((SecurityService)application.getService(SecurityService.class)).getCurrentUser(request);
                ContentPublisher cp = (ContentPublisher)application.getModule(ContentPublisher.class);
                Bookmark bookmark = (Bookmark)cp.view(id, user);
                String url = bookmark.getUrl();
                if (url != null && url.trim().length() > 0) {
                    // add hit statistic
                    boolean preview = ContentUtil.isPreviewRequest(request);
                    if (!preview) {
                        ContentReporter reporter = (ContentReporter)application.getModule(ContentReporter.class);
                        reporter.audit(bookmark.getId(), ContentManager.USE_CASE_VIEW, bookmark.getVersion(), user);
                    }

                    // redirect to url
                    response.sendRedirect(url);
                }
            }
        }
        catch (InvalidKeyException e) {
            Log.getLog(getClass()).error("Invalid bookmark key: " + e.toString());
        }
        catch (DataObjectNotFoundException e) {
            Log.getLog(getClass()).error("Bookmark not found: " + e.toString());
        }
        catch (ContentException e) {
            Log.getLog(getClass()).error("Bookmark content exception: " + e.toString());
        }
        catch (IOException e) {
            Log.getLog(getClass()).error("Bookmark IO exception: " + e.toString());
        }
    }

    public String getDefaultTemplate() {
        return "";
    }

}

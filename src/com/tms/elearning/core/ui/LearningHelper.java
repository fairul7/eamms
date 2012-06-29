package com.tms.elearning.core.ui;

import kacang.ui.Event;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DataObjectNotFoundException;

import java.io.Serializable;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.ui.ContentHelper;

/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: Mar 8, 2005
 * Time: 10:59:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class LearningHelper implements Serializable{

      public final static String REQUEST_KEY_CONTENT_ID = "com.tms.cms.core.ui.ContentObjectId";
      public final static String REQUEST_KEY_CONTENT = "com.tms.cms.core.ui.ContentObject";
      public final static String REQUEST_KEY_CONTENT_LIST = "com.tms.cms.core.ui.ContentObjectList";
      public final static String REQUEST_KEY_CONTENT_ACTION = "com.tms.cms.core.ui.ContentAction";

      public static String getId(Event evt) {
          // look in session
          String id = (String)evt.getRequest().getSession().getAttribute(REQUEST_KEY_CONTENT_ID);
          return id;
      }

      public static void setId(Event evt, String id) {
          // store in session
          evt.getRequest().getSession().setAttribute(REQUEST_KEY_CONTENT_ID, id);
      }

      public static ContentObject getContentObject(Event evt, String id) {
          // look in request
          ContentObject co = (ContentObject)evt.getRequest().getAttribute(REQUEST_KEY_CONTENT);
          if (co != null) {
              return co;
          }

          // determine id
          String coid = getId(evt);
          if (coid == null)
              coid = id;

          if (coid != null) {
              // get current user
              SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
              User user = security.getCurrentUser(evt.getRequest());


              // retrieve object from module
              try {
                  ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
                  co = cm.view(coid, user);

                  // check permission, ignore if root
                  if (ContentManager.CONTENT_TREE_ROOT_ID.equals(co.getId()) || cm.hasPermission(co, user.getId(), ContentManager.USE_CASE_PREVIEW))
                      evt.getRequest().setAttribute(REQUEST_KEY_CONTENT, co);
                  else
                      co = null;
              }
              catch (DataObjectNotFoundException e) {
                  Log.getLog(ContentHelper.class).error("Error loading content " + id, e);
                  co = null;
              }
              catch (Exception e) {
                  Log.getLog(ContentHelper.class).error("Error loading content " + id, e);
                  co = null;
              }
          }
          return co;
      }

}

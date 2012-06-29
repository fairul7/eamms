package com.tms.cms.comment;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorIn;
import kacang.model.operator.DaoOperator;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.SequencedHashMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;


/**
 * Represents a Comment. Comments are contained in a Commentary.
 */
public class Comment extends ContentObject {

    private Properties properties;

    public Comment() {
        properties = new Properties();
    }

    public void copyValues(ContentObject co) {
        try {
            BeanUtils.copyProperties(this, co);
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
    }

    public Class getContentModuleClass() {
        return CommentModule.class;
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

    public float getScore() {
        try {
            String score = properties.getProperty("score");
            return Float.parseFloat(score);
        }
        catch (Exception e) {
            return 0;
        }
    }

    public void setScore(float score) {
        properties.setProperty("score", new Float(score).toString());
    }

    public float getScale() {
        try {
            String scale = properties.getProperty("scale");
            return Float.parseFloat(scale);
        }
        catch (Exception e) {
            return 0;
        }
    }

    public void setScale(float scale) {
        properties.setProperty("scale", new Float(scale).toString());
    }

    private float average;

    public Collection getPublishedCommentList() {
        return getPublishedCommentList(null);
    }

    public Collection getPublishedCommentList(User user) {
        Collection results = new ArrayList();
        try {
            ContentManager contentManager = (ContentManager)Application.getInstance().getModule(ContentManager.class);

            float total = 0;
            int count = 0;
            float score = 0;
            Collection usernameList = new ArrayList();
            Collection tmpResults = new ArrayList();

            // get comments
            ContentObject root = this;
            String permissionId = (user != null) ? ContentManager.USE_CASE_VIEW : null;
            Collection objects = contentManager.viewListWithContents(null, new String[] {Comment.class.getName()}, null, null, null, null, null, null, Boolean.TRUE, Boolean.FALSE, null, null, false, 0, -1, permissionId, user);
            Map objectMap = new SequencedHashMap();
            for (Iterator j = objects.iterator(); j.hasNext();) {
                ContentObject co = (ContentObject) j.next();
                Comment comment = new Comment();
                comment.copyValues(co);
                objectMap.put(co.getId(), comment);
            }
            objectMap.put(root.getId(), root);

            // traverse list and populate tree
            for (Iterator i = objectMap.values().iterator(); i.hasNext();) {
                Comment comment = (Comment) i.next();
                if (comment.getParentId() != null) {
                    ContentObject parent = (ContentObject) objectMap.get(comment.getParentId());
                    if (parent != null) {
                        if (!parent.containsChild(comment))
                            parent.addChild(comment);
                        comment.setParent(parent);
                        score = comment.getScore();
                        if (score > 0) {
                            total += comment.getScore();
                            count++;
                        }
                        tmpResults.add(comment);
                        usernameList.add(comment.getAuthor());
                    }
                }
            }
            results = root.getChildren();
            average = Math.round(total / count);

            // populate user names
            DaoQuery q = new DaoQuery();
            q.addProperty(new OperatorIn("username", usernameList.toArray(), DaoOperator.OPERATOR_AND));
            SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
            Collection userList = security.getUsers(q, 0, -1, null, false);
            Map userMap = new HashMap();
            for (Iterator i=userList.iterator(); i.hasNext();) {
                User u = (User)i.next();
                userMap.put(u.getUsername(), u);
            }
            for (Iterator i=tmpResults.iterator(); i.hasNext();) {
                ContentObject co = (ContentObject)i.next();
                User u = (User)userMap.get(co.getAuthor());
                if (u != null) {
                    co.setProperty("user", u);
                }
            }
            tmpResults = null;
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving published comments", e);
        }
        return results;
    }

    public float getAverageScore() {
        return average;
    }

}

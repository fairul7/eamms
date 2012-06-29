package com.tms.cms.bookmark;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.profile.model.ProfiledContent;
import kacang.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * Represents a Bookmark (A link to a URL).
 */
public class Bookmark extends ContentObject implements ProfiledContent {

    private Properties properties;

    public Bookmark() {
        properties = new Properties();
    }

    public Class getContentModuleClass() {
        return BookmarkModule.class;
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

    public String getUrl() {
        return properties.getProperty("url");
    }

    public void setUrl(String url) {
        if (url != null)
            properties.setProperty("url", url);
        else
            properties.remove("url");
    }

    public String getTarget() {
        // HACK: use the 'related' field to represent the target
        return getRelated();
    }

    public void setTarget(String target) {
        // HACK: use the 'related' field to represent the target
        setRelated(target);
    }

}

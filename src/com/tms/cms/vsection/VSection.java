package com.tms.cms.vsection;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;
import kacang.Application;
import kacang.util.Log;

import java.util.*;

/**
 * Represents a Virtual Section.
 */
public class VSection extends ContentObject {

    public VSection() {
    }

    public Class getContentModuleClass() {
        return VSectionModule.class;
    }

    public String[] getIds() {
        String contents = getContents();
        if (contents == null) {
            return new String[] {""};
        }
        else {
            StringTokenizer st = new StringTokenizer(contents, ",;\n\t");
            String[] ids = new String[st.countTokens()];
            int i=0;
            while(st.hasMoreTokens()) {
                ids[i] = st.nextToken().trim();
                i++;
            }
            if (ids.length > 0)
                return ids;
            else
                return new String[] {""};
        }
    }

    public void setIds(String[] ids) {
        if (ids != null) {
            StringBuffer buffer = new StringBuffer();
            for (int i=0; i<ids.length; i++) {
                if (i > 0)
                    buffer.append(",");
                buffer.append(ids[i]);
            }
            setContents(buffer.toString());
        }
        else {
            setContents(null);
        }
    }

    /**
     * Convenience class to retrieve published content objects within this VSection
     * @return
     */
    public Collection getContentObjectList() {
        try {
            if (getIds() != null) {
                // get objects
                String[] keys = getIds();
                ContentPublisher cp = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);
                Collection list = cp.viewListWithContents(keys, null, null, null, Boolean.FALSE, null, false, 0, -1, null, null);

                // sort
                Map tmpMap = new HashMap();
                for (Iterator i=list.iterator(); i.hasNext();) {
                    ContentObject co = (ContentObject)i.next();
                    tmpMap.put(co.getId(), co);
                }
                Collection results = new ArrayList();
                for (int j=0; j<keys.length; j++) {
                    ContentObject co = (ContentObject)tmpMap.get(keys[j]);
                    if (co != null) {
                        results.add(co);
                    }
                }
                return results;
            }
            else {
                return new ArrayList();
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
            return new ArrayList();
        }
    }

    public boolean isIndexable() {
        return false;
    }

}

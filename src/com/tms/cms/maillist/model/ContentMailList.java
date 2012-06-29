package com.tms.cms.maillist.model;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.ekms.setup.model.SetupException;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.User;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ContentMailList extends MailList {
    private String header;
    private String footer;
    private List contentIdList;
    protected String serverUrl;

    public String getBody() {
        List list;
        StringBuffer sb;

        sb = new StringBuffer();

        if(header!=null) {
            sb.append(header);
        }

        // get server URL
        Application application = Application.getInstance();
        SetupModule setup = (SetupModule)application.getModule(SetupModule.class);
        try {
            serverUrl = setup.get("siteUrl");
        } catch (SetupException e) {
        }

        list = getUnsentContentIdList();
        for(int i=0; i<list.size(); i++) {
            appendContent(sb, (String)list.get(i));
        }

        if(footer!=null) {
            sb.append(footer);
        }

        return sb.toString();
    }

    private void appendContent(StringBuffer sb, String contentId) {

        ContentPublisher cp = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
        try {
            MailListModule module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
            User u = module.getModuleUser();
            ContentObject co = cp.view(contentId, u);

            if(isHtml()) {
                sb.append("<table border='0' width='100%' cellpadding='2' cellspacing='0'>");
                sb.append("<tr><td><b>" + co.getName() + "</b><br>");
                sb.append("<i>" + co.getAuthor()  + " " + co.getDate() + "</i></td></tr>");
                sb.append("<tr><td>" + co.getSummary());
                if (serverUrl != null) {
                    sb.append(" [ <a href='" + serverUrl + "/cms/content.jsp?id=" + co.getId() +  "'>Read More</a> ]");
                }
                sb.append("</td></tr>");
                sb.append("</table><br>");

            } else {
                sb.append(co.getName() + "\n");
                sb.append(co.getAuthor() + ", " + co.getDate() + "\n");
                sb.append(co.getSummary() + "\n");
                if (serverUrl != null) {
                    sb.append("URL: " + serverUrl + "/cms/content.jsp?id=" + co.getId() + "\n\n");
                }
            }

        } catch(ContentException e) {
            Log.getLog(this.getClass()).error(e.getMessage(), e);
        } catch(DataObjectNotFoundException e) {
            Log.getLog(this.getClass()).error(e.getMessage(), e);
        } catch(MailListException e) {
            Log.getLog(this.getClass()).error(e.getMessage(), e);
        }

    }

    public void clearSentContentIds() {
        MailListModule module;

        module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
        try {
            module.deleteSentItems(getId());

        } catch(MailListException e) {
            Log.getLog(this.getClass()).error(e.getMessage(), e);
        }
    }

    public int getSentContentIdCount() {
        MailListModule module;
        List sentList;

        module = (MailListModule) Application.getInstance().getModule(MailListModule.class);

        try {
            sentList = module.getSentItems(getId());
            return sentList.size();

        } catch(MailListException e) {
            Log.getLog(this.getClass()).error(e.getMessage(), e);
            return 0;
        }

    }

    public int getUnsentContentIdCount() {
        return getUnsentContentIdList().size();
    }

    public List getUnsentContentIdList() {
        MailListModule module;
        List unsentList;
        List sentList;

        unsentList = new ArrayList(getContentIdList());
        module = (MailListModule) Application.getInstance().getModule(MailListModule.class);

        try {
            sentList = module.getSentItems(getId());
            unsentList.removeAll(sentList);

        } catch(MailListException e) {
            Log.getLog(this.getClass()).error(e.getMessage(), e);
        }

        return unsentList;
    }

    public List getContentIdList() {
        return contentIdList;
    }

    public void setContentIdList(List contentIdList) {
        this.contentIdList = contentIdList;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    // === [ for DbUtil ] ======================================================
    /**
     * Getter for DbUtil.
     * @return
     */
    public String getContentIds() {
        List list = getContentIdList();
        StringBuffer sb;

        if(list == null) {
            return "";
        } else {

            sb = new StringBuffer();
            for(int i = 0; i < list.size(); i++) {
                sb.append((String) list.get(i));
                if(i + 1 < list.size()) {
                    sb.append(",");
                }
            }
            return sb.toString();
        }
    }

    /**
     * Setter for DbUtil.
     * @param s
     */
    public void setContentIds(String s) {
        StringTokenizer st;
        List list;

        list = getContentIdList();
        if(list == null) {
            list = new ArrayList();
            setContentIdList(list);
        } else {
            list.clear();
        }

        st = new StringTokenizer(s, ",");
        while(st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
    }

}

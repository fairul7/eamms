package com.tms.cms.maillist.model;

import com.tms.cms.article.Article;
import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.section.Section;
import com.tms.cms.vsection.VSection;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.User;
import kacang.util.Log;

import java.util.*;

public class ScheduledMailList extends ContentMailList {
    private boolean active;
    private Date startDate;
    private Date endDate;
    private String contentId;

    // scheduling data
    private String scheduleRepeatInterval;
    private int scheduleDay;
    private Date scheduleStartTime;
    private Date scheduleEndTime;

    private List unsubscribedEmailList;

    /**
     * Make this dynamic depending on scheduled rules.
     * @return
     */
    public List getContentIdList() {
        // do not process if contentId is null
        if(contentId==null) {
            return new ArrayList();
        }

        ContentPublisher cp = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
        ContentObject tree, co;
        VSection vs;
        Section s;
        ArrayList list = new ArrayList();

        try {
            MailListModule module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
            User u = module.getModuleUser();
            co = cp.view(contentId, u);

            if(co instanceof VSection) {
                // virtual section
                vs = (VSection) co;
                for(int i=0; i<vs.getIds().length; i++) {
                    list.add(vs.getIds()[i]);
                }

            } else {
                // normal section
                s = (Section) co;

                
                tree = cp.viewTree(s.getId(), new String[]{Section.class.getName(), Article.class.getName()}, null, (u == null ? null : u.getId()));
                
                
                addChildrenToList(list, tree);
            }

        } catch(ContentException e) {
            Log.getLog(this.getClass()).error(e.getMessage(), e);
        } catch(DataObjectNotFoundException e) {
            Log.getLog(this.getClass()).error(e.getMessage(), e);
        } catch(MailListException e) {
            Log.getLog(this.getClass()).error(e.getMessage(), e);
        }

        return list;
    }

    public void unsubscribe(String email) {
        getUnsubscribedEmailList().add(email);
    }

    public void subscribe(String email) {
        getUnsubscribedEmailList().remove(email);
        getRecipientList().add(email);
    }


    /**
     * NOTE: Recursive method.
     * @param list
     * @param co
     */
    private void addChildrenToList(ArrayList list, ContentObject co) {
        Collection childCo = co.getChildren();
        ContentObject co2;

        // add articles for this level first
        for(Iterator iter = childCo.iterator(); iter.hasNext();) {
            co2 = (ContentObject) iter.next();
            if("com.tms.cms.article.Article".equals(co2.getClassName())) {
                list.add(co2.getId());
            }
        }

        // traverse child sections
        for(Iterator iter = childCo.iterator(); iter.hasNext();) {
            co2 = (ContentObject) iter.next();
            if("com.tms.cms.section.Section".equals(co2.getClassName())) {
                addChildrenToList(list, co2);
            }
        }
    }

    // TODO: option to NOT recurse section. This method works already. Just do the DataObject to support (persist this option)
    public List getContentIdList2() {
        ContentPublisher cp = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
        ContentObject c;
        VSection vs;
        Section s;
        ArrayList list = new ArrayList();

        try {
            MailListModule module = (MailListModule) Application.getInstance().getModule(MailListModule.class);
            User u = module.getModuleUser();
            c = cp.view(contentId, u);

            if(c instanceof VSection) {
                // virtual section
                vs = (VSection) c;
                for(int i = 0; i < vs.getIds().length; i++) {
                    list.add(vs.getIds()[i]);
                }

            } else {
                // normal section
                s = (Section) c;

                Collection co = cp.viewList(null, new String[]{Article.class.getName()}, null, s.getId(), Boolean.FALSE, null, false, 0, -1, null, null);
                for(Iterator iter = co.iterator(); iter.hasNext();) {
                    c = (ContentObject) iter.next();
                    list.add(c.getId());
                }
            }

        } catch(ContentException e) {
            Log.getLog(this.getClass()).error(e.getMessage(), e);
        } catch(DataObjectNotFoundException e) {
            Log.getLog(this.getClass()).error(e.getMessage(), e);
        } catch(MailListException e) {
            Log.getLog(this.getClass()).error(e.getMessage(), e);
        }

        return list;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getScheduleDay() {
        return scheduleDay;
    }

    public void setScheduleDay(int scheduleDay) {
        this.scheduleDay = scheduleDay;
    }

    public Date getScheduleEndTime() {
        // TODO: unused field. remove this field!
        return getScheduleStartTime();
    }

    public void setScheduleEndTime(Date scheduleEndTime) {
        this.scheduleEndTime = scheduleEndTime;
    }

    public String getScheduleRepeatInterval() {
        return scheduleRepeatInterval;
    }

    public void setScheduleRepeatInterval(String scheduleRepeatInterval) {
        this.scheduleRepeatInterval = scheduleRepeatInterval;
    }

    public Date getScheduleStartTime() {
        return scheduleStartTime;
    }

    public void setScheduleStartTime(Date scheduleStartTime) {
        this.scheduleStartTime = scheduleStartTime;
    }

    public List getUnsubscribedEmailList() {
        return unsubscribedEmailList;
    }

    public void setUnsubscribedEmailList(List unsubscribedEmailList) {
        this.unsubscribedEmailList = unsubscribedEmailList;
    }

    /**
     * Getter for DbUtil.
     * @return
     */
    public String getUnsubscribedEmails() {
        List list = getUnsubscribedEmailList();
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
    public void setUnsubscribedEmails(String s) {
        StringTokenizer st;
        List list;

        list = getUnsubscribedEmailList();
        if(list == null) {
            list = new ArrayList();
            setUnsubscribedEmailList(list);
        } else {
            list.clear();
        }

        st = new StringTokenizer(s, ",");
        while(st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
    }
}

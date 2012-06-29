package com.tms.cms.maillist.model;

import com.tms.util.MailUtil;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.DefaultModule;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.services.scheduling.JobSchedule;
import kacang.services.scheduling.SchedulingException;
import kacang.services.scheduling.SchedulingService;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Encryption;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.util.*;

public class MailListModule extends DefaultModule {
    private static final String MODULE_USERNAME = "[mailinglist]";
    private static final String MODULE_PASSWORD = Encryption.encrypt("[mailinglist123]");

    private Log log = Log.getLog(getClass());

    public void init() {
        super.init();
    }

    // === [ use cases ] =======================================================
    public void sendMailingList(MailList ml) {
        MailLog mailLog;
        String content;

        mailLog = new MailLog();
        mailLog.setId(UuidGenerator.getInstance().getUuid());
        mailLog.setMessage("Unknown error while sending mailing list.");
        mailLog.setStartDate(new Date());

        mailLog.setHtml(ml.isHtml());
        mailLog.setMailListType(ml.getMailListType());
        mailLog.setSenderEmail(ml.getSenderEmail());
        mailLog.setRecipientsEmail(ml.getAbsoluteRecipientsEmail());
        mailLog.setSubject(ml.getSubject());

        try {

            if(ml.isReadyToSend()) {

                boolean hasContent = true;

                if(ml instanceof ContentMailList) {
                    ContentMailList cml = (ContentMailList) ml;
                    if(cml.getUnsentContentIdCount()==0) {
                        // nothing to send, don't send
                        mailLog.setMessage("No content item to send at this moment");
                        hasContent = false;
                    }
                }

                if(hasContent) {
                    content = ml.getContent();
                    mailLog.setContent(content);

                    StringTokenizer st = new StringTokenizer(ml.getAbsoluteRecipientsEmail(), ",;");
                    String email;
                    while(st.hasMoreTokens()) {
                        email = st.nextToken();
                        MailUtil.sendEmail(null, ml.isHtml(), ml.getSenderEmail(), email, null, null, ml.getSubject(), content);
                    }

                    mailLog.setMessage("Mailing list sent");

                    if(ml instanceof ContentMailList) {
                        // update sent items for ContentMailList
                        ContentMailList cml = (ContentMailList) ml;
                        List contentIdList = cml.getContentIdList();
                        for(int i=0; i<contentIdList.size(); i++) {
                            createSentItem(ml.getId(), (String)contentIdList.get(i));
                        }
                    }
                }

            } else {
                // mailing list not ready to send
                mailLog.setMessage("Mailing list not ready to be sent");
            }

        } catch(MailListException e) {
            log.error(e.getMessage(), e);
            mailLog.setMessage("Error sending mailing list. " + e.getMessage());

        } finally {
            // save mail log
            mailLog.setEndDate(new Date());

            try {
                createMailLog(mailLog);
            } catch(MailListException e) {
                log.error("Error creating mail log", e);
            }
        }

    }

    // === [ MailList ] ========================================================
    public void createMailList(MailList ml) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            dao.insertMailList(ml);

            if(ml.getMailListType()==MailList.MAIL_LIST_TYPE_SCHEDULED) {
                scheduleMailingList((ScheduledMailList)ml);
            }

        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public MailList getMailList(String id) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            return dao.selectMailList(id);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public int getMailListCount(int mailListType, String search) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            return dao.selectMailListCount(mailListType, search);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public Collection getMailLists(int mailListType, String search, String sort, boolean direction, int start, int maxRows) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            return dao.selectMailLists(mailListType, search, sort, direction, start, maxRows);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public void updateMailList(MailList ml) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            dao.updateMailList(ml);

            if(ml.getMailListType() == MailList.MAIL_LIST_TYPE_SCHEDULED) {
                scheduleMailingList((ScheduledMailList) ml);
            }

        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public void deleteMailList(String id) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            dao.deleteMailList(id);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    // === [ MailLog ] =========================================================
    public void createMailLog(MailLog mailLog) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            dao.insertMailLog(mailLog);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public MailLog getMailLog(String id) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            return dao.selectMailLog(id);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public int getMailLogCount(String search) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            return dao.selectMailLogCount(search);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public Collection getMailLogs(String search, String sort, boolean direction, int start, int maxRows) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            return dao.selectMailLogs(search, sort, direction, start, maxRows);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public void updateMailLog(MailLog mailLog) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            dao.updateMailLog(mailLog);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public void deleteMailLog(String id) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            dao.deleteMailLog(id);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }


    // === [ MailTemplate ] =========================================================
    public void createMailTemplate(MailTemplate template) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            dao.insertMailTemplate(template);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public MailTemplate getMailTemplate(String id) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            return dao.selectMailTemplate(id);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public int getMailTemplateCount(String search) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            return dao.selectMailTemplateCount(search);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public Collection getMailTemplates(String search, String sort, boolean direction, int start, int maxRows) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            return dao.selectMailTemplates(search, sort, direction, start, maxRows);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public void updateMailTemplate(MailTemplate template) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            dao.updateMailTemplate(template);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public void deleteMailTemplate(String id) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            dao.deleteMailTemplate(id);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }


    // === [ MailList history (sent contentIds) ] ==============================
    public void createSentItem(String mailListId, String contentId) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            dao.insertSentItem(mailListId, contentId);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public void insertSentItems(String mailListId, String[] contentIds) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            dao.insertSentItems(mailListId, contentIds);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public List getSentItems(String mailListId) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            return dao.selectSentItems(mailListId);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }

    public void deleteSentItems(String mailListId) throws MailListException {
        MailListDao dao = (MailListDao) getDao();

        try {
            dao.deleteSentItems(mailListId);
        } catch(MailListDaoException e) {
            throw new MailListException(e);
        }
    }


    // === [ private methods ] =================================================
    protected User getModuleUser() throws MailListException {
/*
        SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
        DaoQuery properties = new DaoQuery();
        properties.addProperty(new OperatorEquals("username", MailListModule.MODULE_USERNAME, DaoOperator.OPERATOR_AND));
        properties.addProperty(new OperatorEquals("password", MailListModule.MODULE_PASSWORD, DaoOperator.OPERATOR_AND));
        Collection users = null;

        try {
            users = ss.getUsers(properties, 0, 1, null, false);
            return (User) users.iterator().next();

        } catch(Exception e) {
            throw new MailListException("Exception while getting mailing list module's system user", e);
        }
*/
        // user not needed, removed
        return null;
    }

    protected void scheduleMailingList(ScheduledMailList ml) {
        JobSchedule jobSchedule;
        ScheduledMailListJob job;
        SchedulingService ss;
        String jobName, jobGroup;

        ss = (SchedulingService) Application.getInstance().getService(SchedulingService.class);
        if(ss==null) {
            log.error("Error getting scheduling service");
            return;
        }

        jobName = ml.getId();
        jobGroup = MailListModule.class.getName();

        // remove from scheduler first, if exist
        jobSchedule = null;
        try {
            jobSchedule = ss.getJobSchedule(jobName, jobGroup);

            try {
                ss.unscheduleJob(jobSchedule);
            } catch(SchedulingException e) {
                log.error("Error unscheduling mailing list job", e);
            }

        } catch(Exception e) {
            // job is not in schedule, no need to remove
        }

        if(ml.isActive()) {
            // add the scheduler, if active

            // create the schedule/trigger
            jobSchedule = new JobSchedule(jobName, ml.getScheduleRepeatInterval());
            jobSchedule.setGroup(jobGroup);
            jobSchedule.setRepeatCount(JobSchedule.REPEAT_INDEFINITELY);
            if(JobSchedule.WEEKLY.equals(ml.getScheduleRepeatInterval())) {
                jobSchedule.setDayOfWeek(ml.getScheduleDay());
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(ml.getScheduleStartTime());
            jobSchedule.setHour(cal.get(Calendar.HOUR_OF_DAY));
            jobSchedule.setMinute(cal.get(Calendar.MINUTE));

            // create the job
            job = new ScheduledMailListJob();
            job.setName(jobName);
            job.setGroup(jobGroup);
            job.getJobTaskData().put("id", ml.getId());

            // schedule the job
            try {
                ss.scheduleJob(job, jobSchedule);
            } catch(SchedulingException e) {
                log.error("Error scheduling mailing list job", e);
            }

        }

    }
}
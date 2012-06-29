package com.tms.cms.maillist.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.util.Log;
import kacang.Application;

import java.util.Iterator;

public class MailListUi extends Widget {

    private boolean showMenu = true;

    ComposedMailListForm composedMailListForm;
    ComposedMailListTable composedMailListTable;
    ContentMailListForm contentMailListForm;
    ContentMailListTable contentMailListTable;
    ScheduledMailListForm scheduledMailListForm;
    ScheduledMailListTable scheduledMailListTable;
    MailLogForm mailLogForm;
    MailLogTable mailLogTable;
    MailTemplateForm mailTemplateForm;
    MailTemplateTable mailTemplateTable;

    private String title;

    public void init() {
        removeChildren();

        composedMailListForm = new ComposedMailListForm("composedMailListForm");
        composedMailListForm.setMethod("POST");
        composedMailListForm.addEventListener(this);
        addChild(composedMailListForm);
        composedMailListForm.init();

        composedMailListTable = new ComposedMailListTable("composedMailListTable");
        composedMailListTable.addEventListener(this);
        addChild(composedMailListTable);
        composedMailListTable.init();

        contentMailListForm = new ContentMailListForm("contentMailListForm");
        contentMailListForm.setMethod("POST");
        contentMailListForm.addEventListener(this);
        addChild(contentMailListForm);
        contentMailListForm.init();

        contentMailListTable = new ContentMailListTable("contentMailListTable");
        contentMailListTable.addEventListener(this);
        addChild(contentMailListTable);
        contentMailListTable.init();

        scheduledMailListForm = new ScheduledMailListForm("scheduledMailListForm");
        scheduledMailListForm.setMethod("POST");
        scheduledMailListForm.addEventListener(this);
        addChild(scheduledMailListForm);
        scheduledMailListForm.init();

        scheduledMailListTable = new ScheduledMailListTable("scheduledMailListTable");
        scheduledMailListTable.addEventListener(this);
        addChild(scheduledMailListTable);
        scheduledMailListTable.init();

        mailLogForm = new MailLogForm("mailLogForm");
        mailLogForm.setMethod("POST");
        mailLogForm.addEventListener(this);
        addChild(mailLogForm);
        mailLogForm.init();

        mailLogTable = new MailLogTable("mailLogTable");
        mailLogTable.addEventListener(this);
        addChild(mailLogTable);
        mailLogTable.init();

        mailTemplateForm = new MailTemplateForm("mailTemplateForm");
        mailTemplateForm.setMethod("POST");
        mailTemplateForm.addEventListener(this);
        addChild(mailTemplateForm);
        mailTemplateForm.init();

        mailTemplateTable = new MailTemplateTable("mailTemplateTable");
        mailTemplateTable.addEventListener(this);
        addChild(mailTemplateTable);
        mailTemplateTable.init();

        showOnly(composedMailListTable);
    }

    public Forward actionPerformed(Event evt) {
        Log log = Log.getLog(this.getClass());
        log.debug("Event occurred for MailListUi. Widget=" +
        evt.getWidget().getAbsoluteName() +
        " Event=" + evt.getType());
        Application application = Application.getInstance();
        // === [ ComposedMailList events ] =====================================
        if(this.equals(evt.getWidget()) && "showComposedMailListTable".equals(evt.getType())) {
            // showComposedMailListTable
            showOnly(composedMailListTable);
            String title = application.getMessage("maillist.label.composedLists","Composed Lists");
            setTitle(title);

        } else if(composedMailListTable.equals(evt.getWidget()) && "sel".equals(evt.getType())) {
            // edit selected composedMailList
            composedMailListForm.setId(evt.getRequest().getParameter("id"));
            showOnly(composedMailListForm);
        } else if(composedMailListTable.equals(evt.getWidget()) && "newComposedMailList".equals(evt.getType())) {
            // newComposedMailList
            composedMailListForm.init();
            showOnly(composedMailListForm);
            String title = application.getMessage("maillist.label.addNewComposedList","Add New Composed List");
            setTitle(title);
        } else if(composedMailListForm.equals(evt.getWidget()) && "saved".equals(evt.getType())) {
            // saved composedMailListForm
            composedMailListForm.setId(composedMailListForm.getId());
            composedMailListForm.setFormSaved(true);
            showOnly(composedMailListForm);
        // === [ ContentMailList events ] ======================================
        } else if(this.equals(evt.getWidget()) && "showContentMailListTable".equals(evt.getType())) {
            // showContentMailListTable
            showOnly(contentMailListTable);
            String title = application.getMessage("maillist.label.contentLists","Content Lists");
            setTitle(title);

        } else if(contentMailListTable.equals(evt.getWidget()) && "sel".equals(evt.getType())) {
            // edit selected contentMailList
            contentMailListForm.setId(evt.getRequest().getParameter("id"));
            showOnly(contentMailListForm);

        } else if(contentMailListTable.equals(evt.getWidget()) && "newContentMailList".equals(evt.getType())) {
            // newContentMailList
            contentMailListForm.init();
            showOnly(contentMailListForm);

            String title = application.getMessage("maillist.label.addNewContentList","Add New Content List");
            setTitle(title);

        } else if(contentMailListForm.equals(evt.getWidget()) && "saved".equals(evt.getType())) {
            // saved contentMailListForm
            contentMailListForm.setId(contentMailListForm.getId());
            contentMailListForm.setFormSaved(true);
            showOnly(contentMailListForm);

        } else if(contentMailListForm.equals(evt.getWidget()) && "clearSent".equals(evt.getType())) {
            // clear sent item
            contentMailListForm.getMailList().clearSentContentIds();
            contentMailListForm.setId(contentMailListForm.getId());
            showOnly(contentMailListForm);

        // === [ ScheduledMailList events ] ====================================
        } else if(this.equals(evt.getWidget()) && "showScheduledMailListTable".equals(evt.getType())) {
            // showScheduledMailListTable
            showOnly(scheduledMailListTable);
            String title = application.getMessage("maillist.label.scheduledLists","Schedules Lists");
            setTitle(title);
        } else if(scheduledMailListTable.equals(evt.getWidget()) && "sel".equals(evt.getType())) {
            // edit selected scheduledMailList
            scheduledMailListForm.setId(evt.getRequest().getParameter("id"));
            showOnly(scheduledMailListForm);

        } else if(scheduledMailListTable.equals(evt.getWidget()) && "newScheduledMailList".equals(evt.getType())) {
            // newScheduledMailList
            scheduledMailListForm.init();
            showOnly(scheduledMailListForm);
            String title = application.getMessage("maillist.label.addNewScheduledList","Add New Scheduled List");
            setTitle(title);

        } else if(scheduledMailListForm.equals(evt.getWidget()) && "saved".equals(evt.getType())) {
            // saved scheduledMailListForm
            scheduledMailListForm.setId(scheduledMailListForm.getId());
            scheduledMailListForm.setFormSaved(true);
            showOnly(scheduledMailListForm);

        } else if(scheduledMailListForm.equals(evt.getWidget()) && "clearSent".equals(evt.getType())) {
            // clear sent item
            scheduledMailListForm.getMailList().clearSentContentIds();
            scheduledMailListForm.setId(scheduledMailListForm.getId());
            showOnly(scheduledMailListForm);
        // === [ MailLog events ] ==============================================
        } else if(this.equals(evt.getWidget()) && "showMailLogTable".equals(evt.getType())) {
            // showMailLogTable
            showOnly(mailLogTable);
            String title = application.getMessage("maillist.label.mailingListLog","Mailing List Log");
            setTitle(title);
        } else if(mailLogTable.equals(evt.getWidget()) && "sel".equals(evt.getType())) {
            // view mailLog
            mailLogForm.setId(evt.getRequest().getParameter("id"));
            showOnly(mailLogForm);

        } else if(mailLogForm.equals(evt.getWidget()) && "Ok".equals(evt.getType())) {
            // ok mailLog - back to (show) mailLogTable
            showOnly(mailLogTable);


        // === [ MailTemplate events ] =========================================
        } else if(this.equals(evt.getWidget()) && "showMailTemplateTable".equals(evt.getType())) {
            // showMailTemplateTable
            showOnly(mailTemplateTable);
            String title = application.getMessage("maillist.label.mailingListTemplates","Mailing List Templates");
            setTitle(title);

        } else if(mailTemplateTable.equals(evt.getWidget()) && "sel".equals(evt.getType())) {
            // edit selected mailTemplate
            mailTemplateForm.setId(evt.getRequest().getParameter("id"));
            showOnly(mailTemplateForm);

        } else if(mailTemplateTable.equals(evt.getWidget()) && "newMailTemplate".equals(evt.getType())) {
            // newMailTemplate
            mailTemplateForm.init();
            showOnly(mailTemplateForm);
            String title = application.getMessage("maillist.label.addNewMailingListTemplate","Add New Mailing List Template");
            setTitle(title);
        } else if(mailTemplateForm.equals(evt.getWidget()) && "saved".equals(evt.getType())) {
            // saved mailTemplateForm
            mailTemplateForm.setId(mailTemplateForm.getId());
            mailTemplateForm.setFormSaved(true);
            showOnly(mailTemplateForm);

        } else {
            // log ignored events
            log.debug("Ignored event in MailListUi. Widget=" +
            evt.getWidget().getAbsoluteName() +
            " Event=" + evt.getType());
        }

        // disable back/forward in browser!
        return new Forward(null, evt.getRequest().getRequestURI(), true);
        //return super.actionPerformed(evt);
    }

    public String getTemplate() {
        return "maillist/mailListUi";
    }

    private void showOnly(Widget widget) {
        Widget childWidget;
        Iterator iterator;

        iterator = getChildren().iterator();
        while(iterator.hasNext()) {
            childWidget = (Widget) iterator.next();
            childWidget.setHidden(true);
        }

        widget.setHidden(false);
    }

    // === [ getters & setters ] ===============================================
    public ComposedMailListForm getComposedMailListForm() {
        return composedMailListForm;
    }

    public void setComposedMailListForm(ComposedMailListForm composedMailListForm) {
        this.composedMailListForm = composedMailListForm;
    }

    public ComposedMailListTable getComposedMailListTable() {
        return composedMailListTable;
    }

    public void setComposedMailListTable(ComposedMailListTable composedMailListTable) {
        this.composedMailListTable = composedMailListTable;
    }

    public MailLogForm getMailLogForm() {
        return mailLogForm;
    }

    public void setMailLogForm(MailLogForm mailLogForm) {
        this.mailLogForm = mailLogForm;
    }

    public MailLogTable getMailLogTable() {
        return mailLogTable;
    }

    public void setMailLogTable(MailLogTable mailLogTable) {
        this.mailLogTable = mailLogTable;
    }

    public MailTemplateForm getMailTemplateForm() {
        return mailTemplateForm;
    }

    public void setMailTemplateForm(MailTemplateForm mailTemplateForm) {
        this.mailTemplateForm = mailTemplateForm;
    }

    public MailTemplateTable getMailTemplateTable() {
        return mailTemplateTable;
    }

    public void setMailTemplateTable(MailTemplateTable mailTemplateTable) {
        this.mailTemplateTable = mailTemplateTable;
    }

    public ContentMailListForm getContentMailListForm() {
        return contentMailListForm;
    }

    public void setContentMailListForm(ContentMailListForm contentMailListForm) {
        this.contentMailListForm = contentMailListForm;
    }

    public ContentMailListTable getContentMailListTable() {
        return contentMailListTable;
    }

    public void setContentMailListTable(ContentMailListTable contentMailListTable) {
        this.contentMailListTable = contentMailListTable;
    }

    public ScheduledMailListForm getScheduledMailListForm() {
        return scheduledMailListForm;
    }

    public void setScheduledMailListForm(ScheduledMailListForm scheduledMailListForm) {
        this.scheduledMailListForm = scheduledMailListForm;
    }

    public ScheduledMailListTable getScheduledMailListTable() {
        return scheduledMailListTable;
    }

    public void setScheduledMailListTable(ScheduledMailListTable scheduledMailListTable) {
        this.scheduledMailListTable = scheduledMailListTable;
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

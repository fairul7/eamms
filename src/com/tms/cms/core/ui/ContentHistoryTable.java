package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 24, 2003
 * Time: 6:40:53 PM
 * To change this template use Options | File Templates.
 */
public class ContentHistoryTable extends ContentListTable {

    private String id;
    private Portlet previewPortlet;
    private PreviewContentObjectPanel previewPanel;

    public ContentHistoryTable() {
    }

    public ContentHistoryTable(String name) {
        super(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Portlet getPreviewPortlet() {
        return previewPortlet;
    }

    public PreviewContentObjectPanel getPreviewPanel() {
        return previewPanel;
    }

    public void init() {
        setNumbering(false);
        setMultipleSelect(false);
        setModel(new ContentHistoryTableModel());

        previewPortlet = new Portlet("previewPortlet");
        previewPortlet.setWidth("100%");
        previewPortlet.setHidden(true);
        previewPanel = new PreviewContentObjectPanel("previewPanel");
        previewPanel.setWidgetManager(getWidgetManager());
        previewPortlet.addChild(previewPanel);
        addChild(previewPortlet);
    }

    public void onRequest(Event evt) {
        ContentObject co = ContentHelper.getContentObject(evt, getId());
        if (co != null) {
            if (!co.getId().equals(getId())) {
                init();
            }
            setId(co.getId());
        }
    }

    public Forward onSelection(Event evt) {
        String version = evt.getRequest().getParameter("version");
        previewPanel.setId(getId());
        previewPanel.setVersion(version);
        previewPortlet.setHidden(false);
        previewPortlet.onMaximize(evt);
        previewPortlet.setText("Version " + version);
        return new Forward("selection");
    }

    public String getDefaultTemplate() {
        return "cms/admin/contentHistoryTable";
    }

    public class ContentHistoryTableModel extends TableModel {

        private Collection history;
//        private boolean showRadio;

        public ContentHistoryTableModel() {

            // add columns
            Application application = Application.getInstance();
            TableColumn versionColumn = new TableColumn("version", application.getMessage("general.label.version", "Version"));
            versionColumn.setUrlParam("version");
            addColumn(versionColumn);

            TableColumn submissionDateColumn = new TableColumn("submissionDate", application.getMessage("general.label.submissionDate", "Submission Date"));
            submissionDateColumn.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(submissionDateColumn);

            TableColumn submissionUserColumn = new TableColumn("submissionUser", application.getMessage("general.label.submittedBy", "Submitted By"));
            addColumn(submissionUserColumn);

            String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
            TableColumn checkedOutColumn = new TableColumn("approved", application.getMessage("general.label.approved", "Approved"));
            checkedOutColumn.setFormat(booleanFormat);
            addColumn(checkedOutColumn);

            TableColumn approvalDateColumn = new TableColumn("approvalDate", application.getMessage("general.label.approvalDate", "Approval Date"));
            approvalDateColumn.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(approvalDateColumn);

            TableColumn approvalUserColumn = new TableColumn("approvalUser", application.getMessage("general.label.approvedBy", "Approved By"));
            addColumn(approvalUserColumn);

            TableColumn commentsColumn = new TableColumn("comments", application.getMessage("general.label.comments", "Comments"));
            addColumn(commentsColumn);

            // add actions
            addAction(new TableAction("rollback", application.getMessage("general.label.rollback", "Rollback"), Application.getInstance().getMessage("cms.message.rollback", "Please confirm rollback - newer versions cannot be recovered")));

        }

        public String getTableRowKey() {
//            return (showRadio) ? "version" : null;
            return "version";
        }

        public Collection getTableRows() {
            try {
                // query
                Application application = Application.getInstance();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                history = cm.viewHistory(getId(), getSort(), isDesc(), getStart(), getRows(), getWidgetManager().getUser());
                return history;
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public int getTotalRowCount() {
            try {
                // query
                Application application = Application.getInstance();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                return cm.viewHistoryCount(getId(), getWidgetManager().getUser());
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
//            System.out.println("Action: " + action + "; Keys: " + Arrays.asList(selectedKeys));

            if ("rollback".equals(action)) {
                if (selectedKeys.length > 0) {
                    try {
                        String version = selectedKeys[0];
                        Application application = Application.getInstance();
                        ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                        User user = getWidgetManager().getUser();
                        cm.rollback(getId(), version, user);
                        previewPanel.setVersion(version);
                        Forward forward = new Forward("rollback");
                        return forward;
                    }
                    catch (Exception e) {
                        Log.getLog(getClass()).error(e.toString(), e);
                        return null;
                    }
                }
                else {
                    return null;
                }
            }
            else {
                return null;
            }
        }
    }


}

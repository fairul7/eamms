package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.ContentUtil;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ContentListTable extends Table {

    private boolean customSelection;
    private boolean showPreview;
    private ContentSelectBox contentSelectBox;

    public ContentListTable() {
    }

    public ContentListTable(String name) {
        super(name);
    }

    public String getContentClass() {
        if (contentSelectBox != null) {
            Map selected = contentSelectBox.getSelectedOptions();
            if (selected != null && selected.size() > 0) {
                return (String)selected.keySet().iterator().next();
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public void setContentClass(String contentClass) {
        if (contentSelectBox != null) {
            contentSelectBox.setSelectedOption(contentClass);
        }
    }

    public boolean isCustomSelection() {
        return customSelection;
    }

    public void setCustomSelection(boolean customSelection) {
        this.customSelection = customSelection;
    }

    public boolean isShowPreview() {
        return showPreview;
    }

    public void setShowPreview(boolean showPreview) {
        this.showPreview = showPreview;
    }

    public void init() {
        contentSelectBox = new ContentSelectBox();
        setModel(new ContentListTableModel());
    }

    public Forward onSelection(Event evt) {
        String id = evt.getRequest().getParameter("id");
        if (!customSelection) {
            ContentHelper.setId(evt, id);
        }
        Forward forward = super.onSelection(evt);
        forward = new Forward("selection");
        return forward;
    }

    public Forward onValidate(Event evt) {
        return super.onValidate(evt);
    }

    public class ContentListTableModel extends TableModel {

        public ContentListTableModel() {

            // add columns
            Application application = Application.getInstance();
            TableColumn nameColumn = new TableColumn("name", application.getMessage("general.label.name", "Name"));
            nameColumn.setUrlParam("id");
            addColumn(nameColumn);

            TableColumn classColumn = new TableColumn("className", application.getMessage("general.label.type", "Type"));
            classColumn.setFormat(new TableResourceFormat("cms.label.iconLabel_", null));
            addColumn(classColumn);

            TableColumn dateColumn = new TableColumn("date", application.getMessage("general.label.date", "Date"));
            dateColumn.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(dateColumn);

            String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
            TableColumn checkedOutColumn = new TableColumn("checkedOut", application.getMessage("general.label.checkedOut", "Checked Out"));
            checkedOutColumn.setFormat(booleanFormat);
            addColumn(checkedOutColumn);

            TableColumn submittedColumn = new TableColumn("submitted", application.getMessage("general.label.submitted", "Submitted"));
            submittedColumn.setFormat(booleanFormat);
            addColumn(submittedColumn);

            TableColumn approvedColumn = new TableColumn("approved", application.getMessage("general.label.approved", "Approved"));
            approvedColumn.setFormat(booleanFormat);
            addColumn(approvedColumn);

            TableColumn publishedColumn = new TableColumn("published", application.getMessage("general.label.published", "Published"));
            publishedColumn.setFormat(booleanFormat);
            addColumn(publishedColumn);

            TableColumn archivedColumn = new TableColumn("archived", application.getMessage("general.label.archived", "Archived"));
            archivedColumn.setFormat(booleanFormat);
            addColumn(archivedColumn);

            // add preview link
            if (showPreview) {
                TableColumn previewColumn = new TableColumn(null, " ", false);
                previewColumn.setLabel(application.getMessage("general.label.preview", "Preview"));
                previewColumn.setUrl("?preview=true");
                previewColumn.setUrlParam("id");
                addColumn(previewColumn);
            }

            // add actions
            if (!customSelection) {
                //Application application = Application.getInstance();
                addAction(new TableAction("approve", application.getMessage("general.label.approve", "Approve"), application.getMessage("cms.message.approve", "Approve Selected Content?")));
                addAction(new TableAction("reject", application.getMessage("general.label.reject", "Reject"), application.getMessage("cms.message.reject", "Reject Selected Content?")));
                addAction(new TableAction("publish", application.getMessage("general.label.publish", "Publish"), application.getMessage("cms.message.publish", "Publish Selected Content?")));
                addAction(new TableAction("withdraw", application.getMessage("general.label.withdraw", "Withdraw"), application.getMessage("cms.message.withdraw", "Withdraw Selected Content?")));
                addAction(new TableAction("archive", application.getMessage("general.label.archive", "Archive"), application.getMessage("cms.message.archive", "Archive Selected Content?")));
                addAction(new TableAction("unarchive", application.getMessage("general.label.unarchive", "Unarchive"), application.getMessage("cms.message.unarchive", "Unarchive Selected Content?")));
                addAction(new TableAction("delete", application.getMessage("general.label.delete", "Delete"), application.getMessage("cms.message.delete", "Delete Selected Content?")));
            }

            // add filters
            TableFilter nameFilter = new TableFilter("name", application.getMessage("general.label.name", "Name"));
            TextField nameField = new TextField("name");
            nameField.setSize("20");
            nameFilter.setWidget(nameField);
            addFilter(nameFilter);

            TableFilter contentTypeFilter = new TableFilter("contentType", application.getMessage("general.label.contentType", "Content Type"));
            contentTypeFilter.setWidget(contentSelectBox);
            addFilter(contentTypeFilter);

            TableFilter checkOutFilter = new TableFilter("status", application.getMessage("general.label.status", "Status"));
            SelectBox checkOutSelectBox = new SelectBox("checkOutSelectBox");
            checkOutSelectBox.setOptions("any="+application.getMessage("general.label.any", "Any")+";checkedIn="+application.getMessage("general.label.notCheckedOut", "Not Checked Out")+";checkedOutPersonal="+application.getMessage("general.label.checkedOutPersonal", "Checked Out (Personal)")+";checkedOut="+application.getMessage("general.label.checkedOut", "Checked Out")+";submitted="+application.getMessage("general.label.submitted", "Submitted")+";approved="+application.getMessage("general.label.approved", "Approved")+"");
            checkOutFilter.setWidget(checkOutSelectBox);
            addFilter(checkOutFilter);

            TableFilter publishedFilter = new TableFilter("published", "Published");
            SelectBox publishedSelectBox = new SelectBox("publishedSelectBox");
            publishedSelectBox.setOptions("any="+application.getMessage("general.label.any", "Any")+";true="+application.getMessage("general.label.published", "Published")+";false="+application.getMessage("general.label.notPublished", "Not Published")+"");
            publishedFilter.setWidget(publishedSelectBox);
            addFilter(publishedFilter);

            TableFilter archivedFilter = new TableFilter("archived", application.getMessage("general.label.archived", "Archived"));
            SelectBox archivedSelectBox = new SelectBox("archivedSelectBox");
            archivedSelectBox.setOptions("any="+application.getMessage("general.label.any", "Any")+";true="+application.getMessage("general.label.archived", "Archived")+";false="+application.getMessage("general.label.notArchived", "Not Archived")+"");
            List selected = new ArrayList();
            selected.add("false");
            archivedSelectBox.setValue(selected);
            archivedFilter.setWidget(archivedSelectBox);
            addFilter(archivedFilter);

        }

        public String getTableRowKey() {
            return "id";
        }

        public Collection getTableRows() {
            try {
                // process filters
                String name = null;
                String checkOutId = null;
                Boolean checkedOut = null;
                Boolean submitted = null;
                Boolean approved = null;
                Boolean published = null;
                Boolean archived = null;
                String[] classArray = null;

                String contentClass = getContentClass();
                if (contentClass != null && contentClass.trim().length() > 0) {
                    classArray = new String[] {contentClass};
                }

                name = (String)getFilterValue("name");

                List list = (List)getFilterValue("status");
                if (list != null && list.size() > 0) {
                    if (list.get(0).equals("checkedIn")) {
                        checkedOut = Boolean.FALSE;
                        submitted = Boolean.FALSE;
                    }
                    else if (list.get(0).equals("checkedOut")) {
                        checkedOut = Boolean.TRUE;
                    }
                    else if (list.get(0).equals("checkedOutPersonal")) {
                        checkedOut = Boolean.TRUE;
                        checkOutId = getWidgetManager().getUser().getId();
                    }
                    else if (list.get(0).equals("submitted")) {
                        submitted = Boolean.TRUE;
                    }
                    else if (list.get(0).equals("approved")) {
                        approved = Boolean.TRUE;
                    }
                }

                list = (List)getFilterValue("published");
                if (list != null && list.size() > 0) {
                    if (list.get(0).equals("true"))
                        published = Boolean.TRUE;
                    else if (list.get(0).equals("false"))
                        published = Boolean.FALSE;
                }

                list = (List)getFilterValue("archived");
                if (list != null && list.size() > 0) {
                    if (list.get(0).equals("true"))
                        archived = Boolean.TRUE;
                    else if (list.get(0).equals("false"))
                        archived = Boolean.FALSE;
                }

                // query
                Application application = Application.getInstance();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                String permission = isCustomSelection() ? ContentManager.USE_CASE_VIEW : ContentManager.USE_CASE_PREVIEW;
                Collection children = cm.viewList(null, classArray, name, null, checkedOut, submitted, approved, archived, published, Boolean.FALSE, checkOutId, getSort(), isDesc(), getStart(), getRows(), permission, getWidgetManager().getUser());
                return children;
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public int getTotalRowCount() {
            try {
                // process filters
                String name = null;
                String checkOutId = null;
                Boolean checkedOut = null;
                Boolean submitted = null;
                Boolean approved = null;
                Boolean published = null;
                Boolean archived = null;
                String[] classArray = null;

                String contentClass = getContentClass();
                if (contentClass != null && contentClass.trim().length() > 0) {
                    classArray = new String[] {contentClass};
                }

                name = (String)getFilterValue("name");

                List list = (List)getFilterValue("status");
                if (list != null && list.size() > 0) {
                    if (list.get(0).equals("checkedIn")) {
                        checkedOut = Boolean.FALSE;
                        submitted = Boolean.FALSE;
                    }
                    else if (list.get(0).equals("checkedOut")) {
                        checkedOut = Boolean.TRUE;
                    }
                    else if (list.get(0).equals("checkedOutPersonal")) {
                        checkedOut = Boolean.TRUE;
                        checkOutId = getWidgetManager().getUser().getId();
                    }
                    else if (list.get(0).equals("submitted")) {
                        submitted = Boolean.TRUE;
                    }
                    else if (list.get(0).equals("approved")) {
                        approved = Boolean.TRUE;
                    }
                }

                list = (List)getFilterValue("published");
                if (list != null && list.size() > 0) {
                    if (list.get(0).equals("true"))
                        published = Boolean.TRUE;
                    else if (list.get(0).equals("false"))
                        published = Boolean.FALSE;
                }

                list = (List)getFilterValue("archived");
                if (list != null && list.size() > 0) {
                    if (list.get(0).equals("true"))
                        archived = Boolean.TRUE;
                    else if (list.get(0).equals("false"))
                        archived = Boolean.FALSE;
                }

                // query
                Application application = Application.getInstance();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                String permission = isCustomSelection() ? ContentManager.USE_CASE_VIEW : ContentManager.USE_CASE_PREVIEW;
                int total = cm.viewCount(null, classArray, name, null, checkedOut, submitted, approved, archived, published, Boolean.FALSE, checkOutId, permission, getWidgetManager().getUser());
                return total;
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
//            System.out.println("Action: " + action + "; Keys: " + Arrays.asList(selectedKeys));

/*
            // set selected objects
            Collection keyList = Arrays.asList(selectedKeys);
            Collection list = new ArrayList(getTableRows());
            for (Iterator i=list.iterator(); i.hasNext();) {
                ContentObject co = (ContentObject)i.next();
                if (!keyList.contains(co.getId())) {
                    i.remove();
                }
            }
            ContentHelper.setContentObjectList(evt, list);

            // set selected action
            ContentHelper.setContentAction(evt, action);
            Forward forward = new Forward("action");
            return forward;
        }
*/

            Application application = Application.getInstance();
            ContentManager cm = (ContentManager)application.getModule(ContentManager.class);           
            User user = getWidgetManager().getUser();

            if ("delete".equals(action)) {
//                System.out.println("Delete: " + action + "; Keys: " + Arrays.asList(selectedKeys));
                for (int i=0; i<selectedKeys.length; i++) {
                    try {
                        cm.delete(selectedKeys[i], true, user);
                    }
                    catch (ContentException e) {
                    }
                }
            }
            else if ("publish".equals(action)) {
//                System.out.println("Publish: " + action + "; Keys: " + Arrays.asList(selectedKeys));

                try {
                    for (int i=0; i<selectedKeys.length; i++) {
                        cm.publish(selectedKeys[i], false, user);
                    }
                }
                catch (ContentException e) {
                    Log.getLog(getClass()).error(e.toString(), e);
                }
            }
            else if ("withdraw".equals(action)) {
//                System.out.println("Withdraw: " + action + "; Keys: " + Arrays.asList(selectedKeys));

                try {
                    for (int i=0; i<selectedKeys.length; i++) {
                        cm.withdraw(selectedKeys[i], false, user);
                    }
                }
                catch (ContentException e) {
                    Log.getLog(getClass()).error(e.toString(), e);
                }
            }
            else if ("archive".equals(action)) {
//                System.out.println("Archive: " + action + "; Keys: " + Arrays.asList(selectedKeys));

                try {
                    for (int i=0; i<selectedKeys.length; i++) {
                        cm.archive(selectedKeys[i], false, user);
                    }
                }
                catch (ContentException e) {
                    Log.getLog(getClass()).error(e.toString(), e);
                }
            }
            else if ("unarchive".equals(action)) {
//                System.out.println("Unarchive: " + action + "; Keys: " + Arrays.asList(selectedKeys));

                try {
                    for (int i=0; i<selectedKeys.length; i++) {
                        cm.unarchive(selectedKeys[i], false, user);
                    }
                }
                catch (ContentException e) {
                    Log.getLog(getClass()).error(e.toString(), e);
                }
            }
            else if ("approve".equals(action)) {
//                System.out.println("Approve: " + action + "; Keys: " + Arrays.asList(selectedKeys));

                try {
                    ContentObject co = null;
                    for (int i=0; i<selectedKeys.length; i++) {
                        boolean alreadySubmitted = true;
                        try {
                            co = cm.view(selectedKeys[i], user);
                            if (!co.isSubmitted()) {
                                alreadySubmitted = false;
                                co = cm.submitForApproval(selectedKeys[i], user);
                            }
                        } catch (ContentException e) {
                            ;
                        }
                        if (co != null) {
                            try {
                                cm.approve(co, user);
                                if (alreadySubmitted) {
                                    ContentUtil.sendApprovalNotification(co, user, true);
                                }
                            } catch (ContentException e) {
                                ;
                            }
                        }
                    }
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error(e.toString());
                }
            }
            else if ("reject".equals(action)) {
//                System.out.println("Reject: " + action + "; Keys: " + Arrays.asList(selectedKeys));

                try {
                    ContentObject co = null;
                    for (int i=0; i<selectedKeys.length; i++) {
                        try {
                            co = cm.view(selectedKeys[i], user);
                            if (co.isCheckedOut()) {
                                co = cm.undoCheckOut(selectedKeys[i], user);
                            }
                            else if (co.isSubmitted()) {
                                cm.reject(co, user);
                                ContentUtil.sendApprovalNotification(co, user, false);
                            }
                        } catch (ContentException e) {
                            ;
                        }
                    }
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error(e.toString());
                }
            }
            else {

            }
            return new Forward("success");
        }
    }


}

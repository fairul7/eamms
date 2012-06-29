package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentUtil;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import java.util.*;

public class ContentChildrenTable extends Table {

    private String id;

    public ContentChildrenTable() {
    }

    public ContentChildrenTable(String name) {
        super(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void init() {
        setId(ContentManager.CONTENT_TREE_ROOT_ID);
        setModel(new ContentChildrenTableModel());
    }

    public void onRequest(Event evt) {
        ContentObject co = ContentHelper.getContentObject(evt, getId());
        if (co != null) {
            setId(co.getId());
        }
        else {
            setId(null);
        }
    }

    public Forward onSelection(Event evt) {
        String id = evt.getRequest().getParameter("id");
        ContentHelper.setId(evt, id);
        setId(id);
        Forward forward = super.onSelection(evt);
        forward = new Forward("selection");
        return forward;
    }

    public class ContentChildrenTableModel extends TableModel {

        public ContentChildrenTableModel() {

            // add columns
            Application application = Application.getInstance();
            TableColumn nameColumn = new TableColumn("name", application.getMessage("general.label.name", "Name"));
            nameColumn.setUrlParam("id");
            addColumn(nameColumn);

            TableColumn classColumn = new TableColumn("className", application.getMessage("general.label.type", "Type"));
            classColumn.setFormat(new TableResourceFormat("cms.label.iconLabel_", null));
            addColumn(classColumn);

            TableColumn childCountColumn = new TableColumn("childCount", application.getMessage("general.label.children", "Children"), false);
            addColumn(childCountColumn);

            TableColumn dateColumn = new TableColumn("date", application.getMessage("general.label.date", "Date"));
            dateColumn.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(dateColumn);

            String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
/*
            TableColumn checkedOutColumn = new TableColumn("checkedOut", application.getMessage("general.label.checkedOut", "Checked Out"));
            checkedOutColumn.setFormat(booleanFormat);
            addColumn(checkedOutColumn);

            TableColumn submittedColumn = new TableColumn("submitted", application.getMessage("general.label.submitted", "Submitted"));
            submittedColumn.setFormat(booleanFormat);
            addColumn(submittedColumn);
*/

            TableColumn approvedColumn = new TableColumn("approved", application.getMessage("general.label.approved", "Approved"));
            approvedColumn.setFormat(booleanFormat);
            addColumn(approvedColumn);

            TableColumn publishedColumn = new TableColumn("published", application.getMessage("general.label.published", "Published"));
            publishedColumn.setFormat(booleanFormat);
            addColumn(publishedColumn);

            TableColumn archivedColumn = new TableColumn("archived", application.getMessage("general.label.archived", "Archived"));
            archivedColumn.setFormat(booleanFormat);
            addColumn(archivedColumn);

            // add actions
            //Application application = Application.getInstance();
            addAction(new TableAction("approve", application.getMessage("general.label.approve", "Approve"), application.getMessage("cms.message.approve", "Approve Selected Content?")));
            addAction(new TableAction("reject", application.getMessage("general.label.reject", "Reject"), application.getMessage("cms.message.reject", "Reject Selected Content?")));
            addAction(new TableAction("publish", application.getMessage("general.label.publish", "Publish"), application.getMessage("cms.message.publish", "Publish Selected Content?")));
            addAction(new TableAction("withdraw", application.getMessage("general.label.withdraw", "Withdraw"), application.getMessage("cms.message.withdraw", "Withdraw Selected Content?")));
            addAction(new TableAction("archive", application.getMessage("general.label.archive", "Archive"), application.getMessage("cms.message.archive", "Archive Selected Content?")));
            addAction(new TableAction("unarchive", application.getMessage("general.label.unarchive", "Unarchive"), application.getMessage("cms.message.unarchive", "Unarchive Selected Content?")));
            addAction(new TableAction("move", application.getMessage("general.label.move", "Move"), application.getMessage("cms.message.move", "Move Selected Content?")));
            addAction(new TableAction("delete", application.getMessage("general.label.delete", "Delete"), application.getMessage("cms.message.delete", "Delete Selected Content?")));

            // add filters
            TableFilter nameFilter = new TableFilter("name", application.getMessage("general.label.name", "Name"));
            TextField nameField = new TextField("name");
            nameField.setSize("20");
            nameFilter.setWidget(nameField);
            addFilter(nameFilter);

            TableFilter publishedFilter = new TableFilter("published", application.getMessage("general.label.published", "Published"));
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
            // check for empty content object
            if (getId() == null) {
                return new ArrayList();
            }

            try {
                // process filters
                String name = null;
                Boolean published = null;
                Boolean archived = null;

                name = (String)getFilterValue("name");

                List list = (List)getFilterValue("published");
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
                String sortColumn = getSort();
                if ("childCount".equals(sortColumn)) {
                    sortColumn = null;
                }
                Collection children = cm.viewList(null, null, name, getId(), null, null, null, archived, published, Boolean.FALSE, null, sortColumn, isDesc(), getStart(), getRows(),ContentManager.USE_CASE_PREVIEW, getWidgetManager().getUser());

                // get child count
                if (children.size() > 0) {
                    Collection keyList = new ArrayList();
                    for (Iterator i=children.iterator(); i.hasNext();) {
                        ContentObject co = (ContentObject)i.next();
                        keyList.add(co.getId());
                    }
                    Map childCountMap = cm.viewChildrenCount(ContentManager.USE_CASE_PREVIEW, (String[])keyList.toArray(new String[0]), null, null, null, archived, published, Boolean.FALSE, getWidgetManager().getUser());
                    for (Iterator i=children.iterator(); i.hasNext();) {
                        ContentObject co = (ContentObject)i.next();
                        Object count = childCountMap.get(co.getId());
                        if (count == null)
                            co.setProperty("childCount", "0");
                        else
                            co.setProperty("childCount", count);
                    }
                }
                return children;
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public int getTotalRowCount() {
            // check for empty content object
            if (getId() == null) {
                return 0;
            }

            try {
                // process filters
                String name = null;
                Boolean published = null;
                Boolean archived = null;

                name = (String)getFilterValue("name");

                List list = (List)getFilterValue("published");
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
                int total = cm.viewCount(null, null, name, getId(), null, null, null, archived, published, Boolean.FALSE, null, ContentManager.USE_CASE_PREVIEW, getWidgetManager().getUser());
                return total;
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {

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
            else if ("move".equals(action)) {
//                System.out.println("Move: " + action + "; Keys: " + Arrays.asList(selectedKeys));

                // HACK: store in request, then forward to the move page
                evt.getRequest().setAttribute("MoveContentObjectKeys", selectedKeys);
                return new Forward("move");
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

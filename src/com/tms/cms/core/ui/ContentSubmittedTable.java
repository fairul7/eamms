package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentException;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 24, 2003
 * Time: 6:40:53 PM
 * To change this template use Options | File Templates.
 */
public class ContentSubmittedTable extends Table {
    public static final String FORWARD_APPROVE = "approve_successful";

    public ContentSubmittedTable() {
    }

    public ContentSubmittedTable(String name) {
        super(name);
    }

    public void init() {
        setModel(new ContentSubmittedTableModel());
    }

    public void onRequest(Event evt) {
    }

    public Forward onSelection(Event evt) {
        String id = evt.getRequest().getParameter("id");
        ContentHelper.setId(evt, id);
        Forward forward = super.onSelection(evt);
        forward = new Forward("selection");
        return forward;
    }

    public class ContentSubmittedTableModel extends TableModel {

        public ContentSubmittedTableModel() {

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
            TableColumn submittedColumn = new TableColumn("submitted", application.getMessage("general.label.submitted", "Submitted"));
            submittedColumn.setFormat(booleanFormat);
            addColumn(submittedColumn);

			addAction(new TableAction("approve", Application.getInstance().getMessage("cms.label.Approve")));
        }

        public String getTableRowKey() {
            return "id";
        }

        public Collection getTableRows() {
            try {
                // query
                Application application = Application.getInstance();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                Collection children = cm.viewList(null, null, null, null, null, Boolean.TRUE, null, Boolean.FALSE, null, Boolean.FALSE, null, getSort(), isDesc(), getStart(), getRows(), ContentManager.USE_CASE_APPROVE, getWidgetManager().getUser());
                return children;
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
                int total = cm.viewCount(null, null, null, null, null, Boolean.TRUE, null, Boolean.FALSE, null, Boolean.FALSE, null, ContentManager.USE_CASE_APPROVE, getWidgetManager().getUser());
                return total;
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

		public Forward processAction(Event evt, String action, String[] selectedKeys)
		{
			Application application = Application.getInstance();
            ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
            User user = getWidgetManager().getUser();
            if ("approve".equals(action))
			{
                for (int i=0; i<selectedKeys.length; i++)
				{
					try
					{
						cm.approve(cm.view(selectedKeys[i], user), user);
					}
					catch (ContentException e)
					{
						Log.getLog(getClass()).error("Error while approving content object " + selectedKeys[i], e);
					}
					catch (DataObjectNotFoundException e)
					{
						Log.getLog(getClass()).error("Unable to retrieve content object " + selectedKeys[i], e);
					}
				}
			}
			return new Forward(FORWARD_APPROVE);
		}

/*
        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            System.out.println("Action: " + action + "; Keys: " + Arrays.asList(selectedKeys));

            Application application = Application.getInstance();
            ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
            User user = getWidgetManager().getUser();

            if ("delete".equals(action)) {
                System.out.println("Delete: " + action + "; Keys: " + Arrays.asList(selectedKeys));
                try {
                    for (int i=0; i<selectedKeys.length; i++) {
                        try {
                            cm.destroy(selectedKeys[i], user);
                        }
                        catch (DataObjectNotFoundException e) {
                        }
                    }
                }
                catch (ContentException e) {
                    e.printStackTrace();
                }
            }
            else if ("publish".equals(action)) {
                System.out.println("Publish: " + action + "; Keys: " + Arrays.asList(selectedKeys));

                try {
                    for (int i=0; i<selectedKeys.length; i++) {
                        cm.publish(selectedKeys[i], false, user);
                    }
                }
                catch (ContentException e) {
                    e.printStackTrace();
                }
            }
            else if ("withdraw".equals(action)) {
                System.out.println("Withdraw: " + action + "; Keys: " + Arrays.asList(selectedKeys));

                try {
                    for (int i=0; i<selectedKeys.length; i++) {
                        cm.withdraw(selectedKeys[i], false, user);
                    }
                }
                catch (ContentException e) {
                    e.printStackTrace();
                }
            }
            else if ("archive".equals(action)) {
                System.out.println("Archive: " + action + "; Keys: " + Arrays.asList(selectedKeys));

                try {
                    for (int i=0; i<selectedKeys.length; i++) {
                        cm.archive(selectedKeys[i], false, user);
                    }
                }
                catch (ContentException e) {
                    e.printStackTrace();
                }
            }
            else if ("unarchive".equals(action)) {
                System.out.println("Unarchive: " + action + "; Keys: " + Arrays.asList(selectedKeys));

                try {
                    for (int i=0; i<selectedKeys.length; i++) {
                        cm.unarchive(selectedKeys[i], false, user);
                    }
                }
                catch (ContentException e) {
                    e.printStackTrace();
                }
            }
            else {

            }
            return new Forward("success");
        }
*/
    }


}

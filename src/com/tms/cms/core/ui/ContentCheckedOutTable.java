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
public class ContentCheckedOutTable extends Table {
    public static final String FORWARD_UNDO = "undo_checkout_successful";

    public ContentCheckedOutTable() {
    }

    public ContentCheckedOutTable(String name) {
        super(name);
    }

    public void init() {
        setModel(new ContentCheckedOutTableModel());
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

    public class ContentCheckedOutTableModel extends TableModel {

        public ContentCheckedOutTableModel() {

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

			// add actions
			addAction(new TableAction("undo", Application.getInstance().getMessage("general.label.undoCheckOut")));
        }

        public String getTableRowKey() {
            return "id";
        }

        public Collection getTableRows() {
            try {
                String userId = getWidgetManager().getUser().getId();

                // query
                Application application = Application.getInstance();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                Collection children = cm.viewList(null, null, null, null, Boolean.TRUE, null, null, null, null, Boolean.FALSE, userId, getSort(), isDesc(), getStart(), getRows(), ContentManager.USE_CASE_PREVIEW, getWidgetManager().getUser());
                return children;
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public int getTotalRowCount() {
            try {
                String userId = getWidgetManager().getUser().getId();

                // query
                Application application = Application.getInstance();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                int total = cm.viewCount(null, null, null, null, Boolean.TRUE, null, null, null, null, Boolean.FALSE, userId, ContentManager.USE_CASE_PREVIEW, getWidgetManager().getUser());
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
			if("undo".equals(action))
			{
				for (int i=0; i<selectedKeys.length; i++)
				{
					try
					{
						cm.undoCheckOut(selectedKeys[i], user);
					}
					catch (ContentException e)
					{
						Log.getLog(this.getClass()).error("Error occured while attempting to undo check out for content " + selectedKeys[i], e);
					}
				}
			}
			return new Forward(FORWARD_UNDO);
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

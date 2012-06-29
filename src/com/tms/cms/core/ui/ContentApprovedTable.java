package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentException;
import com.tms.cms.image.Image;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.util.Log;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class ContentApprovedTable extends Table {
    public static final String FORWARD_PUBLISH = "publish_successful";

    public ContentApprovedTable() {
    }

    public ContentApprovedTable(String name) {
        super(name);
    }

    public void init() {
        setModel(new ContentApprovedTableModel());
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

    public class ContentApprovedTableModel extends TableModel {

        public ContentApprovedTableModel() {

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
            TableColumn submittedColumn = new TableColumn("approved", application.getMessage("general.label.approved", "Approved"));
            submittedColumn.setFormat(booleanFormat);
            addColumn(submittedColumn);

			addAction(new TableAction("publish", Application.getInstance().getMessage("cms.label.Publish")));
        }

        public String getTableRowKey() {
            return "id";
        }

        public Collection getTableRows() {
            try {
                // query
                Application application = Application.getInstance();
                // ignore images
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                Class[] classes = cm.getAllowedClasses(null);
                List classList = new ArrayList();
                for (int i=0; i<classes.length; i++) {
                    Class cl = (Class)classes[i];
                    if (!cl.equals(Image.class)) {
                        classList.add(cl.getName());
                    }
                }
                String[] classNames = (String[])classList.toArray(new String[classList.size()]);
                Collection children = cm.viewList(null, classNames, null, null, null, null, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null, getSort(), isDesc(), getStart(), getRows(), ContentManager.USE_CASE_PUBLISH, getWidgetManager().getUser());
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
                // ignore images
                Class[] classes = cm.getAllowedClasses(null);
                List classList = new ArrayList();
                for (int i=0; i<classes.length; i++) {
                    Class cl = (Class)classes[i];
                    if (!cl.equals(Image.class)) {
                        classList.add(cl.getName());
                    }
                }
                String[] classNames = (String[])classList.toArray(new String[classList.size()]);
                int total = cm.viewCount(null, classNames, null, null, null, null, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null, ContentManager.USE_CASE_PUBLISH, getWidgetManager().getUser());
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
			if ("publish".equals(action))
			{
				for (int i=0; i<selectedKeys.length; i++)
				{
					try
					{
                        cm.publish(selectedKeys[i], false, user);
					}
					catch (ContentException e)
					{
						Log.getLog(getClass()).error("Error while publishing content object " + selectedKeys[i], e);
					}
				}
            }
            return new Forward(FORWARD_PUBLISH);
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

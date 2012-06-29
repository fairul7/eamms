package com.tms.collab.directory.ui;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorIn;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.services.security.Group;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class UserAppointmentTable extends Table {

	private String keys;

	private String values;

	public UserAppointmentTable() {
	}

	public UserAppointmentTable(String name) {
		super(name);
	}

	public void init() {
		super.init();
		setWidth("100%");
		setModel(new UserTableModel());
		loadGroups();
	}

	protected void loadGroups() {
		Application application = Application.getInstance();
		SecurityService service = (SecurityService) application
				.getService(SecurityService.class);
		Map groupMap = new SequencedHashMap();
		try {
			DaoQuery q = new DaoQuery();
			q.addProperty(new OperatorEquals("active", "1",
					DaoOperator.OPERATOR_AND));
			Collection list = service.getGroups(q, 0, -1, "groupName", false);
			groupMap.put("-1", application.getMessage(
					"security.label.filterGroup", "Filter Group"));
			for (Iterator i = list.iterator(); i.hasNext();) {
				Group group = (Group) i.next();
				groupMap.put(group.getId(), group.getName());
			}
		} catch (Exception e) {
			Log.getLog(UserMessagingTable.class).error(
					"Error loading groups: " + e.toString(), e);
		}
		// Adding Filters
		TableFilter groupFilter = getModel().getFilter("group");
		SelectBox groupSelect = (SelectBox) groupFilter.getWidget();
		groupSelect.setOptionMap(groupMap);
		groupSelect.setMultiple(false);
	}

	public void onRequest(Event evt) {
		loadGroups();
	}

	public void setQuery(String query) {
		TextField q = (TextField) getModel().getFilter("query").getWidget();
		q.setValue(query);
	}

	public void setSelectedGroup(String groupId) {
		SelectBox sb = (SelectBox) getModel().getFilter("group").getWidget();
		sb.setSelectedOptions(new String[] { groupId });
	}

	public class UserTableModel extends TableModel {
		public UserTableModel() {
			super();
			Application application = Application.getInstance();
			// Adding Columns
			TableColumn nameColumn = new TableColumn("username", application
					.getMessage("security.label.username", "Username"));
			addColumn(nameColumn);
			// NOTE: BUG 2773
			// addColumn(new TableColumn("displayFirstName",
			// application.getMessage("security.label.firstName", "First
			// Name")));
			addColumn(new TableColumn("firstName", application.getMessage(
					"security.label.firstName", "First Name")));
			addColumn(new TableColumn("lastName", application.getMessage(
					"security.label.lastName", "Last Name")));
			addColumn(new TableColumn("email1", application.getMessage(
					"security.label.email", "Email")));
			addColumn(new TableColumn("telOffice", "Phone"));
			addColumn(new TableColumn("telMobile", "Mobile"));

			// Adding Actions
			addAction(new TableAction("select", "Select"));
		

			// Adding Filters
			TableFilter userNameFilter = new TableFilter("query");
			TableFilter groupFilter = new TableFilter("group");

			Map groupMap = new SequencedHashMap();
			SelectBox groupSelect = new SelectBox();
			groupSelect.setOptionMap(groupMap);
			groupSelect.setMultiple(false);
			groupFilter.setWidget(groupSelect);

			addFilter(userNameFilter);
			addFilter(groupFilter);
		}

		public Collection getTableRows() {
			Collection list = new ArrayList();
			SecurityService service = (SecurityService) Application
					.getInstance().getService(SecurityService.class);
			try {
				String groupId = getGroupFilter();
				if (groupId == null || "".equals(groupId)
						|| "-1".equals(groupId)) {
					list = service.getUsers(generateDaoProperties(),
							getStart(), getRows(), getSort(), isDesc());
				} else {
					list = service.getGroupUsers(groupId,
							generateDaoProperties(), getStart(), getRows(),
							getSort(), isDesc());
				}
			} catch (Exception e) {
				Log.getLog(UserMessagingTable.class).error(e.toString(), e);
			}
			return list;
		}

		public int getTotalRowCount() {
			try {
				SecurityService service = (SecurityService) Application
						.getInstance().getService(SecurityService.class);
				String groupId = getGroupFilter();
				if (groupId == null || "".equals(groupId)
						|| "-1".equals(groupId)) {
					return service.getUserCount(generateDaoProperties());
				} else {
					return service.getGroupUsersCount(groupId,
							generateDaoProperties());
				}
			} catch (Exception e) {
				Log.getLog(UserMessagingTable.class).error(e.toString(), e);
			}
			return 0;
		}

		private DaoQuery generateDaoProperties() {
			DaoQuery properties = new DaoQuery();
			OperatorParenthesis op = new OperatorParenthesis(
					DaoOperator.OPERATOR_AND);
			op.addOperator(new OperatorLike("username",
					getFilterValue("query"), null));
			op.addOperator(new OperatorLike("firstName",
					getFilterValue("query"), DaoOperator.OPERATOR_OR));
			op.addOperator(new OperatorLike("lastName",
					getFilterValue("query"), DaoOperator.OPERATOR_OR));
			op.addOperator(new OperatorLike("email1", getFilterValue("query"),
					DaoOperator.OPERATOR_OR));
			op.addOperator(new OperatorLike("telOffice",
					getFilterValue("query"), DaoOperator.OPERATOR_OR));
			op.addOperator(new OperatorLike("telHome", getFilterValue("query"),
					DaoOperator.OPERATOR_OR));
			op.addOperator(new OperatorLike("telMobile",
					getFilterValue("query"), DaoOperator.OPERATOR_OR));
			properties.addProperty(op);
			properties.addProperty(new OperatorEquals("u.active", "1",
					DaoOperator.OPERATOR_AND));
			return properties;
		}

		public String getGroupFilter() {
			Collection group = (Collection) getFilterValue("group");
			if (group != null)
				if (!(group.isEmpty()))
					return (String) group.iterator().next();
			return "";
		}

		public String getTableRowKey() {
			return "id";
		}

		public Forward processAction(Event event, String action,
				String[] selectedKeys) {
			try {
				keys=null;
				values=null;
				if ("select".equals(action)) {
					Map selectedUsers = generateOptionMap(selectedKeys);
					for (int i = 0; i < selectedKeys.length; i++) {
						if (keys != null) {
							keys = keys + "|||" + selectedKeys[i];
							values = values
									+ "|||"
									+ (String) selectedUsers
											.get(selectedKeys[i]);
						} else {
							keys = selectedKeys[i];
							values = (String) selectedUsers
									.get(selectedKeys[i]);
						}
					}
					return new Forward("select_intranet");
				} else {
					return null;
				}
			} catch (Exception e) {
				Log.getLog(getClass()).error(
						"Error processing action " + action + ": "
								+ e.toString(), e);
				event.getRequest().getSession().setAttribute("error", e);
				return new Forward("error");
			}
		}
	}

	protected Map generateOptionMap(String[] ids) throws SecurityException {
		Map usersMap = new SequencedHashMap();
		if (ids == null || ids.length == 0) {
			return usersMap;
		}

		try {
			Application app = Application.getInstance();
			SecurityService security = (SecurityService) app
					.getService(SecurityService.class);
			DaoQuery query = new DaoQuery();
			query.addProperty(new OperatorIn("id", ids,
					DaoOperator.OPERATOR_AND));
			String sort = "username";
			Collection userList = security.getUsers(query, 0, -1, sort, false);

			// build users map
			Map tmpMap = new SequencedHashMap();
			for (Iterator i = userList.iterator(); i.hasNext();) {
				User user = (User) i.next();
				String label = user.getName();
				tmpMap.put(user.getId(), label);
			}

			// sort
			for (int j = 0; j < ids.length; j++) {
				String name = (String) tmpMap.get(ids[j]);
				if (name == null) {
					name = "---";
				} else if (name.trim().length() > 50) {
					name = name.substring(0, 50) + "..";
				}
				usersMap.put(ids[j], name);
			}
		} catch (SecurityException e) {
			Log.getLog(getClass()).error("Error retrieving users", e);
		}

		return usersMap;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

}

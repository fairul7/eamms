package com.tms.cms.digest.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

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
import kacang.services.security.ui.UsersSelectBox.UsersPopupTable;
import kacang.stdui.PopupSelectBox;
import kacang.stdui.PopupSelectBoxTable;
import kacang.stdui.PopupSelectBoxTableModel;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.ui.Event;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.cms.digest.model.DigestModule;
import com.tms.cms.digest.model.RecipientDataObject;

public class RecipientsSelectBox extends PopupSelectBox {

    private boolean showGroupName;

    public RecipientsSelectBox() {
        super();
    }

    public RecipientsSelectBox(String name) {
        super(name);
    }
    
    public boolean isShowGroupName() {
		return showGroupName;
	}

	public void setShowGroupName(boolean showGroupName) {
		this.showGroupName = showGroupName;
	}

	protected Table initPopupTable() {
        return new RecipientsPopupTable();
    }

    protected Map generateOptionMap(String[] ids) {
        Map usersMap = new SequencedHashMap();
        if (ids == null || ids.length == 0) {
            return usersMap;
        }

        try {
            Application app = Application.getInstance();
            DigestModule dm = (DigestModule)app.getModule(DigestModule.class);
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("recipientId", ids, DaoOperator.OPERATOR_AND));
            Collection recipientList = dm.getRecipients(query);

            // build users map
            Map tmpMap = new SequencedHashMap();
            for (Iterator i=recipientList.iterator(); i.hasNext();) {
            	RecipientDataObject recipients = (RecipientDataObject)i.next();
                String label = recipients.getRecipientName();
                tmpMap.put(recipients.getRecipientId(), label);
            }

            // sort
            for (int j=0; j<ids.length; j++) {
                String name = (String)tmpMap.get(ids[j]);
                if (name == null) {
                    name = "---";
                }
                else if (name.trim().length() > 50) {
                    name = name.substring(0, 50) + "..";
                }
                usersMap.put(ids[j], name);
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving recipients", e);
        }

        return usersMap;
    }

    public class RecipientsPopupTable extends PopupSelectBoxTable {

        public RecipientsPopupTable() {
        }

        public RecipientsPopupTable(String name) {
            super(name);
        }

        public void init() {
            super.init();
            setWidth("100%");
            setModel(new RecipientsPopupTableModel());
            getModel().setSort("recipientName");
        }

        public class RecipientsPopupTableModel extends PopupSelectBoxTableModel {
            public RecipientsPopupTableModel() {
                super();

                Application application = Application.getInstance();
                //Adding Actions
                addAction(new TableAction(FORWARD_SELECT,  application.getMessage("general.label.select", "Select")));

                TableColumn tcName = new TableColumn("recipientName", application.getMessage("digest.label.recipients"));
                tcName.setUrlParam("recipientId");
                addColumn(tcName);
                
                TableFilter tfSearchCriteria = new TableFilter("searchCriteria");
                addFilter(tfSearchCriteria); 
            }

            public Collection getTableRows() {
            	Log log = Log.getLog(this.getClass());
                try
                {
                    String searchCriteria = (String) getFilterValue("searchCriteria");
                    if(searchCriteria == null)
                        searchCriteria = "";
                   

                    DigestModule digestModule = (DigestModule) Application.getInstance().getModule(DigestModule.class);
                    return digestModule.getRecipients(searchCriteria, getSort(), isDesc(), getStart(), getRows());
                }
                catch (Exception e)
                {
                    // log error and return an empty collection
                    log.error(e);
                    return new ArrayList();
                }
            }

            public int getTotalRowCount() {
            	try
                {
                    String searchCriteria = (String) getFilterValue("searchCriteria");
                    if(searchCriteria == null)
                        searchCriteria = "";
                   

                    DigestModule digestModule = (DigestModule) Application.getInstance().getModule(DigestModule.class);
                    return digestModule.getNumOfRecipients(searchCriteria);
                }
                catch (Exception e)
                {
                    return 0;
                }
            }


            public String getTableRowKey() {
                return "recipientId";
            }

        }
    }

}

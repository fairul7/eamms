package com.tms.ekms.search.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.services.security.User;

import java.util.Collection;
import java.util.ArrayList;

import com.tms.ekms.search.model.SearchProfileModule;
import com.tms.ekms.search.model.SearchProfileException;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 25, 2005
 * Time: 3:07:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchProfileTable extends Table
{
    SearchProfileModel model;

    public SearchProfileTable() {
        super();
    }

    public SearchProfileTable(String name) {
        super(name);
    }

    public void init() {
        super.init();
        model = new SearchProfileModel();
        setModel(model);
        setWidth("100%");
    }

    public class SearchProfileModel extends TableModel
    {
        private Log log = Log.getLog(getClass());

        public SearchProfileModel() {
            super();
            Application app = Application.getInstance();
            TableColumn profileCol = new TableColumn("profilename", app.getMessage("searchprofile.label.name"));
            profileCol.setUrlParam("profileId");
            addColumn(profileCol);
            TableColumn lastRunCol = new TableColumn("lastRun", app.getMessage("searchprofile.label.status"));
            TableDateFormat tdf = new TableDateFormat(app.getProperty("globalDatetimeLong"));
            lastRunCol.setFormat(tdf);
            addColumn(lastRunCol);
            addColumn(new TableColumn("matchFound", app.getMessage("searchprofile.label.match")));
            addAction(new TableAction("delete", app.getMessage("searchprofile.label.delete"), app.getMessage("searchprofile.popup.delete")));
            addFilter(new TableFilter("profilename"));




        }

        public Collection getTableRows() {
            Collection profiles = new ArrayList();
            String name = (String) getFilterValue("profilename");
            SearchProfileModule module = (SearchProfileModule) Application.getInstance().getModule(SearchProfileModule.class);
            User user = getWidgetManager().getUser();
            try {
                if((name == null || "".equals(name))){
                    profiles = module.getUserSearchProfiles(user.getId(), getStart(), getRows(), getSort(), isDesc());
                }else{
                    profiles = module.getUserSearchProfiles(user.getId(), name, getStart(), getRows(), getSort(), isDesc());
                }
            } catch (SearchProfileException e) {
                Log.getLog(getClass()).error("Error while initializing SearchProfileModel", e);
            }
            return profiles;
        }

        public int getTotalRowCount() {
            int count = 0;
            String name = (String) getFilterValue("profilename");
            SearchProfileModule module = (SearchProfileModule) Application.getInstance().getModule(SearchProfileModule.class);
            User user = getWidgetManager().getUser();
            try {
                if((name == null || "".equals("profilename"))){
                    count = module.getUserSearchProfilesCount(user.getId());
                }else{
                    count = module.getUserSearchProfilesCount(user.getId(), name);
                }
            } catch (SearchProfileException e) {
                Log.getLog(getClass()).error("Error while initializing SearchProfileModel", e);
            }
            return count;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getTableRowKey()
        {
            return "profileId";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if("delete".equals(action)){
                Application app = Application.getInstance();
                SearchProfileModule module = (SearchProfileModule) app.getModule(SearchProfileModule.class);
                for(int i=0; i < selectedKeys.length; i++){
                    try {
                        module.deleteProfile(selectedKeys[i]);
                    } catch (SearchProfileException e) {
                        log.error("Error deleted search profiles", e);
                    }

                }
            }
            return null;
        }
    }
}

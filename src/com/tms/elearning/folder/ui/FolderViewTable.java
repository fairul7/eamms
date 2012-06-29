package com.tms.elearning.folder.ui;

import kacang.stdui.*;
import kacang.util.Log;
import kacang.Application;
import kacang.services.security.User;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.ui.WidgetManager;
import kacang.model.DaoException;
import com.tms.elearning.folder.model.FolderModuleDao;
import com.tms.elearning.folder.model.FolderModule;
import com.tms.elearning.folder.model.Folder;
import kacang.stdui.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Dec 3, 2004
 * Time: 7:02:39 PM
 * To change this template use File | Settings | File Templates.
 */

public class FolderViewTable extends Table implements Serializable  {



    public void init() {
        setModel(new FolderTableModel());
    }
    public class FolderTableModel extends TableModel {

        public FolderTableModel() {
            Application application = Application.getInstance();
            WidgetManager manager = getWidgetManager();
            User user = manager.getUser();
            String userId=user.getUsername();
            Log log = Log.getLog(getClass());

            TableColumn naCol = new TableColumn("name", application.getMessage("eLearning.module.label.name"));
            naCol.setUrlParam("id");
            addColumn(naCol);

            addColumn(new TableColumn("courseName", application.getMessage("eLearning.module.label.course")));
            addFilter(new TableFilter("name"));

            TableFilter courseFilter = new TableFilter("course");
            SelectBox courseSelect = new SelectBox("courseid");
            courseSelect.setOptions("-1="+application.getMessage("eLearning.topic.label.coursemenu"));

            FolderModuleDao folderDao = (FolderModuleDao)Application.getInstance().getModule(FolderModule.class).getDao();

            try {
                HashMap hash = new HashMap();

                Collection courses = folderDao.getCourses(userId);
                for(Iterator i= courses.iterator();i.hasNext();){
                    Folder temp =(Folder)i.next();
                    hash.put(temp.getCourseId(),temp.getCourseName());
                }
                courseSelect.setOptionMap(hash);
            } catch (DaoException e) {
                log.error(e.toString(),e);
            }
            courseFilter.setWidget(courseSelect);
            addFilter(courseFilter);

            addAction(new TableAction("quit", application.getMessage("eLearning.module.label.quit")));
           
        }

        public Collection getTableRows() {
            String name = (String)getFilterValue("name");
            List courseList = (List)getFilterValue("course");
            String course = "-1";
            WidgetManager manager = getWidgetManager();
            User user = manager.getUser();
            String userId=user.getUsername();

            if(courseList.size()>0) {
                course = (String)courseList.get(0);
            }

            Application application = Application.getInstance();
            FolderModule module = (FolderModule)application.getModule(FolderModule.class);
            return module.findFolders(name,course,userId, getSort(), isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            String name = (String)getFilterValue("name");
            List courseList = (List)getFilterValue("course");
            String course = "-1";

            if(courseList.size()>0) {
                course = (String)courseList.get(0);
            }
            Application application = Application.getInstance();
            FolderModule module = (FolderModule)application.getModule(FolderModule.class);
            return module.countFolders(name,course);
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {

            return null;
        }

    }

}

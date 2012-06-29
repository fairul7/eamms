package com.tms.elearning.core.ui;

import kacang.stdui.*;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.Application;
import kacang.model.DaoException;
import kacang.util.Log;
import kacang.ui.WidgetManager;
import kacang.ui.Forward;
import kacang.ui.Event;
import com.tms.elearning.folder.model.Folder;
import com.tms.elearning.folder.model.FolderModule;

import java.util.Collection;
/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 25, 2004
 * Time: 1:36:34 PM
 * To change this template use File | Settings | File Templates.
 */

public class CourseModulesTables extends Table  {

    String courseId;

    public void init() {
        setModel(new com.tms.elearning.core.ui.CourseModulesTables.FolderTableModel());
    }

    public void onRequest(Event evt){

        courseId = evt.getRequest().getParameter("cid").trim();
        init();
    }
    public class FolderTableModel extends TableModel {

        public FolderTableModel() {

            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            User current = getWidgetManager().getUser();
            WidgetManager manager = getWidgetManager();
            User user = manager.getUser();
            String userId=user.getUsername();
            Log log = Log.getLog(getClass());

            TableColumn naCol = new TableColumn("name", "Name");
            addColumn(naCol);

            try
            {
                if (service.hasPermission(current.getId(), "com.tms.elearning.folder.Edit", null, null))
                    naCol.setUrlParam("id");
            }
            catch (Exception e)
            {
                Log.getLog(Folder.class).error(e.getMessage(), e);
            }

            addColumn(new TableColumn("courseName", "Course"));
         //   addFilter(new TableFilter("name"));

           /* TableFilter courseFilter = new TableFilter("course");
            SelectBox courseSelect = new SelectBox("courseid");
            courseSelect.setOptions("-1=--- Course ---");*/

     /*       FolderModuleDao folderDao = (FolderModuleDao)Application.getInstance().getModule(FolderModule.class).getDao();

            try {
                Collection courses = folderDao.getCourses(userId);
                HashMap hash = new HashMap();
                for (Iterator iterator = courses.iterator(); iterator.hasNext();) {
                    Folder folder = (Folder) iterator.next();
                    //courseSelect.setOptions(folder.getCourseId()+"="+folder.getCourseName());
                    hash.put(folder.getCourseId(),folder.getCourseName());
                }
                courseSelect.setOptionMap(hash);
            } catch (DaoException e) {
                log.error(e.toString(),e);
            }
            courseFilter.setWidget(courseSelect);
            addFilter(courseFilter);

            try{
                if (service.hasPermission(current.getId(), "com.tms.elearning.folder.Add", null, null))
                    addAction(new TableAction("add", "Add"));

                if (service.hasPermission(current.getId(), "com.tms.elearning.folder.Delete", null, null))
                    addAction(new TableAction("delete", "Delete", "Confirm?"));

            } catch (Exception e) {
                Log.getLog(Folder.class).error("Error loading modules: " + e.toString(), e);
            }                       */
        }

        public Collection getTableRows() {

            String name = (String)getFilterValue("name");
       /*     List courseList = (List)getFilterValue("course");
            String course = "-1";*/
            WidgetManager manager = getWidgetManager();
            User user = manager.getUser();
            String userId=user.getUsername();

         /*   if(courseList.size()>0) {
                course = (String)courseList.get(0);
            }*/

            Application application = Application.getInstance();
            FolderModule module = (FolderModule)application.getModule(FolderModule.class);
            return module.findFolders(name,courseId,userId, getSort(), isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            String name = (String)getFilterValue("name");
         /*   List courseList = (List)getFilterValue("course");
            String course = "-1";

            if(courseList.size()>0) {
                course = (String)courseList.get(0);
            }*/
            Application application = Application.getInstance();
            FolderModule module = (FolderModule)application.getModule(FolderModule.class);
            return module.countFolders(courseId);
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
      /*       if ("add".equals(action)) {
                return new Forward("add");
            }
            else if ("delete".equals(action)) {
                Application application = Application.getInstance();
                FolderModule module = (FolderModule)application.getModule(FolderModule.class);
                for (int i=0; i<selectedKeys.length; i++) {
                    module.deleteFolder(selectedKeys[i]);
                }
            }*/
            return null;
        }

    }

}

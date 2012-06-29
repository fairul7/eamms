package com.tms.elearning.folder.ui;

import com.tms.elearning.folder.model.FolderModule;

import kacang.Application;

import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;

import java.io.Serializable;

import java.util.Collection;


/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 25, 2004
 * Time: 1:36:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class FolderTableStud extends Table implements Serializable {
    protected String cid;
    protected String introduction;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void init() {
        setModel(new FolderTableModel());
    }

    public class FolderTableModel extends TableModel {
        public FolderTableModel() {
            /*
             SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
             User current = getWidgetManager().getUser();
             WidgetManager manager = getWidgetManager();
             User user = manager.getUser();
             String userId=user.getUsername();
             Log log = Log.getLog(getClass());
              */
            Application application = Application.getInstance();
            TableColumn naColl = new TableColumn("name",
            application.getMessage("eLearning.module.label.module"));

            naColl.setUrlParam("id");
            addColumn(naColl);

            TableColumn naCol = new TableColumn("", "", false);
            naCol.setLabel(application.getMessage(
                    "eLearning.module.label.lessons"));
            naCol.setUrl("lessonsStud.jsp");
            naCol.setUrlParam("id");
            addColumn(naCol);

            //for assessment
            TableColumn naCol2 = new TableColumn("", "", false);
            naCol2.setLabel(application.getMessage(
                    "eLearning.module.label.assessments"));
            naCol2.setUrl("AssessmentByModule.jsp");
            naCol2.setUrlParam("id");
            addColumn(naCol2);




            /*
          try
          {
              if (service.hasPermission(current.getId(), "com.tms.elearning.folder.Edit", null, null))
                  naCol.setUrlParam("id");
          }
          catch (Exception e)
          {
              Log.getLog(Folder.class).error(e.getMessage(), e);
          }
            */
            /*
            addColumn(new TableColumn("courseName", "Course"));
            addFilter(new TableFilter("name"));
                   */
            /*
            TableFilter courseFilter = new TableFilter("course");
            SelectBox courseSelect = new SelectBox("courseid");
            courseSelect.setOptions("-1=--- Course ---");

            FolderModuleDao folderDao = (FolderModuleDao)Application.getInstance().getModule(FolderModule.class).getDao();

            try {
                Collection courses = folderDao.getCourses(userId);
                Map hash = new HashMap();
                hash.put(new String("-1"),new String("--- Course ---"));
                for (Iterator iterator = courses.iterator(); iterator.hasNext();) {
                    Folder folder = (Folder) iterator.next();
                    hash.put(folder.getCourseId(),folder.getCourseName());
                }
                TreeMap tree = new TreeMap(hash);
                courseSelect.setOptionMap(tree);
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
            }

                */

            //-enhancment on elearning start

            /*   TableFilter courseFilter = new TableFilter("course");
            SelectBox courseSelect = new SelectBox("courseid");
            courseSelect.setOptions("-1=--- Course ---");

            FolderModuleDao folderDao = (FolderModuleDao)Application.getInstance().getModule(FolderModule.class).getDao();

            try {
                Collection courses = folderDao.getCourses("%");
                Map hash = new HashMap();
                hash.put(new String("-1"),new String("--- Course ---"));
                for (Iterator iterator = courses.iterator(); iterator.hasNext();) {
                    Folder folder = (Folder) iterator.next();
                    hash.put(folder.getCourseId(),folder.getCourseName());
                }
                TreeMap tree = new TreeMap(hash);
                courseSelect.setOptionMap(tree);
            } catch (DaoException e) {

            }
            courseFilter.setWidget(courseSelect);
            addFilter(courseFilter);*/

            //-enhancment on elearning end
        }

        public Collection getTableRows() {
            /*
            String name = (String)getFilterValue("name");
            List courseList = (List)getFilterValue("course");
            String course = "-1";
            WidgetManager manager = getWidgetManager();
            User user = manager.getUser();
            String userId=user.getUsername();

            if(courseList.size()>0) {
              course = (String)courseList.get(0);
            }
              */
            Application application = Application.getInstance();
            FolderModule module = (FolderModule) application.getModule(FolderModule.class);

            //return module.findFolders(name,course,userId, getSort(), isDesc(), getStart(), getRows());
            return module.findFolders(getCid(), isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            /*
              String name = (String)getFilterValue("name");
              List courseList = (List)getFilterValue("course");
              String course = "-1";

              if(courseList.size()>0) {
                  course = (String)courseList.get(0);
              }
              */
            Application application = Application.getInstance();
            FolderModule module = (FolderModule) application.getModule(FolderModule.class);

            //  return module.countFolders(name,course);
            return module.countFoldersByCourse(getCid());
        }

        public String getTableRowKey() {
            return "id";
        }

        /*

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
             if ("add".equals(action)) {
                return new Forward("add");
            }
            else if ("delete".equals(action)) {
                Application application = Application.getInstance();
                FolderModule module = (FolderModule)application.getModule(FolderModule.class);
                for (int i=0; i<selectedKeys.length; i++) {
                    module.deleteFolder(selectedKeys[i]);
                }
            }
            return null;
        }

        */
    }
}

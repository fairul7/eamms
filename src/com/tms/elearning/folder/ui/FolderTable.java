package com.tms.elearning.folder.ui;

import com.tms.elearning.course.model.Course;
import com.tms.elearning.folder.model.Folder;
import com.tms.elearning.folder.model.FolderModule;
import com.tms.elearning.folder.model.FolderModuleDao;

import kacang.Application;

import kacang.model.DaoException;

import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.stdui.*;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import java.io.Serializable;

import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 25, 2004
 * Time: 1:36:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class FolderTable extends Table implements Serializable {
    public void init() {
        setModel(new FolderTableModel());
    }

    public class FolderTableModel extends TableModel {

        public FolderTableModel() {
            Application application = Application.getInstance();
            SecurityService service = (SecurityService) Application.getInstance()
                                                                   .getService(SecurityService.class);

            FolderModule module = (FolderModule) application.getModule(FolderModule.class);
            String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            User current = getWidgetManager().getUser();
            WidgetManager manager = getWidgetManager();
            User user = manager.getUser();
            String userId = user.getUsername();
            Log log = Log.getLog(getClass());

            TableColumn naCol = new TableColumn("name", "Name");
            addColumn(naCol);

            try {
                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.folder.Edit", null, null)) {
                    naCol.setUrl("editFolder.jsp");
                }

                naCol.setUrlParam("id");
            } catch (Exception e) {
                Log.getLog(Folder.class).error(e.getMessage(), e);
            }

            addColumn(new TableColumn("courseName", "Course"));
            addFilter(new TableFilter("name"));

            TableColumn activate = new TableColumn("is_public",
                    application.getMessage("eLearning.element.active", "Active"));
            Map map = new HashMap();
            map.put("1",
                "<img src=\"" + contextPath +
                "/common/table/booleantrue.gif\">");
            map.put("0", "");

            TableFormat stringFormat = new TableStringFormat(map);
            activate.setFormat(stringFormat);
            addColumn(activate);

            FolderModuleDao folderDao = (FolderModuleDao) Application.getInstance()
                                                                     .getModule(FolderModule.class)
                                                                     .getDao();

            try {
                Collection courses = folderDao.getCourses(userId);

                Map hash = new SequencedHashMap();
                hash.put(new String("-1"),
                    new String(application.getMessage(
                            "eLearning.topic.label.coursemenu")));

                for (Iterator iterator = courses.iterator();
                        iterator.hasNext();) {
                    Folder folder = (Folder) iterator.next();
                    hash.put(folder.getCourseId(), folder.getCourseName());
                }
            } catch (DaoException e) {
                log.error(e.toString(), e);
            }

            try {
                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.folder.Add", null, null)) {
                    addAction(new TableAction("activate",
                            application.getMessage("eLearning.button.activate")));
                    addAction(new TableAction("deactivate",
                            application.getMessage(
                                "eLearning.button.deactivate")));
                    addAction(new TableAction("add",
                            application.getMessage("eLearning.module.label.add")));
                }

                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.folder.Delete", null, null)) {
                    addAction(new TableAction("delete",
                            application.getMessage(
                                "eLearning.module.label.delete"),
                            application.getMessage(
                                "eLearning.module.label.confirm")));
                }
            } catch (Exception e) {
                Log.getLog(Folder.class).error(application.getMessage(
                        "eLearning.module.label.errorloadingmodules") + " " +
                    e.toString(), e);
            }

            TableFilter tfRead = new TableFilter("course");
            SelectBox selectRead = new SelectBox("select_course");

            Collection loadCourse = module.loadCourse();

            selectRead.addOption("",
                application.getMessage("eLearning.filter.label.course"));

            for (Iterator icount = loadCourse.iterator(); icount.hasNext();) {
                Course course = (Course) icount.next();

                selectRead.addOption(course.getId(), course.getName());
            }

            selectRead.setMultiple(false);
            tfRead.setWidget(selectRead);
            addFilter(tfRead);
        }

        public Collection getTableRows() {
            String name = (String) getFilterValue("name");

            List readStatus = (List) getFilterValue("course");
            String course = "";

            if (readStatus.size() > 0) {
                course = (String) readStatus.get(0);
            }

            if ((course != null) &&
                    (course.trim().equalsIgnoreCase("null") ||
                    course.trim().equals(""))) {
                course = "";
            }

            // String course = "-1";
            WidgetManager manager = getWidgetManager();
            User user = manager.getUser();
            String userId = user.getUsername();

            Application application = Application.getInstance();
            FolderModule module = (FolderModule) application.getModule(FolderModule.class);

            return module.findFolders(name, course, userId, getSort(),
                isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            String name = (String) getFilterValue("name");
            List readStatus = (List) getFilterValue("course");
            String course = "";

            if (readStatus.size() > 0) {
                course = (String) readStatus.get(0);
            }

            if ((course != null) &&
                    (course.trim().equalsIgnoreCase("null") ||
                    course.trim().equals(""))) {
                course = "";
            }

            //String course = "-1";
            Application application = Application.getInstance();
            FolderModule module = (FolderModule) application.getModule(FolderModule.class);

            return module.countFolders(name, course);
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event evt, String action,
            String[] selectedKeys) {
            Application application = Application.getInstance();
            FolderModule module = (FolderModule) application.getModule(FolderModule.class);



            if ("add".equals(action)) {
                return new Forward("add");
            }

             if (selectedKeys.length <= 0) {
                return new Forward("notSelected");
            }
             if ("activate".equals(action)) {
                for (int i = 0; i < selectedKeys.length; i++) {
                    module.setActivationModule(selectedKeys[i]);
                }
                return new Forward("activate");
            } else if ("deactivate".equals(action)) {
                for (int i = 0; i < selectedKeys.length; i++) {
                    module.setDeactivationModule(selectedKeys[i]);
                }
                return new Forward("deactivate");
            }
            else if ("delete".equals(action)) {
                for (int i = 0; i < selectedKeys.length; i++) {
                    module.deleteFolder(selectedKeys[i]);
                }

                return new Forward("deleted");
            }

            return null;
        }
    }
}

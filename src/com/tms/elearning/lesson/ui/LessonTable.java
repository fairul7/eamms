package com.tms.elearning.lesson.ui;

import com.tms.elearning.lesson.model.Lesson;
import com.tms.elearning.lesson.model.LessonModule;
import com.tms.elearning.course.model.Course;
import com.tms.elearning.folder.model.FolderModule;
import com.tms.elearning.folder.model.Folder;

import kacang.Application;

import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.stdui.*;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import kacang.util.Log;

import java.util.*;
import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 26, 2004
 * Time: 4:55:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class LessonTable extends Table implements Serializable{
    private SelectBox folderSelect;


    public void init() {
        setModel(new com.tms.elearning.lesson.ui.LessonTable.LessonTableModel());
    }

    public class LessonTableModel extends TableModel {
        public LessonTableModel() {

            String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            Application application = Application.getInstance();
            SecurityService service = (SecurityService) Application.getInstance()
                                                                   .getService(SecurityService.class);
            User current = getWidgetManager().getUser();
            WidgetManager manager = getWidgetManager();
            User user = manager.getUser();
            String userId = user.getUsername();
            Log log = Log.getLog(getClass());

            TableColumn naCol = new TableColumn("name",
                    application.getMessage("eLearning.lesson.label.lessonname"));

            addColumn(naCol);

            try {
                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.lesson.Edit", null, null)) {
                    naCol.setUrl("editLesson.jsp");
                }

                naCol.setUrlParam("id");
            } catch (Exception e) {
                Log.getLog(Lesson.class).error(e.getMessage(), e);
            }

            addColumn(new TableColumn("courseName",
                    application.getMessage("eLearning.lesson.label.course")));
            addColumn(new TableColumn("folderName",
                    application.getMessage("eLearning.lesson.label.module")));
            addFilter(new TableFilter("name"));



             TableColumn activate = new TableColumn("is_public", application.getMessage("eLearning.element.active", "Active"));
	            Map map = new HashMap();
	            map.put("1", "<img src=\"" + contextPath + "/common/table/booleantrue.gif\">");
	            map.put("0", "");
	            TableFormat stringFormat = new TableStringFormat(map);
	            activate.setFormat(stringFormat);
	            addColumn(activate);



            try {
                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.lesson.Add", null, null)) {
                    addAction(new TableAction("activate", application.getMessage("eLearning.button.activate")));
                    addAction(new TableAction("deactivate", application.getMessage("eLearning.button.deactivate")));
                    addAction(new TableAction("add",
                            application.getMessage("eLearning.module.label.add")));
                }

                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.lesson.Delete", null, null)) {
                    addAction(new TableAction("delete",
                            application.getMessage(
                                "eLearning.lesson.label.delete"),
                            application.getMessage(
                                "eLearning.lesson.label.confirm")));
                }
            } catch (Exception e) {
                Log.getLog(Lesson.class).error(application.getMessage(
                        "eLearning.lesson.label.errorloadlessons") + " " +
                    e.toString(), e);
            }





           /* TableFilter tfRead = new TableFilter("course");
            SelectBox selectRead = new SelectBox("select_course");


                 FolderModule folderModule = (FolderModule) application.getModule(FolderModule.class);

                Collection loadCourse= folderModule.loadCourse();

                selectRead.addOption("", application.getMessage("eLearning.filter.label.course"));

                          for (Iterator icount = loadCourse.iterator(); icount.hasNext();) {
                              Course course = (Course) icount.next();


                              selectRead.addOption(course.getId(), course.getName());
                          }


                selectRead.setMultiple(false);
                tfRead.setWidget(selectRead);
                addFilter(tfRead);
*/

                   TableFilter tfRead2 = new TableFilter("module");
            SelectBox selectRead2 = new SelectBox("select_module");


                 LessonModule lessonModule = (LessonModule) application.getModule(LessonModule.class);

                Collection loadModule = lessonModule.loadModule();

                selectRead2.addOption("", application.getMessage("eLearning.filter.label.module"));

                          for (Iterator icount = loadModule.iterator(); icount.hasNext();) {
                              Folder folder = (Folder) icount.next();


                              selectRead2.addOption(folder.getId(), folder.getName());
                          }


                selectRead2.setMultiple(false);
                tfRead2.setWidget(selectRead2);
                addFilter(tfRead2);




        }

        public Collection getTableRows() {
            String name = (String) getFilterValue("name");
            Application application = Application.getInstance();
            LessonModule module = (LessonModule) application.getModule(LessonModule.class);


/*

            List readStatus = (List) getFilterValue("course");
                           String course = "";
                           if (readStatus.size() > 0)
                               course = (String) readStatus.get(0);
                           if (course != null && (course.trim().equalsIgnoreCase("null") || course.trim().equals("")))
                               course = "";
*/


            List readStatus2 = (List) getFilterValue("module");
                           String folder = "";
                           if (readStatus2.size() > 0)
                               folder = (String) readStatus2.get(0);
                           if (folder != null && (folder.trim().equalsIgnoreCase("null") || folder.trim().equals("")))
                               folder = "";








            WidgetManager manager = getWidgetManager();
            User user = manager.getUser();
            String userId = user.getUsername();

            return module.findLessons(name, "", folder, userId, getSort(),
                isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            String name = (String) getFilterValue("name");



                   /*     List readStatus = (List) getFilterValue("course");
                           String course = "";
                           if (readStatus.size() > 0)
                               course = (String) readStatus.get(0);
                           if (course != null && (course.trim().equalsIgnoreCase("null") || course.trim().equals("")))
                               course = "";*/


            List readStatus2 = (List) getFilterValue("module");
                           String folder = "";
                           if (readStatus2.size() > 0)
                               folder = (String) readStatus2.get(0);
                           if (folder != null && (folder.trim().equalsIgnoreCase("null") || folder.trim().equals("")))
                               folder = "";





            Application application = Application.getInstance();
            LessonModule module = (LessonModule) application.getModule(LessonModule.class);

            return module.countLessons(name, "", folder);
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event evt, String action,
            String[] selectedKeys) {

             Application application = Application.getInstance();
                LessonModule module = (LessonModule) application.getModule(LessonModule.class);



            if ("add".equals(action)) {
                return new Forward("add");
            }

             if(selectedKeys.length <= 0) return new Forward("notSelected");
            if("activate".equals(action)){

                 for (int i = 0; i < selectedKeys.length; i++) {
                    module.setActivationLesson(selectedKeys[i]);
                }
                return new Forward("activate");
            }
            else if("deactivate".equals(action)){

                 for (int i = 0; i < selectedKeys.length; i++) {
                    module.setDeactivationLesson(selectedKeys[i]);
                }
                return new Forward("deactivate");
            }

            else if ("delete".equals(action)) {




                for (int i = 0; i < selectedKeys.length; i++) {
                    module.deleteLesson(selectedKeys[i]);
                }

                return new Forward("deleted");
            }

            return null;
        }
    }
}

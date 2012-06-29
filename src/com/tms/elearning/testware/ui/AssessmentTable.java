package com.tms.elearning.testware.ui;

import kacang.Application;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.model.DaoException;
import kacang.stdui.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.ui.WidgetManager;
import kacang.util.Log;
import java.util.*;
import java.io.Serializable;

import com.tms.elearning.testware.model.*;
import com.tms.elearning.course.model.*;
import com.tms.elearning.folder.model.FolderModule;
import com.tms.elearning.folder.model.Folder;
import com.tms.elearning.lesson.model.LessonModule;

/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Nov 23, 2004
 * Time: 3:25:14 PM
 * To change this template use Options | File Templates.
 */
public class AssessmentTable extends Table implements Serializable{



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected String id ;




    public void init() {
        setModel(new AssessmentTableModel());
        setPageSize(10);
    }

    public class AssessmentTableModel extends TableModel {
        public AssessmentTableModel() {
            Application application = Application.getInstance();
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);

            String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            User current = getWidgetManager().getUser();
            WidgetManager manager = getWidgetManager();
            User user = manager.getUser();
            String userId=user.getUsername();
            Log log = Log.getLog(getClass());

            addFilter(new TableFilter("key"));

            try{
                if (service.hasPermission(current.getId(), "com.tms.elearning.testware.Add", null, null))
                {
                    addAction(new TableAction("activate", application.getMessage("eLearning.button.activate")));
                    addAction(new TableAction("deactivate",application.getMessage("eLearning.button.deactivate")));
                    addAction(new TableAction("add", application.getMessage("eLearning.assessment.add")));
                }
                if (service.hasPermission(current.getId(), "com.tms.elearning.testware.Delete", null, null))
                    addAction(new TableAction("delete",application.getMessage("eLearning.question.delete"),application.getMessage("eLearning.assessment.deleteassessment")));

            } catch (Exception e) {
                Log.getLog(Assessment.class).error("Error loading assessments: " + e.toString(), e);
            }

            TableColumn assCol = new TableColumn("name",application.getMessage("eLearning.assessment.label"));
            addColumn(assCol);


           try
            {
                if (service.hasPermission(current.getId(), "com.tms.elearning.testware.Edit", null, null))
                {

                    assCol.setUrl("assessmentEdit.jsp");
                    assCol.setUrlParam("id");

                }
            }
            catch (Exception e)
            {
                Log.getLog(Assessment.class).error(e.getMessage(), e);
            }


             addColumn(new TableColumn("courseName",
                    application.getMessage("eLearning.lesson.label.course")));
             addColumn(new TableColumn("folderName",
                    application.getMessage("eLearning.lesson.label.module")));



            TableColumn activate = new TableColumn("is_public", application.getMessage("eLearning.element.active", "Active"));
	            Map map = new HashMap();
	            map.put("1", "<img src=\"" + contextPath + "/common/table/booleantrue.gif\">");
	            map.put("0", "");
	            TableFormat stringFormat = new TableStringFormat(map);
	            activate.setFormat(stringFormat);
	            addColumn(activate);


/*
            TableFilter tfRead = new TableFilter("course");
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
                     addFilter(tfRead);*/


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
            String keyword = (String)getFilterValue("key");
       /*     List courseList = (List)getFilterValue("course");
            String course = "-1";*/


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









            Application application = Application.getInstance();
            AssessmentModule module = (AssessmentModule)application.getModule(AssessmentModule.class);
            return module.getAssessments(keyword,"", folder, getSort(),isDesc(),getStart(),getRows());
        }

        public int getTotalRowCount() {
            String keyword = (String)getFilterValue("key");


          /*  List readStatus = (List) getFilterValue("course");
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








            Application application = Application.getInstance();
            AssessmentModule module = (AssessmentModule)application.getModule(AssessmentModule.class);
            return module.countAssessments(keyword,"", folder);
        }

       public Forward processAction(Event evt, String action, String[] selectedKeys) {
           Application application = Application.getInstance();
                           AssessmentModule module = (AssessmentModule)application.getModule(AssessmentModule.class);



           if ("add".equals(action)) {
                return new Forward("add");
            }

           if(selectedKeys.length <=0)return new Forward("notSelected");


            if("activate".equals(action)){

                for (int i=0; i<selectedKeys.length; i++) {
                    module.setActivationAssessment(selectedKeys[i]);
                }
                return new Forward("activate");
           }

           else if("deactivate".equals(action)){
                  for (int i=0; i<selectedKeys.length; i++) {
                    module.setDeactivationAssessment(selectedKeys[i]);
                }
                return new Forward("deactivate");
           }
            else if ("delete".equals(action)) {



                for (int i=0; i<selectedKeys.length; i++) {
                    module.deleteAssessment(selectedKeys[i]);
                }
                return new Forward("deleted");
            }
           return null;
        }

        public String getTableRowKey() {
            return "id";
        }

    }
}

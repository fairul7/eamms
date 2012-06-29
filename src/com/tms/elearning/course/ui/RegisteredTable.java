package com.tms.elearning.course.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.services.security.User;
import kacang.ui.Forward;
import kacang.ui.Event;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.tms.elearning.course.model.CourseModule;
import com.tms.elearning.testware.model.QuestionModule;
import com.tms.elearning.coursecategory.model.Category;
import com.tms.util.FormatUtil;

/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Nov 12, 2004
 * Time: 3:59:35 PM
 * To change this template use File | Settings | File Templates.
 */

public class RegisteredTable extends Table  {

    private String courseid;


    public void init() {
        setModel(new SubjectTableModel());
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public class SubjectTableModel extends TableModel {

        public SubjectTableModel() {

            Application application = Application.getInstance();

            TableColumn naCol = new TableColumn("name", application.getMessage("eLearning.course.label.registeredcourse"));

                    
                        naCol.setUrlParam("id");

            addColumn(naCol);

            addColumn(new TableColumn("categoryName", application.getMessage("eLearning.course.label.category")));
                  
            TableColumn naCol2 = new TableColumn("createdDate", application.getMessage("eLearning.course.label.datecreated"));
            naCol2.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));

            addColumn(naCol2);


                    TableColumn viewDetail= new TableColumn("", application.getMessage("eLearning.viewDetail.Report"),false);
                                 viewDetail.setLabel(application.getMessage("eLearning.viewDetail.view"));
                                 viewDetail.setUrl("studentDetail.jsp");
                                 viewDetail.setUrlParam("id"); //call same page then only query for courseId
                                 addColumn(viewDetail);


             addAction(new TableAction("delete", application.getMessage("eLearning.course.label.withdraw"),
                            application.getMessage("eLearning.course.label.deleteselectedcourse")));



            addFilter(new TableFilter("course"));






        TableFilter tfRead = new TableFilter("category");
            SelectBox selectRead = new SelectBox("select_category");

             CourseModule module = (CourseModule) application.getModule(CourseModule.class);



            Collection loadCategory = module.loadCategory();

            selectRead.addOption("", application.getMessage("eLearning.filter.label.category"));

                      for (Iterator icount = loadCategory.iterator(); icount.hasNext();) {
                          Category category = (Category) icount.next();


                          selectRead.addOption(category.getId(), category.getCategory());
                      }


            selectRead.setMultiple(false);
            tfRead.setWidget(selectRead);
            addFilter(tfRead);






        }

        public Collection getTableRows() {
            String course = (String)getFilterValue("course");


            List readStatus = (List) getFilterValue("category");
                           String category = "";
                           if (readStatus.size() > 0)
                               category = (String) readStatus.get(0);
                           if (category != null && (category.trim().equalsIgnoreCase("null") || category.trim().equals("")))
                               category = "";

            Application application = Application.getInstance();
            CourseModule module = (CourseModule)application.getModule(CourseModule.class);
            User user = application.getCurrentUser();


              return module.findRegistered(course, category, isDesc(), getStart(), getRows(), user.getId());




        }

        public int getTotalRowCount() {

            String course = (String)getFilterValue("course");



            List readStatus = (List) getFilterValue("category");
                           String category = "";
                           if (readStatus.size() > 0)
                               category = (String) readStatus.get(0);
                           if (category != null && (category.trim().equalsIgnoreCase("null") || category.trim().equals("")))
                               category = "";




            Application application = Application.getInstance();
            CourseModule module = (CourseModule)application.getModule(CourseModule.class);
                User user = application.getCurrentUser();

              return module.countRegisteredStudent(course, category, user.getId());



        }

        public String getTableRowKey() {
            return "id";
        }
        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if ("add".equals(action)) {
                return new Forward("add");
            }

            else if ("delete".equals(action)) {
                Application application = Application.getInstance();
                CourseModule module = (CourseModule)application.getModule(CourseModule.class);

                for (int i = 0; i < selectedKeys.length; i++) {
                    module.deleteRegisteredCourse(selectedKeys[i]);
                }

                return new Forward("deleted");
            }


            return null;
        }
   }
}

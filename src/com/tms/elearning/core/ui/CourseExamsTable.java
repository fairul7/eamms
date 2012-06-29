package com.tms.elearning.core.ui;

import com.tms.elearning.testware.model.Assessment;
import com.tms.elearning.testware.model.AssessmentModule;

import kacang.Application;

import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import kacang.util.Log;

import java.util.Collection;


/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: Mar 8, 2005
 * Time: 10:07:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class CourseExamsTable extends Table {
    private String courseid;

    public void init() {
        setModel(new CourseExamsTable.AssessmentViewTableModel());
        setPageSize(10);
    }

    public void onRequest(Event evt) {
        //super.onRequest(evt);
        courseid = evt.getRequest().getParameter("cid").trim();
        init();
    }

    public String getCourseid() {
        return this.courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public class AssessmentViewTableModel extends TableModel {
        public AssessmentViewTableModel() {
            SecurityService service = (SecurityService) Application.getInstance()
                                                                   .getService(SecurityService.class);
            User current = getWidgetManager().getUser();
            WidgetManager manager = getWidgetManager();
            User user = manager.getUser();
            String userId = user.getUsername();
            Log log = Log.getLog(getClass());

            addFilter(new TableFilter("key"));

            TableColumn assCol = new TableColumn("name", "Assessment");
            addColumn(assCol);

            TableColumn qCol = new TableColumn("", "Questions", false);
            qCol.setLabel("questions");

            //    qCol.setUrl("asmtQuestion.jsp");
            addColumn(qCol);

            /*  TableColumn qbankCol = new TableColumn("","Question Bank",false);
              qbankCol.setLabel("question bank");
              qbankCol.setUrl("question.jsp");
              addColumn(qbankCol);*/
            try {
                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.testware.Edit", null, null)) {
                    assCol.setUrlParam("id");

                    //  qCol.setUrlParam("examid");
                    // qbankCol.setUrlParam("examid");
                }
            } catch (Exception e) {
                Log.getLog(Assessment.class).error(e.getMessage(), e);
            }

            /*      TableFilter courseFilter = new TableFilter("course");
                  SelectBox courseSelect = new SelectBox("courseid");
                  courseSelect.setOptions("-1=--- Course ---");

                  TopicModuleDao tpcDao = (TopicModuleDao)Application.getInstance().getModule(TopicModule.class).getDao();

                  try {
                      Collection courses = tpcDao.getCourses(userId);
                      HashMap hash = new HashMap();
                      for(Iterator i= courses.iterator();i.hasNext();){
                          Topic temp =(Topic)i.next();
                          hash.put(temp.getCourse_id(),temp.getCourse_name());
                      }
                      courseSelect.setOptionMap(hash);
                  } catch (DaoException e) {
                      log.error(e.toString(),e);
                  }
                  courseFilter.setWidget(courseSelect);
                  addFilter(courseFilter);                     */
        }

        public Collection getTableRows() {
            String keyword = (String) getFilterValue("key");

            /*   List courseList = (List)getFilterValue("course");
               String course = "-1";

               if(courseList.size()>0) {
                   course = (String)courseList.get(0);
               }*/
            Application application = Application.getInstance();
            AssessmentModule module = (AssessmentModule) application.getModule(AssessmentModule.class);

            return module.getCourseAssessments(keyword, courseid, getSort(),
                isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            String keyword = (String) getFilterValue("key");

            /*     List courseList = (List)getFilterValue("course");
                 String course = "-1";

                 if(courseList.size()>0) {
                     course = (String)courseList.get(0);
                 }*/
            Application application = Application.getInstance();
            AssessmentModule module = (AssessmentModule) application.getModule(AssessmentModule.class);

            return module.countCourseAssessments(keyword, courseid);
        }

        public Forward processAction(Event evt, String action,
            String[] selectedKeys) {
            /*
            if ("add".equals(action)) {
                 return new Forward("add");
             }
             else if ("delete".equals(action)) {
                 Application application = Application.getInstance();
                 AssessmentModule module = (AssessmentModule)application.getModule(AssessmentModule.class);
                 for (int i=0; i<selectedKeys.length; i++) {
                     module.deleteAssessment(selectedKeys[i]);
                 }
             }
             */
            return null;
        }

        public String getTableRowKey() {
            return "id";
        }
    }
}

/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 18, 2004
 * Time: 6:57:04 PM
 * To change this template use File | Settings | File Templates.
 */
package com.tms.elearning.course.ui;

import com.tms.elearning.course.model.Course;
import com.tms.elearning.course.model.CourseModule;
import com.tms.elearning.coursecategory.model.Category;

import kacang.Application;

import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.stdui.*;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import kacang.util.Log;

import java.util.*;


public class CourseTable extends Table {
    public void init() {
        setModel(new CourseTableModel());
    }

    public class CourseTableModel extends TableModel {
        public CourseTableModel() {
            String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            Application application = Application.getInstance();
            SecurityService service = (SecurityService) Application.getInstance()
                                                                   .getService(SecurityService.class);
            User current = getWidgetManager().getUser();
            TableColumn naCol = new TableColumn("name",
                    application.getMessage("eLearning.course.label.name"));
            addColumn(naCol);

            try {
                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.course.Edit", null, null)) {
                    naCol.setUrl("editCourse.jsp");
                }

                naCol.setUrlParam("id");
            } catch (Exception e) {
                Log.getLog(Course.class).error(e.getMessage(), e);
            }

            addColumn(new TableColumn("author",
                    application.getMessage("eLearning.course.label.author")));
            addColumn(new TableColumn("instructor",
                    application.getMessage("eLearning.course.label.instructor")));
            addColumn(new TableColumn("category",
                    application.getMessage("eLearning.course.label.category")));

            //add tick icon
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

            TableColumn viewDetail = new TableColumn("",
                    application.getMessage("eLearning.viewDetail.Report"), false);
            viewDetail.setLabel(application.getMessage(
                    "eLearning.viewDetail.view"));
           // viewDetail.setUrl("courses.jsp");
            viewDetail.setUrlParam("id");
            addColumn(viewDetail);

            addFilter(new TableFilter("name"));

            TableFilter tfRead = new TableFilter("category");
            SelectBox selectRead = new SelectBox("select_category");

            CourseModule module = (CourseModule) application.getModule(CourseModule.class);

            Collection loadCategory = module.loadCategory();

            selectRead.addOption("",
                application.getMessage("eLearning.filter.label.category"));

            for (Iterator icount = loadCategory.iterator(); icount.hasNext();) {
                Category category = (Category) icount.next();

                selectRead.addOption(category.getId(), category.getCategory());
            }

            selectRead.setMultiple(false);
            tfRead.setWidget(selectRead);
            addFilter(tfRead);

            try {
                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.course.Add", null, null)) {
                    addAction(new TableAction("activate",
                            application.getMessage("eLearning.button.activate")));
                    addAction(new TableAction("deactivate",
                            application.getMessage(
                                "eLearning.button.deactivate")));
                    addAction(new TableAction("add",
                            application.getMessage("eLearning.button.add")));
                }

                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.course.Delete", null, null)) {
                    addAction(new TableAction("delete", "Delete",
                            application.getMessage(
                                "eLearning.course.label.confirm")));
                }
            } catch (Exception e) {
                Log.getLog(Course.class).error(application.getMessage(
                        "eLearning.course.label.errorloadcourse") + " " +
                    e.toString(), e);
            }
        }

        public Collection getTableRows() {
            String name = (String) getFilterValue("name");

            List readStatus = (List) getFilterValue("category");
            String category = "";

            if (readStatus.size() > 0) {
                category = (String) readStatus.get(0);
            }

            if ((category != null) &&
                    (category.trim().equalsIgnoreCase("null") ||
                    category.trim().equals(""))) {
                category = "";
            }

            WidgetManager manager = getWidgetManager();
            User user = manager.getUser();
            String userId = user.getUsername();

            Application application = Application.getInstance();
            CourseModule module = (CourseModule) application.getModule(CourseModule.class);

            return module.findCourses(name, category, userId, getSort(),
                isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            String name = (String) getFilterValue("name");

            List readStatus = (List) getFilterValue("category");
            String category = "";

            if (readStatus.size() > 0) {
                category = (String) readStatus.get(0);
            }

            if ((category != null) &&
                    (category.trim().equalsIgnoreCase("null") ||
                    category.trim().equals(""))) {
                category = "";
            }

            Application application = Application.getInstance();
            CourseModule module = (CourseModule) application.getModule(CourseModule.class);

            return module.countCourses(name, category);
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event evt, String action,
            String[] selectedKeys) {
            Application application = Application.getInstance();
            CourseModule module = (CourseModule) application.getModule(CourseModule.class);

            if ("add".equals(action)) {
                return new Forward("add");
            }

            if (selectedKeys.length <= 0) {
                return new Forward("notSelected");
            }

            if ("activate".equals(action)) {
                for (int i = 0; i < selectedKeys.length; i++) {
                    module.setActivateCourse(selectedKeys[i]);
                }

                return new Forward("activate");
            }
            else if ("deactivate".equals(action)) {
                for (int i = 0; i < selectedKeys.length; i++) {
                    module.setDeactivateCourse(selectedKeys[i]);
                }

                return new Forward("deactivate");
            }
            else if ("delete".equals(action)) {
                for (int i = 0; i < selectedKeys.length; i++) {
                    module.deleteCourse(selectedKeys[i]);
                }

                return new Forward("deleted");
            }

            return null;
        }
    }
}


